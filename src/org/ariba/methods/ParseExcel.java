package org.ariba.methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ariba.main.Details;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class ParseExcel {
	
	private XSSFWorkbook wb;
	private XSSFSheet sheet;
	private InputStream input;
	private ArrayList<ArrayList<String>> teams = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> tasks = new ArrayList<ArrayList<String>>();
	String path = "";
	
	public ArrayList<ArrayList<String>> getTeams() {
		return teams;
	}

	public void setTeams(ArrayList<ArrayList<String>> teams) {
		this.teams = teams;
	}

	public ArrayList<ArrayList<String>> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<ArrayList<String>> tasks) {
		this.tasks = tasks;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void parseExcel()
	{
		try {
			input = new FileInputStream(path);
			wb = new XSSFWorkbook(input);
			sheet = wb.getSheetAt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getTeam();
		getTask();
	}
	
	private void getTeam()
	{
		sheet = wb.getSheetAt(2);
		int row = 2;
		int column = 1;
		ArrayList<String> taskrow = new ArrayList<String>();
		int emptyCounter = 0;
		do {
			if (column == 4) {
				column = 1;
				row++;
				emptyCounter = 0;
				teams.add(taskrow);
				taskrow = new ArrayList<String>();
			} else {
				taskrow.add(getCell(row, column));
				column++;
			}
			if (getCell(row, column) == "") 
			{
				emptyCounter++;
			}
		} while (emptyCounter < 3);
//		System.out.println(teams);
	}
	
	private void getTask()
	{
		sheet = wb.getSheetAt(3);
		int row = 3;
		int column = 1;
		ArrayList<String> taskrow = new ArrayList<String>();
		String currentPhase = "";
		int emptyCounter = 0;
		do{
			if(column == 26)
			{
				column = 1;
				row++;
				emptyCounter = 0;
				tasks.add(taskrow);
				taskrow = new ArrayList<String>();
			} else {
				if(column == 1)
				{
					if (getCell(row, column) == "") 
					{
						if(getCell(row,7).equals(""))
						{
							emptyCounter++;
						}
						else
						{
							taskrow.add("Task");
						}
						
					}
					else
					{
						taskrow.add("Phase");
						taskrow.add(getCell(row, column));
						currentPhase = getCell(row,column);
					}
				}
				else if(column == 7 || column == 8 || column == 9 || column == 10 || column == 24)
				{
					if (getCell(row, column) == "") 
					{
						taskrow.add("");
						emptyCounter++;
						
					}
					else
					{
						taskrow.add(getCell(row, column));
					}
					
					if(emptyCounter == 6)
					{
						taskrow.add("");
						emptyCounter++;
					}
					else
					{
						if(column == 24 && currentPhase.equals(""))
						{
							taskrow.add("");
							emptyCounter++;
						}
						else if(column == 24 && !currentPhase.equals(""))
						{
							taskrow.add(currentPhase);
						}
					}
					
					
				}
				column++;
			}
		}while(emptyCounter<7);
//		System.out.println(tasks);
	}
	
	private String getCell(int b, int c) {
		String A = new String();
//		DecimalFormat doubFormat = new DecimalFormat("###");
		DataFormatter formatter = new DataFormatter();
		try {

			Row row = sheet.getRow(b);
			Cell cell = row.getCell(c);
			switch (cell.getCellType()) {
			case 1:
				A = cell.getStringCellValue();
				break;
			case 0:
				if (DateUtil.isCellDateFormatted(cell)) {
					A = cell.getDateCellValue().toString();
				} else if (String.valueOf(cell.getNumericCellValue()).equals("712.0")) {
					A = "712";
				} else {
					A = formatter.formatCellValue(cell);
				}
				break;
			case 4:
				A = String.valueOf(cell.getBooleanCellValue());
				break;
			case 2:
				switch (cell.getCachedFormulaResultType()) {
				case 1:
					A = cell.getRichStringCellValue().getString();
					break;
				case 0:
					if (DateUtil.isCellDateFormatted(cell)) {
						A = cell.getDateCellValue().toString();
					} else {
						A = String.valueOf(cell.getNumericCellValue());
					}
					break;
				case 4:
					A = String.valueOf(cell.getBooleanCellValue());
					break;
				case 3:
					A = "";
				}
				break;
			case 3:
				A = "";
			}
		} catch (Exception e) {
			return "";
		}
		return A;
	}
	
	
	
	
	
	public int getRow(String path,String sheetName,String search,int start,int end) throws Exception{
		String cellData = "";
		FileInputStream input = new FileInputStream(new File(path));
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		XSSFSheet sheet=null;
		if(sheetName.trim().length()>0){
			sheet = workbook.getSheet(sheetName);
		}else{
			sheet = workbook.getSheetAt(0);
		}

		XSSFRow row = null;
		int rows = sheet.getPhysicalNumberOfRows();
		if(end==0||end<0){
			end = rows;
		}
		int cols = sheet.getRow(1).getPhysicalNumberOfCells();
		for(int rowIndex=start;rowIndex<end;rowIndex++){
			row = sheet.getRow(rowIndex);
			for(int colIndex=0;colIndex<cols;colIndex++){
				XSSFCell cell = row.getCell(colIndex);
				cellData = getCellString(cell).trim();
				if(cellData.trim().equals(search)){
//					workbook.close();
					return rowIndex;
				}
			}
		}
//		workbook.close();
		input.close();
		return -1;
	}
	
	public int getColumn(String path,String sheetName,String search,int start) throws Exception{
		String cellData = "";
		FileInputStream input = new FileInputStream(new File(path));
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		XSSFSheet sheet=null;
		if(sheetName.trim().length()>0){
			sheet = workbook.getSheet(sheetName);
		}else{
			sheet = workbook.getSheetAt(0);
		}

		XSSFRow row = null;
		int rows = sheet.getPhysicalNumberOfRows();
		int cols = sheet.getRow(1).getPhysicalNumberOfCells();

		for(int rowIndex=1;rowIndex<rows;rowIndex++){
			row = sheet.getRow(rowIndex);
			for(int colIndex=start;colIndex<cols+1;colIndex++){
				XSSFCell cell = row.getCell(colIndex);
				cellData = getCellString(cell).trim();
				if(cellData.trim().equals(search)){
//					workbook.close();
					return colIndex;
				}
			}
		}
//		workbook.close();
		input.close();
		return -1;
	}
	
	
	public String getCellString(XSSFCell cell){
		int type;
		String output = null;
		if(cell!=null){
			type = cell.getCellType();
			switch(type){
			case 0:
				output = String.valueOf(new BigDecimal(cell.getNumericCellValue()).toPlainString());
				break;
			case 1:
				output = cell.getStringCellValue();
				break;
			case 2:
				output = cell.getCellFormula();
				break;
			case 3:
				output = " ";
				break;
			case 4:
				output = String.valueOf(cell.getBooleanCellValue());
				break;
			case 5:
				output = String.valueOf(cell.getErrorCellString());
				break;
			}
		}else{
			output = " ";
		}

		output = output.replace(";","<semicolon>");
		return output;
	}

	public String getSpecificData(String path,String sheetName,String rowData,String columnName) {
		String cellContent = null;
		try{
			int rowIndex = getRow(path, sheetName,rowData, 2, 0);
			int columnIndex = getColumn(path,sheetName, columnName, 1);
			
			FileInputStream input = new FileInputStream(new File(path));
			XSSFWorkbook workbook = new XSSFWorkbook(input);
			XSSFSheet sheet = null;
			if(sheetName.trim().length()>0){
				sheet = workbook.getSheet(sheetName);
			}else{
				sheet = workbook.getSheetAt(0);
			}
			
			XSSFCell cell = sheet.getRow(rowIndex).getCell(columnIndex);
			cellContent = getCellString(cell);
	//		workbook.close();
			input.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return cellContent;
	}
	
	
	public List<String> getTasksTab(){
		
		List<String> output = new ArrayList<String>();
		try{
			System.setProperty("ROW", "3");//Table start row
			System.setProperty("COLUMN", "2");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Liquid Ariba\\Sample Contract Design Matrix - Copy.xlsx");
			String executionQuery = "Select * from `Tasks Tab`";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){
//				output.add(rs.getField("Phase (Optional)")+"~"+rs.getField("Sub-phase n (Optional)")+"~"+rs.getField("Sub-phase (n+1) (Optional)")+"~"+rs.getField("Phase Description")+"~"+rs.getField("Recurring")+"~"+rs.getField("Recurrence Pattern")+"|"+rs.getField("Task Name")+"|"+rs.getField("Task Description")+"|"+rs.getField("Mandatory")+"|"+rs.getField("Type")+"|"+rs.getField("Milestone")+"|"+rs.getField("Owner")+"|"+rs.getField("Allow Auto Approval")+"|"+rs.getField("Approval Rule Flow Type")+"|"+rs.getField("Approver/Reviewer")+"|"+rs.getField("Observer")+"|"+rs.getField("Repeat for each document draft")+"|"+rs.getField("Recipients")+"|"+rs.getField("Notification Days")+"|"+rs.getField("Frequency")+"|"+rs.getField("Auto Start")+"|"+rs.getField("Requires Manual Completion")+"|"+rs.getField("Associated Documents")+"|"+rs.getField("Predecessor")+"|"+rs.getField("Conditions"));
//				output.add(rs.getField("Phase (Optional)")+"~"+rs.getField("Sub-phase n (Optional)")+"~"+rs.getField("Sub-phase (n+1) (Optional)")+"~"+rs.getField("Phase Description")+"~"+rs.getField("Recurring")+"~"+rs.getField("Recurrence Pattern")+"~"+rs.getField("Task Name")+"~"+rs.getField("Task Description")+"~"+rs.getField("Mandatory")+"~"+rs.getField("Type")+"~"+rs.getField("Milestone")+"~"+rs.getField("Owner")+"~"+rs.getField("Allow Auto Approval")+"~"+rs.getField("Approval Rule Flow Type")+"~"+rs.getField("Approver/Reviewer")+"~"+rs.getField("Observer")+"~"+rs.getField("Repeat for each document draft")+"~"+rs.getField("Recipients")+"~"+rs.getField("Notification Days")+"~"+rs.getField("Frequency")+"~"+rs.getField("Auto Start")+"~"+rs.getField("Requires Manual Completion")+"~"+rs.getField("Associated Documents")+"~"+rs.getField("Predecessor")+"~"+rs.getField("Conditions")+"~"+rs.getField("Signature Provider")+"~"+rs.getField("Signer"));
//				output.add(rs.getField("Phase (Optional)")+"~"+rs.getField("Sub-phase n (Optional)")+"~"+rs.getField("Sub-phase (n+1) (Optional)")+"~"+rs.getField("Phase Description")+"~"+rs.getField("Recurring")+"~"+rs.getField("Recurrence Pattern")+"~"+rs.getField("Task Name")+"~"+rs.getField("Task Description")+"~"+rs.getField("Mandatory")+"~"+rs.getField("Type")+"~"+rs.getField("Milestone")+"~"+rs.getField("Owner")+"~"+rs.getField("Allow Auto Approval")+"~"+rs.getField("Approval Rule Flow Type")+"~"+rs.getField("Approver/Reviewer")+"~"+rs.getField("Observer")+"~"+rs.getField("Repeat for each document draft")+"~"+rs.getField("Recipients")+"~"+rs.getField("Notification Days")+"~"+rs.getField("Frequency")+"~"+rs.getField("Auto Start")+"~"+rs.getField("Requires Manual Completion")+"~"+rs.getField("Associated Documents")+"~"+rs.getField("Predecessor")+"~"+rs.getField("Conditions")+"~"+rs.getField("Signature Provider")+"~"+rs.getField("Signer")+"~"+rs.getField("Rank")+"~"+rs.getField("Action"));
				output.add(rs.getField("Phase (Optional)")+"~"+rs.getField("Sub-phase n (Optional)")+"~"+rs.getField("Sub-phase (n+1) (Optional)")+"~"+rs.getField("Phase Description")+"~"+rs.getField("Recurring")+"~"+rs.getField("Recurrence Pattern")+"~"+rs.getField("Task Name")+"~"+rs.getField("Task Description")+"~"+rs.getField("Mandatory")+"~"+rs.getField("Type")+"~"+rs.getField("Milestone")+"~"+rs.getField("Owner")+"~"+rs.getField("Allow Auto Approval")+"~"+rs.getField("Approval Rule Flow Type")+"~"+rs.getField("Approver/Reviewer")+"~"+rs.getField("Observer")+"~"+rs.getField("Repeat for each document draft")+"~"+rs.getField("Recipients")+"~"+rs.getField("Notification Days")+"~"+rs.getField("Frequency")+"~"+rs.getField("Auto Start")+"~"+rs.getField("Requires Manual Completion")+"~"+rs.getField("Associated Documents")+"~"+rs.getField("Predecessor")+"~"+rs.getField("Conditions")+"~"+rs.getField("Signature Provider")+"~"+rs.getField("Signer")+"~"+rs.getField("Rank"));
			}

			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}
	
		return output;
	}
	
	
	public List<String> getDocumentsTab(){
		
		List<String> output = new ArrayList<String>();
		try{
			System.setProperty("ROW", "2");//Table start row
			System.setProperty("COLUMN", "2");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Test\\Design Matrix - Template.xlsm");
			String executionQuery = "Select * from `Documents Tab`";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){
				if (!rs.getField("Folder Name").isEmpty() || !rs.getField("Document Name").isEmpty()){
					output.add(rs.getField("Folder Name")+"~"+rs.getField("Folder Description")+"~"+rs.getField("Document Name")+"~"+rs.getField("Document Description")+"~"+rs.getField("Type")+"~"+rs.getField("Owner")+"~"+rs.getField("Editors")+"~"+rs.getField("Access Control")+"~"+rs.getField("Is Publish Required")+"~"+rs.getField("Conditions")+"~"+rs.getField("Document Path")+"~"+rs.getField("Document Choice Type")+"~"+rs.getField("Document Choice"));
				}
			}

			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}
	
		return output;
		
	}
	
	
	public List<String> getTeamTab(){
		
		List<String> output = new ArrayList<String>();
		try{
			System.setProperty("ROW", "2");//Table start row
			System.setProperty("COLUMN", "2");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Liquid Ariba\\Sample Contract Design Matrix - Copy.xlsx");
			String executionQuery = "Select * from `Team Tab`";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){
				if (!rs.getField("Project Group").isEmpty()){
					output.add(rs.getField("Project Group")+"~"+rs.getField("Project Roles")+"~"+rs.getField("Can Owner Edit this Project Group")+"~"+rs.getField("Members")+"~"+rs.getField("Conditions"));
				}
			}

			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}
	
		return output;
		
	}
	
	
	public String getEventRules(String field){
		String value = "";
		try{
			System.setProperty("ROW", "2");//Table start row
			System.setProperty("COLUMN", "2");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Lara\\Design Matrix - Template v4.1.RFI.XLSM");
			String executionQuery = "Select * from `Event Rules` where Field = '" + field + "'";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){
//				if (!rs.getField(Details.eventType).isEmpty() || !rs.getField("Visibility").isEmpty()){
					value = rs.getField(Details.eventType) +";"+ rs.getField("Visibility");
//				}
			}
			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}
		
		
		return value;
	}
	
	
	//Sourcing Library
	public List <String> getSourcingLibraryContent(){
		List <String> value = new ArrayList<String>();
		try{
			System.setProperty("ROW", "3");//Table start row
			System.setProperty("COLUMN", "1");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Sourcing Library Test Input.xlsx");
			String executionQuery = "Select * from `Sourcing Library`";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){

				switch (rs.getField("Content")){
				
				case "KPI":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" +
							rs.getField("KPI Type") + "^" + rs.getField("KPI Source") + "^" + rs.getField("Value Type") + "^" + rs.getField("Number of decimal places") + "^" + rs.getField("Acceptable Values") + "^" + rs.getField("Range From") + "^" + rs.getField("Range To") + "^" + rs.getField("Report Metric") + "^" + 
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File"));
					break;
					
				case "Section":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control"));
					break;
					
				case "Table Section":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control"));
					break;
					
				case "Lot":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Commodity (Lot)") + "^" + rs.getField("Lot Type") + "^" + rs.getField("Response Required for this item or lot") + "^" + rs.getField("Apply to All Items and Lots") + "^" + rs.getField("Customized Offline Responses") + "^" + rs.getField("Extended Price (Initial)") + "^" + rs.getField("Extended Price (Historic)") + "^" + rs.getField("Extended Price (Reserve)"));
					break;
					
				case "Line Item":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Commodity (Line Item)") + "^" + rs.getField("Customize Offline Response") + "^" + rs.getField("Response required for this item or lot") + "^" + rs.getField("Apply to all items and lots") + "^" + rs.getField("Unit or Extended Bidding") + "^" + rs.getField("Price/Extended Price (initial)") + "^" + rs.getField("Price/Extended Price (historic)") + "^" + rs.getField("Price/Extended Price (reserve)") + "^" + rs.getField("Quantity"));
					break;
					
				case "Question":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Include in Cost") + "^" + rs.getField("Is this a prerequisite question to continue with the event?") + "^" + rs.getField("Owner must review responses before participants can continue with the event") + "^" + rs.getField("Answer Type") + "^" + rs.getField("Number of Decimal Places") + "^" + rs.getField("Acceptable Values") + "^" + rs.getField("Response Required") + "^" + rs.getField("Hide participant's reponses from each other") + "^" + rs.getField("Participant can add comments and attachments") + "^" + rs.getField("Use participant-specific initial values") + "^" + rs.getField("Customized Offline Response") + "^" + rs.getField("Initial Value") + "^" + rs.getField("Range From") + "^" + rs.getField("Range To") + "^" + rs.getField("Allow participants to specify other value?") + "^" + rs.getField("Allow participants to select multiple values?") + "^" + rs.getField("Value List of Choices") + "^" + rs.getField("Set Default") +
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File"));
					break;
					
				case "Requirement":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File"));
					break;
					
				case "Attachment From Desktop":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Search File") + "^" + rs.getField("Explore File"));
					break;
					
				case "Attachment From Library":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Search File") + "^" + rs.getField("Explore File"));
					break;
					
				case "Cost Terms":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Customized Offline Response"));
					break;
					
				case "Content From Library":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Content from Library") + rs.getField("Add Content From Library") + "^" + rs.getField("Copy Visibility") + "^" + rs.getField("External System") + "^" + rs.getField("All Content") + "^" + rs.getField("Title/Keywords") + "^" + rs.getField("Search Item") + "^" + rs.getField("From") + "^" + rs.getField("Project Type"));
					break;
				
				}
			}
			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}
		
		return value;

	}
	
	
	
	public List <String> getEventContent(){
		List <String> value = new ArrayList<String>();
		try{
			System.setProperty("ROW", "3");//Table start row
			System.setProperty("COLUMN", "1");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Liquid Ariba\\Sample RFI Design Matrix.xlsx");
			String executionQuery = "Select * from `Event Content`";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){

				switch (rs.getField("Content")){
				
				case "Section":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Table Section":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Question":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Include in Cost") + "^" + rs.getField("Is this a prerequisite question to continue with the event?") + "^" + rs.getField("Owner must review responses before participants can continue with the event") + "^" + rs.getField("Answer Type") + "^" + rs.getField("Number of Decimal Places") + "^" + rs.getField("Acceptable Values") + "^" + rs.getField("Response Required") + "^" + rs.getField("Hide participant's reponses from each other") + "^" + rs.getField("Participant can add comments and attachments") + "^" + rs.getField("Use participant-specific initial values") + "^" + rs.getField("Customized Offline Response") + "^" + rs.getField("Initial Value") + "^" + rs.getField("Range From") + "^" + rs.getField("Range To") + "^" + rs.getField("Allow participants to specify other value?") + "^" + rs.getField("Allow participants to select multiple values?") + "^" + rs.getField("Value List of Choices") + "^" + rs.getField("Set Default") + "^" +
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File") + "^" + rs.getField("Sub-Content Name") + "^" + rs.getField("Make this term as read-only?"));
					break;
					
				case "Requirement":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File") + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Attachment":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File") + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Attachment From Desktop":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Attach File"));
					break;
					
				case "Attachment From Library":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Search File") + "^" + rs.getField("Explore File"));
					break;
					
				case "Cost Terms":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Customized Offline Response") + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Content From Library":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + 
							rs.getField("Content from Library") + "^" + rs.getField("Add Content From Library") + "^" + rs.getField("Copy Visibility") + "^" + rs.getField("External System") + "^" + rs.getField("All Content") + "^" + rs.getField("Title/Keywords") + "^" + rs.getField("Search Item") + "^" + rs.getField("From") + "^" + rs.getField("Project Type"));
					break;
				}
			}
			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}
		
		return value;

	}

	public List<String> getEventDefinitions(){
		List <String> value = new ArrayList<String>();
		try{
			System.setProperty("ROW", "3");//Table start row
			System.setProperty("COLUMN", "1");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
			//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Liquid Ariba\\Sample RFI Design Matrix.xlsx");
			String executionQuery = "Select * from `Event Content`";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){
				
				break;
			}
			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}

		return value;
	}
	
	
	
	public List <String> getSourcingLibrary(){
		List <String> value = new ArrayList<String>();
		try{
			System.setProperty("ROW", "3");//Table start row
			System.setProperty("COLUMN", "1");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Test\\Configuration Input File - Test Sourcing Library.xlsm");
			String executionQuery = "Select * from `Sourcing Library`";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){

				switch (rs.getField("Content")){
				
				case "KPI":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("KPI Type") + "^" + rs.getField("KPI Source") + "^" + rs.getField("Value Type") + "^" + rs.getField("Number of decimal places") + "^" + rs.getField("Acceptable Values") + "^" + rs.getField("Range From") + "^" + rs.getField("Range To") + "^" + rs.getField("Report Metric") + "^" + 
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File") + "^" + rs.getField("Sub-Content Name"));
					break;
				
				case "Section":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Table Section":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + rs.getField("Sub-Content Name"));
					break;
				
				case "Lot":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Commodity (Lot)") + "^" + rs.getField("Lot Type") + "^" + rs.getField("Response Required for this item or lot") + "^" + rs.getField("Apply to All Items and Lots") + "^" + rs.getField("Customized Offline Responses") + "^" + rs.getField("Extended Price (Initial)") + "^" + rs.getField("Extended Price (Historic)") + "^" + rs.getField("Extended Price (Reserve)") + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Line Item":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Commodity (Line Item)") + "^" + rs.getField("Customize Offline Response") + "^" + rs.getField("Response required for this item or lot") + "^" + rs.getField("Apply to all items and lots") + "^" + rs.getField("Unit or Extended Bidding") + "^" + rs.getField("Price/Extended Price (initial)") + "^" + rs.getField("Price/Extended Price (historic)") + "^" + rs.getField("Price/Extended Price (reserve)") + "^" + rs.getField("Quantity")  + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Question":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Include in Cost") + "^" + rs.getField("Is this a prerequisite question to continue with the event?") + "^" + rs.getField("Owner must review responses before participants can continue with the event") + "^" + rs.getField("Answer Type") + "^" + rs.getField("Number of Decimal Places") + "^" + rs.getField("Acceptable Values") + "^" + rs.getField("Response Required") + "^" + rs.getField("Hide participant's reponses from each other") + "^" + rs.getField("Participant can add comments and attachments") + "^" + rs.getField("Use participant-specific initial values") + "^" + rs.getField("Customized offline response") + "^" + rs.getField("Initial Value") + "^" + rs.getField("Range from") + "^" + rs.getField("Range to") + "^" + rs.getField("Allow participants to specify other value?") + "^" + rs.getField("Allow participants to select multiple values?") + "^" + rs.getField("Value List of Choices") + "^" + rs.getField("Set Default") + "^" +
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File")  + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Requirement":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File")  + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Attachment":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Attach File") + "^" + rs.getField("Search File") + "^" + rs.getField("Explore File"));
					break;
					
				case "Attachment From Desktop":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Attach File"));
					break;
					
				case "Attachment From Library":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Search File") + "^" + rs.getField("Explore File"));
					break;
					
				case "Cost Terms":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Description") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Customized offline Response")  + "^" + rs.getField("Sub-Content Name"));
					break;
					
				case "Formula":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + rs.getField("Content Name") + "^" + rs.getField("Visible to Participant/Supplier") + "^" + rs.getField("Team Access Control") + "^" + 
							rs.getField("Formula") + "^" + rs.getField("Result Type") + "^" + rs.getField("Number of Decimal Places") + "^" + rs.getField("Response Required?") + "^" + rs.getField("Hide Participant's Responses from each other")  + "^" + rs.getField("Sub-Content Name")); 
					break;
					
				case "Content From Library":
					value.add(rs.getField("Content") + "^" + rs.getField("Parent Content Name") + "^" + 
							rs.getField("Content from Library") + "^" + rs.getField("Add Content From Library") + "^" + rs.getField("Copy Visibility") + "^" + rs.getField("External System") + "^" + rs.getField("All Content") + "^" + rs.getField("Title/Keywords") + "^" + rs.getField("Search Item") + "^" + rs.getField("From") + "^" + rs.getField("Project Type")  + "^" + rs.getField("Content Name")  + "^" + rs.getField("Sub-Content Name"));
					break;
					
				}
			}
			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}
		
		return value;

	}
	
	
	//Add Question
	public List <String> getTemplateQuestions(){
		List <String> value = new ArrayList<String>();
		try{
			System.setProperty("ROW", "3");//Table start row
			System.setProperty("COLUMN", "11");//Table start column
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Details.path);
//			Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Configuration Input File - Lara.xlsm");
			String executionQuery = "Select * from `Conditions Tab`";
			Recordset rs = conn.executeQuery(executionQuery);
			while (rs.next()){
				if (!rs.getField("Answer").isEmpty()){
					value.add(rs.getField("Question") + "^" + rs.getField("Visibility Conditions") + "^" + rs.getField("Answer") + "^" + rs.getField("Defined Condition") + "^" + rs.getField("Default Answer") + "^" + rs.getField("Condition"));
				}
			}
			rs.close();
			conn.close();

		}catch(FilloException e){
			e.printStackTrace();
		}
		
		return value;

	}
	
	

		public List <String> getConditions(){
			List <String> value = new ArrayList<String>();
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
				String executionQuery = "Select * from `Conditions Tab`";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Value").isEmpty()){
						value.add(rs.getField("Name") + "^" + rs.getField("Description") + "^" + rs.getField("Condition") + "^" + rs.getField("Subcondition") + "^" + rs.getField("Category") + "^" + rs.getField("Field") + "^" + rs.getField("Comparison") + "^" + rs.getField("Value"));
					}
				}
				rs.close();
				conn.close();

			}catch(FilloException e){
				e.printStackTrace();
			}
			
			return value;

		}
	
	
		public String getSpecificData(String tab, String field){
			String value = "";
			try{
				System.setProperty("ROW", "2");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Test\\Configuration Input File.xlsm");
				String executionQuery = "Select Value from `"+tab+"` Where Field = '" + field + "'";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					value = rs.getField("Value").trim();
				}
				rs.close();
				conn.close();

			}catch(FilloException e){
				e.printStackTrace();
			}
			
			return value;
			
		}
		
		public String getEventCondition(){
			String value = "";
			try{
				System.setProperty("ROW", "2");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Test\\Configuration Input File - Event Template.xlsm");
				String executionQuery = "Select Conditions from `Documents Tab`";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Conditions").isEmpty()){
						value = rs.getField("Conditions").trim();
					}
				}
				rs.close();
				conn.close();

			}catch(FilloException e){
				e.printStackTrace();
			}
			
			return value;
			
		}
		
		public boolean isDocumentExistInExcel(String folderName, String documentName){
			boolean isDocumentExist = false;
			try{
				System.setProperty("ROW", "2");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQuery = "Select * from `Documents Tab` where `Folder Name` = '"+folderName+"' and `Document Name` = '"+documentName+"'";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Document Name").isEmpty()){
						isDocumentExist = true;
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return isDocumentExist;
			}
			return isDocumentExist;
			
		}
		
		public boolean isDocFolderExistInExcel(String folderName){
			boolean isFolderExist = false;
			try{
				System.setProperty("ROW", "2");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQuery = "Select * from `Documents Tab` where `Folder Name` = '"+folderName+"'";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Folder Name").isEmpty()){
						isFolderExist = true;
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return isFolderExist;
			}
			return isFolderExist;
			
		}
		
		public String getDocumentInExcel(String folderName, String documentName){
			String value = "";
			try{
				System.setProperty("ROW", "2");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQuery = "Select * from `Documents Tab` where `Folder Name` = '"+folderName+"' and `Document Name` = '"+documentName+"'";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Folder Name").isEmpty() || !rs.getField("Document Name").isEmpty()){
						value = rs.getField("Folder Name")+"~"+rs.getField("Folder Description")+"~"+rs.getField("Document Name")+"~"+rs.getField("Document Description")+"~"+rs.getField("Type")+"~"+rs.getField("Owner")+"~"+rs.getField("Editors")+"~"+rs.getField("Access Control")+"~"+rs.getField("Is Publish Required")+"~"+rs.getField("Conditions")+"~"+rs.getField("Document Path")+"~"+rs.getField("Document Choice Type")+"~"+rs.getField("Document Choice");
						break;
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				e.printStackTrace();
			}
			return value;
		}
		
		
		public boolean isPhaseExistInExcel(String phaseName){
			boolean isPhaseExist = false;
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQuery = "Select * from `Tasks Tab` where `Phase (Optional)` = '"+phaseName+"'";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Phase (Optional)").isEmpty()){
						isPhaseExist = true;
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return isPhaseExist;
			}
			return isPhaseExist;
		}
		
		public boolean isSubPhaseExistInExcel(String phaseName, String subPhaseName){
			boolean isPhaseExist = false;
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQuery = "Select * from `Tasks Tab` where `Phase (Optional)`='"+phaseName+"' and `Sub-phase n (Optional)`='"+subPhaseName+"'";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Sub-phase n (Optional)").isEmpty()){
						isPhaseExist = true;
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return isPhaseExist;
			}
			return isPhaseExist;
		}
		
		public String getPhasePhaseBasedOnSubphase(String subphase){
			String phase = "";
			String executionQuery = "";
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
				//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				
				executionQuery = "Select * from `Tasks Tab` where `Sub-phase n (Optional)`='"+subphase+"'";

				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Phase (Optional)").isEmpty()){
						phase = rs.getField("Phase (Optional)");
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return phase;
			}
			return phase;
		}
		
		public boolean isEventPhaseExistInExcel(String phaseName) {
			boolean isPhaseExist = false;
			try{
				
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQueryPhase = "Select * from `Tasks Tab` where `Phase (Optional)` = '"+phaseName+"'";
				Recordset rs = conn.executeQuery(executionQueryPhase);
				if(rs.next()) {
					isPhaseExist = true;
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				
			}
			
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQuerySubPhase1 = "Select * from `Tasks Tab` where  `Sub-phase n (Optional)`='"+phaseName+"'";
				Recordset rs = conn.executeQuery(executionQuerySubPhase1);
				if(rs.next()) {
					isPhaseExist = true;
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				
			}
			return isPhaseExist;
		}
		
		
		
		public boolean isTaskExistInExcel(String phase, String taskName){
			boolean isTaskExist = false;
			String executionQuery = "";
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				
				if (phase.isEmpty()){
					executionQuery = "Select * from `Tasks Tab` where `Task Name`='"+taskName+"'";
				}else{
					executionQuery = "Select * from `Tasks Tab` where `Phase (Optional)` = '"+phase+"' and `Task Name`='"+taskName+"'";
				}
				
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Task Name").isEmpty()){
						isTaskExist = true;
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return isTaskExist;
			}
			return isTaskExist;
		}
		
		public String getPhaseBasedOnTask(String taskName){
			String phase = "";
			String executionQuery = "";
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
				//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				
				executionQuery = "Select * from `Tasks Tab` where `Task Name`='"+taskName+"'";

				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Phase (Optional)").isEmpty()){
						phase = rs.getField("Phase (Optional)");
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return phase;
			}
			return phase;
		}
		
		public String getSubPhaseBasedOnTask(String taskName){
			String phase = "";
			String executionQuery = "";
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
				//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				
				executionQuery = "Select * from `Tasks Tab` where `Task Name`='"+taskName+"'";

				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Sub-phase n (Optional)").isEmpty()){
						phase = rs.getField("Sub-phase n (Optional)");
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return phase;
			}
			return phase;
		}
		
		public String getTaskInExcel(String phase, String taskName){
			String value = "";
			String executionQuery = "";
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "2");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				if (phase.isEmpty()){
					executionQuery = "Select * from `Tasks Tab` where `Task Name`='"+taskName+"'";
				}else{
					executionQuery = "Select * from `Tasks Tab` where `Phase (Optional)` = '"+phase+"' and `Task Name`='"+taskName+"'";
				}
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Task Name").isEmpty()){
						value = rs.getField("Task Name")+"~"+rs.getField("Task Description")+"~"+rs.getField("Type")+"~"+rs.getField("Mandatory")+"~"+rs.getField("Milestone")+"~"+rs.getField("Owner")+"~"+rs.getField("Allow Auto Approval")+"~"+rs.getField("Approval Rule Flow Type")+"~"+rs.getField("Approver/Reviewer")+"~"+rs.getField("Observer")+"~"+rs.getField("Repeat for each document draft")+"~"+rs.getField("Recipients")+"~"+rs.getField("Notification Days")+"~"+rs.getField("Frequency")+"~"+rs.getField("Auto Start")+"~"+rs.getField("Requires Manual Completion")+"~"+rs.getField("Associated Documents")+"~"+rs.getField("Predecessor")+"~"+rs.getField("Conditions")+"~"+rs.getField("Signature Provider")+"~"+rs.getField("Signer")+"~"+rs.getField("Rank");
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				e.printStackTrace();
			}
			return value;
		}
		
		
		public boolean isProjectGroupExistInExcel(String projectGroup){
			boolean isProjectGroupExist = false;
			try{
				System.setProperty("ROW", "2");//Table start row
				System.setProperty("COLUMN", "1");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQuery = "Select * from `Team Tab` where `Project Group` = '"+projectGroup+"'";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Project Group").isEmpty()){
						isProjectGroupExist = true;
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				return isProjectGroupExist;
			}
			return isProjectGroupExist;
		}
		
		public String getProjectGroupInExcel(String projectGroup){
			String value = "";
			try{
				System.setProperty("ROW", "2");//Table start row
				System.setProperty("COLUMN", "1");//Table start column
				Fillo fillo = new Fillo();
				Connection conn = fillo.getConnection(Details.path);
//				Connection conn = fillo.getConnection("C:\\Users\\glenn.a.pesigan\\Desktop\\Design Matrix - Template v2.0.XLSM");
				String executionQuery = "Select * from `Team Tab` where `Project Group` = '"+projectGroup+"'";
				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					if (!rs.getField("Project Group").isEmpty()){
						value = rs.getField("Project Group")+"~"+rs.getField("Project Roles")+"~"+rs.getField("Can Owner Edit this Project Group")+"~"+rs.getField("Members")+"~"+rs.getField("Conditions");
						break;
					}
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				e.printStackTrace();
			}
			return value;
		}


		public boolean isContentExistingInExcel(String contentName) {
			boolean isExisting = false;
			String executionQuery = "";
			try{
				System.setProperty("ROW", "3");//Table start row
				System.setProperty("COLUMN", "1");//Table start column
				Fillo fillo = new Fillo();
				//				Connection conn = fillo.getConnection(Details.path);
				Connection conn = fillo.getConnection("C:\\Users\\jan.dwain.f.domondon\\Documents\\Design Matrix - Template v2.0.XLSM");

				executionQuery = "Select * from `Event Content` where `Content Name`='"+contentName+"'";

				Recordset rs = conn.executeQuery(executionQuery);
				while (rs.next()){
					isExisting = true;
				}
				rs.close();
				conn.close();
			}catch(FilloException e){
				isExisting = false;
			}

			return isExisting;
		}
}

