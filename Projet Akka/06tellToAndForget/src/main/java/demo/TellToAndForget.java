package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class TellToAndForget {

  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("system");
    ActorRef a = system.actorOf(Props.create(ActorA.class), "a");
    ActorRef b = system.actorOf(Props.create(ActorB.class), "b");
    ActorRef transmitter = system.actorOf(Props.create(Transmitter.class), "transmitter");

    a.tell(new ActorRefs(b, transmitter), ActorRef.noSender());
    a.tell("start", ActorRef.noSender());
  }
}