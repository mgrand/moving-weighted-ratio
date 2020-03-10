# Moving Weighted Ratio

This is a class that can be used to track the ratio of some subset of
events to the total number of events. For example, the number of
requests that produced an error result to the total number of requests.
The ratio is maintained as a weighted moving average. More recent
measurements have more weight than older measurements.

 The way it works is that there are 15 bit counters provided in groups
 of two or four. Each can counter in a group can be incremented
 independently of the others.

 The counters are kept as 15 bit quantities. When a counter in a group
 overflows, all the counters in the group are right-shifted. Right
 shifting all of the counters in a group handles the overflow while
 preserving the ratios between them.

 These operations are thread-safe and do not block.