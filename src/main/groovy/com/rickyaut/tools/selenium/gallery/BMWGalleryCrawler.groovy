package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

WebDriver driver = new ChromeDriver()
driver.get("http://www.bmwusa.com/standard/content/allbmws/allbmwsnew.aspx?Series=1,3,4,5,6,7,X1,X3,X5,X6,Z4,M,BMW%20i3")
sleep(30000)
List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".seriesFullWidth .modelSizeForAll"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	WebElement thumbnail = vehicleElement.findElement(By.cssSelector("a img.imageSizeForAll"));
	vehicleObjects<<[name: vehicleElement.findElement(By.tagName("h3")).getText(),
					url: vehicleElement.findElement(By.cssSelector(".modelHoverMenu .BmwButtonBlue a")).getAttribute("href"),
					thumbnailUrl: thumbnail.getAttribute("src")]
} 

for(def vehicleObject : vehicleObjects){
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
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/car/bmw-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
