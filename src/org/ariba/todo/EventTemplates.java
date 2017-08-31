package org.ariba.todo;

import javax.swing.JOptionPane;

import org.ariba.elements.Element;
import org.ariba.main.Details;
import org.ariba.methods.Commands;
import org.ariba.methods.parseExcel;
import org.openqa.selenium.By;

import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;

public class EventTemplates {

	public void execute() {

		Details.template = "Event Template";

		parseExcel data = new parseExcel();
		Details.actionToPerform = data.getSpecificData(Details.path, "Configuration", "Action", "Value").trim();
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
		String eventType = data.getSpecificData(Details.path, "Overview Tab", "Event Type", "Value").trim();

		String editConditions = data.getSpecificData(Details.path, "Configuration", "Conditions", "Value").trim();
		String editOverview = data.getSpecificData(Details.path, "Configuration", "Overview", "Value").trim();
		String editTasks = data.getSpecificData(Details.path, "Configuration", "Tasks", "Value").trim();
		String editTeam = data.getSpecificData(Details.path, "Configuration", "Team", "Value").trim();

		Details.eventType = eventType;

		Commands action = new Commands(15);

		/*----------------Login Page-----------------*/

		//Login
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
		action.explicitWait(Element.lnkManage, 30);
		action.sendKeysEnter(Element.lnkManage);
		if (action.isElementVisible(Element.lnkTemplates, 0)){
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

		switch (Details.actionToPerform){

		/*------------Create New Project Template Page--------------*/

		case "Create New":

			if (!folder.isEmpty()){
				if(action.explicitWait(By.linkText(folder),10) != null){
					action.sendKeysEnter(By.linkText(folder));
					action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Template')]"));
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
					action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Template')]"));
				}
			}else{
				action.clickButton("Actions");
				action.click(By.xpath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following::a[contains(text(),'Template')]"));
			}

			action.writeToLogs("Create Template on " +folder+ " folder.");
			action.writeToLogs("");





			action.writeToLogs("------------CREATE TEMPLATE-------------");
			//Select type of project for template
			action.selectProjectTypeTemplate("Sourcing Project");
			action.writeToLogs("Project Type: Sourcing Project");
			action.inputText(Element.txtProjectName, templateName);
			action.writeToLogs(">>Title: " + templateName);
			action.inputDescription(Element.txtProjectDescription, description);

			/*if (!baseLanguage.isEmpty()){
			action.click(Element.drpBaseLanguage);
			action.click(By.xpath("//div[@role='option' and contains(text(),'"+baseLanguage+"')]"));
		}*/
			action.populateDropdown("Base Language", baseLanguage);


			//Select Quick Project
			action.click(Element.rdoQuickProject);
			action.writeToLogs(">>Project: Quick Project");
			action.waitFor(3);
			action.populateDropdown("Event Type", eventType);

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
			break;

			/*-----------End of Create New Project Template------------*/


		case "Update Existing":

			if (!folder.isEmpty()){
				action.sendKeysEnter(By.linkText(folder));
				action.click(Element.lnkOpen);
				action.writeToLogs("Open " + folder + " folder.");
			}

			action.sendKeysEnter(By.linkText(templateName));
			action.click(Element.lnkOpen);
			action.writeToLogs("Open " + templateName + " template.");
			break;
		}

		action.waitFor(2);

		//Click Ignore button
		if (action.isElementVisible(Element.btnIgnore, 5)){
			action.click(Element.btnIgnore);
		}

		/*--------------Conditions Tab---------------*/
		switch (Details.actionToPerform){
		case "Create New":
			action.writeToLogs("---------------CONDITIONS---------------");
			action.addCondition();
			action.waitFor(1);
			action.addQuestion();
			action.writeToLogs("----------------------------------------");
			action.writeToLogs("");
			break;
		case "Update Existing":
			if (editConditions.equals("Yes")){
				action.writeToLogs("---------------CONDITIONS---------------");
				action.addCondition();
				action.waitFor(1);
				action.addQuestion();
				action.writeToLogs("----------------------------------------");
				action.writeToLogs("");
			}
			break;
		}
		/*--------------End of Conditions------------*/


		/*--------------Overview Tab---------------*/

		switch (Details.actionToPerform){
		case "Create New":
			action.writeToLogs("----------------OVERVIEW----------------");
			action.configureOverviewTab(owner, processStatus, rank, accessControl, conditions, description);
			action.writeToLogs("------------------------------------------");
			action.writeToLogs("");
			break;
		case "Update Existing":
			if (editOverview.equals("Yes")){
				action.writeToLogs("----------------OVERVIEW----------------");
				action.configureOverviewTab(owner, processStatus, rank, accessControl, conditions, description);
				action.writeToLogs("------------------------------------------");
				action.writeToLogs("");
			}
			break;
		}

		/*--------------End of Overview------------*/

		/*--------------Team Tab------------------*/
		switch (Details.actionToPerform){
		case "Create New":
			action.writeToLogs("------------------TEAM------------------");
			action.configureTeamTab(true);
			action.writeToLogs("------------------------------------------");
			action.writeToLogs("");
			break;
		case "Update Existing":
			
			if (editTeam.equals("Yes")){
				action.writeToLogs("------------------TEAM------------------");
				action.updateTeamTab(true);
				action.writeToLogs("------------------------------------------");
				action.writeToLogs("");
			}
			break;
		}
		/*--------------End of Team---------------*/


		/*-------------Documents Tab-----------------*/
		action.writeToLogs("---------------DOCUMENTS----------------");
		//		action.configureDocumentsTab();
		action.navigateTab("Overview");
		action.waitFor(2);
		String eventTemplateConditions = data.getEventCondition();
		action.populateCondition(By.xpath("//td[@class='tableBody w-tbl-cell' and contains(.,'"+templateName+"')]/following-sibling::td//a"), eventTemplateConditions);
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");
		/*-----------End of Documents----------------*/


		/*---------------Tasks Tab------------------*/
		switch (Details.actionToPerform){
		case "Create New":
			action.writeToLogs("-----------------TASKS------------------");
			action.configureEventTemplateTaskTab();
			action.writeToLogs("------------------------------------------");
			action.writeToLogs("");
			break;
		case "Update Existing":
			if (editTasks.equals("Yes")){
				action.writeToLogs("-----------------TASKS------------------");
				//action.addDocumentsFromExcelToUI();
				action.updateEventTemplateTaskTab();
				action.addTasks();
				//action.updateDocumentsTab();
				action.writeToLogs("------------------------------------------");
				action.writeToLogs("");
			}
			break;
		}
		
		System.exit(0);
		/*--------------End of Tasks-----------------*/


		/*---------------Event Template------------------*/

		action.waitFor(2);
		action.openEventTemplate(eventType);

		action.writeToLogs("-----------------RULES------------------");
		switch (eventType){
		case "RFI":
			action.writeToLogs("Auction Format");
			action.auctionFormat();
			action.writeToLogs("");
			action.writeToLogs("Envelope Rules");
			action.envelopeRules_RFI();
			action.writeToLogs("");
			action.writeToLogs("Timing Rules");
			action.timingRules_RFI();
			action.writeToLogs("");
			action.writeToLogs("Bidding Rules");
			action.biddingRules_RFI();
			action.writeToLogs("");
			action.writeToLogs("Currency Rules");
			action.currencyRules_RFI();
			action.writeToLogs("");
			action.writeToLogs("Project Owner Actions");
			action.projectOwnerActions_RFI();
			action.writeToLogs("");
			action.writeToLogs("Market Feedback");
			action.marketFeedback_RFI();
			action.writeToLogs("");
			action.writeToLogs("Message Board");
			action.messageBoard();
			action.writeToLogs("");
			action.writeToLogs("Include Bidder Aggreement");
			action.includeBidderAgreement();
			break;

		case "RFP":
			action.writeToLogs("Auction Format");
			action.auctionFormat();
			action.writeToLogs("");
			action.writeToLogs("Timing Rules");
			action.timingRules_RFP();
			action.writeToLogs("");
			action.writeToLogs("Bidding Rules");
			action.biddingRules_RFP();
			action.writeToLogs("");
			action.writeToLogs("Currency Rules");
			action.currencyRules_RFI();
			action.writeToLogs("");
			action.writeToLogs("Project Owner Actions");
			action.projectOwnerActions_RFI();
			action.writeToLogs("");
			action.writeToLogs("Market Feedback");
			action.marketFeedback_RFP();
			action.writeToLogs("");
			action.writeToLogs("Message Board");
			action.messageBoard();
			action.writeToLogs("");
			action.writeToLogs("Include Bidder Aggreement");
			action.includeBidderAgreement();
			break;

		case "Auction":
			action.writeToLogs("Auction Format");
			action.auctionFormat();
			action.writeToLogs("");
			action.writeToLogs("Timing Rules");
			action.timingRules_Auction();
			action.writeToLogs("");
			action.writeToLogs("Bidding Rules");
			action.biddingRules_RFP();
			action.writeToLogs("");
			action.writeToLogs("Currency Rules");
			action.currencyRules_RFI();
			action.writeToLogs("");
			action.writeToLogs("Project Owner Actions");
			action.projectOwnerActions_RFI();
			action.writeToLogs("");
			action.writeToLogs("Market Feedback");
			action.marketFeedback_RFP();
			action.writeToLogs("");
			action.writeToLogs("Message Board");
			action.messageBoard();
			action.writeToLogs("");
			action.writeToLogs("Include Bidder Aggreement");
			action.includeBidderAgreement();
			break;

		case "Forward Auction":
			action.writeToLogs("Auction Format");
			action.auctionFormat();
			action.writeToLogs("");
			action.writeToLogs("Timing Rules");
			action.timingRules_ForwardAuction();
			action.writeToLogs("");
			action.writeToLogs("Bidding Rules");
			action.biddingRules_RFP();
			action.writeToLogs("");
			action.writeToLogs("Currency Rules");
			action.currencyRules_RFI();
			action.writeToLogs("");
			action.writeToLogs("Project Owner Actions");
			action.projectOwnerActions_RFI();
			action.writeToLogs("");
			action.writeToLogs("Market Feedback");
			action.marketFeedback_RFP();
			action.writeToLogs("");
			action.writeToLogs("Message Board");
			action.messageBoard();
			action.writeToLogs("");
			action.writeToLogs("Include Bidder Aggreement");
			action.includeBidderAgreement();
			break;
		}
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");


		action.clickButton("Next");
		if (action.isElementVisible(By.className("msgText"), 2)){
			action.writeToLogs("[ERROR] There's a problem that requires completion or correction in order to complete your request.");
			return;
		}
		action.clickButton("Next");
		action.waitFor(3);

		action.writeToLogs("----------------CONTENT-----------------");
		action.sendKeysEnter(By.linkText("Content"));
		action.click(By.linkText("Content"));
		action.waitFor(2);
		action.configureEventContent();
		action.writeToLogs("------------------------------------------");
		action.writeToLogs("");

		action.clickButton("Next");
		action.waitFor(2);
		action.clickButton("Exit");
		action.waitFor(2);
		action.click(By.linkText("return to project"));
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
