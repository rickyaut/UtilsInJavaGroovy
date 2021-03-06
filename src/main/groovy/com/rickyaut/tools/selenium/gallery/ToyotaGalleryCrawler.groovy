package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.toyota.com/all-vehicles/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector("ul.filterResultGird li.gridItem"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	WebElement innerElement = vehicleElement.findElement(By.cssSelector(".gridItemInner"))
	def href = innerElement.getAttribute("data-href");
	def linkedSeries = innerElement.getAttribute("data-linkedseries");
	vehicleObjects<<[name: Utils.getText(driver, vehicleElement.findElement(By.cssSelector(".titleContainer .page-header"))).trim(), 
					url: "http://www.toyota.com/"+(linkedSeries? linkedSeries:href)+"/photo-gallery.html",
					thumbnailUrl: vehicleElement.findElement(By.cssSelector("table .carImageContainer img")).getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/toyota-gallery.json", vehicleObjects);
for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/toyota-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	def images = []
	for(WebElement element: driver.findElements(By.cssSelector(".photo-thumbs li.photo-thumb a img"))){
		images<<[description:element.getAttribute("data-description"),
			thumbnailUrl:element.getAttribute("src"),
			imageUrl:"http://www.toyota.com"+element.getAttribute("data-fullscreen")]
	}
	def videos = [];
	for(WebElement element: driver.findElements(By.cssSelector(".video-thumbs ul li img"))){
		videos<<[description: element.getAttribute("data-yttitle"), youtubeID: element.getAttribute("data-yt-id")]
	}
	vehicleObject<<[images: images, videos: videos]
	return vehicleObject
}
