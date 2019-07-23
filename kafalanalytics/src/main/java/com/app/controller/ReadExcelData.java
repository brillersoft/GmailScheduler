package com.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.EmployeePersonalDataDTO;
import com.app.pojo.OrganizationPojo;

public class ReadExcelData {
	public static void main(String args[]) {
		try {
			File file = new File("/home/kafalsoft/Desktop/Clientdemo.xlsx");
			String[] provider_header = { "Client Name", "Client Domain", "Client Country" };

			ArrayList<String> a1 = processfile(file, provider_header);
			System.out.println("hi it sunday-------" + a1);
			ArrayList<OrganizationPojo> a2 = orgmapper(a1, provider_header);
			for (OrganizationPojo temp : a2) {
				System.out.println("a2---" + temp.getOrganizationName());
			}
			ArrayList<OrganizationPojo> a3 = checknullOrganizationmultiple(a2);
			for (OrganizationPojo temp : a3) {
				System.out.println("a3" + temp.getOrganizationName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<String> processfile(File file, String[] provider_header) throws Exception {

		if (checkfilesize(file) > 5) {
			throw new Exception("The size of file is greater than permissible size.");
		}
		if (FilenameUtils.isExtension((file).getName(), "xls")) {
			return ReadExcelData.readXLSFile(file, provider_header);
		} else if (FilenameUtils.isExtension((file).getName(), "xlsx")) {
			return ReadExcelData.readXLSXExcel(file, provider_header);
		} else {
			throw new Exception("The Format is not supported by the application.");
		}
	}

	@SuppressWarnings("resource")
	public static ArrayList<String> readXLSFile(File file, String[] provider_header) throws Exception { // For .xls file
		InputStream ExcelFileToRead = new FileInputStream(file);
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);
		HSSFSheet sheet = wb.getSheetAt(0);
		Iterator<Row> rows = sheet.rowIterator();
		int maxNumOfCells = sheet.getRow(0).getLastCellNum();
		ArrayList<String> sheetdata = new ArrayList<String>();
		while (rows.hasNext()) {
			String cellvalue = new String();
			HSSFRow row = (HSSFRow) rows.next();
			row.getFirstCellNum();
			Iterator<Cell> cells = row.cellIterator();
			ArrayList<HSSFCell> data = new ArrayList<HSSFCell>();
			for (int cellCounter = 0; cellCounter < maxNumOfCells; cellCounter++) { // Loop through cells

				HSSFCell cell;

				if (row.getCell(cellCounter) == null) {
					cell = row.createCell(cellCounter);
				} else {
					cell = row.getCell(cellCounter);
				}

				data.add(cell);

			}

			for (HSSFCell cell : data) {

				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {

					cellvalue = cellvalue + cell.getStringCellValue().trim() + "$";

				} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					cellvalue = cellvalue + "<<<" + "$";
				} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
					cellvalue = cellvalue + ">>>" + "$";
				}

			}

			sheetdata.add(cellvalue);

		}

		wb.close();
		ExcelFileToRead.close();
		return sheetdata;
	}

	@SuppressWarnings("resource")
	public static ArrayList<String> readXLSXExcel(File file, String[] provider_header) throws Exception { // for the
																											// .xlsx
																											// file.

		FileInputStream inputStream = new FileInputStream(file);

		Workbook wb = new XSSFWorkbook(inputStream);
		Sheet sheet = wb.getSheetAt(0);
		int maxNumOfCells = sheet.getRow(0).getLastCellNum();
		Iterator<Row> rows = sheet.rowIterator();

		ArrayList<String> sheetdata = new ArrayList<String>();

		while (rows.hasNext()) {
			String cellvalue = new String();
			XSSFRow row = (XSSFRow) rows.next();
			row.getFirstCellNum();
			Iterator<Cell> cells = row.cellIterator();
			ArrayList<XSSFCell> data = new ArrayList<XSSFCell>();
			for (int cellCounter = 0; cellCounter < maxNumOfCells; cellCounter++) { // Loop through cells

				XSSFCell cell;

				if (row.getCell(cellCounter) == null) {
					cell = row.createCell(cellCounter);
				} else {
					cell = row.getCell(cellCounter);
				}

				data.add(cell);

			}

			for (XSSFCell cell : data) {

				if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {

					cellvalue = cellvalue + cell.getStringCellValue().trim() + "$";

				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
					cellvalue = cellvalue + "<<<" + "$";
				} else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
					cellvalue = cellvalue + ">>>" + "$";
				}

			}

			sheetdata.add(cellvalue);

		}

		wb.close();
		inputStream.close();
		return sheetdata;

	}

