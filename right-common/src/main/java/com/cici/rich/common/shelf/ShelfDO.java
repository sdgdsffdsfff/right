package com.cici.rich.common.shelf;

import java.io.Serializable;

public class ShelfDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2824002344299808276L;
	private Integer id;
	private Long numIid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}
}
