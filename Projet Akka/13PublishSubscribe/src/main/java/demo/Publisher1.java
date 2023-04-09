package demo;

import akka.actor.UntypedAbstractActor;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class Publisher1 extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String topic = "topic1";
    private ActorRef server;
    
	public static Props createActor() {
		return Props.create(Publisher1.class, () -> {
			return new Publisher1();
		});
	}

    @Override
    public void onReceive(Object message) {
    	if (message instanceof ActorRef) { 
    		this.server = (ActorRef) message;
			this.log.info("Received actorRef server : " + (ActorRef) message + ".");
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(2000), getSelf(), new CommandMessage("hello"), getContext().system().dispatcher(), ActorRef.noSender());
			this.log.info("Asking the scheduler to send me command message \"hello\" in 2 seconds.");
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(6000), getSelf(), new CommandMessage("hello2"), getContext().system().dispatcher(), ActorRef.noSender());
			this.log.info("Asking the scheduler to send me command message \"hello\" in 6 seconds.");
		} else if (message instanceof CommandMessage){
			String messageToPub = ((CommandMessage) message).getContent();
			this.log.info("Received command message : \"" + messageToPub + "\".");
	        this.server.tell(new PubMessage(this.topic, messageToPub), this.getSelf());
			this.log.info("Sent: \"" + messageToPub + "\" to topic: \"" + this.topic + "\".");
		}
	}
}
