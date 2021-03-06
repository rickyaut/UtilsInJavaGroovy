package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.mustangboats.com.au/tournament-pleasure-boats.htm")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector("#boat_range>ul>li>div>ul"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def url = vehicleElement.findElement(By.cssSelector("a")).getAttribute("href")
	vehicleObjects<<[name: vehicleElement.findElement(By.cssSelector("a img")).getAttribute("alt"), 
					url: url,
					thumbnailUrl: vehicleElement.findElement(By.cssSelector("a img")).getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/boat/mustang-gallery.json", vehicleObjects);
for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/boat/mustang-gallery.json")

private updateVehicleImages(WebDriver driver, vehicleObject) {
	driver.get(vehicleObject.url)
	def images = []
	for(WebElement element : driver.findElements(By.cssSelector("#group li a"))){
		images<<[description: "",
			thumbnailUrl: element.findElement(By.tagName("img")).getAttribute("src"),
			imageUrl: element.getAttribute("href") ]
	}
	vehicleObject<<[images: images]
	return vehicleObject
}
