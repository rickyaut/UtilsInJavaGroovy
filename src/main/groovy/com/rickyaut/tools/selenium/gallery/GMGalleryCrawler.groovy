package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

import com.rickyaut.tools.common.Utils

def vehicleObjects = []
def getBuickGallery(){
	def brandVehicleObjects = []
	WebDriver driver = new ChromeDriver()
	driver.get("http://www.buick.com")
	
	List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".vehicle_teaser"))
	for(WebElement vehicleElement: vehicleElements){
		WebElement anchor = vehicleElement.findElement(By.tagName("a"))
		WebElement image = anchor.findElement(By.tagName("img"))
		brandVehicleObjects<<[name: anchor.getAttribute("title").replaceAll("2014", "").replaceAll("Buick", ""),
						url: anchor.getAttribute("href"),
						thumbnailUrl: image.getAttribute("src")]
	}
	
GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/gm-buick-gallery.json", vehicleObjects);
	for(def vehicleObject : brandVehicleObjects){
		if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
			updateVehicleImages(driver, vehicleObject)
		}
	}
	driver.quit();
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/gm-buick-gallery.json")
}

private updateBuickVehicleImages(WebDriver driver, vehicleObject) {
	try{
		driver.get(vehicleObject.url)
		driver.findElement(By.linkText("Photo Gallery")).click();
		def exteriorImages = []
		for(WebElement element: driver.findElements(By.cssSelector(".content ul.thumbnails li a"))){
			WebElement image = element.findElement(By.tagName("img"));
			exteriorImages<<[description:element.getAttribute("title").replaceAll("|2014 BUICK", "").trim(),
				thumbnailUrl:image.getAttribute("src"),
				imageUrl:element.getAttribute("href")]
		}
		driver.findElement(By.cssSelector(".panel .mod li.nav_page a[title='INTERIOR']")).click();
		def interiorImages = []
		for(WebElement element: driver.findElements(By.cssSelector(".content ul.thumbnails li a"))){
			WebElement image = element.findElement(By.tagName("img"));
			interiorImages<<[description:element.getAttribute("title").replaceAll("|2014 BUICK", "").trim(),
				thumbnailUrl:image.getAttribute("src"),
				imageUrl:element.getAttribute("href")]
		}
		def videos = [];
		vehicleObject<<[exteriorImages: exteriorImages, interiorImages: interiorImages, videos: videos]

	}catch(Exception ex){ex.printStackTrace()}
	return vehicleObject
}

def getChevroletGallery(){
	def brandVehicleObjects = []
	WebDriver driver = new ChromeDriver()
	driver.get("http://www.chevrolet.com/tools/help-choose-vehicles.html")
	
	List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".modVi_hmc_1"))
	for(WebElement vehicleElement: vehicleElements){
		WebElement anchor = vehicleElement.findElement(By.tagName("a"))
		WebElement image = anchor.findElement(By.cssSelector("img.size_2"))
		brandVehicleObjects<<[name: vehicleElement.findElement(By.cssSelector(".content h3 a")).getText(),
						url: anchor.getAttribute("href"),
						thumbnailUrl: image.getAttribute("src")]
	}
	
GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/gm-chevrolet-gallery.json", vehicleObjects);
	for(def vehicleObject : brandVehicleObjects){
		if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
			updateVehicleImages(driver, vehicleObject)
		}
	}
	driver.quit();
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/gm-chevrolet-gallery.json")
}

private updateChevoletVehicleImages(WebDriver driver, vehicleObject) {
	try{
		driver.get(vehicleObject.url)
		driver.findElement(By.linkText("Photos & Videos")).click();
		def exteriorImages = []
		for(WebElement element: driver.findElements(By.cssSelector(".content ul.thumbnails li a"))){
			WebElement image = element.findElement(By.tagName("img"));
			exteriorImages<<[description:element.getAttribute("title"),
				thumbnailUrl:image.getAttribute("src"),
				imageUrl:element.getAttribute("href")]
		}
		driver.findElement(By.cssSelector(".panel .mod li.nav_page a[title='Interior']")).click();
		def interiorImages = []
		for(WebElement element: driver.findElements(By.cssSelector(".content ul.thumbnails li a"))){
			WebElement image = element.findElement(By.tagName("img"));
			interiorImages<<[description:element.getAttribute("title"),
				thumbnailUrl:image.getAttribute("src"),
				imageUrl:element.getAttribute("href")]
		}
		def videos = [];
		vehicleObject<<[exteriorImages: exteriorImages, interiorImages: interiorImages, videos: videos]

	}catch(Exception ex){
		println vehicleObject.url
		ex.printStackTrace()
	}
	return vehicleObject
}

def getCadillacGallery(){
	def brandVehicleObjects = []
	WebDriver driver = new ChromeDriver()
	driver.get("http://www.cadillac.com/vehicle-lineup.html")
	
	List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".modVi_1"))
	for(WebElement vehicleElement: vehicleElements){
		WebElement anchor = vehicleElement.findElement(By.tagName("a"))
		WebElement image = anchor.findElement(By.cssSelector("img"))
		brandVehicleObjects<<[name: vehicleElement.findElement(By.tagName("h3")).getText(),
						url: anchor.getAttribute("href"),
						thumbnailUrl: image.getAttribute("src")]
	}
	
GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/gm-cadillac-gallery.json", vehicleObjects);
	for(def vehicleObject : brandVehicleObjects){
		if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
			updateVehicleImages(driver, vehicleObject)
		}
	}
	driver.quit();
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/gm-cadillac-gallery.json")
}

private updateCadillacVehicleImages(WebDriver driver, vehicleObject) {
	for(int index=0; index<5; index++){
		try{
			driver.get(vehicleObject.url)
			driver.findElement(By.linkText("PHOTOS & VIDEOS")).click();
			def interiorImages = []
			for(WebElement element: driver.findElements(By.cssSelector(".content .thumbnails ul.gallery li a"))){
				String href = element.getAttribute("href")
				String imageid = href.substring(href.indexOf("#")+1)
				WebElement thumbnail = element.findElement(By.tagName("img"));
				WebElement image = driver.findElement(By.cssSelector("dt#"+imageid+" img"))
				interiorImages<<[description:image.getAttribute("alt"),
					thumbnailUrl:thumbnail.getAttribute("src"),
					imageUrl:image.getAttribute("src")]
			}
			driver.findElement(By.cssSelector(".mod li a[title='Exterior']")).click();
			def exteriorImages = []
			for(WebElement element: driver.findElements(By.cssSelector(".content .thumbnails ul.gallery li a"))){
				String href = element.getAttribute("href")
				String imageid = href.substring(href.indexOf("#")+1)
				WebElement thumbnail = element.findElement(By.tagName("img"));
				WebElement image = driver.findElement(By.cssSelector("dt#"+imageid+" img"))
				exteriorImages<<[description:image.getAttribute("alt"),
					thumbnailUrl:thumbnail.getAttribute("src"),
					imageUrl:image.getAttribute("src")]
			}
			def videos = [];
			vehicleObject<<[exteriorImages: exteriorImages, interiorImages: interiorImages, videos: videos]
			break;
		}catch(Exception ex){ex.printStackTrace()}

	}
	return vehicleObject
}

def getGMCGallery(){
	def brandVehicleObjects = []
	WebDriver driver = new ChromeDriver()
	driver.get("http://www.gmc.com/current-vehicles.html")
	
	List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".modVi_1"))
	for(WebElement vehicleElement: vehicleElements){
		WebElement anchor = vehicleElement.findElement(By.tagName("a"))
		WebElement image = anchor.findElement(By.cssSelector("img"))
		brandVehicleObjects<<[name: vehicleElement.findElement(By.tagName("h3")).getText(),
						url: anchor.getAttribute("href"),
						thumbnailUrl: image.getAttribute("src")]
	}
	
GroovyUtils.mergeExistingJSONIntoVehicleObjects("./export/car/gm-gmc-gallery.json", vehicleObjects);
	for(def vehicleObject : brandVehicleObjects){
		if(!vehicleObject.images && !vehicleObject.interiorImages && !vehicleObject.exteriorImages){
			updateVehicleImages(driver, vehicleObject)
		}
	}
	driver.quit();
GroovyUtils.exportToJsonFile(vehicleObjects, "./export/car/gm-gmc-gallery.json")
}

private updateGMCVehicleImages(WebDriver driver, vehicleObject) {
	for(int index=0; index<5; index++){
		try{
			driver.get(vehicleObject.url)
			driver.findElement(By.linkText("GALLERY")).click();
			def interiorImages = []
			for(WebElement element: driver.findElements(By.cssSelector(".content .thumbnails ul.gallery li a"))){
				String href = element.getAttribute("href")
				String imageid = href.substring(href.indexOf("#")+1)
				WebElement thumbnail = element.findElement(By.tagName("img"));
				WebElement image = driver.findElement(By.cssSelector("dt#renamed-"+imageid+" img"))
				interiorImages<<[description:image.getAttribute("alt"),
					thumbnailUrl:thumbnail.getAttribute("src"),
					imageUrl:image.getAttribute("src")]
			}
			driver.findElement(By.cssSelector(".mod li a[title='INTERIOR']")).click();
			def exteriorImages = []
			for(WebElement element: driver.findElements(By.cssSelector(".content .thumbnails ul.gallery li a"))){
				String href = element.getAttribute("href")
				String imageid = href.substring(href.indexOf("#")+1)
				WebElement thumbnail = element.findElement(By.tagName("img"));
				WebElement image = driver.findElement(By.cssSelector("dt#renamed-"+imageid+" img"))
				exteriorImages<<[description:image.getAttribute("alt"),
					thumbnailUrl:thumbnail.getAttribute("src"),
					imageUrl:image.getAttribute("src")]
			}
			def videos = [];
			vehicleObject<<[exteriorImages: exteriorImages, interiorImages: interiorImages, videos: videos]
			break;
		}catch(Exception ex){ex.printStackTrace()}

	}
	return vehicleObject
}

//getBuickGallery();
//getChevroletGallery();
//getCadillacGallery();
getGMCGallery();

