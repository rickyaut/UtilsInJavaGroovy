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
driver.get("http://www.jaguarusa.com/all-models/all-models/index.html")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".new-vehicles .hide-mobile a"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	vehicleElement.click();
	sleep(2000)
	for(WebElement element2 : driver.findElements(By.cssSelector(".tab-item"))){
		WebElement anchor = element2.findElement(By.cssSelector("a:nth-child(1)"))
		vehicleObjects<<[name: getText(driver, element2.findElement(By.cssSelector(".tab-header h3"))), 
						url: anchor.getAttribute("href"),
						thumbnailUrl: anchor.findElement(By.cssSelector("img")).getAttribute("src")]
	}
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	driver.get(driver.findElement(By.linkText("GALLERY")).getAttribute("href"));
	sleep(2000)
	try{
		def images = []
		for(WebElement element : driver.findElements(By.cssSelector(".gallery-media img"))){
			String imageURL = element.getAttribute("data-desktop-src")
			images<<[description: element.getAttribute("alt"),
				thumbnailUrl: imageURL.replaceAll("_desktop_1366x769.jpg", "_gallerythumbnail_195x110.jpg"),
				imageUrl: imageURL ]
		}
		vehicleObject<<[images: images]
	}catch(Exception ex){
		ex.printStackTrace()
	}

}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/jaguar-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
