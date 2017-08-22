package org.ariba.methods;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ariba.elements.Element;
import org.ariba.main.Details;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Commands {
	
	static int phaseCount = 0;
	
	String server = Details.server;
	String user = Details.user;
	String pass = Details.pass;
	Display display = Details.display;
	Text logging = Details.logging;
	
	public int timeOut;
	
	ArrayList<ArrayList<String>> taskTab = Details.taskTab;
	ArrayList<ArrayList<String>> teamTab = Details.teamTab;
	
	private WebDriver driver;
	
	public Commands(int timeOut){
		this.timeOut = timeOut;
//		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/src/drivers/chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", "drivers/ChromeDriver/chromedriver.exe");
		
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);

		driver.get(server);
		
	}
	
	
	public boolean navigateTab(String tab){
		waitFor(2);
		if (explicitWait(By.xpath("//div[@class='w-page-tabs']//a[text()='"+tab+"']"), 10) != null){
//			click(By.xpath("//div[@class='w-page-tabs']//a[text()='"+tab+"']"));
			sendKeysEnter(By.xpath("//div[@class='w-page-tabs']//a[text()='"+tab+"']"));
			return true;
		}else{
			return false;
		}
	}
	
	public void clickActions(String action){
		click(Element.btnActions);
		click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'"+action+"')]"));
		writeToLogs("Click 'Actions' > '" +action+ "'");
	}
	
	public void clickButton(String button){
		click(By.xpath("//button/span[contains(text(),'"+button+"')]"));
		writeToLogs("Click '" +button+ "' button");
	}
	
	
	/*
	 * Author: Glenn Pesigan
	 * Description: This will create a folder
	 * Parameters: name, description, owner, accessControl
	 * Example: createNewFolder("Test Folder", "Test Description", "", "");
	 */ 
	public void createNewFolder(String name, String description){
		
		writeToLogs("Create New Folder: " + name);
		clickActions("Folder");
		waitFor(2);
		waitForButtonToExist("Create", 5);
		populateTextField("Name", name);
		inputDescription(Element.txtProjectDescription, description);
//		populateChooserField("Owner", owner);
//		populateChooserMultiple("Access Control", accessControl);
		clickButton("Create");
		
	}
	
	
	
	/*
	 * Author: Glenn Pesigan
	 * Description: This will create a phase
	 * Parameters: title, description, rank, recurringSchedule, recurrencePattern, predecessors
	 * Example: createPhase("Test Phase", "Test Description", "", "");
	 */ 
	public void createPhase(String title, String description, String recurringSchedule, String recurrencePattern, String predecessors){
		
		waitFor(2);
		waitForButtonToExist("OK", 5);
		writeToLogs("Create New Phase");
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
//		populateTextField("Rank", rank);
		populateRadioButton("Recurring Schedule", recurringSchedule);
		if (recurringSchedule.equals("Yes")){
			if(!recurrencePattern.isEmpty()){
				String [] rp = recurrencePattern.split("-");
				inputText(By.xpath("//input[@size=5]"), rp[0]);
				inputText(By.xpath("//input[@size=5][2]"), rp[1]);
			}
		}
		
		//Predecessor
		selectPredecessors(predecessors);
		waitFor(2);
		click(Element.btnOK);

	}
	
	
	
	/*
	 * Author: Glenn Pesigan
	 * Description: This will create a new to do task
	 * Parameters: title, description, rank, recurringSchedule, recurrencePattern, predecessors
	 * Example: createPhase("Test Phase", "Test Description", "", "");
	 */ 
	public void createToDoTask(String title, String description, String owner, String observers, String isMilestone, String required, String predecessors, String associatedDocument, String repeat){

		click(Element.lnkCreateToDoTask);
		
		writeToLogs("Create To Do Task");
		
		waitFor(2);
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserField("Owner", owner);
		populateChooserMultipleAlt("Observers", observers);
//		populateTextField("Due Date", dueDate);
		populateRadioButton("Is milestone", isMilestone);
		populateRadioButton("Required", required);
//		populateDropdown("Field Setting", fieldSetting);
		
		//Predecessor
		selectPredecessors(predecessors);
		
//		populateTextField("Rank", rank);
		
		waitFor(2);
		click(Element.btnOK);
		
		if (!associatedDocument.isEmpty()){
			
			waitFor(2);
			sendKeysEnter(By.xpath("//a[contains(.,'"+title+"')]"));
			click(Element.lnkAssociateDocument);
			associateDocument("", associatedDocument);
			waitFor(2);
			populateRadioButton("Repeat for Each Document Draft", repeat);
			waitFor(2);
			click(Element.btnOK);
		}
		
		
		
	}
	
	
	
	/*
	 * Author: Glenn Pesigan
	 * Description: This will create a new to do task
	 * Parameters: title, description, rank, recurringSchedule, recurrencePattern, predecessors
	 * Example: createPhase("Test Phase", "Test Description", "", "");
	 */ 
	public void createNotificationTask(String title, String description, String owner, String recipients, String notificationDays, String notificationFrequency, String autoStart, String manualCompletion, String predecessors, String associatedDocument){
		
		writeToLogs("Create Notification Task");
		
		if (!associatedDocument.isEmpty()){
			
			click(Element.lnkCreateToDoTask);
			waitFor(2);
			click(Element.btnOK);

			waitFor(2);
			
			if(Details.template.equals("Event Template")){
				if (explicitWait(By.className("w-oc-icon-off"), 5) != null){
					List <WebElement> imgExpand = driver.findElements(By.className("w-oc-icon-off"));
					for (WebElement expand : imgExpand){
						expand.click();
						waitFor(2);
					}
				}
			}
			
			sendKeysEnter(By.xpath("//a[contains(.,'New To Do Task')]"));
			click(Element.lnkAssociateDocument);
			associateDocument("Notification", associatedDocument);
			
		}else{
			
			click(Element.lnkNotificationTask);
			
		}
		
		waitFor(2);
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserField("Owner", owner);
		populateChooserMultiple("Recipients", recipients);
		
		if (!notificationDays.isEmpty()){
			String [] notifDays = notificationDays.split("-");
			System.out.println(notifDays[0] + " - " + notifDays[1]);
			populateTextField("Notification Days", notifDays[0]);
			populateDropdown("Notification Days", notifDays[1]);
		}
		
		populateDropdown("Notification Frequency", notificationFrequency);
		waitFor(2);
		populateCheckBox("Should Auto-Start Schedule", autoStart);
		populateCheckBox("Requires Manual Completion", manualCompletion);
		
		//Predecessor
		selectPredecessors(predecessors);
		
//		populateTextField("Rank", rank);
		
		waitFor(1);
		click(Element.btnOK);
		
	}
	
	
	
	public void createReviewTask(String taskType, String title, String description, String owner, String reviewers, String approvalRuleFlow, String observers, String milestone, String required, String repeat, String predecessors, String associatedDocument){
		
		writeToLogs("Create " + taskType + " Task");
		
		if (!associatedDocument.isEmpty()){
			
			click(Element.lnkCreateToDoTask);
			waitFor(2);
			click(Element.btnOK);

			waitFor(2);
			
			if(Details.template.equals("Event Template")){
				if (explicitWait(By.className("w-oc-icon-off"), 5) != null){
					List <WebElement> imgExpand = driver.findElements(By.className("w-oc-icon-off"));
					for (WebElement expand : imgExpand){
						expand.click();
						waitFor(2);
					}
				}
			}
			
			sendKeysEnter(By.xpath("//a[contains(.,'New To Do Task')]"));
			click(Element.lnkAssociateDocument);
			associateDocument(taskType, associatedDocument);
			
		}else{
			
			click(Element.lnkCreateReviewTask);
			
		}

		waitFor(2);
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserField("Owner", owner);
		populateChooserMultiple("Reviewers", reviewers);
		
		//Approval Rule Flow Type
		switch (approvalRuleFlow.toLowerCase()){
		case "parallel":
			click(Element.rdoParallel);
			waitFor(3);
			break;
		case "serial":
			click(Element.rdoSerial);
			waitFor(3);
			break;
		case "custom":
			click(Element.rdoCustom);
			waitFor(3);
			break;
		}
		
		populateChooserMultipleAlt("Observers", observers);
		populateRadioButton("Is milestone", milestone);
		populateRadioButton("Required", required);
		populateRadioButton("Repeat for Each Document Draft", repeat);
		
		
		//Predecessor
		selectPredecessors(predecessors);
		
//		populateTextField("Rank", rank);
		
		waitFor(2);
		click(Element.btnOK);

		
	}
	
	
	
	public void createApprovalTask(String taskType, String title, String description, String owner, String allowAutoApproval, String approvers, String approvalRuleFlow, String observers, String milestone, String required, String repeat, String predecessors, String associatedDocument){

		writeToLogs("Create " + taskType + " Task");
		
		if (!associatedDocument.isEmpty()){
			
			click(Element.lnkCreateToDoTask);
			waitFor(2);
			click(Element.btnOK);

			waitFor(2);
			
			if(Details.template.equals("Event Template")){
				if (explicitWait(By.className("w-oc-icon-off"), 5) != null){
					List <WebElement> imgExpand = driver.findElements(By.className("w-oc-icon-off"));
					for (WebElement expand : imgExpand){
						expand.click();
						waitFor(2);
					}
				}
			}
			
			sendKeysEnter(By.xpath("//a[contains(.,'New To Do Task')]"));
			click(Element.lnkAssociateDocument);
			associateDocument(taskType, associatedDocument);
			
		}else{
			
			click(Element.lnkCreateApprovalTask);
			
		}

		waitFor(2);
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserField("Owner", owner);
		
		populateRadioButton("Allow auto approval", allowAutoApproval);
		
		populateChooserMultipleAlt("Approvers", approvers);
		
		//Approval Rule Flow Type
		switch (approvalRuleFlow.toLowerCase()){
		case "parallel":
			click(Element.rdoParallel);
			waitFor(3);
			break;
		case "serial":
			click(Element.rdoSerial);
			waitFor(3);
			break;
		case "custom":
			click(Element.rdoCustom);
			waitFor(3);
			break;
		}
		
		populateChooserMultipleAlt("Observers", observers);
		populateRadioButton("Is milestone", milestone);
		populateRadioButton("Required", required);
		populateRadioButton("Repeat for Each Document Draft", repeat);
		
		
		//Predecessor
		selectPredecessors(predecessors);
		
//		populateTextField("Rank", rank);
		
		waitFor(2);
		click(Element.btnOK);

	}
	
	
	public void createNegotiationTask(String title, String description, String owner, String reviewers, String approvalRuleFlow, String observers, String milestone, String required, String repeat, String predecessors, String associatedDocument){
		
		writeToLogs("Create Negotiation Task");
		
		if (!associatedDocument.isEmpty()){
			
			click(Element.lnkCreateToDoTask);
			waitFor(2);
			click(Element.btnOK);

			waitFor(2);
			
			if(Details.template.equals("Event Template")){
				if (explicitWait(By.className("w-oc-icon-off"), 5) != null){
					List <WebElement> imgExpand = driver.findElements(By.className("w-oc-icon-off"));
					for (WebElement expand : imgExpand){
						expand.click();
						waitFor(2);
					}
				}
			}
			
			sendKeysEnter(By.xpath("//a[contains(.,'New To Do Task')]"));
			click(Element.lnkAssociateDocument);
			associateDocument("Negotiation", associatedDocument);
			
		}else{
			click(Element.lnkCreateNegotiationTask);
		}

		waitFor(2);
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserField("Owner", owner);
		
		populateChooserMultiple("Reviewers", reviewers);
		
		//Approval Rule Flow Type
		switch (approvalRuleFlow.toLowerCase()){
		case "parallel":
			click(Element.rdoParallel);
			waitFor(3);
			break;
		case "serial":
			click(Element.rdoSerial);
			waitFor(3);
			break;
		case "custom":
			click(Element.rdoCustom);
			waitFor(3);
			break;
		}
		
		populateChooserMultipleAlt("Observers", observers);
		populateRadioButton("Is milestone", milestone);
		populateRadioButton("Required", required);
		populateRadioButton("Repeat for Each Document Draft", repeat);
		
		
		//Predecessor
		selectPredecessors(predecessors);
		
//		populateTextField("Rank", rank);
		waitFor(2);
		click(Element.btnOK);

	}
	
	
	public void createNewDocument(String documentPath, String title, String description, String owner, String isPublishRequired){
		
		click(Element.lnkUploadDocument);
		
		writeToLogs("Create New Document");
		
		uploadFile(documentPath);
		inputDescription(Element.txtProjectDescription, description);
//		populateDropdown("Base Language", baseLanguage);
//		populateDropdown("Use As", useAs);
		populateRadioButton("Is Publish Required", isPublishRequired);
		waitFor(3);
		clickButton("Create");
		
		explicitWait(Element.btnActions, 60);
		clickActions("Edit Attributes");

		explicitWait(By.xpath("//button/span[contains(text(),'Save')]"), 5);
		
		populateTextField("Title", title);
		populateChooserField("Owner", owner);
		waitFor(2);
		clickButton("Save");
		
	}
	
	
	
	
	
	/*
	 * Author: Glenn Pesigan
	 * Description: This will populate the text fields
	 * Parameters: field, value
	 * Example: populateTextField("Name", "Test");
	 */
	
	public void populateTextField(String field, String value)
	{
		try{
			if (!value.isEmpty()){
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				boolean populated = false;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						if (fieldName.equals(field)){
							inputText(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//input[@type='text']"), value.trim());
							writeToLogs(">>" + field + ": " + value);
							populated = true;
							break;
						}
					}
				}
				
				if (populated){
					System.out.println("[PASSED] Input \"" + value + "\" on " + field + " field.");
				}else{
					System.out.println("[FAILED] Unable to populate the field " + field);
				}
			}
		}catch(Exception e){
			writeToLogs("[info] Unable to populate the field " + field);
		}

	}
	
	
	
	public void populateTextArea(String field, String value) {
		try {
			if (!value.isEmpty()) {
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				for (int i = 0; i < totalLabel; i++) {
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()) {
						fieldName = fieldName.substring(0, fieldName.length() - 1).trim();
						if (fieldName.equals(field)) {
							inputText(By.xpath("(//td/label)[" + (i + 1) + "]/../following-sibling::td[2]//textarea"), value.trim());
							writeToLogs(">>" + field + ": " + value);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			writeToLogs("[info] Unable to populate the field " + field);
		}

	}

	
	
	public void populateChooserField(String field, String value) {
		try{
			if (!value.isEmpty()){
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				boolean populated = false;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						if (fieldName.equals(field)){
							
//							click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//a/div/div"));
//							click(Element.lnkSearchMore);
							
							sendKeysEnter(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//input"));
							
							
							inputText(Element.txtSearchField, value.trim());
							click(Element.btnSearchField);
							waitFor(2);
							if (explicitWait(By.xpath("//td[starts-with(.,'"+value.trim()+"')]/../../../../../following-sibling::td//button"), 5) != null){
								click(By.xpath("//td[starts-with(.,'"+value.trim()+"')]/../../../../../following-sibling::td//button"));
								populated = true;
								writeToLogs(">>" + field + ": " + value);
								waitFor(2);
							}else{
								click(Element.btnDoneSearch);
								writeToLogs("[INFO] Cannot find " +value+ " on " + field);
							}
							break;
						}
					}
				}
				
				
				if (populated){
					System.out.println("[PASSED] Select\" " + value + "\" on " + field + " field.");
				}else{
					System.out.println("[FAILED] Unable to populate the field " + field);
				}
				
				
			}
		}catch(Exception e){
			writeToLogs("[INFO] Unable to populate the field " + field);
		}
	}
	
	public void populateChooserMultiple(String field, String value) {
		try{
			if (!value.isEmpty()){
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				boolean populated = false;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						if (fieldName.equals(field)){
							
//							click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//a/div/div"));
//							click(Element.lnkSearchMore);
							
							sendKeysEnter(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//input"));
							
							String [] data = value.split("\\|");
							
							for(String val : data){
								inputText(Element.txtSearchField, val.trim());
								click(Element.btnSearchField);
								waitFor(2);
								if (explicitWait(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1') and contains(.,'"+val.trim()+"')]//td//label"), 5) != null){
									click(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1') and contains(.,'"+val.trim()+"')]//td//label"));
									System.out.println("Select " + val + " on " + field);
									populated = true;
									waitFor(2);
								}else{
									writeToLogs("[INFO] Cannot find " +val+ " on " + field);
								}
							}
							click(Element.btnDoneSearch);
							writeToLogs(">>" + field + ": " + value);
							break;
						}
					}
				}
				
				if (populated){
					System.out.println("[PASSED] Select\" " + value + "\" on " + field + " field.");
				}else{
					System.out.println("[FAILED] Unable to populate the field " + field);
				}
				
			}
		}catch(Exception e){
			writeToLogs("[INFO] Unable to populate the field " + field);
		}
	}
	
	public void populateChooserMultipleAlt(String field, String value) {
		try{
			if (!value.isEmpty()){
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				boolean populated = false;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						if (fieldName.equals(field)){
							
//							click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//a/div/div"));
//							click(Element.lnkSearchMore);
							
							sendKeysEnter(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//input"));
							
							String [] data = value.split("\\|");
							
							for(String val : data){
								inputText(Element.txtSearchField, val.trim());
								click(Element.btnSearchField);
								waitFor(2);
								if (explicitWait(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1')]//a[normalize-space()='"+val.trim()+"']/../../../../../../preceding-sibling::td//label"), 5) != null){
									click(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1')]//a[normalize-space()='"+val.trim()+"']/../../../../../../preceding-sibling::td//label"));
									System.out.println("Select " + val + " on " + field);
									populated = true;
									waitFor(2);
								}else{
									writeToLogs("[INFO] Cannot find " +val+ " on " + field);
								}
							}
							click(Element.btnDoneSearch);
							writeToLogs(">>" + field + ": " + value);
							break;
						}
					}
				}
				
				if (populated){
					System.out.println("[PASSED] Select\" " + value + "\" on " + field + " field.");
				}else{
					System.out.println("[FAILED] Unable to populate the field " + field);
				}
				
			}
		}catch(Exception e){
			writeToLogs("[INFO] Unable to populate the field " + field);
		}
	}
	
	public void populateRadioButton(String field, String value){
		try{
			if (!value.isEmpty()){
				int radioIndex = 0;
				switch (value.toLowerCase().trim()){
				case "yes":
				case "required":
					radioIndex = 1;
					break;
				case "no":
				case "optional":
					radioIndex = 2;
					break;
				}
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				boolean populated = false;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						
						if (fieldName.charAt(fieldName.length() - 1) != '?'){
							fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						}

						if (fieldName.equals(field)){
							click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//span/label["+radioIndex+"]/div"));
							populated = true;
							writeToLogs(">>" + field + ": " + value);
							waitFor(2);
							break;
						}
					}
				}
				
				if (populated){
					System.out.println("[PASSED] Select \"" + value + "\" on " + field + " field.");
				}else{
					System.out.println("[FAILED] Unable to populate the field " + field);
				}
				
			}
		}catch(Exception e){
			writeToLogs("[INFO] Unable to populate the field " + field);
		}

	}
	
	
	public void populateDropdown(String field, String value){
		try{
			if (!value.isEmpty()){
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				boolean populated = false;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						if (fieldName.charAt(fieldName.length() - 1) != '?'){
							fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						}
						if (fieldName.equals(field)){
							click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//span[@class='w-dropdown-pic-ct']"));
							if (explicitWait(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[normalize-space()=\""+value.trim()+"\"]"), 5) != null){
//								click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(.,\""+value.trim()+"\")]"));
								click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[normalize-space()=\""+value.trim()+"\"]"));
								populated = true;
								writeToLogs(">>" + field + ": " + value);
								waitFor(2);
							}
							break;
						}
					}
				}
				
				if (populated){
					System.out.println("[PASSED] Select \"" + value + "\" on " + field + " field.");
				}else{
					System.out.println("[FAILED] Unable to populate the field " + field);
				}
			}
		}catch(Exception e){
			writeToLogs("[INFO] Unable to populate the field " + field);
		}

	}
	
	public void populateDropdownAlt(String field, String value){
		try{
			if (!value.isEmpty()){
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				boolean populated = false;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						if (fieldName.charAt(fieldName.length() - 1) != '?'){
							fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						}
						if (fieldName.equals(field)){
							click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//span[@class='w-dropdown-pic-ct']"));
							click(By.xpath("(//td/label)["+ (i+1) +"]"));
							click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//span[@class='w-dropdown-pic-ct']"));
							if (explicitWait(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[normalize-space()=\""+value.trim()+"\"]"), 5) != null){
								click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[normalize-space()=\""+value.trim()+"\"]"));
								populated = true;
								writeToLogs(">>" + field + ": " + value);
								waitFor(2);
							}
							break;
						}
					}
				}
				
				if (populated){
					System.out.println("[PASSED] Select \"" + value + "\" on " + field + " field.");
				}else{
					System.out.println("[FAILED] Unable to populate the field " + field);
				}
			}
		}catch(Exception e){
			writeToLogs("[INFO] Unable to populate the field " + field);
		}

	}
	
	public void populateCheckBox(String field, String check){
		try{
			if (!check.isEmpty()){
				boolean isChecked = false;
				switch (check){
				case "Yes":
					isChecked = true;
					break;
				case "No":
					isChecked = false;
					break;
				}
				
				if (isChecked){
					int totalLabel = driver.findElements(By.xpath("//td/label")).size();
					String fieldName;
					boolean populated = false;
					for (int i=0;i<totalLabel;i++){
						WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
						fieldName = lblField.getText().trim();
						if (!fieldName.isEmpty()){
							fieldName = fieldName.substring(0, fieldName.length()-1).trim();
							if (fieldName.equals(field)){
								click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//label"));
								populated = true;
								writeToLogs(">>" + field + ": " + check);
								waitFor(2);
								break;
							}
						}
					}
					
					if (populated){
						System.out.println("[PASSED] Tick the checkbox for " + field + " field.");
					}else{
						System.out.println("[FAILED] Unable to check the field " + field);
					}
				}
				
			}
		}catch(Exception e){
			writeToLogs("[INFO] Unable to populate the field " + field);
		}

	}
	
	
	public void selectProjectTypeTemplate(String projectType){
		
		explicitWait(By.xpath("//td[@class='sectionHead' and contains(text(),'"+projectType+"')]/preceding-sibling::td//label"), 5);
		click(By.xpath("//td[@class='sectionHead' and contains(text(),'"+projectType+"')]/preceding-sibling::td//label"));
		System.out.println("Select " + projectType + " template.");
		waitFor(2);
		click(Element.btnOK);
		
	}
	
	
	public void uploadFile(String filePath){
		if (!filePath.isEmpty()){
			short count = 0;
			while (true){
				try{
					Actions action = new Actions(driver);
					WebElement element = driver.findElement(By.xpath("//input[@type='file']"));
					action.moveToElement(element).perform();
					element.sendKeys(filePath);
					writeToLogs(">>Upload File: " + filePath);
					break;
				}catch(Exception e){
					if (count > 3){
						System.out.println("[info] Input text ("+filePath+") failed on element with locator: " + By.xpath("//input[@type='file']").toString());
						throw e;
					}else{
						waitFor(1);
						count++;
					}
					}
				}
		}
	}
	
	
	public void writeToLogs(String message){
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		Display.getDefault().asyncExec(new Runnable()
		{
			@Override
			public void run()
			{
				logging.setText(logging.getText() + "["+dtf.format(now)+"] : " + message + "\r\n");
				logging.setTopIndex(logging.getLineCount() - 1);
			}
		});
	}
	

	//Funtions
	
	public void waitFor(int seconds){
		try{
			Thread.sleep(seconds * 1000);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public WebElement explicitWait(By locator, int seconds){
		try{
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			return element;
		}catch(Exception e){
			System.out.println("[info] Failed to explicitly wait for element with locator: " + locator.toString());
			return null;
		}
	}
	
	public void click(By locator){
		short count = 0;
		while (true){
			try{
				Actions action = new Actions(driver);
				WebElement element = driver.findElement(locator);
				action.moveToElement(element).perform();
				element.click();
				break;
			}catch(Exception e){
				if (count > 5){
					System.out.println("[FAILED] Click failed on element with locator: " + locator.toString());
					throw e;
				}else{
					System.out.println("[TRY: "+count+"] Click failed on element with locator: " + locator.toString());
					waitFor(1);
					count++;
				}
			}
		}
	}
	
	public String getElementText(By locator){
		short count = 0;
		while (true){
			try{
				Actions action = new Actions(driver);
				WebElement element = driver.findElement(locator);
				action.moveToElement(element).perform();
				return element.getText().trim();
			}catch(Exception e){
				if (count > 5){
					System.out.println("[FAILED] Get text on element with locator: " + locator.toString());
					throw e;
				}else{
					System.out.println("[TRY: "+count+"] Get text on element with locator: " + locator.toString());
					waitFor(1);
					count++;
				}
			}
		}
	}
	
	public void clickAlt(By locator){
		short count = 0;
		while (true){
			try{
//				Actions action = new Actions(driver);
				WebElement element = driver.findElement(locator);
//				action.moveToElement(element).perform();
				element.click();
				break;
			}catch(Exception e){
				if (count > 5){
					System.out.println("[FAILED] Click failed on element with locator: " + locator.toString());
					throw e;
				}else{
					System.out.println("[TRY: "+count+"] Click failed on element with locator: " + locator.toString());
					waitFor(1);
					count++;
				}
			}
		}
	}
	
	public void inputText(By locator, String text){
		if (!text.isEmpty()){
			short count = 0;
			while (true){
				try{
					Actions action = new Actions(driver);
					WebElement element = driver.findElement(locator);
					action.moveToElement(element).perform();
					element.clear();
					element.sendKeys(text);
					System.out.println("[PASSED] Input \"" +text+ "\".");
					break;
				}catch(Exception e){
					if (count > 5){
						System.out.println("[FAILED] Input text ("+text+") failed on element with locator: " + locator.toString());
						throw e;
					}else{
						System.out.println("[TRY: "+count+"] Input text ("+text+") failed on element with locator: " + locator.toString());
						waitFor(1);
						count++;
					}
					}
				}
			}
		}
	
	public void inputDescription(By locator, String text){
		if(!text.isEmpty()){
			driver.switchTo().defaultContent();
			explicitWait(locator, 15);
			driver.switchTo().frame(driver.findElement(locator));
			explicitWait(By.tagName("body"),15);
			click(By.tagName("body"));
			inputText(By.tagName("body"),text);
			writeToLogs(">>Description: " + text);
			System.out.println("[PASSED] Input \"" +text+ "\" on Description field.");
			driver.switchTo().defaultContent();
			waitFor(3);
		}
	}
	
	
	public boolean isElementVisible(By by, int timeOutInSeconds){
		driver.manage().timeouts().implicitlyWait(timeOutInSeconds, TimeUnit.SECONDS);
		if(driver.findElements(by).size() > 0){
			driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
			return true;
		}else{
			driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
			return false;
		}
		
	}
	
	
	
	
	
	public void updateTaskTab(){
		
		WebElement pageHead = explicitWait(By.className("w-page-head"), 10);
		String titleName = pageHead.getText().trim();
		if (titleName.length() > 40){
			titleName = titleName.substring(0, 40);
		}
		
		navigateTab("Tasks");
		explicitWait(Element.lblRequiredTasks, 5);
		List <WebElement> row = driver.findElements(By.xpath("//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1]"));
		
		System.out.println("Number of rows in Tasks: " + row.size());
		parseExcel retrieve = new parseExcel();
		String tasksToDelete = "";
		String phaseToDelete = "";
		int rowCount = 0;
		for (int i=1; i<=row.size(); i++){
			WebElement objCheck = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+i+"]//span[contains(@title,'Template')]"),5);
			if (objCheck.getAttribute("title").trim().startsWith("Phase")){
				WebElement objPhaseName = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+i+"]//a[contains(@title,'Applicable:')]"),5);
				String phaseNameUI = objPhaseName.getText().trim();
				System.out.println("i=" + i + " Phase: " + phaseNameUI);
				if (retrieve.isPhaseExistInExcel(phaseNameUI)){
					//Phase
					writeToLogs("Phase: " + phaseNameUI + " exists in Excel");
					sendKeysEnter(By.linkText(phaseNameUI));
					click(Element.lnkOpen);
					writeToLogs("Open " + phaseNameUI + " phase.");
					waitFor(3);
					List <WebElement> insidePhase = driver.findElements(By.xpath("//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1]"));
					System.out.println("Sub Phase tasks row: " + insidePhase.size());
					rowCount = insidePhase.size();
					if (insidePhase.get(0).getText().contains("No items")){
						rowCount = 0;
					}
					String deleteTaskInsidePhase = "";
					String deletePhaseInsidePhase = "";
					for (int j=1; j<=rowCount; j++){
						WebElement objCheck1 = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+j+"]//span[contains(@title,'Template')]"),5);
						if (objCheck1.getAttribute("title").trim().startsWith("Phase")){
							WebElement objSubPhaseName = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+j+"]//a[contains(@title,'Applicable:')]"),5);
							String subPhaseNameUI = objSubPhaseName.getText().trim();
							System.out.println("j=" + j + " Sub-phase: " + subPhaseNameUI);
							if (retrieve.isSubPhaseExistInExcel(phaseNameUI, subPhaseNameUI)){
								//Phase
								writeToLogs("Sub Phase: " + subPhaseNameUI + " exists in Excel");
								sendKeysEnter(By.linkText(subPhaseNameUI));
								click(Element.lnkOpen);
								writeToLogs("Open " + subPhaseNameUI + " sub-phase.");
								waitFor(5);
								List <WebElement> insideSubPhase = driver.findElements(By.xpath("//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1]"));
								rowCount = insideSubPhase.size();
								if (insidePhase.get(0).getText().contains("No items")){
									rowCount = 0;
								}
								String tasksToDeleteInsideSubPhase = "";
								for (int k=1; k<=rowCount; k++){
									WebElement objCheck2 = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+k+"]//span[contains(@title,'Template')]"),5);
									
									if (objCheck2.getAttribute("title").trim().contains("Task")){
										WebElement objTaskName = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+k+"]//a[contains(@title,'Applicable:')]"),5);
										String taskNameUI = objTaskName.getText().replace("*", "").trim();
										System.out.println("k=" + k + " Task: " + taskNameUI);
										if (retrieve.isTaskExistInExcel(phaseNameUI,taskNameUI)){
											//Edit Task
											writeToLogs("Task '" + taskNameUI + "' is exists in excel.");
											editTask(phaseNameUI, taskNameUI);
										}else{
											//delete the task
//											deleteTask(taskNameUI);
											writeToLogs("Task '" + taskNameUI + "' is not exists in excel.");
											System.out.println("For deletion: " + taskNameUI);
											tasksToDeleteInsideSubPhase = "~" + taskNameUI + tasksToDeleteInsideSubPhase;
										}
									}
								}
								
								//Delete Tasks
								if (!tasksToDeleteInsideSubPhase.isEmpty()){
									tasksToDeleteInsideSubPhase = tasksToDeleteInsideSubPhase.substring(1, tasksToDeleteInsideSubPhase.length());
									String [] deleteTask = tasksToDeleteInsideSubPhase.split("~");
									for (String dt : deleteTask){
										deleteTask(dt);
									}
									tasksToDeleteInsideSubPhase = "";
								}
								
								if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
									sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
								}
								waitFor(2);
							}else{
								//Delete Sub-phase
								writeToLogs("Phase '" + subPhaseNameUI + "' is not exists in excel.");
								System.out.println("For deletion: " + subPhaseNameUI);
								deletePhaseInsidePhase = "~" + subPhaseNameUI + deletePhaseInsidePhase;
							}
						}else if (objCheck1.getAttribute("title").trim().contains("Task")){
							//Task
							WebElement objTaskName = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+j+"]//a[contains(@title,'Applicable:')]"),5);
							String taskNameUI = objTaskName.getText().replace("*", "").trim();
							System.out.println("j=" + j + " Task: " + taskNameUI);
							if (retrieve.isTaskExistInExcel(phaseNameUI,taskNameUI)){
								//Edit Task
								editTask(phaseNameUI, taskNameUI);
							}else{
								//delete the task
//								deleteTask(taskNameUI);
								writeToLogs("Task '" + taskNameUI + "' is not exists in excel.");
								System.out.println("For deletion: " + taskNameUI);
								deleteTaskInsidePhase = "~" + taskNameUI + deleteTaskInsidePhase;
							}
							
						}
					}
					
					//Delete Tasks
					if (!deleteTaskInsidePhase.isEmpty()){
						deleteTaskInsidePhase = deleteTaskInsidePhase.substring(1, deleteTaskInsidePhase.length());
						String [] deleteTask = deleteTaskInsidePhase.split("~");
						for (String dt : deleteTask){
							deleteTask(dt);
						}
					}
					
					//Delete Phases
					if (!deletePhaseInsidePhase.isEmpty()){
						deletePhaseInsidePhase = deletePhaseInsidePhase.substring(1, deletePhaseInsidePhase.length());
						String [] deletePhase = deletePhaseInsidePhase.split("~");
						for (String dp : deletePhase){
							deletePhase(dp);
						}
						deletePhaseInsidePhase = "";
					}
					
					if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
						sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
					}
					waitFor(2);
				}else{
					//to delete phase
					writeToLogs("Phase '" + phaseNameUI + "' is not exists in excel.");
					System.out.println("For deletion: " + phaseNameUI);
					phaseToDelete = "~" + phaseNameUI + phaseToDelete;
					
				}
			}else if (objCheck.getAttribute("title").trim().contains("Task")){
				WebElement objTaskName = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+i+"]//a[contains(@title,'Applicable:')]"),5);
				String taskNameUI = objTaskName.getText().replace("*", "").trim();
				System.out.println("i=" + i + " Task: " + taskNameUI);
				if (retrieve.isTaskExistInExcel("",taskNameUI)){
					//Edit Task
					editTask("", taskNameUI);
				}else{
					//delete the task
//					deleteTask(taskNameUI);
					writeToLogs("Task '" + taskNameUI + "' is not exists in excel.");
					System.out.println("For deletion: " + taskNameUI);
					tasksToDelete = "~" + taskNameUI + tasksToDelete;
					
				}
			}
			
		}
		
		//Delete Tasks
		if (!tasksToDelete.isEmpty()){
			tasksToDelete = tasksToDelete.substring(1, tasksToDelete.length());
			String [] deleteTask = tasksToDelete.split("~");
			for (String dt : deleteTask){
				deleteTask(dt);
			}
			tasksToDelete = "";
		}
		
		//Delete Phases
		if (!phaseToDelete.isEmpty()){
			phaseToDelete = phaseToDelete.substring(1, phaseToDelete.length());
			String [] deletePhase = phaseToDelete.split("~");
			for (String dp : deletePhase){
				deletePhase(dp);
			}
			phaseToDelete = "";
		}
		
		
		
		
		if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
			sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
		}
		
		
		
		waitFor(2);
	}
	
	public void deleteTask(String taskNameUI){
		//delete the task
//		writeToLogs("Task '" +taskNameUI+ "' is not exist in excel");
		sendKeysEnter(By.partialLinkText(taskNameUI));
		click(Element.lnkViewTaskDetails);
		waitFor(2);
		if (isElementVisible(Element.btnActions, 5)){
			click(Element.btnActions);
		}else{
			click(Element.lnkTaskActionsForReview);
		}
		click(Element.lnkDelete);
		waitForButtonToExist("OK", 5);
		clickButton("OK");
		writeToLogs("Task '" +taskNameUI+ "' was deleted.");
	}
	
	
	public void deletePhase(String phaseNameUI){
		
		sendKeysEnter(By.linkText(phaseNameUI));
		click(Element.lnkOpen);
		writeToLogs("Open " + phaseNameUI + " phase.");
		waitFor(3);
		List <WebElement> insidePhase = driver.findElements(By.xpath("//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1]"));
		int rowCount = insidePhase.size();
		if (insidePhase.get(0).getText().contains("No items")){
			rowCount = 0;
		}
		String tasksToDeleteInsidePhase = "";
		for (int i=1; i<=rowCount; i++){
			WebElement objCheck2 = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+i+"]//span[contains(@title,'Template')]"),5);
			if (objCheck2.getAttribute("title").trim().contains("Task")){
				WebElement objTaskName = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+i+"]//a[contains(@title,'Applicable:')]"),5);
				String taskNameUI = objTaskName.getText().replace("*", "").trim();
				System.out.println("For deletion: " + taskNameUI);
				tasksToDeleteInsidePhase = "~" + taskNameUI + tasksToDeleteInsidePhase;
			}else if (objCheck2.getAttribute("title").trim().startsWith("Phase")){
				WebElement objSubPhaseName = explicitWait(By.xpath("(//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1])["+i+"]//a[contains(@title,'Applicable:')]"),5);
				String phaseName = objSubPhaseName.getText().trim();
				System.out.println("For deletion: " + phaseName);
				deletePhase(phaseName);
			}
		}
		
		//Delete Tasks
		if (!tasksToDeleteInsidePhase.isEmpty()){
			tasksToDeleteInsidePhase = tasksToDeleteInsidePhase.substring(1, tasksToDeleteInsidePhase.length());
			String [] deleteTask = tasksToDeleteInsidePhase.split("~");
			for (String dt : deleteTask){
				deleteTask(dt);
			}
		}
		
		if (isElementVisible(Element.lnkParentPhase, 5)){
			sendKeysEnter(Element.lnkParentPhase);
			System.out.println("Click Parent Phase.");
		}
		
		sendKeysEnter(By.linkText(phaseNameUI));
		click(Element.lnkPhaseTaskDetails);
		waitFor(2);
		explicitWait(Element.lnkPhaseActions, 5);
		click(Element.lnkPhaseActions);
		click(Element.lnkDelete);
		waitForButtonToExist("OK", 5);
		clickButton("OK");
		writeToLogs("Phase '" +phaseNameUI+ "' was deleted.");
		
	}
	
	
	public void editTask(String phaseNameUI, String taskNameUI){
		
		sendKeysEnter(By.partialLinkText(taskNameUI));
		click(Element.lnkViewTaskDetails);
		explicitWait(Element.lblTaskPageHead, 5);
		writeToLogs("Click '" +taskNameUI+ "' > 'View Task Details'");
		
		String docAssociated = "";
		if (isElementVisible(Element.lnkDocAssociated, 0)){
			docAssociated = getElementText(Element.lnkDocAssociated);
			System.out.println("Document Associated: " + docAssociated);
		}
		
		if (isElementVisible(Element.btnActions, 0)){
			click(Element.btnActions);
		}else{
			click(Element.lnkTaskActionsForReview);
		}
		
		click(Element.lnkEdit);
		writeToLogs("Click 'Actions' > 'Edit'");
		parseExcel retrieve = new parseExcel();
		String [] task = retrieve.getTaskInExcel(phaseNameUI, taskNameUI).split("~", -1);
		String title = task[0].trim();
		String description = task[1].trim();
		String type = task[2].trim();
		String required = task[3].trim();
		String isMilestone = task[4].trim();
		String owner = task[5].trim();
		String autoApproval = task[6].trim();
		String approvalRuleFlow = task[7].trim();
		String approverReviewer = task[8].trim();
		String observers = task[9].trim();
		String repeat = task[10].trim();
		String recipients = task[11].trim();
		String notificationDays = task[12].trim();
		String frequency = task[13].trim();
		String autoStart = task[14].trim();
		String manualCompletion = task[15].trim();
		String associatedDocument = task[16].trim();
		String predecessors = task[17].trim();
		String conditions = task[18].trim();
		String signatureProvider = task[19].trim();
		String signer = task[20].trim();
		String rank = task[21].trim();
		
		waitForButtonToExist("OK", 5);
		waitFor(2);
		populateTextField("Title", title);
		populateChooserField("Owner", owner);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserMultipleAlt("Observers", observers);
//		populateTextField("Due Date", dueDate);
		populateRadioButton("Is milestone", isMilestone);
		populateRadioButton("Required", required);
		populateTextField("Rank", rank);
		selectPredecessors(predecessors);
		populateRadioButton("Repeat for Each Document Draft", repeat);
		populateCondition(Element.lnkCondition, conditions);
		populateTextField("Rank", rank);
		
		switch (type){
		case "Negotiation":
		case "Review":
			populateChooserMultiple("Reviewers", approverReviewer);
			//Approval Rule Flow Type
			if (approvalRuleFlow.isEmpty()){
				switch (approvalRuleFlow.toLowerCase()){
				case "parallel":
					click(Element.rdoParallel);
					waitFor(3);
					break;
				case "serial":
					click(Element.rdoSerial);
					waitFor(3);
					break;
				case "custom":
					click(Element.rdoCustom);
					waitFor(3);
					break;
				}
			}
			break;
		case "Approval":
			populateRadioButton("Allow auto approval", autoApproval);
			populateChooserMultiple("Approvers", approverReviewer);
			//Approval Rule Flow Type
			if (approvalRuleFlow.isEmpty()){
				switch (approvalRuleFlow.toLowerCase()){
				case "parallel":
					click(Element.rdoParallel);
					waitFor(3);
					break;
				case "serial":
					click(Element.rdoSerial);
					waitFor(3);
					break;
				case "custom":
					click(Element.rdoCustom);
					waitFor(3);
					break;
				}
			}
			break;
		case "Notification":
			populateChooserMultiple("Recipients", recipients);
			if (!notificationDays.isEmpty()){
				String [] notifDays = notificationDays.split("-");
				System.out.println(notifDays[0] + " - " + notifDays[1]);
				populateTextField("Notification Days", notifDays[0]);
				populateDropdown("Notification Days", notifDays[1]);
			}
			
			populateDropdown("Notification Frequency", frequency);
			waitFor(2);
			populateCheckBox("Should Auto-Start Schedule", autoStart);
			populateCheckBox("Requires Manual Completion", manualCompletion);
			break;
		}
		
		clickButton("OK");
		
		System.out.println("Associated Document: " + associatedDocument);
		
		if (!associatedDocument.isEmpty()){
			waitFor(2);
			sendKeysEnter(By.partialLinkText(taskNameUI));
			click(Element.lnkViewTaskDetails);
			explicitWait(Element.lblTaskPageHead, 5);
			if (isElementVisible(Element.btnActions, 0)){
				click(Element.btnActions);
			}else{
				click(Element.lnkTaskActionsForReview);
			}
			
			click(Element.lnkAssociateDocument);
			associateDocument(type, associatedDocument);
			
			waitForButtonToExist("Cancel", 5);
			
			if (isElementVisible(Element.btnOK, 0)){
				switch (type){
				case "Negotiation":
				case "Review":
					populateChooserMultiple("Reviewers", approverReviewer);
					//Approval Rule Flow Type
					if (approvalRuleFlow.isEmpty()){
						switch (approvalRuleFlow.toLowerCase()){
						case "parallel":
							click(Element.rdoParallel);
							waitFor(3);
							break;
						case "serial":
							click(Element.rdoSerial);
							waitFor(3);
							break;
						case "custom":
							click(Element.rdoCustom);
							waitFor(3);
							break;
						}
					}
					break;
				case "Approval":
					populateRadioButton("Allow auto approval", autoApproval);
					populateChooserMultiple("Approvers", approverReviewer);
					//Approval Rule Flow Type
					if (approvalRuleFlow.isEmpty()){
						switch (approvalRuleFlow.toLowerCase()){
						case "parallel":
							click(Element.rdoParallel);
							waitFor(3);
							break;
						case "serial":
							click(Element.rdoSerial);
							waitFor(3);
							break;
						case "custom":
							click(Element.rdoCustom);
							waitFor(3);
							break;
						}
					}
					break;
				case "Notification":
					populateChooserMultiple("Recipients", recipients);
					if (!notificationDays.isEmpty()){
						String [] notifDays = notificationDays.split("-");
						System.out.println(notifDays[0] + " - " + notifDays[1]);
						populateTextField("Notification Days", notifDays[0]);
						populateDropdown("Notification Days", notifDays[1]);
					}
					
					populateDropdown("Notification Frequency", frequency);
					waitFor(2);
					populateCheckBox("Should Auto-Start Schedule", autoStart);
					populateCheckBox("Requires Manual Completion", manualCompletion);
					break;
				}
				clickButton("OK");
				waitFor(2);
			}
			clickButton("Cancel");
			
		}else if (type.equals("To Do") && associatedDocument.isEmpty()){
			if (isElementVisible(Element.lnkDocAssociated, 5)){
				waitFor(2);
				sendKeysEnter(By.partialLinkText(taskNameUI));
				click(Element.lnkViewTaskDetails);
				explicitWait(Element.lblTaskPageHead, 5);
				if (isElementVisible(Element.btnActions, 0)){
					click(Element.btnActions);
				}else{
					click(Element.lnkTaskActionsForReview);
				}
				click(Element.lnkAssociateDocument);
				associateDocument(type, "(no value)");
				waitFor(2);
				if (isElementVisible(Element.btnOK, 5)){
					click(Element.btnOK);
				}
				clickButton("Exit");
			}
		}
	}
	
	public void configureTaskTab(){
		
		WebElement pageHead = explicitWait(By.className("w-page-head"), 10);
		String titleName = pageHead.getText().trim();
		if (titleName.length() > 40){
			titleName = titleName.substring(0, 40);
		}
		
		navigateTab("Tasks");
		
		parseExcel retrieve = new parseExcel();
		List <String> tasks = retrieve.getTasksTab();

		for(String t : tasks){
			String [] task = t.split("~", -1);
			String type = task[9].trim();
			String phase = task[0].trim();
			String subPhase1 = task[1].trim();
			String subPhase2 = task[2].trim();
			
			//Tasks
			String title = task[6].trim();
			String description =  task[7].trim();
			String owner = task[11].trim();
			String observers = task[15].trim();
			String isMilestone = task[10].trim();
			String required = task[8].trim();
			String predecessors = task[23].trim();
			String associatedDocument = task[22].replaceAll(".docx", "").trim();
			String conditions = task[24].trim();

			
			//Notification
			String recipients = task[17].trim();
			String notificationDays = task[18].trim();
			String notificationFrequency = task[19].trim();
			String autoStart = task[20].trim();
			String manualCompletion = task[21].trim();
			
			
			//Review
			String reviewers = task[14].trim();
			String approvalRuleFlow = task[13].trim();
			String repeat = task[16].trim();
			
			//Approval
			String allowAutoApproval = task[12].trim();
			
			//Signature
			String signatureProvider = task[25].trim();
			String signer = task[26].trim();
			
			waitFor(2);
			
			if (type.isEmpty()){
				
				if (!phase.isEmpty() && subPhase1.isEmpty()){
					
					if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
						sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
					}

					//Create Phase
					waitFor(2);
					click(Element.btnActions);
					click(Element.lnkCreatePhase);
					createPhase(phase, task[3], task[4], task[5], task[23]);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+phase+"')]/following-sibling::td[4]//a"), conditions);

				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && subPhase2.isEmpty()){
					
					if (!isElementVisible(By.xpath("//div[@class='accentBox bodyBold leg-p-2-5-0-2 flL a-path-node a-path-node-hilite' and contains(text(),'"+phase+"')]"), 5)){
						if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 0)){
							sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
						}
						waitFor(2);
						sendKeysEnter(By.linkText(phase));
						click(Element.lnkOpen);
					}
					
					//Create Sub Phase 1
					waitFor(2);
					click(Element.btnActions);
					click(Element.lnkCreatePhase);
					createPhase(subPhase1, task[3], task[4], task[5], task[23]);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+subPhase1+"')]/following-sibling::td[4]//a"), conditions);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && !subPhase2.isEmpty()){
					
					if (!isElementVisible(By.xpath("//div[@class='accentBox bodyBold leg-p-2-5-0-2 flL a-path-node a-path-node-hilite' and contains(text(),'"+subPhase1+"')]"), 5)){
						if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 0)){
							sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
						}
						waitFor(2);
						sendKeysEnter(By.linkText(phase));
						click(Element.lnkOpen);
						waitFor(2);
						sendKeysEnter(By.linkText(subPhase1));
						click(Element.lnkOpen);
					}
					
					//Create Sub Phase 2
					waitFor(2);
					click(Element.btnActions);
					click(Element.lnkCreatePhase);
					createPhase(subPhase2, task[3], task[4], task[5], task[23]);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+subPhase2+"')]/following-sibling::td[4]//a"), conditions);
				}
			}else{
				
				if (phase.isEmpty()){
					
					if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
						sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
					}
					
					//Create Task outside Phase
					waitFor(2);
					click(Element.btnActions);
					createTask(type, title, description, owner, observers, isMilestone, required, predecessors, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, associatedDocument, reviewers, approvalRuleFlow, repeat, allowAutoApproval, signatureProvider, signer);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+title+"')]/following-sibling::td[4]//a"), conditions);
					
				}else if (!phase.isEmpty() && subPhase1.isEmpty()){
					
					if (!isElementVisible(By.xpath("//div[@class='accentBox bodyBold leg-p-2-5-0-2 flL a-path-node a-path-node-hilite' and contains(text(),'"+phase+"')]"), 5)){
						if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 0)){
							sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
						}
						waitFor(2);
						sendKeysEnter(By.linkText(phase));
						click(Element.lnkOpen);
					}
					
					//Create Task in Phase
					waitFor(2);
					click(Element.btnActions);
					createTask(type, title, description, owner, observers, isMilestone, required, predecessors, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, associatedDocument, reviewers, approvalRuleFlow, repeat, allowAutoApproval, signatureProvider, signer);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+title+"')]/following-sibling::td[4]//a"), conditions);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && subPhase2.isEmpty()){
					
					if (!isElementVisible(By.xpath("//div[@class='accentBox bodyBold leg-p-2-5-0-2 flL a-path-node a-path-node-hilite' and contains(text(),'"+subPhase1+"')]"), 5)){
						if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 0)){
							sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
						}
						waitFor(2);
						sendKeysEnter(By.linkText(phase));
						click(Element.lnkOpen);
						waitFor(2);
						sendKeysEnter(By.linkText(subPhase1));
						click(Element.lnkOpen);
					}
					
					//Create Task in Sub Phase 1
					waitFor(2);
					click(Element.btnActions);
					createTask(type, title, description, owner, observers, isMilestone, required, predecessors, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, associatedDocument, reviewers, approvalRuleFlow, repeat, allowAutoApproval, signatureProvider, signer);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+title+"')]/following-sibling::td[4]//a"), conditions);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && !subPhase2.isEmpty()){
					
					
					if (!isElementVisible(By.xpath("//div[@class='accentBox bodyBold leg-p-2-5-0-2 flL a-path-node a-path-node-hilite' and contains(text(),'"+subPhase1+"')]"), 5)){
						sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
						waitFor(2);
						sendKeysEnter(By.linkText(phase));
						click(Element.lnkOpen);
						waitFor(2);
						sendKeysEnter(By.linkText(subPhase1));
						click(Element.lnkOpen);
						waitFor(2);
						sendKeysEnter(By.linkText(subPhase2));
						click(Element.lnkOpen);
					}
					//Create Task in Sub Phase 2
					waitFor(2);
					click(Element.btnActions);
					createTask(type, title, description, owner, observers, isMilestone, required, predecessors, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, associatedDocument, reviewers, approvalRuleFlow, repeat, allowAutoApproval, signatureProvider, signer);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+title+"')]/following-sibling::td[4]//a"), conditions);
					
				}
				
			}
			
			writeToLogs("");
			
		}
		
	}	
		
	public void createTask(String taskType, String title, String description, String owner, String observers, String isMilestone, String required, String predecessors, String recipients, String notificationDays, String notificationFrequency, String autoStart, String manualCompletion, String associatedDocument, String reviewers, String approvalRuleFlow, String repeat, String allowAutoApproval, String signatureProvider, String signer){
		
		switch (taskType){
		case "To Do":
			createToDoTask(title, description, owner, observers, isMilestone, required, predecessors, associatedDocument, repeat);
			break;
		case "Notification":
			createNotificationTask(title, description, owner, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, predecessors, associatedDocument);
			break;
		case "Review":
			createReviewTask("Review", title, description, owner, reviewers, approvalRuleFlow, observers, isMilestone, required, repeat, predecessors, associatedDocument);
			break;
		case "Review for Team Grading":
			createReviewTask("Review for Team Grading", title, description, owner, reviewers, approvalRuleFlow, observers, isMilestone, required, repeat, predecessors, associatedDocument);
			break;
		case "Negotiation":
			createNegotiationTask(title, description, owner, reviewers, approvalRuleFlow, observers, isMilestone, required, repeat, predecessors, associatedDocument);
			break;
		case "Approval":
			createApprovalTask("Approval", title, description, owner, allowAutoApproval, reviewers, approvalRuleFlow, observers, isMilestone, required, repeat, predecessors, associatedDocument);
			break;
		case "Approval For Publish":
			createApprovalTask("Approval For Publish", title, description, owner, allowAutoApproval, reviewers, approvalRuleFlow, observers, isMilestone, required, repeat, predecessors, associatedDocument);
			break;
		case "Approval For Award":
			createApprovalTask("Approval For Award", title, description, owner, allowAutoApproval, reviewers, approvalRuleFlow, observers, isMilestone, required, repeat, predecessors, associatedDocument);
			break;
		case "Signature":
			createSignatureTask(signatureProvider, signer, title, description, owner, isMilestone, repeat, predecessors, associatedDocument);
			break;
		}
		
		
	}
	
	
	public void selectPredecessors(String predecessors){
		
		if (!predecessors.isEmpty()){
			sendKeysEnter(By.xpath("//td/label[text()='Predecessors:']/../following-sibling::td//a[starts-with(text(),'select')]"));
			explicitWait(Element.imgTableOptions, 5);
			waitFor(2);
			click(Element.imgTableOptions);
			click(Element.lnkExpandAll);
			waitFor(2);
			String [] predecessor = predecessors.split("\\|");
	 		for (String p : predecessor){
	 			click(By.xpath("//td[text()='"+p+"']"));
	 			waitFor(3);
	 		}
	 		writeToLogs(">>Predecessors: " + predecessors);
			click(Element.btnOK);
		}
	}
	
	public void sendKeysEnter(By locator) {
		short count = 0;
		while (true){
			try{
				Actions action = new Actions(driver);
				WebElement element = driver.findElement(locator);
				action.moveToElement(element).perform();
				element.sendKeys(Keys.ENTER);
				break;
			}catch(Exception e){
				if (count > 5){
					System.out.println("[info] Click failed on element with locator: " + locator.toString());
					throw e;
				}else{
					System.out.println("[TRY: "+count+"] Click failed on element with locator: " + locator.toString());
					waitFor(1);
					count++;
				}
			}
		}
		
		
	}
	
	
	
	public void associateDocument(String taskType, String associatedDocument){
		
		explicitWait(Element.btnOK, 10);
		
		if (isElementVisible(By.className("w-oc-icon-off"), 2)){
			List <WebElement> expand = driver.findElements(By.className("w-oc-icon-off"));
			for (WebElement e : expand){
				e.click();
				waitFor(2);
			}
			if (isElementVisible(By.className("w-oc-icon-off"), 1)){
				expand = driver.findElements(By.className("w-oc-icon-off"));
				for (WebElement e : expand){
					e.click();
					waitFor(2);
				}
			}
		}
		writeToLogs(">>Associated Document: " + associatedDocument);
		click(By.xpath("//table[@class='tableBody']//td[contains(normalize-space(),'"+associatedDocument+"')]"));
		waitFor(2);
		
		/*-----------Task Type-------------*/
		
		//Select task type code here...
		click(By.className("w-dropdown-pic-ct"));
		click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(text(),'"+taskType+"')]"));
		waitFor(2);
		click(Element.btnOK);

	}
	
	
	
	
	public void addContent(String content) {
		
		clickButton("Add");
		click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'"+content+"')]"));
		
	}
	
	
	public void createSignatureTask(String signatureProvider, String signer, String title, String description, String owner, String isMilestone, String repeat, String predecessors, String associatedDocument) {
		
		writeToLogs("Create Signature Task");
		
		if (!associatedDocument.isEmpty()){
			
			click(Element.lnkCreateToDoTask);
			waitFor(2);
			click(Element.btnOK);

			waitFor(2);
			sendKeysEnter(By.xpath("//a[contains(.,'New To Do Task')]"));
			click(Element.lnkAssociateDocument);
			associateDocument("Signature", associatedDocument);
			
			
			switch (signatureProvider){
			case "DocuSign":
				click(By.xpath("//td[contains(text(),'DocuSign')]/preceding-sibling::td//label"));
				waitFor(2);
				break;
			case "Paper Signature":
				click(By.xpath("//td[contains(text(),'Paper Signature')]/preceding-sibling::td//label"));
				waitFor(2);
				break;
			}
			
			//Click OK button
			click(By.id("_iye0e"));
			
			
			
		}else{
			
			click(Element.lnkCreateSignatureTask);
			
		}
		
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);

		if (signatureProvider.equals("DocuSign")){
			waitFor(2);
			addSigner(signer);
			waitFor(2);
		}

		populateChooserField("Owner", owner);
		populateRadioButton("Is milestone", isMilestone);
