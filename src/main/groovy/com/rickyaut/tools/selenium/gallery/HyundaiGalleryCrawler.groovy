package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

WebDriver driver = new ChromeDriver()
driver.get("https://www.hyundaiusa.com/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector("li.vehicle-catagory-panel li.vehicle-box a.vehicle-item"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	vehicleObjects<<[name: vehicleElement.findElement(By.cssSelector("span.vehicle-thumb img")).getAttribute("alt"), 
					url: vehicleElement.getAttribute("href"),
					thumbnailUrl: vehicleElement.findElement(By.cssSelector("span.vehicle-thumb img")).getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/hyundai-gallery.json", vehicleObjects);

for(def vehicleObject : vehicleObjects){
	if((!vehicleObject.images||vehicleObject.images?.isEmpty()) && (!vehicleObject.interiorImages|| vehicleObject.interiorImages?.isEmpty()) && (!vehicleObject.exteriorImages|| vehicleObject.exteriorImages?.isEmpty())){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/hyundai-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	try{
		driver.get(vehicleObject.url)
		driver.findElement(By.linkText("GALLERY")).click();
		def images = []
		for(def element : driver.findElements(By.cssSelector("#vehicle_gallery .has_shadow a.image_hover"))){
			if(element.getAttribute("href").indexOf("youtube")<0){
				images<<[description: element.getAttribute("title"),
					thumbnailUrl: element.findElement(By.cssSelector("img")).getAttribute("src"),
					imageUrl: element.getAttribute("href") ]
			}
		}
		vehicleObject<<[images: images]
		return vehicleObject
	}catch(Exception ex){
		ex.printStackTrace()
	}
}
