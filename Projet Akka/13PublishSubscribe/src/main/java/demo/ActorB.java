package demo;

import akka.actor.UntypedAbstractActor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class ActorB extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef server;
    private final String topic1 = "topic1", topic2 = "topic2";
    
	public static Props createActor() {
		return Props.create(ActorB.class, () -> {
			return new ActorB();
		});
	}

    @Override
    public void onReceive(Object message) {
    	if (message instanceof ActorRef) { 
    		this.server = (ActorRef) message;
			this.log.info("Received actorRef server : " + (ActorRef) message + ".");
	        this.server.tell(new SubDemand(this.topic1), this.getSelf());
			this.log.info("Sent \"SUBSCRIBE\" to topic: \"" + this.topic1 + "\".");
	        this.server.tell(new SubDemand(this.topic2), this.getSelf());
			this.log.info("Sent \"SUBSCRIBE\" to topic: \"" + this.topic2 + "\".");
		} else if (message instanceof SubMessage){
			this.log.info("Received message : \"" + ((SubMessage) message).getContent() + "\" from topic: \"" + ((SubMessage) message).getTopic() + "\".");
		}
	}
}
