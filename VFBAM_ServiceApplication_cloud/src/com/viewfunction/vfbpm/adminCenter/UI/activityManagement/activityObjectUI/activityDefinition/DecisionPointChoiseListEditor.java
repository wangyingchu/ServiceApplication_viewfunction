package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class DecisionPointChoiseListEditor extends VerticalLayout{
	private static final long serialVersionUID = -4391123906925258186L;
	
	private BaseForm newDataFieldValueForm;
	private String inputFieldIdPerfix="inputFieldIdPerfix";
	private int inputFieldNumber=0;	
	private VerticalLayout additionalFieldContainer;
	private Map<String,TextField> additionalInputFieldMap;
	private UserClientInfo userClientInfo;
	
	public DecisionPointChoiseListEditor(final String[] orginalChoiseList,UserClientInfo userClientInfo){			
		additionalInputFieldMap=new HashMap<String,TextField>();
		newDataFieldValueForm=new BaseForm();
		additionalFieldContainer=new VerticalLayout();		
		newDataFieldValueForm.getLayout().addComponent(additionalFieldContainer);
		this.userClientInfo=userClientInfo;
		if(orginalChoiseList!=null&&orginalChoiseList.length>0){
			for(int i=0;i<orginalChoiseList.length;i++){				
				renderNewDataField(orginalChoiseList[i]);
			}
		}else{			
			renderNewDataField(null);
		}	
		
		Button addMoreValueButton = new Button("Add more option");
		addMoreValueButton.setStyleName(BaseTheme.BUTTON_LINK);
		addMoreValueButton.setCaption(null);
		addMoreValueButton.setIcon(UICommonElementDefination.AppPanel_newActionbarIcon);		
		addMoreValueButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DecisionPointChoiseListEditor_addOptionButton"));		
		addMoreValueButton.addListener(new ClickListener(){  
			private static final long serialVersionUID = -2624897096053606198L;

			public void buttonClick(ClickEvent event) {							
				renderNewDataField(null);
			}});	
		
		List<Button> buttonList = new ArrayList<Button>();		   
	    buttonList.add(addMoreValueButton);
	    BaseButtonBar startNewActivityButtonBar = new BaseButtonBar(450, 40, Alignment.MIDDLE_RIGHT, buttonList);
	    newDataFieldValueForm.getLayout().addComponent(startNewActivityButtonBar);		
		this.addComponent(newDataFieldValueForm);		
	}	
	
	private void renderNewDataField(String fieldValue){		
		HorizontalLayout newFieldLayout=renderDataInputLayout(inputFieldNumber,fieldValue);
		inputFieldNumber++;		
		additionalFieldContainer.addComponent(newFieldLayout);
		HorizontalLayout spaceDiv=new HorizontalLayout();
		spaceDiv.setHeight("5px");
		additionalFieldContainer.addComponent(spaceDiv);		
	}
	
	private HorizontalLayout renderDataInputLayout(int fieldIndex,String fieldValue){
		final String fieldMapKey=inputFieldIdPerfix+fieldIndex;		
		HorizontalLayout inputFieldContainer=new HorizontalLayout();	
		TextField stringFieldValue=new TextField();	
		//stringFieldValue.setImmediate(true);
		stringFieldValue.setRequired(true);	
		stringFieldValue.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DecisionPointChoiseListEditor_newOptionInputPrompt"));
		stringFieldValue.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DecisionPointChoiseListEditor_newOptionInputErrMsg"));
		stringFieldValue.setWidth("190px");	
		if(fieldValue!=null){
			stringFieldValue.setValue(fieldValue);
		}		
		inputFieldContainer.addComponent(stringFieldValue);		
		additionalInputFieldMap.put(fieldMapKey, stringFieldValue);
		Button deleteCurrentValueButton=new Button("Delete this value");		
		deleteCurrentValueButton.setCaption(null);
		deleteCurrentValueButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deleteCurrentValueButton.setStyleName(BaseTheme.BUTTON_LINK);
		deleteCurrentValueButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DecisionPointChoiseListEditor_deleteOptionButton"));		
		deleteCurrentValueButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = -5692551518501372689L;

			public void buttonClick(ClickEvent event) {	
				additionalInputFieldMap.remove(fieldMapKey);				
				additionalFieldContainer.removeComponent(event.getComponent().getParent());
			}});		
		deleteCurrentValueButton.setStyleName(BaseTheme.BUTTON_LINK);
		inputFieldContainer.addComponent(deleteCurrentValueButton);
		return inputFieldContainer;
	}
	
	public boolean validateDateField(){
		boolean validateResult=true;
		Collection<TextField> fieldCollection=additionalInputFieldMap.values();
		TextField[] fieldArray=new TextField[additionalInputFieldMap.size()];
		fieldCollection.toArray(fieldArray);
		if(fieldArray==null||fieldArray.length==0){
			//newDataFieldValueForm.setValidationVisible(true);
			//newDataFieldValueForm.setComponentError(new UserError("At least one field is requred."));			
			return true;
		}		
		for(TextField currentField:fieldArray){
			validateResult =validateResult &currentField.isValid();		    
			if(!validateResult){
				newDataFieldValueForm.setValidationVisible(true);
				newDataFieldValueForm.setComponentError(new UserError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_DecisionPointChoiseListEditor_optionInputFormErrMsg")));		    	
			}	
		}		
	    if(validateResult){  
	    	newDataFieldValueForm.setValidationVisible(false);
	      	newDataFieldValueForm.setComponentError(null);
	    }
		return validateResult;
	}
	
	public String[] getChoistList(){
		Collection<TextField> fieldCollection=additionalInputFieldMap.values();
		TextField[] fieldArray=new TextField[additionalInputFieldMap.size()];
		fieldCollection.toArray(fieldArray);		
		String[] choiseList=new String[fieldArray.length];
		for(int i=0;i<fieldArray.length;i++){
			choiseList[i]=fieldArray[i].getValue().toString();			
		}		
		return choiseList;
	}
}