	public static List removeblankandnumericfield(List sheetdata) {

		ArrayList<String> junkdata = new ArrayList<String>();

		for (int i = 0; i < sheetdata.size(); i++) {
			String str = (String) sheetdata.get(i);
			if (str.contains("")) { // removing of numeric.
				junkdata.add(str);
			}

		}
		sheetdata.removeAll(junkdata);

		return sheetdata;

	}

	public static boolean checkheader(String str, String[] provider_header) {
		// str= str.replaceAll("&","").replace('$', ',');
		str = str.replace('$', ',');
		String[] checkstr = str.split(",");
		if (Arrays.equals(checkstr, provider_header)) {
			return true;
		} else {
			return false;
		}

	}

	public static double checkfilesize(File file) {
		double size = 0.0D;
		if (file.exists() || file.isFile()) {
			size = (double) file.length() / (1024 * 1024);
		}
		return size;
	}

	public static File fileconverter(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public static ArrayList<OrganizationPojo> orgmapper(ArrayList<String> a1, String[] provider_header)
			throws Exception { // Method to map data for organization.

		if (checkheader(a1.get(0), provider_header)) {

		} else {
			throw new Exception("Header Not Matched of the given file.");
		}

		ArrayList<OrganizationPojo> organizationList = new ArrayList<OrganizationPojo>();

		for (int i = 1; i < a1.size(); i++) {
			String str = a1.get(i);
			str = str.substring(0, str.length() - 1);
			str = str.replace('$', ',');
			String[] arrOfStr = str.split(",");
			OrganizationPojo organizationPojo = new OrganizationPojo();
			organizationPojo.setOrganizationName(arrOfStr[0]);
			organizationPojo.setDomainName(arrOfStr[1]);
			organizationPojo.setCountry(arrOfStr[2]);
			organizationList.add(organizationPojo);

		}
		return organizationList;
	}

	public static ArrayList<EmployeePersonalDataDTO> empmapper(ArrayList<String> a1, String[] provider_header)
			throws Exception { // Method to map data for
		// employee.

		if (checkheader(a1.get(0), provider_header)) {

		} else {
			throw new Exception("Header Not Matched of the given file.");
		}

		ArrayList<EmployeePersonalDataDTO> employeeList = new ArrayList<EmployeePersonalDataDTO>();

		for (int i = 1; i < a1.size(); i++) {
			String str = a1.get(i);
			str = str.substring(0, str.length() - 1);
			str = str.replace('$', ',');
			String[] arrOfStr = str.split(",");
			EmployeePersonalDataDTO employeePersonalDataDTO = new EmployeePersonalDataDTO();
			employeePersonalDataDTO.setEmployeeName(arrOfStr[0]);
			employeePersonalDataDTO.setEmployeeId(arrOfStr[1]);
			employeePersonalDataDTO.setEmailId(arrOfStr[2]);
			employeePersonalDataDTO.setReportTo(arrOfStr[3]);
			employeeList.add(employeePersonalDataDTO);

		}
		return employeeList;
	}

	public static ArrayList<String> checknullEmployees(ArrayList<String> sheetdata) {

		ArrayList<String> junkdata = new ArrayList<String>();

		for (int i = 0; i < sheetdata.size(); i++) {
			String str = sheetdata.get(i);
			if (str.contains(">>>")) { // removing of numeric.
				junkdata.add(str);
			}
			// else if(str.contains("<<<")) { // removing blank.
			// junkdata.add(str);
			// }
		}
		sheetdata.removeAll(junkdata);

		return sheetdata;

	}

	public static ArrayList<OrganizationPojo> checknullOrganization(ArrayList<OrganizationPojo> organizationList) {

		ArrayList<OrganizationPojo> emptylist = new ArrayList<OrganizationPojo>();

		for (OrganizationPojo organizationPojo : organizationList) {
			if (organizationPojo.getOrganizationName().equals("") || organizationPojo.getDomainName().equals("")) { // Country
																													// name
																													// may
																													// be
																													// empty.
				emptylist.add(organizationPojo);
			}
		}
		organizationList.removeAll(emptylist);
		return organizationList;

	}

	public static ArrayList<OrganizationPojo> checknullOrganizationmultiple(
			ArrayList<OrganizationPojo> organizationList) {

		ArrayList<OrganizationPojo> emptylist = new ArrayList<OrganizationPojo>();

		for (OrganizationPojo organizationPojo : organizationList) {
			if (organizationPojo.getOrganizationName().equals("<<<")
					|| organizationPojo.getDomainName().equals("<<<")) { // Country name may be empty.
				emptylist.add(organizationPojo);
			}
			if (organizationPojo.getOrganizationName().equals(">>>")
					|| organizationPojo.getDomainName().equals(">>>")) { // Country name may be empty.
				emptylist.add(organizationPojo);
			}
		}
		organizationList.removeAll(emptylist);
		return organizationList;

	}

	public static ArrayList<EmployeePersonalDataDTO> checknullemployee(ArrayList<EmployeePersonalDataDTO> employeeList) {

		ArrayList<EmployeePersonalDataDTO> emptylist = new ArrayList<EmployeePersonalDataDTO>();

		for (EmployeePersonalDataDTO employeePersonalDataDTO : employeeList) {
			if (employeePersonalDataDTO.getEmployeeName().equals("")
					|| employeePersonalDataDTO.getEmployeeId().equals("")
					|| employeePersonalDataDTO.getEmailId().equals("")) { // Report To id may be null
				
				emptylist.add(employeePersonalDataDTO);
			}
		}
		employeeList.removeAll(emptylist);
		return employeeList;

	}

	public static ArrayList<EmployeePersonalDataDTO>  checknullemployeemultiple(
			ArrayList<EmployeePersonalDataDTO> employeeList) {

		ArrayList<EmployeePersonalDataDTO> emptylist = new ArrayList<EmployeePersonalDataDTO>();

		for (EmployeePersonalDataDTO employeePersonalDataDTO : employeeList) {
			if (employeePersonalDataDTO.getEmployeeName().equals("<<<")
					|| employeePersonalDataDTO.getEmployeeId().equals("<<<")||employeePersonalDataDTO.getEmailId().equals("<<<")) {
				
				// Report to may be empty.
				emptylist.add(employeePersonalDataDTO);
			}
			if (employeePersonalDataDTO.getEmployeeName().equals(">>>")
					|| employeePersonalDataDTO.getEmployeeId().equals(">>>")||employeePersonalDataDTO.getEmailId().equals(">>>")) { // Report to may be empty.
				emptylist.add(employeePersonalDataDTO);
			}
//			if(employeePersonalDataDTO.getEmpReportToId().equals(">>>") || employeePersonalDataDTO.getEmpReportToId().equals("<<<")) {
//				employeePersonalDataDTO.setReportTo("");
//				
//			}
		}
		employeeList.removeAll(emptylist);
		
		for(EmployeePersonalDataDTO e1 : employeeList) {
			if(e1.getReportTo().equals("<<<") || e1.getReportTo().equals(">>>") ) {
				e1.setReportTo("");
			}
		}
		return employeeList;

	}

}
