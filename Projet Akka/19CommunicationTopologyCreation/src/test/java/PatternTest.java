

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;

import demo.*;

public class PatternTest {
	
	private static ActorSystem system;
	
	@BeforeClass
	public static void setup() {
		system = ActorSystem.create();
	}
	
	@AfterClass
	public static void teardown() {
		TestKit.shutdownActorSystem(system);
		system = null;
	}
	
	@Test
	public void testBroadcastRoundRobin() throws InterruptedException {
		new TestKit(system) {
			{
				final ActorRef actor1 = system.actorOf(MyActor.createActor(), "actor1");
				final ActorRef actor2 = system.actorOf(MyActor.createActor(), "actor2");
				final ActorRef actor3 = system.actorOf(MyActor.createActor(), "actor3");

				actor1.tell(actor2, ActorRef.noSender());
				actor2.tell(actor2, ActorRef.noSender());
				actor2.tell(actor3, ActorRef.noSender());
				actor3.tell(actor1, ActorRef.noSender());
				actor3.tell(actor2, ActorRef.noSender());

				
			}
		};
	}
}
