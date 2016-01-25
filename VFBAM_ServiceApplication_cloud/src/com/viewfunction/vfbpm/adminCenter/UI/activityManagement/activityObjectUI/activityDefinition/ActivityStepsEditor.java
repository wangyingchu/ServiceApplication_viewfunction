package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityStepsEditor  extends VerticalLayout{
	private static final long serialVersionUID = -2416662367772642059L;
	private UserClientInfo userClientInfo;
	public HorizontalLayout saveStepPropertyChangeNofityContainer;
	private BusinessActivityDefinition businessActivityDefinition;
	private DialogWindow setpropertyWindow;
	private Map<String,HorizontalLayout> stepPropertyValueContainerMap;	
	private BusinessActivityDefinitionUI businessActivityDefinitionUI;	
	
	public ActivityStepsEditor(String activitySpaceName,BusinessActivityDefinition businessActivityDefinition,UserClientInfo userClientInfo,BusinessActivityDefinitionUI businessActivityDefinitionUI){
		this.userClientInfo=userClientInfo;	
		this.stepPropertyValueContainerMap=new HashMap<String,HorizontalLayout>();
		this.businessActivityDefinitionUI=businessActivityDefinitionUI;
		this.businessActivityDefinition=businessActivityDefinition;
		HorizontalLayout stepPropertyContainer=new HorizontalLayout();		
		stepPropertyContainer.setWidth("100%");
		stepPropertyContainer.setHeight("20px");
		stepPropertyContainer.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_EditorActionContainer);		
		HorizontalLayout propertyChangeMessageContainer=new HorizontalLayout();	
		if(this.businessActivityDefinitionUI!=null){
			HorizontalLayout divSpace_0=new HorizontalLayout();
			divSpace_0.setWidth("10px");
			propertyChangeMessageContainer.addComponent(divSpace_0);
		}		
		Label propertiesLabel=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_activityStepPropertyLabel"));
		propertyChangeMessageContainer.addComponent(propertiesLabel);		
		saveStepPropertyChangeNofityContainer=new HorizontalLayout();
		HorizontalLayout divSpace=new HorizontalLayout();
		divSpace.setWidth("10px");
		Embedded infoIcon=new Embedded(null, UICommonElementDefination.AppPanel_InfoActionbarIcon);
		Label saveDataNotification=new Label("<span style='color:#2779c7;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_activityStepPropertyChangedLabel")+"</span>", Label.CONTENT_XHTML);
		saveStepPropertyChangeNofityContainer.addComponent(divSpace);
		saveStepPropertyChangeNofityContainer.addComponent(infoIcon);
		saveStepPropertyChangeNofityContainer.addComponent(saveDataNotification);		
		propertyChangeMessageContainer.addComponent(saveStepPropertyChangeNofityContainer);	
		saveStepPropertyChangeNofityContainer.setVisible(false);				
		stepPropertyContainer.addComponent(propertyChangeMessageContainer);
		stepPropertyContainer.setComponentAlignment(propertyChangeMessageContainer, Alignment.MIDDLE_LEFT);		
		this.addComponent(stepPropertyContainer);
		try {			
			String[] stepsNameArray=this.businessActivityDefinition.getExposedSteps();	
			for(int i=0;i<stepsNameArray.length;i++){
				String currentStepName=stepsNameArray[i];				
				Role relatedRole=this.businessActivityDefinition.getActivityStepRelatedRole(currentStepName);			
				this.addComponent(renderStepInfoProperty(currentStepName,relatedRole));
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();		
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		}			
	}	
	
	private PropertyItem renderStepInfoProperty(final String stepName,Role relatedRole){				
		HorizontalLayout propertyValueContainer=new HorizontalLayout();		
		this.stepPropertyValueContainerMap.put(stepName, propertyValueContainer);
		renderStepPropertyValueContainer(stepName,propertyValueContainer);		
		HorizontalLayout stepActionContainer=new HorizontalLayout();		
		stepActionContainer.setWidth("100px");		
		Button editStepButton=new Button();		
		editStepButton.setCaption(null);
		editStepButton.setIcon(UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		editStepButton.setStyleName(BaseTheme.BUTTON_LINK);
		editStepButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_editStepDataLabel"));
		editStepButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -2335703133961884822L;

			public void buttonClick(ClickEvent event) {				
				renderEditStepDataFieldUI(stepName);				
			}});		
		stepActionContainer.addComponent(editStepButton);
		
		stepActionContainer.addComponent(new Label("|"));
		
		Button editProcessDataButton=new Button();		
		editProcessDataButton.setCaption(null);
		editProcessDataButton.setIcon(UICommonElementDefination.ICON_activityStep);
		editProcessDataButton.setStyleName(BaseTheme.BUTTON_LINK);
		editProcessDataButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_editStepProcessPropertyLabel"));
		editProcessDataButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 2785584684847999878L;

			public void buttonClick(ClickEvent event) {				
				renderEditStepProcessVariableListUI(stepName);				
			}});		
		stepActionContainer.addComponent(editProcessDataButton);		
		
		Button editDesicitionPointButton=new Button();		
		editDesicitionPointButton.setCaption(null);
		editDesicitionPointButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceStepDecisionPointIcon);
		editDesicitionPointButton.setStyleName(BaseTheme.BUTTON_LINK);
		editDesicitionPointButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_editStepDecisionPointLabel"));
		final String relatedRoleString=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_relatedRoleLabel");
		editDesicitionPointButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 4084899161946200418L;

			public void buttonClick(ClickEvent event) {				
				renderSetDesicitionPointUI(stepName);				
			}});		
		stepActionContainer.addComponent(editDesicitionPointButton);		
		
		String roleInfo=null;
		if(relatedRole!=null){
			roleInfo=relatedRoleString+" "+relatedRole.getDisplayName()+" ( "+relatedRole.getRoleName()+" )";
		}
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,stepName,roleInfo,propertyValueContainer,stepActionContainer);		
		return namePropertyItem;
	}	
	
	private void removeSetPropertyWindow(){
		this.getApplication().getMainWindow().removeWindow(setpropertyWindow);
		setpropertyWindow=null;
	}
	
	private void renderEditStepDataFieldUI(final String stepName){	
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepDataWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepDataWindowDesc1")+" <span style='color:#ce0000;font-weight:bold;'>"+stepName+"</span>";
        VerticalLayout setStatusLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);
		
		final OptionGroup dataFieldsSelect = new OptionGroup("");
		dataFieldsSelect.setMultiSelect(true);
		dataFieldsSelect.setNullSelectionAllowed(true);			
		
		//DataFieldDefinition[] dataFieldDefnArray=this.businessActivityDefinitionUI.exposedDataFieldsEditor.getDataFieldDefinition();
		DataFieldDefinition[] dataFieldDefnArray=this.businessActivityDefinition.getActivityDataFields();
		
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty("propertyName_DataField", DataFieldDefinition.class,null);
        container.addContainerProperty("propertyName_DataFieldDisplayName", String.class,null);
        dataFieldsSelect.setContainerDataSource(container);
        dataFieldsSelect.setItemCaptionPropertyId("propertyName_DataFieldDisplayName");        
        Map<String, DataFieldDefinition[]> step_DataFieldDefinition=this.businessActivityDefinition.getActivityStepsExposedDataField();				
		DataFieldDefinition[] dataFieldDefinitionArray=step_DataFieldDefinition.get(stepName);
		String[] currentStepExposedDataFieldArray=null;
		if(dataFieldDefinitionArray!=null){
			currentStepExposedDataFieldArray=new String[dataFieldDefinitionArray.length];
	        for(int i=0;i<dataFieldDefinitionArray.length;i++){
	        	currentStepExposedDataFieldArray[i]=dataFieldDefinitionArray[i].getFieldName();        	
	        }  
		}        
		for(int i=0;i<dataFieldDefnArray.length;i++){				
			DataFieldDefinition currentDataField=dataFieldDefnArray[i];
			String id = ""+i;
			Item item = container.addItem(id);	
			String dataFieldName=currentDataField.getFieldName();
			String dataFieldCaption=currentDataField.getDisplayName()+" ( "+currentDataField.getFieldName()+" )";
			item.getItemProperty("propertyName_DataField").setValue(currentDataField);  
			item.getItemProperty("propertyName_DataFieldDisplayName").setValue(dataFieldCaption);		
			if(alreadyContainsDataField(dataFieldName,currentStepExposedDataFieldArray)){
				dataFieldsSelect.select(id);					
			}
		}					
		inputFieldListLayout.addComponent(dataFieldsSelect);		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepData_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -2385004185506881591L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				Set selectedDataFieldIdxSet=(Set)dataFieldsSelect.getValue();				
				Object[] idxArry=selectedDataFieldIdxSet.toArray();	
				DataFieldDefinition[] newDadaFieldNameArray=new DataFieldDefinition[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					newDadaFieldNameArray[i]=(DataFieldDefinition)selecteItem.getItemProperty("propertyName_DataField").getValue();					
				}
				doSetStepExposedDataFields(stepName,newDadaFieldNameArray);
			}
	     });  		
		if(this.businessActivityDefinitionUI==null){
			confirmButton.setEnabled(false);
		}
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepData_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -5453740733832501356L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
				removeSetPropertyWindow();
	           }
	     });	
	    cancelButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmButton);
        buttonList.add(cancelButton); 
        setStatusLayout.addComponent(new BaseButtonBar(120, 30, Alignment.MIDDLE_RIGHT, buttonList));
        setpropertyWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, 420,setStatusLayout);
        setpropertyWindow.setResizable(false);
        setpropertyWindow.center();
        setpropertyWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(setpropertyWindow);		
	}
	
	private void doSetStepExposedDataFields(String stepName,DataFieldDefinition[] newDadaFieldNameArray){      		
		DataFieldDefinition[] oldDataFieldDefinitionArray=this.businessActivityDefinition.getActivityStepsExposedDataField().get(stepName); 
		try {
			if(oldDataFieldDefinitionArray==null||oldDataFieldDefinitionArray.length==0){				
				this.businessActivityDefinition.setActivityStepExposedDataFields(stepName, newDadaFieldNameArray);
				this.businessActivityDefinition.setStepProcessVariableList(stepName, null);						
			}else{
				for(DataFieldDefinition dataFieldDefinition:newDadaFieldNameArray){						
					if(!alreadyContainsDataField(dataFieldDefinition,oldDataFieldDefinitionArray)){
						this.businessActivityDefinition.addActivityStepExposedDataField(stepName, dataFieldDefinition);	
					}
				}
				for(DataFieldDefinition oldDataFieldDefinition:oldDataFieldDefinitionArray){
					if(!alreadyContainsDataField(oldDataFieldDefinition,newDadaFieldNameArray)){
						this.businessActivityDefinition.removeActivityStepExposedDataField(stepName, oldDataFieldDefinition.getFieldName());												
					}
				}
				String[] stepProcessVariableList=this.businessActivityDefinition.getStepProcessVariableList(stepName);				
				if(stepProcessVariableList!=null&&stepProcessVariableList.length!=0){
					List<String> newstepProcessVariableList=new ArrayList<String>();
					for(String oldProcessVariable:stepProcessVariableList){
						if(alreadyContainsDataField(oldProcessVariable,newDadaFieldNameArray)){
							newstepProcessVariableList.add(oldProcessVariable);						
						}						
					}
					String[] newstepProcessVariableArray=new String[newstepProcessVariableList.size()];
					newstepProcessVariableList.toArray(newstepProcessVariableArray);
					this.businessActivityDefinition.setStepProcessVariableList(stepName, newstepProcessVariableArray);						
				}				
			}
			HorizontalLayout propertyValueContainer= stepPropertyValueContainerMap.get(stepName);
			propertyValueContainer.removeAllComponents();
			renderStepPropertyValueContainer(stepName,propertyValueContainer);
			removeSetPropertyWindow();
			saveStepPropertyChangeNofityContainer.setVisible(true);
			this.businessActivityDefinitionUI.activityDefinitionStepDataModified();			
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		}	
	}
	
	private boolean alreadyContainsDataField(DataFieldDefinition dataFieldDf,DataFieldDefinition[] dataFieldDfArray){
		if(dataFieldDfArray==null||dataFieldDfArray.length==0){
			return false;
		}else{			
			for(DataFieldDefinition currentField:dataFieldDfArray){
				if(currentField.getFieldName().equals(dataFieldDf.getFieldName())){
					return true;
				}
			}		
			return false;
		}
	}
	
	private boolean alreadyContainsDataField(String dataFieldDf,DataFieldDefinition[] dataFieldDfArray){
		if(dataFieldDfArray==null||dataFieldDfArray.length==0){
			return false;
		}else{			
			for(DataFieldDefinition currentField:dataFieldDfArray){
				if(currentField.getFieldName().equals(dataFieldDf)){
					return true;
				}
			}		
			return false;
		}
	}
	
	private boolean alreadyContainsDataField(String dataFieldName,String[] dataFieldsArray){
		if(dataFieldsArray==null){
			return false;
		}
		for(String currentField:dataFieldsArray){
			if(currentField.equals(dataFieldName)){
				return true;
			}
		}		
		return false;
	}
	private void renderStepPropertyValueContainer(String stepName,HorizontalLayout propertyValueContainer){
		Map<String, DataFieldDefinition[]> step_DataFieldDefinition=this.businessActivityDefinition.getActivityStepsExposedDataField();		
		String[] processVariableArray=this.businessActivityDefinition.getStepProcessVariableList(stepName);
		DataFieldDefinition[] dataFieldDefinitionArray=step_DataFieldDefinition.get(stepName);
		if(dataFieldDefinitionArray!=null){
			if(dataFieldDefinitionArray.length>5){
				Label dataField0Label=new Label("<span style='color:#1360a8;'>"+dataFieldDefinitionArray[0].getFieldName()+"</span>",Label.CONTENT_XHTML);
				dataField0Label.setDescription(dataFieldDefinitionArray[0].getDisplayName());			
				Label dataField1Label=new Label("<span style='color:#1360a8;'>"+dataFieldDefinitionArray[1].getFieldName()+"</span>",Label.CONTENT_XHTML);
				dataField1Label.setDescription(dataFieldDefinitionArray[1].getDisplayName());			
				Label dataField2Label=new Label("<span style='color:#1360a8;'>"+dataFieldDefinitionArray[2].getFieldName()+"</span>",Label.CONTENT_XHTML);
				dataField2Label.setDescription(dataFieldDefinitionArray[2].getDisplayName());			
				Label dataField3Label=new Label("<span style='color:#1360a8;'>"+dataFieldDefinitionArray[3].getFieldName()+"</span>",Label.CONTENT_XHTML);
				dataField3Label.setDescription(dataFieldDefinitionArray[3].getDisplayName());			
				Label dataField4Label=new Label("<span style='color:#1360a8;'>"+dataFieldDefinitionArray[4].getFieldName()+"</span>",Label.CONTENT_XHTML);
				dataField4Label.setDescription(dataFieldDefinitionArray[4].getDisplayName());			
				propertyValueContainer.addComponent(dataField0Label);
				if(alreadyContainsDataField(dataFieldDefinitionArray[0].getFieldName(),processVariableArray)){
					Embedded processFieldIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceProcessFieldIcon);					
					processFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepProcessVarLabel"));
					propertyValueContainer.addComponent(processFieldIcon);
				}	
				propertyValueContainer.addComponent(new Label(";&nbsp;&nbsp;",Label.CONTENT_XHTML));				
				propertyValueContainer.addComponent(dataField1Label);
				if(alreadyContainsDataField(dataFieldDefinitionArray[1].getFieldName(),processVariableArray)){
					Embedded processFieldIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceProcessFieldIcon);
					processFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepProcessVarLabel"));
					propertyValueContainer.addComponent(processFieldIcon);
				}
				propertyValueContainer.addComponent(new Label(";&nbsp;&nbsp;",Label.CONTENT_XHTML));
				propertyValueContainer.addComponent(dataField2Label);
				if(alreadyContainsDataField(dataFieldDefinitionArray[2].getFieldName(),processVariableArray)){
					Embedded processFieldIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceProcessFieldIcon);
					processFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepProcessVarLabel"));
					propertyValueContainer.addComponent(processFieldIcon);
				}
				propertyValueContainer.addComponent(new Label(";&nbsp;&nbsp;",Label.CONTENT_XHTML));
				propertyValueContainer.addComponent(dataField3Label);
				if(alreadyContainsDataField(dataFieldDefinitionArray[3].getFieldName(),processVariableArray)){
					Embedded processFieldIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceProcessFieldIcon);
					processFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepProcessVarLabel"));
					propertyValueContainer.addComponent(processFieldIcon);
				}
				propertyValueContainer.addComponent(new Label(";&nbsp;&nbsp;",Label.CONTENT_XHTML));
				propertyValueContainer.addComponent(dataField4Label);
				if(alreadyContainsDataField(dataFieldDefinitionArray[4].getFieldName(),processVariableArray)){
					Embedded processFieldIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceProcessFieldIcon);
					processFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepProcessVarLabel"));
					propertyValueContainer.addComponent(processFieldIcon);
				}
				propertyValueContainer.addComponent(new Label("......",Label.CONTENT_XHTML));
			}else{
				for(DataFieldDefinition dataFieldDefinition:dataFieldDefinitionArray){					
					Label dataFieldLabel=new Label("<span style='color:#1360a8;'>"+dataFieldDefinition.getFieldName()+"</span>",Label.CONTENT_XHTML);
					dataFieldLabel.setDescription(dataFieldDefinition.getDisplayName());		
					propertyValueContainer.addComponent(dataFieldLabel);				
					if(alreadyContainsDataField(dataFieldDefinition.getFieldName(),processVariableArray)){
						Embedded processFieldIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceProcessFieldIcon);
						processFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepProcessVarLabel"));
						propertyValueContainer.addComponent(processFieldIcon);
					}					
					propertyValueContainer.addComponent(new Label(";&nbsp;&nbsp;",Label.CONTENT_XHTML));									
				}
			}			
		}		
	}
	
	private void renderEditStepProcessVariableListUI(final String stepName){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepProcessVariableWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepProcessVariableWindowDesc1")+" <span style='color:#ce0000;font-weight:bold;'>"+stepName+"</span>";
        VerticalLayout setStatusLayout = new VerticalLayout();	        
        HorizontalLayout spaceDivLayout=new HorizontalLayout();
        spaceDivLayout.setHeight("10px");
        setStatusLayout.addComponent(spaceDivLayout);  
        
        HorizontalLayout attributeNameinputFieldListLayout_0=new HorizontalLayout();	        
		HorizontalLayout attributeNameLeftSpaceLayout_0=new HorizontalLayout();	
		attributeNameLeftSpaceLayout_0.setWidth("20px");
		attributeNameinputFieldListLayout_0.addComponent(attributeNameLeftSpaceLayout_0);		
		
		Role currentStepRole=null;
		final ComboBox rolsChoise = new ComboBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepRoleLabel"));
		rolsChoise.setNullSelectionAllowed(true);			
		rolsChoise.setWidth("250px");
		rolsChoise.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepRoleInputPrompt"));		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.businessActivityDefinition.getActivitySpaceName());
		Role[] rolesArray=null;
		try {
			rolesArray = activitySpace.getRoles();
			 currentStepRole=this.businessActivityDefinition.getActivityStepRelatedRole(stepName);
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		}		
		final IndexedContainer roleContainer = new IndexedContainer();
		rolsChoise.setContainerDataSource(roleContainer);
		rolsChoise.setItemCaptionPropertyId("ROLE_DISPLAYNAME");
		rolsChoise.setEnabled(true);		
		roleContainer.addContainerProperty("ROLE_DISPLAYNAME", String.class, null);
		roleContainer.addContainerProperty("ROLE", Role.class, null);						
		for(int i=0;i<rolesArray.length;i++){
			String id = ""+i;
			Item item = roleContainer.addItem(id);	
			item.getItemProperty("ROLE_DISPLAYNAME").setValue(rolesArray[i].getDisplayName()+ "( "+rolesArray[i].getRoleName()+" )");
	        item.getItemProperty("ROLE").setValue(rolesArray[i]);	        
	        if(currentStepRole!=null&&currentStepRole.getRoleName().equals(rolesArray[i].getRoleName())){
	        	rolsChoise.select(id);
	        }
		}	
		attributeNameinputFieldListLayout_0.addComponent(rolsChoise);		
		setStatusLayout.addComponent(attributeNameinputFieldListLayout_0); 
        
		HorizontalLayout spaceDivLayout_1=new HorizontalLayout();
		spaceDivLayout_1.setHeight("10px");
	    setStatusLayout.addComponent(spaceDivLayout_1);
		
        HorizontalLayout attributeNameinputFieldListLayout=new HorizontalLayout();	        
		HorizontalLayout attributeNameLeftSpaceLayout=new HorizontalLayout();	
		attributeNameLeftSpaceLayout.setWidth("20px");
		attributeNameinputFieldListLayout.addComponent(attributeNameLeftSpaceLayout);        
		final TextField stepUserIdentityAttributeName=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepuserIDAttrNameLabel"));
		stepUserIdentityAttributeName.setWidth("250px");
		stepUserIdentityAttributeName.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepuserIDAttrNameInputPrompt"));	
		if(this.businessActivityDefinition.getStepUserIdentityAttributeName(stepName)!=null){			
			stepUserIdentityAttributeName.setValue(this.businessActivityDefinition.getStepUserIdentityAttributeName(stepName));			
		}		
		attributeNameinputFieldListLayout.addComponent(stepUserIdentityAttributeName);		
		setStatusLayout.addComponent(attributeNameinputFieldListLayout);  
		
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);				
		final OptionGroup dataFieldsSelect = new OptionGroup("");
		dataFieldsSelect.setMultiSelect(true);
		dataFieldsSelect.setNullSelectionAllowed(true);			
		String[] oldProcessVariableArray=this.businessActivityDefinition.getStepProcessVariableList(stepName);       			
		DataFieldDefinition[] dataFieldDefinitionArray=this.businessActivityDefinition.getActivityStepsExposedDataField().get(stepName);		
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty("propertyName_DataFieldName", String.class,null);
        container.addContainerProperty("propertyName_DataFieldDisplayName", String.class,null);
        dataFieldsSelect.setContainerDataSource(container);
        dataFieldsSelect.setItemCaptionPropertyId("propertyName_DataFieldDisplayName");
        if(dataFieldDefinitionArray!=null){
        	for(int i=0;i<dataFieldDefinitionArray.length;i++){				
    			DataFieldDefinition currentDataField=dataFieldDefinitionArray[i];
    			String id = ""+i;
    			Item item = container.addItem(id);	
    			String dataFieldName=currentDataField.getFieldName();
    			String dataFieldCaption=currentDataField.getDisplayName()+" ( "+currentDataField.getFieldName()+" )";
    			item.getItemProperty("propertyName_DataFieldName").setValue(dataFieldName);  
    			item.getItemProperty("propertyName_DataFieldDisplayName").setValue(dataFieldCaption);		
    			if(alreadyContainsDataField(dataFieldName,oldProcessVariableArray)){
    				dataFieldsSelect.select(id);					
    			}
    		}				
        }			
		inputFieldListLayout.addComponent(dataFieldsSelect);		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepProcessVariabl_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -6595590958869969593L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				Set selectedDataFieldIdxSet=(Set)dataFieldsSelect.getValue();				
				Object[] idxArry=selectedDataFieldIdxSet.toArray();	
				String[] newDadaFieldNameArray=null;
				if(idxArry.length>0){
					newDadaFieldNameArray=new String[idxArry.length];					
				}				
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					newDadaFieldNameArray[i]=selecteItem.getItemProperty("propertyName_DataFieldName").getValue().toString();					
				}
				String stepUserIdentityAttributeNameString=null;
				if(stepUserIdentityAttributeName.getValue()!=null&&!stepUserIdentityAttributeName.getValue().toString().equals("")){
					stepUserIdentityAttributeNameString=stepUserIdentityAttributeName.getValue().toString();					
				}
				String stepRole=null;
				if(rolsChoise.getValue()!=null&&!rolsChoise.getValue().toString().equals("")){
					Item selecteItem=roleContainer.getItem(rolsChoise.getValue());
					Role selectedRole=(Role)selecteItem.getItemProperty("ROLE").getValue();						
					stepRole=selectedRole.getRoleName();
				}				
				doSetStepProcessVariables(stepName,newDadaFieldNameArray,stepUserIdentityAttributeNameString,stepRole);
			}
	     });  
		if(this.businessActivityDefinitionUI==null){
			confirmButton.setEnabled(false);
		}
		
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepProcessVariabl_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -8834379584909293587L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
				removeSetPropertyWindow();
	         }
	     });	
	    cancelButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmButton);
        buttonList.add(cancelButton); 
        setStatusLayout.addComponent(new BaseButtonBar(120, 30, Alignment.MIDDLE_RIGHT, buttonList));
        setpropertyWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, 420,setStatusLayout);
        setpropertyWindow.setResizable(false);
        setpropertyWindow.center();
        setpropertyWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(setpropertyWindow);			
	}
	
	private void doSetStepProcessVariables(String stepName,String[] stepProcessVariableList,String stepUserIdentityAttributeName,String stepRole){
		try {
			this.businessActivityDefinition.setStepProcessVariableList(stepName, stepProcessVariableList);
			this.businessActivityDefinition.setStepUserIdentityAttributeName(stepName, stepUserIdentityAttributeName);				
			try {
				this.businessActivityDefinition.setActivityStepRelatedRole(stepName, stepRole);
			} catch (ActivityEngineProcessException e) {					
				e.printStackTrace();
			}					
			HorizontalLayout propertyValueContainer= stepPropertyValueContainerMap.get(stepName);
			propertyValueContainer.removeAllComponents();
			renderStepPropertyValueContainer(stepName,propertyValueContainer);
			removeSetPropertyWindow();
			saveStepPropertyChangeNofityContainer.setVisible(true);
			this.businessActivityDefinitionUI.activityDefinitionStepDataModified();				
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
	}
	
	private void renderSetDesicitionPointUI(final String stepName){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepDecisionPointWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepDecisionPointWindowDesc1")+" <span style='color:#ce0000;font-weight:bold;'>"+stepName+"</span>";
        VerticalLayout setStatusLayout = new VerticalLayout();	        
        HorizontalLayout spaceDivLayout=new HorizontalLayout();
        spaceDivLayout.setHeight("10px");
        setStatusLayout.addComponent(spaceDivLayout);        
        HorizontalLayout attributeNameinputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout attributeNameLeftSpaceLayout=new HorizontalLayout();	
		attributeNameLeftSpaceLayout.setWidth("20px");
		attributeNameinputFieldListLayout.addComponent(attributeNameLeftSpaceLayout);        
		final TextField stepDecisionPointAttributeName=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepDesionPAttrNameLabel"));
		stepDecisionPointAttributeName.setWidth("360px");
		stepDecisionPointAttributeName.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepDesionPAttrNameInptPrompt"));	
		if(this.businessActivityDefinition.getStepDecisionPointAttributeName(stepName)!=null){			
			stepDecisionPointAttributeName.setValue(this.businessActivityDefinition.getStepDecisionPointAttributeName(stepName));			
		}		
		attributeNameinputFieldListLayout.addComponent(stepDecisionPointAttributeName);		
		setStatusLayout.addComponent(attributeNameinputFieldListLayout);  		
		HorizontalLayout spaceDivLayout_2=new HorizontalLayout();
        spaceDivLayout_2.setHeight("10px");
        setStatusLayout.addComponent(spaceDivLayout_2);  	        
        
        HorizontalLayout labelContainerLayout=new HorizontalLayout();
        HorizontalLayout labelLeftSpaceLayout=new HorizontalLayout();	
        labelLeftSpaceLayout.setWidth("20px");        
        labelContainerLayout.addComponent(labelLeftSpaceLayout);
        setStatusLayout.addComponent(labelContainerLayout);        
        Label DecisionPointChoiseListLabel=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_stepDesionChoiseListLabel"));       
        labelContainerLayout.addComponent(DecisionPointChoiseListLabel);
		
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);			
		
		String[] currentChoiseList=this.businessActivityDefinition.getStepDecisionPointChoiseList(stepName);
		final DecisionPointChoiseListEditor decisionPointChoiseListEditor=new DecisionPointChoiseListEditor(currentChoiseList,this.userClientInfo);	
		inputFieldListLayout.addComponent(decisionPointChoiseListEditor);
		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepDesionPoint_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -6595590958869969593L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				boolean checkResult=decisionPointChoiseListEditor.validateDateField();
				if(checkResult){
					String[] choiseListValue=null;
						if(decisionPointChoiseListEditor.getChoistList()!=null&&decisionPointChoiseListEditor.getChoistList().length>0){
							choiseListValue=decisionPointChoiseListEditor.getChoistList();	
						}					
					String decisionPointAttributeName=null;
					if(stepDecisionPointAttributeName.getValue()!=null&&!stepDecisionPointAttributeName.getValue().toString().equals("")){
						decisionPointAttributeName=stepDecisionPointAttributeName.getValue().toString();					
					}					
					doSetDecisionPointChoiseList(stepName,choiseListValue,decisionPointAttributeName);				
				}
			}
	     });  
		if(this.businessActivityDefinitionUI==null){
			confirmButton.setEnabled(false);
		}
		
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepsEditor_setStepDesionPoint_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -8834379584909293587L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
				removeSetPropertyWindow();
	         }
	     });	
	    cancelButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmButton);
        buttonList.add(cancelButton); 
        setStatusLayout.addComponent(new BaseButtonBar(120, 30, Alignment.MIDDLE_RIGHT, buttonList));
        setpropertyWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, 420,setStatusLayout);
        setpropertyWindow.setResizable(false);
        setpropertyWindow.center();
        setpropertyWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(setpropertyWindow);			
	}
	
	private void doSetDecisionPointChoiseList(String stepName,String[] decisionPointChoiseList,String decisionPointAttributeName){
		try {
			this.businessActivityDefinition.setStepDecisionPointChoiseList(stepName, decisionPointChoiseList);
			this.businessActivityDefinition.setStepDecisionPointAttributeName(stepName, decisionPointAttributeName);
			removeSetPropertyWindow();
			saveStepPropertyChangeNofityContainer.setVisible(true);
			this.businessActivityDefinitionUI.activityDefinitionStepDataModified();				
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}
	}
}