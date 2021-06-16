package com.fracta.james.domain.models;

import java.util.List;
import java.util.Objects;

public class InlineQuerySend {

	private final long id;
	private final String offset;
	private final List<InlineQuerySend> results;
	
	public InlineQuerySend(long id, String offset, List<InlineQuerySend> results) {
		this.id = id;
		this.offset = offset;
		this.results = results;
	}
	public InlineQuerySend(long id, List<InlineQuerySend> results) {
		super();
		this.id = id;
		this.offset = null;
		this.results = results;
	}
	
	public long getId() {
		return id;
	}
	public String getOffset() {
		return offset;
	}
	public List<InlineQuerySend> getResults() {
		return results;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, offset, results);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		var other = (InlineQuerySend) o;
		return Objects.equals(id, other.id)
				&& Objects.equals(offset, other.offset)
				&& Objects.equals(results, other.results);
	}
	
	@Override
	public String toString() {
		return "InlineQuerySend{id=" + id
				+ ", offset=" + offset
				+ ", results=" + results
				+ "}";
	}
	
}
