package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;


public class MyActor extends UntypedAbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private final List<ActorRef> knownActors = new ArrayList<>();
	
	public static Props createActor() {
		return Props.create(MyActor.class, () -> {
			return new MyActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		// TODO Auto-generated method stub
		if (message instanceof ActorRef) {
			this.knownActors.add((ActorRef) message);
			this.log.info("Received actor ref : " + (ActorRef) message + ". Added to known actors.");
		}
		
	}
	
	

}
