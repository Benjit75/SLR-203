package demo;

public class SeqLengthMessage extends Object{
	private final Object message;
	private final Integer seqId, length;
	
	public SeqLengthMessage(Object message, Integer id, Integer length) {
		this.message = message;
		this.seqId = id;
		this.length = length;
	}

	public Integer getSeqId() {
		return this.seqId;
	}
	
	public Object getMessage() {
		return this.message;
	}
	
	public Integer getLength() {
		return this.length;
	}
	
	public String toString() {
		return "(\"" + this.getMessage() + "\", seqId=" + this.getSeqId() + " length=" + this.getLength() +")";
	}
	
}
