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
	- a message Queue?
	- a structured P2P system?
	- an unstructured P2P system?
	- a distributed set implementation?
	- a distributed hash table implementation?
2. What is the average-case, best-case, and worst-case space consumed at each Node?
3. What is the average-case, best-case and worst-case number and size of messages being sent as a result of 
	- A PUT message from a client, and
	- A successful GET message from a client (that is, a value is found and sent back.)
	- An unsuccessful GET message from a client (that is, no value is found.)
4. Based on 2 and 3, write a paragraph or two on the current scalability of your system. 
5. Based on 2, 3 and 4, give suggestions for improving the scalability of your system. 

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
