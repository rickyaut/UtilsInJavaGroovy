package com.rickyaut.tools.fatwire;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.rickyaut.tools.common.Utils;

public class FatwireDriver7Impl implements FatwireDriver {

	protected static final Log logger = LogFactory.getLog(FatwireDriver7Impl.class);
	private static final String LOGIN_URL = "/ContentServer?pagename=OpenMarket/Xcelerate/UIFramework/LoginPage";
	protected static final String REPLACE_ASSETID = "Variable.AssetId";
	protected static final String REPLACE_ASSETTYPE = "Variable.AssetType";
	protected static final String INSPECT_URL = "/ContentServer?id=Variable.AssetId&AssetType=Variable.AssetType&cs_environment=standard&pagename=OpenMarket%2FXcelerate%2FActions%2FContentDetailsFront&cs_formmode=WCM";
	protected static final String SEARCH_URI = "/ContentServer?pagename=OpenMarket%2FXcelerate%2FActions%2FShowSearches";
	protected static final String NEW_URL = "/ContentServer?pagename=OpenMarket%2FXcelerate%2FActions%2FShowStartMenuItems";
	protected static final String WORKFLOW_URL = "/ContentServer?pagename=OpenMarket%2FXcelerate%2FActions%2FShowWorkflowFront";
	protected static final String PUBLISHING_URL = "/ContentServer?pagename=OpenMarket%2FXcelerate%2FActions%2FPublishConsoleFront";
	private static final String PREVIEW_FINDCHILDRENASSET_URL = "/ContentServer?id=Variable.AssetId&cs_environment=standard&pagename=OpenMarket%2FXcelerate%2FActions%2FInspectElementAssetFront&cs_formmode=WCM";
	private static final String LIST_ALL_ASSETTYPE_URL = "/ContentServer?action=list&cs_environment=standard&pagename=OpenMarket%2FXcelerate%2FAdmin%2FAssetTypeFront&cs_formmode=WCM";
	private static final String FINDCHILDNODECSELEMENTNAME = "VJFindChildNode";

	protected WebDriver driver;
	private Map<String, AssetType> assetTypes;
	private String appContextURL;
	private String findChildNodeCSElementId;
	private String currentUserName;

	public FatwireDriver7Impl(WebDriver driver) {
		super();
		this.driver = driver;
		assetTypes = new LinkedHashMap<String, AssetType>();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void login(String appContextURL, String userName, String password, String siteName) {
		this.appContextURL = appContextURL;
		this.currentUserName = userName;
		driver.get(appContextURL + LOGIN_URL);
		setValue(driver.findElement(By.name("username")), userName);
		setValue(driver.findElement(By.name("password")), password);
		driver.findElement(By.id("loginform")).submit();
		selectSite(siteName);
	}

	@Override
	public void ensureFindChildNodeElementExists() throws FatwireDriverException {
		searchAssetByName("CSElement", FINDCHILDNODECSELEMENTNAME);
		List<WebElement> links = driver.findElements(By.xpath("//a[img[@alt='Inspect this item']]"));
		if (links.isEmpty()) {
			newAsset("CSElement", AssetTypeEnum.Simple);
			setValue(findNameInput("CSElement", AssetTypeEnum.Simple), FINDCHILDNODECSELEMENTNAME);
			setValue(findDescInput(AssetTypeEnum.Simple), FINDCHILDNODECSELEMENTNAME);
			setValue(driver.findElement(By.name("CSElement:Arguments:0:name")), "assetId");
			driver.findElement(By.name("CSElement:Arguments:0:name"))
					.findElement(By.xpath("following-sibling::a[position()=1]")).click();
			driver.findElement(By.name("CSElement:Arguments:0:required")).click();
			setValue(driver.findElement(By.name("CSElement:Arguments:1:name")), "assetShortType");
			driver.findElement(By.name("CSElement:Arguments:1:name"))
					.findElement(By.xpath("following-sibling::a[position()=1]")).click();
			driver.findElement(By.name("CSElement:Arguments:0:required")).click();
			clickOnContinue();
			executeJavascript("setJspString()", "");
			driver.findElement(By.name("CSElement:url")).clear();
			try {
				String content = Utils.getResourceContentFromClassPath(FatwireDriver7Impl.class.getPackage().getName()
						.replace(".", "/")
						+ "/" + FINDCHILDNODECSELEMENTNAME + ".txt");
				setValue(driver.findElement(By.name("CSElement:url")), content);
			} catch (IOException e) {
				executeJavascript("confirm(arguments[0])",
						"The PangAppleFindChildNode CSElement was not created successfully?");
				e.printStackTrace();
			}
			checkFieldsAndSubmit();
			findChildNodeCSElementId = findCurrentAssetID();
		} else {
			findChildNodeCSElementId = Utils
					.findRegularExpressionMatcher(links.get(0).getAttribute("href"), "\\d{13}")[0];
		}
	}

	protected void listChildAsset(String assetID, String assetType) {
		/*
		 * String hostName = getHostNameFromWebDriver(driver); driver.get(hostName + PREVIEW_FINDCHILDRENASSET_URL);
		 * driver.findElement(By.name("assetId")).sendKeys(assetID); driver.findElement(By.name("AppForm")).submit();
		 */
	}

	protected String translateXPathStringLowerCase(String xpathString) {
		return String.format("translate(\"%s\", \"%s\", \"%s\")", xpathString, "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
				"abcdefghijklmnopqrstuvwxyz");
	}

	@Override
	public void get(String url) {
		driver.get(url);
	}

	@Override
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return driver.findElement(by);
	}

