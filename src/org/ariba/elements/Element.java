
package org.ariba.elements;

import org.openqa.selenium.By;

public class Element {
	
	//Login page
	public final static By txtUserName = new By.ById("UserName");
	public final static By txtPassword = new By.ById("Password");
	public final static By btnLogin = new By.ByXPath("//input[@type='submit' and @value='Login']");
	public final static By lblErrorLogin = new By.ByClassName("w-md-icon-error-m");
	
	//Navigate
	public final static By lnkManage = new By.ByXPath("//a[@_mid='Manage']");
	public final static By lnkTemplates = new By.ByXPath("//div[@class='awmenuEx awmenu w-pm-menu']//a[@title='Templates']");
	public final static By lnkSourcingLibrary = new By.ByXPath("//div[@class='awmenuEx awmenu w-pm-menu']//a[@title='Sourcing Library']");
	
	//Templates page
	public final static By lblTemplatesPageHead = new By.ByXPath("//div[@class='w-page-head a-pi-header-full' and contains(text(),'Templates')]");
	public final static By lnkOverviewTab = new By.ByXPath("//div[@class='w-page-tabs']//a[text()='Overview']");
	public final static By lnkPropertiesActions = new By.ByXPath("//a[@_mid='TemplateActionMenu']");
	
	public final static By lnkOverviewActions = new By.ByXPath("//a[@_mid='OverviewMenu']");
	public final static By lnkFullView = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Full View')]");
	public final static By lnkEditProperties = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Edit Properties')]");
	public final static By lnkPublish = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Publish')]");
	public final static By lnkEditTeam = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Edit Team')]");
	
	//Common Elements
	public final static By btnOK = new By.ByXPath("//button/span[contains(text(),'OK')]");
	public final static By btnExit = new By.ByXPath("//button/span[contains(text(),'Exit')]");
	public final static By btnIgnore = new By.ByXPath("//button/span[contains(text(),'Ignore')]");
	public final static By btnActions = new By.ByXPath("//button/span[contains(text(),'Actions')]");
	public final static By lnkCondition = new By.ByXPath("//label[contains(text(),'Condition')]/../following-sibling::td//a");
	public final static By btnWrapperOk = new By.ByXPath("//div[contains(@class,'w-dlg-buttons')]//button/span[contains(text(),'OK')]");
	
	//Chooser Field
	public final static By lnkSearchMore = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[@class='w-chSearchLink']");
	public final static By txtSearchField = new By.ByXPath("//input[@aria-label='Search for a specific value']");
	public final static By btnSearchField = new By.ByXPath("//button[@title='Search for a specific value in the list']");
	public final static By btnDoneSearch = new By.ByXPath("//button[@title='Done Button']");
	
	
	//Create New Project Template
	public final static By txtProjectName = new By.ByXPath("//label[text()='Name:']/../following-sibling::td[2]//input");
	public final static By txtProjectDescription = new By.ByXPath("//iframe[contains(@id,'XinhaIFrame')]");
	public final static By drpBaseLanguage = new By.ById("_hy1brd");
	public final static By rdoFullProject = new By.ByXPath("//span[text()='Full Project']/preceding-sibling::div//label");
	public final static By rdoQuickProject = new By.ByXPath("//span[text()='Quick Project']/preceding-sibling::div//label");
	
	//Template Overview Page
	public final static By lblProjectName = new By.ByClassName("w-page-head");
	
	
	//Add Group
	public final static By btnAddGroup = new By.ByXPath("//button/span[normalize-space()='Add Group']");
	public final static By txtGroupTitle = new By.ByXPath("//label[text()='Title:']/../following-sibling::td[2]//input");
	public final static By drpCanOwnerEdit = new By.ById("_dwwkhd");
	public final static By optYes = new By.ById("_dwwkhd0");
	public final static By optNo = new By.ById("_dwwkhd1");
	public final static By lnkSelectRole = new By.ByXPath("//label[text()='Roles:']/../following-sibling::td[2]//a[text()='select']");
	public final static By txtSearch = new By.ById("_ejawzd");
	public final static By btnSearch = new By.ById("_dyrw2b");
	public final static By chkSelect = new By.ByXPath("//input[@id='_pf9st']/following-sibling::label");
	public final static By btnDone = new By.ById("_9l$jub");
	
	//Task Actions
	public final static By lnkTaskActions = new By.ByXPath("//a[@_mid='CreateMenu' and normalize-space(text()='Actions')]");
	
