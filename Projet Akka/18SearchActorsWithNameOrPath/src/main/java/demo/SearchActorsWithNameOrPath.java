package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

public class SearchActorsWithNameOrPath{
	
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("system");

    ActorRef creator = system.actorOf(ActorCreator.createActor(), "creator");

    creator.tell(new CreateMessage(), ActorRef.noSender());
    creator.tell(new CreateMessage(), ActorRef.noSender());
    
    try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    ActorSelection a = system.actorSelection("user/creator");
    ActorSelection a1 = system.actorSelection("user/creator/actor0");
    ActorSelection a2 = system.actorSelection("user/creator/actor1");

    System.out.println("a: " + a.path());
    System.out.println("a1: " + a1.path());
    System.out.println("a2: " + a2.path());

  }
}