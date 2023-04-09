package demo;

import akka.actor.ActorRef;

public class MessageToForward {
	private final String string;
	private final ActorRef to;
	
	MessageToForward(String string, ActorRef to){
		this.to = to;
		this.string = string;
	}
	
	public ActorRef getTo() {
		return this.to;
	}
	
	public String getString() {
		return this.string;
	}

}
