

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
				final ActorRef a = system.actorOf(ActorA.createActor(), "a");
				final ActorRef b = system.actorOf(ActorBC.createActor(), "b");
				final ActorRef c = system.actorOf(ActorBC.createActor(), "c");
				final ActorRef broadcaster = system.actorOf(Broadcaster.createActor(), "broadcaster");
				
				a.tell(broadcaster, ActorRef.noSender());
				b.tell(broadcaster, ActorRef.noSender());
				c.tell(broadcaster, ActorRef.noSender());
				
			}
		};
	}
}
