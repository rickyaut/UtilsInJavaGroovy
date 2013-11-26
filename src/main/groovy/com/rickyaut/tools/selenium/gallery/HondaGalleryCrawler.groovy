package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

WebDriver driver = new ChromeDriver()
driver.get("http://automobiles.honda.com/all-models.aspx")

List<WebElement> vehicleElements = driver.findElements(By.cssSelector("div#Models .ModelUnitContainer"))
def vehicleObjects = []
for(WebElement vehicleElement: vehicleElements){
	WebElement modelImage = vehicleElement.findElement(By.cssSelector(".ModelImage img"))
	WebElement modelText = vehicleElement.findElement(By.cssSelector(".ModelText")).findElement(By.xpath("a[1]"))
	if(modelImage && modelText){
		def href = modelText.getAttribute("href");
		JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
		
		vehicleObjects<<[name: modelText.getText().trim(),
						url: href,
						thumbnailUrl: modelImage.getAttribute("src")]
	}
} 

for(def vehicleObject : vehicleObjects){
	driver.get(vehicleObject.url)
	driver.findElement(By.cssSelector("#LeftsideNavTop")).findElement(By.linkText("Photos")).click();
	def exteriorImages = []
	def interiorImages = []
	for(WebElement element: driver.findElements(By.cssSelector("#GalleryThumbnails .GalleryThumbnail, #GalleryThumbnails .GalleryThumbnailSelected"))){
		WebElement thumbnailImage = element.findElement(By.tagName("img"));
		thumbnailImage.findElement(By.xpath("ancestor::a[1]")).click();
		sleep(500)
		try{
			WebElement image = driver.findElement(By.cssSelector("#GalleryMain #GalleryMainImage img"))
			exteriorImages<<[description:element.getAttribute("data-description"),
				thumbnailUrl:thumbnailImage.getAttribute("src"),
				imageUrl:image.getAttribute("src")]
		}catch(Exception ex){}
	}
	driver.findElement(By.cssSelector(".GenericTab")).findElement(By.linkText("Interior")).click();
	for(WebElement element: driver.findElements(By.cssSelector("#GalleryThumbnails .GalleryThumbnail, #GalleryThumbnails .GalleryThumbnailSelected"))){
		WebElement thumbnailImage = element.findElement(By.tagName("img"));
		thumbnailImage.findElement(By.xpath("ancestor::a[1]")).click();
		sleep(500)
		try{
			WebElement image = driver.findElement(By.cssSelector("#GalleryMain #GalleryMainImage img"))
			interiorImages<<[description:element.getAttribute("data-description"),
				thumbnailUrl:thumbnailImage.getAttribute("src"),
				imageUrl:image.getAttribute("src")]
		}catch(Exception ex){}
	}
	def videos = [];
	vehicleObject<<[interiorImages: interiorImages, exteriorImages: exteriorImages, videos: videos]
}
def json = new groovy.json.JsonBuilder(vehicleObjects)
def file = new File("./export/honda-gallery.json")
if(file.exists()){
	file.delete();
}
file << json.toPrettyString()
