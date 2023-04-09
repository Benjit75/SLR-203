package demo;

public class PubMessage {
	private final String topic;
	private final Object content;
	
	public PubMessage(String topic, Object content) {
		this.topic = topic;
		this.content = content;
	}
	
	public final String getTopic() {
		return this.topic;
	}
	
	public final Object getContent() {
		return this.content;
	}
}
