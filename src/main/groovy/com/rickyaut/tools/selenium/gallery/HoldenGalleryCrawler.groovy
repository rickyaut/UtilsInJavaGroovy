package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.holden.com.au/gallery/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".tab-content li a"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def url = vehicleElement.getAttribute("href")
	if(!url.startsWith("http://")){
		url ="http://www.holden.com.au"+url
	}
	def vehicleName = Utils.getText(driver, vehicleElement.findElement(By.tagName("span")))
	def vehicleThumbnailURL = vehicleElement.findElement(By.tagName("img")).getAttribute("src")
	vehicleObjects<<[name: vehicleName, 
					url: url,
					thumbnailUrl: vehicleThumbnailURL]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/holden-gallery.json", vehicleObjects);
for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/holden-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	def exteriorImages = []
	for(def imageElement : driver.findElements(By.cssSelector(".mainCarousel li.exterior img "))){
		def src = imageElement.getAttribute("src")
		exteriorImages<<[description: imageElement.getAttribute("alt"), thumbnailUrl: src+"&image.resize.width=148&image.resize.height=96", imageUrl: src ]
	}
	vehicleObject<<[exteriorImages: exteriorImages]
	def interiorImages = []
	for(def imageElement : driver.findElements(By.cssSelector(".mainCarousel li.interior img "))){
		def src = imageElement.getAttribute("src")
		interiorImages<<[description: imageElement.getAttribute("alt"), thumbnailUrl: src+"&image.resize.width=148&image.resize.height=96", imageUrl: src ]
	}
	vehicleObject<<[interiorImages: interiorImages]
	return vehicleObject
}
