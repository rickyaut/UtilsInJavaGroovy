package com.rickyaut.tools.fatwire;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface FatwireDriver extends WebDriver {

	void login(String appContextURL, String userName, String password, String siteName);

	void ensureFindChildNodeElementExists() throws FatwireDriverException;

	void changeSite(String newSiteName);

	void selectSite(String newSiteName);

	void browseTo(String url);

	void listChildAssets(String assetType, String assetID);

	void inspectAsset(String assetTypeDesc, String assetID) throws FatwireDriverException;

	boolean isInspectingAsset();

	void browseToWorkflow(String workflowName) throws FatwireDriverException;

	void publish(String dest);

	void searchAssetByAttribute(String assetType, NameValuePair... query)
			throws FatwireDriverException;

	void searchAssetByName(String assetType, String keyword);

	void searchAssetByID(String assetType, String assetID) throws FatwireDriverException;

	boolean hasMoreSearchResult();

	boolean scrollToNextSearchResult();

	void newAsset(String assetType, AssetTypeEnum flexType);

	void editCurrentAsset();

	void deleteCurrentAsset();

	void copyCurrentAsset(String... assignee);

	void clickOnContinue();

	void submitForm(String formName);

	void checkFieldsAndSubmit();

	void sendCurrentAssetToWorkFlow(String workflowName, String... assignee);

	void removeCurrentAssetFromWorkflow(String workflowName);

	void assignCurrentAsset(String... assignee);

	WebElement findWhereElementForRemoveOperation();

	WebElement findWhereElementForSelectOperation();

	WebElement findWhatElementForRemoveOperation(WebElement wrapperElement, String parentName, String parentDefinition);

	WebElement findWhatElementForSelectOperation();

	WebElement findAssociationWrapper(String associationName) throws FatwireDriverException;

	boolean hasLinkedNamedAttribute(String attrName);

	void unlinkNamedAttributeFromCurrentAsset(String attrName);

	void linkNamedAttributeWithCurrentAsset(String attrName, String associatedAssetName, String associatedAssetID,
			String associatedAssetType);

	boolean hasLinkedNamedAssociation();

	void unlinkNamedAssociationFromCurrentAsset(String currentAssetType, String associationDescription)
			throws FatwireDriverException;

	void linkNamedAssociationWithCurrentAsset(String currentAssetType, String associationDescription,
			String associatedAssetID, String associatedAssetName) throws FatwireDriverException;

	void unlinkUnnamedAssociationWithCurrentAsset(WebElement where, String associatedAssetType, String associatedAssetId);

	void linkUnnamedAssociationWithCurrentAsset(WebElement where, String associatedAssetType, String associatedAssetId,
			String associatedAssetName) throws FatwireDriverException;

	Object executeJavascript(String script, Object... arguments);

	void shareCurrentAssetWithAnotherSite(boolean toBeShared, String... siteName);

	WebElement findNameInput(String assetType, AssetTypeEnum flexType);

	WebElement findDescInput(AssetTypeEnum flexType);

	WebElement findHiddenTemplateId(AssetTypeEnum flexType);

	WebElement findRightCellByLeftTitle(String title) throws FatwireDriverException;

	String findCurrentAssetID() throws FatwireDriverException;

	String findCurrentAssetName() throws FatwireDriverException;

	String findCurrentAssetDescription() throws FatwireDriverException;

	void back(int steps);

	void selectRadioByValue(List<WebElement> radios, String value, boolean ignoreCase);

	WebElement getCheckedRadio(List<WebElement> radios);

	boolean selectOptionByText(WebElement dropdown, boolean ignoreCase, String... texts);

	boolean selectOptionByValue(WebElement dropdown, boolean ignoreCase, String... values);

	List<WebElement> getSelectedOptions(WebElement dropdown);

	void selectCheckBoxByText(List<WebElement> checkboxes, String... texts);

	void selectCheckBoxByValue(List<WebElement> checkboxes, String... texts);

	List<WebElement> getSelectedCheckBoxes(List<WebElement> checkboxes);

	void setValue(WebElement element, String value);

	String getValue(WebElement element);

	void findAllAssetTypes() throws FatwireDriverException;

	Association findAssociationByDesc(String assetTypeDescription, String assocDesc) throws FatwireDriverException;

	Association findAssociationByDesc(AssetType assetType, String assocDesc) throws FatwireDriverException;

	AssetType findAssetTypeByDesc(String assetTypeDescription) throws FatwireDriverException;

	AssetType extractAssetTypeByDescription(String assetTypeDescription) throws FatwireDriverException;

	AssetType extractAssetTypeByURI(String href) throws FatwireDriverException;

	void browseToPlacePage(String... ids);

	String[] getCurrentNamedAssociation(String assetType, Association association);

	String[][] getCurrentUnnamedAssociations(WebElement container);

	WebElement findNameInput(String assetTypeDesc) throws FatwireDriverException;

	WebElement findDescInput(String assetTypeDesc) throws FatwireDriverException;

	void placePageToCurrentPage(String string, String destPageRank);

	void unplacePage(String[] childPageIds);

	WebElement findRightCellByLeftTitle(WebElement element, String title) throws FatwireDriverException;

}
