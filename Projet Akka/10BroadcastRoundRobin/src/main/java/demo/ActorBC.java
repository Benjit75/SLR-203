package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorBC extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef broadcaster;
    
	public static Props createActor() {
		return Props.create(ActorBC.class, () -> {
			return new ActorBC();
		});
	}

    @Override
    public void onReceive(Object message) {
    	if (message instanceof ActorRef) { 
            this.broadcaster = (ActorRef) message;
			this.log.info("Received actorRef broadcaster : " + (ActorRef) message + ".");
            this.broadcaster.tell("join", this.getSelf());
            this.log.info("Sent join request to broadcaster.");
        } else {
            this.log.info("Received message : \"" + message + "\" from " + getSender());
        }
    }
}
