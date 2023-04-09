package demo;

public class SeqMessage extends Object{
	private final Object message;
	private final Integer seqId;
	
	public SeqMessage(Object message, Integer id) {
		this.message = message;
		this.seqId = id;
	}

	public Integer getSeqId() {
		return this.seqId;
	}
	
	public Object getMessage() {
		return this.message;
	}
	
	public String toString() {
		return "(" + this.getMessage() + ", seqId=" + this.getSeqId() + ")";
	}
	
}
