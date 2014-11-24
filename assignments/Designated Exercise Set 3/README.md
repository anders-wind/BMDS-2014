Designated Exercise Set 3
=========================

Answers
-------------------------

### 15.4 
**In the central server algorithm for mutual exclusion, describe a situation in which two requests are not processed in happened-before order.**
Network latencies might affect the order of the token requests the server receives. 
Process a might request the token before process b, but if the latency of the connection of process a is high, then the request of process b might reach the centralized server before the request of process a.
   
_(Page 636)_


### 15.6 
**Give an example execution of the ring-based algorithm to show that processes are not necessarily granted entry to the critical section in happened-before order.**
The processes in the ring can communicate independently of the ring. 
Imagine we have a clockwise ring of process A, B and C, where process A currently has the token. Process C requests the token. Process B then requests the token.

Process B would then in this case get the token before process C, even though process C requested the token before B. This means that the processes aren't granted entry in happened-before order.
	  
_(Page 637)_


### 15.23 
**Show that Byzantine agreement can be reached for three generals, with one of them faulty, if the generals digitally sign their messages.**

(See page 665)


### 17.4 
**Give an example of the interleaving, of two transactions that is serially equivalent at each server but is not serially equivalent globally.**
om
(See page 740)


### 17.10 
**A centralized global deadlock detector holds the union of local wait-for graphs. Give an example to explain how a phantom deadlock could be detected if a waiting transaction in a deadlock cycle aborts during the deadlock detection procedure.**

If the deadlock detection procedure has "passed" a transaction that then releases its lock (by aborting), then the deadlock no longer exists. 
A phantom deadlock will therefore be registered by the deadlock detection algorithm until it runs again.
_(Page 745)_


### 17.11 
**Consider the edge-chasing algorithm (without priorities). Give examples to show that it could detect phantom deadlocks.**
By checking the entire wait-for graph, any deadlocks are found and any phantom deadlocks are "cleared up". 
If the probe visits every process in the cycle, then any phantom deadlocks will be found. (Shitty explanation, but phantom deadlocks can't exist with edge-chasing.)
(See page 746)


### 17.12 
**A server manages the objects a1, a2, ... an.
It provides two operations for its clients:**

		read(i) 				returns the value of ai		write(i, Value) 		assigns Value to ai
	**The transactions T, U and V are defined as follows:**
		T: x = read(i); write(j, 44); 
		U: write(i, 55); write(j, 66); 
		V: write(k, 77); write(k, 88);1. **Describe the information written to the log file on behalf of these three transactions if strict two-phase locking is in use and U acquires ai and aj before T.** 


2. **Describe how the recovery manager would use this information to recover the effects of T, U and V when the server is replaced after a crash.**


3. **What is the significance of the order of the commit entries in the log file?**


(See pages 753–754)