	//Team tab
	public final static By lnkTeamTab = new By.ByXPath("//div[@class='w-page-tabs']//a[text()='Team']");

	
	//Approval Rule Flow Type
	public final static By rdoParallel = new By.ByXPath("//span[contains(.,'Parallel')]/preceding-sibling::div//label");
	public final static By rdoSerial = new By.ByXPath("//span[contains(.,'Serial')]/preceding-sibling::div//label");
	public final static By rdoCustom = new By.ByXPath("//span[contains(.,'Custom')]/preceding-sibling::div//label");
	
	
	//Actions
	public final static By lnkOpen = new By.ByXPath("//div[@class='awmenu w-pm-menu']//b[text()='Open']");
	public final static By lnkEditOpen = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(@class,'w-pmi-item') and contains(.,'Open')]");
	public final static By lnkEditContent = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Edit')]");
	public final static By lnkEditAttributes = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Edit Attributes')]");
	public final static By lnkEditTask = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Edit Task')]");
	public final static By lnkEdit = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Edit')]");
	public final static By lnkDelete = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Delete')]");
	public final static By lnkViewTaskDetails = new By.ByXPath("//div[@class='awmenu w-pm-menu']//b[text()='View Task Details']");
	public final static By lnkPhaseTaskDetails = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[normalize-space()='View Phase Details']");
	public final static By lnkCreatePhase = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following-sibling::a[contains(text(),'Phase')]");
	public final static By lnkCreateToDoTask = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following-sibling::a[contains(text(),'To Do')]");
	public final static By lnkNotificationTask = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following-sibling::a[contains(text(),'Notification')]");
	public final static By lnkCreateReviewTask = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following-sibling::a[contains(text(),'Review')]");
	public final static By lnkCreateNegotiationTask = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following-sibling::a[contains(text(),'Negotiation')]");
	public final static By lnkCreateApprovalTask = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following-sibling::a[contains(text(),'Approval')]");
	public final static By lnkCreateSignatureTask = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Create')]/following-sibling::a[contains(text(),'Signature')]");
	
	public final static By lblPageHead = new By.ByClassName("w-page-head");
	public final static By lnkTaskActionsForReview = new By.ByXPath("//a[@_mid='TaskActionsForReview']");
	public final static By lnkPhaseActions = new By.ByXPath("//a[@_mid='PhaseActions']");
	
	//Predecessors
	public final static By imgTableOptions = new By.ByXPath("//div[@title='Table Options Menu']");
	public final static By lnkExpandAll = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Expand All')]");
	
	
	//Documents
	public final static By lnkDocumentsTab = new By.ByXPath("//a[@role='tab' and text()='Documents']");
	public final static By lnkUploadDocument = new By.ByXPath("//div[@class='awmenu w-pm-menu']//div[contains(text(),'Upload')]/following-sibling::a[contains(text(),'Document')]");
	public final static By lnkAssociateDocument = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Associate Document')]");
	public final static By lnkDocumentChoice = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Document Choice')]");
	public final static By lnkDocumentChoicePrev = new By.ByXPath("//span[normalize-space()='Page']/preceding-sibling::a/span[@class='w-batch-nav']");
	public final static By lnkDocumentChoiceNext = new By.ByXPath("//span[normalize-space()='Page']/following-sibling::a/span[@class='w-batch-nav']");
	
	//Event
	
	
	public final static By txtPrebidEndTime = new By.ByXPath("//label[contains(text(),'Prebid')]/../following-sibling::td[2]//input[contains(@class,'img-right-time')]");
	public final static By rdoWhenClickPublish = new By.ByXPath("//label[contains(text(),'When I Click the')]/../preceding-sibling::td//label");
	public final static By rdoScheduleFortheFuture = new By.ByXPath("//label[text()='Schedule For the Future:']/../preceding-sibling::td//label");
	public final static By txtScheduleForTheFuture_Date = new By.ByXPath("//label[normalize-space()='Schedule For the Future:']/following-sibling::table//input");
	public final static By txtScheduleForTheFuture_Time = new By.ByXPath("//label[normalize-space()='Schedule For the Future:']/following-sibling::table//input[contains(@class,'img-right-time')]");
	
	public final static By txtResponseStartDate_Time = new By.ByXPath("//label[contains(text(),'Response')]/../following-sibling::td[2]//input[contains(@class,'img-right-time')]");
	public final static By txtBiddingStartTime_Time = new By.ByXPath("//label[contains(text(),'Bidding')]/../following-sibling::td[2]//input[contains(@class,'img-right-time')]");
	public final static By txtResponseStartDate_ScheduleForTheFuture_Date = new By.ByXPath("//label[contains(text(),'Schedule For the Future:')]/../../following-sibling::tr//input");
	public final static By txtResponseStartDate_ScheduleForTheFuture_Time = new By.ByXPath("//label[contains(text(),'Schedule For the Future:')]/../../following-sibling::tr//input");
	public final static By txtDueDate_Duration = new By.ByXPath("//label//span[text()='Duration:']/../following-sibling::input");
	
	//Error Message
	public final static By msgText = new By.ByClassName("msgText");
	public final static By txtOtherEmailAddress = new By.ByXPath("//label[contains(text(),'Email')]/../../following-sibling::tr//input[@type='text']");
	public final static By lblEvent = new By.ByXPath("//div[@class='wizTOCHeader w-wizard-toc-header']");
	
	
	//Add KPI
	public final static By rdoKPISupportingData = new By.ByXPath("//span[contains(text(),'This KPI will contain supporting data (KPIs, questions, and/or requirements)')]/preceding-sibling::div//label");
	public final static By rdoKPIBasedonSurvey = new By.ByXPath("//span[contains(text(),'This KPI will be based on Survey or Report data')]/preceding-sibling::div//label");
	public final static By lnkAttFile = new By.ByXPath("//a[contains(.,'Attach a file')]");
	public final static By lnkUpdateFromDesktop = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(.,'Upload a file from desktop')]");
	public final static By lnkReport = new By.ByXPath("//td[text()='Report Metric']/following-sibling::td[2]//a");
	public final static By txtRangeLow = new By.ByXPath("//td[text()='Range:']/following-sibling::td[2]//input");
	public final static By txtRangeUp = new By.ByXPath("//td[text()='Range:']/following-sibling::td[2]//label//input");
	
	//Add Lot
	public final static By rdoItemLot = new By.ByXPath("//span[starts-with(text(),'Item Lot')]/preceding-sibling::div//label");
	public final static By rdoBasket = new By.ByXPath("//span[starts-with(text(),'Basket')]/preceding-sibling::div//label");
	public final static By rdoBasketNoItems = new By.ByXPath("//span[starts-with(text(),'Basket with No Items')]/preceding-sibling::div//label");
	public final static By rdoBundle = new By.ByXPath("//span[starts-with(text(),'Bundle')]/preceding-sibling::div//label");
	public final static By chkApplyToAll = new By.ByXPath("//td[contains(text(),'Apply to all items and lots')]//label");
	public final static By btnDropDown = new By.ByXPath("//div[@id='_gxv6gb']/span[2]");
	public final static By lnkYesCustom = new By.ById("_gxv6gb1");
	
	
	//Add Question
	public final static By lnkRefDoc = new By.ByXPath("//a[contains(text(),'Attach a file')]");
	public final static By lnkUpdateDesktop = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Upload a file from desktop')]");
	public final static By lnkSelectFromLibrary = new By.ByXPath("//div[@class='awmenu w-pm-menu']//a[contains(text(),'Select file from library')]");
	public final static By txtRangeLower = new By.ByXPath("//td[text()='Range:']/following-sibling::td[2]//input");
	public final static By txtRangeUpper = new By.ByXPath("//td[text()='Range:']/following-sibling::td[2]//label//input");
