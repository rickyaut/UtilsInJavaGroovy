package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

import com.rickyaut.tools.common.Utils

WebDriver driver = new ChromeDriver()
driver.get("http://www.bmwusa.com/standard/content/allbmws/allbmwsnew.aspx?Series=1,3,4,5,6,7,X1,X3,X5,X6,Z4,M,BMW%20i3")
sleep(30000)
List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".seriesFullWidth .modelSizeForAll"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	WebElement thumbnail = vehicleElement.findElement(By.cssSelector("a img.imageSizeForAll"));
	vehicleObjects<<[name: Utils.getText(driver, vehicleElement.findElement(By.tagName("h3"))),
					url: vehicleElement.findElement(By.cssSelector(".modelHoverMenu .BmwButtonBlue a")).getAttribute("href"),
					thumbnailUrl: thumbnail.getAttribute("src")]
} 

GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/bmw-gallery.json", vehicleObjects);
for(def vehicleObject : vehicleObjects){
	if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
		updateVehicleImages(driver, vehicleObject)
	}
}
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/bmw-gallery.json")

private updateVehicleImages(WebDriver driver, def vehicleObject) {
	driver.get(vehicleObject.url)
	try{
		driver.findElement(By.linkText("Media Gallery")).click();
		def images = []
		for(WebElement element: driver.findElements(By.cssSelector(".ModelMediaContainer .ModelMedia"))){
			String style = element.getAttribute("style");
			int startIndex = "background-image: url(".length();
			int endIndex = style.indexOf(")")
			String url = style.substring(startIndex, endIndex)
			if(url.endsWith("_@316x146.arox")||url.endsWith("_@652x302.arox")){
				images<<[description:"",
					thumbnailUrl:url.replaceAll("_@652x302.arox", "_@316x146.arox"),
					imageUrl:url.replaceAll("_@316x146.", ".").replaceAll("_@652x302.", ".")]
			}
		}
		def videos = [];
		vehicleObject<<[images: images, videos: videos]
	}catch(Exception ex){
		println vehicleObject.url
		ex.printStackTrace()
	}
	return vehicleObject
}
