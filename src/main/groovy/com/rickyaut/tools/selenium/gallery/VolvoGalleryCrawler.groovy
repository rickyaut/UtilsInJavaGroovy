package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Actions

import com.rickyaut.tools.common.Utils

void getCarGallery(){
	WebDriver driver = new ChromeDriver()
	driver.get("http://www.volvocars.com/us/Pages/default.aspx")
	
	List<WebElement> vehicleElements = driver.findElements(By.cssSelector("#top #model-nav .model"))
	def vehicleObjects = []
	for(WebElement vehicleElement: vehicleElements){
		def url = vehicleElement.findElement(By.cssSelector("a")).getAttribute("href")
		Actions action = new Actions(driver)
		action.moveToElement(vehicleElement).perform()
		sleep(500)
		vehicleObjects<<[name: Utils.getText(driver, vehicleElement.findElement(By.cssSelector("a"))).trim(),
						url: url,
						thumbnailUrl: driver.findElement(By.cssSelector(".flyout-link img")).getAttribute("src")]
	}
	
	for(def vehicleObject : vehicleObjects){
		driver.get(vehicleObject.url)
		driver.findElement(By.linkText("Photos")).click();
		def images = []
		for(WebElement element : driver.findElements(By.cssSelector("#thumbnail-container li a"))){
			images<<[description: "",
				thumbnailUrl: element.findElement(By.cssSelector("img")).getAttribute("src"),
				imageUrl: element.getAttribute("href") ]
		}
		vehicleObject<<[images: images]
	}
	def json = new groovy.json.JsonBuilder(vehicleObjects)
	def file = new File("./export/car/volvo-gallery.json")
	if(file.exists()){
		file.delete();
	}
	file << json.toPrettyString()
	driver.quit();
}


void getTruckGallery(){
	WebDriver driver = new ChromeDriver()
	driver.get("http://apps.volvotrucks.com/trucksoverview/home.aspx/index?lama=en-us")
	
	List<WebElement> vehicleElements = driver.findElements(By.cssSelector("td.truckimage"))
	def vehicleObjects = []
	for(WebElement vehicleElement: vehicleElements){
		vehicleObjects<<[name: Utils.getText(driver, vehicleElement.findElement(By.xpath("following-sibling::td[1]")).findElement(By.cssSelector(".headline"))).trim(),
						url: vehicleElement.findElement(By.cssSelector("a")).getAttribute("href"),
						thumbnailUrl: vehicleElement.findElement(By.cssSelector("img")).getAttribute("src")]
	}
	
	for(def vehicleObject : vehicleObjects){
		driver.get(vehicleObject.url)
		def images = []
		for(WebElement element : driver.findElements(By.cssSelector(".product-gallery-thumbnails img"))){
			images<<[description: "",
				thumbnailUrl: element.getAttribute("src"),
				imageUrl: element.getAttribute("src").replaceAll("/thumbs/120x69_", "/892x438_") ]
		}
		vehicleObject<<[images: images]
	}
	def json = new groovy.json.JsonBuilder(vehicleObjects)
	def file = new File("./export/truck/volvo-gallery.json")
	if(file.exists()){
		file.delete();
	}
	file << json.toPrettyString()
	driver.quit();
}

//getCarGallery();
getTruckGallery();