//	public final static By rdoSpecifyValue = new By.ByCssSelector("label.w-rdo.w-rdo-dsize");
//	public final static By rdoSelectMultipleValues = new By.ByCssSelector("#_teqhdb > span.w-rdo-list > label > div.w-rdo-container > label.w-rdo.w-rdo-dsize");
	public final static By btnAdd = new By.ByXPath("//td[contains(@class,'tableFooter')]//button/span[contains(text(),'Add')]");
	
	//Add Attachment from Library
	public final static By rdoSearch = new By.ByXPath("//td/label[starts-with(text(),'Search')]/../preceding-sibling::td//label");
	public final static By rdoExplore = new By.ByXPath("//td/label[starts-with(text(),'Explore')]/../preceding-sibling::td[1]//label");
	public final static By txtSearchTerm = new By.ByXPath("//input[@_pl='Search using Title, ID, or any other term']");
	public final static By chkFirstSelection = new By.ByXPath("//tr[@class=' tableRow1']//label[@bh='RDO']");
	public final static By btnSearchDoc = new By.ByXPath("//button/span[normalize-space()='Search']");
//	public final static By chkDoc = new By.ByXPath("//tr[@id='_v88j5b']/td/span/div/label");
//	public final static By txtDocDesc = new By.ByXPath("//div[@id='_bnfc7']/table/tbody/tr[3]/td[2]");
	
	//Add Requirement
	public final static By lnkRefDocument = new By.ByLinkText("Attach a file");
//	public final static By lnkDesktopUpdate = new By.ById("_yrrjdd");
//	public final static By lnkSelectLibrary = new By.ById("_dlvflc");
	
	
	//Add Attachment
	public final static By lnkAttachment = new By.ByXPath(("//td[text()='Attachment:']/following-sibling::td[2]//a[contains(.,'Attach a file')]"));
	
	//Add Section
