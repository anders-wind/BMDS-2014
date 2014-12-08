Mini-project 1
==============

This is the first of the course's four mini-project. The purpose of this mini-project is for you to (a) get acquainted with socket programming and (b) experience first-hand one of the central difficulties of distributed systems: that messages may be lost or just delayed, and that you can't tell the difference.

#Rules
You must submit in groups of size at least 2; your submission must be in the form of single .zip archive. Note that approval of your submission is a prequisite for attending the examination. 

The description below assumes you write your programs in Java. If you like, write your programs in a different language. If you do, you must of course translate “using only DatagramSocket” etc. appropriately. If your chosen language is not either Java or C#, you cannot assume that the TAs can help you. If your chosen language is not one of C, C++, C#, F#, Java, Scala, Ruby, Python, OCaml or Haskell, please contact me before beginning.

#Project
##Write a UDP forwarder. 
Your program must accept as input a hostname or ip h and two ports p1, p2. Your program must then listen on the local host on p1, forwarding all datagrams received on p1 to host h at port p2. Ignore return traffic from h.

Submit your solution as a single Java-file UDPForwarder.java.

##Write a TCP forwarder
i.e., a program which given a host h and ports p1, p2 listens at p1, and for any connection established at p1 establishes a connection to h, p2, forwarding messages in either direction. Test your forwarder by inserting it between a browser and a HTTP-server, i.e., if you run your forwarder to itu.dk port 80 at local port 8080, point your browser to “http://localhost:8080”.

Submit your solution as a single Java-file TCPForwarder.java.

##Write a drop-in replacement for DatagramSocket 
which randomly discards, duplicates or reorders a given percentage of sent datagrams. Inherit from DatagramSocket.

Submit your solution as a single Java-file QuestionableDatagramSocket.java.

##Implement an RFC862 server 
adhering to the RFC 862 specification. Use port 7007 instead of 7.

Submit your solution as a single Java-file RFC862.java.

##Write programs to estimate UDP datagram loss
Your programs must accept as input (a) datagram size, (b) number of datagrams sent and (c) interval between transmissions. The program must output (1) absolute number and percentage of lost datagrams and (2) absolute number and percentage of duplicated datagrams. It is acceptable if your estimate cannot distinguish between losing a single reply, losing a single response, or losing both a reply and a response.

Use your loss estimator to demonstrate:
- Datagram loss on a local connection on a single machine (i.e., no physical net).
- Datagram loss on Wifi. 
- Datagram loss on ethernet.
- Datagram loss on the Internet (i.e., transmitting across multiple physical nets).
- Indicate for each of these four cases the parameters (a-c above) you used to elicit the loss and the observed lossage (i-ii above). Explain where and why you expect the loss to be happening. 

Submit java source file(s) and a .txt file briefly summarising your findings.

##Write programs to reliably communicate over UDP
Using only DatagramSocket, write programs A,B which together reliably transmits a string of length less than 255 characters. Program A accepts at startup a destination ip and port and a string, then transmits the string, somehow ensures that the string correctly arrived, says so and terminates. Program B repeatedly receives such strings and prints them. Your transmission mechanism must guarantee that for each invocation of Program A with string S, Program B prints S exactly once. The server must handle multiple concurrent clients, and can use only space linear in the number of concurrent clients. (Worry about this only when you have a working solution for the rest.)

If you decide this is impossible, explain why and make your best approximation.

Submit java source files and optionally a .txt file briefly summarising your impossiblity argument.

##Write a .txt file with feedback
Indicate briefly how much time you spend on design and on code, where your major obstacles were, and any comments you might have. Anonymise the time breakdown ("A: 4hr design, 8hr code."; no names). This information is for the benefit of me, and will be used to tailor future mini-projects.

#Hints
- Design your solutions as a group, then split out to implement. Re-convene as a group when your design comes apart.
- One way to split the work is along a UDP/TCP axis for the first four parts. However, make sure that you get to do, or at least understand, both; don't do only UDP or only TCP.
- Don’t do anything with threads requiring synchronisation if you can at all avoid it. In particular, don't write to the same variable/member from distinct threads.
- Source code from Lecture 2 might come in handy. 
- An echo-server is running at 106.185.40.123:7.
- One quick and dirty way to achieve serialisation is using ByteBuffer (see wrap); however, you might also like DataInputStream.
- Google and especially stackoverflow are your friends when you are wondering how to do something specific in Java; say, how to convert a string to a byte array or how to interrupt a thread. Check dates, though: Some popular answers on stackoverflow while still working may have better solutions in recent java releases.
- You should not run out of time, but if you do, indicate briefly in .txt files what your solutions would have been/how your present solutions are defective and how they should be improved.
