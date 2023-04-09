# SLR203 Akka-Patterns Benjamin Ternot
## Examples :
Patterns 01 to 05

## Top Priority
Patterns 06, 10, 19, 20, 21, 22

## BONUS
24 (LeaderElection)
23 (Distributed HashTable) too difficult

## Medium
13, 18


### Questions on 22shortestLengthFlooding
1.  If we send the first message to A, it appears that P receives 3 messages, if we do not count the messages that were sent to give the actor references and the order of giving this answer.
The shortest length received is 3, and we can even determine with the logs that it is path A->D->I->P.
		
2. By beginning with L, P receives 3 messages.
The shorter length received is 4, (path L->M->N->O->P).
		
3. By beginning with I, Q receives 2 messages.
The shorter length received is 5 (path I->P->C->J->R->Q).
		
NOTE: the number of receive messages can depend of the run, but the shortest length does not (even if the path can, because of possible multiple shortest path).