//		populateRadioButton("Required", required);
		populateRadioButton("Repeat for Each Document Draft", repeat);
		waitFor(2);
		
		
		//Predecessor
		selectPredecessors(predecessors);
		
		
		waitFor(2);
		click(Element.btnOK);
		
		if (!associatedDocument.isEmpty()){
			waitFor(2);
			clickButton("Cancel");
			waitFor(2);
			sendKeysEnter(By.xpath("//a[contains(.,'New To Do Task')]"));
			click(Element.lnkViewTaskDetails);
			waitFor(3);
			clickActions("Delete");
			waitFor(2);
			click(Element.btnOK);
		}

	}
	
	
	
	public void addSigner(String value){
		if (!value.isEmpty()){
			
			writeToLogs(">>Add Signers: " + value);
			clickButton("Add Signer");
			click(By.xpath("//div[@class='awmenu w-pm-menu']//span[contains(text(),'Other')]"));
			
			String [] data = value.split("\\|");
			
			for(String val : data){
				inputText(Element.txtSearchField, val);
				click(Element.btnSearchField);
				waitFor(2);
				if (explicitWait(By.xpath("//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"), 5) != null){
					click(By.xpath("//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"));
					waitFor(2);
				}else{
					writeToLogs("[ERROR] Cannot find " +val+ " value for Signers");
				}
			}
			
			clickButton("OK");			
			
		}
	}
	

	
	public void configureDocumentsTab() {
		
		WebElement pageHead = explicitWait(By.className("w-page-head"), 10);
		String titleName = pageHead.getText().trim();
		
		navigateTab("Documents");
		
		parseExcel retrieve = new parseExcel();
		List <String> documents = retrieve.getDocumentsTab();

		for(String d : documents){
			String [] document = d.split("~", -1);
			String folderName = document[0].trim();
			String folderDescription = document[1].trim();
			String documentName = document[2].trim();
			String documentDescription = document[3].trim();
			String type = document[4].trim();
			String owner = document[5].trim();
			String editors = document[6].trim();
			String accessControl = document[7].trim();
			String isPublishRequired = document[8].trim();
			String conditions = document[9].trim();
			String documentPath = document[10].trim();
			String documentChoiceType = document[11].trim();
			String documentChoice = document[12].trim();
			
			if (!folderName.isEmpty()){
				
				if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
					sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
				}
				
				if (!isElementVisible(By.linkText(folderName),5)){
					createNewFolder(folderName, folderDescription);
				}
				
				switch (type){
				
				case "Document":
					if(!documentName.isEmpty()){
						waitFor(2);
						sendKeysEnter(By.linkText(folderName));
						click(Element.lnkOpen);
						waitFor(2);
						click(Element.btnActions);
						createNewDocument(documentPath, documentName, documentDescription, owner, isPublishRequired);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
					}
					break;
					
				case "Contract Terms":
					waitFor(2);
					sendKeysEnter(By.linkText(folderName));
					click(Element.lnkOpen);
					waitFor(2);
					createContractTerms(documentName, documentDescription, owner, editors, accessControl, isPublishRequired);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
					break;
					
				case "Document Choice":
					waitFor(2);
					sendKeysEnter(By.linkText(folderName));
					click(Element.lnkOpen);
					waitFor(2);
					createDocumentChoice(documentName, documentDescription, documentChoiceType, documentChoice);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
					break;
					
				}
			}else{
				
				
				if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
					sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
				}
				
				
				switch (type){
				
				case "Document":
					waitFor(2);
					click(Element.btnActions);
					createNewDocument(documentPath, documentName, documentDescription, owner, isPublishRequired);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
					break;
					
				case "Contract Terms":
					waitFor(2);
					createContractTerms(documentName, documentDescription, owner, editors, accessControl, isPublishRequired);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
					break;
					
				case "Document Choice":
					waitFor(2);
					sendKeysEnter(By.linkText(folderName));
					click(Element.lnkOpen);
					waitFor(2);
					createDocumentChoice(documentName, documentDescription, documentChoiceType, documentChoice);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
					break;
					
				}

				
			}
			
			writeToLogs("");
		}
	}
	
	public void editDocument(String folderName, String documentName){
		
		sendKeysEnter(By.linkText(documentName));
		click(Element.lnkEditAttributes);
		parseExcel retrieve = new parseExcel();
		String [] doc = retrieve.getDocumentInExcel(folderName, documentName).split("~",-1);
		String title = doc[2].trim();
		String description = doc[3].trim();
		String owner = doc[5].trim();
		String editors = doc[6].trim();
		String accessControl = doc[7].trim();
		String isPublishRequired = doc[8].trim();
		String conditions = doc[9].trim();
		
		writeToLogs("Edit '"+documentName+"' document.");
		waitForButtonToExist("Save", 5);
		waitFor(3);
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserField("Owner", owner);
		populateChooserMultipleAlt("Editors", editors);
		populateChooserMultipleAlt("Access Control", accessControl);
		populateRadioButton("Is Publish Required", isPublishRequired);
		populateCondition(Element.lnkCondition, conditions);
		waitFor(3);
		clickButton("Save");
	}
	
	public void deleteDocument(String documentName){
		sendKeysEnter(By.linkText(documentName));
		click(Element.lnkDelete);
		waitFor(2);
		clickButton("OK");
	}
	
	public void updateDocumentsTab(){
		updateDocumentsFromUIToExcel();
		updateDocumentsFromExcelToUI();
	}
	
	public boolean isDocFolderExistInUI(String folderName){	
		boolean isExist = false;
		List<WebElement> rows = driver.findElements(By.xpath("//div[@class='tableBody']//table[@class='tableBody']//tr[contains(@class,'awtDrg_docPanel')]/td[1]//a[@class='hoverArrow hoverLink']"));
		for (int i=1; i<=rows.size(); i++){
			WebElement objDoc = explicitWait(By.xpath("(//div[@class='tableBody']//table[@class='tableBody']//tr[contains(@class,'awtDrg_docPanel')]/td[1]//a[@class='hoverArrow hoverLink'])["+i+"]"), 10);
			if (!objDoc.getAttribute("_mid").contains("Doc")){
				String folderInUI = objDoc.getText().trim();
				if(folderInUI.equals(folderName)){
					return isExist = true;
				}
			}
		}
		return isExist;
	}
	
	public boolean isDocumentExistInUI(String documentName){	
		boolean isExist = false;
		List<WebElement> rows = driver.findElements(By.xpath("//div[@class='tableBody']//table[@class='tableBody']//tr[contains(@class,'awtDrg_docPanel')]/td[1]//a[@class='hoverArrow hoverLink']"));
		for (int i=1; i<=rows.size(); i++){
			WebElement objDoc = explicitWait(By.xpath("(//div[@class='tableBody']//table[@class='tableBody']//tr[contains(@class,'awtDrg_docPanel')]/td[1]//a[@class='hoverArrow hoverLink'])["+i+"]"), 10);
			if (objDoc.getAttribute("_mid").contains("Doc")){
				String docUI = objDoc.getText().trim();
				System.out.println("Get Text: " + docUI);
				if(docUI.equals(documentName)){
					return isExist = true;
				}
			}
		}
		return isExist;
	}
	
	
	public void updateDocumentsFromExcelToUI(){
		
		WebElement pageHead = explicitWait(By.className("w-page-head"), 10);
		String titleName = pageHead.getText().trim();
		
		navigateTab("Documents");
		
		parseExcel retrieve = new parseExcel();
		List <String> documents = retrieve.getDocumentsTab();
		
		for(String d : documents){
			String [] document = d.split("~", -1);
			String folderName = document[0].trim();
			String folderDescription = document[1].trim();
			String documentName = document[2].trim();
			String documentDescription = document[3].trim();
			String type = document[4].trim();
			String owner = document[5].trim();
			String editors = document[6].trim();
			String accessControl = document[7].trim();
			String isPublishRequired = document[8].trim();
			String conditions = document[9].trim();
			String documentPath = document[10].trim();
			String documentChoiceType = document[11].trim();
			String documentChoice = document[12].trim();
			System.out.println("Document: " + documentName);
			if (!folderName.isEmpty()){

				if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
					sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
				}
				
				if (!isDocFolderExistInUI(folderName)){
					//add folder
					createNewFolder(folderName, folderDescription);
					switch (type){
					case "Document":
						if(!documentName.isEmpty()){
							waitFor(2);
							sendKeysEnter(By.linkText(folderName));
							click(Element.lnkOpen);
							waitFor(2);
							click(Element.btnActions);
							createNewDocument(documentPath, documentName, documentDescription, owner, isPublishRequired);
							populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						}
						break;
						
					case "Contract Terms":
						waitFor(2);
						sendKeysEnter(By.linkText(folderName));
						click(Element.lnkOpen);
						waitFor(2);
						createContractTerms(documentName, documentDescription, owner, editors, accessControl, isPublishRequired);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
						
					case "Document Choice":
						waitFor(2);
						sendKeysEnter(By.linkText(folderName));
						click(Element.lnkOpen);
						waitFor(2);
						createDocumentChoice(documentName, documentDescription, documentChoiceType, documentChoice);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
						
					}
				}
				
				
			}else{
				
				
				if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
					sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
				}
				
				if (!isDocumentExistInUI(documentName)){
					//add document
					switch (type){
					case "Document":
						waitFor(2);
						click(Element.btnActions);
						createNewDocument(documentPath, documentName, documentDescription, owner, isPublishRequired);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
						
					case "Contract Terms":
						waitFor(2);
						createContractTerms(documentName, documentDescription, owner, editors, accessControl, isPublishRequired);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
						
					case "Document Choice":
						waitFor(2);
						sendKeysEnter(By.linkText(folderName));
						click(Element.lnkOpen);
						waitFor(2);
						createDocumentChoice(documentName, documentDescription, documentChoiceType, documentChoice);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
					}
				}
				
				

				
			}
			
			writeToLogs("");
		}
		
		
	}
	
	public void addDocumentsFromExcelToUI(){
		
		WebElement pageHead = explicitWait(By.className("w-page-head"), 10);
		String titleName = pageHead.getText().trim();
		
		navigateTab("Documents");
		
		parseExcel retrieve = new parseExcel();
		List <String> documents = retrieve.getDocumentsTab();
		
		for(String d : documents){
			String [] document = d.split("~", -1);
			String folderName = document[0].trim();
			String folderDescription = document[1].trim();
			String documentName = document[2].trim();
			String documentDescription = document[3].trim();
			String type = document[4].trim();
			String owner = document[5].trim();
			String editors = document[6].trim();
			String accessControl = document[7].trim();
			String isPublishRequired = document[8].trim();
			String conditions = document[9].trim();
			String documentPath = document[10].trim();
			String documentChoiceType = document[11].trim();
			String documentChoice = document[12].trim();
			System.out.println("Folder: " + folderName);
			System.out.println("Document: " + documentName);
			if (!folderName.isEmpty()){

				if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
					sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
					waitFor(3);
				}
				
				if (!isDocFolderExistInUI(folderName)){
					//add folder
					System.out.println("Folder '" +folderName+ "' is not exists in UI.");
					createNewFolder(folderName, folderDescription);
					switch (type){
					case "Document":
						if(!documentName.isEmpty()){
							waitFor(2);
							sendKeysEnter(By.linkText(folderName));
							click(Element.lnkOpen);
							waitFor(2);
							click(Element.btnActions);
							createNewDocument(documentPath, documentName, documentDescription, owner, isPublishRequired);
							populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						}
						break;
						
					case "Contract Terms":
						waitFor(2);
						sendKeysEnter(By.linkText(folderName));
						click(Element.lnkOpen);
						waitFor(2);
						createContractTerms(documentName, documentDescription, owner, editors, accessControl, isPublishRequired);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
						
					case "Document Choice":
						waitFor(2);
						sendKeysEnter(By.linkText(folderName));
						click(Element.lnkOpen);
						waitFor(2);
						createDocumentChoice(documentName, documentDescription, documentChoiceType, documentChoice);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
						
					}
				}else{
					//Folder exist in UI
					System.out.println("Folder '" +folderName+ "' is exists in UI.");
					sendKeysEnter(By.linkText(folderName));
					click(Element.lnkOpen);
					waitFor(3);
					if (!isDocumentExistInUI(documentName)){
						//add document
						System.out.println("Document '" +documentName+ "' is not exists in UI.");
						switch (type){
						case "Document":
							waitFor(2);
							click(Element.btnActions);
							createNewDocument(documentPath, documentName, documentDescription, owner, isPublishRequired);
							populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
							break;
							
						case "Contract Terms":
							waitFor(2);
							createContractTerms(documentName, documentDescription, owner, editors, accessControl, isPublishRequired);
							populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
							break;
							
						case "Document Choice":
							waitFor(2);
							sendKeysEnter(By.linkText(folderName));
							click(Element.lnkOpen);
							waitFor(2);
							createDocumentChoice(documentName, documentDescription, documentChoiceType, documentChoice);
							populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
							break;
						}
					}else{
						System.out.println("Document '" +documentName+ "' is exists in UI.");
					}
					
				}
				
				
			}else{
				
				
				if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
					sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
					waitFor(3);
				}
				
				if (!isDocumentExistInUI(documentName)){
					//add document
					System.out.println("Document '" +documentName+ "' is not exists in UI.");
					switch (type){
					case "Document":
						waitFor(2);
						click(Element.btnActions);
						createNewDocument(documentPath, documentName, documentDescription, owner, isPublishRequired);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
						
					case "Contract Terms":
						waitFor(2);
						createContractTerms(documentName, documentDescription, owner, editors, accessControl, isPublishRequired);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
						
					case "Document Choice":
						waitFor(2);
						sendKeysEnter(By.linkText(folderName));
						click(Element.lnkOpen);
						waitFor(2);
						createDocumentChoice(documentName, documentDescription, documentChoiceType, documentChoice);
						populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+documentName+"')]/following-sibling::td[2]//a"), conditions);
						break;
					}
				}else{
					System.out.println("Document '" +documentName+ "' is exists in UI.");
				}
				
				

				
			}
			
			writeToLogs("");
		}
		
		
	}
	
	public void updateDocumentsFromUIToExcel() {
		
		
		WebElement pageHead = explicitWait(By.className("w-page-head"), 10);
		String titleName = pageHead.getText().trim();
		navigateTab("Documents");
		
		parseExcel retrieve = new parseExcel();
		//get the document rows
		List<WebElement> rows = driver.findElements(By.xpath("//div[@class='tableBody']//table[@class='tableBody']//tr[contains(@class,'awtDrg_docPanel')]/td[1]//a[@class='hoverArrow hoverLink']"));
		String forDeletion = "";
		for (int i=1; i<=rows.size(); i++){
			WebElement objDoc = explicitWait(By.xpath("(//div[@class='tableBody']//table[@class='tableBody']//tr[contains(@class,'awtDrg_docPanel')]/td[1]//a[@class='hoverArrow hoverLink'])["+i+"]"), 10);
			if (objDoc.getAttribute("_mid").contains("Doc")){
				//this is document
				String documentName = objDoc.getText().trim();
				if (retrieve.isDocumentExistInExcel("", documentName)){
					editDocument("",documentName);
				}else{
//					deleteDocument(documentName);
					forDeletion = forDeletion + documentName + "~";
					writeToLogs("For deletion: " + documentName);
				}
			}else{
				//this is folder
				String folderName = objDoc.getText().trim();
				if (retrieve.isDocFolderExistInExcel(folderName)){
					sendKeysEnter(By.linkText(folderName));
					click(Element.lnkOpen);
					writeToLogs("Open '" +folderName+ "' folder");
					waitFor(3);
					List<WebElement> doxInsideFolder = driver.findElements(By.xpath("//div[@class='tableBody']//table[@class='tableBody']//tr[contains(@class,'awtDrg_docPanel')]/td[1]//a[@class='hoverArrow hoverLink']"));
					String forDeletion1 = "";
					for (int j=1; j<=doxInsideFolder.size(); j++){
						WebElement objDoc1  = explicitWait(By.xpath("(//div[@class='tableBody']//table[@class='tableBody']//tr[contains(@class,'awtDrg_docPanel')]/td[1]//a[@class='hoverArrow hoverLink'])["+j+"]"), 10);
						if (objDoc1.getAttribute("_mid").contains("Doc")){
							String documentName = objDoc1.getText().trim();
							if (retrieve.isDocumentExistInExcel(folderName, documentName)){
								editDocument(folderName, documentName);
							}else{
//								deleteDocument(documentName);
								forDeletion1 = forDeletion1 + documentName + "~";
								writeToLogs("For deletion: " + documentName);
							}
						}
					}
					if (!forDeletion1.isEmpty()){
						forDeletion1 = forDeletion1.substring(0, forDeletion1.length()-1);
						String [] toDelete = forDeletion1.split("~");
						for (String del : toDelete){
							deleteDocument(del.trim());
						}
					}
					if (!isElementVisible(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node' and contains(text(),'"+titleName+"')]"), 5)){
						sendKeysEnter(By.xpath("//div[@class='leg-p-2-5-0-2 flL a-path-node']/a[contains(text(),'"+titleName+"')]"));
					}
				}
			}
		}
		if (!forDeletion.isEmpty()){
			String [] toDelete = forDeletion.split("~");
			for (String del : toDelete){
				deleteDocument(del.trim());
			}
		}
		writeToLogs("");
	}
	
	
	public void updateTeamTab(boolean quickProject){
		
		if (quickProject){
			sendKeysEnter(Element.lnkPropertiesActions);
			click(Element.lnkEditTeam);
			waitFor(2);
		}else{
			navigateTab("Team");
			waitFor(2);
			clickActions("Edit");
		}
		
		List<WebElement> row = driver.findElements(By.xpath("//div[@class='tableBody']//table[@class='tableBody']//tr[@_awtisprimaryrow='1']/td[2]"));
			parseExcel retrieve = new parseExcel();
			for (int i=1; i<=row.size(); i++){
				WebElement objProjectGroupUI = explicitWait(By.xpath("(//div[@class='tableBody']//table[@class='tableBody']//tr[@_awtisprimaryrow='1']/td[2])["+i+"]"), 5);			
				//if project group exist in excel and ui
				if (objProjectGroupUI.getAttribute("class").contains("normal")){
					String projectGroup = objProjectGroupUI.getText().trim();
					if (retrieve.isProjectGroupExistInExcel(projectGroup)){
						
					}
				//if project group exist in excel file
				}//else if (objProjectGroupUI.getAttribute("").contains("")) {
					
//				} else if (objProjectGroupUI.getAttribute("").contains("")) {
				
					
				//if project group exist in ui but not in excel (for deletion)
//				}
			
			}
		//Test
		
		
	}	
	
	public void configureTeamTab(boolean quickProject){
	
		if (quickProject){
			sendKeysEnter(Element.lnkPropertiesActions);
			click(Element.lnkEditTeam);
			waitFor(2);
		}else{
			navigateTab("Team");
			waitFor(2);
			clickActions("Edit");
		}
		
		parseExcel retrieve = new parseExcel();
		List <String> team = retrieve.getTeamTab();

		for(String t : team){
			String [] tm = t.split("~", -1);
			String projectGroup = tm[0].trim();
			String projectRoles = tm[1].trim();
			String canOwnerEdit = tm[2].trim();
//			String systemGroup = tm[3].trim();
			String members = tm[4].trim();
			String conditions = tm[5].trim();
			
			
			waitFor(2);
			
			
			if(projectGroup.equals("Project Owner")){
				writeToLogs("Team " + projectGroup + " is already added!");
				populateCondition(By.xpath("//span[text()='"+projectGroup+"']/../../../../../../../following-sibling::td//a[contains(text(),'(none)')]"), conditions);
			}else{
				//Click Add Group button
				click(Element.btnAddGroup);
				writeToLogs("Add Group: " + projectGroup);
				//Team Title
				explicitWait(Element.txtGroupTitle, 15);
				inputText(Element.txtGroupTitle, projectGroup);
				
				//Can Edit?
				/*if (!canOwnerEdit.isEmpty()){
					click(Element.drpCanOwnerEdit);
					switch(canOwnerEdit.toLowerCase()){
					case "yes":
						click(Element.optYes);
						break;
					case "no":
						click(Element.optNo);
						break;
					}
					writeToLogs(">>Can Owner Edit this Project Group: " + canOwnerEdit);
					waitFor(2);
				}*/
				populateDropdown("Can owner edit this Project Group", canOwnerEdit);
				/*-----------Select Values for Roles------------*/
				
				
				
				if (!projectRoles.isEmpty()){
					waitFor(2);
					sendKeysEnter(Element.lnkSelectRole);
		
					String [] data = projectRoles.split("\\|");
					for(String val : data){
						inputText(Element.txtSearchField, val);
						click(Element.btnSearchField);
						waitFor(2);
						if (explicitWait(By.xpath("//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"), 5) != null){
							click(By.xpath("//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"));
							waitFor(2);
						}else{
							writeToLogs("[ERROR] Cannot find " +val+ " value for Roles");
						}
					}
					writeToLogs(">>Project Roles: " + projectRoles);
					click(Element.btnDoneSearch);
					waitFor(2);
				}
				
				
				/*-----------Select Values for Roles------------*/
				
				
				click(Element.btnOK);
				
				
				/*-----------Select Values for Members------------*/
				if (!members.isEmpty()){
					
					waitFor(2);
					explicitWait(By.xpath("//span[text()='"+projectGroup+"']/../../../../../../../following-sibling::td//div[@title='Select from the list']"), 5);
					click(By.xpath("//span[text()='"+projectGroup+"']/../../../../../../../following-sibling::td//div[@title='Select from the list']"));
					click(Element.lnkSearchMore);
		
					String [] member = members.split("\\|");
					
					for(String val : member){
						inputText(Element.txtSearchField, val);
						click(Element.btnSearchField);
						waitFor(2);
						if (explicitWait(By.xpath("//div[@class='w-dlg-content']//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"), 5) != null){
							click(By.xpath("//div[@class='w-dlg-content']//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"));
							waitFor(2);
						}else{
							writeToLogs("[INFO] Cannot find " +val+ " value for Signers");
						}
					}
					writeToLogs(">>Members: " + members);
					click(Element.btnDoneSearch);
					waitFor(2);
				}
				/*-----------Select Values for Members------------*/
				
				
				
				/*-----------Select Conditions------------*/
				populateCondition(By.xpath("//span[text()='"+projectGroup+"']/../../../../../../../following-sibling::td//a[contains(text(),'(none)')]"), conditions);
				/*-----------Select Conditions------------*/
	
			}
			
			writeToLogs("");
		}
		
		waitFor(2);
		click(Element.btnOK);

	}

	public void editTeamTab(boolean quickProject){
	

		parseExcel retrieve = new parseExcel();
		List <String> team = retrieve.getTeamTab();
	
		for(String t : team){
			String [] tm = t.split("~", -1);
			String projectGroup = tm[0].trim();
			String projectRoles = tm[1].trim();
//			String canOwnerEdit = tm[2].trim();
	//		String systemGroup = tm[3].trim();
			String members = tm[4].trim();
			String conditions = tm[5].trim();
			
			
			waitFor(2);
		if (!projectGroup.equals("Project Owner")){
			
			click(By.xpath("//a/span[contains(text(),'"+projectGroup+"')]"));
			waitFor(2);
			sendKeysEnter(Element.lnkSelectRole);
			click(By.xpath("//td[@width='40%']//label"));
			waitFor(2);
			String [] data = projectRoles.split("\\|");
			for(String val : data){
				inputText(Element.txtSearchField, val);
				click(Element.btnSearchField);
				waitFor(2);
				if (explicitWait(By.xpath("//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"), 5) != null){
	//				click(By.xpath("//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"));
					waitFor(2);
				}else{
					writeToLogs("[ERROR] Cannot find " +val+ " value for Roles");
				}
			}
			writeToLogs(">>Project Roles: " + projectRoles);
			click(Element.btnDoneSearch);
			waitFor(2);
			
			click(Element.btnOK);
		
		}
		
		if (!members.isEmpty()){
			
			waitFor(2);
			explicitWait(By.xpath("//span[text()='"+projectGroup+"']/../../../../../../../following-sibling::td//div[@title='Select from the list']"), 5);
			click(By.xpath("//span[text()='"+projectGroup+"']/../../../../../../../following-sibling::td//div[@title='Select from the list']"));
			click(Element.lnkSearchMore);
			click(By.xpath("//td[@width='40%']//label"));
			waitFor(2);
			String [] member = members.split("\\|");
			
			for(String val : member){
				inputText(Element.txtSearchField, val);
				click(Element.btnSearchField);
				waitFor(2);
				if (explicitWait(By.xpath("//div[@class='w-dlg-content']//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"), 5) != null){
					click(By.xpath("//div[@class='w-dlg-content']//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"));
					waitFor(2);
				}else{
					writeToLogs("[INFO] Cannot find " +val+ " value for Signers");
				}
			}
			writeToLogs(">>Members: " + members);
			click(Element.btnDoneSearch);
			waitFor(2);
		}
		
		populateCondition(By.xpath("//span[text()='"+projectGroup+"']/../../../../../../../following-sibling::td//a[contains(text(),'(none)')]"), conditions);
		}					
	}


	

	public void configureOverviewTab(String owner, String processStatus, String rank, String accessControl, String conditions, String description){
		
		navigateTab("Overview");
		waitFor(2);
		explicitWait(Element.lnkPropertiesActions, 10);
		sendKeysEnter(Element.lnkPropertiesActions);
		click(Element.lnkEditProperties);
		waitForButtonToExist("Save", 5);
		waitFor(2);
		populateChooserField("Owner", owner);
		populateDropdown("Process Status", processStatus);
		populateTextField("Rank", rank);
		populateChooserMultiple("Access Control", accessControl);
		populateCondition(Element.lnkCondition, conditions);
		waitFor(2);
		clickButton("Save");
		
	}
	
	
	public void createContractTerms(String title, String description, String owner, String editors, String accessControl, String isPublishRequired) {
		
		writeToLogs("Create Contract Terms");
		
		clickActions("Contract Terms");
		waitFor(2);
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserField("Owner", owner);
		populateChooserField("Editors", editors);
		populateChooserMultiple("Access Control", accessControl);
		populateRadioButton("Is Publish Required", isPublishRequired);
		waitFor(2);
		clickButton("Save");
		
	}
	
	
	
	public void createDocumentChoice(String title, String description, String type, String documentChoice) {
		
		writeToLogs("Create Document Choice");
		clickActions("Document Choice");
		waitFor(2);
		populateTextField("Title", title);
		inputDescription(Element.txtProjectDescription, description);
		populateChooserField("Type", type);

		waitForButtonToExist("Add Choice", 5);
		
		
		if (!documentChoice.isEmpty()){
			clickButton("Add Choice");
			waitForButtonToExist("OK",5);
			do {
			    if (isElementVisible(By.xpath("//td[normalize-space()='"+documentChoice+"']/preceding-sibling::td//label"), 2)){
			    	click(By.xpath("//td[normalize-space()='"+documentChoice+"']/preceding-sibling::td//label"));
			    	writeToLogs(">>Document Choice: " + documentChoice);
			    	clickButton("OK");
			    	break;
			    }
			    if (isElementVisible(Element.lnkDocumentChoiceNext, 2)){
			    	click(Element.lnkDocumentChoiceNext);
			    }else{
			    	System.out.println("[FAILED] Document Choice: " + documentChoice + " is not found.");
			    }
			}while (isElementVisible(Element.lnkDocumentChoiceNext, 2));
		}
		
		waitFor(2);
		waitForButtonToExist("Save", 5);
		clickButton("Save");
		waitFor(2);
		waitForButtonToExist("Done", 5);
		clickButton("Done");
	}
	
	
	
	public void select(String category, String value){
		
//		value = "HVE Sourcing Event Contents|Standard|HVE Request for Proposal - Standard";
		
		
		
		
		
		String [] c = value.split("\\|");
    	
    	for (int i=0; i<c.length; i++){
    		
    		if (i==c.length-1){
    			if (isElementVisible(By.xpath("//span[contains(.,'"+c[i].trim()+"')]"), 5)){
    				click(By.xpath("//span[contains(.,'"+c[i].trim()+"')]"));
    				waitFor(2);
	    			break;
	    		}else{
	    			writeToLogs("[ERROR]" +c[i].trim()+  " is not available");
	    		}
    		}
    		
    		if (isElementVisible(By.xpath("//span[contains(.,'"+c[i].trim()+"')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"), 5)){
    			click(By.xpath("//span[contains(.,'"+c[i].trim()+"')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"));
    		}else{
    			writeToLogs("[ERROR]" +c[i].trim()+  " is not available");
    		}
    		
    		
    		
    	}
    	
    	clickButton("Select");
    	
		
	}
	
	
	public void configureEventTaskTab(){
		
		parseExcel retrieve = new parseExcel();
		List <String> tasks = retrieve.getTasksTab();

		for(String t : tasks){
			String [] task = t.split("~", -1);
			String type = task[9].trim();
			String phase = task[0].trim();
			String subPhase1 = task[1].trim();
			String subPhase2 = task[2].trim();
			
			//Tasks
			String title = task[6].trim();
			String description =  task[7].trim();
			String owner = task[11].trim();
			String observers = task[15].trim();
			String isMilestone = task[10].trim();
			String required = task[8].trim();
			String predecessors = task[23].trim();
			String associatedDocument = task[22].trim();
			String conditions = task[24].trim();

			
			//Notification
			String recipients = task[17].trim();
			String notificationDays = task[18].trim();
			String notificationFrequency = task[19].trim();
			String autoStart = task[20].trim();
			String manualCompletion = task[21].trim();
			
			
			//Review
			String reviewers = task[14].trim();
			String approvalRuleFlow = task[13].trim();
			String repeat = task[16].trim();
			
			//Approval
			String allowAutoApproval = task[12].trim();
			
			//Signature
			String signatureProvider = task[25].trim();
			String signer = task[26].trim();
			
			
			if (type.isEmpty()){
				
				if (!phase.isEmpty() && subPhase1.isEmpty()){

					//Create Phase
					waitFor(2);
					sendKeysEnter(Element.lnkTaskActions);
					click(Element.lnkCreatePhase);
					createPhase(phase, task[3], task[4], task[5], task[23]);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+phase+"')]/following-sibling::td[2]//a"), conditions);

				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && subPhase2.isEmpty()){
					
					//Create Sub Phase 1
					waitFor(2);
					sendKeysEnter(By.linkText(phase));
					click(Element.lnkCreatePhase);
					createPhase(subPhase1, task[3], task[4], task[5], task[23]);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+phase+"')]/following-sibling::td[2]//a"), conditions);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && !subPhase2.isEmpty()){
					
					if (isElementVisible(By.xpath("//a[contains(text(),'"+phase+"')]/../../../preceding-sibling::td[2]//div[@class='w-oc-icon-off']"), 5)){
						click(By.xpath("//a[contains(text(),'"+phase+"')]/../../../preceding-sibling::td[2]//div[@class='w-oc-icon-off']"));
					}
					
					//Create Sub Phase 2
					waitFor(2);
					sendKeysEnter(By.linkText(subPhase1));
					click(Element.lnkCreatePhase);
					createPhase(subPhase2, task[3], task[4], task[5], task[23]);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+phase+"')]/following-sibling::td[2]//a"), conditions);
					
				}
			}else{
				
				if (phase.isEmpty()){

					//Create Task outside Phase
					waitFor(2);
					sendKeysEnter(Element.lnkTaskActions);
					createTask(type, title, description, owner, observers, isMilestone, required, predecessors, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, associatedDocument, reviewers, approvalRuleFlow, repeat, allowAutoApproval, signatureProvider, signer);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+title+"')]/following-sibling::td[2]//a"), conditions);
					
				}else if (!phase.isEmpty() && subPhase1.isEmpty()){
					
					//Create Task in Phase
					waitFor(2);
					sendKeysEnter(By.linkText(phase));
					createTask(type, title, description, owner, observers, isMilestone, required, predecessors, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, associatedDocument, reviewers, approvalRuleFlow, repeat, allowAutoApproval, signatureProvider, signer);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+title+"')]/following-sibling::td[2]//a"), conditions);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && subPhase2.isEmpty()){
					
					if (isElementVisible(By.xpath("//a[contains(text(),'"+phase+"')]/../../../preceding-sibling::td[2]//div[@class='w-oc-icon-off']"), 5)){
						click(By.xpath("//a[contains(text(),'"+phase+"')]/../../../preceding-sibling::td[2]//div[@class='w-oc-icon-off']"));
					}
					
					//Create Task in Sub Phase 1
					waitFor(2);
					sendKeysEnter(By.linkText(subPhase1));
					createTask(type, title, description, owner, observers, isMilestone, required, predecessors, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, associatedDocument, reviewers, approvalRuleFlow, repeat, allowAutoApproval, signatureProvider, signer);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+title+"')]/following-sibling::td[2]//a"), conditions);
					
				}else if(!phase.isEmpty() && !subPhase1.isEmpty() && !subPhase2.isEmpty()){
					
					if (isElementVisible(By.xpath("//a[contains(text(),'"+phase+"')]/../../../preceding-sibling::td[2]//div[@class='w-oc-icon-off']"), 5)){
						click(By.xpath("//a[contains(text(),'"+phase+"')]/../../../preceding-sibling::td[2]//div[@class='w-oc-icon-off']"));
						if (isElementVisible(By.xpath("//a[contains(text(),'"+subPhase1+"')]/../../../preceding-sibling::td[2]//div[@class='w-oc-icon-off']"), 5)){
							click(By.xpath("//a[contains(text(),'"+subPhase1+"')]/../../../preceding-sibling::td[2]//div[@class='w-oc-icon-off']"));
							
						}
					}
					
					//Create Task in Sub Phase 2
					waitFor(2);
					sendKeysEnter(By.linkText(subPhase2));
					createTask(type, title, description, owner, observers, isMilestone, required, predecessors, recipients, notificationDays, notificationFrequency, autoStart, manualCompletion, associatedDocument, reviewers, approvalRuleFlow, repeat, allowAutoApproval, signatureProvider, signer);
					populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+title+"')]/following-sibling::td[2]//a"), conditions);
					
				}
				
			}
			
			writeToLogs("");
			
		}
		
	}
	
	
	
	public void populateVisibility(String field, String value){
		try{
			if (!value.isEmpty()){
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				boolean populated = false;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						if (fieldName.charAt(fieldName.length() - 1) != '?'){
							fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						}
						if (fieldName.equals(field)){
							scrollAndClick(By.xpath("(//td/label)["+ (i+1) +"]/../../following-sibling::tr//span[@class='w-dropdown-pic-ct']"));
							click(By.xpath("(//td/label)["+ (i+1) +"]"));
							click(By.xpath("(//td/label)["+ (i+1) +"]/../../following-sibling::tr//span[@class='w-dropdown-pic-ct']"));
							if (explicitWait(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(.,'"+value+"')]"), 5) != null){
								click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(.,'"+value+"')]"));
								populated = true;
								waitFor(2);
							}
							break;
						}
					}
				}
				
				if (populated){
					System.out.println("[PASSED] Select \"" + value + "\" on " + field + " field visibility.");
				}else{
					System.out.println("[FAILED] Unable to populate the field " + field + " visibility.");
				}
				
			}
		}catch(Exception e){
			writeToLogs("[ERROR] Unable to populate the field " + field + " visibility.");
		}

	}
	
	
	public String [] splitValues(String value, String delimeter){
		String[] val = null;
		if (value.contains(";")){
			val = value.split(delimeter);
		}
		return val;
	}
	
	
	public void auctionFormat(){
		
		parseExcel retrieve = new parseExcel();
		String [] biddingFormat = retrieve.getEventRules("Bidding format for the event").split(";",-1);
		
		//Auction
		populateDropdown("Bidding format for the event", biddingFormat[0]);
	}
	
	public void timingRules_RFP(){
		
		parseExcel retrieve = new parseExcel();
		
		//Capacity Type
		String [] capacityTypeForTheEvent = retrieve.getEventRules("Capacity type for the event").split(";",-1);
		populateDropdown("Capacity type for the event", capacityTypeForTheEvent[0]);

		//Timing Rules
		String [] enablePreviewPeriodBeforeBiddingOpens = retrieve.getEventRules("Enable preview period before bidding opens").split(";",-1);
		populateRadioButton("Enable preview period before bidding opens", enablePreviewPeriodBeforeBiddingOpens[0]);
		populateVisibility("Enable preview period before bidding opens", enablePreviewPeriodBeforeBiddingOpens[1]);

		if (enablePreviewPeriodBeforeBiddingOpens[0].equalsIgnoreCase("Yes")){
			String [] canParticipantsPlaceBidsDuringPreviewPeriod = retrieve.getEventRules("Can participants place bids during preview period").split(";",-1);
			populateDropdown("Can participants place bids during preview period", canParticipantsPlaceBidsDuringPreviewPeriod[0]);
			populateVisibility("Can participants place bids during preview period", canParticipantsPlaceBidsDuringPreviewPeriod[1]);
			
			String startTime = retrieve.getEventRules("Start time");
			if (!startTime.contains("Publish")){
				click(Element.rdoScheduleFortheFuture);
				waitFor(2);
				String [] scheduleForFuture = startTime.split(";",-1);
		
				String [] dateTime =  scheduleForFuture[0].split("\\|");
				inputText(Element.txtScheduleForTheFuture_Date, dateTime[0]);
				if (dateTime.length > 1){
					inputText(Element.txtScheduleForTheFuture_Time, dateTime[1]);
				}
				populateVisibility("Schedule For the Future", scheduleForFuture[1]);
			}
			
			
			if (canParticipantsPlaceBidsDuringPreviewPeriod[0].equalsIgnoreCase("Allow prebids") || canParticipantsPlaceBidsDuringPreviewPeriod[0].equalsIgnoreCase("Require prebids")){
				if (!retrieve.getEventRules("Prebid end time").isEmpty()){
					String [] prebidEndTime = retrieve.getEventRules("Prebid end time").split("\\|");
					populateTextField("Prebid end time", prebidEndTime[0]);
					if (prebidEndTime.length > 1){
						inputText(Element.txtPrebidEndTime, prebidEndTime[1]);
					}
				}
			}
			
		}
		
		
		if (capacityTypeForTheEvent[0].isEmpty() || capacityTypeForTheEvent[0].equals("Standard")){
			
			String [] specifyHowLotBiddingWillBeginAndEnd = retrieve.getEventRules("Specify how lot bidding will begin and end").split(";",-1);
			populateDropdown("Specify how lot bidding will begin and end", specifyHowLotBiddingWillBeginAndEnd[0]);
			populateVisibility("Specify how lot bidding will begin and end", specifyHowLotBiddingWillBeginAndEnd[1]);
			
			if (specifyHowLotBiddingWillBeginAndEnd[0].equals("Staggered") || specifyHowLotBiddingWillBeginAndEnd[0].equals("Serial")){
				
				String [] runningTimeForTheFirstLot = retrieve.getEventRules("Running time for the first lot").split(";",-1);
				if (!runningTimeForTheFirstLot[0].isEmpty()){
					String [] runningTimeForTheFirstLotValue = runningTimeForTheFirstLot[0].split("\\|");
					populateTextField("Running time for the first lot", runningTimeForTheFirstLotValue[0]);
					populateDropdown("Running time for the first lot", runningTimeForTheFirstLotValue[1]);
				}
				populateVisibility("Running time for the first lot", runningTimeForTheFirstLot[1]);
				
				String [] timeBetweenLotClosing = retrieve.getEventRules("Time between lot closing").split(";",-1);
				if (!timeBetweenLotClosing[0].isEmpty()){
					String [] timeBetweenLotClosingValue = timeBetweenLotClosing[0].split("\\|");
					populateTextField("Time between lot closing", timeBetweenLotClosingValue[0]);
					populateDropdown("Time between lot closing", timeBetweenLotClosingValue[1]);
				}
				populateVisibility("Time between lot closing", timeBetweenLotClosing[1]);
			}
			
			if (specifyHowLotBiddingWillBeginAndEnd[0].isEmpty() || specifyHowLotBiddingWillBeginAndEnd[0].equals("Parallel")){
				if (!retrieve.getEventRules("Due date").isEmpty()){
					String [] dueDate = retrieve.getEventRules("Due date").split("\\|");
					inputText(Element.txtDueDate_Duration, dueDate[0]);
					if (dueDate.length > 1){
						populateDropdown("Due date", dueDate[1].replaceAll(";", ""));
					}
				}
			}
		}
		
		
		String [] responseStartDate = retrieve.getEventRules("Response start date").split(";",-1);
		if (!responseStartDate[0].isEmpty()){
			String [] responseStartDateValue =  responseStartDate[0].split("\\|");
			populateTextField("Response start date", responseStartDateValue[0]);
			inputText(Element.txtResponseStartDate_Time, responseStartDateValue[1]);
		}
		populateVisibility("Response start date", responseStartDate[1]);
		
		String [] setReviewPeriodAfterLotCloses = retrieve.getEventRules("Set a review period after lot closes").split(";",-1);
		populateRadioButton("Set a review period after lot closes", setReviewPeriodAfterLotCloses[0]);
		populateVisibility("Set a review period after lot closes", setReviewPeriodAfterLotCloses[1]);
		
		if (setReviewPeriodAfterLotCloses[0].equals("Yes")){
			String [] reviewTimePeriod = retrieve.getEventRules("Review time period").split(";",-1);
			if (!reviewTimePeriod[0].isEmpty()){
				String [] reviewTimePeriodValue = reviewTimePeriod[0].split("\\|");
				populateTextField("Review time period", reviewTimePeriodValue[0]);
				populateDropdown("Review time period", reviewTimePeriodValue[1]);
			}
			populateVisibility("Review time period", reviewTimePeriod[1]);
		}
		
		String [] allowBiddingOvertime = retrieve.getEventRules("Allow bidding overtime").split(";",-1);
		populateRadioButton("Allow bidding overtime", allowBiddingOvertime[0]);
		populateVisibility("Allow bidding overtime", allowBiddingOvertime[1]);
		
		
		if (allowBiddingOvertime[0].equals("Yes")){
			String [] bidRankTriggersOvertime = retrieve.getEventRules("Bid rank that triggers overtime").split(";",-1);
			populateTextField("Bid rank that triggers overtime", bidRankTriggersOvertime[0]);
			populateVisibility("Bid rank that triggers overtime", bidRankTriggersOvertime[1]);
			
			String [] startOvertimeIfBidSubmitted = retrieve.getEventRules("Start overtime if bid submitted within (minutes)").split(";",-1);
			populateTextField("Start overtime if bid submitted within (minutes)", startOvertimeIfBidSubmitted[0]);
			populateVisibility("Start overtime if bid submitted within (minutes)", startOvertimeIfBidSubmitted[1]);
			
			String [] overTimePeriod = retrieve.getEventRules("Overtime period (minutes)").split(";",-1);
			populateTextField("Overtime period (minutes)", overTimePeriod[0]);
			populateVisibility("Overtime period (minutes)", overTimePeriod[1]);
		}
		
		String [] estimatedAwardDate = retrieve.getEventRules("Estimated Award Date").split(";",-1);
		populateTextField("Estimated Award Date", estimatedAwardDate[0]);
		populateVisibility("Estimated Award Date", estimatedAwardDate[1]);
		
	}
	
	
	public void timingRules_Auction(){
		
		parseExcel retrieve = new parseExcel();
		
		//Capacity Type
		String [] capacityTypeForTheEvent = retrieve.getEventRules("Capacity type for the event").split(";",-1);
		populateDropdown("Capacity type for the event", capacityTypeForTheEvent[0]);

		//Timing Rules
		String [] enablePreviewPeriodBeforeBiddingOpens = retrieve.getEventRules("Enable preview period before bidding opens").split(";",-1);
		populateRadioButton("Enable preview period before bidding opens", enablePreviewPeriodBeforeBiddingOpens[0]);
		populateVisibility("Enable preview period before bidding opens", enablePreviewPeriodBeforeBiddingOpens[1]);

		if (enablePreviewPeriodBeforeBiddingOpens[0].equalsIgnoreCase("Yes")){
			String [] canParticipantsPlaceBidsDuringPreviewPeriod = retrieve.getEventRules("Can participants place bids during preview period").split(";",-1);
			populateDropdown("Can participants place bids during preview period", canParticipantsPlaceBidsDuringPreviewPeriod[0]);
			populateVisibility("Can participants place bids during preview period", canParticipantsPlaceBidsDuringPreviewPeriod[1]);
			
			if (canParticipantsPlaceBidsDuringPreviewPeriod[0].equalsIgnoreCase("Allow prebids") || canParticipantsPlaceBidsDuringPreviewPeriod[0].equalsIgnoreCase("Require prebids")){
				if (!retrieve.getEventRules("Prebid end time").isEmpty()){
					String [] prebidEndTime = retrieve.getEventRules("Prebid end time").split("\\|");
					populateTextField("Prebid end time", prebidEndTime[0]);
					inputText(Element.txtPrebidEndTime, prebidEndTime[1]);
				}
				String startTime = retrieve.getEventRules("Start time");
				if (!startTime.contains("Publish")){
					click(Element.rdoScheduleFortheFuture);
					waitFor(2);
					String [] scheduleForFuture = startTime.split(";",-1);
					String [] dateTime =  scheduleForFuture[0].split("\\|");
					
					inputText(Element.txtScheduleForTheFuture_Date, dateTime[0]);
					inputText(Element.txtScheduleForTheFuture_Time, dateTime[1]);
					populateVisibility("Schedule For the Future", scheduleForFuture[1]);
				}
				
			}
			
		}
		
		
		if (capacityTypeForTheEvent[0].isEmpty() || capacityTypeForTheEvent[0].equals("Standard")){
			String [] specifyHowLotBiddingWillBeginAndEnd = retrieve.getEventRules("Specify how lot bidding will begin and end").split(";",-1);
			populateDropdown("Specify how lot bidding will begin and end", specifyHowLotBiddingWillBeginAndEnd[0]);
			populateVisibility("Specify how lot bidding will begin and end", specifyHowLotBiddingWillBeginAndEnd[1]);
			
			if (specifyHowLotBiddingWillBeginAndEnd[0].equals("Staggered") || specifyHowLotBiddingWillBeginAndEnd[0].equals("Serial")){
				
				String [] runningTimeForTheFirstLot = retrieve.getEventRules("Running time for the first lot").split(";",-1);
				String [] runningTimeForTheFirstLotValue = runningTimeForTheFirstLot[0].split("\\|");
				populateTextField("Running time for the first lot", runningTimeForTheFirstLotValue[0]);
				populateDropdown("Running time for the first lot", runningTimeForTheFirstLotValue[1]);
				populateVisibility("Running time for the first lot", runningTimeForTheFirstLot[1]);
				
				String [] timeBetweenLotClosing = retrieve.getEventRules("Time between lot closing").split(";",-1);
				if (!timeBetweenLotClosing[0].isEmpty()){
					String [] timeBetweenLotClosingValue = timeBetweenLotClosing[0].split("\\|");
					populateTextField("Time between lot closing", timeBetweenLotClosingValue[0]);
					populateDropdown("Time between lot closing", timeBetweenLotClosingValue[1]);
				}
				populateVisibility("Time between lot closing", timeBetweenLotClosing[1]);
				
			}
			
			
		}
		
		
		String [] biddingStartTime = retrieve.getEventRules("Bidding start time").split(";",-1);
		String [] biddingStartTimeValue =  biddingStartTime[0].split("\\|");
		
		populateTextField("Bidding start time", biddingStartTimeValue[0]);
		if (biddingStartTimeValue.length > 1){
			inputText(Element.txtBiddingStartTime_Time, biddingStartTimeValue[1]);
		}
		populateVisibility("Bidding start time", biddingStartTime[1]);
		
		String [] setReviewPeriodAfterLotCloses = retrieve.getEventRules("Set a review period after lot closes").split(";",-1);
		populateRadioButton("Set a review period after lot closes", setReviewPeriodAfterLotCloses[0]);
		populateVisibility("Set a review period after lot closes", setReviewPeriodAfterLotCloses[1]);
		
		if (setReviewPeriodAfterLotCloses[0].equals("Yes")){
			String [] reviewTimePeriod = retrieve.getEventRules("Review time period").split(";",-1);
			if (!reviewTimePeriod[0].isEmpty()){
				String [] reviewTimePeriodValue = reviewTimePeriod[0].split("\\|");
				populateTextField("Review time period", reviewTimePeriodValue[0]);
				populateDropdown("Review time period", reviewTimePeriodValue[1]);
			}
			populateVisibility("Review time period", reviewTimePeriod[1]);
		}
		
		String [] allowBiddingOvertime = retrieve.getEventRules("Allow bidding overtime").split(";",-1);
		populateRadioButton("Allow bidding overtime", allowBiddingOvertime[0]);
		populateVisibility("Allow bidding overtime", allowBiddingOvertime[1]);
		
		
		if (allowBiddingOvertime[0].equals("Yes")){
			String [] bidRankTriggersOvertime = retrieve.getEventRules("Bid rank that triggers overtime").split(";",-1);
			populateTextField("Bid rank that triggers overtime", bidRankTriggersOvertime[0]);
			populateVisibility("Bid rank that triggers overtime", bidRankTriggersOvertime[1]);
			
			String [] startOvertimeIfBidSubmitted = retrieve.getEventRules("Start overtime if bid submitted within (minutes)").split(";",-1);
			populateTextField("Start overtime if bid submitted within (minutes)", startOvertimeIfBidSubmitted[0]);
			populateVisibility("Start overtime if bid submitted within (minutes)", startOvertimeIfBidSubmitted[1]);
			
			String [] overTimePeriod = retrieve.getEventRules("Overtime period (minutes)").split(";",-1);
			populateTextField("Overtime period (minutes)", overTimePeriod[0]);
			populateVisibility("Overtime period (minutes)", overTimePeriod[1]);
		}
		
		String [] estimatedAwardDate = retrieve.getEventRules("Estimated Award Date").split(";",-1);
		populateTextField("Estimated Award Date", estimatedAwardDate[0]);
		populateVisibility("Estimated Award Date", estimatedAwardDate[1]);
		
	}
	
	
	public void timingRules_ForwardAuction(){
		
		parseExcel retrieve = new parseExcel();
		
		//Capacity Type
		String [] capacityTypeForTheEvent = retrieve.getEventRules("Capacity type for the event").split(";",-1);
		populateDropdown("Capacity type for the event", capacityTypeForTheEvent[0]);

		//Timing Rules
		String [] enablePreviewPeriodBeforeBiddingOpens = retrieve.getEventRules("Enable preview period before bidding opens").split(";",-1);
		populateRadioButton("Enable preview period before bidding opens", enablePreviewPeriodBeforeBiddingOpens[0]);
		populateVisibility("Enable preview period before bidding opens", enablePreviewPeriodBeforeBiddingOpens[1]);

		if (enablePreviewPeriodBeforeBiddingOpens[0].equalsIgnoreCase("Yes")){
			String [] canParticipantsPlaceBidsDuringPreviewPeriod = retrieve.getEventRules("Can participants place bids during preview period").split(";",-1);
			populateDropdown("Can participants place bids during preview period", canParticipantsPlaceBidsDuringPreviewPeriod[0]);
			populateVisibility("Can participants place bids during preview period", canParticipantsPlaceBidsDuringPreviewPeriod[1]);
			
			if (canParticipantsPlaceBidsDuringPreviewPeriod[0].equalsIgnoreCase("Allow prebids") || canParticipantsPlaceBidsDuringPreviewPeriod[0].equalsIgnoreCase("Require prebids")){

				String [] prebidEndTime = retrieve.getEventRules("Prebid end time").split("\\|");
				populateTextField("Prebid end time", prebidEndTime[0]);
				if(prebidEndTime.length > 1){
					inputText(Element.txtPrebidEndTime, prebidEndTime[1]);
				}
				
				String startTime = retrieve.getEventRules("Start time");
				if (!startTime.contains("Publish")){
					click(Element.rdoScheduleFortheFuture);
					waitFor(2);
					String [] scheduleForFuture = startTime.split(";",-1);
					String [] dateTime =  scheduleForFuture[0].split("\\|");
					
					inputText(Element.txtScheduleForTheFuture_Date, dateTime[0]);
					if (dateTime.length > 1){
						inputText(Element.txtScheduleForTheFuture_Time, dateTime[1]);
					}
					populateVisibility("Schedule For the Future", scheduleForFuture[1]);
				}
				
			}
			
		}
		
		
		if (capacityTypeForTheEvent[0].isEmpty() || capacityTypeForTheEvent[0].equals("Standard")){
			String [] specifyHowLotBiddingWillBeginAndEnd = retrieve.getEventRules("Specify how lot bidding will begin and end").split(";",-1);
			populateDropdown("Specify how lot bidding will begin and end", specifyHowLotBiddingWillBeginAndEnd[0]);
			populateVisibility("Specify how lot bidding will begin and end", specifyHowLotBiddingWillBeginAndEnd[1]);
			
			if (specifyHowLotBiddingWillBeginAndEnd[0].equals("Staggered") || specifyHowLotBiddingWillBeginAndEnd[0].equals("Serial")){
				
				String [] runningTimeForTheFirstLot = retrieve.getEventRules("Running time for the first lot").split(";",-1);
				String [] runningTimeForTheFirstLotValue = runningTimeForTheFirstLot[0].split("\\|");
				populateTextField("Running time for the first lot", runningTimeForTheFirstLotValue[0]);
				if (runningTimeForTheFirstLotValue.length > 1){
					populateDropdown("Running time for the first lot", runningTimeForTheFirstLotValue[1]);
				}
				populateVisibility("Running time for the first lot", runningTimeForTheFirstLot[1]);
				
				String [] timeBetweenLotClosing = retrieve.getEventRules("Time between lot closing").split(";",-1);
				String [] timeBetweenLotClosingValue = timeBetweenLotClosing[0].split("\\|");
				populateTextField("Time between lot closing", timeBetweenLotClosingValue[0]);
				if (timeBetweenLotClosingValue.length > 1){
					populateDropdown("Time between lot closing", timeBetweenLotClosingValue[1]);
				}
				populateVisibility("Time between lot closing", timeBetweenLotClosing[1]);
				
			}
			
			
		}
		
		
		String [] biddingStartTime = retrieve.getEventRules("Bidding start time").split(";",-1);
		String [] biddingStartTimeValue =  biddingStartTime[0].split("\\|");
		
		populateTextField("Bidding start time", biddingStartTimeValue[0]);
		if (biddingStartTimeValue.length > 1){
			inputText(Element.txtBiddingStartTime_Time, biddingStartTimeValue[1]);
		}
		populateVisibility("Bidding start time", biddingStartTime[1]);
		
		String [] responseEndTime = retrieve.getEventRules("Response end time").split("\\|");
		populateTextField("Response end time", responseEndTime[0]);
		if (responseEndTime.length > 1){
			populateDropdown("Response end time", responseEndTime[1]);
		}
		String [] setReviewPeriodAfterLotCloses = retrieve.getEventRules("Set a review period after lot closes").split(";",-1);
		populateRadioButton("Set a review period after lot closes", setReviewPeriodAfterLotCloses[0]);
		populateVisibility("Set a review period after lot closes", setReviewPeriodAfterLotCloses[1]);
		
		if (setReviewPeriodAfterLotCloses[0].equals("Yes")){
			String [] reviewTimePeriod = retrieve.getEventRules("Review time period").split(";",-1);
			String [] reviewTimePeriodValue = reviewTimePeriod[0].split("\\|");
			populateTextField("Review time period", reviewTimePeriodValue[0]);
			if (reviewTimePeriodValue.length>1){
				populateDropdown("Review time period", reviewTimePeriodValue[1]);
			}
			populateVisibility("Review time period", reviewTimePeriod[1]);
		}
		
		String [] allowBiddingOvertime = retrieve.getEventRules("Allow bidding overtime").split(";",-1);
		populateRadioButton("Allow bidding overtime", allowBiddingOvertime[0]);
		populateVisibility("Allow bidding overtime", allowBiddingOvertime[1]);
		
		
		if (allowBiddingOvertime[0].equals("Yes")){
			String [] bidRankTriggersOvertime = retrieve.getEventRules("Bid rank that triggers overtime").split(";",-1);
			populateTextField("Bid rank that triggers overtime", bidRankTriggersOvertime[0]);
			populateVisibility("Bid rank that triggers overtime", bidRankTriggersOvertime[1]);
			
			String [] startOvertimeIfBidSubmitted = retrieve.getEventRules("Start overtime if bid submitted within (minutes)").split(";",-1);
			populateTextField("Start overtime if bid submitted within (minutes)", startOvertimeIfBidSubmitted[0]);
			populateVisibility("Start overtime if bid submitted within (minutes)", startOvertimeIfBidSubmitted[1]);
			
			String [] overTimePeriod = retrieve.getEventRules("Overtime period (minutes)").split(";",-1);
			populateTextField("Overtime period (minutes)", overTimePeriod[0]);
			populateVisibility("Overtime period (minutes)", overTimePeriod[1]);
		}
		
		String [] estimatedAwardDate = retrieve.getEventRules("Estimated Award Date").split(";",-1);
		populateTextField("Estimated Award Date", estimatedAwardDate[0]);
		populateVisibility("Estimated Award Date", estimatedAwardDate[1]);
		
	}
	
	
	
	public void envelopeRules_RFI(){

		parseExcel retrieve = new parseExcel();

		//Envelope Rules
		String [] numberOfEnvelopes = retrieve.getEventRules("Number of Envelopes").split(";",-1);
		populateDropdown("Number of Envelopes", numberOfEnvelopes[0]);
		populateVisibility("Number of Envelopes", numberOfEnvelopes[1]);
		
		if (!numberOfEnvelopes[0].equals("No Envelope")){
			
			String authorizedTeamToOpenEnv = retrieve.getEventRules("Authorize Teams to Open Envelopes");
			String [] noe = authorizedTeamToOpenEnv.split("~");
			
			for (int i=0; i<noe.length; i++){
				
				sendKeysEnter(By.xpath("(//div[@class='w-chMenuPositionObj']//input[@type='text'])["+(i+1)+"]"));

				String [] data = noe[i].split("\\|");
				
				for(String val : data){
					inputText(Element.txtSearchField, val);
					click(Element.btnSearchField);
					waitFor(2);
					if (explicitWait(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"), 5) != null){
						click(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1') and contains(.,'"+val+"')]//td//label"));
						System.out.println("Select " + val + " on Envelop Id " + (i+1));
						waitFor(2);
					}else{
						writeToLogs("[INFO] Cannot find " +val+ " on Envelop Id " + (i+1));
					}
				}
				click(Element.btnDoneSearch);
				waitFor(2);
				click(By.className("pageHead"));
			}
			
			String [] keeptheRejectedEnvelopBids = retrieve.getEventRules("Keep the rejected envelope bids").split(";",-1);
			populateRadioButton("Keep the rejected envelope bids", keeptheRejectedEnvelopBids[0]);
			populateVisibility("Keep the rejected envelope bids", keeptheRejectedEnvelopBids[1]);
			
			String [] discardBidsforEventUpdating = retrieve.getEventRules("Discard bids for event updating").split(";",-1);
			populateDropdown("Discard bids for event updating", discardBidsforEventUpdating[0]);
			populateVisibility("Discard bids for event updating", discardBidsforEventUpdating[1]);
			
			String [] sendNotificationToEnvelopeOpeners = retrieve.getEventRules("Send notification to envelope openers").split(";",-1);
			populateRadioButton("Send notification to envelope openers", sendNotificationToEnvelopeOpeners[0]);
			populateVisibility("Send notification to envelope openers", sendNotificationToEnvelopeOpeners[1]);

		}
		
	}
	
	
	public void timingRules_RFI(){
		
		parseExcel retrieve = new parseExcel();
		
		//Timing Rules
		String [] enablePreviewPeriodBeforeBiddingOpens = retrieve.getEventRules("Enable preview period before bidding opens").split(";",-1);
		populateRadioButton("Enable preview period before bidding opens", enablePreviewPeriodBeforeBiddingOpens[0]);
		populateVisibility("Enable preview period before bidding opens", enablePreviewPeriodBeforeBiddingOpens[1]);

		if (enablePreviewPeriodBeforeBiddingOpens[0].equalsIgnoreCase("Yes")){
			String [] canParticipantsPlaceBidsDuringPreviewPeriod = retrieve.getEventRules("Can participants place bids during preview period").split(";",-1);
			populateDropdownAlt("Can participants place bids during preview period", canParticipantsPlaceBidsDuringPreviewPeriod[0]);
			populateVisibility("Can participants place bids during preview period", canParticipantsPlaceBidsDuringPreviewPeriod[1]);
			
			if (canParticipantsPlaceBidsDuringPreviewPeriod[0].equalsIgnoreCase("Allow prebids") || canParticipantsPlaceBidsDuringPreviewPeriod[0].equalsIgnoreCase("Require prebids")){
				String [] prebidEndTime = retrieve.getEventRules("Prebid end time").split("\\|");
				populateTextField("Prebid end time", prebidEndTime[0]);
				if (prebidEndTime.length>1){
					inputText(Element.txtPrebidEndTime, prebidEndTime[1]);
				}
				String startTime = retrieve.getEventRules("Start time");
				if (!startTime.contains("Publish")){
					click(Element.rdoScheduleFortheFuture);
					waitFor(2);
					String [] scheduleForFuture = startTime.split(";",-1);
					String [] dateTime =  scheduleForFuture[0].split("\\|");
					
					inputText(Element.txtScheduleForTheFuture_Date, dateTime[0]);
					if (dateTime.length>1){
						inputText(Element.txtScheduleForTheFuture_Time, dateTime[1]);
					}
					populateVisibility("Schedule For the Future", scheduleForFuture[1]);
				}
				
			}
			
		}
		
		String responseStartDateVal = retrieve.getEventRules("Response start date");
		if (responseStartDateVal.contains("Publish")){
			click(Element.rdoWhenClickPublish);
		}else{
			click(Element.rdoScheduleFortheFuture);
			String [] responseStartDate = responseStartDateVal.split(";",-1);
			String [] responseStartDateValue =  responseStartDate[0].split("\\|");
			
			populateTextField("Response start date", responseStartDateValue[0]);
			if (responseStartDateValue.length>1){
				inputText(Element.txtResponseStartDate_Time, responseStartDateValue[1]);
			}
			populateVisibility("Response start date", responseStartDate[1]);
		}
		
		
		String [] dueDate = retrieve.getEventRules("Due date").split("\\|");
		inputText(Element.txtDueDate_Duration, dueDate[0]);
		if (dueDate.length>1){
			populateDropdown("Due date", dueDate[1].replaceAll(";", ""));
		}
		String [] setReviewPeriodAfterLotCloses = retrieve.getEventRules("Set a review period after lot closes").split(";",-1);
		populateRadioButton("Set a review period after lot closes", setReviewPeriodAfterLotCloses[0]);
		populateVisibility("Set a review period after lot closes", setReviewPeriodAfterLotCloses[1]);
		
		if (setReviewPeriodAfterLotCloses[0].equals("Yes")){
			String [] reviewTimePeriod = retrieve.getEventRules("Review time period").split(";",-1);
			String [] reviewTimePeriodValue = reviewTimePeriod[0].split("\\|");
			populateTextField("Review time period", reviewTimePeriodValue[0]);
			if (reviewTimePeriodValue.length > 1){
				populateDropdown("Review time period", reviewTimePeriodValue[1]);
			}
			populateVisibility("Review time period", reviewTimePeriod[1]);
		}
		
		String [] allowBiddingOvertime = retrieve.getEventRules("Allow bidding overtime").split(";",-1);
		populateRadioButton("Allow bidding overtime", allowBiddingOvertime[0]);
		populateVisibility("Allow bidding overtime", allowBiddingOvertime[1]);
		
		
		if (allowBiddingOvertime[0].equals("Yes")){
			String [] bidRankTriggersOvertime = retrieve.getEventRules("Bid rank that triggers overtime").split(";",-1);
			populateTextField("Bid rank that triggers overtime", bidRankTriggersOvertime[0]);
			populateVisibility("Bid rank that triggers overtime", bidRankTriggersOvertime[1]);
			
			String [] startOvertimeIfBidSubmitted = retrieve.getEventRules("Start overtime if bid submitted within (minutes)").split(";",-1);
			populateTextField("Start overtime if bid submitted within (minutes)", startOvertimeIfBidSubmitted[0]);
			populateVisibility("Start overtime if bid submitted within (minutes)", startOvertimeIfBidSubmitted[1]);
			
			String [] overTimePeriod = retrieve.getEventRules("Overtime period (minutes)").split(";",-1);
			populateTextField("Overtime period (minutes)", overTimePeriod[0]);
			populateVisibility("Overtime period (minutes)", overTimePeriod[1]);
		}
		
	}
	
	
	public void biddingRules_RFI(){
		
		parseExcel retrieve = new parseExcel();
		
		String [] bidGuardianPercentage = retrieve.getEventRules("Bid Guardian percentage").split(";",-1);
		populateTextField("Bid Guardian percentage", bidGuardianPercentage[0]);
		
		String [] enableScoringOnParticipants = retrieve.getEventRules("Enable scoring on participant responses").split(";",-1);
		populateRadioButton("Enable scoring on participant responses", enableScoringOnParticipants[0]);
		populateVisibility("Enable scoring on participant responses", enableScoringOnParticipants[1]);
		
		if (enableScoringOnParticipants[0].isEmpty() || enableScoringOnParticipants[0].equals("Yes")){
			String [] defaultGradingMethod = retrieve.getEventRules("Default Grading Method").split(";",-1);
			populateDropdown("Default Grading Method", defaultGradingMethod[0]);
			populateVisibility("Default Grading Method", defaultGradingMethod[1]);
			
			String [] enableBlindGradingOnParticipantResponses = retrieve.getEventRules("Enable blind grading on participant responses").split(";",-1);
			populateRadioButton("Enable blind grading on participant responses", enableBlindGradingOnParticipantResponses[0]);
			populateVisibility("Enable blind grading on participant responses", enableBlindGradingOnParticipantResponses[1]);
		}
		
		waitFor(2);
		String [] canParticipantsCreateAlternativeResponses = retrieve.getEventRules("Can participants create alternative responses?").split(";",-1);
		populateRadioButton("Can participants create alternative responses?", canParticipantsCreateAlternativeResponses[0]);
		populateVisibility("Can participants create alternative responses?", canParticipantsCreateAlternativeResponses[1]);
		
		if (canParticipantsCreateAlternativeResponses[0].equals("Yes")){
			String [] canParticipantsCreateAlternativePricing = retrieve.getEventRules("Can participants create alternative pricing?").split(";",-1);
			populateRadioButton("Can participants create alternative pricing?", canParticipantsCreateAlternativePricing[0]);
			populateVisibility("Can participants create alternative pricing?", canParticipantsCreateAlternativePricing[1]);
			
			String [] canParticipantsCreateBundles = retrieve.getEventRules("Can participants create bundles?").split(";",-1);
			populateRadioButton("Can participants create bundles?", canParticipantsCreateBundles[0]);
			populateVisibility("Can participants create bundles?", canParticipantsCreateBundles[1]);
			
			String [] canParticipantsCreateTiers = retrieve.getEventRules("Can participants create tiers?").split(";",-1);
			populateRadioButton("Can participants create tiers?", canParticipantsCreateTiers[0]);
			populateVisibility("Can participants create tiers?", canParticipantsCreateTiers[1]);
		}
		
		String [] enableCustomOfflineResponse = retrieve.getEventRules("Enable custom offline response").split(";",-1);
		populateDropdown("Enable custom offline response", enableCustomOfflineResponse[0]);
		populateVisibility("Enable custom offline response", enableCustomOfflineResponse[1]);
		
	}
	
	
	public void biddingRules_RFP(){
		
		parseExcel retrieve = new parseExcel();
		
		String [] useTransformationBiddingFormat = retrieve.getEventRules("Use transformation bidding format").split(";",-1);
		populateRadioButton("Use transformation bidding format", useTransformationBiddingFormat[0]);
		
		String [] bidGuardianPercentage = retrieve.getEventRules("Bid Guardian percentage").split(";",-1);
		populateTextField("Bid Guardian percentage", bidGuardianPercentage[0]);
		
		String [] allowOwnerToChangeBidImprovementRulesAtTHeLotLevel = retrieve.getEventRules("Allow owner to change bid improvement rules at the lot level").split(";",-1);
		populateRadioButton("Allow owner to change bid improvement rules at the lot level", allowOwnerToChangeBidImprovementRulesAtTHeLotLevel[0]);
		
		String [] enableScoringOnParticipants = retrieve.getEventRules("Enable scoring on participant responses").split(";",-1);
		populateRadioButton("Enable scoring on participant responses", enableScoringOnParticipants[0]);
		populateVisibility("Enable scoring on participant responses", enableScoringOnParticipants[1]);
		
		if (enableScoringOnParticipants[0].equals("Yes")){
			String [] defaultGradingMethod = retrieve.getEventRules("Default Grading Method").split(";",-1);
			populateDropdown("Default Grading Method", defaultGradingMethod[0]);
			populateVisibility("Default Grading Method", defaultGradingMethod[1]);
			
			String [] enableBlindGradingOnParticipantResponses = retrieve.getEventRules("Enable blind grading on participant responses").split(";",-1);
			populateRadioButton("Enable blind grading on participant responses", enableBlindGradingOnParticipantResponses[0]);
			populateVisibility("Enable blind grading on participant responses", enableBlindGradingOnParticipantResponses[1]);
		}

		String [] canParticipantsCreateAlternativeResponses = retrieve.getEventRules("Can participants create alternative responses?").split(";",-1);
		populateRadioButton("Can participants create alternative responses?", canParticipantsCreateAlternativeResponses[0]);
		populateVisibility("Can participants create alternative responses?", canParticipantsCreateAlternativeResponses[1]);
		
		if (canParticipantsCreateAlternativeResponses[0].equals("Yes")){
			String [] canParticipantsCreateAlternativePricing = retrieve.getEventRules("Can participants create alternative pricing?").split(";",-1);
			populateRadioButton("Can participants create alternative pricing?", canParticipantsCreateAlternativePricing[0]);
			populateVisibility("Can participants create alternative pricing?", canParticipantsCreateAlternativePricing[1]);
			
			String [] canParticipantsCreateBundles = retrieve.getEventRules("Can participants create bundles?").split(";",-1);
			populateRadioButton("Can participants create bundles?", canParticipantsCreateBundles[0]);
			populateVisibility("Can participants create bundles?", canParticipantsCreateBundles[1]);
			
			String [] canParticipantsCreateTiers = retrieve.getEventRules("Can participants create tiers?").split(";",-1);
			populateRadioButton("Can participants create tiers?", canParticipantsCreateTiers[0]);
			populateVisibility("Can participants create tiers?", canParticipantsCreateTiers[1]);
		}

		
		String [] enableCustomOfflineResponse = retrieve.getEventRules("Enable custom offline response").split(";",-1);
		populateDropdown("Enable custom offline response", enableCustomOfflineResponse[0]);
		populateVisibility("Enable custom offline response", enableCustomOfflineResponse[1]);
		
		if (enableCustomOfflineResponse[0].equals("Yes")){
			String [] allowParticipantsToUsePreferredLocale = retrieve.getEventRules("Allow participants to use preferred locale for custom offline responses").split(";",-1);
			populateDropdown("Allow participants to use preferred locale for custom offline responses", allowParticipantsToUsePreferredLocale[0]);
			populateVisibility("Allow participants to use preferred locale for custom offline responses", allowParticipantsToUsePreferredLocale[1]);
		}
		
		
		String [] mustParticipantsImproveTheirBids = retrieve.getEventRules("Must participants improve their bids").split(";",-1);
		populateRadioButton("Must participants improve their bids", mustParticipantsImproveTheirBids[0]);
		populateVisibility("Must participants improve their bids", mustParticipantsImproveTheirBids[1]);
		
		if (mustParticipantsImproveTheirBids[0].isEmpty() || mustParticipantsImproveTheirBids[0].equals("Yes")){
			String [] mustParticipantsBeatLeadBid = retrieve.getEventRules("Must participants beat lead bid").split(";",-1);
			populateDropdown("Must participants beat lead bid", mustParticipantsBeatLeadBid[0]);
			populateVisibility("Must participants beat lead bid", mustParticipantsBeatLeadBid[1]);
			
			if (!mustParticipantsBeatLeadBid[0].contains("Yes")){
				String [] createABufferToProtectLeadBid = retrieve.getEventRules("Create a buffer to protect lead bid").split(";",-1);
				populateRadioButton("Create a buffer to protect lead bid", createABufferToProtectLeadBid[0]);
				populateVisibility("Create a buffer to protect lead bid", createABufferToProtectLeadBid[1]);
				waitFor(2);
				String [] canParticipantsSubmitTieBids = retrieve.getEventRules("Can participants submit tie bids").split(";",-1);
				populateDropdown("Can participants submit tie bids", canParticipantsSubmitTieBids[0]);
				populateVisibility("Can participants submit tie bids", canParticipantsSubmitTieBids[1]);
			}
			
			String [] improveBidAmountBy = retrieve.getEventRules("Improve bid amount by").split(";",-1);
			populateDropdown("Improve bid amount by", improveBidAmountBy[0]);
			populateVisibility("Improve bid amount by", improveBidAmountBy[1]);
			
			String [] allowOwnerToRequireImprovementForNonCompetitiveTerms = retrieve.getEventRules("Allow owner to require improvement on non-competitive terms").split(";",-1);
			populateRadioButton("Allow owner to require improvement on non-competitive terms", allowOwnerToRequireImprovementForNonCompetitiveTerms[0]);
			populateVisibility("Allow owner to require improvement on non-competitive terms", allowOwnerToRequireImprovementForNonCompetitiveTerms[1]);

		}else{
			waitFor(2);
			String [] canParticipantsSubmitTieBids = retrieve.getEventRules("Can participants submit tie bids").split(";",-1);
			populateDropdown("Can participants submit tie bids", canParticipantsSubmitTieBids[0]);
			populateVisibility("Can participants submit tie bids", canParticipantsSubmitTieBids[1]);
		}
	}
	
	
	public void currencyRules_RFI(){
		
		parseExcel retrieve = new parseExcel();
		
		String [] allowParticipantsSelectBiddingCurrency = retrieve.getEventRules("Allow participants to select bidding currency").split(";",-1);
		populateRadioButton("Allow participants to select bidding currency", allowParticipantsSelectBiddingCurrency[0]);
		populateVisibility("Allow participants to select bidding currency", allowParticipantsSelectBiddingCurrency[1]);
		
		if (allowParticipantsSelectBiddingCurrency[0].equals("Yes")){
			String [] showCurrencyExchangeRatesToParticipants = retrieve.getEventRules("Show currency exchange rates to participants").split(";",-1);
			populateRadioButton("Show currency exchange rates to participants", showCurrencyExchangeRatesToParticipants[0]);
			populateVisibility("Show currency exchange rates to participants", showCurrencyExchangeRatesToParticipants[1]);
		}
		
	}
	
	public void projectOwnerActions_RFI(){
		parseExcel retrieve = new parseExcel();
		
		String [] canProjectOwnerCreateFormulas = retrieve.getEventRules("Can project owner create formulas").split(";",-1);
		populateDropdown("Can project owner create formulas", canProjectOwnerCreateFormulas[0]);
		
		String [] canProjectOwnerCreateResponseTeamByDefault = retrieve.getEventRules("Can Project owner create response team by default").split(";",-1);
		populateRadioButton("Can Project owner create response team by default", canProjectOwnerCreateResponseTeamByDefault[0]);
		populateVisibility("Can Project owner create response team by default", canProjectOwnerCreateResponseTeamByDefault[1]);
	}
	
	
	public void marketFeedback_RFI() {
		
		parseExcel retrieve = new parseExcel();
		
		String [] specifyHowParticipantsViewMarketInformation = retrieve.getEventRules("Specify how participants view market information").split(";",-1);
		populateDropdown("Specify how participants view market information", specifyHowParticipantsViewMarketInformation[0]);
		populateVisibility("Specify how participants view market information", specifyHowParticipantsViewMarketInformation[1]);
		
		String [] showParticipantResponsesToOtherParticipants = retrieve.getEventRules("Show participant responses to other participants").split(";",-1);
		populateDropdown("Show participant responses to other participants", showParticipantResponsesToOtherParticipants[0]);
		populateVisibility("Show participant responses to other participants", showParticipantResponsesToOtherParticipants[1]);
		
		String [] hideTheNumberOfBidders = retrieve.getEventRules("Hide the number of bidders by using the same participant alias").split(";",-1);
		populateDropdown("Hide the number of bidders by using the same participant alias", hideTheNumberOfBidders[0]);
		populateVisibility("Hide the number of bidders by using the same participant alias", hideTheNumberOfBidders[1]);
		
		String [] showLeadBidToAllParticipants = retrieve.getEventRules("Show lead bid to all participants").split(";",-1);
		populateDropdown("Show lead bid to all participants", showLeadBidToAllParticipants[0]);
		populateVisibility("Show lead bid to all participants", showLeadBidToAllParticipants[1]);
		
		String [] showReservePriceToAllParticipants = retrieve.getEventRules("Show reserve price to all participants").split(";",-1);
		populateDropdown("Show reserve price to all participants", showReservePriceToAllParticipants[0]);
		populateVisibility("Show reserve price to all participants", showReservePriceToAllParticipants[1]);
		
		String [] canParticipantsSeeRanks = retrieve.getEventRules("Can participants see ranks?").split(";",-1);
		populateDropdown("Can participants see ranks?", canParticipantsSeeRanks[0]);
		populateVisibility("Can participants see ranks?", canParticipantsSeeRanks[1]);
		
		String [] showLineItemLevelRankInALot = retrieve.getEventRules("Show Line Item level rank in Lot").split(";",-1);
		populateDropdown("Show Line Item level rank in Lot", showLineItemLevelRankInALot[0]);
		populateVisibility("Show Line Item level rank in Lot", showLineItemLevelRankInALot[1]);
		
		String [] showCalculatedValueOfCompetitiveTerm = retrieve.getEventRules("Show calculated value of competitive term before participant submits bid").split(";",-1);
		populateRadioButton("Show calculated value of competitive term before participant submits bid", showCalculatedValueOfCompetitiveTerm[0]);
		populateVisibility("Show calculated value of competitive term before participant submits bid", showCalculatedValueOfCompetitiveTerm[1]);
		
		String [] showFormulasToAllParticipants = retrieve.getEventRules("Show formulas to all participants").split(";",-1);
		populateRadioButton("Show formulas to all participants", showFormulasToAllParticipants[0]);
		populateVisibility("Show formulas to all participants", showFormulasToAllParticipants[1]);
		
		String [] indicateParticipantSpecificInitialValues = retrieve.getEventRules("Indicate to participants that participant-specific initial values have been specified").split(";",-1);
		populateRadioButton("Indicate to participants that participant-specific initial values have been specified", indicateParticipantSpecificInitialValues[0]);
		populateVisibility("Indicate to participants that participant-specific initial values have been specified", indicateParticipantSpecificInitialValues[1]);
	
		String [] allowParticipantsToSeeScoringWeights = retrieve.getEventRules("Allow participants to see scoring weights").split(";",-1);
		populateRadioButton("Allow participants to see scoring weights", allowParticipantsToSeeScoringWeights[0]);
		populateVisibility("Allow participants to see scoring weights", allowParticipantsToSeeScoringWeights[1]);
	
	
	}
	
	public void marketFeedback_RFP() {
		
		parseExcel retrieve = new parseExcel();
		
		String [] specifyHowParticipantsViewMarketInformation = retrieve.getEventRules("Specify how participants view market information").split(";",-1);
		populateDropdown("Specify how participants view market information", specifyHowParticipantsViewMarketInformation[0]);
		populateVisibility("Specify how participants view market information", specifyHowParticipantsViewMarketInformation[1]);
		
		String [] showParticipantResponsesToOtherParticipants = retrieve.getEventRules("Show participant responses to other participants").split(";",-1);
		populateDropdown("Show participant responses to other participants", showParticipantResponsesToOtherParticipants[0]);
		populateVisibility("Show participant responses to other participants", showParticipantResponsesToOtherParticipants[1]);
		
		String [] showLeadBidToAllParticipants = retrieve.getEventRules("Show lead bid to all participants").split(";",-1);
		if (showParticipantResponsesToOtherParticipants[0].isEmpty() || showParticipantResponsesToOtherParticipants[0].equals("After participant's first response is accepted")){
			String [] hideTheNumberOfBidders = retrieve.getEventRules("Hide the number of bidders by using the same participant alias").split(";",-1);
			populateDropdown("Hide the number of bidders by using the same participant alias", hideTheNumberOfBidders[0]);
			populateVisibility("Hide the number of bidders by using the same participant alias", hideTheNumberOfBidders[1]);
		}else{
			
			populateDropdown("Show lead bid to all participants", showLeadBidToAllParticipants[0]);
			populateVisibility("Show lead bid to all participants", showLeadBidToAllParticipants[1]);
			
		}
		
		String [] showReservePriceToAllParticipants = retrieve.getEventRules("Show reserve price to all participants").split(";",-1);
		populateDropdown("Show reserve price to all participants", showReservePriceToAllParticipants[0]);
		populateVisibility("Show reserve price to all participants", showReservePriceToAllParticipants[1]);
		
		String [] canParticipantsSeeRanks = retrieve.getEventRules("Can participants see ranks?").split(";",-1);
		populateDropdown("Can participants see ranks?", canParticipantsSeeRanks[0]);
		populateVisibility("Can participants see ranks?", canParticipantsSeeRanks[1]);
		
		String [] showLineItemLevelRankInALot = retrieve.getEventRules("Show Line Item level rank in Lot").split(";",-1);
		populateDropdown("Show Line Item level rank in Lot", showLineItemLevelRankInALot[0]);
		populateVisibility("Show Line Item level rank in Lot", showLineItemLevelRankInALot[1]);
		
		String [] showCalculatedValueOfCompetitiveTerm = retrieve.getEventRules("Show calculated value of competitive term before participant submits bid").split(";",-1);
		populateRadioButton("Show calculated value of competitive term before participant submits bid", showCalculatedValueOfCompetitiveTerm[0]);
		populateVisibility("Show calculated value of competitive term before participant submits bid", showCalculatedValueOfCompetitiveTerm[1]);
		
		String [] showFormulasToAllParticipants = retrieve.getEventRules("Show formulas to all participants").split(";",-1);
		populateRadioButton("Show formulas to all participants", showFormulasToAllParticipants[0]);
		populateVisibility("Show formulas to all participants", showFormulasToAllParticipants[1]);

		if (!showLeadBidToAllParticipants[0].equals("No")){
			String [] showBidGraphToAllParticipants = retrieve.getEventRules("Show bid graph to all participants").split(";",-1);
			populateRadioButton("Show bid graph to all participants", showBidGraphToAllParticipants[0]);
			populateVisibility("Show bid graph to all participants", showBidGraphToAllParticipants[1]);
		}
		
		String [] indicateParticipantSpecificInitialValues = retrieve.getEventRules("Indicate to participants that participant-specific initial values have been specified").split(";",-1);
		populateRadioButton("Indicate to participants that participant-specific initial values have been specified", indicateParticipantSpecificInitialValues[0]);
		populateVisibility("Indicate to participants that participant-specific initial values have been specified", indicateParticipantSpecificInitialValues[1]);
	
		String [] allowParticipantsToSeeScoringWeights = retrieve.getEventRules("Allow participants to see scoring weights").split(";",-1);
		populateRadioButton("Allow participants to see scoring weights", allowParticipantsToSeeScoringWeights[0]);
		populateVisibility("Allow participants to see scoring weights", allowParticipantsToSeeScoringWeights[1]);
	
	
	}
	
	
	
	
	public void messageBoard() {
		
		parseExcel retrieve = new parseExcel();
		
		String [] emailAddressUsed = retrieve.getEventRules("Email address used for the ''From'' and ''Reply To'' fields in emails to participants").split(";",-1);
		String [] emailAddressUsedValue = emailAddressUsed[0].split("\\|");
		populateDropdown("Email address used for the 'From' and 'Reply To' fields in emails to participants", emailAddressUsedValue[0]);
		populateVisibility("Email address used for the 'From' and 'Reply To' fields in emails to participants", emailAddressUsed[1]);
		
		if (emailAddressUsedValue[0].equals("Other email address")){
			inputText(Element.txtOtherEmailAddress, emailAddressUsedValue[1]);
		}
		
		String [] allowParticipantsToSendMessages = retrieve.getEventRules("Allow participants to send messages to project team").split(";",-1);
		populateDropdown("Allow participants to send messages to project team", allowParticipantsToSendMessages[0]);
		
		if (allowParticipantsToSendMessages[0].isEmpty() || allowParticipantsToSendMessages[0].equals("Yes")){
			String [] messageBoardOpeningTime = retrieve.getEventRules("Message board opening time").split(";",-1);
			populateDropdown("Message board opening time", messageBoardOpeningTime[0]);
			
			String [] messageBoardClosingTime = retrieve.getEventRules("Message board closing time").split(";",-1);
			populateDropdownAlt("Message board closing time", messageBoardClosingTime[0]);
			populateVisibility("Message board closing time", messageBoardClosingTime[1]);
		}
		
		String [] chooseWhoMustAccessTheEventMessageBoard = retrieve.getEventRules("Choose who must access the event message board to view user created messages").split(";",-1);
		populateDropdown("Choose who must access the event message board to view user created messages", chooseWhoMustAccessTheEventMessageBoard[0]);
		populateVisibility("Choose who must access the event message board to view user created messages", chooseWhoMustAccessTheEventMessageBoard[1]);
		
		String [] chooseDefaultRecipientsForEmails = retrieve.getEventRules("Choose the default recipients for emails sent to team members").split(";",-1);
		populateDropdown("Choose the default recipients for emails sent to team members", chooseDefaultRecipientsForEmails[0]);
		populateVisibility("Choose the default recipients for emails sent to team members", chooseDefaultRecipientsForEmails[1]);
		
		String [] disableSystemNotifications = retrieve.getEventRules("Disable system notifications for participants who have submitted responses").split(";",-1);
		populateRadioButton("Disable system notifications for participants who have submitted responses", disableSystemNotifications[0]);
		populateVisibility("Disable system notifications for participants who have submitted responses", disableSystemNotifications[1]);
	}
	
	
	public void includeBidderAgreement(){
		
		parseExcel retrieve = new parseExcel();
		String [] includeBidderAgreementAsPrerequisite = retrieve.getEventRules("Would you like to include the bidder agreement as a prerequisite?").split(";",-1);
		populateDropdown("Would you like to include the bidder agreement as a prerequisite?", includeBidderAgreementAsPrerequisite[0]);
		populateVisibility("Would you like to include the bidder agreement as a prerequisite?", includeBidderAgreementAsPrerequisite[1]);
		
	}
	
	
	public void openEventTemplate(String eventType){
		
		switch (eventType){
		case "RFI":
			explicitWait(By.xpath("//span[@title='RFI template']"), 5);
			click(By.xpath("//span[@title='RFI template']"));
			if (explicitWait(By.xpath("//button/span[contains(text(),'Next')]"), 2) != null){
				writeToLogs("Configure the Even Rules...");
			}else{
				click(By.xpath("//span[@title='RFI template']"));
			}
			break;
		case "RFP":
			explicitWait(By.xpath("//span[@title='RFP template']"), 5);
			click(By.xpath("//span[@title='RFP template']"));
			if (explicitWait(By.xpath("//button/span[contains(text(),'Next')]"), 2) != null){
				writeToLogs("Configure the Even Rules...");
			}else{
				click(By.xpath("//span[@title='RFP template']"));
			}
			break;
		case "Auction":
		case "Forward Auction":
			explicitWait(By.xpath("//span[@title='RFQ template']"), 5);
			click(By.xpath("//span[@title='RFQ template']"));
			if (explicitWait(By.xpath("//button/span[contains(text(),'Next')]"), 2) != null){
				writeToLogs("Configure the Even Rules...");
			}else{
				click(By.xpath("//span[@title='RFQ template']"));
			}
			break;
		}
		
		

	}
	
	
	public void scrollAndClick(By by)
	{
	   
	   WebElement element = driver.findElement(by);
	   int elementPosition = element.getLocation().getY();
	   String js = String.format("window.scroll(0, %s)", elementPosition-150);
	   ((JavascriptExecutor)driver).executeScript(js);
	   element.click();
	   
	   waitFor(1);
	}
	
	
	
	
	/*-------------Gab-------------*/
	public void populateCommodity(String field, String value) {
		try{
			if (!value.isEmpty()){
				int totalLabel = driver.findElements(By.xpath("//td/label")).size();
				String fieldName;
				for (int i=0;i<totalLabel;i++){
					WebElement lblField = driver.findElements(By.xpath("//td/label")).get(i);
					fieldName = lblField.getText().trim();
					if (!fieldName.isEmpty()){
						fieldName = fieldName.substring(0, fieldName.length()-1).trim();
						if (fieldName.equals(field)){
							
							click(By.xpath("(//td/label)["+ (i+1) +"]/../following-sibling::td[2]//a/div/div"));
							click(Element.lnkSearchMore);
							inputText(Element.txtSearchField, value);
							click(Element.btnSearchField);
							waitFor(2);
							if (explicitWait(By.xpath("//button[@title='Select this value for the field']"),5) != null){
								click(By.xpath("//button[@title='Select this value for the field']"));
								waitFor(2);
							}else{
								click(Element.btnDoneSearch);
								writeToLogs("[INFO] Cannot find " +value+ " on " + field);
							}
							break;
						}
					}
				}
			}
		}catch(Exception e){
			writeToLogs("[INFO] Unable to populate the field " + field);
		}
	}
	
	
	
	
	public void addKPI(String content){

		//String name, String description, String kpiType, String kpiSource,  String valueType, String numberDecimalPlaces, String acceptValues, String documentFile, String visibleToSupplier, String teamAccessControl, String rangeLower, String rangeUpper
		
		String [] kpi = content.split("\\^", -1);
		String parentContent = kpi[1].trim();
		
		String name = kpi[2].trim();
		String description = kpi[3].trim();
		String kpiType = kpi[6].trim();
		String kpiSource = kpi[7].trim();
		String valueType = kpi[8].trim();
		String numberDecimalPlaces = kpi[9].trim();
		String acceptValues = kpi[10].trim();
		String documentFile = kpi[14].trim();
		String visibleToSupplier = kpi[4].trim();
		String teamAccessControl = kpi[5].trim();
		String rangeLower = kpi[11].trim();
		String rangeUpper = kpi[12].trim();
		String reportMetric = kpi[13].trim();
		String subContent = kpi[15].trim();
		
		boolean createKPIunderKPI = false;
		
		/*if (!parentContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'KPI')]"));
			createKPIunderKPI = true;
		}else{
			click(Element.btnAdd);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'KPI')]"));
		}*/
		
		if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
			click(Element.btnAdd);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'KPI')]"));
			waitForButtonToExist("OK", 5);
			populateTextField("Name", parentContent);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'KPI')]"));
			waitForButtonToExist("OK", 5);
			populateTextField("Name", name);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'KPI')]"));
			waitForButtonToExist("OK", 5);
			populateTextField("Name", subContent);
		}
		
