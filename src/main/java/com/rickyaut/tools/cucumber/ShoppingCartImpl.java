package com.rickyaut.tools.cucumber;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartImpl implements ShoppingCart {
	private List<String> items = new ArrayList<String>();
	
	public void addItem(String name) {
		items.add(name);
	}

	public List<String> listItems() {
		return items;
	}

	public void removeItem(String item) {
		items.remove(item);
	}

}
