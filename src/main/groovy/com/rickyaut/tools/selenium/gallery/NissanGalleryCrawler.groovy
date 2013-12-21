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
driver.get("http://www.nissanusa.com/buildyournissan?lang=en&fromSav=true&next=header.vehicles.sav.button")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector("#model_lines .super-div"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	vehicleObjects<<[name: Utils.getText(driver, vehicleElement.findElement(By.cssSelector("span.model-letter"))).trim(), 
					url: vehicleElement.findElement(By.cssSelector("li.explore a")).getAttribute("href"),
					thumbnailUrl: vehicleElement.findElement(By.cssSelector("img.car-picture")).getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/nissan-gallery.json", vehicleObjects);

for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		try{
			updateVehicleImages(driver, vehicleObject)
		}catch(Exception ex){}
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/nissan-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	try{
		driver.findElement(By.linkText("COLORS & PHOTOS")).click();
		def exteriorImages = []
		def interiorImages = []
		for(def element : driver.findElements(By.cssSelector("#vehicle-gallery .group"))){
			if(StringUtils.equals(element.getAttribute("data-gallery-group"), "exterior")){
				for(def item : element.findElements(By.cssSelector("li.image"))){
					exteriorImages<<[description: Utils.getText(driver, item.findElement(By.cssSelector("p.description"))),
						thumbnailUrl: item.findElement(By.cssSelector("a img")).getAttribute("src"),
						imageUrl: item.findElement(By.cssSelector("a")).getAttribute("href") ]
				}
			}else{
				for(def item : element.findElements(By.cssSelector("li.image"))){
					interiorImages<<[description: Utils.getText(driver, item.findElement(By.cssSelector("p.description"))),
						thumbnailUrl: item.findElement(By.cssSelector("a img")).getAttribute("src"),
						imageUrl: item.findElement(By.cssSelector("a")).getAttribute("href") ]
				}
			}
		}
		vehicleObject<<[exteriorImages: exteriorImages, interiorImages: interiorImages]
	}catch(Exception ex){
		driver.findElement(By.linkText("Gallery")).click();
		def images = []
		for(def element : driver.findElements(By.cssSelector("li.slide"))){
			try{
				images<<[description: Utils.getText(driver, element.findElement(By.cssSelector("figcaption"))).trim(),
					thumbnailUrl: "",
					imageUrl: element.findElement(By.cssSelector("img")).getAttribute("src") ]
	
			}catch(Exception ex2){}
		}
		vehicleObject<<[images: images]
	}
	return vehicleObject
}
