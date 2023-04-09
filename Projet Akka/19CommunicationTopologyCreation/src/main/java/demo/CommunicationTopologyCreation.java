package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.ArrayList;

public class CommunicationTopologyCreation {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("CommunicaationTopologyCreation");
		ArrayList<ActorRef> actors = new ArrayList<ActorRef>();
		final int nbActors = 4;
		boolean adjacencyMatrix [][] = 
			{
				{false, true, true, false},
				{false, false, false, true}, 
                {true, false, false, true},
                {true, false, false, true}
			};
		for (int i = 0; i<nbActors; i++) {
			actors.add(system.actorOf(MyActor.createActor(), "actor"+Integer.toString(i)));
		}
		for (int i = 0; i<nbActors; i++) {
			for (int j = 0; j<nbActors; j++) {
				if (adjacencyMatrix[i][j]) {
					actors.get(i).tell(actors.get(j), ActorRef.noSender());
				}
			}
		}
		
		
	}

}