//	public final static By lnkVisibilityCondition = new By.ById("_bq3a0");
//	public final static By lnkOthers = new By.ById("_l_hrnb");
//	public final static By lnkCreateCondition = new By.ById("_kab89d");
		
	//Add Line Items
//	public final static By chkApplyToAll = new By.ByXPath("//tr[@id='_abln7c']/td[3]/table/tbody/tr/td/table/tbody/tr/td[2]/div/label");
	public final static By rdoUnitBidding = new By.ByXPath("//label[contains(text(),'unit bidding')]/preceding-sibling::div//label");
	public final static By rdoExtendedBidding = new By.ByXPath("//label[contains(text(),'extended bidding')]/preceding-sibling::div[1]//label");
		
	//Add Content from Library
	public final static By rdoExploreLibrary = new By.ByXPath("//label[@for='explore']/../preceding-sibling::td[1]//label");
	public final static By rdoExploreProject = new By.ByXPath("//label[@for='exploreProject']/../preceding-sibling::td[1]//label");
	public final static By rdoSearchLibrary = new By.ByXPath("//label[@for='search']/../preceding-sibling::td[1]//label");
	public final static By chkCopyVisibility = new By.ByXPath("//label[@for='SelectCopyConditionsId']/preceding-sibling::div[1]//label");
	public final static By chkAllContent = new By.ByXPath("//th[contains(@class,'tableHead')]//label[@bh='CHK']");
//	public final static By chkCopyParticipant = new By.ByXPath("//div[@id='_8vlxud']/div/div/div/div[3]/label");
	public final static By txtTitle = new By.ByXPath("//td[text()='Title:']/following-sibling::td//input");
	public final static By txtKeywords = new By.ByXPath("//td[text()='Keywords:']/following-sibling::td//input");
	
	//Add Formula
//	public final static By txtFormula = new By.ById("_ssosl");

	public final static By lblStatus = new By.ByXPath("//td[text()='Keywords:']/following-sibling::td//input");
	public final static By lblRequiredTasks = new By.ByXPath("//span[contains(@class,' u-required-task')]");
	public final static By lblTaskPageHead = new By.ByXPath("//td[@class='w-stepnavbar-title-area']/span");
	public final static By tasksRowCount =  new By.ByXPath("//table[@class='tableBody']//tr[contains(@class,'awtDrg_planTree')]/td[1]");
	
	public final static By lnkParentPhase = new By.ByXPath("//div[contains(@class,'accentBox')]/../preceding-sibling::span[1]//a");
	public final static By lnkDocAssociated = new By.ByXPath("//div[contains(@class,'accentBox')]/../preceding-sibling::span[1]//a");
	public final static By lblNoItems = new By.ByXPath("//td[normalize-space()='No items']");
	
	
	//Event Template
	public final static By divCollapseIcon = new By.ByXPath("//div[contains(@class,'w-oc-icon-off')]");
	public final static By divDefinitionHeader = new By.ByXPath("//div[contains(@class,'accentBox bodyBold leg-p-2-5-0-2 flL a-path-node a-path-node-hilite')]");
	public final static By lnkDefinitionName = new By.ByXPath("//a[contains(@_mid,'AtomicContentMenu')]");
	
	//Line Items
	public final static By txtInitialPrice = new By.ByXPath("//td[contains(@class,'tableBody w-tbl-cell') and contains(.,'Price')]/following-sibling::td[1]//input");
	public final static By txtHistoricPrice = new By.ByXPath("//td[contains(@class,'tableBody w-tbl-cell') and contains(.,'Price')]/following-sibling::td[2]//input");
	public final static By txtResercePrice = new By.ByXPath("//td[contains(@class,'tableBody w-tbl-cell') and contains(.,'Price')]/following-sibling::td[3]//input");
	public final static By txtQuantity = new By.ByXPath("//td[contains(@class,'tableBody w-tbl-cell') and contains(.,'Quantity')]/following-sibling::td[1]//input");
	public final static By txtInitialExtendedPrice = new By.ByXPath("//td[contains(@class,'tableBody w-tbl-cell') and contains(.,'Extended Price')]/following-sibling::td[1]//input");
	public final static By txtHistoricExtendedPrice = new By.ByXPath("//td[contains(@class,'tableBody w-tbl-cell') and contains(.,'Extended Price')]/following-sibling::td[2]//input");
	public final static By txtReserveExtendedPrice = new By.ByXPath("//td[contains(@class,'tableBody w-tbl-cell') and contains(.,'Extended Price')]/following-sibling::td[3]//input");
}

