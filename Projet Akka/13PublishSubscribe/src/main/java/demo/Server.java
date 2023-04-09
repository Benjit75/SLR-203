package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import akka.actor.UntypedAbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Server extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final HashMap<String, ArrayList<ActorRef>> subscribers = new HashMap<String, ArrayList<ActorRef>>();
    
	public static Props createActor() {
		return Props.create(Server.class, () -> {
			return new Server();
		});
	}

	@Override
	public void onReceive(Object message) {
    	if (message instanceof SubDemand) {
    		String topic = ((SubDemand) message).getTopic();
    		this.log.info("Received subscribe demand from : " + getSender() + " to topic: \"" + topic + "\".");
    		if (this.subscribers.containsKey(topic)) {
    			if (!this.subscribers.get(topic).contains(getSender())) {
    				this.subscribers.get(topic).add(getSender());
    	    		this.log.info("Actor : " + getSender() + " added to topic: \"" + topic + "\".");
    			} else {
    				this.log.info("Actor : " + getSender() + " already in topic: \"" + topic + "\".");
    			}
    		} else {
    			ArrayList<ActorRef> list = new ArrayList<ActorRef>();
    			list.add(getSender());
    			this.subscribers.put(topic, list);
	    		this.log.info("Actor : " + getSender() + " added to new topic: \"" + topic + "\".");
    		}
    	} else if (message instanceof UnsubDemand){
    		String topic = ((UnsubDemand) message).getTopic();
    		this.log.info("Received unsubscribe demand from : " + getSender() + " to topic: \"" + topic + "\".");
    		if (this.subscribers.containsKey(topic)) {
    			if (this.subscribers.get(topic).contains(getSender())) {
    				this.subscribers.get(topic).remove(getSender());
    	    		this.log.info("Actor : " + getSender() + " removed from topic: \"" + topic + "\".");
    			} else {
    				this.log.info("Actor : " + getSender() + " was not in topic: \"" + topic + "\". Unsub request ignored.");
    			}
    		} else {
	    		this.log.info("Topic: \"" + topic + "\" does not exist. Unsub request of actor : " + getSender() + " ignored.");
    		}
		} else if (message instanceof PubMessage) {
			String topic = ((PubMessage) message).getTopic();
			Object content = ((PubMessage) message).getContent();
    		this.log.info("Received publication: \"" + content + "\" from : " + getSender() + " to topic: \"" + topic + "\".");
    		if (this.subscribers.containsKey(topic)) {
    			SubMessage subMessage = new SubMessage(topic, content);
    			for (ActorRef sub : this.subscribers.get(topic)) {
    				sub.tell(subMessage, this.getSelf());
    				this.log.info("Publication: \"" + content + "\" from topic: \"" + topic + "\" sent to sub: " + sub + ".");
    			}
    		} else {
	    		this.log.info("Topic: \"" + topic + "\" does not currently have subscribers. Publication lost.");
    		}
		}
    	
	}
}

