Designated Exercise Set 3
=========================

Answers
-------------------------

### 15.4 
**In the central server algorithm for mutual exclusion, describe a situation in which two requests are not processed in happened-before order.**

(See page 636)


### 15.6 
**Give an example execution of the ring-based algorithm to show that processes are not necessarily granted entry to the critical section in happened-before order.**

(See page 637)


### 15.23 
**Show that Byzantine agreement can be reached for three generals, with one of them faulty, if the generals digitally sign their messages.**

(See page 665)


### 17.4 
**Give an example of the interleaving, of two transactions that is serially equivalent at each server but is not serially equivalent globally.**

(See page 740)


### 17.10 
**A centralized global deadlock detector holds the union of local wait-for graphs. Give an example to explain how a phantom deadlock could be detected if a waiting transaction in a deadlock cycle aborts during the deadlock detection procedure.**

See page 745


### 17.11 
**Consider the edge-chasing algorithm (without priorities). Give examples to show that it could detect phantom deadlocks.**

See page 746


### 17.12 
**A server manages the objects a1, a2, ... an.
It provides two operations for its clients:**

		read(i) 			returns the value of ai
	

		U: write(i, 55); write(j, 66); 
		V: write(k, 77); write(k, 88);
2. Describe how the recovery manager would use this information to recover the effects of T, U and V when the server is replaced after a crash. 
3. What is the significance of the order of the commit entries in the log file?

See pages 753–754

