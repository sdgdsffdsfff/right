/**
 * @FileName PaginationResult.java
 * @Description: 
 * @Date 2014年11月26日 下午4:02:33
 * @author qj
 * @version V1.0
 * 
 */
package com.cici.rich.dao;

import java.util.List;

/**
 * @FileName PaginationResult.java
 * @Description: 
 * @Date 2014年11月26日 下午4:02:33
 * @author qj
 * @version V1.0
 * 
 */
public class PaginationResult<T> {

	private List<T> objectList;
	private Page page;
	/**
	 * @return the objectList
	 */
	public List<T> getObjectList() {
		return objectList;
	}
	/**
	 * @param objectList the objectList to set
	 */
	public void setObjectList(List<T> objectList) {
		this.objectList = objectList;
	}
	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
	}
	
}
