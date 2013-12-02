package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

String getText(WebDriver driver, WebElement element){
	JavascriptExecutor js = (JavascriptExecutor)driver;
	String text = js.executeScript('return $(arguments[0]).text()', element);
	return text;
}
WebDriver driver = new ChromeDriver()
driver.get("http://www.holden.com.au/gallery/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".tab-content li a"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def url = vehicleElement.getAttribute("href")
	if(!url.startsWith("http://")){
		url ="http://www.holden.com.au"+url
	}
	def vehicleName = getText(driver, vehicleElement.findElement(By.tagName("span")))
	def vehicleThumbnailURL = vehicleElement.findElement(By.tagName("img")).getAttribute("src")
	vehicleObjects<<[name: vehicleName, 
					url: url,
					thumbnailUrl: vehicleThumbnailURL]
} 

for(def vehicleObject : vehicleObjects){
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
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/car/holden-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
