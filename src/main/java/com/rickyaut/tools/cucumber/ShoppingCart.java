package com.rickyaut.tools.cucumber;

import java.util.List;

public interface ShoppingCart {
	void addItem(String name);
	List<String> listItems();
	void removeItem(String item);
	
}
