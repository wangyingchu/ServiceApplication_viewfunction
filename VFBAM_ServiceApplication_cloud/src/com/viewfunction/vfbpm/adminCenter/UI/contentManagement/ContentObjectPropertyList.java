package com.viewfunction.vfbpm.adminCenter.UI.contentManagement;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.PropertyType;
import javax.jcr.Binary;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentReposityConstant;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil.DeleteContentObjectPropertyResult;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.util.CommonStyleUtil;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;

public class ContentObjectPropertyList extends VerticalLayout{
	private static final long serialVersionUID = 8260696739963872505L;
	
	Panel containerPanel;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd'-'HH:mm:ss z");
	UserClientInfo userClientInfo;
	private ContentManagementPanel contentManagementPanel;
	private static DialogWindow deletePropertyConfirmWindow;
	
	public ContentObjectPropertyList(UserClientInfo userClientInfo){		
		containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		this.userClientInfo=userClientInfo;
		VerticalLayout containPanelLayot=((VerticalLayout)containerPanel.getContent());		
		containPanelLayot.setMargin(false);
		containPanelLayot.setSpacing(false);		
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		HorizontalLayout objectPropertyContainerInitLayout=new HorizontalLayout();
		objectPropertyContainerInitLayout.setWidth("100%");
		objectPropertyContainerInitLayout.setHeight("22px");
		objectPropertyContainerInitLayout.setStyleName("ui_contentManagementPropertyList_2");
		containerPanel.addComponent(objectPropertyContainerInitLayout);
		this.addComponent(containerPanel);	
	}
	
	public void setContentManagementPanel(ContentManagementPanel contentManagementPanel) {
		this.contentManagementPanel = contentManagementPanel;
	}
	
	public void renderPropertyItemList(List<ContentObjectProperty> propertyList){
		containerPanel.removeAllComponents();
		boolean oddFlag=true;
		for(int i=0;i<propertyList.size();i++){
			ContentObjectProperty cop=propertyList.get(i);
			 if (!cop.getPropertyName().equals(JCRContentReposityConstant.SPACE_ROOT_CONTENT_OBJECT_ID)
                     && !cop.getPropertyName().equals(JCRContentReposityConstant.SPACE_ROOT_CONTENT_OBJECT_ID)) {
				containerPanel.addComponent(addPropertyItemLayout(cop,oddFlag));
				oddFlag=!oddFlag;	
             }					
		}
		
		HorizontalLayout objectPropertyContainerInitLayout=new HorizontalLayout();
		objectPropertyContainerInitLayout.setWidth("100%");
		objectPropertyContainerInitLayout.setHeight("22px");
		objectPropertyContainerInitLayout.setStyleName("ui_contentManagementPropertyList_2");		
		containerPanel.addComponent(objectPropertyContainerInitLayout);	
	}
	
