package demo;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ElectionActor extends UntypedAbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private int id, leader;
    private boolean participated, elected, done; //done is not used in my implementation
    private ActorRef nextActor; //the only actor we can send message to
    
    public static Props createActor() {
		return Props.create(ElectionActor.class, () -> {
			return new ElectionActor();
		});
	}
    
    public void onReceive(Object message) throws Exception {
    	if (message instanceof ActorRef) {
    		this.nextActor = (ActorRef) message; //Acknowledgment of our next actor
    		this.log.info("Received next actor: " + this.nextActor + ".");
    	} else if (message instanceof IdMessage) {
        	this.id = ((IdMessage) message).getId(); //we get our id
    		this.log.info("Received our id: " + this.id + ".");
        } else if (message instanceof StartElectionMessage) {
    		this.log.info("Received Start order. Waiting 2 seconds that everyone receive their id and next actor.");
			this.getContext().system().scheduler().scheduleOnce(Duration.ofMillis(2000), getSelf(), new StartAfterWaitMessage(), getContext().system().dispatcher(), ActorRef.noSender());
        } else if (message instanceof StartAfterWaitMessage) {
        	this.log.info("Wait over.");
            this.participated = true;
            nextActor.tell(new ElectionMessage(this.id), this.getSelf()); //we are the one starting, sending our id
    		this.log.info("Sending our id: " + this.id + " to next actor: " + this.nextActor + ".");
        } else if (message instanceof ElectionMessage) {
            int idElection = ((ElectionMessage) message).getId();
            if (idElection > this.id) {
                this.participated = true;
                this.nextActor.tell(new ElectionMessage(idElection), getSelf()); //transfer the message that a bigger id exists
        		this.log.info("Received higher id: " + idElection + ", sending it to next actor: " + this.nextActor + ".");
            } else if (idElection < id) {
            	if (!this.participated) {
            		this.participated = true;
                    this.nextActor.tell(new ElectionMessage(this.id), this.getSelf()); //we haven't already participated and for now there is no bigger id
            		this.log.info("Received lower id: " + idElection + ", but we have not participated yet so we send our id: " + this.id + " to next actor: " + this.nextActor + ".");
            	} else {
            		this.log.info("Received lower id: " + idElection + ", and we have already participated.");
            	}
            } else {
                this.elected = true; //we are the one elected
                this.leader = this.id;
                this.done = true;
                this.nextActor.tell(new ElectedMessage(this.id), this.getSelf()); //send the new to assert our power
        		this.log.info("Received our id: " + idElection + ", we then are leader and have to assert our domination by sending our id to next actor: " + this.nextActor + ".");
            }
        } else if (message instanceof ElectedMessage) {
            this.leader = ((ElectedMessage) message).getId(); //someone has been elected
            this.done = true;
            this.elected = (this.leader == this.id); //maybe it's us but we would eventually already know
            if (!this.elected) {
            	this.nextActor.tell(new ElectedMessage(this.leader), this.getSelf()); //if it is not, we got to tell the others the id of leader
        		this.log.info("Received leader id: " + this.leader + ". We have to tell the next actor: " + this.nextActor + ".");
            } else {
        		this.log.info("Received elected id: " + this.leader + ". That is us, everyone should already know our election.");
            }
        }
    }
}