package com.rickyaut.tools.selenium.gallery

import groovy.json.JsonSlurper

import org.apache.commons.lang.StringUtils
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.kia.com/us/en/vehicles")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".vehicle-grid "))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	vehicleObjects<<[name: vehicleElement.findElement(By.cssSelector("span.vehicle-thumb img")).getAttribute("alt"), 
					url: vehicleElement.getAttribute("href"),
					thumbnailUrl: vehicleElement.findElement(By.cssSelector("span.vehicle-thumb img")).getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/kia-gallery.json", vehicleObjects);

for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/kia-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	driver.findElement(By.linkText("GALLERY")).click();
	def images = []
	for(def element : driver.findElements(By.cssSelector("#vehicle_gallery .has_shadow a.image_hover"))){
		images<<[description: element.getAttribute("title"),
			thumbnailUrl: element.findElement(By.cssSelector("img")).getAttribute("src"),
			imageUrl: element.getAttribute("href") ]
	}
	vehicleObject<<[images: images]
	return vehicleObject
}
