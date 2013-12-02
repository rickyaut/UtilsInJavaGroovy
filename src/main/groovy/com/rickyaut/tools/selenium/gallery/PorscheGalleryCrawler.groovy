package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.porsche.com/usa/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".m-01-menu-item .m-01-sub-menu a.m-01-model-figure"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	vehicleObjects<<[name: Utils.getText(driver, vehicleElement).trim(), 
					url: vehicleElement.getAttribute("href"),
					thumbnailUrl: vehicleElement.findElement(By.tagName("img")).getAttribute("data-image-src")]
} 

for(def vehicleObject : vehicleObjects){
	try{
		driver.get(vehicleObject.url)
		driver.findElement(By.linkText("View all media")).click();
		def images = []
		for(WebElement element: driver.findElements(By.cssSelector("ul.galleryPictures li a"))){
			WebElement image = element.findElement(By.tagName("img"));
			images<<[description:"",
				thumbnailUrl:image.getAttribute("src"),
				imageUrl:element.getAttribute("rel")]
		}
		def videos = [];
		vehicleObject<<[images: images, videos: videos]
	
	}catch(Exception ex){}
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/car/porsche-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