//		populateTextField("Name", name);
		inputDescription(Element.txtProjectDescription, description);
		
		
		writeToLogs(">>KPI Type: " + kpiType);
		switch (kpiType){
		case "This KPI will contain supporting data (KPIs, questions, and/or requirements)":
			if (!createKPIunderKPI){
				click(Element.rdoKPISupportingData);
				waitFor(2);
			}
			break;
			
		case "This KPI will be based on Survey or Report Data":
			if (!createKPIunderKPI){
				click(Element.rdoKPIBasedonSurvey);
				waitFor(3);
			}
			populateDropdown("KPI Source", kpiSource);
			
			switch (kpiSource){
			case "Survey":
				waitFor(2);
				populateDropdownAlt("Value Type", valueType);
				populateTextField("Number of decimal places", numberDecimalPlaces);
				populateDropdownAlt("Acceptable Values", acceptValues);
				
				if (!documentFile.isEmpty()){
					waitFor(2);
					sendKeysEnter(Element.lnkAttFile);
					click(Element.lnkUpdateFromDesktop);
					uploadFile(documentFile);
					click(Element.btnOK);
					waitForButtonToExist("Done", 60);
				}

				break;
				
			case "Report":
				
				sendKeysEnter(Element.lnkReport);
				waitFor(3);
				waitForButtonToExist("Cancel", 5);
				
				if (isElementVisible(By.linkText("Vault"), 5)){
					click(By.linkText("Vault"));
					waitFor(3);
				}

				// Vault   >   Knowledge Areas   >   Prepackaged Reports   >   Event Reports   >  Detailed Reports > Accepted Suppliers Summary > Supplier Count 
				 String[] c = reportMetric.split("\\>");
	
		        for (int i = 0; i < c.length; i++) {
		        		
		        	  System.out.println(c[i].trim());
		        	  if (c[i].trim().contentEquals("Vault")){
		        		  i = i+1;
		        	  }
		        	  
		        	  if (i == c.length - 1) {
		        		  if (isElementVisible(By.linkText(c[i].trim()),5)) {
		        			  click(By.linkText(c[i].trim()));
		        			  break;
	                       } else {
	                              writeToLogs("[ERROR]" + c[i].trim() + " is not available");
	                       }
		        	  }
		        	  
		        	  if (isElementVisible(By.xpath("//span[contains(.,'" + c[i].trim() + "')]"), 5)) {
                            click(By.xpath("//span[contains(.,'" + c[i].trim() + "')]"));
                            waitFor(2);
		        	  } else {
                            writeToLogs("[ERROR]" + c[i].trim() + " is not available");
		        	  }
	                  
//	                  if (isElementVisible(By.xpath("//span[contains(.,'" + c[i].trim() + "')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"), 5)) {
//	                         click(By.xpath("//span[contains(.,'" + c[i].trim() + "')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"));
//	                  }
	                  
	                  
		        }
		            
		            
	            waitFor(2);
				populateDropdownAlt("Value Type", valueType);
				populateTextField("Number of decimal places", numberDecimalPlaces);
				populateDropdownAlt("Acceptable Values", acceptValues);
				
				if (!documentFile.isEmpty()){
					waitFor(2);
					sendKeysEnter(Element.lnkAttFile);
					click(Element.lnkUpdateFromDesktop);
					uploadFile(documentFile);
					click(Element.btnOK);
					waitForButtonToExist("Done", 60);
				}  
		            
				 
				break;
			}
			
		}
		
		
		if (acceptValues.equals("Limited Range")){
			inputText(Element.txtRangeLow, rangeLower);
			inputText(Element.txtRangeUp, rangeUpper);
		}
		
		populateDropdown("Visible to Supplier", visibleToSupplier);
		waitFor(3);
		populateChooserMultiple("Team Access Control", teamAccessControl);
		
		clickButton("Done");
	}
	
	//Done!!
	
	/*
	 * Function for Add Lot
	 */

	public void addLot (String content){
		
		//String name, String description, String commod, String lotType, String visibleToParticipant, String teamAccessControl, String customOfflineResponse, String requiredYesNo, String applyAllItems, String requiredResponseYesNo
		String [] lot = content.split("\\^", -1);
		String parentContent = lot[1].trim();
		String name = lot[2].trim();
		String description = lot[3].trim();
		String commod = lot[6].trim();
		String lotType = lot[7].trim();
		String visibleToParticipant = lot[4].trim();
		String teamAccessControl = lot[5].trim();
		String customOfflineResponse = lot[10].trim();
//		String requiredYesNo = lot[1].trim();
		String applyAllItems = lot[9].trim();
		String requiredResponseYesNo = lot[8].trim();
		String subContent = lot[14].trim();
		
		/*if (!parentContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Lot')]"));
		}else{
			click(Element.btnAdd);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Lot')]"));
		}*/
		
		
		if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
			click(Element.btnAdd);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Lot')]"));
			waitForButtonToExist("OK", 5);
			populateTextField("Name", parentContent);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Lot')]"));
			waitForButtonToExist("OK", 5);
			populateTextField("Name", name);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Lot')]"));
			waitForButtonToExist("OK", 5);
			populateTextField("Name", subContent);
		}
		
		
		
