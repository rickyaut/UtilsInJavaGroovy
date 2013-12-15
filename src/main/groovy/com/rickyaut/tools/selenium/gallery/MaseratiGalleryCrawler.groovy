package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

WebDriver driver = new ChromeDriver()
driver.get("http://www.maserati.com/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".hVersion .picbox a img"))
def vehicleObjects = []
for(WebElement vehicleImage: vehicleElements){
	WebElement anchor = vehicleImage.findElement(By.xpath("ancestor::a[1]"))
	vehicleObjects<<[name: vehicleImage.getAttribute("title"), 
					url: anchor.getAttribute("href"),
					thumbnailUrl: vehicleImage.getAttribute("src")]
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	def images = []
	for(WebElement element: driver.findElements(By.cssSelector(".platypus .slide figure img"))){
		images<<[description:"",
			thumbnailUrl:"",
			imageUrl:element.getAttribute("src")]
	}
	def videos = [];
	vehicleObject<<[images: images, videos: videos]
}
def json = new groovy.json.JsonBuilder([lastUpdate: new Date().format("yyyy-MM-dd"), vehicles: vehicleObjects])
def file = new File("./export/car/maserati-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
