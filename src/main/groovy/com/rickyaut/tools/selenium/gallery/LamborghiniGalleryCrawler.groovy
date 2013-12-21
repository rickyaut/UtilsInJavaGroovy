package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.lamborghini.com/en/models/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".tx-core4-slidestage-subnavigation-level1 .slideStageLink"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def url = vehicleElement.getAttribute("href")
	def id = vehicleElement.getAttribute("id").replaceAll("-link", "-teaser")
	vehicleObjects<<[//name: modelName.replace("»", "").trim(), 
					url: url,
					thumbnailUrl: driver.findElement(By.cssSelector("#tx-core4-slidestage-subnavigation-stage #"+id+" img")).getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/lamborghini-gallery.json", vehicleObjects);
for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/lamborghini-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	vehicleObject<<[name: Utils.getText(driver, driver.findElement(By.cssSelector("nav.grd-inner h1"))).trim()]
	try{
		driver.findElement(By.linkText("GALLERY")).click()
		def images = []
		for(WebElement element : driver.findElements(By.cssSelector(".slides figure.core4slide-slide"))){
			images<<[description: "",
				thumbnailUrl: element.getAttribute("data-core4slide-thumb"),
				imageUrl: element.getAttribute("data-core4slide-source") ]
		}
		vehicleObject<<[images: images]
	}catch(Exception ex){
		ex.printStackTrace()
	}
	return vehicleObject
}
