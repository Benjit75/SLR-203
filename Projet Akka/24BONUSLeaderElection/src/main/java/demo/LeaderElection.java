package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.util.ArrayList;

public class LeaderElection {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("LeaderElectionSystem");
        ArrayList<ActorRef> actors = new ArrayList<>();
        int numActors = 3; // number of processes in the system, supposed > 0
        
        // create actors and assign id
        actors.add(system.actorOf(ElectionActor.createActor(), "actor0"));
        actors.get(0).tell(new IdMessage(0), ActorRef.noSender());
        for (int i = 1; i < numActors; i++) {
            actors.add(system.actorOf(ElectionActor.createActor(), "actor" + i));
            actors.get(i).tell(new IdMessage(i), ActorRef.noSender());
            actors.get(i - 1).tell(actors.get(i), ActorRef.noSender());
        }
        actors.get(numActors - 1).tell(actors.get(0), ActorRef.noSender());
        
        // start the election
        int start = (int) Math.random()*(numActors-1);
        actors.get(start).tell(new StartElectionMessage(), ActorRef.noSender());
    }
}