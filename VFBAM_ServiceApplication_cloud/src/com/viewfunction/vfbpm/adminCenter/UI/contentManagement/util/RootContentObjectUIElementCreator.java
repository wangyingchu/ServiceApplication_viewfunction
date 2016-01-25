package com.viewfunction.vfbpm.adminCenter.UI.contentManagement.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil.AddRootContentObjectResult;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil.DeleteContentObjectResult;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentSpaceDataProvider;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil;

public class RootContentObjectUIElementCreator implements Serializable{	
	private static final long serialVersionUID = 1794186959832897068L;
	
	private HierarchicalContainer contentSpaceDataContainer;
	private final UserClientInfo userClientInfo;
	private final Application application;
	
	private static DialogWindow addRootContentObjectWindow;
	private static DialogWindow deleteRootContentObjectWindow;
	
	public RootContentObjectUIElementCreator( HierarchicalContainer contentSpace_DataContainer,UserClientInfo userClientInfo,final Application application){
		this.contentSpaceDataContainer=contentSpace_DataContainer;
		this.userClientInfo=userClientInfo;
		this.application=application;
	}
	 
	 public DialogWindow createAddRootContentObjectFormWindow(Object actionItemID) {
		 Object actionItemName = contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	     String windowTitle = userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addRootContentObject_win_title");	   
	     String windowDesc = userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addRootContentObject_win_desc")
	    		 +" "+CommonStyleUtil.formatCurrentItemStyle(actionItemName.toString());	     
	     addRootContentObjectWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, createAddRootContentObjectForm(actionItemID)); 
	     return addRootContentObjectWindow;
	 }
	 
	 public DialogWindow createDeleteRootContentObjectConfirmWindow(Object targetItem) {
	        Object parentItemKeyID = this.contentSpaceDataContainer.getParent(targetItem);
	        Object parentItemName = this.contentSpaceDataContainer.getItem(parentItemKeyID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	        Object contentObjectName = this.contentSpaceDataContainer.getItem(targetItem).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	        String windowTitle = userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteRootContentObject_win_title");
	        String windowDesc = userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteRootContentObject_win_desc_1")
	        		+ " "+CommonStyleUtil.formatCurrentItemStyle(contentObjectName.toString()) +" "+ 
	        		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteRootContentObject_win_desc_2") +" "+ CommonStyleUtil.formatParentItemStyle(parentItemName.toString());
	        deleteRootContentObjectWindow=UIComponentCreator.createDialogWindow_DeleteData_Confirm(windowTitle, windowDesc, buildDeleteContentObjectWindowControlButtons(targetItem));
	        return deleteRootContentObjectWindow;
	    } 
	 
 	 private Layout createAddRootContentObjectForm(Object actionItemID) {
		 final VerticalLayout formContainer = new VerticalLayout();
	     final BaseForm newRootContentObjectForm = new BaseForm();
	     final Object parentItemId = actionItemID;
	     newRootContentObjectForm.setImmediate(true);
	     final TextField newRootContentObjectName = new TextField(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addRootContentObject_rcobnLabel"));
	     newRootContentObjectName.setInputPrompt(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addRootContentObject_rcobnProtmLabel"));
	     newRootContentObjectName.setWidth("250px");
	     newRootContentObjectName.setRequired(true);
	     newRootContentObjectName.setRequiredError(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addRootContentObject_rcobnErrorLabel"));
	     final AbstractValidator validator = new AbstractValidator(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addRootContentObject_rcobnExistLabel")) {
			private static final long serialVersionUID = 7489908886234137266L;

				public boolean isValid(Object value) {
	                boolean checkResult = !checkNewItemAlreadyExistInContentSpaceDataContainer(parentItemId, value.toString());
	                return checkResult;
	            }
	        };
	        newRootContentObjectName.addValidator(validator);
	        newRootContentObjectForm.addField("_RootContentObjectName", newRootContentObjectName);
	        final Button okButton = new Button(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_button_confirmAddLabel"));
	        final Button cancelAddbutton = new Button(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_button_cancelAddLabel"), new Button.ClickListener() {	  
				private static final long serialVersionUID = 1L;

				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {					
					application.getMainWindow().removeWindow(addRootContentObjectWindow);
					addRootContentObjectWindow=null;					
				}
	        });
	        cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	        okButton.addListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
	                newRootContentObjectForm.setValidationVisible(true);
	                boolean validateResult = newRootContentObjectForm.isValid();
	                Label messageLable = new Label("<b style='color:#333333;'>"+
	                		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addingRCOMessage")+"</b>", Label.CONTENT_XHTML);
	                messageLable.setHeight("24px");
	                if (validateResult) {
	                    newRootContentObjectForm.setValidationVisible(false);
	                    newRootContentObjectName.setEnabled(false);
	                    String contentSpaceName = contentSpaceDataContainer.getItem(parentItemId).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue().toString();
	                    String rootContentObjectName = newRootContentObjectName.getValue().toString();
	                    newRootContentObjectName.removeValidator(validator);
	                    newRootContentObjectForm.getLayout().removeComponent(newRootContentObjectName);
	                    newRootContentObjectForm.getLayout().addComponent(messageLable);
	                    okButton.setEnabled(false);
	                    cancelAddbutton.setCaption(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_button_closeWindowLabel"));
	                    AddRootContentObjectResult addrelu = ContentRepositoryDataUtil.addRootContentObject(contentSpaceName, rootContentObjectName);
	                    String contentspacepostFix;
	                    int rootContentObjectID;
	                    String currentRootContentObjectItemId;
	                    Item rootContentObjectItem;
	                    switch (addrelu) {
	                        case AddRootContentObjectSuccessful:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>"+
	                            		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addRCOSucssMessage")+"</b>", Label.CONTENT_XHTML));
	                            contentspacepostFix = parentItemId.toString().substring(ContentSpaceDataProvider.contentSpaceIdPerfix.length(), parentItemId.toString().length());
	                            rootContentObjectID = contentSpaceDataContainer.getChildren(parentItemId).size();
	                            currentRootContentObjectItemId = ContentSpaceDataProvider.rootContentObjectIdPerfix + rootContentObjectID + contentspacepostFix;
	                            rootContentObjectItem = contentSpaceDataContainer.addItem(currentRootContentObjectItemId);
	                            rootContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(rootContentObjectName);
	                            rootContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(contentSpaceName);
	                            contentSpaceDataContainer.setParent(currentRootContentObjectItemId, parentItemId);
	                            contentSpaceDataContainer.setChildrenAllowed(currentRootContentObjectItemId, false);
	                            break;
	                        case RootContentObjectAlreadyExists:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>"+
	                            		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_addRCODupMessage")+"</b>", Label.CONTENT_XHTML));
	                            contentspacepostFix = parentItemId.toString().substring(ContentSpaceDataProvider.contentSpaceIdPerfix.length(), parentItemId.toString().length());
	                            rootContentObjectID = contentSpaceDataContainer.getChildren(parentItemId).size();
	                            currentRootContentObjectItemId = ContentSpaceDataProvider.rootContentObjectIdPerfix + rootContentObjectID + contentspacepostFix;
	                            rootContentObjectItem = contentSpaceDataContainer.addItem(currentRootContentObjectItemId);
	                            rootContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(rootContentObjectName);
	                            rootContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(contentSpaceName);
	                            contentSpaceDataContainer.setParent(currentRootContentObjectItemId, parentItemId);
	                            long childrenNumber = ContentRepositoryDataUtil.getRootComponentChildrenNumber(contentSpaceName, rootContentObjectName);
	                            if (childrenNumber > 0) {
	                                contentSpaceDataContainer.setChildrenAllowed(currentRootContentObjectItemId, true);
	                            } else {
	                                contentSpaceDataContainer.setChildrenAllowed(currentRootContentObjectItemId, false);
	                            }
	                            break;
	                        case GetRepositoryErrorDuringOperation:
	                            messageLable.setValue(new Label("<b style='color:#333333;'>"+
	                            		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_RepErrorMessage")+"</b>", Label.CONTENT_XHTML));
	                            break;
	                    }
	                }
	            }
	        });

	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(okButton);
	        buttonList.add(cancelAddbutton);
	        BaseButtonBar addRootContentObjectButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, buttonList);
	        newRootContentObjectForm.getFooter().addComponent(addRootContentObjectButtonBar);
	        formContainer.addComponent(newRootContentObjectForm);
	        return formContainer;
 	 }
	 
	 private HorizontalLayout buildDeleteContentObjectWindowControlButtons(Object actionItemID) {
	        final Object currentItemID = actionItemID;
	        final Object parentItemID = this.contentSpaceDataContainer.getParent(actionItemID);
	        final Object parentItemName = this.contentSpaceDataContainer.getItem(parentItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	        final Object contentObjectName = this.contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
	        final Object contentSpaceName = this.contentSpaceDataContainer.getItem(actionItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).getValue();

	        Button confirmDeleteButton = new Button(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_button_confirmDelLabel"));
	        confirmDeleteButton.addListener(new Button.ClickListener() {	            
				private static final long serialVersionUID = 8726136010722796479L;

				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
	                HorizontalLayout deleteResultLayout = new HorizontalLayout();
	                deleteResultLayout.setStyleName(UICommonElementDefination.DIALOG_ITEM_DESCRIPTION_LABLE_STYLE);
	                deleteResultLayout.setWidth("450px");
	                deleteResultLayout.setHeight("40px");

	                Label messageLable = new Label("<b style='color:#333333;'>"+
	                		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteingRCOMessage")+"</b>", Label.CONTENT_XHTML);
	                Button closeButton = new Button(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_button_closeDialogLabel"));
	                closeButton.setStyleName(BaseTheme.BUTTON_LINK);
	                closeButton.addListener(new Button.ClickListener() {	                   
						private static final long serialVersionUID = 354709127929716419L;

						public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	                      
	                    	application.getMainWindow().removeWindow(deleteRootContentObjectWindow);
	    	            	deleteRootContentObjectWindow=null;
	                    }
	                });
	                deleteResultLayout.addComponent(messageLable);
	                deleteResultLayout.setComponentAlignment(messageLable, Alignment.MIDDLE_CENTER);
	                deleteResultLayout.addComponent(closeButton);
	                deleteResultLayout.setComponentAlignment(closeButton, Alignment.MIDDLE_CENTER);
	                deleteRootContentObjectWindow.refreshDialogWindowContent(deleteResultLayout, Alignment.MIDDLE_RIGHT);
	                DeleteContentObjectResult deleteResult = null;
	                boolean parentNodeIsContentSpace = false;
	                if (currentItemID.toString().contains(ContentSpaceDataProvider.subContentObjectIdPerfix)) {
	                    //delete content object
	                    String parentNodeABSPath = ContentRepositoryDataUtil.getContentObjectABSPathByItemId(contentSpaceDataContainer, parentItemID);
	                    deleteResult = ContentRepositoryDataUtil.deleteContentObject(contentSpaceName.toString(), parentNodeABSPath, contentObjectName.toString());
	                } else {
	                    //delete root content object
	                    parentNodeIsContentSpace = true;
	                    deleteResult = ContentRepositoryDataUtil.deleteRootContentObject(parentItemName.toString(), contentObjectName.toString());
	                }
	                switch (deleteResult) {
	                    case DeleteContentObjectSuccessful:
	                        messageLable.setValue(new Label("<b style='color:#333333;'>"+
	                        		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deletRCOSucssMessage")+"</b>", Label.CONTENT_XHTML));
	                        contentSpaceDataContainer.removeItem(currentItemID);
	                        if (!parentNodeIsContentSpace && contentSpaceDataContainer.getChildren(parentItemID).size() == 0) {
	                            contentSpaceDataContainer.setChildrenAllowed(parentItemID, false);
	                        }
	                        break;
	                    case ContentObjectAlreadyDeleted:
	                        messageLable.setValue(new Label("<b style='color:#333333;'>"+
	                        		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deletRCONotExistMessage")+"</b>", Label.CONTENT_XHTML));
	                        contentSpaceDataContainer.removeItem(currentItemID);

	                        if (!parentNodeIsContentSpace && contentSpaceDataContainer.getChildren(parentItemID).size() == 0) {
	                            contentSpaceDataContainer.setChildrenAllowed(parentItemID, false);
	                        }
	                        break;
	                    case GetRepositoryErrorDuringOperation:
	                        messageLable.setValue(new Label("<b style='color:#333333;'>"+
	                        		userClientInfo.getI18NProperties().getProperty("contentManage_dialog_RepErrorMessage")+"</b>", Label.CONTENT_XHTML));
	                        break;
	                }
	            }
	        });

	        Button cancelDeleteButton = new Button(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_button_cancelDeleteLabel"));
	        cancelDeleteButton.addListener(new Button.ClickListener() {	           
				private static final long serialVersionUID = -4204581909706979978L;

				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
	            	application.getMainWindow().removeWindow(deleteRootContentObjectWindow);
	            	deleteRootContentObjectWindow=null;
	            }
	        });
	        cancelDeleteButton.setStyleName(BaseTheme.BUTTON_LINK);
	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(confirmDeleteButton);
	        buttonList.add(cancelDeleteButton);
	        return new BaseButtonBar(300, 45, Alignment.MIDDLE_RIGHT, buttonList);
	    }
	 	 
	 private boolean checkNewItemAlreadyExistInContentSpaceDataContainer(Object parentNodeID, String newItemString) {
		 return ContentRepositoryDataUtil.checkNewItemAlreadyExistInContentSpaceDataContainer(this.contentSpaceDataContainer, parentNodeID, newItemString);		
	 }	
}