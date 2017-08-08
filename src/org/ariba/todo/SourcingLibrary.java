package org.ariba.todo;

import javax.swing.JOptionPane;

import org.ariba.elements.Element;
import org.ariba.main.Details;
import org.ariba.methods.Commands;
import org.ariba.methods.parseExcel;
import org.openqa.selenium.By;

public class SourcingLibrary {

	public void execute() {
		
		Details.template = "Sourcing Library";
		
		parseExcel data = new parseExcel();
		String folder = data.getSpecificData(Details.path, "Configuration", "Folder Name", "Value").trim();
		String title = data.getSpecificData(Details.path, "Content Document", "Title", "Value").trim();
		String description = data.getSpecificData(Details.path, "Content Document", "Description", "Value").trim();
		String baseLanguage = data.getSpecificData(Details.path, "Content Document", "Base Language", "Value").trim();
		String commodity = data.getSpecificData(Details.path, "Content Document", "Commodity", "Value").trim();
//		String publishKeywords = data.getSpecificData(Details.path, "Overview Tab", "Publish Keywords", "Value").trim();
		

		/*----------------Login Page-----------------*/
		
		//Login
		Commands action = new Commands(30);
		action.writeToLogs("-----------------LOGIN------------------");
		action.writeToLogs("Open the Chrome Browser");
		action.inputText(Element.txtUserName, Details.user);
		action.inputText(Element.txtPassword, Details.pass);
		action.click(Element.btnLogin);

		if(action.isElementVisible(Element.lblErrorLogin, 2)){
			action.writeToLogs("[ERROR] Invalid Username or Password.");
			return;
		}else{
			action.writeToLogs("User successfully logged in!");
		}
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		
		/*---------------End of Login-----------------*/
		
		
		
		/*----------------Navigate to Sourcing Library--------------*/
		action.writeToLogs("Navigate to Sourcing Library page.");
		action.writeToLogs("");
		action.waitFor(5);
		action.explicitWait(Element.lnkManage, 15);
		action.sendKeysEnter(Element.lnkManage);
		action.click(Element.lnkSourcingLibrary);
		
		/*-------------------------End-----------------------------*/
		
		
		
		/*----------------Create Sourcing Library to Folder--------------*/
		
		action.waitFor(2);

		if (!folder.isEmpty()){
			if(action.explicitWait(By.linkText(folder),10) != null){
				action.sendKeysEnter(By.linkText(folder));
				action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Content Document')]"));
			}else{
				action.writeToLogs("--------------CREATE FOLDER-------------");
				action.clickActions("Folder");
				action.populateTextField("Name", folder);
				action.clickButton("Create");
				
				if (action.isElementVisible(By.className("msgText"), 2)){
					action.writeToLogs("[ERROR] Another folder or document in the selected parent folder already has the same name.");
					return;
				}
				
				action.writeToLogs(folder + " is successfully created.");
				action.writeToLogs("------------------------------------------");
				action.writeToLogs("");
				
				action.sendKeysEnter(By.linkText(folder));
				action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Content Document')]"));
			}
		}else{
			action.clickButton("Actions");
			action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Content Document')]"));
		}
		
		action.writeToLogs("Create Content Document on " +folder+ " folder.");
		action.writeToLogs("");
		

		/*----------------Create Sourcing Library to Folder--------------*/
		action.writeToLogs("--------------CONTENT DOCUMENT-------------");
		
		action.waitForButtonToExist("Create", 5);
		action.populateTextField("Title", title);
		action.inputDescription(Element.txtProjectDescription, description);
		action.populateDropdown("Base Language", baseLanguage);
		action.populateChooserField("Commodity", commodity);
		action.clickButton("Create");
		
		if (action.isElementVisible(By.className("msgText"), 2)){
			action.writeToLogs("[ERROR] Another folder or document in the selected parent folder already has the same name.");
			return;
		}

		action.writeToLogs("");
		
		action.waitForButtonToExist("Done", 5);
		
		action.configureSourcingLibrary();
		action.waitFor(2);
		action.clickButton("Done");
		
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		action.writeToLogs("---------------COMPLETED----------------");
		
		JOptionPane.showMessageDialog(null,"Finished!");

	}
	
	
	
	
}
