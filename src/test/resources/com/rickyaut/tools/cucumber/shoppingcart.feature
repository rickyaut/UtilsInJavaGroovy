Feature: Shopping Cart
	Scenario: add item
		Given I have a shopping cart
		When I add Apple 
		Then Apple has been added to shopping cart
	Scenario: add item to real shopping cart
		Given I have a real shopping cart
		When I add Apple to real shopping cart
		Then Apple has been added to the real shopping cart
	Scenario: remove item from real shopping cart
		Given I have a real shopping cart
		And the real shopping cart has Banana
		When Banana is removed from real shopping cart
		Then the real shopping cart should not have Banana
	Scenario: the real shopping cart is not empty
		Given I have a real shopping cart
		Then the real shopping cart should have Apple
		