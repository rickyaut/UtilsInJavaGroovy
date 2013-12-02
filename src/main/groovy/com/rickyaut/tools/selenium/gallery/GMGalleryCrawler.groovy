package com.rickyaut.tools.selenium.gallery

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver

def vehicleObjects = []
String getText(WebDriver driver, WebElement element){
	JavascriptExecutor js = (JavascriptExecutor)driver;
	String text = js.executeScript('return $(arguments[0]).text()', element);
	return text;
}

def getBuickGallery(){
	def brandVehicleObjects = []
	WebDriver driver = new ChromeDriver()
	driver.get("http://www.buick.com")
	
	List<WebElement> vehicleElements = driver.findElements(By.cssSelector(".vehicle_teaser"))
	for(WebElement vehicleElement: vehicleElements){
		WebElement anchor = vehicleElement.findElement(By.tagName("a"))
		WebElement image = anchor.findElement(By.tagName("img"))
		brandVehicleObjects<<[name: anchor.getAttribute("title"),
						url: anchor.getAttribute("href"),
						thumbnailUrl: image.getAttribute("src")]
	}
	
	for(def vehicleObject : brandVehicleObjects){
		try{
			driver.get(vehicleObject.url)
			driver.findElement(By.linkText("Photo Gallery")).click();
			def exteriorImages = []
			for(WebElement element: driver.findElements(By.cssSelector(".content ul.thumbnails li a"))){
				WebElement image = element.findElement(By.tagName("img"));
				exteriorImages<<[description:element.getAttribute("title"),
					thumbnailUrl:image.getAttribute("src"),
					imageUrl:element.getAttribute("href")]
			}
			driver.findElement(By.cssSelector(".panel .mod li.nav_page a[title='INTERIOR']")).click();
			def interiorImages = []
			for(WebElement element: driver.findElements(By.cssSelector(".content ul.thumbnails li a"))){
				WebElement image = element.findElement(By.tagName("img"));
				interiorImages<<[description:element.getAttribute("title"),
					thumbnailUrl:image.getAttribute("src"),
					imageUrl:element.getAttribute("href")]
			}
			def videos = [];
			vehicleObject<<[exteriorImages: exteriorImages, interiorImages: interiorImages, videos: videos]
		
		}catch(Exception ex){ex.printStackTrace()}
	}
	driver.quit();
	def json = new groovy.json.JsonBuilder(brandVehicleObjects)
	def file = new File("./export/car/gm-buick-gallery.json")
	if(file.exists()){
		file.delete();
	}
	file << json.toPrettyString()
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
	
	for(def vehicleObject : brandVehicleObjects){
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
	}
	driver.quit();
	def json = new groovy.json.JsonBuilder(brandVehicleObjects)
	def file = new File("./export/car/gm-chevrolet-gallery.json")
	if(file.exists()){
		file.delete();
	}
	file << json.toPrettyString()
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
	
	for(def vehicleObject : brandVehicleObjects){
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
		
		}catch(Exception ex){ex.printStackTrace()}
	}
	driver.quit();
	def json = new groovy.json.JsonBuilder(brandVehicleObjects)
	def file = new File("./export/car/gm-cadillac-gallery.json")
	if(file.exists()){
		file.delete();
	}
	file << json.toPrettyString()
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
	
	for(def vehicleObject : brandVehicleObjects){
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
		
		}catch(Exception ex){ex.printStackTrace()}
	}
	driver.quit();
	def json = new groovy.json.JsonBuilder(brandVehicleObjects)
	def file = new File("./export/car/gm-gmc-gallery.json")
	if(file.exists()){
		file.delete();
	}
	file << json.toPrettyString()
}

getBuickGallery();
getChevroletGallery();
getCadillacGallery();
getGMCGallery();

