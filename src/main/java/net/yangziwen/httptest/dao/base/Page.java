package net.yangziwen.httptest.dao.base;

import java.util.Collections;
import java.util.List;

public class Page<E> {
	
	public static final String DEFAULT_OFFSET = "0";
	public static final String DEFAULT_LIMIT = "30";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Page EMPTY_PAGE = new Page(0, 0, 0, Collections.emptyList());

	private int offset;
	private int limit;
	private int count;
	private List<E> list;
	
	public Page() {}
	
	public Page(int offset, int limit, int count, List<E> list) {
		this.offset = offset;
		this.limit = limit;
		this.count = count;
		this.list = list != null? list: Collections.<E>emptyList();
	}
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<E> getList() {
		return list;
	}
	public void setList(List<E> list) {
		this.list = list;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Page<T> emptyPage() {
		return (Page<T>) EMPTY_PAGE;
	}
	
}
