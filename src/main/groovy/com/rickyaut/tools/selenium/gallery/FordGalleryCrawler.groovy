package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.ford.com/vehicles/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".vehicle_card"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def url = vehicleElement.getAttribute("data-href")
	if(!url.startsWith("http://")){
		url ="http://www.ford.com"+url
	}
	vehicleObjects<<[name: vehicleElement.findElement(By.cssSelector(".front h3")).getText(), 
					url: url,
					thumbnailUrl: vehicleElement.findElement(By.cssSelector(".front img")).getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/ford-gallery.json", vehicleObjects);
for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/ford-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	def galleryLink = null
	try{galleryLink = driver.findElement(By.linkText("Gallery"))}catch(Exception ex){}
	if(galleryLink){
		galleryLink.click()
		def photoLink = null
		try{photoLink=driver.findElement(By.linkText("Photos"))}catch(Exception ex){}
		if(photoLink){
			photoLink.click()
			def exteriorLink = null
			try{exteriorLink = driver.findElement(By.linkText("Exterior"))}catch(Exception ex){}
			if(exteriorLink){
				exteriorLink.click()
				def exteriorImages = []
				for(def imageElement : driver.findElements(By.cssSelector("#tdi_TABLE_images img"))){
					def src = imageElement.getAttribute("src").replaceAll("_ext_med.", "_ext_sm.")
					exteriorImages<<[description: imageElement.getAttribute("alt"), thumbnailUrl: src, imageUrl: src.replaceAll("_ext_sm.", "_ext_lg.") ]
				}
				vehicleObject<<[exteriorImages: exteriorImages]
			}
			def interiorLink = null
			try{interiorLink = driver.findElement(By.linkText("Interior"))}catch(Exception ex){}
			if(interiorLink){
				interiorLink.click()
				def interiorImages = []
				for(def imageElement : driver.findElements(By.cssSelector("#tdi_TABLE_images img"))){
					def src = imageElement.getAttribute("src").replaceAll("_int_med.", "_int_sm.")
					interiorImages<<[description: imageElement.getAttribute("alt"), thumbnailUrl: src, imageUrl: src.replaceAll("_int_sm.", "_int_lg.") ]
				}
				vehicleObject<<[interiorImages: interiorImages]
			}
		}
	}
	return vehicleObject
}
