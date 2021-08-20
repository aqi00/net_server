package com.servlet.nearby.bean;

import java.util.List;

public class QueryResponse {
    private String code="0";
    private String desc;
	private List<PersonInfo> personList;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

	public void setPersonList(List<PersonInfo> personList) {
		this.personList = personList;
	}
	
	public List<PersonInfo> getPersonList() {
		return this.personList;
	}
}
