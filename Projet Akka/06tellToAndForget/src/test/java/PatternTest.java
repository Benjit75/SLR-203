
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
	public void testPattern() {
		new TestKit(system) {
			{
				final ActorRef a = system.actorOf(ActorA.createActor(), "a");
				final ActorRef b = system.actorOf(ActorB.createActor(), "b");
				final ActorRef transmitter = system.actorOf(Transmitter.createActor(), "transmitter");
				
				a.tell(new ActorRefs(transmitter, b), ActorRef.noSender());
				
				a.tell("start", ActorRef.noSender());
			}
		};
	}
}