//		populateTextField("Name", name);
		inputDescription(Element.txtProjectDescription, description);
		waitFor(3);
		populateCommodity("Commodity", commod);
		
		writeToLogs(">>Lot Type: " + lotType);
		switch (lotType){
		case "Item Lot - Bid at Item level, compete at Lot level (collect item pricing during bidding)":
			click(Element.rdoItemLot);
			break;

		case "Basket - Bid at Lot level, compete at Lot level (collect item pricing post bidding)":
			click(Element.rdoBasket);
			break;
		
		case "Basket with No Items - Bid at Lot level, compete at Lot level (do not collect item pricing)":
			click(Element.rdoBasketNoItems);
			break;
				
		case "Bundle - Bid discounted value at Item level, compete at Lot level (collect item pricing during bidding)":
			click(Element.rdoBundle);
			break;
					
		}
		
		waitFor(3);
		populateDropdown("Visible to Participant", visibleToParticipant);
		waitFor(2);
		
		if (lotType.equals("Basket with No Items - Bid at Lot level, compete at Lot level (do not collect item pricing)")){
			/*if (customOfflineResponse.equals("Yes")){
				writeToLogs(">>Custom Offline Response: Yes");
				click(Element.btnDropDown);
				click(Element.lnkYesCustom);
			}*/
			populateDropdownAlt("Customized Offline Response", customOfflineResponse);
		}

		populateChooserMultiple("Team Access Control", teamAccessControl);
		waitFor(2);
				
		waitFor(2);
		
		populateRadioButton("Response required for this item or lot", requiredResponseYesNo);
				
		if (applyAllItems.equals("Yes")){
			click(Element.chkApplyToAll);	
		}

		
		clickButton("Done");
	} 
	
	//Done!!
	
	//Add Question
	
	public void addQuestion (String content){
		
		//String name, String includeInCost, String prereqQuestion, String reviewResponse, String answerType, String acceptValue, String numberDecimal, String visibleParticipant, String responseRequired, String addComAtt, String hideResponses, String attachFile, String refDocument, String specInitialValues, String teamAccessControl, String initialValue, String rangeLower, String rangeUpper
		String [] question = content.split("\\^", -1);
		String parentContent = question[1].trim();

		String name = question[2].trim();
		String includeInCost = question[5].trim();
		String prereqQuestion = question[6].trim();
		String reviewResponse = question[7].trim();
		String answerType = question[8].trim();
		String acceptValue = question[10].trim();

		String numberDecimal = question[9].trim();
		String visibleParticipant = question[3].trim();
		String responseRequired = question[11].trim();
		String addComAtt = question[13].trim();
		String hideResponses = question[12].trim();
		
		String attachFile = question[23].trim();
		String searchFile = question[24].trim();
//		String exploreFile = question[25].trim();
		
		String specInitialValues = question[14].trim();
		String teamAccessControl = question[4].trim();
		String initialValue = question[16].trim();
		String rangeLower = question[17].trim();
		String rangeUpper = question[18].trim();
		
		String specifyOtherValue = question[19].trim();
		String selectMultipleValues = question[20].trim();
		String valueListOfChoices = question[21].trim();
		
		String subContent = question[26].trim();
		String readOnly = question[27].trim();
		
		if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
			click(Element.btnAdd);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Question')]"));
			//Question name
			inputDescription(Element.txtProjectDescription, parentContent);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Question')]"));
			//Question name
			inputDescription(Element.txtProjectDescription, name);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Question')]"));
			//Question name
			inputDescription(Element.txtProjectDescription, subContent);
		}
		
		//include in cost
		populateDropdown("Include in cost", includeInCost);
		
		//prereq question
		waitFor(2);
		populateDropdown("Is this a prerequisite question to continue with the event?", prereqQuestion);

		if (prereqQuestion.equals("Yes, with an access gate on event content") || prereqQuestion.equals("Yes, restricting response submission")){
			waitFor(2);
			populateDropdownAlt("Owner must review responses before participants can continue with event", reviewResponse);
		}
		
		//answer type
		populateDropdown("Answer Type", answerType);
		waitFor(2);
		
		//added 06-16-2017
		populateRadioButton("Make this term as read-only?", readOnly);
		
		switch (answerType){
		case "Text (single line limited)":
			populateDropdown("Acceptable Values", acceptValue);
			break;
		case "Text (single line)":
			populateDropdown("Acceptable Values", acceptValue);
			break;
		case "Text (multiple lines)":
			break;
		case "Whole Number":
			populateDropdown("Acceptable Values", acceptValue);
			break;
		case "Decimal Number":
			populateDropdown("Acceptable Values", acceptValue);
			populateTextField("Number of decimal places", numberDecimal);
			break;
		case "Date":
			populateDropdown("Acceptable Values", acceptValue);
			break;
		case "Money":
			populateDropdown("Acceptable Values", acceptValue);
			populateTextField("Number of decimal places", numberDecimal);
			break;
		case "Yes/No":
			break;	
		case "Certificate":
			break;
		case "Address":
			break;
		case "Percentage":
			populateDropdown("Acceptable Values", acceptValue);
			waitFor(2);
			populateTextField("Number of decimal places", numberDecimal);
			break;
		case "Quantity":
			populateDropdown("Acceptable Values", acceptValue);
			break;
		}
		
		//response required
		populateDropdown("Response Required?", responseRequired);
		
		
		//uploading

		if (!attachFile.isEmpty()){
			sendKeysEnter(Element.lnkRefDoc);
			click(Element.lnkUpdateDesktop);
			uploadFile(attachFile);
			click(Element.btnOK);
			waitForButtonToExist("Done", 60);
		}
		
		if (!searchFile.isEmpty()){
			sendKeysEnter(Element.lnkRefDoc);
			click(Element.lnkSelectFromLibrary);
			click(Element.rdoSearch);
			inputText(Element.txtSearchTerm, searchFile);
			click(Element.btnSearchDoc);
			click(Element.chkFirstSelection);
			waitFor(2);
			clickButton("OK");
			waitFor(2);
		}
		
		/*
		switch (attachFile){
		case "Desktop":
			click(Element.lnkUpdateDesktop);
			uploadFile(refDocument);
			click(Element.btnOK);
			waitForButtonToExist("Done", 60);
			break;
		case "Library":
			click(Element.rdoSearch);
			inputText(Element.txtSearchTerm, refDocument);
			click(Element.btnSearchDoc);
			click(Element.chkFirstSelection);
			waitFor(2);
			clickButton("OK");
			waitFor(2);
		}
		*/
		
		
		//visible to participant
		populateDropdownAlt("Visible to Participant", visibleParticipant);
		
		//hide participants response
		populateDropdownAlt("Hide participants' responses from each other", hideResponses);
		
		if (responseRequired.equals("Yes, Participant Required")){
			populateDropdownAlt("Participant can add additional comments and attachments", addComAtt);
		}
		
		
		//participant
		populateDropdownAlt("Use participant-specific initial values?", specInitialValues);
		
		//team access control
		populateChooserMultiple("Team Access Control", teamAccessControl);
		
		populateRadioButton("Allow participants to specify other value?", specifyOtherValue);
		populateRadioButton("Allow participants to select multiple values?", selectMultipleValues);
		
		
		//initial value
		switch (answerType){
		case "Text (single line limited)":
			switch(acceptValue){
			case "Any Value":
				populateTextField("Initial Value", initialValue);
				break;
				
			case "List of Choices":
				String [] choices = valueListOfChoices.split("\\|");
								
				for (int i = 1; i < choices.length; i++) {
					click(Element.btnAdd);
				}
				
				for (int i = 0; i < choices.length - 1; i++){
					inputText(By.xpath("(//table[@class='tableBody']//input[@type='text'])["+(i+1)+"]"), choices[i]);
				}
				break;
			}
			break;	
			
		case "Text (single line)":
			switch(acceptValue){
			case "Any Value":
				populateTextField("Initial Value", initialValue);
				break;
			case "List of Choices":
				String [] choices = valueListOfChoices.split("\\|", -1);
				
//				for (int i = 1; i < choices.length; i++) {
//					click(Element.btnAdd);
//				}
				
				for (int i = 0; i < choices.length - 1; i++){
					if (i>0){
						click(Element.btnAdd);
					}
					inputText(By.xpath("(//table[@class='tableBody']//input[@type='text'])["+(i+1)+"]"), choices[i]);
				}
				break;
			}
			break;
			
		case "Text (multiple lines)":
			populateTextArea("Initial Value", initialValue);
			break;
			
		case "Whole Number":
			switch(acceptValue){
			case "Any Value":
				populateTextField("Initial Value", initialValue);
				break;
			case "List of Choices":
				String [] choices = valueListOfChoices.split("\\|");
				
				for (int i = 1; i < choices.length; i++) {
					click(Element.btnAdd);
				}
				
				for (int i = 0; i < choices.length - 1; i++){
					inputText(By.xpath("(//table[@class='tableBody']//input[@type='text'])["+(i+1)+"]"), choices[i]);
				}
				break;
			case "Limited Range":
				populateTextField("Initial Value", initialValue);
				inputText(Element.txtRangeLower, rangeLower);
				inputText(Element.txtRangeUpper, rangeUpper);
				break;
			}
			break;
			
		case "Decimal Number":
			switch(acceptValue){
			case "Any Value":
				populateTextField("Initial Value", initialValue);
				break;
			case "List of Choices":
				String [] choices = valueListOfChoices.split("\\|");
				
				for (int i = 1; i < choices.length; i++) {
					click(Element.btnAdd);
				}
				
				for (int i = 0; i < choices.length - 1; i++){
					inputText(By.xpath("(//table[@class='tableBody']//input[@type='text'])["+(i+1)+"]"), choices[i]);
				}
				break;
			case "Limited Range":
				populateTextField("Initial Value", initialValue);
				inputText(Element.txtRangeLower, rangeLower);
				inputText(Element.txtRangeUpper, rangeUpper);
				break;
			}
			break;
		case "Date":
			populateTextField("Initial Value", initialValue);
			break;
		case "Money":
			switch(acceptValue){
			case "Any Value":
				populateTextField("Initial Value", initialValue);
				break;
			case "List of Choices":
				String [] choices = valueListOfChoices.split("\\|");
				
				for (int i = 1; i < choices.length; i++) {
					click(Element.btnAdd);
				}
				
				for (int i = 0; i < choices.length - 1; i++){
					inputText(By.xpath("(//table[@class='tableBody']//input[@type='text'])["+(i+1)+"]"), choices[i]);
				}
				break;
			case "Limited Range":
				populateTextField("Initial Value", initialValue);
				inputText(Element.txtRangeLower, rangeLower);
				inputText(Element.txtRangeUpper, rangeUpper);
				break;
			}
			break;
		case "Yes/No":
			populateDropdown("Initial Value", initialValue);
			break;	
		case "Certificate":
			populateDropdown("Initial Value", initialValue);
			break;
		case "Address":
			break;
		case "Percentage":
			switch(acceptValue){
			case "Any Value":
				populateTextField("Initial Value", initialValue);
				break;
			case "List of Choices":
				String [] choices = valueListOfChoices.split("\\|");
				
				for (int i = 1; i < choices.length; i++) {
					click(Element.btnAdd);
				}
				
				for (int i = 0; i < choices.length - 1; i++){
					inputText(By.xpath("(//table[@class='tableBody']//input[@type='text'])["+(i+1)+"]"), choices[i]);
				}
				break;
			case "Limited Range":
				populateTextField("Initial Value", initialValue);
				inputText(Element.txtRangeLower, rangeLower);
				inputText(Element.txtRangeUpper, rangeUpper);
				break;
			}
			break;
		case "Quantity":
			switch(acceptValue){
			case "Any Value":
				populateTextField("Initial Value", initialValue);
				break;
			case "List of Choices":
				String [] choices = valueListOfChoices.split("\\|");
				
				for (int i = 1; i < choices.length; i++) {
					click(Element.btnAdd);
				}
				
				for (int i = 0; i < choices.length - 1; i++){
					inputText(By.xpath("(//table[@class='tableBody']//input[@type='text'])["+(i+1)+"]"), choices[i]);
				}
				break;
			case "Limited Range":
				populateTextField("Initial Value", initialValue);
				inputText(Element.txtRangeLower, rangeLower);
				inputText(Element.txtRangeUpper, rangeUpper);
				break;
			}
			break;
		}
		
		waitFor(3);
		clickButton("Done");
	}
	
	//Add Attachments from Library - TEST
	
	public void addAttachmentLibrary (String searchOrExplore, String searchTerm){
		
		//String searchOrExplore, String searchTerm
		
		
		switch (searchOrExplore){
		case "Search":
			click(Element.rdoSearch);
			waitFor(2);
			inputText(Element.txtSearchTerm, searchTerm);
			click(Element.btnSearchDoc);
			waitFor(2);
			click(Element.chkFirstSelection);
			clickButton("OK");
			clickButton("Done");
		break;
		
		case "Explore":
			click(Element.rdoExplore);
			waitFor(5);
			//Contract Workspaces > 2017 > Mar > CW222xxxx > CW22216xx > CW 0303 > Contract Documents > Main Agreement
			String [] c = searchTerm.split("\\>");

            for (int i = 0; i < c.length; i++) {

                  if (i == c.length - 1) { // -2 for report
                         if (isElementVisible(By.xpath("//span[contains(.,'" + c[i].trim() + "')]"), 5)) {
                                click(By.xpath("//span[contains(.,'" + c[i].trim() + "')]"));
                                waitFor(2);
                                break;
                         } else {
                                writeToLogs("[ERROR]" + c[i].trim() + " is not available");
                         }
                  }

                  if (isElementVisible(By.xpath("//span[contains(.,'" + c[i].trim() + "')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"), 5)) {
                         scrollAndClick(By.xpath("//span[contains(.,'" + c[i].trim() + "')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"));
                         waitFor(2);
                  } 
             
            }
            
//            click(Element.chkDoc);
            waitFor(2);
            click(Element.btnOK);
            waitForButtonToExist("Done", 5);
        	break;
		}
		
		
		
	}
	// Done!
	
	//Add Requirement
	
	public void addRequirement (String content){
		
		//String name, String refDocument, String desktopOrLibrary, String visibleParticipant, String teamAccessControl, String searchOrExplore, String searchTerm
		String [] req = content.split("\\^", -1);
		String parentContent = req[1].trim();
		
		String name = req[2].trim();
		
		String visibleParticipant = req[4].trim();
		String teamAccessControl = req[5].trim();
		String attachFile = req[6].trim();
		String searchFile = req[7].trim();
		String exploreFile = req[8].trim();
		String subContent = req[9].trim();
		

		if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
			click(Element.btnAdd);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Requirement')]"));
			inputDescription(Element.txtProjectDescription, parentContent);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Requirement')]"));
			inputDescription(Element.txtProjectDescription, name);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Requirement')]"));
			inputDescription(Element.txtProjectDescription, subContent);
		}

		
		/*
		switch (desktopOrLibrary){
		case "Desktop":
			click(Element.lnkDesktopUpdate);
			uploadFile(refDocument);
			click(Element.btnOK);
			break;
		case "Library":
			click(Element.lnkSelectLibrary);
			addAttachmentLibrary(searchOrExplore, searchTerm);
			break;
		}
		*/
		
		
		if (!attachFile.isEmpty()){
			sendKeysEnter(Element.lnkRefDocument);
			click(Element.lnkUpdateDesktop);
			uploadFile(attachFile);
			click(Element.btnOK);
			waitFor(2);
		}
		
		if (!searchFile.isEmpty()){
			sendKeysEnter(Element.lnkRefDocument);
			click(Element.lnkSelectFromLibrary);
			addAttachmentLibrary("Search", searchFile);
			waitFor(2);
		}
		
		if (!exploreFile.isEmpty()){
			sendKeysEnter(Element.lnkRefDocument);
			click(Element.lnkSelectFromLibrary);
			addAttachmentLibrary("Explore", exploreFile);
			waitFor(2);
		}

		// end of upload
		
		populateDropdownAlt("Visible to Participant", visibleParticipant);
		populateChooserMultiple("Team Access Control", teamAccessControl);
		
		waitFor(2);
		clickButton("Done");
	} 
	// Done!
	
	
	//Add Attachment
	
		public void addAttachment (String content){
			
			//String name, String refDocument, String desktopOrLibrary, String visibleParticipant, String teamAccessControl, String searchOrExplore, String searchTerm
			String [] req = content.split("\\^", -1);
			String parentContent = req[1].trim();
			
			String name = req[2].trim();
			
			String visibleParticipant = req[4].trim();
			String teamAccessControl = req[5].trim();
			String attachFile = req[6].trim();
			String searchFile = req[7].trim();
			String exploreFile = req[8].trim();

			if (!parentContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Attachment')]"));

				inputDescription(Element.txtProjectDescription, name);
				
				// upload
				sendKeysEnter(Element.lnkAttachment);
				
				if (!attachFile.isEmpty()){
					click(Element.lnkUpdateDesktop);
					uploadFile(attachFile);
					click(Element.btnOK);
				}
				
				if (!searchFile.isEmpty()){
					click(Element.lnkSelectFromLibrary);
					addAttachmentLibrary("Search", searchFile);
				}
				
				if (!exploreFile.isEmpty()){
					click(Element.lnkSelectFromLibrary);
					addAttachmentLibrary("Explore", exploreFile);
				}
	
				// end of upload
				
				waitFor(2);
				populateDropdownAlt("Visible to Participant", visibleParticipant);
				populateChooserMultiple("Team Access Control", teamAccessControl);
				
				waitFor(2);
				clickButton("Done");
				
			}
		} 
		// Done!
	
	
	//Add Cost Terms - DONE
	
	public void addCostTerms (String content){
		
		//String name, String description, String visibleParticipant, String customOfflineResponse, String teamAccessControl
		
		String [] costTerms = content.split("\\^", -1);
		String parentContent = costTerms[1].trim();
		String name = costTerms[2].trim();
		String description = costTerms[3].trim();
		String visibleParticipant = costTerms[4].trim();
		String customOfflineResponse = costTerms[6].trim();
		String teamAccessControl = costTerms[5].trim();
		String subContent = costTerms[7].trim();
		
		if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
			click(Element.btnAdd);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Cost Terms')]"));
			waitForButtonToExist("Done", 5);
			populateTextField("Name", parentContent);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Cost Terms')]"));
			waitForButtonToExist("Done", 5);
			populateTextField("Name", name);
		}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
			clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Cost Terms')]"));
			waitForButtonToExist("Done", 5);
			populateTextField("Name", subContent);
		}
		
		populateTextField("Name", name);
		inputDescription(Element.txtProjectDescription, description);
		
		waitFor(2);
		populateDropdownAlt("Visible to Participant", visibleParticipant);
		populateDropdownAlt("Customized Offline Response", customOfflineResponse);
		populateChooserMultiple("Team Access Control", teamAccessControl);
		waitFor(2);
		
		clickButton("Done");
	}
	
	
	
	
	/*------------Haziel-------------*/
	
	// Add Section
		public void addSection(String content) {
			
			//String name, String description, String visibleToParticipant, String teamAccessControl, String visibilityCondition, String select, String selectCondition
			
			String [] section = content.split("\\^", -1);
			String parentContent = section[1].trim();
			String name = section[2].trim();
			
			String description = section[3].trim();
			String visibleToParticipant = section[4].trim();
			String teamAccessControl = section[5].trim();
			
			String subContent = section[6].trim();
//			String visibilityCondition = section[2].trim();
//			String select = section[2].trim();
//			String selectCondition = section[2].trim();
			
			if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Section')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", parentContent);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Section')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", name);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Section')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", subContent);
			}
			
			inputDescription(Element.txtProjectDescription, description);
			populateDropdownAlt("Visible to Participant", visibleToParticipant);
			populateChooserMultiple("Team Access Control", teamAccessControl);
			waitFor(2);
			click(Element.btnOK);

			// *************************************Cannot create condition
			// click(Element.lnkVisibilityCondition);
			// switch(visibilityCondition){
			// case "Others":
			// click(Element.lnkOthers);
			// populateDropdown("Select", select);
			// populateTextField("Name", name);
			// click(Element.btnSearchField);
			// populateChooserMultiple("Visibility Condition", selectCondition);
			// clickButton("Done");
			// break;

			// case "Create Condition":
			// click(Element.lnkCreateCondition);
			// some code here
			// clickButton("OK");
			// break;
			// }
			// clickButton("Done");
			// End of Condition******************************************
		}

		// Add Line Item
		public void addLineItem(String content) {
			
			//String name, String description, String commodity, String visibleToParticipant, String customizedOR, String teamAccessControl, String responseRequired, String applyAllItems, String unitBidding, String quantity, String initial, String historic, String reserve
			String [] lineItem = content.split("\\^", -1);
			String parentContent = lineItem[1].trim();
			
			String name = lineItem[2].trim();
			String description = lineItem[3].trim();
			String commodity = lineItem[6].trim();
			String visibleToParticipant = lineItem[4].trim();
			String customizedOR = lineItem[7].trim();
			String teamAccessControl = lineItem[5].trim();
			String responseRequired = lineItem[8].trim();
			String applyAllItems = lineItem[9].trim();
			String unitBidding = lineItem[10].trim();
			String quantity = lineItem[14].trim();
			String initial = lineItem[11].trim();
			String historic = lineItem[12].trim();
			String reserve = lineItem[13].trim();
			String subContent = lineItem[15].trim();
			
			/*if (!parentContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Line Item')]"));
			}else{
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Line Item')]"));
			}*/
			
			
			if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Line Item')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", parentContent);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Line Item')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", name);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Line Item')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", subContent);
			}
			

