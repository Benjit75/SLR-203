import java.util.ArrayList;
import java.time.Duration;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;

import demo.*;


public class PatternTest {
	
	private static ActorSystem system;
	
	public static final String getCharForNumber(int i) {
		
		if (i > -1 && i < 26) {
			return String.valueOf((char)(i + 65));
		}
	    return "";
	}
	
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
		ArrayList<ActorRef> actors = new ArrayList<ActorRef>();
		final int nbActors = 18;
		int adjacencyMatrix [][] = 
			{//	 A B C D E F G H I J K L M N O P Q R
				{0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0},//A
				{1,0,1,1,1,1,0,0,0,0,1,0,0,0,0,0,0,0},//B
				{1,1,0,1,1,1,0,0,0,1,0,0,0,0,0,0,0,0},//C
				{1,1,1,0,1,1,0,0,1,0,0,0,0,0,0,0,0,0},//D
				{1,1,1,1,0,1,1,0,0,0,0,0,0,0,0,0,0,0},//E
				{1,1,1,1,1,0,0,0,0,0,0,1,0,0,0,0,0,0},//F
				{0,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0},//G
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},//H
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},//I
				{0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1},//J
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},//K
				{0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},//L
				{0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0},//M
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},//N
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},//O
				{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},//P
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},//Q
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},//R
			};
		for (int i = 0; i<nbActors; i++) {
			actors.add(system.actorOf(MyActor.createActor(), getCharForNumber(i)));
		}
		
		for (int i = 0; i<nbActors; i++) {
			for (int j = 0; j<nbActors; j++) {
				if (adjacencyMatrix[i][j] == 1) {
					actors.get(i).tell(actors.get(j), ActorRef.noSender());
				}
			}
		}
		//Problem: we have to wait to make sure all the references are received.
		//Solution, using the scheduler as in pattern n°10: BroadcastRoundRobin.
		int sendingTo = 11; //0 for A, 11 for L, 8 for I
		int askingStatsTo = 15; //15 for P, 16 for Q
		system.scheduler().scheduleOnce(Duration.ofSeconds(2), actors.get(sendingTo), "m", system.dispatcher(), ActorRef.noSender());
		system.scheduler().scheduleOnce(Duration.ofSeconds(10), actors.get(askingStatsTo), "TELL-YOUR-STATS", system.dispatcher(), ActorRef.noSender());
		
		//By beginning with A, P receives 3 messages (except the order "TELL-YOUR-STATS" and actor refs).
		//The shorter length received is 3 (we can also detect that the path of this message was A->D->I->P).
		
		//By beginning with L, P receives 3 messages (except the order "TELL-YOUR-STATS" and actor refs).
		//The shorter length received is 4 (we can also detect that the path of this message was L->M->N->O->P).
		
		//By beginning with I, Q receives 2 messages (except the order "TELL-YOUR-STATS" and actor refs).
		//The shorter length received is 5 (we can also detect that the path of this message was I->P->C->J->R->Q).
		
		//NOTE: the number of receive message can depend of the run, but the shortest length does not (even if the path can, because of possible multiple shortest path).
	}

}