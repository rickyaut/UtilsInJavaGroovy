package com.rickyaut.tools.cucumber;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.rickyaut.tools.cucumber.ShoppingCart;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ShoppingCartImplStepsdefs {
	@Autowired
	ShoppingCart shoppingCart;

	@Given("^I have a real shopping cart$")
	public void I_have_a_shopping_cart() {
		assertNotNull(shoppingCart);
	}

	@When("^I add ([a-zA-Z]+) to real shopping cart$")
	public void I_add_an_item(String item) {
		shoppingCart.addItem(item);
	}

	@Then("^([a-zA-Z]+) has been added to the real shopping cart$")
	public void the_item_has_been_added_to_shopping_cart(String item) {
		Assert.assertTrue(shoppingCart.listItems().contains(item));
	}
	
	@And("^the real shopping cart has ([a-zA-Z]+)$")
	public void the_shopping_cart_has(String item){
		shoppingCart.addItem(item);
	}
	
	@When("^([a-zA-Z]+) is removed from real shopping cart$")
	public void item_is_removed_from_shopping_cart(String item){
		shoppingCart.removeItem(item);
	}
	
	@Then("^the real shopping cart should not have ([a-zA-Z]+)$")
	public void shopping_cart_no_longer_has_this_item(String item){
		Assert.assertFalse(shoppingCart.listItems().contains(item));
	}
	
	@Then("^the real shopping cart should have ([a-zA-Z]+)$")
	public void the_real_shopping_cart_should_have(String item){
		Assert.assertTrue(shoppingCart.listItems().contains(item));
	}
}
