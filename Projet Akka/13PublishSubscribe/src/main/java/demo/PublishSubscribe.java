package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class PublishSubscribe {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("PublishSubscribe");
        ActorRef server = system.actorOf(Server.createActor(), "server");
        ActorRef a = system.actorOf(ActorA.createActor(), "a");
        ActorRef b = system.actorOf(ActorB.createActor(), "b");
        ActorRef c = system.actorOf(ActorC.createActor(), "c");
        ActorRef p1 = system.actorOf(Publisher1.createActor(), "p1");
        ActorRef p2 = system.actorOf(Publisher2.createActor(), "p2");

        
        a.tell(server, ActorRef.noSender());
        b.tell(server, ActorRef.noSender());
        c.tell(server, ActorRef.noSender());
        p1.tell(server, ActorRef.noSender());
        p2.tell(server, ActorRef.noSender());
	}
}
