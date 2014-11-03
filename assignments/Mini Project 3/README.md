#Part A
####You must implement the following three kinds of processes, potentially running at distinct machines.

A "Node" process takes as arguments a local port and optionally the IP/port of another Node process. The Node then listens for PUT and GET requests on its local port.

A "Put-client" process takes as arguments the IP/port of a Node, an integer key, and a string value. The client then submits a PUT(key, value) message to the indicated node and terminates.

A "Get-client" process takes arguments the IP/port of a Node, and an integer key. It submits a GET(key, ip2, port2) message to the indicated Node, then listens on ip2/port2 for a PUT(key, value) message, which, if it arrives, indicates that the Node network has stored the association (key, value), that is, that some Put-client previously issued that PUT. 

If the network of Nodes receives inconsistent PUTs, (e.g., PUT(1, A) then later PUT(1, B)), the value of subsequent GETs is undefined (i.e., no answer, PUT(1, A) and PUT(1, B) are all valid results.).

**Example execution. (We have omitted IP addresses for brevity.)**

	Node starts at 1025.
	Node starts at 1026, knowing 1025. 
	Client PUT(1, A) to 1025.
	Client at 2048 sends GET(1, 2048) to 1025; eventually receives PUT(1, A) at 2048 from someone.
	Client at 2049 sends GET(1, 2049) to 1026; eventually receives PUT(1, A) at 2049 from someone.
	Client at 2049 sends GET(2, 2049) to 1025; never receives an answer from anyone.
	Node C starts at 1027, knowing 1026.
	Client at 2048 sends GET(1, 2048) to 1027; eventually receives PUT(1, A) at 2048 from someone.
	PUT(2, B) to 1025. 

**After implementation, answer the following questions:**

1. Is your system:
	- a publish/subscribe system?
		No. Nodes do not subscribe to anyone, but just listens. Instead a Node can have responsebility to send a message on to another node.
	- a message Queue?
		No. Since no queues or centrilized servers are involved.
	- a structured P2P system?
		No. The Get call takes O(N) time in the worst case, since nodes are connected in a long line, and not in a pastry format.
	- an unstructured P2P system?
		Yes. The system is a P2P system where nodes functions as peers. The System is unstructered since the Get function takes O(N) in the worst case.
	- a distributed set implementation?
	- a distributed hash table implementation?
		No. But by doing so one could achieve a Structered P2P system.
2. What is the average-case, best-case, and worst-case space consumed at each Node?
	E is the amount of Puts(messages), N is the amount of nodes. 
	Worst Case for each Node is E where all messages are on one single node. 
	Best Case for each Node is 0 where all messages are on other nodes.
	Average Case for each Node is E/N such that all nodes have an equal amount of the messages
3. What is the average-case, best-case and worst-case number and size of messages being sent as a result of 
	- A PUT message from a client, and
		The Put message is called to a single node and therefore the amount of messages are constant for all three cases.
	- A successful GET message from a client (that is, a value is found and sent back.)
		Worst case O(N), averagecase O(N) or N/2 and best case is 1.
	- An unsuccessful GET message from a client (that is, no value is found.)
		For all cases the result is infinite. DONT KNOW IF THIS IS TRUE
4. Based on 2 and 3, write a paragraph or two on the current scalability of your system. 
	The scalability is quite poor riht now. In case of failure the system is almost unusable and for a few thousand peers the system will also get too slow to use.
5. Based on 2, 3 and 4, give suggestions for improving the scalability of your system. 
	BY implementing a structured P2P system suh as the pastry structure one could improve the Get time and space consumption at the nodes. A Timeout functionality could make the system failsafe such that a constant and not infinite time consumption in case of a unsuccesfull GET. The Heartbeat functionality could improve the system in the case that a node leaves the system.

#Part B 
####Enhance your Node processes of Part A such that the network is resilient to the loss of any one Node. Continuing the above example: 

Node 1025 crashes.
Client at 2048 sends GET(2, 2048) to 1026, eventually receives PUT(2, B) from someone.

Similarly, if instead 1026 or 1027 crashes, issuing GET(2, ...) to any of the remaining nodes should get the proper result PUT(2,B).

Your resiliency mechanism does not need to accommodate subsequent crashes: if a second note crashes your system need no longer be able to provide proper responses. 

If your system either always reconfigures itself so that it can handle subsequent crashes or sometimes can reconfigure itself and you know and can argue for the probability of that happening, your group wins a Ritter Sport of your preference for each group member along with positive remarks at the November 17 lecture. 

**Along with the documentation, submit the answer to the following questions.**

1. Briefly explain your solution and why it works. 
2. What is the average-case, best-case, and worst-case space consumed at each Node?
3. What is the average-case, best-case and worst-case number and size of messages being sent as a result of 
	- A PUT message from a client, and
	- A successful GET message from a client (that is, a value is found and sent back.)
	- An unsuccessful GET message from a client (that is, no value is found.)
4. Write a paragraph or two suggesting improvements to scalability that does not compromise your one-node-resiliency.
5. If your solution provides exactly one-node-resiliency, write a paragraph or two suggesting methods your one-node-resiliency can be expanded to n-node resiliency, assuming your network has time to reconfigure itself in between node failures.

#Hints

1. As always, go for the simplest possible solution. As always, this exercise has a fairly simple solution, however, for Part B, it is perhaps not so easy to argue that the simple solution works.
2. ...  that said, this is a harder exercise than the previous ones. Expect to use some more time discussing design. 
3. I expect you to this using only TCP or UDP sockets and whatever's in the Java SE API. 
4. If you run out of time, then stop when you've spent 25 hours per group member; spend the remaining one hour per group member writing up what you've got, then submit that. (You've got a total of 30 hours for this mini-project, the lecture before it, and reading for that lecture.)
5. You will likely need to transmit different kinds of messages. One convenient way to do this in Java is to use serialization. E.g., make, say, message classes implementing Serializable, then use ObjectOutputStream and ObjectInputStream to read/write them.
6. There are two ways to go about this mini has important: you could either design and implement both parts A and B in one go, or you could first completely forget about part B while designing and implementing part A, then add part B. 
7. Use the TAs, the Forum, and the Office Hours. Feel free to bring your design by and see if we see any problems with it—doing so might save you lots of time. 
