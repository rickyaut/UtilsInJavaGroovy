package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

WebDriver driver = new ChromeDriver()
driver.get("http://www.bmw.com/com/en/")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector("div.topnavi_layer div.content div.layer_content_models div.model"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	WebElement firstA = vehicleElement.findElement(By.xpath("a[1]"))
	WebElement firstP = vehicleElement.findElement(By.xpath("p[1]"))
	WebElement thumbnailImage = null;
	WebElement innerA = null;
	try{thumbnailImage = firstA?.findElement(By.tagName("img"));}catch(Exception ex){}
	try{innerA = firstP?.findElement(By.tagName("a"));}catch(Exception ex){}
	if(firstA && firstP && thumbnailImage&&innerA){
		def href = firstA?.getAttribute("href");
		JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
		
		vehicleObjects<<[name: jsExecutor.executeScript("return arguments[0].innerHTML", innerA).trim(),
						url: href,
						thumbnailUrl: thumbnailImage.getAttribute("src")]
	}
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	try{
		driver.findElement(By.linkText("Images & videos")).click();
	}catch(Exception ex){
		try{
			driver.findElement(By.linkText("Images and videos")).click();
		}catch(Exception exq){
			continue;
		}
	}
	def images = []
	for(WebElement element: driver.findElements(By.cssSelector("ul.category li"))){
		WebElement image = element.findElement(By.tagName("img"));
		images<<[description:element.getAttribute("data-description"),
			thumbnailUrl:image.getAttribute("src"),
			imageUrl:"http://www.bmw.com/"+element.getAttribute("data-image-medium")?.replaceAll("../", "")]
	}
	for(WebElement element: driver.findElements(By.cssSelector("div.category div.thumbImageBox a"))){
		WebElement thumbnailImage = element.findElement(By.tagName("img"));
		element.click();
		WebElement overlay = driver.findElement(By.cssSelector("#mediaGalleryLightboxLayer"));
		String imageURL = "";
		List<WebElement> linkElements = driver?.findElements(By.cssSelector("#mediaGalleryLightboxLayer .linklist a.standard:active"));
		for(WebElement linkElement : linkElements){
			if(linkElement.getText().contains("1,600") || linkElement.getText().contains("1.600")){
				imageURL = "http://www.bmw.com/"+linkElement.getAttribute("href")?.replaceAll("../", "");
				imageURL = imageURL.indexOf("?")>0?imageURL.substring(0, imageURL.indexOf("?")):imageURL;
			}
		}
		images<<[description:"",
			thumbnailUrl:thumbnailImage.getAttribute("src"),
			imageUrl:imageURL]
		driver.findElement(By.cssSelector("div#mediaGalleryLightboxLayer div#lightbox div#closeButtonLayer")).click();
	}
	def videos = [];
	vehicleObject<<[images: images, videos: videos]
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/bmw-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
