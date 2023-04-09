package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.ArrayList;

public class ControlledFlooding {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("Flooding");
		ArrayList<ActorRef> actors = new ArrayList<ActorRef>();
		final int nbActors = 5;
		boolean adjacencyMatrix [][] = 
			{
				{false, true, true, false, false},
				{false, false, false, true, false}, 
                {false, false, false, true, false},
                {false, false, false, false, true},
                {false, true, false, false, false},
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
		for (int count = 0; count<10; count++) {
			actors.get(0).tell("flood message", ActorRef.noSender());
		}
		
	}

}
