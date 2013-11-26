package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

String getText(WebDriver driver, WebElement element){
	JavascriptExecutor js = (JavascriptExecutor)driver;
	String text = js.executeScript('return $(arguments[0]).text()', element);
	return text;
}

WebDriver driver = new ChromeDriver()
driver.get("http://www.lexus.com/models/allVehicles/index.html")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector("#allVehiclesContainer .allVehiclesVehicle .allVehiclesVehicleImg a"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def image = vehicleElement.findElement(By.cssSelector("img"))
	vehicleObjects<<[name: image.getAttribute("IS"), 
					url: vehicleElement.getAttribute("href"),
					thumbnailUrl: image.getAttribute("src")]
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	driver.get(driver.findElement(By.cssSelector("li.gallery a")).getAttribute("href"))
	def images = []
	for(WebElement element : driver.findElements(By.cssSelector(".galleryPhoto img"))){
		String imageURL = element.getAttribute("src").replaceAll("_462x249.jpg", "_227x121.jpg")
		images<<[description: element.getAttribute("alt"),
			thumbnailUrl: imageURL,
			imageUrl: imageURL.replaceAll("_227x121.jpg", "_1024x576.jpg") ]
	}
	vehicleObject<<[images: images]
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/lexus-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()