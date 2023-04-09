package demo;

public class SubMessage {
	private final String topic;
	private final Object content;
	
	public SubMessage(String topic, Object content) {
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
