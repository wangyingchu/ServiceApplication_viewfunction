package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.ActivityDefinitionsChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.BusinessActivityDefinitionsTable;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ExposedDataFieldsEditor;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.MainTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
public class RosterUI  extends VerticalLayout{
	private static final long serialVersionUID = 2413518698583468866L;
	
	private Roster roster;
	private UserClientInfo userClientInfo;
	private RosterPropertyList rosterPropertyList;
	private ActivityObjectDetail activityObjectDetail;
	private String propertyName_ActivityTypeName="propertyName_ActivityTypeName";
	private LightContentWindow activityTypeSelectWindow;
	private BusinessActivityDefinitionsTable businessActivityDefinitionsTable;
	private DialogWindow editRosterWindow;
	private VerticalLayout businessActivitiesUIContainer;
	
	public RosterUI(final Roster currentRoster, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail){
		this.roster=currentRoster;
		this.userClientInfo=userClientInfo;
		this.activityObjectDetail=activityObjectDetail;
		Button parentObjectLinkButton=new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_rostersLabel"));
		parentObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);		
		parentObjectLinkButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 5053624693547825869L;

			public void buttonClick(ClickEvent event) {				
				renderRostersUI(currentRoster.getActivitySpaceName());
			}}); 		
			
		MainTitleBar mainTitleBar=new MainTitleBar(this.roster.getActivitySpaceName(),parentObjectLinkButton,this.roster.getRosterName());
		this.addComponent(mainTitleBar);		
		
		//render Roster data		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);
		
		// header part		
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRosterIcon);				
		HorizontalLayout rosterActionbarContainer=new HorizontalLayout();			
		rosterActionbarContainer.setWidth("30px");		
		Button editRosterButton=new Button();
		editRosterButton.setCaption(null);
		editRosterButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		editRosterButton.setStyleName(BaseTheme.BUTTON_LINK);
		editRosterButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_editRosterButton"));
		editRosterButton.addListener(new ClickListener(){		
			private static final long serialVersionUID = -2347390083648839401L;

				public void buttonClick(ClickEvent event) {							
					renderEditRosterFormUI();
				}});		
		rosterActionbarContainer.addComponent(editRosterButton);		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,this.roster.getRosterName(),SectionTitleBar.BIGFONT,rosterActionbarContainer);
		containerPanel.addComponent(sectionTitleBar);
		
		rosterPropertyList=new RosterPropertyList(this.roster,this.userClientInfo);
		containerPanel.addComponent(rosterPropertyList);			
		//Roster detail		
		HorizontalLayout divHorizontalLayout_1=new HorizontalLayout();
		divHorizontalLayout_1.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout_1);			
	
		Embedded containActivityDefinesIcon=new Embedded(null, UICommonElementDefination.ICON_roster_light);			
		HorizontalLayout activityTypeActionbarContainer=new HorizontalLayout();			
		activityTypeActionbarContainer.setWidth("30px");		
		Button modifyActivityTypeButton=new Button();
		modifyActivityTypeButton.setCaption(null);
		modifyActivityTypeButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		modifyActivityTypeButton.setStyleName(BaseTheme.BUTTON_LINK);
		modifyActivityTypeButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_editActivityTypeButton"));	
		modifyActivityTypeButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = 8171390740019665569L;

			public void buttonClick(ClickEvent event) {							
				renderModifyActivityDefinesFormUI();
			}});	
		activityTypeActionbarContainer.addComponent(modifyActivityTypeButton);		
		SectionTitleBar activityDefinationSectionTitleBar=new SectionTitleBar(containActivityDefinesIcon,userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_activityDefinSectionTitle"),SectionTitleBar.MIDDLEFONT,activityTypeActionbarContainer);		
		containerPanel.addComponent(activityDefinationSectionTitleBar);		
		try {
			String[] containedActivityTypes=roster.getContainedActivityTypes();
			BusinessActivityDefinition[] businessActivityDefinitionArray;
			if(containedActivityTypes!=null){
				businessActivityDefinitionArray=new BusinessActivityDefinition[containedActivityTypes.length];
				String activityName=roster.getActivitySpaceName();
				ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activityName);
				for(int i=0;i<containedActivityTypes.length;i++){
					businessActivityDefinitionArray[i]=activitySpace.getBusinessActivityDefinition(containedActivityTypes[i]);
				}
			}else{
				businessActivityDefinitionArray=new BusinessActivityDefinition[0];
			}			
			businessActivityDefinitionsTable=
					new BusinessActivityDefinitionsTable(businessActivityDefinitionArray,this.userClientInfo,this.activityObjectDetail);			
			containerPanel.addComponent(businessActivityDefinitionsTable);			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {
			e.printStackTrace();
		}		
				
		HorizontalLayout divHorizontalLayout_2=new HorizontalLayout();
		divHorizontalLayout_2.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout_2);	
		
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields_light);
		SectionTitleBar datafieldSectionTitleBar=new SectionTitleBar(expDataFieldsIcon,userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_dataFieldsSectionTitle"),SectionTitleBar.MIDDLEFONT,null);		
		containerPanel.addComponent(datafieldSectionTitleBar);			
		DataFieldDefinition[] dataFieldDefinations=null;
		try {
			dataFieldDefinations=this.roster.getExposedDataFields();					
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		}	
		ExposedDataFieldsEditor exposedDataFieldsEditor=new ExposedDataFieldsEditor(this.roster,dataFieldDefinations,userClientInfo,false,null);
		containerPanel.addComponent(exposedDataFieldsEditor);
		
		HorizontalLayout divHorizontalLayout_3=new HorizontalLayout();
		divHorizontalLayout_3.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout_3);		
		
		Embedded activityInstancesIconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceActivityInstance_lightIcon);			
		HorizontalLayout rosterBusinessActivitiesActionbarContainer=new HorizontalLayout();			
		rosterBusinessActivitiesActionbarContainer.setWidth("30px");		
		Button fetchBusinessActivitiesButton=new Button();
		fetchBusinessActivitiesButton.setCaption(null);
		fetchBusinessActivitiesButton.setIcon(UICommonElementDefination.Appaction_fetchDataIcon);
		fetchBusinessActivitiesButton.setStyleName(BaseTheme.BUTTON_LINK);
		fetchBusinessActivitiesButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_fetchActivitiesButton"));		
		rosterBusinessActivitiesActionbarContainer.addComponent(fetchBusinessActivitiesButton);
		
		fetchBusinessActivitiesButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 6857190167206773526L;

			public void buttonClick(ClickEvent event) {							
				renderBusinessActiviesUI();
			}});		
		SectionTitleBar tasksSectionTitleBar=new SectionTitleBar(activityInstancesIconEmbedded,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_businessActivitySectionTitle"),SectionTitleBar.MIDDLEFONT,rosterBusinessActivitiesActionbarContainer);
		containerPanel.addComponent(tasksSectionTitleBar);
		
		businessActivitiesUIContainer=new VerticalLayout();
		containerPanel.addComponent(businessActivitiesUIContainer);
	}
	
	public void renderRostersUI(String activitySpaceName){
		ActivityObjectDetail activityObjectDetail =(ActivityObjectDetail)(this.getParent().getParent());
		activityObjectDetail.renderRostersUI(activitySpaceName);
	}
	
	private void renderModifyActivityDefinesFormUI(){
		Embedded activityTypeOfRosterIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceBusinessActivity_blackIcon);		
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_activityDefineWindowTitle")+" </b> ", Label.CONTENT_XHTML);
		final OptionGroup activityTypesSelect = new OptionGroup("");
		activityTypesSelect.setMultiSelect(true);
		activityTypesSelect.setNullSelectionAllowed(true);				
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(propertyName_ActivityTypeName, String.class,null);      
        activityTypesSelect.setContainerDataSource(container);
        activityTypesSelect.setItemCaptionPropertyId(propertyName_ActivityTypeName);
        ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.roster.getActivitySpaceName());
        try {
			BusinessActivityDefinition[] activityTypeArray=activitySpace.getBusinessActivityDefinitions();
			String[] alreadyContainedActivityTypeArray=this.roster.getContainedActivityTypes();			
			if(activityTypeArray!=null){
				for(int i=0;i<activityTypeArray.length;i++){
					BusinessActivityDefinition currentType=activityTypeArray[i];
					String id = ""+i;
					Item item = container.addItem(id);	
					String activityTypeName=currentType.getActivityType();
					item.getItemProperty(propertyName_ActivityTypeName).setValue(activityTypeName); 
					if(alreadyContainedInRoster(activityTypeName,alreadyContainedActivityTypeArray)){
						activityTypesSelect.select(id);					
					}					
				}				
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}	 
        
        VerticalLayout activityTypesSelectContainerLayout=new VerticalLayout();		
        activityTypesSelectContainerLayout.addComponent(activityTypesSelect);		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateactivityDefine_confirmButton"));		
		okButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 1040351003082655037L;

			public void buttonClick(ClickEvent event) {				
				Set selectedActivityTypeIdxSet=(Set)activityTypesSelect.getValue();				
				Object[] idxArry=selectedActivityTypeIdxSet.toArray();	
				String[] newActivityTypeArray=new String[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					newActivityTypeArray[i]=selecteItem.getItemProperty(propertyName_ActivityTypeName).getValue().toString();					
				}
				doUpdateActivityTypea(newActivityTypeArray);				
				removeEditActivityTypeWindow();
			}
		  });		
		Button cancelAddbutton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateactivityDefine_cancelButton"),new Button.ClickListener() {		
			private static final long serialVersionUID = -849355329004831752L;

			public void buttonClick(ClickEvent event) {               
				removeEditActivityTypeWindow();			
	        }
	    });
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
		    
		buttonList.add(okButton);
		buttonList.add(cancelAddbutton);
		BaseButtonBar selectActivityTypesButtonBar = new BaseButtonBar(100, 30, Alignment.BOTTOM_RIGHT, buttonList);		
		activityTypesSelectContainerLayout.addComponent(selectActivityTypesButtonBar);		
		activityTypeSelectWindow=new LightContentWindow(activityTypeOfRosterIcon,propertyNameLable,activityTypesSelectContainerLayout,"300px");		
		activityTypeSelectWindow.center();
		this.getApplication().getMainWindow().addWindow(activityTypeSelectWindow);	
	}
	
	private void removeEditActivityTypeWindow(){
		this.getApplication().getMainWindow().removeWindow(activityTypeSelectWindow);
		activityTypeSelectWindow=null;
	}
	
	private boolean alreadyContainedInRoster(String typeName,String[] typeArray){
		if(typeArray==null){
			return false;
		}
		for(String currentType:typeArray){
			if(currentType.equals(typeName)){
				return true;
			}
		}		
		return false;
	}
	
	private void doUpdateActivityTypea(String[] activityTypes){		
		try {
			String[] orgContainedActivityTypes=this.roster.getContainedActivityTypes();			
			if(orgContainedActivityTypes!=null&&orgContainedActivityTypes.length!=0){
				for(String orgActivityType:orgContainedActivityTypes){
					if(!ifContainsInActivityTypeArray(orgActivityType,activityTypes)){						
						this.roster.removeActivityType(orgActivityType);
					}
				}				
			}else{
				if(orgContainedActivityTypes!=null){
					for(String activityType:orgContainedActivityTypes){
						this.roster.removeActivityType(activityType);
					}
				}				
			}
			ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.roster.getActivitySpaceName());
			for(String activityTypeName:activityTypes){
				String orgRosterName=activitySpace.getBusinessActivityDefinition(activityTypeName).getRosterName();
				if(orgRosterName!=null){
					activitySpace.getRoster(orgRosterName).removeActivityType(activityTypeName);
				}				
				this.roster.addActivityType(activityTypeName);		
			}			
			
			String[] containedActivityTypes=this.roster.getContainedActivityTypes();
			BusinessActivityDefinition[] businessActivityDefinitionArray;
			if(containedActivityTypes!=null){
				businessActivityDefinitionArray=new BusinessActivityDefinition[containedActivityTypes.length];			
				for(int i=0;i<containedActivityTypes.length;i++){
					businessActivityDefinitionArray[i]=activitySpace.getBusinessActivityDefinition(containedActivityTypes[i]);
				}
			}else{
				businessActivityDefinitionArray=new BusinessActivityDefinition[0];
			}			
			businessActivityDefinitionsTable.reloadContent(businessActivityDefinitionArray);
			String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateactivityDefineSucess_msg1")+" "+
			this.roster.getRosterName()+"  "+
			userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateactivityDefineSucess_msg2");
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);	
			this.userClientInfo.getEventBlackboard().fire(new ActivityDefinitionsChangeEvent(this.roster.getActivitySpaceName()));				
		} catch (ActivityEngineRuntimeException e) {
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
	}
	
	private boolean ifContainsInActivityTypeArray(String orgActivityType,String[] activityTypes){
		if(activityTypes==null){
			return false;
		}
		for(String curTypeName:activityTypes){
			if(curTypeName.equals(orgActivityType)){
				return true;
			}
		}		
		return false;
	}
	
	private void renderEditRosterFormUI(){
		VerticalLayout windowContent = new VerticalLayout();		
		final BaseForm editRosterDataForm=new BaseForm();			
		final TextField rosterNameField=new TextField(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_rosterNameField"));		
		rosterNameField.setValue(this.roster.getRosterName());
		rosterNameField.setEnabled(false);
		rosterNameField.setWidth("250px");
		rosterNameField.setRequired(true);	
		editRosterDataForm.addField("rosterName", rosterNameField);
		
		final TextField rosterDisplayNameField=new TextField(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_rosterDisplayNameField"));		
		rosterDisplayNameField.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_rosterDisplayNameFieldDesc"));
		rosterDisplayNameField.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_rosterDisplayNameFieldErrMsg"));
		rosterDisplayNameField.setValue(this.roster.getDisplayName());
		rosterDisplayNameField.setWidth("250px");
		rosterDisplayNameField.setRequired(true);		
		editRosterDataForm.addField("rosterDisplayName", rosterDisplayNameField);
		
		final TextArea rosterDescTextArea=new TextArea(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_rosterDescField"));
		rosterDescTextArea.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_rosterDescFieldDesc"));
		rosterDescTextArea.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_rosterDescFieldErrMsg"));
		rosterDescTextArea.setValue(this.roster.getDescription());
		rosterDescTextArea.setRows(2);
		rosterDescTextArea.setColumns(20);
		rosterDescTextArea.setRequired(true);
		editRosterDataForm.addField("rosterDescription", rosterDescTextArea);	
		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_confirmButton"));
		okButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 7131769482151866097L;

			public void buttonClick(ClickEvent event) {				
				editRosterDataForm.setValidationVisible(true);
				editRosterDataForm.setComponentError(null);
	            boolean validateResult = editRosterDataForm.isValid();		            
	            if(validateResult){  
	            	String newRosterDisplayName=rosterDisplayNameField.getValue().toString();
					String newRosterDesc=rosterDescTextArea.getValue().toString();													
					doUpdateRoster(newRosterDisplayName,newRosterDesc);	            	    	
	            }	            		           
			}
		  });
		
	    Button cancelAddbutton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_cancelButton"),new Button.ClickListener() {
			private static final long serialVersionUID = -1765875679076434059L;

			public void buttonClick(ClickEvent event) {               
            	removeEditRosterWindow();
            }
        });
	    cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	    
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar editRoleQueueButtonBar = new BaseButtonBar(300, 30, Alignment.BOTTOM_LEFT, buttonList);
	    editRosterDataForm.getLayout().addComponent(editRoleQueueButtonBar);
	    windowContent.addComponent(editRosterDataForm);
	    String windowTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_windowTitle");
		String subTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRoster_windowDesc")+" "+" <span style='color:#ce0000; font-weight:bold;'>"+this.roster.getRosterName()+"</span>";
		editRosterWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle,subTitle, windowContent);		
		this.getApplication().getMainWindow().addWindow(editRosterWindow);		
	}
	
	private void removeEditRosterWindow(){
		this.getApplication().getMainWindow().removeWindow(editRosterWindow);
		editRosterWindow=null;
	}
	
	private void doUpdateRoster(String rosterDisp,String rosterDesc){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.roster.getActivitySpaceName());
		try {
			this.roster=activitySpace.updateRoster(this.roster.getRosterName(), rosterDisp, rosterDesc);
			this.rosterPropertyList.reloadContent(this.roster);			
			this.userClientInfo.getEventBlackboard().fire(new RostersChangeEvent(this.roster.getActivitySpaceName()));
			String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRosterSucess_msg1")+" "+
					this.roster.getRosterName()+"  "+
					userClientInfo.getI18NProperties().getProperty("ActivityManage_rosterUI_updateRosterSucess_msg2");
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
			removeEditRosterWindow();			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}
	
	private void renderBusinessActiviesUI(){
		businessActivitiesUIContainer.removeAllComponents();		
		BusinessActivitiesTable businessActivitiesTable=new BusinessActivitiesTable(this.roster,this.userClientInfo);		
		businessActivitiesUIContainer.addComponent(businessActivitiesTable);		
	}
}