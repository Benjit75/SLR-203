package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Transmitter extends UntypedAbstractActor {
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	
	 public static Props createActor() {
			return Props.create(Transmitter.class, () -> {
				return new Transmitter();
			});
	}
	 
    @Override
    public void onReceive(Object message) throws Exception {
    	if (message instanceof MessageToForward) {
    		MessageToForward msgToFrwd = (MessageToForward) message;
    		this.log.info("Received message : " + msgToFrwd.getString() + " from " + getSender() + " to forward to " + msgToFrwd.getTo() + ".");
    		msgToFrwd.getTo().tell(msgToFrwd.getString(), getSender());
    		this.log.info("Message forwarded.");
    	}
    }
}
