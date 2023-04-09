package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class BroadcastRoundRobin {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("RoundRobin");
        ActorRef broadcaster = system.actorOf(Broadcaster.createActor(), "broadcaster");
        ActorRef a = system.actorOf(ActorA.createActor(), "a");
        ActorRef b = system.actorOf(ActorBC.createActor(), "b");
        ActorRef c = system.actorOf(ActorBC.createActor(), "c");

        
        a.tell(broadcaster, ActorRef.noSender());
        b.tell(broadcaster, ActorRef.noSender());
        c.tell(broadcaster, ActorRef.noSender());
    }
}

