package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.audiusa.com/models")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".model-list-component li.responsive-image"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	def url = vehicleElement.findElement(By.cssSelector(".resp-holder a")).getAttribute("href")
	vehicleObjects<<[name: Utils.getText(driver, vehicleElement.findElement(By.cssSelector(".txt h5:nth-child(1)"))), 
					url: url,
					thumbnailUrl: vehicleElement.findElement(By.cssSelector(".resp-holder img")).getAttribute("src")]
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	for(int i = 0; i < 5; i++){
		try{
			def images = []
			for(WebElement element : driver.findElements(By.linkText("View gallery"))){
				element.click();
				(new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".modal-full-bleed #modal-close")))
				for(def element2 : driver.findElements(By.cssSelector("#modal .carousel-viewport .carousel-items .global-gallery-image"))){
					String imageURL = element2.findElement(By.cssSelector("img")).getAttribute("src")
					images<<[description: Utils.getText(driver, element2.findElement(By.cssSelector("h5"))),
						thumbnailUrl: imageURL.replaceAll("1280", "230"),
						imageUrl: imageURL ]
				}
				driver.findElement(By.cssSelector(".modal-full-bleed #modal-close")).click();
			}
			vehicleObject<<[images: images]
			break;
		}catch(Exception ex){
			driver.navigate().refresh();
		}
	
	}
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/car/audi-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
