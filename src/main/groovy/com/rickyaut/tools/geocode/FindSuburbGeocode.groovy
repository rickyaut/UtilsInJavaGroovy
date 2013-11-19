package com.rickyaut.tools.geocode

import java.util.Properties;

import org.apache.commons.logging.LogFactory
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook

import groovy.json.JsonSlurper
import groovyx.net.http.*

class FindSuburbGeocode{
	def static log = LogFactory.getLog(this)
	public static void main(String[] args){
		InputStream suburbFile = new FileInputStream("import/pc-book_20120202.xls");
		Random rand = new Random();
		Workbook workbook = new HSSFWorkbook(suburbFile);
		Sheet sheet = workbook.getSheet("suburbs");
		def slurper = new JsonSlurper()
		
		try{
			int rowIndex = 0;
			String previousPostcode="";
			BigDecimal lng, lat;
			while(rowIndex++<sheet.getLastRowNum()){
				Row row = sheet.getRow(rowIndex);
				String postcode = getCell(row, 0)?.getStringCellValue();
				String suburb = getCell(row, 1)?.getStringCellValue();
				String state = getCell(row, 2)?.getStringCellValue();
				String country = 'Australia';
				if(!row.getCell(3)?.getNumericCellValue()||!row.getCell(4)?.getNumericCellValue()){
		Properties sysProps = System.getProperties();
		sysProps.put("proxySet", "true");
		sysProps.put("proxyHost", "zapp");
		sysProps.put("proxyPort", "8080");
					def payload = new URL("http://maps.googleapis.com/maps/api/geocode/json?address=$suburb,+$state+$postcode+$country&sensor=false".replaceAll(" ", "+")).text
					def result = slurper.parseText(payload);
					if(result.status=="OK"){
						previousPostcode = postcode;
						lng = result.results.geometry.location.lng[0];
						lat = result.results.geometry.location.lat[0];
						getCell(row, 3).setCellValue(lng);
						getCell(row, 4).setCellValue(lat);
						sleep(rand.nextInt(10000))
					}else if(result.status =="OVER_QUERY_LIMIT"){
						break;
					}
				}
			}
		}finally{
			OutputStream os = new FileOutputStream("export/pc-book_20120202_geocode.xls")
			workbook.write(os)
		}
	}
	
	private static Cell getCell(Row row, int index){
		Cell cell = row.getCell(index)
		if(!cell){
			cell = row.createCell(index)
		}
		return cell;
	}
}