//			populateTextField("Name", name);
			inputDescription(Element.txtProjectDescription, description);
			waitFor(2);
			populateCommodity("Commodity", commodity);
			populateDropdown("Visible to Praticipant", visibleToParticipant);
			populateDropdown("Customized Offline Response", customizedOR);
			populateChooserMultiple("Team Access Control", teamAccessControl);
			populateRadioButton("Response required for this item or lot", responseRequired);

			if (applyAllItems == "Yes") {
				click(Element.chkApplyToAll);
			}

			switch (unitBidding) {
			case "Partcipant bid per unit (unit bidding)":
				click(Element.rdoUnitBidding);
				populateTextField("Initial", initial);
				populateTextField("Historic", historic);
				populateTextField("Reserve", reserve);
				populateTextField("Quantity", quantity);

			case "Participant bid on all units (extended bidding)":
				click(Element.rdoExtendedBidding);
				populateTextField("Initial", initial);
				populateTextField("Historic", historic);
				populateTextField("Reserve", reserve);
				populateTextField("Quantity", quantity);
			}
			
			waitFor(2);
			clickButton("Done");
		}

		// Attachment from Desktop
		public void addAttachmentsFromDesktop(String content) {
			
			//String filePath, String description
			
			String [] attach = content.split("\\^", -1);
			String parentContent = attach[1].trim();
			String filePath = attach[5].trim();
			String description = attach[2].trim();
			String visibleToParticipant = attach[3].trim();
			String teamAccessControl = attach[4].trim();
			
			if (!parentContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Attachments From Desktop')]"));
			}else{
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Attachments From Desktop')]"));
			}
			
			inputDescription(Element.txtProjectDescription, description);
			uploadFile(filePath);

			populateDropdownAlt("Visible to Participant", visibleToParticipant);
			populateChooserMultiple("Team Access Control", teamAccessControl);
			
			waitFor(2);
			clickButton("Done");
		}

		// Add Formula
		public void addFormula(String content) {
			
			//String name, String formula, String resultType, String numberOfDecimal, String responseRequired, String visibleToParticipant, String visibilityValue, String hideParticipantsResponses,String teamAccessControl, String visible
			String [] strFormula = content.split("\\^", -1);
			String parentContent = strFormula[1].trim();
			
			String name = strFormula[2].trim();
			String formula = strFormula[5].trim();
			String resultType = strFormula[6].trim();
			String numberOfDecimal = strFormula[7].trim();
			String responseRequired = strFormula[8].trim();
			String visibleToParticipant = strFormula[3].trim();
//			String visibilityValue = strFormula[1].trim();
			String hideParticipantsResponses = strFormula[9].trim();
			String teamAccessControl = strFormula[4].trim();
			String visible = strFormula[1].trim();
			String subContent = strFormula[10].trim();
			
			/*if (!parentContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Formula')]"));
			}else{
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Formula')]"));
			}*/
			
			
			if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Formula')]"));
				waitForButtonToExist("OK", 5);
				inputDescription(Element.txtProjectDescription, parentContent);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Formula')]"));
				waitForButtonToExist("OK", 5);
				inputDescription(Element.txtProjectDescription, name);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Formula')]"));
				waitForButtonToExist("OK", 5);
				inputDescription(Element.txtProjectDescription, subContent);
			}
			
			