	@Override
	public String getPageSource() {
		return driver.getPageSource();
	}

	@Override
	public void close() {
		driver.close();
	}

	@Override
	public void quit() {
		/*
		 * if(StringUtils.isNotBlank(findChildNodeCSElementId)){ inspectAsset("CSElement", false, null,
		 * findChildNodeCSElementId); deleteCurrentAsset(); }
		 */
		driver.quit();
	}

	@Override
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		return driver.switchTo();
	}

	@Override
	public Navigation navigate() {
		return driver.navigate();
	}

	@Override
	public Options manage() {
		return driver.manage();
	}

	@Override
	public void changeSite(String newSiteName) {
		driver.switchTo().defaultContent();
		driver.switchTo().frame("XcelBanner");
		driver.findElement(By.linkText("Site:")).click();
		driver.switchTo().defaultContent();
		selectSite(newSiteName);
	}

	public void selectSite(String newSiteName) {
		driver.switchTo().frame("XcelWorkFrames");
		driver.findElement(By.linkText(newSiteName)).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame("XcelWorkFrames").switchTo().frame("XcelAction");
	}

	@Override
	public void browseTo(String url) {
		if (StringUtils.startsWithIgnoreCase(url, appContextURL)) {
			url = url.substring(appContextURL.length());
		}
		if (StringUtils.startsWithIgnoreCase(url, "http://") || StringUtils.startsWithIgnoreCase(url, "https://")) {
			driver.get(url);
		} else {
			StringBuffer theURL = new StringBuffer(appContextURL);
			if (!appContextURL.endsWith("/") && !url.startsWith("/")) {
				theURL.append("/");
			} else if (appContextURL.endsWith("/") && url.startsWith("/")) {
				theURL.deleteCharAt(theURL.lastIndexOf("/"));
			}
			theURL.append(url);
			driver.switchTo().defaultContent();
			executeJavascript("window.frames[1].frames[1].location.href=arguments[0]", theURL.toString());
			driver.switchTo().frame("XcelWorkFrames").switchTo().frame("XcelAction");
		}
	}

	@Override
	public void listChildAssets(String assetType, String assetID) {
		String url = PREVIEW_FINDCHILDRENASSET_URL.replace(REPLACE_ASSETID, findChildNodeCSElementId);
		browseTo(url);
		setValue(driver.findElement(By.name("assetShortType")), assetType);
		setValue(driver.findElement(By.name("assetId")), assetID);
		driver.findElement(By.name("AppForm")).submit();
	}

	@Override
	public boolean isInspectingAsset() {
		try {
			findCurrentAssetName();
		} catch (FatwireDriverException e) {
			return false;
		}
		return true;
	}

	@Override
	public void browseToWorkflow(String workflowName) throws FatwireDriverException {
		browseTo(WORKFLOW_URL);
		try {
			WebElement workflowAnchor = driver.findElement(By.linkText(workflowName));
			workflowAnchor.click();
		} catch (Exception ex) {
			throw new FatwireDriverException(String.format("workflow %s not exists", workflowName));
		}
	}

	public void publish(String dest) {
		browseTo(PUBLISHING_URL);
		Select select = new Select(driver.findElement(By.name("target")));
		List<WebElement> options = select.getOptions();
		for (int index = 0; index < options.size(); index++) {
			WebElement option = options.get(index);
			if (option.getText().toUpperCase().startsWith(dest.toUpperCase())) {
				select.selectByIndex(index);
				driver.findElement(By.name("AppForm")).submit();
				break;
			}
		}
		executeJavascript("return setCheck(document.forms[0]);", "");

	}

	@Override
	public void searchAssetByName(String assetType, String keyword) {
		browseTo(SEARCH_URI);
		browseFirstSearchPage(assetType);
		if (!StringUtils.isBlank(keyword)) {
			setValue(driver.findElement(By.name("SearchString")), keyword);
		}
		executeJavascript("setPagename();", "");
	}

	private void browseFirstSearchPage(String assetType) {
		driver.findElement(By.xpath("//tr[td[3]='" + assetType + "']/td[5]//a")).click();
	}

	@Override
	public void searchAssetByID(String assetTypeDesc, String assetID) throws FatwireDriverException {
		browseTo(SEARCH_URI);
		browseFirstSearchPage(assetTypeDesc);
		driver.findElement(By.linkText("advanced search")).click();
		setValue(driver.findElement(By.name("Id")), assetID);
		AssetTypeEnum flexType = findAssetTypeByDesc(assetTypeDesc).getType();
		if (AssetTypeEnum.Simple.equals(flexType)) {
			executeJavascript("return checkFields();", "");
		} else {
			WebElement updatedAfterTextElement = driver.findElement(By.name("UpdatedAfterText"));
			String updatedAfterText = updatedAfterTextElement.getAttribute("value");
			WebElement updatedBeforeTextElement = driver.findElement(By.name("UpdatedBeforeText"));
			String updatedBeforeText = updatedBeforeTextElement.getAttribute("value");
			WebElement form = driver.findElement(By.name("AppForm"));
			executeJavascript(
					"formatSetDate(arguments[0], arguments[1], arguments[2]); formatSetDate(arguments[3], arguments[4], arguments[5]); arguments[6].submit()",
					updatedAfterText, updatedAfterTextElement, "UpdatedAfter", updatedBeforeText,
					updatedBeforeTextElement, "UpdatedBefore", form);
		}
	}

	@Override
	public void searchAssetByAttribute(String assetType, NameValuePair... query) throws FatwireDriverException {
		browseTo(SEARCH_URI);
		browseFirstSearchPage(assetType);
		driver.findElement(By.linkText("advanced search")).click();
		WebElement form = driver.findElement(By.name("AppForm"));
		executeJavascript("if( Fixpagename('SearchFront','SelectAttributes')){form.submit();}", form);
		for (NameValuePair p : query) {
			selectOptionByText(driver.findElement(By.name("MyTmplAttributes")), true, p.getName());
		}
		driver.findElement(By.name("Add1")).click();
		executeJavascript("selAllAll(); return Fixpagename('SearchFront','AttrSearchForm')", "");
		for (NameValuePair p : query) {
			List<WebElement> elements = findRightCellByLeftTitle(p.getName() + " is").findElements(By.tagName("input"));
			for (WebElement e : elements) {
				if (e.isDisplayed()) {
					e.clear();
					setValue(e, p.getValue());
				}
			}
		}
		form = driver.findElement(By.name("AppForm"));
		executeJavascript("if(validateSearchFields()){arguments[0].submit()}", form);
	}

	@Override
	public boolean hasMoreSearchResult() {
		WebElement nextLink = findNextLinkOfSearchResult();
		return nextLink != null;
	}

	private WebElement findNextLinkOfSearchResult() {
		WebElement nextLink = null;
		List<WebElement> elements = driver.findElements(By.cssSelector("span>a"));
		for (WebElement e : elements) {
			if (e.getText().matches("^Next \\d+$")) {
				nextLink = e;
				break;
			}
		}
		return nextLink;
	}

	@Override
	public boolean scrollToNextSearchResult() {

		WebElement nextLink = findNextLinkOfSearchResult();
		if (nextLink != null) {
			nextLink.click();
			return true;
		}
		return false;
	}

	@Override
	public void newAsset(String assetType, AssetTypeEnum flexType) {
		browseTo(NEW_URL);
		String type = assetType;
		if (AssetTypeEnum.Parent.equals(flexType)) {
			type += " Parent";
		}
		driver.findElement(By.xpath("//tr[td[3]='" + type + "']/td[5]//a")).click();
		assignCurrentAsset(currentUserName);
	}

	@Override
	public void editCurrentAsset() {
		WebElement lnkEdit = driver.findElement(By.xpath("//a[.=\"Edit\"]"));
		if (lnkEdit != null) {
			lnkEdit.click();
		}
	}

	@Override
	public void deleteCurrentAsset() {
		driver.findElement(By.xpath("//a[.=\"Delete\"]")).click();
		driver.findElement(By.name("AppForm")).submit();
	}

	@Override
	public void copyCurrentAsset(String... assignee) {
		clickOnEditFunctionsOptionStartWith("Copy");
		try {
			driver.findElement(By.xpath("//div[text()='Choose Assignees']"));
			if (assignee != null) {
				assignCurrentAsset(assignee);
			} else {
				assignCurrentAsset(currentUserName);
			}
		} catch (Exception e) {
		}

	}

	@Override
	public void clickOnContinue() {
		executeJavascript("return FixpagenameWithCheck('CopyFront','ContentFormElement');", new Object[0]);
	}

	public void assignCurrentAsset(String... assignee) {
		List<WebElement> ask = driver.findElements(By.tagName("select"));
		for (WebElement e : ask) {
			if (StringUtils.startsWith(e.getAttribute("name"), "ask:")) {
				if (assignee != null) {
					selectOptionByText(e, false, assignee);
					executeJavascript("if(SetAssignees()!=false){arguments[0].submit(); return false;}",
							driver.findElement(By.name("AppForm")));
				}
			}
		}
	}

	public void submitForm(String formName) {
		driver.findElement(By.name(formName)).submit();
	}

	public void checkFieldsAndSubmit() {
		executeJavascript("return checkfields();");
	}

	private void clickOnEditFunctionsOptionStartWith(String s) {
		List<WebElement> elements = driver.findElement(By.name("EditFunctions")).findElements(By.tagName("option"));
		for (WebElement e : elements) {
			if (e.getText().startsWith(s)) {
				e.click();
				return;
			}
		}
	}

	@Override
	public void sendCurrentAssetToWorkFlow(String workflowName, String... assignee) {
		clickOnEditFunctionsOptionStartWith("Status");
		if(selectOptionByText(driver.findElement(By.name("AssignFunctions")), true, "Add to Workflow Group")){
			String href =null;
			int count = 5;
			while(count-->0){
			  try{
	        href = driver.findElement(By.linkText(workflowName)).getAttribute("href");
	        if(StringUtils.isNotBlank(href)){
	          break;
	        }
			  }catch(Exception ex){
			    try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
			    continue;
			  }
			}
			String workflowID = Utils.findRegularExpressionMatcher(href, "\\d{13}")[0];
			driver.findElement(By.xpath("//input[@value='" + workflowID + "']")).click();
			driver.findElement(By.name("AppForm")).submit();
		}
		assignCurrentAsset(assignee);
	}

	@Override
	public void removeCurrentAssetFromWorkflow(String workflowName) {
		clickOnEditFunctionsOptionStartWith("Status");
		if(selectOptionByText(driver.findElement(By.name("AssignFunctions")), true, "Remove from Workflow Group"))
			driver.findElement(By.name("AppForm")).submit();
		if(selectOptionByText(driver.findElement(By.name("AssignFunctions")), true, "Remove from Workflow"))
			driver.findElement(By.name("AppForm")).submit();
	}

	// setParentNode(auDriver, mediaType, null, tempParents.get(parentDef), TEMP_PREFIX.concat(parentDef), "Media_P",
	// "ParentSelect", "d"+currentParentID);
	// setParentNode(nzDriver, "Media_P", null, tempParents.get(parentDef), TEMP_PREFIX.concat(parentDef), "Media_P",
	// "ParentSelect", null);
	// String tp, dtp;
	// tp = dtp = "";
	// for(WebElement input : inputs){
	// String inputName = input.getAttribute("name");
	// if(inputName.toUpperCase().matches("^TP\\d{13}$")){
	// tp = inputName;
	// }else if(inputName.toUpperCase().matches("^DTP\\d{26}$")){
	// dtp = inputName;
	// }
	// }
	// setParentNode(nzDriver, "Media_P", null, newMediaID, mediaName, "Media_C", tp, dtp);
	private void setParentNode(WebDriver driver, String assetType, String assetID, String newParentID,
			String newParentName, String newParentType, String tp, String dtp) {
		WebElement appForm, dParentId, templateChosen, pageName;
		// start to add new one
		appForm = driver.findElement(By.name("AppForm"));
		StringBuffer buf = new StringBuffer();
		buf.append("id=").append(newParentID).append(",assettype=").append(newParentType).append(",")
				.append(Utils.encodeString(newParentName));
		setValue(appForm.findElement(By.name(tp)), buf.toString());

		templateChosen = findHiddenTemplateChosen(appForm);
		// TODO setHiddenElementValue(driver, templateChosen, getHiddenElementValue(driver,
		// findTemplateIdInput(StringUtils.equals("Media_P", assetType)?"flexgroups:flexgrouptemplateid":null)));
		pageName = findHiddenPagename(appForm);
		try {
			if (findRightCellByLeftTitle("ID") != null) {
				setValue(pageName, "OpenMarket/Xcelerate/Actions/EditFront");
			} else {
				setValue(pageName, "OpenMarket/Xcelerate/Actions/NewContentFront");
			}
		} catch (Exception ex) {
			setValue(pageName, "OpenMarket/Xcelerate/Actions/NewContentFront");
		}
		try {
			dParentId = appForm.findElement(By.name("d" + newParentID));
			if (dParentId != null) {
				setValue(dParentId, "");
			}
		} catch (Exception ex) {

		}
		appForm.submit();
		driver.findElement(By.name("AppForm")).submit();
	}

	@Override
	public boolean hasLinkedNamedAttribute(String attrName) {
		logger.warn("method nor implemented, please instanciate a subclass and use inherited one");
		return false;
	}

	@Override
	public void unlinkNamedAttributeFromCurrentAsset(String attrName) {
		logger.warn("method nor implemented, please instanciate a subclass and use inherited one");
	}

	@Override
	public void linkNamedAttributeWithCurrentAsset(String attrName, String associatedAssetName,
			String associatedAssetID, String associatedAssetType) {
		logger.warn("method nor implemented, please instanciate a subclass and use inherited one");
	}

	@Override
	public boolean hasLinkedNamedAssociation() {
		logger.warn("method nor implemented, please instanciate a subclass and use inherited one");
		return false;
	}

	@Override
	public void unlinkNamedAssociationFromCurrentAsset(String currentAssetTypeDesc, String associationDescription)
			throws FatwireDriverException {
		WebElement descSibling = driver.findElement(By.xpath(String.format(
				"//tr[td[text()='%s']]/following-sibling::tr/td", associationDescription)));
		Association association = findAssociationByDesc(currentAssetTypeDesc, associationDescription);
		List<WebElement> inputs = descSibling.findElements(By.tagName("input"));
		for (WebElement input : inputs) {
			if (StringUtils.equalsIgnoreCase(input.getAttribute("type"), "text")) {
				String assetType = findAssetTypeByDesc(currentAssetTypeDesc).getAssetType();
				setValue(
						driver.findElement(By.name(String.format("%s:Association-named:%s", assetType,
								association.getName()))), "");
				setValue(input, "(none)");
				break;
			}
		}
	}

	@Override
	public void linkNamedAssociationWithCurrentAsset(String currentAssetTypeDesc, String associationDescription,
			String associatedAssetID, String associatedAssetName) throws FatwireDriverException {
		WebElement descSibling = driver.findElement(By.xpath(String.format(
				"//tr[td[text()='%s']]/following-sibling::tr/td", associationDescription)));
		Association association = findAssociationByDesc(currentAssetTypeDesc, associationDescription);
		List<WebElement> inputs = descSibling.findElements(By.tagName("input"));
		for (WebElement input : inputs) {
			if (StringUtils.equalsIgnoreCase(input.getAttribute("type"), "text")) {
				String assetType = findAssetTypeByDesc(currentAssetTypeDesc).getAssetType();
				setValue(
						driver.findElement(By.name(String.format("%s:Association-named:%s_type", assetType,
								association.getName()))), findAssetTypeByDesc(association.getChild()).getAssetType());
				setValue(
						driver.findElement(By.name(String.format("%s:Association-named:%s", assetType,
								association.getName()))), associatedAssetID);
				setValue(input, associatedAssetName);
				break;
			}
		}
	}

	private WebElement findHiddenPagename(WebElement appForm) {
		return appForm.findElement(By.name("pagename"));
	}

	private WebElement findHiddenTemplateChosen(WebElement appForm) {
		return appForm.findElement(By.name("TemplateChosen"));
	}

	@Override
	public Object executeJavascript(String script, Object... arguments) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript(script, arguments);
	}

	@Override
	public void shareCurrentAssetWithAnotherSite(boolean toBeShared, String... siteName) {
		clickOnEditFunctionsOptionStartWith("Share");
		List<String> siteIDs = new ArrayList<String>();
		for (String name : siteName) {
			String siteID = driver.findElement(By.xpath("//label[text()='" + name + "']")).getAttribute("for");
			siteIDs.add(siteID);
		}
		List<WebElement> checkboxes = driver.findElements(By.name("pubidlist"));
		for (WebElement checkbox : checkboxes) {
			if (siteIDs.contains(checkbox.getAttribute("value"))) {
				if (checkbox.isSelected() && !toBeShared || !checkbox.isSelected() && toBeShared) {
					checkbox.click();
				}
			}
		}
		driver.findElement(By.name("AppForm")).submit();
	}

	@Override
	public WebElement findNameInput(String assetType, AssetTypeEnum flexType) {
		if (AssetTypeEnum.Simple.equals(flexType)) {
			return driver.findElement(By.name(StringUtils.join(new String[] { assetType, ":", "name" })));
		} else if (AssetTypeEnum.Parent.equals(flexType)) {
			return driver.findElement(By.name("flexgroups:name"));
		} else {
			return driver.findElement(By.name("flexassets:name"));
		}

	}

	@Override
	public WebElement findDescInput(AssetTypeEnum flexType) {
		if (AssetTypeEnum.Simple.equals(flexType)) {
			return driver.findElement(By.name("descriptionvis"));
		} else if (AssetTypeEnum.Parent.equals(flexType)) {
			return driver.findElement(By.name("flexgroups:description"));
		} else {
			return driver.findElement(By.name("flexassets:description"));
		}
	}

	@Override
	public WebElement findHiddenTemplateId(AssetTypeEnum flexType) {
		if (AssetTypeEnum.Parent.equals(flexType)) {
			return driver.findElement(By.name("flexgroups:flexgrouptemplateid"));
		} else {
			return driver.findElement(By.name("flexassets:flextemplateid"));
		}
	}

	@Override
	public WebElement findRightCellByLeftTitle(String title) throws FatwireDriverException {
		try {
			return driver.findElement(By.xpath(String.format(
					"//*[text()=\"%s:\" ]/ancestor::tr[position()=1]/td[last()]", title)));
		} catch (Exception ex) {
			List<WebElement> elements = driver.findElements(By.xpath("//tr/td[1]"));
			for (WebElement element : elements) {
				String text = element.getText().replaceAll("\\*|\\s|\\(|\\)", "").toLowerCase();
				title = title.replaceAll("\\*|\\s|\\(|\\)", "").toLowerCase();
				if (text.equals(title + ":")) {
					return element.findElement(By.xpath("../td[last()]"));
				}
			}
		}
		throw new FatwireDriverException("Invalid title for: " + title);
	}

	@Override
	public WebElement findRightCellByLeftTitle(WebElement element, String title) throws FatwireDriverException {
		try {
			return element.findElement(By.xpath(String.format(
					"//*[normalize-space(text())=\"%s:\" ]/ancestor::tr[position()=1]/td[last()]", title)));
		} catch (Exception ex) {
		}
		throw new FatwireDriverException("Invalid title for: " + title);
	}

	@Override
	public String findCurrentAssetID() throws FatwireDriverException {
		return findRightCellByLeftTitle("ID").getText();
	}

	@Override
	public String findCurrentAssetName() throws FatwireDriverException {
		return findRightCellByLeftTitle("Name").getText();
	}

	@Override
	public String findCurrentAssetDescription() throws FatwireDriverException {
		return findRightCellByLeftTitle("Description").getText();
	}

	@Override
	public void back(int steps) {
		Navigation navigate = driver.navigate();
		for (int i = 0; i < steps; i++) {
			navigate.back();
		}
	}

	@Override
	public void selectRadioByValue(List<WebElement> radios, String value, boolean ignoreCase) {
		for (WebElement e : radios) {
			if (ignoreCase && StringUtils.equalsIgnoreCase(e.getAttribute("value"), value) || !ignoreCase
					&& StringUtils.equals(e.getAttribute("value"), value)) {
				if (!e.isSelected()) {
					e.click();
				}
			}
		}
	}

	@Override
	public WebElement getCheckedRadio(List<WebElement> radios) {
		for (WebElement e : radios) {
			if (e.isSelected()) {
				return e;
			}
		}
		return null;
	}

	@Override
	public boolean selectOptionByText(WebElement dropdown, boolean ignoreCase, String... texts) {
		boolean selected = false;
		List<WebElement> options = dropdown.findElements(By.tagName("option"));
		Select select = new Select(dropdown);
		for (String text : texts) {
			int index = 0;
			for (WebElement option : options) {
				if (ignoreCase && StringUtils.equalsIgnoreCase(option.getText(), text) || !ignoreCase
						&& StringUtils.equals(option.getText(), text)) {
					select.selectByIndex(index);
					selected = true;
					break;
				}
				index++;
			}
		}
		return selected;
	}

	@Override
	public boolean selectOptionByValue(WebElement dropdown, boolean ignoreCase, String... values) {
		boolean selected = false;
		List<WebElement> options = dropdown.findElements(By.tagName("option"));
		Select select = new Select(dropdown);
		for (String text : values) {
			int index = 0;
			for (WebElement option : options) {
				if (ignoreCase && StringUtils.equalsIgnoreCase(option.getAttribute("value"), text) || !ignoreCase
						&& StringUtils.equals(option.getAttribute("value"), text)) {
					select.selectByIndex(index);
					selected = true;
					break;
				}
				index++;
			}
		}
		return selected;
	}

	@Override
	public List<WebElement> getSelectedOptions(WebElement dropdown) {
		return new Select(dropdown).getAllSelectedOptions();
	}

	@Override
	public void selectCheckBoxByText(List<WebElement> checkboxes, String... texts) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectCheckBoxByValue(List<WebElement> checkboxes, String... values) {
		for (WebElement checkbox : checkboxes) {
			if (!checkbox.isSelected() && ArrayUtils.contains(values, checkbox.getAttribute("value"))) {
				checkbox.click();
			} else if (checkbox.isSelected()) {
				checkbox.click();
			}
		}
	}

	@Override
	public List<WebElement> getSelectedCheckBoxes(List<WebElement> checkboxes) {
		List<WebElement> selected = new ArrayList<WebElement>();
		for (WebElement checkbox : checkboxes) {
			if (checkbox.isSelected()) {
				selected.add(checkbox);
			}
		}
		return selected;
	}

	@Override
	public WebElement findWhereElementForRemoveOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebElement findWhereElementForSelectOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebElement findWhatElementForRemoveOperation(WebElement wrapperElement, String parentName,
			String parentDefinition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebElement findWhatElementForSelectOperation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WebElement findAssociationWrapper(String associationName) throws FatwireDriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(WebElement element, String value) {
		executeJavascript("arguments[0].value=arguments[1]", element, value);
	}

	@Override
	public String getValue(WebElement element) {
		return executeJavascript("return arguments[0].value;", element).toString();
	}

	@Override
	public void unlinkUnnamedAssociationWithCurrentAsset(WebElement where, String associatedAssetType,
			String associatedAssetId) {
		Select select = new Select(where);
		String optionValue = String.format("%s,%s", associatedAssetType, associatedAssetId);
		select.selectByValue(optionValue);
		executeJavascript("RemoveTreeSelection(arguments[0]);", where);
	}

	@Override
	public void linkUnnamedAssociationWithCurrentAsset(WebElement where, String associatedAssetType,
			String associatedAssetId, String associatedAssetName) throws FatwireDriverException {
		String optionTitle = String.format("%s (%s)", associatedAssetName, associatedAssetType);
		String optionValue = String.format("%s,%s", associatedAssetType, associatedAssetId);
		Select select = new Select(where);
		for (WebElement option : select.getOptions()) {
			if (StringUtils.equals(option.getAttribute("value"), optionValue)) {
				throw new FatwireDriverException(String.format("%s is already in list", optionTitle));
			}
		}
		executeJavascript(
				"var optionSize = arguments[0].options.length; arguments[0].options[optionSize] = new Option(arguments[1], arguments[2])",
				where, optionTitle, optionValue);

	}

	private String encodeString(String s) {
		return String.format("%x", new BigInteger(s.getBytes()));
	}

	public void findAllAssetTypes() throws FatwireDriverException {
		assetTypes.clear();
		browseTo(LIST_ALL_ASSETTYPE_URL);
		List<WebElement> inspectLinks = driver.findElements(By.xpath("//a[img[@alt='Inspect This Asset Type']]"));
		List<String> assetTypeHREF = new ArrayList<String>();
		for (WebElement link : inspectLinks) {
			assetTypeHREF.add(link.getAttribute("href"));
		}

		for (String href : assetTypeHREF) {
			extractAssetTypeByURI(href);
		}

	}

	@Override
	public AssetType extractAssetTypeByURI(String href) throws FatwireDriverException {
		List<WebElement> inspectLinks;
		browseTo(href);
		String name = findCurrentAssetName();
		String description = findCurrentAssetDescription();
		String id = findCurrentAssetID();
		String type = findRightCellByLeftTitle("Type").getText();
		List<Association> associations = new ArrayList<Association>();
		try {
			new Select(driver.findElement(By.name("AssetFunctions"))).selectByVisibleText("Associations");
			Thread.sleep(5000);
			inspectLinks = driver.findElements(By.xpath("//a[img[@alt='Inspect This Association']]"));
			logger.info("found "+ inspectLinks.size() +" associations for "+ name);
			List<String> associationHREF = new ArrayList<String>();
			for (WebElement link : inspectLinks) {
				associationHREF.add(link.getAttribute("href"));
			}

			for (String href2 : associationHREF) {
				browseTo(href2);
				String assocName = findCurrentAssetName();
				String assocDescription = findCurrentAssetDescription();
				String child = findRightCellByLeftTitle("Child AssetType").getText();
				associations.add(new Association(assocName, assocDescription, child));
			}

		} catch (Exception ex) {
		  ex.printStackTrace();
		}

		AssetType assetType = new AssetType(id, name, description, AssetTypeEnum.fromTypeName(type), associations);
		/*if (AssetTypeEnum.Child.equals(assetType.getType())) {
			WebElement flexFamily = findRightCellByLeftTitle("Flex Family");
			String flexAttributeURL = findRightCellByLeftTitle(flexFamily, "Flex Attribute").findElement(
					By.tagName("a")).getAttribute("href");
			String parentDefinitionURL = findRightCellByLeftTitle(flexFamily, "Flex Parent Definition").findElement(
					By.tagName("a")).getAttribute("href");
			String flexDefinitionURL = findRightCellByLeftTitle(flexFamily, "Flex Definition").findElement(
					By.tagName("a")).getAttribute("href");
			String flexParentURL = findRightCellByLeftTitle(flexFamily, "Flex Parent").findElement(By.tagName("a"))
					.getAttribute("href");
			String flexFilterURL = findRightCellByLeftTitle(flexFamily, "Flex Filter").findElement(By.tagName("a"))
					.getAttribute("href");
			assetType.setFlexAttribute(extractAssetTypeByURI(flexAttributeURL));
			assetType.setParentDefinition(extractAssetTypeByURI(parentDefinitionURL));
			assetType.setFlexDefinition(extractAssetTypeByURI(flexDefinitionURL));
			assetType.setFlexParent(extractAssetTypeByURI(flexParentURL));
			assetType.setFlexFilter(extractAssetTypeByURI(flexFilterURL));
		}*/
		assetTypes.put(assetType.getAssetType(), assetType);
		return assetType;
	}

	@Override
	public Association findAssociationByDesc(String assetTypeDescription, String assocDesc)
			throws FatwireDriverException {
		AssetType assetType = findAssetTypeByDesc(assetTypeDescription);
		if (assetType == null) {
			assetType = extractAssetTypeByDescription(assetTypeDescription);
			if (assetType == null) {
				throw new FatwireDriverException("Can't find asset type called " + assetTypeDescription);
			}
		}
		return findAssociationByDesc(assetType, assocDesc);
	}

	@Override
	public AssetType extractAssetTypeByDescription(String assetTypeDescription) throws FatwireDriverException {
		browseTo(LIST_ALL_ASSETTYPE_URL);
		List<WebElement> inspectLinks = driver.findElements(By.xpath("//a[img[@alt='Inspect This Asset Type']]"));
		for (WebElement inspectLink : inspectLinks) {
			WebElement descriptionElement = inspectLink.findElement(By.xpath("ancestor::td/following-sibling::td[4]"));
			if (assetTypeDescription.equals(descriptionElement.getText())) {
				return extractAssetTypeByURI(inspectLink.getAttribute("href"));
			}
		}
		return null;
	}

	@Override
	public Association findAssociationByDesc(AssetType assetType, String assocDesc) throws FatwireDriverException {
		for (Association association : assetType.getAssociations()) {
			if (StringUtils.equalsIgnoreCase(association.getDescription(), assocDesc)) {
				return association;
			}
		}
		throw new FatwireDriverException("Can't find association called " + assocDesc + " for "
				+ assetType.getAssetType());
	}

	@Override
	public void browseToPlacePage(String... ids) {
		String n0Param = "";
		String p0Param = "";
		for (String id : ids) {
			p0Param = n0Param;
			n0Param += String.format("%%26id%%253d%s%%252cassettype%%253dPage", id);
		}
		StringBuffer buf = new StringBuffer("/ContentServer?").append("n0_=ROOT%26adhoc%253dPlacedPages")
				.append(n0Param).append("%26&AssetType=Page").append("&p0_=ROOT%26adhoc%253dPlacedPages")
				.append(p0Param).append("%26&pagename=OpenMarket%2FGator%2FUIFramework%2FTreeOpURL&op=Place+Page");
		logger.info(buf.toString());
		browseTo(buf.toString());
	}

	public AssetType findAssetTypeByDesc(String assetTypeDesc) throws FatwireDriverException {
		for (AssetType type : assetTypes.values()) {
			if (StringUtils.equalsIgnoreCase(type.getDescription(), assetTypeDesc)) {
				return type;
			}
		}
		throw new FatwireDriverException("Missing asset type description: " + assetTypeDesc);
	}

	@Override
	public String[] getCurrentNamedAssociation(String assetType, Association association) {
		WebElement descSibling = null;
		try {
			descSibling = driver.findElement(By.xpath(String.format("//tr[td[text()='%s']]/following-sibling::tr/td",
					association.getDescription())));

		} catch (Exception ex) {
			logger.warn(ex);
		}
		if (descSibling != null) {
			List<WebElement> inputs = descSibling.findElements(By.tagName("input"));
			for (WebElement input : inputs) {
				if (StringUtils.equalsIgnoreCase(input.getAttribute("type"), "text")) {
					String[] associatedAsset = new String[] {
							getValue(driver.findElement(By.name(String.format("%s:Association-named:%s", assetType,
									association.getName())))), getValue(input) };
					return StringUtils.isBlank(associatedAsset[0]) ? null : associatedAsset;
				}
			}
		}
		return null;
	}

	@Override
	public String[][] getCurrentUnnamedAssociations(WebElement container) {
		Select select = new Select(container);
		List<WebElement> options = select.getOptions();
		String[][] namedAssociations = new String[options.size()][];
		int index = 0;
		for (WebElement option : options) {
			String[] namedAssociation = new String[3];
			String s = option.getAttribute("value");
			namedAssociation[0] = StringUtils.right(s, 13);// id
			namedAssociation[1] = StringUtils.substring(s, 0, s.length() - 14).trim();// type
			s = option.getText();
			namedAssociation[2] = s.substring(0,
					s.length() - 3 - assetTypes.get(namedAssociation[1]).getDescription().length()).trim();// name

			namedAssociations[index++] = namedAssociation;
		}
		return namedAssociations;
	}

	@Override
	public void inspectAsset(String assetTypeDesc, String assetID) throws FatwireDriverException {
		String url = StringUtils.replaceOnce(INSPECT_URL, REPLACE_ASSETID, assetID);
		AssetType assetType = findAssetTypeByDesc(assetTypeDesc);
		url = StringUtils.replaceOnce(url, REPLACE_ASSETTYPE, assetType.getAssetType());
		browseTo(url);
	}

	@Override
	public WebElement findNameInput(String assetTypeDesc) throws FatwireDriverException {
		AssetType assetType = findAssetTypeByDesc(assetTypeDesc);
		if (AssetTypeEnum.Simple.equals(assetType.type)) {
			return driver.findElement(By.name(StringUtils.join(new String[] { assetType.assetType, ":", "name" })));
		} else if (AssetTypeEnum.Parent.equals(assetType.type)) {
			return driver.findElement(By.name("flexgroups:name"));
		} else {
			return driver.findElement(By.name("flexassets:name"));
		}

	}

	@Override
	public WebElement findDescInput(String assetTypeDesc) throws FatwireDriverException {
		AssetType assetType = findAssetTypeByDesc(assetTypeDesc);
		if (AssetTypeEnum.Simple.equals(assetType.type)) {
			return driver.findElement(By.name(StringUtils.join(new String[] { assetType.getAssetType(), ":",
					"description" })));// "descriptionvis"
		} else if (AssetTypeEnum.Parent.equals(assetType.type)) {
			return driver.findElement(By.name("flexgroups:description"));
		} else {
			return driver.findElement(By.name("flexassets:description"));
		}
	}

	@Override
	public void placePageToCurrentPage(String pageId, String destPageRank) {
		setValue(driver.findElement(By.name("Rank-" + pageId)), destPageRank);
	}

	@Override
	public void unplacePage(String[] childPageIds) {
		String[] parentPageIds = (String[]) ArrayUtils.subarray(childPageIds, 0, childPageIds.length - 1);
		browseToPlacePage(parentPageIds);
		WebElement removeCheckbox = driver.findElement(By.name("Remove-" + childPageIds[childPageIds.length - 1]));
		removeCheckbox.click();
		executeJavascript("return CheckFields()");
	}

}
