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
	
GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/volvo-gallery.json", vehicleObjects);
	for(def vehicleObject : vehicleObjects){
		if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
			updateCarImages(driver, vehicleObject)
		}
	}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/volvo-gallery.json")
	driver.quit();
}

private updateCarImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	driver.findElement(By.linkText("Photos")).click();
	def images = []
	for(WebElement element : driver.findElements(By.cssSelector("#thumbnail-container li a"))){
		images<<[description: "",
			thumbnailUrl: element.findElement(By.cssSelector("img")).getAttribute("src"),
			imageUrl: element.getAttribute("href") ]
	}
	vehicleObject<<[images: images]
	return vehicleObject
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
	
GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/truck/volvo-gallery.json", vehicleObjects);
	for(def vehicleObject : vehicleObjects){
		if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
			updateTruckImages(driver, vehicleObject)
		}
	}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/truck/volvo-gallery.json")
	driver.quit();
}

private updateTruckImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	def images = []
	for(WebElement element : driver.findElements(By.cssSelector(".product-gallery-thumbnails img"))){
		images<<[description: "",
			thumbnailUrl: element.getAttribute("src"),
			imageUrl: element.getAttribute("src").replaceAll("/thumbs/120x69_", "/892x438_") ]
	}
	vehicleObject<<[images: images]
	return vehicleObject
}

getCarGallery();
getTruckGallery();