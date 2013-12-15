package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.subaru.com/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".vehicles .vehicle-item "))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	vehicleObjects<<[name: Utils.getText(driver, vehicleElement.findElement(By.cssSelector(".model-name"))).trim(), 
					url: vehicleElement.findElement(By.cssSelector("a")).getAttribute("href"),
					thumbnailUrl: vehicleElement.findElement(By.cssSelector("img.global-nav-img")).getAttribute("src").replaceAll("handrail.png", "nav_default.png")]
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	driver.findElement(By.cssSelector("a#gallery")).click()
	try{
		def images = []
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".gallery_grid_container img.thumb_image")))
		for(WebElement element : driver.findElements(By.cssSelector(".gallery_grid_container img.thumb_image"))){
			images<<[description: element.getAttribute("alt"),
				thumbnailUrl: element.getAttribute("src"),
				imageUrl: element.getAttribute("src").replaceAll("mp_thumb_220", "mp_hero_880") ]
		}
		vehicleObject<<[images: images]
	}catch(Exception ex){
		ex.printStackTrace()
	}
}
def json = new groovy.json.JsonBuilder([lastUpdate: new Date().format("yyyy-MM-dd"), vehicles: vehicleObjects])
def file = new File("./export/car/subaru-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
