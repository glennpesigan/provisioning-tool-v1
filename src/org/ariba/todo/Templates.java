package org.ariba.todo;

import javax.swing.JOptionPane;

import org.ariba.elements.Element;
import org.ariba.main.Details;
import org.ariba.methods.Commands;
import org.ariba.methods.parseExcel;
import org.openqa.selenium.By;

public class Templates {

	public void execute() {
		
		Details.template = "Template";
		
		parseExcel data = new parseExcel();
		String folder = data.getSpecificData(Details.path, "Configuration", "Folder Name", "Value").trim();
		String projectType = data.getSpecificData(Details.path, "Overview Tab", "Template", "Value").trim();
		String templateName = data.getSpecificData(Details.path, "Overview Tab", "Template Name", "Value").trim();
		String description = data.getSpecificData(Details.path, "Overview Tab", "Description", "Value").trim();
		String owner = data.getSpecificData(Details.path, "Overview Tab", "Owner", "Value").trim();
		String processStatus = data.getSpecificData(Details.path, "Overview Tab", "Process Status", "Value").trim();
		String baseLanguage = data.getSpecificData(Details.path, "Overview Tab", "Base Language", "Value").trim();
		String project = data.getSpecificData(Details.path, "Overview Tab", "Project", "Value").trim();
		String rank = data.getSpecificData(Details.path, "Overview Tab", "Rank", "Value").trim();
		String accessControl = data.getSpecificData(Details.path, "Overview Tab", "Access Control", "Value").trim();
		String conditions = data.getSpecificData("Overview Tab", "Conditions").trim();
		String isPublish = data.getSpecificData("Overview Tab", "Publish").trim();
		

		/*----------------Login Page-----------------*/
		
		//Login
		
		Commands action = new Commands(30);
		
		action.writeToLogs("-----------------LOGIN------------------");
		action.writeToLogs("Open the Chrome Browser");
		action.writeToLogs("Login User: " + Details.user);
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
		
		action.waitFor(5);
		action.explicitWait(Element.lnkManage, 15);
		action.sendKeysEnter(Element.lnkManage);
		if (action.isElementVisible(Element.lnkTemplates, 2)){
			action.click(Element.lnkTemplates);
		}else{
			action.sendKeysEnter(Element.lnkManage);
			action.click(Element.lnkTemplates);
		}
		
		action.writeToLogs("Navigate to Templates page.");
		action.writeToLogs("");
		
		if(!action.navigateTab("Documents")){
			action.click(Element.lnkOverviewActions);
			action.click(Element.lnkFullView);
			action.navigateTab("Documents");
		}
		
		action.waitFor(3);

		if (!folder.isEmpty()){
			
			if(action.explicitWait(By.linkText(folder),10) != null){
				action.sendKeysEnter(By.linkText(folder));
				action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Template')]"));
			}else{
				action.writeToLogs("--------------CREATE FOLDER-------------");
				action.clickActions("Folder");
				action.populateTextField("Name", folder);
				action.writeToLogs("Folder Name: " + folder);
				action.clickButton("Create");
				
				if (action.isElementVisible(By.className("msgText"), 2)){
					action.writeToLogs("[ERROR] Another folder or document in the selected parent folder already has the same name.");
					return;
				}
				
				action.writeToLogs(folder + " is successfully created.");
				action.writeToLogs("------------------------------------------");
				action.writeToLogs("");
				
				action.sendKeysEnter(By.linkText(folder));
				action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Template')]"));
				
			}
		}else{
			action.clickButton("Actions");
			action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Template')]"));
		}
		
		action.writeToLogs("Create Template on " +folder+ " folder.");
		action.writeToLogs("");

		/*--------------Create New Project Template------------*/
		
		action.writeToLogs("------------CREATE TEMPLATE-------------");
		action.selectProjectTypeTemplate(projectType);
		action.writeToLogs("Project Type: " + projectType);
	
		action.inputText(Element.txtProjectName, templateName);
		action.writeToLogs(">>Title: " + templateName);
		
		action.inputDescription(Element.txtProjectDescription, description);
		
		/*if (!baseLanguage.isEmpty()){
			action.click(Element.drpBaseLanguage);
			action.click(By.xpath("//div[@role='option' and contains(text(),'"+baseLanguage+"')]"));
		}*/
		

		if (projectType.equals("Sourcing Project") || projectType.equals("Sourcing Request")){
			action.writeToLogs(">>Project: " + project);
			switch (project){
			case "Full Project":
				action.click(Element.rdoFullProject);
				action.waitFor(2);
				break;
			case "Quick Project":
				action.click(Element.rdoQuickProject);
				action.waitFor(2);
				action.populateRadioButton("Event Type", "RFI");
				break;
			}
		}
		action.populateDropdown("Base Language", baseLanguage);
		action.waitFor(2);
		action.click(Element.btnOK);
		
		if (action.isElementVisible(By.className("msgText"), 2)){
			action.writeToLogs("[ERROR] Another folder or document in the selected parent folder already has the same name.");
			return;
		}
		
		action.explicitWait(Element.lblProjectName, 15);
		action.writeToLogs(templateName + " is successfully created!");
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		
		/*-----------End of Create New Project Template------------*/
			
		//Click Ignore button
		if (action.isElementVisible(Element.btnIgnore, 5)){
			action.click(Element.btnIgnore);
		}

		//Click Properties Action > Full View
		action.click(Element.lnkOverviewTab);
		action.waitFor(2);
		action.explicitWait(Element.lnkPropertiesActions, 10);
		action.sendKeysEnter(Element.lnkPropertiesActions);
		
		if (action.isElementVisible(Element.lnkFullView, 2)){
			action.click(Element.lnkFullView);
			action.waitFor(2);
		}
		
		/*--------------Conditions Tab---------------*/
		action.writeToLogs("---------------CONDITIONS---------------");
		action.addCondition();
		action.waitFor(1);
		action.addQuestion();
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		/*--------------End of Conditions------------*/
		
		
		/*--------------Overview Tab---------------*/
		action.writeToLogs("----------------OVERVIEW----------------");
		action.configureOverviewTab(owner, processStatus, rank, accessControl, conditions);
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		/*--------------End of Overview------------*/
		
		
		/*--------------Team Tab------------------*/
		action.writeToLogs("------------------TEAM------------------");
		action.configureTeamTab(false);
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		/*--------------End of Team---------------*/
		
		
		/*-------------Documents Tab-----------------*/
		action.writeToLogs("---------------DOCUMENTS----------------");
		action.configureDocumentsTab();
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		/*-----------End of Documents----------------*/

		
		/*---------------Tasks Tab------------------*/
		action.writeToLogs("-----------------TASKS------------------");
		action.configureTaskTab();
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		/*--------------End of Tasks-----------------*/
		
		if (isPublish.equals("Yes")){
			action.navigateTab("Overview");
			action.waitFor(2);
			action.explicitWait(Element.lnkPropertiesActions, 10);
			action.sendKeysEnter(Element.lnkPropertiesActions);
			action.click(Element.lnkPublish);
			if (action.isElementVisible(By.xpath("//div[@class='w-msg-banner-msg' and contains(text(),'has been published')]"), 5)){
				action.writeToLogs("'" +templateName+ "' has been published successfully, and is available for new projects.");
				action.writeToLogs("");
			}
		}

		action.writeToLogs("---------------COMPLETED----------------");
		
		JOptionPane.showMessageDialog(null,"Finished!");

	}
	
	
	
	
}
