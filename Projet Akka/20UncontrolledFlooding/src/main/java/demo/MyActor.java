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
			//we could verify if we already know this actor (slower though)
			//if (!knownActors.contains((ActorRef) message)) {
			this.knownActors.add((ActorRef) message);//}
			this.log.info("Received actor ref : " + (ActorRef) message + ". Added to known actors.");
		} else {
			this.log.info("Received message : \"" + message + "\" from actor : " + getSender() + ".");
			this.forward(message);
		}
		
	}
	
	private void forward(Object message) {
		for (ActorRef actor : this.knownActors) {
			actor.tell(message, this.getSelf());
			this.log.info("Message : \"" + message + "\" forwarded to actor : " + actor + ".");
		}
	}
	

}
