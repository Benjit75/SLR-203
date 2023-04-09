package demo;

import akka.actor.UntypedAbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class ActorC extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String topic = "topic2";
    private ActorRef server;
    
	public static Props createActor() {
		return Props.create(ActorC.class, () -> {
			return new ActorC();
		});
	}

    @Override
    public void onReceive(Object message) {
    	if (message instanceof ActorRef) { 
    		this.server = (ActorRef) message;
			this.log.info("Received actorRef server : " + (ActorRef) message + ".");
	        this.server.tell(new SubDemand(this.topic), this.getSelf());
			this.log.info("Sent \"SUBSCRIBE\" to topic: \"" + this.topic + "\".");
		} else if (message instanceof SubMessage){
			this.log.info("Received message : \"" + ((SubMessage) message).getContent() + "\" from topic: \"" + ((SubMessage) message).getTopic() + "\".");
		}
	}
}
