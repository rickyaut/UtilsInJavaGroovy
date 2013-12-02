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
driver.get("http://www.lamborghini.com/en/models/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".tx-core4-slidestage-subnavigation-level1 .slideStageLink"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def url = vehicleElement.getAttribute("href")
	def id = vehicleElement.getAttribute("id").replaceAll("-link", "-teaser")
	//def modelName = getText(driver, vehicleElement)
	vehicleObjects<<[//name: modelName.replace("»", "").trim(), 
					url: url,
					thumbnailUrl: driver.findElement(By.cssSelector("#tx-core4-slidestage-subnavigation-stage #"+id+" img")).getAttribute("src")]
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	vehicleObject<<[name: driver.findElement(By.cssSelector("nav.grd-inner h1")).getText().trim()]
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
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/car/lamborghini-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
