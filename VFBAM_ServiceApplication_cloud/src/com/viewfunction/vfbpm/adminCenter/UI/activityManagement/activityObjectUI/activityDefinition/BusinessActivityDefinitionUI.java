package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

import java.util.ArrayList;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ExposedDataFieldsEditor;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.StartNewActivityEditor;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.MainTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
public class BusinessActivityDefinitionUI extends VerticalLayout{
	private static final long serialVersionUID = -3281364760965585160L;

	public BusinessActivityDefinition businessActivityDefinition;
	private UserClientInfo userClientInfo;
	public Button commitDefinitionChangeButton;
	
	// paramters for update BusinessActivityDefinition
	private boolean activityDefinitionModified=false;
	public Roster newRoster;
	public boolean newStatus;	
	private HorizontalLayout savePropertyChangeNofityContainer;
	
	private ExposedDataFieldsEditor exposedDataFieldsEditor;
	private BusinessActivityDefinitionPropertyList businessActivityDefinitionPropertyList;
	private ActivityStepsEditor activityStepsEditor;
	
	private VerticalLayout definationContainer;
	private VerticalLayout stepDataContainer;
	private VerticalLayout dataFieldsContainer;
	
	private DialogWindow setpropertyWindow;
	
	public BusinessActivityDefinitionUI(final BusinessActivityDefinition businessActivityDefinition, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail){		
		this.businessActivityDefinition=businessActivityDefinition;
		this.userClientInfo=userClientInfo;
		this.newStatus=businessActivityDefinition.isEnabled();
		//render title bar		
		Button parentObjectLinkButton=new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_businessActivityDefinitionsLabel"));
		parentObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);		
		parentObjectLinkButton.addListener(new ClickListener(){ 	
			private static final long serialVersionUID = 8129190112517177662L;

			public void buttonClick(ClickEvent event) {				
				renderBusinessActivityDefinitionsUI(businessActivityDefinition.getActivitySpaceName());
			}}); 		
		
		MainTitleBar mainTitleBar=new MainTitleBar(businessActivityDefinition.getActivitySpaceName(),parentObjectLinkButton,businessActivityDefinition.getActivityType());
		this.addComponent(mainTitleBar);
		
		//render BusinessActivityDefinition data		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);				
						