	private HorizontalLayout addPropertyItemLayout(ContentObjectProperty cop,boolean oddFlag){
		HorizontalLayout objectPropertyContainerLayout_1=new HorizontalLayout();
		objectPropertyContainerLayout_1.setWidth("100%");
		objectPropertyContainerLayout_1.setHeight("22px");
		if(oddFlag){
			objectPropertyContainerLayout_1.setStyleName("ui_contentManagementPropertyList");
		}else{
			objectPropertyContainerLayout_1.setStyleName("ui_contentManagementPropertyList_2");
			
		}
		HorizontalLayout propertyInfoContainer=new HorizontalLayout();
		
		Embedded typeIcon=null;
		Label propertValue=null;
		if(cop.getPropertyType()==PropertyType.STRING){			
			if(!cop.isMultiple()){
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_stringTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_string"));
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+cop.getPropertyValue()+"</span>" ,Label.CONTENT_XHTML);				
			}else{
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_stringArrayTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_stringArray"));				
				String[] valueArray=(String[])cop.getPropertyValue();
				StringBuffer valueString=new StringBuffer();
				if(valueArray.length>0){
					valueString.append(valueArray[0]);				
					for(int i=1;i<valueArray.length;i++){
						valueString.append("<span style='color:#222222'>;</span>");
						valueString.append(valueArray[i]);			
					}
				}				
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+valueString.toString()+"</span>" ,Label.CONTENT_XHTML);
			}			
		}else if(cop.getPropertyType()==PropertyType.BINARY){			
			if(!cop.isMultiple()){
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_binaryTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_binary"));	
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+((Binary)cop.getPropertyValue()).toString()+"</span>" ,Label.CONTENT_XHTML);
			}else{
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_binaryArrayTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_binaryArray"));
				Binary[] binaryArray=(Binary[])cop.getPropertyValue();
				StringBuffer valueString=new StringBuffer();
				valueString.append(binaryArray[0].toString());
				for(int i=1;i<binaryArray.length;i++){
					valueString.append("<span style='color:#222222'>;</span>");
					valueString.append(binaryArray[i].toString());			
				}
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+valueString.toString()+"</span>" ,Label.CONTENT_XHTML);
			}
		}else if(cop.getPropertyType()==PropertyType.BOOLEAN){
			if(!cop.isMultiple()){
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_booleanTypeContentIcon);
				typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_boolean"));
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+cop.getPropertyValue()+"</span>" ,Label.CONTENT_XHTML);
			}else{
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_booleanArrayTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_booleanArray"));				
				boolean[] valueArray=(boolean[])cop.getPropertyValue();
				StringBuffer valueString=new StringBuffer();
				valueString.append(valueArray[0]);				
				for(int i=1;i<valueArray.length;i++){
					valueString.append("<span style='color:#222222'>;</span>");
					valueString.append(valueArray[i]);			
				}
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+valueString.toString()+"</span>" ,Label.CONTENT_XHTML);
			}			
		}else if(cop.getPropertyType()==PropertyType.DATE){
			if(!cop.isMultiple()){			
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_dateTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_date"));
				Calendar cal=(Calendar)cop.getPropertyValue();
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+formatter.format(cal.getTime())+"</span>" ,Label.CONTENT_XHTML);
			}else{
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_dateArrayTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_dateArray"));
				Calendar[] valueArray=(Calendar[])cop.getPropertyValue();
				StringBuffer valueString=new StringBuffer();
				valueString.append(formatter.format(valueArray[0].getTime()));				
				for(int i=1;i<valueArray.length;i++){
					valueString.append("<span style='color:#222222'>;</span>");
					valueString.append(formatter.format(valueArray[i].getTime()));			
				}
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+valueString.toString()+"</span>" ,Label.CONTENT_XHTML);
			}
		}else if(cop.getPropertyType()==PropertyType.DECIMAL){
			if(!cop.isMultiple()){	
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_decimalTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_decimale"));
				BigDecimal bigDecimal=(BigDecimal)cop.getPropertyValue();
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+bigDecimal.toPlainString()+"</span>" ,Label.CONTENT_XHTML);
			}else{
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_decimalArrayTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_decimalArray"));
				BigDecimal[] valueArray=(BigDecimal[])cop.getPropertyValue();
				StringBuffer valueString=new StringBuffer();
				valueString.append(valueArray[0].toPlainString());				
				for(int i=1;i<valueArray.length;i++){
					valueString.append("<span style='color:#222222'>;</span>");
					valueString.append(valueArray[i]);			
				}
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+valueString.toString()+"</span>" ,Label.CONTENT_XHTML);
			}
		}else if(cop.getPropertyType()==PropertyType.DOUBLE){
			if(!cop.isMultiple()){	
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_doubleTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_double"));
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+cop.getPropertyValue()+"</span>" ,Label.CONTENT_XHTML);
			}else{
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_doubleArrayTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_doubleArray"));				
				double[] valueArray=(double[])cop.getPropertyValue();
				StringBuffer valueString=new StringBuffer();
				valueString.append(valueArray[0]);				
				for(int i=1;i<valueArray.length;i++){
					valueString.append("<span style='color:#222222'>;</span>");
					valueString.append(valueArray[i]);			
				}
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+valueString.toString()+"</span>" ,Label.CONTENT_XHTML);
			}
		}else if(cop.getPropertyType()==PropertyType.LONG){
			if(!cop.isMultiple()){
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_longTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_long"));
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+cop.getPropertyValue()+"</span>" ,Label.CONTENT_XHTML);
			}else{
				typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_longArrayTypeContentIcon);
				typeIcon.setDescription(
						userClientInfo.getI18NProperties().getProperty("contentManage_objectType_longArray"));				
				long[] valueArray=(long[])cop.getPropertyValue();
				StringBuffer valueString=new StringBuffer();
				valueString.append(valueArray[0]);				
				for(int i=1;i<valueArray.length;i++){
					valueString.append("<span style='color:#222222'>;</span>");
					valueString.append(valueArray[i]);			
				}
				propertValue=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+valueString.toString()+"</span>" ,Label.CONTENT_XHTML);
			}
		}	
		propertyInfoContainer.addComponent(typeIcon);		
		
		Label propertyName=new Label("<span style='margin-left: 10px;margin-right: 15px;color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;'>"+cop.getPropertyName()+"</span>" ,Label.CONTENT_XHTML);		
		propertyInfoContainer.addComponent(propertyName);		
		propertyInfoContainer.addComponent(propertValue);
		objectPropertyContainerLayout_1.addComponent(propertyInfoContainer);
		objectPropertyContainerLayout_1.setComponentAlignment(propertyInfoContainer, Alignment.MIDDLE_LEFT);		
		HorizontalLayout propertyActionContainer=new HorizontalLayout();
		propertyActionContainer.setWidth("60px");
		
		
		final String propertyKey=cop.getPropertyName();
		
		Button editPropertyButton=new Button();		
		editPropertyButton.setCaption(null);
		editPropertyButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
		editPropertyButton.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_button_editPropertyDesc"));
		editPropertyButton.addListener(new ClickListener(){				
			private static final long serialVersionUID = 7869028811395071793L;

			public void buttonClick(ClickEvent event) {				
				
			}});
		propertyActionContainer.addComponent(editPropertyButton);
		
		Button deletePropertyButton=new Button();		
		deletePropertyButton.setCaption(null);
		deletePropertyButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deletePropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
		deletePropertyButton.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_button_deletePropertyDesc"));
		deletePropertyButton.addListener(new ClickListener(){				
			private static final long serialVersionUID = 6882124130840225720L;

			public void buttonClick(ClickEvent event) {	
				launchDeletePropertyConfirmWindow(propertyKey);
			}});
		propertyActionContainer.addComponent(deletePropertyButton);
		
		objectPropertyContainerLayout_1.addComponent(propertyActionContainer);
		objectPropertyContainerLayout_1.setComponentAlignment(propertyActionContainer, Alignment.MIDDLE_RIGHT);	
		return objectPropertyContainerLayout_1;		
	}
	
	private void launchDeletePropertyConfirmWindow(String propertyName){
		String currentContentSpace=contentManagementPanel.contentObjectDetail.currentContentSpace;
		String currentContentObjectABSPath=contentManagementPanel.contentObjectDetail.currentContentObjectABSPath;
		String windowTitle = userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteProperty_win_title");
		String windowDesc = userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteProperty_win_desc")+ " " + CommonStyleUtil.formatCurrentItemStyle(propertyName);
		ContentSpace cs = null;	     
	    try {
	    	cs = ContentComponentFactory.connectContentSpace(currentContentSpace);	     
	    	BaseContentObject currentObject=cs.getContentObjectByAbsPath(currentContentObjectABSPath);
	    	String currentContentObjectName=currentObject.getContentObjectName();
	    	windowDesc = userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteProperty_win_desc")+ " " 
	    			+ CommonStyleUtil.formatCurrentItemStyle(propertyName) +" "
	    			+ userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteProperty_win_desc_2")+" " + CommonStyleUtil.formatParentItemStyle(currentContentObjectName); 
	    }catch (ContentReposityException e) {
	            e.printStackTrace();
	     } finally {
	    	 if (cs != null) {
	    		 cs.closeContentSpace();
	         }
	     }		   
		Application application=contentManagementPanel.getApplication();		
		if (deletePropertyConfirmWindow != null) {
			application.getMainWindow().removeWindow(deletePropertyConfirmWindow);
			deletePropertyConfirmWindow=null;	            
		}
		deletePropertyConfirmWindow=UIComponentCreator.createDialogWindow_DeleteData_Confirm(windowTitle, windowDesc, buildDeletePropertyWindowControlButtons(propertyName));
		deletePropertyConfirmWindow.center();		
		application.getMainWindow().addWindow(deletePropertyConfirmWindow);	   
	}
	
	private VerticalLayout buildDeletePropertyWindowControlButtons(final String propertyName){
		final Application application=contentManagementPanel.getApplication();
		final String currentContentSpace=contentManagementPanel.contentObjectDetail.currentContentSpace;
		final String currentContentObjectABSPath=contentManagementPanel.contentObjectDetail.currentContentObjectABSPath;
		final Object currentDataObjectItemID=contentManagementPanel.contentObjectDetail.currentDataObjectItemID;
		
		HorizontalLayout deleteResultLayout = new HorizontalLayout();
		deleteResultLayout.setStyleName(UICommonElementDefination.DIALOG_ITEM_DESCRIPTION_LABLE_STYLE);
        deleteResultLayout.setWidth("450px");
        deleteResultLayout.setHeight("20px");
		final Label messageLable = new Label("", Label.CONTENT_XHTML);
		deleteResultLayout.addComponent(messageLable);
      
         
		Button confirmDeleteButton = new Button(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_button_confirmDelLabel"));
		confirmDeleteButton.setDisableOnClick(true);
		confirmDeleteButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = -6521460962885499326L;

			public void buttonClick(ClickEvent event) {
				String resultMesssage=null;
				messageLable.setValue("<b style='color:#333333;'>"+userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteingProMessage")+"</b>");
				DeleteContentObjectPropertyResult result=ContentRepositoryDataUtil.deleteContentObjectProperty(currentContentSpace, currentContentObjectABSPath, propertyName);
                switch(result){
                	case DeletePropertySuccessful: 
                		resultMesssage="<b style='color:#333333;'>"+userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deleteProSucssMessage")+"</b>";
                		messageLable.setValue(resultMesssage);
                		contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(currentDataObjectItemID);	
                		break;
                	case PropertyAlreadyDeleted: 
                		resultMesssage="<b style='color:#333333;'>"+userClientInfo.getI18NProperties().getProperty("contentManage_dialog_deletProDupMessage")+"</b>";
                		messageLable.setValue(resultMesssage);
                		contentManagementPanel.contentObjectDetail.loadContentObjectDetailInfo(currentDataObjectItemID);
                		break;
                	case GetRepositoryErrorDuringOperation: 
                		resultMesssage="<b style='color:#333333;'>"+
                				userClientInfo.getI18NProperties().getProperty("contentManage_dialog_RepErrorMessage")+"</b>";
                		messageLable.setValue(resultMesssage);
                		break;
                }
            }
        });		
		
		VerticalLayout windowContentLayout = new VerticalLayout();
		windowContentLayout.addComponent(deleteResultLayout);
		Button cancelDeleteButton = new Button(userClientInfo.getI18NProperties().getProperty("contentManage_dialog_button_cancelDeleteLabel"));
		cancelDeleteButton.setStyleName(BaseTheme.BUTTON_LINK);		
		cancelDeleteButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = -6521460962885499325L;

			public void buttonClick(ClickEvent event) {	               
                application.getMainWindow().removeWindow(deletePropertyConfirmWindow);
                deletePropertyConfirmWindow=null;                
            }
        });			
	    List<Button> buttonList = new ArrayList<Button>();
	    buttonList.add(confirmDeleteButton);
	    buttonList.add(cancelDeleteButton);
	    windowContentLayout.addComponent(new BaseButtonBar(300, 45, Alignment.MIDDLE_RIGHT, buttonList));
	    return windowContentLayout;
	}
}