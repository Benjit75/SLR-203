package demo;

import akka.actor.ActorRef;


public class ActorRefs {
    private final ActorRef b;
    private final ActorRef transmitter;

    public ActorRefs(ActorRef b, ActorRef transmitter) {
      this.b = b;
      this.transmitter = transmitter;
    }
    
    public ActorRef getB() {
    	return this.b;
    }
    
    public ActorRef getTransmitter() {
    	return this.transmitter;
    }
  }
