package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.mazdausa.com/MusaWeb/displayModelSelector.action")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".model_vehicle"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def nameElement = vehicleElement.findElement(By.cssSelector(".model_name"))
	vehicleObjects<<[name: Utils.getText(driver, nameElement), 
					url: "http://www.mazdausa.com/MusaWeb/displayPage.action?pageParameter=modelsGallery&vehicleCode="+nameElement.getAttribute("id"),
					thumbnailUrl: vehicleElement.findElement(By.cssSelector("img.modimg")).getAttribute("src")]
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	def images = []
	for(WebElement element : driver.findElements(By.cssSelector(".gallery_cell .thumbHolder a"))){
		String imageURL = element.findElement(By.cssSelector("img")).getAttribute("src")
		images<<[description: '',
			thumbnailUrl: imageURL,
			imageUrl: element.getAttribute("href") ]
	}
	vehicleObject<<[images: images]
}
def json = new groovy.json.JsonBuilder([lastUpdate: new Date().format("yyyy-MM-dd"), vehicles: vehicleObjects])
def file = new File("./export/car/mazda-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
