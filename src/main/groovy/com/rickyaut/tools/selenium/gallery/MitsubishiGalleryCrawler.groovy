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
driver.get("http://www.mitsubishicars.com/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".vehicle-dropdown-item"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	vehicleObjects<<[name: Utils.getText(driver, vehicleElement.findElement(By.cssSelector("h3"))).trim(), 
					url: vehicleElement.getAttribute("href"),
					thumbnailUrl: vehicleElement.findElement(By.cssSelector(".vehicle-dropdown-image img")).getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/mitsubishi-gallery.json", vehicleObjects);

for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/mitsubishi-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	try{
		driver.get(vehicleObject.url)
		driver.findElement(By.linkText("GALLERY")).click();
		driver.findElement(By.linkText("PHOTOS")).click();
		def images = []
		for(def element : driver.findElements(By.cssSelector("li.gallery-photolink a.gallery-image"))){
			images<<[description: element.getAttribute("data-caption"),
				thumbnailUrl: element.findElement(By.cssSelector("img")).getAttribute("src"),
				imageUrl: element.findElement(By.cssSelector("img")).getAttribute("src").replaceAll("/small/", "/").replaceAll("/large/", "/") ]
		}
		vehicleObject<<[images: images]
		return vehicleObject
	
	}catch(Exception ex){
		ex.printStackTrace();
	}
}
