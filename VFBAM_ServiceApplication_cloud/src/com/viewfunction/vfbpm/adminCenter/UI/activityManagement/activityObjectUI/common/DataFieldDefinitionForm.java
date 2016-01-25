package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.PropertyType;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class DataFieldDefinitionForm extends VerticalLayout{
	private static final long serialVersionUID = -6110064904373716459L;
	
	private String _DataFieldName_PROPERTY="_DataFieldName_PROPERTY";
	private String _DataFieldDisplayName_PROPERTY="_DataFieldDisplayName_PROPERTY";
	private String _DataFieldDescription_PROPERTY="_DataFieldDescription_PROPERTY";
	private String _DataFieldType_PROPERTY="_DataFieldType_PROPERTY";
	
	private BaseForm newDataFieldDefinitionForm;
	private TextField newDataFieldName;
	private TextField newDataFieldDisplayName;
	private TextArea newDataFieldDescription;
	private ComboBox newDataFieldType;
	private CheckBox isArrayField;
	private CheckBox isMandatoryField;
	private CheckBox isSystemField;
	private Map<String,DataFieldDefinition> dataFieldDefinitionMap;
	private ExposedDataFieldsEditor exposedDataFieldsEditor;
	private UserClientInfo userClientInfo;
	public DataFieldDefinitionForm(ExposedDataFieldsEditor exposedDataFieldsEditor,final Map<String,DataFieldDefinition> dataFieldDefinitionMap,UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;
		this.dataFieldDefinitionMap=dataFieldDefinitionMap;
		this.exposedDataFieldsEditor=exposedDataFieldsEditor;
		newDataFieldDefinitionForm = new BaseForm();
		newDataFieldDefinitionForm.setImmediate(true);		
		
		newDataFieldName = new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_nameInputLabel"));
		newDataFieldName.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_nameInputDescLabel"));
		newDataFieldName.setWidth("250px");
		newDataFieldName.setRequired(true);
		newDataFieldName.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_nameInputReqLabel"));		
        newDataFieldDefinitionForm.addField(_DataFieldName_PROPERTY, newDataFieldName);
        final String nameFieldErrLabel=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_nameInputErrLabel");
        AbstractValidator validator = new AbstractValidator("") {
			private static final long serialVersionUID = 9015081564240640184L;

			public boolean isValid(Object value) {
                if (dataFieldDefinitionMap.containsKey(value)) {
                    this.setErrorMessage(nameFieldErrLabel);
                    return false;
                }
                return true;
            }
        };
        newDataFieldName.addValidator(validator);
		
        newDataFieldDisplayName = new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_dispInputLabel"));
        newDataFieldDisplayName.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_dispInputDescLabel"));
        newDataFieldDisplayName.setWidth("250px");
        newDataFieldDisplayName.setRequired(true);
        newDataFieldDisplayName.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_dispInputReqLabel"));		
        newDataFieldDefinitionForm.addField(_DataFieldDisplayName_PROPERTY, newDataFieldDisplayName);       		
        
        newDataFieldDescription = new TextArea(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_descInputLabel"));
        newDataFieldDescription.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_descInputDescLabel"));
        newDataFieldDescription.setRows(2);
        newDataFieldDescription.setColumns(20);
        newDataFieldDescription.setRequired(true);
        newDataFieldDescription.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_descInputReqLabel"));		
        newDataFieldDefinitionForm.addField(_DataFieldDescription_PROPERTY, newDataFieldDescription);
        
        newDataFieldType= new ComboBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_typeInputLabel"));
        newDataFieldType.addItem("STRING");
        newDataFieldType.addItem("BINARY");
        newDataFieldType.addItem("BOOLEAN");
        newDataFieldType.addItem("DATE");
        newDataFieldType.addItem("DECIMAL");
        newDataFieldType.addItem("DOUBLE");
        newDataFieldType.addItem("LONG");
        newDataFieldType.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_typeInputDescLabel"));
        newDataFieldType.setWidth("250px");
        newDataFieldType.setRequired(true);
        newDataFieldType.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_typeInputReqLabel"));		
        newDataFieldDefinitionForm.addField(_DataFieldType_PROPERTY, newDataFieldType);       
        
        isArrayField=new CheckBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_arrayInputLabel"));         
        isMandatoryField=new CheckBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_mandatoryInputLabel"));         
        isSystemField=new CheckBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_systemInputLabel"));         
        
        HorizontalLayout checkBoxLayout=new HorizontalLayout();         
        checkBoxLayout.addComponent(isArrayField);
        checkBoxLayout.addComponent(isMandatoryField);
        checkBoxLayout.addComponent(isSystemField);		
        newDataFieldDefinitionForm.getLayout().addComponent(checkBoxLayout);
		
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_confirmAddButtonLabel"));		
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_cancelAddButtonLabel"));
		okButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = -4950638643441632577L;

			public void buttonClick(ClickEvent event) {	 
				saveNewDataField();
            }
        });	
		cancelAddbutton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5178190683979932145L;

			public void buttonClick(ClickEvent event) {	 
            	closeWindow();
            }
        });		
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);		 
		List<Button> buttonList = new ArrayList<Button>();
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar addRootContentObjectButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, buttonList);
	    newDataFieldDefinitionForm.getFooter().addComponent(addRootContentObjectButtonBar);
	    this.addComponent(newDataFieldDefinitionForm);
	}
	
	public DataFieldDefinitionForm(ExposedDataFieldsEditor exposedDataFieldsEditor,final Map<String,DataFieldDefinition> dataFieldDefinitionMap,DataFieldDefinition dataFieldDefinition,final PropertyItem parentPropertyItem,UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;
		this.dataFieldDefinitionMap=dataFieldDefinitionMap;
		this.exposedDataFieldsEditor=exposedDataFieldsEditor;
		newDataFieldDefinitionForm = new BaseForm();
		newDataFieldDefinitionForm.setImmediate(true);		
		
		newDataFieldName = new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_nameInputLabel"));
		newDataFieldName.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_nameInputDescLabel"));
		newDataFieldName.setWidth("250px");
		newDataFieldName.setRequired(true);
		newDataFieldName.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_nameInputReqLabel"));		
		newDataFieldName.setValue(dataFieldDefinition.getFieldName());
		newDataFieldName.setEnabled(false);		
        newDataFieldDefinitionForm.addField(_DataFieldName_PROPERTY, newDataFieldName);       
		
        newDataFieldDisplayName = new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_dispInputLabel"));
        newDataFieldDisplayName.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_dispInputDescLabel"));
        newDataFieldDisplayName.setWidth("250px");
        newDataFieldDisplayName.setRequired(true);
        newDataFieldDisplayName.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_dispInputReqLabel"));
        newDataFieldDisplayName.setValue(dataFieldDefinition.getDisplayName());
        newDataFieldDefinitionForm.addField(_DataFieldDisplayName_PROPERTY, newDataFieldDisplayName);       		
        
        newDataFieldDescription = new TextArea(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_descInputLabel"));
        newDataFieldDescription.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_descInputDescLabel"));
        newDataFieldDescription.setRows(2);
        newDataFieldDescription.setColumns(20);
        newDataFieldDescription.setRequired(true);
        newDataFieldDescription.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_descInputReqLabel"));	
        newDataFieldDescription.setValue(dataFieldDefinition.getDescription());
        newDataFieldDefinitionForm.addField(_DataFieldDescription_PROPERTY, newDataFieldDescription);
        
        newDataFieldType= new ComboBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_typeInputLabel"));
        newDataFieldType.addItem("STRING");
        newDataFieldType.addItem("BINARY");
        newDataFieldType.addItem("BOOLEAN");
        newDataFieldType.addItem("DATE");
        newDataFieldType.addItem("DECIMAL");
        newDataFieldType.addItem("DOUBLE");
        newDataFieldType.addItem("LONG");
        newDataFieldType.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_typeInputDescLabel"));
        newDataFieldType.setWidth("250px");
        newDataFieldType.setRequired(true);
        newDataFieldType.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_typeInputReqLabel"));        
        switch(dataFieldDefinition.getFieldType()){
	        case PropertyType.STRING: newDataFieldType.setValue("STRING");break;
	        case PropertyType.BINARY: newDataFieldType.setValue("BINARY");break;
	        case PropertyType.BOOLEAN: newDataFieldType.setValue("BOOLEAN");break;
	        case PropertyType.DATE: newDataFieldType.setValue("DATE");break;
	        case PropertyType.DECIMAL: newDataFieldType.setValue("DECIMAL");break;
	        case PropertyType.DOUBLE: newDataFieldType.setValue("DOUBLE");break;
	        case PropertyType.LONG: newDataFieldType.setValue("LONG");break;
        }               
        newDataFieldDefinitionForm.addField(_DataFieldType_PROPERTY, newDataFieldType);       
        
        isArrayField=new CheckBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_arrayInputLabel")); 
        isArrayField.setValue(new Boolean(dataFieldDefinition.isArrayField()));
        isMandatoryField=new CheckBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_mandatoryInputLabel"));
        isMandatoryField.setValue(new Boolean(dataFieldDefinition.isMandatoryField()));
        isSystemField=new CheckBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_systemInputLabel"));         
        isSystemField.setValue(new Boolean(dataFieldDefinition.isSystemField()));
        HorizontalLayout checkBoxLayout=new HorizontalLayout();         
        checkBoxLayout.addComponent(isArrayField);
        checkBoxLayout.addComponent(isMandatoryField);
        checkBoxLayout.addComponent(isSystemField);		
        newDataFieldDefinitionForm.getLayout().addComponent(checkBoxLayout);
		
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_confirmUpdateButtonLabel"));		
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_cancelUpdateButtonLabel"));
		okButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = -4950638643441632577L;

			public void buttonClick(ClickEvent event) {	 
				updateDataField(parentPropertyItem);
            }
        });	
		cancelAddbutton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5178190683979932145L;

			public void buttonClick(ClickEvent event) {	 
            	closeWindow();
            }
        });		
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);		 
		List<Button> buttonList = new ArrayList<Button>();
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar addRootContentObjectButtonBar = new BaseButtonBar(300, 45, Alignment.MIDDLE_RIGHT, buttonList);
	    newDataFieldDefinitionForm.getFooter().addComponent(addRootContentObjectButtonBar);
	    this.addComponent(newDataFieldDefinitionForm);
	}
	
	private void saveNewDataField(){		
		newDataFieldDefinitionForm.setValidationVisible(true);
        boolean validateResult = newDataFieldDefinitionForm.isValid();
		if(validateResult){
			boolean isArray=((Boolean)isArrayField.getValue()).booleanValue();
			boolean isMandatory=((Boolean)isMandatoryField.getValue()).booleanValue();
			boolean isSystem=((Boolean)isSystemField.getValue()).booleanValue();
			int dataFieldType=PropertyType.STRING;
			if(newDataFieldType.getValue().toString().equals("STRING")){
				dataFieldType=PropertyType.STRING;
			}else if(newDataFieldType.getValue().toString().equals("BINARY")){
				dataFieldType=PropertyType.BINARY;
			}else if(newDataFieldType.getValue().toString().equals("BOOLEAN")){
				dataFieldType=PropertyType.BOOLEAN;
			}else if(newDataFieldType.getValue().toString().equals("DATE")){
				dataFieldType=PropertyType.DATE;
			}else if(newDataFieldType.getValue().toString().equals("DECIMAL")){
				dataFieldType=PropertyType.DECIMAL;
			}else if(newDataFieldType.getValue().toString().equals("DOUBLE")){
				dataFieldType=PropertyType.DOUBLE;
			}else if(newDataFieldType.getValue().toString().equals("LONG")){
				dataFieldType=PropertyType.LONG;
			}
			DataFieldDefinition dataFieldDefination=ActivityComponentFactory.cteateDataFieldDefinition(newDataFieldName.getValue().toString(), dataFieldType, isArray);
			dataFieldDefination.setDescription(newDataFieldDescription.getValue().toString());
			dataFieldDefination.setDisplayName(newDataFieldDisplayName.getValue().toString());
			dataFieldDefination.setMandatoryField(isMandatory);
			dataFieldDefination.setSystemField(isSystem);		
			this.dataFieldDefinitionMap.put(dataFieldDefination.getFieldName(), dataFieldDefination);			
			PropertyItem propertyItem=this.exposedDataFieldsEditor.renderDataFieldDefinition(dataFieldDefination);			
			if(this.exposedDataFieldsEditor.businessActivityDefinitionUI!=null){				
				DataFieldDefinition[] newDataFieldDefinitions=this.exposedDataFieldsEditor.getDataFieldDefinition();				
				this.exposedDataFieldsEditor.businessActivityDefinitionUI.businessActivityDefinition.resetActivityDataFields(newDataFieldDefinitions);
			}			
			this.exposedDataFieldsEditor.addComponent(propertyItem);			
			if(!this.exposedDataFieldsEditor.isInBusinessActivityDefinitionUI()){
				this.exposedDataFieldsEditor.saveDefinationButton.setEnabled(true);
			}else{
				this.exposedDataFieldsEditor.businessActivityDefinitionModifiedNotice();
			}			
			this.exposedDataFieldsEditor.saveNofityContainer.setVisible(true);
			closeWindow();
		}		
	}
	
	private void updateDataField(PropertyItem parentPropertyItem){		
		newDataFieldDefinitionForm.setValidationVisible(true);
        boolean validateResult = newDataFieldDefinitionForm.isValid();
		if(validateResult){
			boolean isArray=((Boolean)isArrayField.getValue()).booleanValue();
			boolean isMandatory=((Boolean)isMandatoryField.getValue()).booleanValue();
			boolean isSystem=((Boolean)isSystemField.getValue()).booleanValue();
			int dataFieldType=PropertyType.STRING;
			if(newDataFieldType.getValue().toString().equals("STRING")){
				dataFieldType=PropertyType.STRING;
			}else if(newDataFieldType.getValue().toString().equals("BINARY")){
				dataFieldType=PropertyType.BINARY;
			}else if(newDataFieldType.getValue().toString().equals("BOOLEAN")){
				dataFieldType=PropertyType.BOOLEAN;
			}else if(newDataFieldType.getValue().toString().equals("DATE")){
				dataFieldType=PropertyType.DATE;
			}else if(newDataFieldType.getValue().toString().equals("DECIMAL")){
				dataFieldType=PropertyType.DECIMAL;
			}else if(newDataFieldType.getValue().toString().equals("DOUBLE")){
				dataFieldType=PropertyType.DOUBLE;
			}else if(newDataFieldType.getValue().toString().equals("LONG")){
				dataFieldType=PropertyType.LONG;
			}
			DataFieldDefinition dataFieldDefinition=ActivityComponentFactory.cteateDataFieldDefinition(newDataFieldName.getValue().toString(), dataFieldType, isArray);
			dataFieldDefinition.setDescription(newDataFieldDescription.getValue().toString());
			dataFieldDefinition.setDisplayName(newDataFieldDisplayName.getValue().toString());
			dataFieldDefinition.setMandatoryField(isMandatory);
			dataFieldDefinition.setSystemField(isSystem);
			this.dataFieldDefinitionMap.remove(dataFieldDefinition.getFieldName());
			this.dataFieldDefinitionMap.put(dataFieldDefinition.getFieldName(), dataFieldDefinition);			
			PropertyItem newParentPropertyItem=this.exposedDataFieldsEditor.renderDataFieldDefinition(dataFieldDefinition);	
			if(this.exposedDataFieldsEditor.businessActivityDefinitionUI!=null){				
				DataFieldDefinition[] newDataFieldDefinitions=this.exposedDataFieldsEditor.getDataFieldDefinition();				
				this.exposedDataFieldsEditor.businessActivityDefinitionUI.businessActivityDefinition.resetActivityDataFields(newDataFieldDefinitions);
			}
			int idx=this.exposedDataFieldsEditor.getComponentIndex(parentPropertyItem);
			this.exposedDataFieldsEditor.addComponent(newParentPropertyItem,idx);
			this.exposedDataFieldsEditor.removeComponent(parentPropertyItem);			
			if(!this.exposedDataFieldsEditor.isInBusinessActivityDefinitionUI()){
				this.exposedDataFieldsEditor.saveDefinationButton.setEnabled(true);
			}else{
				this.exposedDataFieldsEditor.businessActivityDefinitionModifiedNotice();
			}	
			this.exposedDataFieldsEditor.saveNofityContainer.setVisible(true);
			closeWindow();
		}		
	}
	
	private void closeWindow(){	
		LightContentWindow ContainerWindow=(LightContentWindow)(this.getParent().getParent());
		this.getApplication().getMainWindow().removeWindow(ContainerWindow);
		ContainerWindow=null;		
	}
}