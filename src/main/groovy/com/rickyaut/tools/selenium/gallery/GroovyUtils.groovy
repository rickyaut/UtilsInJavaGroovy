package com.rickyaut.tools.selenium.gallery

import groovy.json.JsonSlurper

import org.apache.commons.lang.StringUtils

class GroovyUtils {
	static void mergeExistingJSONIntoVehicleObjects(def jsonFileName, def vehicleObjects){
		def file = new File(jsonFileName)
		if(file.exists()){
			def slurper = new JsonSlurper()
			def result = slurper.parseText(new String(file.readBytes()))
			for(def resultVehicle : result.vehicles){
				for(def vehicleObject: vehicleObjects){
					if(StringUtils.equals(resultVehicle.name, vehicleObject.name)){
						if(resultVehicle.images){
							vehicleObject.images = resultVehicle.images;
						}
						if(resultVehicle.exteriorImages){
							vehicleObject.exteriorImages = resultVehicle.exteriorImages;
						}
						if(resultVehicle.interiorImages){
							vehicleObject.interiorImages = resultVehicle.interiorImages;
						}
					}
				}
			}
		}
	}
	
	static void exportToJsonFile(def vehicleObjects, def jsonFileName){
		def json = new groovy.json.JsonBuilder([lastUpdate: new Date().format("yyyy-MM-dd"), vehicles: vehicleObjects])
		def file = new File(jsonFileName)
		if(file.exists()){
			file.delete();
		}
		file << json.toPrettyString()
		
	}
}
