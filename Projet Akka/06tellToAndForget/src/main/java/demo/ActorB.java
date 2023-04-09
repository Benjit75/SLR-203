package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorB extends UntypedAbstractActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	 public static Props createActor() {
			return Props.create(ActorB.class, () -> {
				return new ActorB();
			});
	}
	 
	@Override
    public void onReceive(Object message) throws Exception {
    	if (message instanceof String) {
    		String string = (String) message;
    		this.log.info("Received message : " + string + " from " + getSender() + ".");
    		getSender().tell("hi", getSelf());
    		this.log.info("Sent reply : \"hi\" to " + getSender() + ".");
    	}
    }
}
