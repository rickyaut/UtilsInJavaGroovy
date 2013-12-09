package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

WebDriver driver = new ChromeDriver()
driver.get("http://www.mbusa.com/mercedes/vehicles")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".bodystyle-class"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	WebElement thumbnail = vehicleElement.findElement(By.cssSelector("a.class-track"));
	for(WebElement modelElement: vehicleElement.findElements(By.cssSelector(".class-list ul li a.model"))){
		vehicleObjects<<[name: Utils.getText(driver, modelElement),
			url: modelElement.getAttribute("href"),
			thumbnailUrl: thumbnail.getAttribute("src")]

	}
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	try{
		driver.findElement(By.cssSelector(".liquid-wrapper .gallery a")).click();
		String interiorURL = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.linkText("Interior")))?.getAttribute("href")
		def interiorImages = []
		def exteriorImages = []
		if(interiorURL){
			driver.get(interiorURL)
			(new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".gallery-wrapper .slide-type-IMAGE .media img")))
			for(WebElement element: driver.findElements(By.cssSelector(".gallery-wrapper .slide-type-IMAGE .media img"))){
				String imageURL = element.getAttribute("src");
				if(imageURL.endsWith("-GOI-D.jpg")){
					interiorImages<<[description:element.getAttribute("alt"),
						thumbnailUrl:imageURL.replaceAll("-GOI-D.jpg", "-GTI-D.jpg"),
						imageUrl:imageURL]
				}else if(imageURL.endsWith("-GOE-D.jpg")){
					exteriorImages<<[description:element.getAttribute("alt"),
						thumbnailUrl:imageURL.replaceAll("-GOE-D.jpg", "-GTE-D.jpg"),
						imageUrl:imageURL]
				}else{
					println imageURL
				}
			}
		}
		def videos = [];
		vehicleObject<<[exteriorImages: exteriorImages, interiorImages: interiorImages, videos: videos]
	}catch(Exception ex){
		ex.printStackTrace()
	}
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/car/benz-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