//			inputDescription(Element.txtProjectDescription, name);
			populateTextArea("Formula", formula);
			clickButton("Validate");
			waitFor(2);
			populateDropdown("Result Type", resultType);
			populateTextField("Number of decimal places", numberOfDecimal);
			waitFor(2);
			populateDropdownAlt("Response Required?", responseRequired);
//			click(By.xpath("//td/label[contains(text(),'Required')]/../following-sibling::td[2]//span[@class='w-dropdown-pic-ct']"));
//			click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(text(),'"+ responseRequired + "')]"));
			
			switch (visible){
			case "Yes":
				populateDropdownAlt("Visible to Paticipant",visibleToParticipant);
//				click(By.xpath("//td/label[contains(text(),'Visible')]/../following-sibling::td[2]//span[@class='w-dropdown-pic-ct']"));
//				click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(text(),'"+ visibleToParticipant + "')]"));
				populateDropdownAlt("Hides participants' responses from each other", hideParticipantsResponses);
//				waitFor(2);
//				click(By.xpath("//td/label[contains(text(),'Hide')]/../following-sibling::td[2]//span[@class='w-dropdown-pic-ct']"));
//				click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(text(),'"+ hideParticipantsResponses + "')]"));
				break;
			case "No":
				populateDropdownAlt("VisibleToParticipant", visibleToParticipant);
//				click(By.xpath("//td/label[contains(text(),'Visible')]/../following-sibling::td[2]//span[@class='w-dropdown-pic-ct']"));
//				click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(text(),'"+ visibleToParticipant + "')]"));
				break;
			}
		// visibility condition
			waitFor(2);
			populateChooserMultiple("Team Access Control", teamAccessControl);
			waitFor(2);
			clickButton("Done");
		}

		
		
		// Add Content from Library
		public void addContentFromLibrary(String content) {
			
			
			//String contentFromLibrary, String selectContent, String externalSystem, String title, String keywords, String from, String projectType, String copyVisibility, String document, String copyParticipant
			
			String [] contentLib = content.split("\\^", -1);
			String parentContent = contentLib[1].trim();
			
			String contentFromLibrary = contentLib[2].trim();
			String selectContent = contentLib[3].trim();
			String externalSystem = contentLib[5].trim();
			String titleKeyword = contentLib[7].trim();
			String searchTerm = contentLib[8].trim();
			String from = contentLib[9].trim();
			String projectType = contentLib[10].trim();
			String copyVisibility = contentLib[4].trim();
//			String document = contentLib[1].trim();
//			String copyParticipant = contentLib[1].trim();
			String name = contentLib[11].trim();
			String subContent = contentLib[12].trim();
			
			waitFor(2);

			/*if (!parentContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Content From Library')]"));
			}else{
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Content From Library')]"));
			}*/
			
			if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Section')]"));
				waitForButtonToExist("OK", 5);
//				populateTextField("Name", parentContent);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Section')]"));
				waitForButtonToExist("OK", 5);
//				populateTextField("Name", name);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Section')]"));
				waitForButtonToExist("OK", 5);
//				populateTextField("Name", subContent);
			}
			
			waitFor(2);
			
			switch (contentFromLibrary) {
			case "Explore Library":
				
				click(Element.rdoExploreLibrary);
				waitFor(2);
				
				//Sourcing Library > Supplier Profile Questionnaire > Export SPQ
				String[] c = selectContent.split("\\>");

				for (int i = 0; i < c.length; i++) {

					if (i == c.length - 1) {
						if (isElementVisible(By.xpath("//span[contains(.,'" + c[i].trim() + "')]"), 5)) {
							click(By.xpath("//span[contains(.,'" + c[i].trim() + "')]"));
							waitFor(2);
							break;
						} else {
							writeToLogs("[ERROR]" + c[i].trim() + " is not available");
						}
					}

					if (isElementVisible(By.xpath("//span[contains(.,'" + c[i].trim() + "')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"), 5)) {
						scrollAndClick(By.xpath("//span[contains(.,'" + c[i].trim() + "')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"));
						waitFor(2);
					}
				}

				clickButton("Select");
				waitFor(2);
				
				if (copyVisibility.equals("No")) {
					click(Element.chkCopyVisibility);
				}

				populateDropdown("External System", externalSystem);
				click(Element.chkAllContent);
				waitFor(4);
				clickButton("Copy");
				break;

			case "Explore Project":
				click(Element.rdoExploreProject);
				waitFor(2);
				String[] d = selectContent.split("\\>");

				for (int i = 0; i < d.length; i++) {

					if (i == d.length - 1) {
						if (isElementVisible(By.xpath("//span[contains(.,'" + d[i].trim() + "')]"), 5)) {
							click(By.xpath("//span[contains(.,'" + d[i].trim() + "')]"));
							waitFor(2);
							break;
						} else {
							writeToLogs("[ERROR]" + d[i].trim() + " is not available");
						}
					}

					if (isElementVisible(By.xpath("//span[contains(.,'" + d[i].trim() + "')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"), 5)) {
						scrollAndClick(By.xpath("//span[contains(.,'" + d[i].trim() + "')]/../preceding-sibling::td//div[@class='w-oc-icon-off']"));
					} else {
						writeToLogs("[ERROR]" + d[i].trim() + " is not available");
					}
				}
				clickButton("Select");
				if (copyVisibility.equals("No")) {
					click(Element.chkCopyVisibility);
				}
//				if (copyParticipant == "Yes") {
//					click(Element.chkCopyVisibility);
//				}
				populateDropdown("External System", externalSystem);
				click(Element.chkAllContent);
				waitFor(4);
				clickButton("Copy");
				break;

			case "Search Library or Events":
				click(Element.rdoSearchLibrary);
				waitFor(2);
				
				switch (titleKeyword){
				case "Title":
					inputText(Element.txtTitle, searchTerm);
					break;
				case "Keywords":
					inputText(Element.txtKeywords, searchTerm);
					break;
				}

//				populateDropdown("From", from);
				click(By.xpath("//td[text()='From:']/following-sibling::td//span[@class='w-dropdown-pic-ct']"));
				click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(text(),'"+ from + "')]"));
				waitFor(2);
