package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorA extends UntypedAbstractActor {
	
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef b;
    private ActorRef transmitter;
    
    public static Props createActor() {
		return Props.create(ActorA.class, () -> {
			return new ActorA();
		});
	}

    @Override
    public void onReceive(Object message) throws Exception {
    	if (message instanceof ActorRefs) {
    		ActorRefs actorRefs = (ActorRefs) message; 
    		this.b = actorRefs.getB();
    		this.transmitter = actorRefs.getTransmitter();
    		this.log.info("Received actorRefs from " +getSender()+".");
    	} else if (message instanceof String) {
    		String string = (String) message;
    		this.log.info("Received message : \"" + string + "\" from " + getSender() + ".");
    		if (string.equals("start")) {
				this.log.info("Command recognized : \"start\".");
				this.transmitter.tell(new MessageToForward("hello", this.b), this.getSelf());
				this.log.info("Sent message : \"hello\" to " + this.transmitter + " with order of forwarding to " + this.b + ".");
			}
    	}
    }
}
