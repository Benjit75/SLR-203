import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;

import demo.*;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

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
	public void testFloodParameter() {
		ActorSystem system = ActorSystem.create("UncontrolledFlooding");
		final int nbActors = 5;
		final boolean FLOOD_PARAMETER = true;
		boolean adjacencyMatrix [][] = 
			{
		        {false, true, true, false, false},
		        {false, false, false, true, false}, 
		        {false, false, false, true, false},
		        {false, false, false, false, true},
		        {false, FLOOD_PARAMETER, false, false, false},
		    };
		ArrayList<ActorRef> actors = new ArrayList<ActorRef>();

		for (int i = 0; i < nbActors; i++) {
			actors.add(system.actorOf(MyActor.createActor(), "actor" + Integer.toString(i)));
		}

		for (int i = 0; i < nbActors; i++) {
			for (int j = 0; j < nbActors; j++) {
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