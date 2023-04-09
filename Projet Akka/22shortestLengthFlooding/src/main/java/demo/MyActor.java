package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import akka.actor.ActorRef;


public class MyActor extends UntypedAbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private final List<ActorRef> knownActors = new ArrayList<>();
	private final HashMap<Integer, Integer> receivedMessages = new HashMap<Integer, Integer>();
	private int nbReceivedMessages = 0;
	
	public static Props createActor() {
		return Props.create(MyActor.class, () -> {
			return new MyActor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		// TODO Auto-generated method stub
		if (message instanceof ActorRef) {
			//we could verify if we already know this actor (slower though)
			//if (!knownActors.contains((ActorRef) message)) {
			this.knownActors.add((ActorRef) message);//}
			this.log.info("Received actor ref : " + (ActorRef) message + ". Added to known actors.");
		} else if (message.equals("TELL-YOUR-STATS")) {
			this.log.info("Number of received messages (excluding this order and actor refs) : " + this.nbReceivedMessages + ", shorter length value for seqId=0 : " + this.receivedMessages.get((Integer) 0) +".");
		} else {
			this.nbReceivedMessages ++;
			this.log.info("Received message : \"" + message + "\", from actor : " + getSender() + ".");
			if (message instanceof SeqLengthMessage) {
				if (!this.receivedMessages.containsKey(((SeqLengthMessage) message).getSeqId())) {
					this.receivedMessages.put(((SeqLengthMessage) message).getSeqId(), ((SeqLengthMessage) message).getLength());
					this.log.info("Message : \"" + message + "\" never seen before, added to known messages.");
					this.forward((SeqLengthMessage) message);
				} else if (((SeqLengthMessage) message).getLength() < this.receivedMessages.get(((SeqLengthMessage) message).getSeqId())) {
					this.receivedMessages.remove(((SeqLengthMessage) message).getSeqId());
					this.receivedMessages.put(((SeqLengthMessage) message).getSeqId(), ((SeqLengthMessage) message).getLength());
					this.log.info("Message : \"" + message + "\" has shorter length than known before, replaced old known message.");
					this.forward((SeqLengthMessage) message);
				} else {
					this.log.info("Message : \"" + message + "\" has longer length than known before, no change done.");
				}
			} else {
				Integer newId;
				if (this.receivedMessages.size() == 0) {
					newId = 0;
				} else {
					newId = Collections.max(this.receivedMessages.keySet()) + 1;
					//Problem using this is that we always have to send the message to the same first actor
				}
				this.receivedMessages.put(newId, (Integer) 0);
				this.log.info("seqId=" + newId + " assigned to message : " + message + ".");
				this.forward(new SeqLengthMessage(message, newId, (Integer) 0));
			}
		}
		
	}
	
	private void forward(SeqLengthMessage message) {
		for (ActorRef actor : this.knownActors) {
			actor.tell(new SeqLengthMessage(message.getMessage(), message.getSeqId(), message.getLength() + 1), this.getSelf());
			this.log.info("Message : \"" + message + "\" forwarded to actor : " + actor + ".");
		}
	}
	

}
