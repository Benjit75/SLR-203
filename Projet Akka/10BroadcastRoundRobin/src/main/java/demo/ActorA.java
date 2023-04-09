package demo;

import akka.actor.UntypedAbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.time.Duration;

public class ActorA extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef broadcaster;
    
	public static Props createActor() {
		return Props.create(ActorA.class, () -> {
			return new ActorA();
		});
	}

    @Override
    public void onReceive(Object message) {
    	if (message instanceof ActorRef) { 
    		this.broadcaster = (ActorRef) message;
			this.log.info("Received actorRef broadcaster : " + (ActorRef) message + ".");
	        getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), "go", getContext().system().dispatcher(), ActorRef.noSender());
			this.log.info("Asking the scheduler to send me \"go\" in 1 second.");
		} else if (message.equals("go")) {
			this.log.info("Received message : \"go\".");
			this.broadcaster.tell("hello", this.getSelf());
			this.log.info("Sent message : \"hello\" to broadcaster.");
		}
	}
}
