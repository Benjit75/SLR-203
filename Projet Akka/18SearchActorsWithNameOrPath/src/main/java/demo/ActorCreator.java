package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorCreator extends UntypedAbstractActor {
	
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	private int nbActorsCreated = 0;
	
	public static Props createActor() {
		return Props.create(ActorCreator.class, () -> {
			return new ActorCreator();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		// TODO Auto-generated method stub
		if (message instanceof CreateMessage) {
			this.log.info("Received order CREATE. Actor"+this.nbActorsCreated+" created.");
			this.getContext().actorOf(MyActor.createActor(), "actor" + this.nbActorsCreated);
			this.nbActorsCreated ++;
		}
	}
	
	
	

}
