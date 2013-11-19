package com.rickyaut.tools.fatwire;


public class Association {
	String name;
	String description;
	String child;
	
	public Association(String assocName, String assocDescription, String child) {
		name = assocName;
		description = assocDescription;
		this.child = child;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getChild() {
		return child;
	}
	
	public void setChild(String child) {
		this.child = child;
	}
}
