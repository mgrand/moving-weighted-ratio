package com.markgrand.healthcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Provide a simple HTTP health check that responds
 * <pre>
 * HTTP/1.1 200 OK
 * Content-Type: text/plain
 * Content-Length: 21
 *
 * Health check is OK!
 * </pre>
 * when it is in a good state. For it to stay in a good state, the heartbeat method must be called periodically.  If too
 * much time elapses between calls to heartbeat, the response changes to:
 * <pre>
 * HTTP/1.1 503 Service Unavailable
 * Content-Type: text/plain
 * Content-Length: 21
 *
 * Health check is Bad
 * </pre>
 * The maximum number of milliseconds that may elapse before the health check starts returning the bad response is the
 * number of milliseconds that is set by {@link #setInternalHeartbeatMillis(int)} and returned by {@link
 * #getInternalHeartbeatMillis()}.
 *
 * @author mxg88rm
 */
public class HealthCheck {
    private static final Logger log = LoggerFactory.getLogger(HealthCheck.class);

    private static final String OK_MESSAGE = "Health check is OK!\r\n";
    private static final String ENVELOPE_TERMINATOR = "\r\n\r\n";
    private static final String OK_HTTP_PREFIX = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + OK_MESSAGE.length() + ENVELOPE_TERMINATOR;
    public static final String OK_RESPONSE = OK_HTTP_PREFIX + OK_MESSAGE;
    private static final String BAD_MESSAGE = "Health check is Bad\r\n";
    private static final String BAD_HTTP_PREFIX = "HTTP/1.1 503 Service Unavailable\r\nContent-Type: text/plain\r\nContent-Length: " + BAD_MESSAGE.length() + ENVELOPE_TERMINATOR;
    public static final String BAD_RESPONSE = BAD_HTTP_PREFIX + BAD_MESSAGE;
    private static final String FATAL_MESSAGE = "Unrecoverable error\r\n";
    private static final String FATAL_HTTP_PREFIX = "HTTP/1.1 500 Server Error\r\nContent-Type: text/plain\r\nContent-Length: " + FATAL_MESSAGE.length() + ENVELOPE_TERMINATOR;
    public static final String FATAL_RESPONSE = FATAL_HTTP_PREFIX + FATAL_MESSAGE;
    public static final int DEFAULT_PORT = 14193;
    private static final String SHUTDOWN_WARNING = "Exception thrown out of health check runner. The health check response is shutting down which may result in this JVM being killed.";
    private static final int MAX_WAIT_FOR_SERVER_SOCKET_BINDING_MILLIS = 100;
    private static final int MAX_LISTENER_BACKLOG = 20;
    static final int DEFAULT_INTERNAL_HEARTBEAT_MILLIS = 10500;

    private static long runnerCount = 1;

    private final int port;

    private HealthCheckRunner runner;
    private boolean stopping = false;
    private long dropDeadTime;
    private int internalHeartbeatMillis = DEFAULT_INTERNAL_HEARTBEAT_MILLIS;
    private boolean fatalError = false;

    /**
     * Create a healthcheck listener that listens on the default port {@value DEFAULT_PORT}
     */
    public HealthCheck() {
        this(DEFAULT_PORT);
    }

    /**
     * Create a healthcheck listener that listens on the given port
     *
     * @param port the port to listen on.
     */
    @SuppressWarnings("WeakerAccess")
    public HealthCheck(int port) {
        this.port = port;
    }

    /**
     * Start the health check listener.
     */
    public synchronized void start() {
        if (runner == null || !runner.isAlive() || !runner.isInterrupted()) {
            stopping = false;
            runner = new HealthCheckRunner();
            log.info("Starting HealthCheckRunner");
            heartbeat();
            runner.start();
            Thread.yield();
            handleInterruptedException(() -> {
                while (!stopping && !Thread.currentThread().isInterrupted()) {
                    //noinspection SynchronizeOnNonFinalField
                    synchronized (runner) {
                        if (runner.isServerSocketBound()) {
                            return;
                        }
                        runner.wait(MAX_WAIT_FOR_SERVER_SOCKET_BINDING_MILLIS);
                    }
                }
            });
        } else {
            log.warn("HealthCheck.start called when HealthCheckRunner is running.");
        }
    }

    /**
     * Stop the health check listener.
     */
    public synchronized void stop() {
        log.info("HealthCheck.stop() called", new Exception("Stack Trace").fillInStackTrace());
        stopping = true;
        if (runner != null && runner.isAlive()) {
            runner.interrupt();
            runner.shutdownServerSocket();
            handleInterruptedException(() -> runner.join());
        }
    }

    /**
     * Tell the health check that an unrecoverable error has occurred. After this call the health check will return a
     * 500 response to all requests.
     */
    public void fail() {
        fatalError = true;
        log.error("HealthCheck.fail was called", new Exception("Stack Trace").fillInStackTrace());
    }

    private void handleInterruptedException(InterruptableThunk thunk) {
        try {
            thunk.run();
        } catch (InterruptedException e) {
            // The current thread was interrupted, but we don't want to deal with that here; so set the interrupted flag and return.
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Return the maximum number of milliseconds that can elapse between calls to {@link #heartbeat()} before the health
     * check starts returning responses indicating an unhealthy state.
     *
     * @return the maximum number of milliseconds that can elapse between calls to {@link #heartbeat()} before the
     * health * check starts returning responses indicating an unhealthy state.
     */
    @SuppressWarnings("unused")
    public int getInternalHeartbeatMillis() {
        return internalHeartbeatMillis;
    }

    /**
     * Set the maximum number of milliseconds that can pass between calls to {@link #heartbeat()} before the health
     * check starts returning 503 responses. Also calls {@link #heartbeat()} internally.
     *
     * @param internalHeartbeatMillis the maximum number of milliseconds that can elapse between calls to {@link
     *                                #heartbeat()} before the health * check starts returning responses indicating an
     *                                unhealthy state.
     * @return this object
     */
    @SuppressWarnings("unused")
    public HealthCheck setInternalHeartbeatMillis(int internalHeartbeatMillis) {
        this.internalHeartbeatMillis = internalHeartbeatMillis;
        heartbeat();
        return this;
    }

    /**
     * Tell the healthcheck that this app is currently in a good state and that it should respond to health check
     * requests with a 200 (OK) response.
     */
    public void heartbeat() {
        log.debug("Internal heartbeat");
        dropDeadTime = System.currentTimeMillis() + internalHeartbeatMillis;
    }

    @FunctionalInterface
    private interface InterruptableThunk {
        void run() throws InterruptedException;
    }

    private class HealthCheckRunner extends Thread {
        private ServerSocket serverSocket;

        public HealthCheckRunner() {
            super("HealthCheck-" + runnerCount);
            runnerCount += 1;
        }

        boolean isServerSocketBound() {
            return serverSocket != null && serverSocket.isBound();
        }

        @Override
        public void run() {
            try (ServerSocket ss = new ServerSocket(port, MAX_LISTENER_BACKLOG)) {
                serverSocket = ss;
                log.info("HealthCheckRunner has started on port {}", port);
                if (log.isDebugEnabled()) {
                    logServerSocketDetails(serverSocket);
                }
                synchronized (this) {
                    notifyAll();
                }
                respondToHealthChecks();
            } catch (SocketException e) {
                if (stopping) {
                    log.info("In response to a stop() call, health check stopped listing for connections.");
                } else {
                    log.error(SHUTDOWN_WARNING, e);
                }
            } catch (Exception e) {
                log.error(SHUTDOWN_WARNING, e);
            } finally {
                log.info("HealthCheckRunner has shut down");
                synchronized (this) {
                    notifyAll();
                }
            }
        }

        private void logServerSocketDetails(ServerSocket serverSocket) {
            log.debug("ServerSocket isClosed: {}; isBound: {}; InetAddress: {}; LocalPort: {}; LocalSocketAddress: {}",
                    serverSocket.isClosed(), serverSocket.isBound(), serverSocket.getInetAddress(),
                    serverSocket.getLocalPort(), serverSocket.getLocalSocketAddress());
        }

        private void respondToHealthChecks() throws IOException {
            while (!isInterrupted()) {
                if (log.isDebugEnabled()) {
                    log.debug("Waiting for connection on {}", serverSocket);
                }
                try (Socket socket = serverSocket.accept()) {
                    String response;
                    if (fatalError) {
                        response = FATAL_RESPONSE;
                    } else {
                        long now = System.currentTimeMillis();
                        response = (now <= dropDeadTime) ? OK_RESPONSE : BAD_RESPONSE;
                    }
                    log.info("Responding to health check with {}", response);
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write(response.getBytes());
                        out.flush();
                    }
                }
            }
        }

        private void shutdownServerSocket() {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    log.warn("Error closing server socket", e);
                }
                serverSocket = null;
            }
        }
    }
}
