package demo;

import java.util.ArrayList;
import java.util.List;
import akka.actor.UntypedAbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Broadcaster extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final List<ActorRef> actors = new ArrayList<>();
    
	public static Props createActor() {
		return Props.create(Broadcaster.class, () -> {
			return new Broadcaster();
		});
	}

	@Override
	public void onReceive(Object message) {
    	if (message.equals("join")) {
    		this.actors.add(getSender());
    		this.log.info("Received join request from " + getSender() + " : actor added.");
    	} else {
			this.log.info("Received message : \"" + message + "\" from : " + getSender() + ".");
			for (ActorRef actor : this.actors) {
				actor.tell(message, getSender()); //
				this.log.info("Message : \"" + message + "\" sent to : " + actor + ".");
			}
		}
	}
}

