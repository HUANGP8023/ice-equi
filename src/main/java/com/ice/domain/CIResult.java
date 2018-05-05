/**
 * Project Name:ice-equi
 * File Name:CIResult.java
 * Package Name:com.ice.domain
 * Date:2018年4月23日下午12:03:16
 * Copyright (c) 2018, 彩慧居 All Rights Reserved.
 *
*/

package com.ice.domain;
/**
 * ClassName:CIResult <br/>
 * Date:     2018年4月23日 下午12:03:16 <br/>
 * @author   R.ZENG
 * @version  
 * @since    JDK 1.8
 */
public class CIResult {
	private int code; // 结果编号
	private String msg = ""; // 结果信息

	/**
	 * 结果对象
	 */
	private Object data;
	public CIResult() {	}
	public CIResult(int code, String msg) {
		this.code = code;
		this.msg = msg == null ? "" : msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}

