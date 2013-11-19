package com.rickyaut.tools.cucumber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.rickyaut.tools.cucumber.ShoppingCart;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ShoppingCartStepsdefs {
	ShoppingCart mockedShoppingCart;
	
	@Given("^I have a shopping cart$")
	public void I_have_a_shopping_cart(){
		mockedShoppingCart = mock(ShoppingCart.class);
	}
	
	@When("^I add ([a-zA-Z]+)$")
	public void I_add_an_item(String item){
		mockedShoppingCart.addItem(item);
	}
	
	@Then("^([a-zA-Z]+) has been added to shopping cart$")
	public void the_item_has_been_added_to_shopping_cart(String item){
		verify(mockedShoppingCart).addItem(item);
	}
}
