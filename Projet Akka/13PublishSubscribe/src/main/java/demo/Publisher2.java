package demo;

import akka.actor.UntypedAbstractActor;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class Publisher2 extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String topic = "topic2";
    private ActorRef server;
    
	public static Props createActor() {
		return Props.create(Publisher2.class, () -> {
			return new Publisher2();
		});
	}

    @Override
    public void onReceive(Object message) {
    	if (message instanceof ActorRef) { 
    		this.server = (ActorRef) message;
			this.log.info("Received actorRef server : " + (ActorRef) message + ".");
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(4000), getSelf(), new CommandMessage("world"), getContext().system().dispatcher(), ActorRef.noSender());
			this.log.info("Asking the scheduler to send me command message \"world\" in 4 seconds.");
		} else if (message instanceof CommandMessage){
			String messageToPub = ((CommandMessage) message).getContent();
			this.log.info("Received command message : \"" + messageToPub + "\".");
	        this.server.tell(new PubMessage(this.topic, messageToPub), this.getSelf());
			this.log.info("Sent: \"" + messageToPub + "\" to topic: \"" + this.topic + "\".");
		}
	}
}