//				populateDropdown("Project Type", projectType);
				click(By.xpath("//td[text()='Project Type:']/following-sibling::td//span[@class='w-dropdown-pic-ct']"));
				click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(text(),'"+ projectType + "')]"));
				
				waitFor(2);
				clickButton("Search");
				waitFor(3);
				
				click(By.xpath("//table[@class='tableBody']//td[contains(.,'"+searchTerm+"')]/preceding-sibling::td//label"));
				
				
				waitFor(3);
				clickButton("Select");
//				if (copyVisibility == "Yes") {
//					click(Element.chkCopyVisibility);
//				}
				populateDropdown("External System", externalSystem);
				click(Element.chkAllContent);
				waitFor(4);
				clickButton("Copy");
				break;
			}
			
			waitForButtonToExist("Done", 5);
			waitFor(2);
			clickButton("Done");
		}

		// Add Table Section
		public void addTableSection(String content) {
			
			String [] section = content.split("\\^", -1);
			String parentContent = section[1].trim();
			String name = section[2].trim();
			String description = section[3].trim();
			String visibleToParticipant = section[4].trim();
			String teamAccessControl = section[5].trim();
			String subContent = section[6].trim();
			
			if (!parentContent.isEmpty() && name.isEmpty() && subContent.isEmpty()){
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Table Section')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", parentContent);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+parentContent+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Table Section')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", name);
			}else if (!parentContent.isEmpty() && !name.isEmpty() && !subContent.isEmpty()){
				clickAlt(By.xpath("//a[contains(@class,'awmenuLink')]/b[text()='"+name+"']"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Table Section')]"));
				waitForButtonToExist("OK", 5);
				populateTextField("Name", subContent);
			}
			
			inputDescription(Element.txtProjectDescription, description);
			populateDropdownAlt("Visible to Participant", visibleToParticipant);
			populateChooserMultiple("Team Access Control", teamAccessControl);
//			clickButton("OK");
//			clickButton("Done");
			waitFor(2);
			click(Element.btnOK);
		}



		//Attachment From Desktop - Event Content
		public void addAttachmentFromDesktopEventContent(String content){
			
			String [] attach = content.split("\\^", -1);
//			String parentContent = attach[1].trim();
			String filePath = attach[6].trim();
			String description = attach[3].trim();
//			String visibleToParticipant = attach[3].trim();
//			String teamAccessControl = attach[4].trim();

			click(Element.btnAdd);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Attachments From Desktop')]"));
			
			inputDescription(Element.txtProjectDescription, description);
			uploadFile(filePath);
			
			waitFor(2);
			clickButton("Done");
			waitForButtonToExist("Add", 60);
			
		}
		
		
		
		
		
		public void configureEventContent(){
			
			parseExcel retrieve = new parseExcel();
			
			List <String> eventContent = retrieve.getEventContent();
			
			for (String ec : eventContent){
				
				String [] content = ec.split("\\^",-1);
				waitFor(3);
				
				writeToLogs("Add " + content[0]);
				
				switch (content[0].trim()){

				case "Section":
					addSection(ec);
					break;
					
				case "Table Section":
					addTableSection(ec);
					break;
					
				case "Question":
					addQuestion(ec);
					break;
					
				case "Requirement":
					addRequirement(ec);
					break;
					
				case "Attachment From Desktop":
					addAttachmentFromDesktopEventContent(ec);
					break;
					
				case "Attachment From Library":
					
					String [] attLib = ec.split("\\^", -1);
//					String parentContent = attLib[1].trim();
					
					click(Element.btnAdd);
					click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Attachments From Library')]"));

					String searchFile = attLib[6].trim();
					String exploreFile = attLib[7].trim();
					
					if (!searchFile.isEmpty()){
						addAttachmentLibrary("Search", searchFile);
					}else if (!exploreFile.isEmpty()){
						addAttachmentLibrary("Explore", exploreFile);
					}
					break;
					
				case "Cost Terms":
					addCostTerms(ec);
					break;
					
				case "Content From Library":
					addContentFromLibrary(ec);
					break;
				}
				
				writeToLogs("");
				
			}
			
			
			
		}
		
		
		
	public void waitForButtonToExist(String button, int seconds){
		explicitWait(By.xpath("//button/span[contains(text(),'"+button+"')]"), seconds);
	}
	
	
	
	public void configureSourcingLibrary(){
		
		parseExcel retrieve = new parseExcel();
		
		List <String> eventContent = retrieve.getSourcingLibrary();
		
		for (String sL : eventContent){
			
			String [] content = sL.split("\\^",-1);
			waitFor(2);
			
			writeToLogs("Add " + content[0]);
			
			switch (content[0].trim()){
			
			case "KPI":
				addKPI(sL);
				break;
			
			case "Section":
				addSection(sL);
				break;
				
			case "Table Section":
				addTableSection(sL);
				break;
			
			case "Lot":
				addLot(sL);
				break;
				
			case "Line Item":
				addLineItem(sL);
				break;
				
			case "Question":
				addQuestion(sL);
				break;
				
			case "Requirement":
				addRequirement(sL);
				break;
				
			case "Attachment From Desktop":
				addAttachmentFromDesktopEventContent(sL);
				break;
				
			case "Attachment From Library":
				
				String [] attLib = sL.split("\\^", -1);
//				String parentContent = attLib[1].trim();
				
				click(Element.btnAdd);
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Attachments From Library')]"));

				String searchFile = attLib[6].trim();
				String exploreFile = attLib[7].trim();
				
				if (!searchFile.isEmpty()){
					addAttachmentLibrary("Search", searchFile);
				}else if (!exploreFile.isEmpty()){
					addAttachmentLibrary("Explore", exploreFile);
					clickButton("Done");
				}

				break;
				
			case "Cost Terms":
				addCostTerms(sL);
				break;
				
			case "Formula":
				addFormula(sL);
				break;
				
			case "Content From Library":	
				addContentFromLibrary(sL);
				break;
				
			}
			
			writeToLogs("");
			
		}
		
		
		
	}
	
	public void deleteQuestions(){
		List<WebElement> row = driver.findElements(By.xpath("(//div[@class='tableBody'])[2]//tr[@_awtisprimaryrow='1']/td[1]"));
		for (int i=1; i<=row.size(); i++){
			WebElement eleQuestion = explicitWait(By.xpath("((//div[@class='tableBody'])[2]//tr[@_awtisprimaryrow='1']/td[1])["+i+"]"), 5);
			String questionUI = eleQuestion.getText().trim();
			click(By.xpath("//table[@class='tableBody']//td[contains(text(),'"+questionUI+"')]/following-sibling::td//a[contains(text(),'Actions')]"));
			click(Element.lnkDelete);
			click(Element.btnOK);
			writeToLogs("Deleted '"+questionUI+"' question.");
		}
	}
	
	
	public void addQuestion() {
		
		navigateTab("Conditions");
		waitFor(2);
		waitForButtonToExist("Add Question", 5);
		
		deleteQuestions();
		
		parseExcel retrieve = new parseExcel();
		List <String> addQuestion = retrieve.getTemplateQuestions();
		int i = 0;
		for (String q : addQuestion){
			
			String [] qq = q.split("\\^",-1);
			String question = qq[0].trim();
			String visibilityConditions = qq[1].trim();
			String answer = qq[2].trim();
			String definedCondition = qq[3].trim();
			String defaultAnswer = qq[4].trim();
			String condition = qq[5].trim();
			
			if (!question.isEmpty()){
				
				if (i > 2){
					click(Element.btnOK);
					waitForButtonToExist("Add Question", 5);
				}
				
				clickButton("Add Question");
				writeToLogs("Add Question");
				waitForButtonToExist("OK", 5);
				populateTextArea("Question", question);
				populateCondition(Element.lnkCondition, visibilityConditions);
				populateRadioButton("Is answer visibility conditional?", "Yes");
				i = 2;
				
			}else{
				i = i + 1;
				if (i > 3){
					clickButton("Add Another Answer");
					waitFor(1);
				}
			}
			waitFor(2);
			writeToLogs(">>Answer: " + answer);
			inputText(By.xpath("//table[@class='tableBody']//tr["+i+"]//td[2]//input"), answer.trim());
			inputText(By.xpath("//table[@class='tableBody']//tr["+i+"]//td[3]//input"), definedCondition.trim());
			populateCondition(By.xpath("//table[@class='tableBody']//tr["+i+"]//td[4]//a"), condition);
			if (defaultAnswer.contains("Yes")){
				click(By.xpath("//table[@class='tableBody']//tr["+i+"]//td[5]//label"));
				waitFor(2);
				if (defaultAnswer.contains("Change")){
					click(By.xpath("//table[@class='tableBody']//tr["+i+"]//td[5]//div[2]//label"));
				}
			}
			
			writeToLogs("");

		}
		
		if (i > 0){
			waitFor(2);
			click(Element.btnOK);
			waitForButtonToExist("Add Question", 5);
		}
	}
	
	
	public void populateCondition(By locator, String value){
		
		if (!value.isEmpty()){
			click(locator);
			click(By.xpath("//div[@class='awmenu w-pm-menu']//span[contains(text(),'Other...')]"));
			
			String [] data = value.split("\\|");
			
			for(String val : data){
				inputText(Element.txtSearchField, val.trim());
				click(Element.btnSearchField);
				waitFor(3);
				if (explicitWait(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1') and contains(.,'"+val.trim()+"')]//td//label"), 5) != null){
					click(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1') and contains(.,'"+val.trim()+"')]//td//label"));
					System.out.println("Select '" + val + "' condition.");
					waitFor(2);
				}else{
					writeToLogs("[FAILED] Cannot find '" + val + "' condition.");
				}
			}
			writeToLogs(">>Conditions: " + value);
			click(Element.btnDoneSearch);
		}
		
	}
	
	
	public void addCondition(){
		
		navigateTab("Conditions");
		waitForButtonToExist("Add Condition", 5);
		
		deleteConditions();
		
		parseExcel retrieve = new parseExcel();
		List <String> addCondition = retrieve.getConditions();
		int i = 0;
		boolean isSubCondition = false;

		for (String con : addCondition){
			
			String [] cond = con.split("\\^", -1);
			String name = cond[0].trim();
			String description = cond[1].trim();
			String condition = cond[2].trim();
			String subCondition = cond[3].trim();
			String category = cond[4].trim();
			String field = cond[5].trim();
			String comparison = cond[6].trim();
			String value = cond[7].trim();
			
			
			if (!name.isEmpty()){
				
				if (i > 0){
					click(Element.btnOK);
					waitForButtonToExist("Add Condition", 5);
				}
				clickButton("Add Condition");
				waitFor(2);
				waitForButtonToExist("OK", 5);
				writeToLogs("Add Condition: " + name);
				populateTextField("Name", name);
				populateTextArea("Descripton", description);
				waitFor(2);
				i = 1;
			}
			
			//Expression
			if (isSubCondition && subCondition.isEmpty()){
				sendKeysEnter(By.xpath("(//td[contains(@class,'tree-expression')]/following-sibling::td//a[@title='Actions Menu'])["+i+"]"));
			}else{
				sendKeysEnter(By.xpath("//td[contains(@class,'tree-expression')]/following-sibling::td//a[@title='Actions Menu']"));
			}
			
			if (!condition.isEmpty()){
				if (!condition.equals("All Are True")){
					click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'" + condition + "')]"));
					waitFor(2);
					sendKeysEnter(By.xpath("//td[contains(@class,'tree-expression')]/following-sibling::td//a[@title='Actions Menu']"));
				}
				isSubCondition = false;
			}
			
			if (!subCondition.isEmpty()){
				i = i + 1;
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Subcondition')]"));
				sendKeysEnter(By.xpath("(//td[contains(@class,'tree-expression')]/following-sibling::td//a[@title='Actions Menu'])["+i+"]"));
				if (!subCondition.equals("All Are True")){
					click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'" + subCondition + "')]"));
					waitFor(2);
					sendKeysEnter(By.xpath("(//td[contains(@class,'tree-expression')]/following-sibling::td//a[@title='Actions Menu'])["+i+"]"));
				}
				isSubCondition = true;
			}

			switch (category){
			
			case "Field Match":
				
				writeToLogs(">>Field Match: " + field + " " + comparison + " " + value);
				
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Field Match')]"));
				
				//Field
				click(By.xpath("//td[contains(text(),'(No Field Selected)')]/following-sibling::td//button"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'" + field + "')]"));
				waitFor(2);
				//Comparison
				click(By.xpath("//td[contains(text(),'(No Value Selected)')]/preceding-sibling::td//span[contains(text(),'is equal to')]"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(.,'"+comparison+"')]"));
				waitFor(2);
				//Value
				click(By.xpath("//td[contains(text(),'(No Value Selected)')]/following-sibling::td//button"));
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Select Value')]"));
				
				explicitWait(By.xpath("//div[@class='w-dlg-buttons']//button[@title='OK Button']"), 5);
				
				//Dropdown
				if (isElementVisible(By.xpath("//div[@class='w-dlg-content']//div[@class='w-dropdown']"), 1)){
					click(By.xpath("//div[@class='w-dlg-content']//div[@class='w-dropdown']"));
					if (explicitWait(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(.,\""+value+"\")]"), 5) != null){
						click(By.xpath("//div[contains(@class,'w-dropdown-items w-dropdown-slide')]//div[contains(.,\""+value+"\")]"));
						waitFor(3);
					}else{
						writeToLogs("[FAILED] " + value + " is not available in " + field);
					}
					waitFor(3);
					click(By.xpath("//div[@class='w-dlg-buttons']//button[@title='OK Button']"));
					break;
				}
				
				//Select
				if (isElementVisible(By.xpath("//div[@class='w-dlg-content']//a[text()='select']"), 1)){
					sendKeysEnter(By.xpath("//div[@class='w-dlg-content']//a[text()='select']"));
					String [] data = value.split("\\|");
					for(String val : data){
						inputText(Element.txtSearchField, val.trim());
						click(Element.btnSearchField);
						waitFor(2);
						if (explicitWait(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1') and contains(.,'"+val.trim()+"')]//td//label"), 5) != null){
							click(By.xpath("//div[@class='w-dlg-dialog']//tr[contains(@class,'tableRow1') and contains(.,'"+val.trim()+"')]//td//label"));
							System.out.println("Select " + val + " on " + field);
							waitFor(3);
						}else{
							writeToLogs("[FAILED] Cannot find " +val+ " on " + field);
						}
					}
					click(Element.btnDoneSearch);
					waitFor(3);
					click(By.xpath("//div[@class='w-dlg-buttons']//button[@title='OK Button']"));
					break;
				}
				
				//Textbox
				if (isElementVisible(By.xpath("//div[@class='w-dlg-content']//input[@class='w-txt']"), 1)){
					inputText(By.xpath("//div[@class='w-dlg-content']//input[@class='w-txt']"), value);
					waitFor(3);
					click(By.xpath("//div[@class='w-dlg-buttons']//button[@title='OK Button']"));
					break;
				}
				
				//Chooser
				if (isElementVisible(By.xpath("//div[@class='w-dlg-content']//input[@class='w-chNoSel w-txt w-txt-dsize w-chInput']"), 1)){
					sendKeysEnter(By.xpath("//div[@class='w-dlg-content']//input[@class='w-chNoSel w-txt w-txt-dsize w-chInput']"));
					inputText(Element.txtSearchField, value.trim());
					click(Element.btnSearchField);
					waitFor(2);
					if (explicitWait(By.xpath("//td[starts-with(.,'"+value.trim()+"')]/../../../../../following-sibling::td//button"), 5) != null){
						click(By.xpath("//td[starts-with(.,'"+value.trim()+"')]/../../../../../following-sibling::td//button"));
						waitFor(3);
					}else{
						click(Element.btnDoneSearch);
						writeToLogs("[FAILED] Cannot find " +value+ " on " + field);
					}
					waitFor(3);
					click(By.xpath("//div[@class='w-dlg-buttons']//button[@title='OK Button']"));
					break;
				}
				
				//Radio Button
				if (isElementVisible(By.xpath("//span[@class='w-rdo-list']"), 1)){
					click(By.xpath("//span[contains(text(),'"+value+"')]/../div//label"));
					waitFor(3);
					click(By.xpath("//div[@class='w-dlg-buttons']//button[@title='OK Button']"));
					break;
				}
				
				
				
				break;
				
			case "Reference to Condition":
				
				writeToLogs(">>Reference to Condition: " + value);
				
				click(By.xpath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Reference to Condition')]"));
				sendKeysEnter(By.xpath("//div[@class='w-dlg-content']//input"));
				inputText(Element.txtSearchField, value.trim());
				click(Element.btnSearchField);
				waitFor(2);
				if (explicitWait(By.xpath("//td[starts-with(.,'"+value.trim()+"')]/../../../../../following-sibling::td//button"), 5) != null){
					click(By.xpath("//td[starts-with(.,'"+value.trim()+"')]/../../../../../following-sibling::td//button"));
					waitFor(3);
				}else{
					click(Element.btnDoneSearch);
					writeToLogs("[FAILED] Cannot find '" +value+ "' on Conditions.");
				}
				click(By.xpath("//div[@class='w-dlg-buttons']//button[@title='OK Button']"));
				waitFor(2);
				break;
			
			}
			
			waitFor(3);
			writeToLogs("");
			
		}
		
		if (i > 0){
			waitFor(2);
			click(Element.btnOK);
			waitForButtonToExist("Add Condition", 5);
		}
	}
	

	public void deleteConditions(){
		
		List <WebElement> row = driver.findElements(By.xpath("//div[@class='tableBody']//table[@class='tableBody']//tr[@_awtisprimaryrow='1']/td[1]"));
		for (int i=1; i<=row.size(); i++){
			WebElement eleCondition = explicitWait(By.xpath("//div[@class='tableBody']//table[@class='tableBody']//tr[@_awtisprimaryrow='1']/td[1])["+i+"]"), 10);
			String name = eleCondition.getText().trim();
			click(By.xpath("//table[@class='tableBody']//td[contains(text(),'"+name+"')]/following-sibling::td//a"));
			click(Element.lnkDelete);
			click(Element.btnOK);
			writeToLogs("Deleted '" +name+ "' condition.");
		}
		
		
	}
	
}	