		// header part		
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceActivityDefineIcon);		
		HorizontalLayout businessActivityDefinitionActionbarContainer=new HorizontalLayout();			
		businessActivityDefinitionActionbarContainer.setWidth("130px");	
		
		Button reloadDefinitionButton=new Button();
		reloadDefinitionButton.setCaption(null);
		reloadDefinitionButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceRefreshActivityDefineIcon);
		reloadDefinitionButton.setStyleName(BaseTheme.BUTTON_LINK);
		reloadDefinitionButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_reloadDefineButton"));	
		reloadDefinitionButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 2564478283994453350L;

			public void buttonClick(ClickEvent event) {							
				refreshCrrentActivityDefinition(null);
			}});	
		businessActivityDefinitionActionbarContainer.addComponent(reloadDefinitionButton);
		
		HorizontalLayout divLayout=new HorizontalLayout();
		divLayout.setWidth("10px");
		businessActivityDefinitionActionbarContainer.addComponent(divLayout);
		
		Button saveDefinitionChangeButton=new Button();
		saveDefinitionChangeButton.setCaption(null);
		saveDefinitionChangeButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		saveDefinitionChangeButton.setStyleName(BaseTheme.BUTTON_LINK);
		saveDefinitionChangeButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_updateDefineButton"));		
		businessActivityDefinitionActionbarContainer.addComponent(saveDefinitionChangeButton);	
		
		commitDefinitionChangeButton=new Button();
		commitDefinitionChangeButton.setCaption(null);
		commitDefinitionChangeButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceCommitActivityDefineChangeIcon);
		commitDefinitionChangeButton.setStyleName(BaseTheme.BUTTON_LINK);
		commitDefinitionChangeButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_commitDefineChangeButton"));
		commitDefinitionChangeButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 2564478283994453350L;

			public void buttonClick(ClickEvent event) {							
				commitBusinessActivityDefinitionChange();
			}});	
		commitDefinitionChangeButton.setEnabled(false);
		businessActivityDefinitionActionbarContainer.addComponent(commitDefinitionChangeButton);		
		
		Button startNewActivityButton=new Button();
		startNewActivityButton.setCaption(null);
		startNewActivityButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceStartActivity_lightIcon);
		startNewActivityButton.setStyleName(BaseTheme.BUTTON_LINK);
		startNewActivityButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_startActivityButton"));
		startNewActivityButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = 2564478283994453350L;

			public void buttonClick(ClickEvent event) {							
				renderStartNewActivityUI();
			}});	
		
		businessActivityDefinitionActionbarContainer.addComponent(startNewActivityButton);		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,businessActivityDefinition.getActivityType(),SectionTitleBar.BIGFONT,businessActivityDefinitionActionbarContainer);
		containerPanel.addComponent(sectionTitleBar);			
		
		definationContainer=new VerticalLayout();			
		HorizontalLayout basicPropertyContainer=new HorizontalLayout();		
		basicPropertyContainer.setWidth("100%");
		basicPropertyContainer.setHeight("20px");
		basicPropertyContainer.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_EditorActionContainer);		
		HorizontalLayout propertyChangeMessageContainer=new HorizontalLayout();	
		HorizontalLayout divSpace_0=new HorizontalLayout();
		divSpace_0.setWidth("10px");
		propertyChangeMessageContainer.addComponent(divSpace_0);	
		Label propertiesLabel=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_activityDefinePropertyLabel"));
		propertyChangeMessageContainer.addComponent(propertiesLabel);		
		savePropertyChangeNofityContainer=new HorizontalLayout();
		HorizontalLayout divSpace=new HorizontalLayout();
		divSpace.setWidth("10px");
		Embedded infoIcon=new Embedded(null, UICommonElementDefination.AppPanel_InfoActionbarIcon);
		Label saveDataNotification=new Label("<span style='color:#2779c7;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_commitChangePromptLabel")+"</span>", Label.CONTENT_XHTML);
		savePropertyChangeNofityContainer.addComponent(divSpace);
		savePropertyChangeNofityContainer.addComponent(infoIcon);
		savePropertyChangeNofityContainer.addComponent(saveDataNotification);		
		propertyChangeMessageContainer.addComponent(savePropertyChangeNofityContainer);	
		savePropertyChangeNofityContainer.setVisible(false);					
		basicPropertyContainer.addComponent(propertyChangeMessageContainer);
		basicPropertyContainer.setComponentAlignment(propertyChangeMessageContainer, Alignment.MIDDLE_LEFT);		
		definationContainer.addComponent(basicPropertyContainer);		
		businessActivityDefinitionPropertyList=new BusinessActivityDefinitionPropertyList(this.businessActivityDefinition,this.userClientInfo,this);		
		definationContainer.addComponent(businessActivityDefinitionPropertyList);		
		containerPanel.addComponent(definationContainer);		
		
		HorizontalLayout divHorizontalLayout=new HorizontalLayout();
		divHorizontalLayout.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout);	
		
		Embedded stepsIcon=new Embedded(null, UICommonElementDefination.ICON_activityStep_light);
		HorizontalLayout businessActivityStepActionbarContainer=new HorizontalLayout();			
		businessActivityStepActionbarContainer.setWidth("60px");	
		Button addStepButton=new Button();
		addStepButton.setCaption(null);
		addStepButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceAddActivityStepIcon);
		addStepButton.setStyleName(BaseTheme.BUTTON_LINK);
		addStepButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addNewStepButton"));	
		addStepButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 5270170576001984511L;

			public void buttonClick(ClickEvent event) {							
				renderAddStepUI();
			}});	
		businessActivityStepActionbarContainer.addComponent(addStepButton);
		Button removeStepsButton=new Button();
		removeStepsButton.setCaption(null);
		removeStepsButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceRemoveActivityStepIcon);
		removeStepsButton.setStyleName(BaseTheme.BUTTON_LINK);
		removeStepsButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_removeStepsButton"));	
		removeStepsButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 6777221688891374822L;

			public void buttonClick(ClickEvent event) {							
				renderRemoveStepsUI();
			}});	
		businessActivityStepActionbarContainer.addComponent(removeStepsButton);		
		
		SectionTitleBar stepsSectionTitleBar=new SectionTitleBar(stepsIcon,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_defineStepsLabel"),SectionTitleBar.MIDDLEFONT,businessActivityStepActionbarContainer);		
		containerPanel.addComponent(stepsSectionTitleBar);		
		stepDataContainer=new VerticalLayout();
		containerPanel.addComponent(stepDataContainer);
		activityStepsEditor=new ActivityStepsEditor(this.businessActivityDefinition.getActivitySpaceName(),this.businessActivityDefinition,this.userClientInfo,this);
		stepDataContainer.addComponent(activityStepsEditor);		
		
		HorizontalLayout divHorizontalLayout_2=new HorizontalLayout();
		divHorizontalLayout_2.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout_2);	
		
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields_light);
		SectionTitleBar datafieldSectionTitleBar=new SectionTitleBar(expDataFieldsIcon,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_defineDatafieldsLabel"),SectionTitleBar.MIDDLEFONT,null);		
		containerPanel.addComponent(datafieldSectionTitleBar);
		
		dataFieldsContainer=new VerticalLayout();
		containerPanel.addComponent(dataFieldsContainer);
		DataFieldDefinition[] dataFieldDefinations=null;		
		dataFieldDefinations=this.businessActivityDefinition.getActivityDataFields();			
		exposedDataFieldsEditor=new ExposedDataFieldsEditor(this.businessActivityDefinition,dataFieldDefinations,userClientInfo,false,this);
		dataFieldsContainer.addComponent(exposedDataFieldsEditor);		
	}
	
	public void renderBusinessActivityDefinitionsUI(String activitySpaceName){
		ActivityObjectDetail activityObjectDetail =(ActivityObjectDetail)(this.getParent().getParent());
		activityObjectDetail.renderBusinessActivityDefinitionsUI(activitySpaceName);
	}
	
	private void renderStartNewActivityUI(){		
		StartNewActivityEditor startNewActivityForm=new StartNewActivityEditor(this.businessActivityDefinition,userClientInfo);	
		Embedded startActivityIcon=new Embedded(null, UICommonElementDefination.ICON_userClient_Activities);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_startActivityWindowTitle")+" "				
				+"</b> <b style='color:#ce0000;'>" + businessActivityDefinition.getActivityType()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(startActivityIcon,propertyNameLable,startNewActivityForm,"1000px");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	public void activityDefinitionPropertyModified(){
		if(!this.activityDefinitionModified){
			this.activityDefinitionModified=true;			
			commitDefinitionChangeButton.setEnabled(true);			
		}
		savePropertyChangeNofityContainer.setVisible(true);
	}
	
	public void activityDefinitionStepDataModified(){
		if(!this.activityDefinitionModified){
			this.activityDefinitionModified=true;			
			commitDefinitionChangeButton.setEnabled(true);			
		}		
	}
	
	private void commitBusinessActivityDefinitionChange(){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.businessActivityDefinition.getActivitySpaceName());			
		//need remove this activityType from roster,if not roster still point to old definition content object 		
		String oldRosterName=this.businessActivityDefinition.getRosterName();
		Roster oldRoster=null;
		try {
			if(oldRosterName!=null){
				oldRoster=activitySpace.getRoster(oldRosterName);
				oldRoster.removeActivityType(this.businessActivityDefinition.getActivityType());						
			}
		} catch (ActivityEngineRuntimeException e) {					
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {						
			e.printStackTrace();
		}		
		//set activityType dtaFieldDefinations
		DataFieldDefinition[] newDataFieldDefinitions=exposedDataFieldsEditor.getDataFieldDefinition();			
		//need check new datafield with StepProcessVariable and step datafields
		
		this.businessActivityDefinition.resetActivityDataFields(newDataFieldDefinitions);	
		//other data are set in BusinessActivityDefinitionPropertyList and ActivityStepsEditor
		try {
			boolean result=activitySpace.updateBusinessActivityDefinition(businessActivityDefinition);			
			if(result){
				//setRoster
				if(this.newRoster!=null){
					this.newRoster.addActivityType(this.businessActivityDefinition.getActivityType());									
				}else{
					if(oldRoster!=null){
						oldRoster.addActivityType(this.businessActivityDefinition.getActivityType());						
					}
				}
				//setStatus
				if(this.newStatus!=this.businessActivityDefinition.isEnabled()){				
					try {
						if(!this.newStatus){
							activitySpace.disableBusinessActivityDefinition(this.businessActivityDefinition.getActivityType());
						}else{
							activitySpace.enableBusinessActivityDefinition(this.businessActivityDefinition.getActivityType());
						}				
					} catch (ActivityEngineRuntimeException e) {				
						e.printStackTrace();
					} catch (ActivityEngineActivityException e) {				
						e.printStackTrace();
					}		
				}				
				String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_activityUpdateSuccessMsg1")+
						" "+this.businessActivityDefinition.getActivityType()+" "+
						this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_activityUpdateSuccessMsg2");
				getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);	
				commitDefinitionChangeButton.setEnabled(false);			
				this.userClientInfo.getEventBlackboard().fire(new ActivityDefinitionsChangeEvent(this.businessActivityDefinition.getActivitySpaceName()));	
				refreshCrrentActivityDefinition(this.businessActivityDefinition);
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		}
	}
	
	private void refreshCrrentActivityDefinition(BusinessActivityDefinition businessActivityDefinition){
		if(businessActivityDefinition==null){
			ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.businessActivityDefinition.getActivitySpaceName());
			try {
				this.businessActivityDefinition=activitySpace.getBusinessActivityDefinition(this.businessActivityDefinition.getActivityType());
			} catch (ActivityEngineRuntimeException e) {			
				e.printStackTrace();
			} catch (ActivityEngineActivityException e) {			
				e.printStackTrace();
			} catch (ActivityEngineDataException e) {			
				e.printStackTrace();
			}			
		}
		this.activityDefinitionModified=false;
		commitDefinitionChangeButton.setEnabled(false);
		savePropertyChangeNofityContainer.setVisible(false);
		
		definationContainer.removeComponent(businessActivityDefinitionPropertyList);
		businessActivityDefinitionPropertyList=null;
		businessActivityDefinitionPropertyList=new BusinessActivityDefinitionPropertyList(this.businessActivityDefinition,this.userClientInfo,this);	
		definationContainer.addComponent(businessActivityDefinitionPropertyList);
		
		stepDataContainer.removeComponent(activityStepsEditor);
		activityStepsEditor=null;
		activityStepsEditor=new ActivityStepsEditor(this.businessActivityDefinition.getActivitySpaceName(),this.businessActivityDefinition,this.userClientInfo,this);
		stepDataContainer.addComponent(activityStepsEditor);
		
		dataFieldsContainer.removeComponent(exposedDataFieldsEditor);	
		exposedDataFieldsEditor=null;
		DataFieldDefinition[] dataFieldDefinations=null;
		dataFieldDefinations=this.businessActivityDefinition.getActivityDataFields();			
		exposedDataFieldsEditor=new ExposedDataFieldsEditor(this.businessActivityDefinition,dataFieldDefinations,userClientInfo,false,this);
		dataFieldsContainer.addComponent(exposedDataFieldsEditor);		
	}
	
	private void removeSetPropertyWindow(){
		this.getApplication().getMainWindow().removeWindow(setpropertyWindow);
		setpropertyWindow=null;
	}
	
	private void renderAddStepUI(){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStepsWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStepsWindowDesc")+
        		" <span style='color:#ce0000;font-weight:bold;'>"+this.businessActivityDefinition.getActivityType()+"</span>";		
        VerticalLayout setStatusLayout = new VerticalLayout();        
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);		
		final TextField stepNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStep_stepNameField"));	
		stepNameField.setWidth("250px");
		stepNameField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStep_stepNameInputPrompt"));	
		inputFieldListLayout.addComponent(stepNameField);
		
		HorizontalLayout inputFieldListLayout2=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout2=new HorizontalLayout();	
		leftSpaceLayout2.setWidth("20px");
		inputFieldListLayout2.addComponent(leftSpaceLayout2);			
		final ComboBox rolsChoise = new ComboBox(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStep_stepRoleField"));
		rolsChoise.setNullSelectionAllowed(true);			
		rolsChoise.setWidth("250px");
		rolsChoise.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStep_stepRoleInputPrompt"));		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.businessActivityDefinition.getActivitySpaceName());
		Role[] rolesArray=null;
		try {
			rolesArray = activitySpace.getRoles();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		final IndexedContainer roleContainer = new IndexedContainer();
		roleContainer.addContainerProperty("ROLE_DISPLAYNAME", String.class, null);
		roleContainer.addContainerProperty("ROLE", Role.class, null);
		if(rolesArray!=null){
			for(int i=0;i<rolesArray.length;i++){
				Item item = roleContainer.addItem(""+i);	
				item.getItemProperty("ROLE_DISPLAYNAME").setValue(rolesArray[i].getDisplayName()+ "( "+rolesArray[i].getRoleName()+" )");
		        item.getItemProperty("ROLE").setValue(rolesArray[i]);							
			}
		}		
		rolsChoise.setContainerDataSource(roleContainer);
		rolsChoise.setItemCaptionPropertyId("ROLE_DISPLAYNAME");
		rolsChoise.setEnabled(true);					
		inputFieldListLayout2.addComponent(rolsChoise);		
		
		HorizontalLayout spaceDiv_1=new HorizontalLayout();
	    spaceDiv_1.setHeight("10px");
	    setStatusLayout.addComponent(spaceDiv_1);
		setStatusLayout.addComponent(inputFieldListLayout);			
		HorizontalLayout spaceDiv_2=new HorizontalLayout();
	    spaceDiv_2.setHeight("10px");
	    setStatusLayout.addComponent(spaceDiv_2);	        
		setStatusLayout.addComponent(inputFieldListLayout2);	
		
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStep_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -2447829988740854917L;
			
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if(stepNameField.getValue()==null||stepNameField.getValue().toString().equals("")){
					return;
				}else{
					String stepName=stepNameField.getValue().toString();
					String stepRole=null;
					if(rolsChoise.getValue()!=null&&!rolsChoise.getValue().toString().equals("")){
						Item selecteItem=roleContainer.getItem(rolsChoise.getValue());
						Role selectedRole=(Role)selecteItem.getItemProperty("ROLE").getValue();						
						stepRole=selectedRole.getRoleName();
					}	
					doAddActivityStep(stepName,stepRole);
				}				
			}			
	     }); 		
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStep_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 6982340420306548515L;

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
	
	private void doAddActivityStep(String stepName,String roleName){
		String[] currentSteps=this.businessActivityDefinition.getExposedSteps();
		if(alreadyHasStepName(stepName,currentSteps)){
			return;
		}else{
			String[] newStepArray=new String[currentSteps.length+1];
			for(int i=0;i<currentSteps.length;i++){
				newStepArray[i]=currentSteps[i];				
			}
			newStepArray[currentSteps.length]=stepName;			
			this.businessActivityDefinition.setExposedSteps(newStepArray);
			if(roleName!=null){
				try {
					this.businessActivityDefinition.setActivityStepRelatedRole(stepName, roleName);
				} catch (ActivityEngineProcessException e) {					
					e.printStackTrace();
				}
			}						
			stepDataContainer.removeComponent(activityStepsEditor);
			activityStepsEditor=null;
			activityStepsEditor=new ActivityStepsEditor(this.businessActivityDefinition.getActivitySpaceName(),this.businessActivityDefinition,this.userClientInfo,this);
			stepDataContainer.addComponent(activityStepsEditor);
			activityDefinitionStepDataModified();		
			activityStepsEditor.saveStepPropertyChangeNofityContainer.setVisible(true);
			removeSetPropertyWindow();
		}		
	}
	
	private boolean alreadyHasStepName(String stepName,Object[] stepsArray){
		if(stepsArray==null){
			return false;
		}
		for(Object currentField:stepsArray){
			if(currentField.toString().equals(stepName)){
				return true;
			}
		}		
		return false;
	}
	
	private void renderRemoveStepsUI(){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_removeStepsWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_removeStepsWindowDesc")+
        		" <span style='color:#ce0000;font-weight:bold;'>"+this.businessActivityDefinition.getActivityType()+"</span>";
        VerticalLayout setStatusLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);
		
		final OptionGroup stepsSelect = new OptionGroup("");
		stepsSelect.setMultiSelect(true);
		stepsSelect.setNullSelectionAllowed(true);		
		String[] currentSteps=this.businessActivityDefinition.getExposedSteps();
		for(String currentStep:currentSteps){
			stepsSelect.addItem(currentStep);			
		}
		inputFieldListLayout.addComponent(stepsSelect);		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_removeStep_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 3363934978538562030L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {				
				Set selectedDataFieldIdxSet=(Set)stepsSelect.getValue();				
				Object[] stepArry=selectedDataFieldIdxSet.toArray();
				doRemoveActivitySteps(stepArry);				
			}
	     });  				
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_removeStep_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -2454826452548387402L;

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
	
	private void doRemoveActivitySteps(Object[] steps){
		String[] currentSteps=this.businessActivityDefinition.getExposedSteps();		
		List<String> leftStepsList=new ArrayList<String>();		
		for(String oldStep:currentSteps){
			if(!alreadyHasStepName(oldStep,steps)){
				leftStepsList.add(oldStep);				
			}
		}				
		Map<String, String> stepRoleMap=this.businessActivityDefinition.getActivityStepRoleMap();
		Map<String, DataFieldDefinition[]> stepDataFieldMap=this.businessActivityDefinition.getActivityStepsExposedDataField();		
		for(Object step:steps){
			String stepName=step.toString();
			stepRoleMap.remove(stepName);
			stepDataFieldMap.remove(stepName);
			try {
				this.businessActivityDefinition.setStepDecisionPointAttributeName(stepName, null);
				this.businessActivityDefinition.setStepDecisionPointChoiseList(stepName, null);
				this.businessActivityDefinition.setStepProcessVariableList(stepName, null);
				this.businessActivityDefinition.setStepUserIdentityAttributeName(stepName, null);
			} catch (ActivityEngineDataException e) {				
				e.printStackTrace();
			}		
		}
		String[] leftStepsArray=new String[leftStepsList.size()];
		leftStepsList.toArray(leftStepsArray);
		this.businessActivityDefinition.setExposedSteps(leftStepsArray);
		
		stepDataContainer.removeComponent(activityStepsEditor);
		activityStepsEditor=null;
		activityStepsEditor=new ActivityStepsEditor(this.businessActivityDefinition.getActivitySpaceName(),this.businessActivityDefinition,this.userClientInfo,this);
		stepDataContainer.addComponent(activityStepsEditor);
		activityDefinitionStepDataModified();		
		activityStepsEditor.saveStepPropertyChangeNofityContainer.setVisible(true);
		removeSetPropertyWindow();		
	}
}