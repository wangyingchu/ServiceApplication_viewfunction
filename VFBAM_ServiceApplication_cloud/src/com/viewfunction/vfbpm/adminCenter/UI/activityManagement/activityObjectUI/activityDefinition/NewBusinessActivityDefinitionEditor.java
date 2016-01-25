package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class NewBusinessActivityDefinitionEditor extends VerticalLayout{
	private static final long serialVersionUID = -3848881164684627231L;

	private UserClientInfo userClientInfo;
	private String activitySpaceName;	

	private Panel containerPanel;
	public Label activityTypeLabel;
	public Label activityDefinitionResourceFileLabel;
	private Label launchDecisionPointAttributeNameLabel;
	private Label launchUserIdentityAttributeNameLabel;
	//private Label launchProcessVariableListLabel;
	private Label launchDecisionPointChoiseListLabel;
	private DialogWindow setpropertyWindow;
	
	//BusinessActivityDefinition data
	public String activityType;
	public File activityDefinitionResourceFile;
	public String launchDecisionPointAttributeName;
	public String launchUserIdentityAttributeName;
	public String[] launchDecisionPointChoiseList;
	
	public NewBusinessActivityDefinitionEditor(String activitySpaceName,UserClientInfo userClientInfo){	
		this.activitySpaceName=activitySpaceName;
		this.userClientInfo=userClientInfo;			
		
		containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		VerticalLayout containPanelLayot=((VerticalLayout)containerPanel.getContent());		
		containPanelLayot.setMargin(false);
		containPanelLayot.setSpacing(false);		
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		
		containerPanel.addComponent(new Label(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_activityTypePropertiesLabel")));		

		activityTypeLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_activityTypeNamePropertyLabel"),activityTypeLabel,null);
		containerPanel.addComponent(namePropertyItem);			
		
		HorizontalLayout activityDefinitionResourceActionContainer=new HorizontalLayout();		
		activityDefinitionResourceActionContainer.setWidth("30px");		
		Button editActivityDefinitionResourceButton=new Button();		
		editActivityDefinitionResourceButton.setCaption(null);
		editActivityDefinitionResourceButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editActivityDefinitionResourceButton.setStyleName(BaseTheme.BUTTON_LINK);
		editActivityDefinitionResourceButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));
		editActivityDefinitionResourceButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -7841644286619493335L;

			public void buttonClick(ClickEvent event) {				
				renderSetDefinitionResourceFileUI();				
			}});
		activityDefinitionResourceActionContainer.addComponent(editActivityDefinitionResourceButton);			
		activityDefinitionResourceFileLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem resourcePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_activityBPMNFilePropertyLabel"),activityDefinitionResourceFileLabel,activityDefinitionResourceActionContainer);
		containerPanel.addComponent(resourcePropertyItem);
		
		HorizontalLayout launchDecisionPointAttributeActionContainer=new HorizontalLayout();		
		launchDecisionPointAttributeActionContainer.setWidth("60px");		
		Button editLaunchDecisionPointAttributeNameButton=new Button();		
		editLaunchDecisionPointAttributeNameButton.setCaption(null);
		editLaunchDecisionPointAttributeNameButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editLaunchDecisionPointAttributeNameButton.setStyleName(BaseTheme.BUTTON_LINK);
		editLaunchDecisionPointAttributeNameButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));
		editLaunchDecisionPointAttributeNameButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = -7810588571177712500L;

			public void buttonClick(ClickEvent event) {				
				renderSetLaunchDecisionPointAttributeNameUI();				
			}});
		launchDecisionPointAttributeActionContainer.addComponent(editLaunchDecisionPointAttributeNameButton);			
		Button deleteDecisionPointAttributeNameButton=new Button();		
		deleteDecisionPointAttributeNameButton.setCaption(null);
		deleteDecisionPointAttributeNameButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deleteDecisionPointAttributeNameButton.setStyleName(BaseTheme.BUTTON_LINK);
		deleteDecisionPointAttributeNameButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_deleteButtonDescLabel"));
		deleteDecisionPointAttributeNameButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = -4698416239435738268L;

			public void buttonClick(ClickEvent event) {				
				doDeleteLaunchDecisionPointAttributeName();				
			}});
		launchDecisionPointAttributeActionContainer.addComponent(deleteDecisionPointAttributeNameButton);		
		launchDecisionPointAttributeNameLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem launchDecisionPointAttributeNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_launchDecisionPointPropertyLabel"),launchDecisionPointAttributeNameLabel,launchDecisionPointAttributeActionContainer);		
		containerPanel.addComponent(launchDecisionPointAttributeNamePropertyItem);	
		
		HorizontalLayout launchDecisionPointChoiseListActionContainer=new HorizontalLayout();		
		launchDecisionPointChoiseListActionContainer.setWidth("60px");		
		Button editlaunchDecisionPointChoiseListButton=new Button();		
		editlaunchDecisionPointChoiseListButton.setCaption(null);
		editlaunchDecisionPointChoiseListButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editlaunchDecisionPointChoiseListButton.setStyleName(BaseTheme.BUTTON_LINK);
		editlaunchDecisionPointChoiseListButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));	
		editlaunchDecisionPointChoiseListButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = -3767090670596999318L;

			public void buttonClick(ClickEvent event) {				
				renderSetlaunchDecisionPointChoiseListUI();				
			}});
		launchDecisionPointChoiseListActionContainer.addComponent(editlaunchDecisionPointChoiseListButton);	
		Button deletelaunchDecisionPointChoiseListButton=new Button();		
		deletelaunchDecisionPointChoiseListButton.setCaption(null);
		deletelaunchDecisionPointChoiseListButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deletelaunchDecisionPointChoiseListButton.setStyleName(BaseTheme.BUTTON_LINK);
		deletelaunchDecisionPointChoiseListButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_deleteButtonDescLabel"));	
		deletelaunchDecisionPointChoiseListButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = -5826322098636700146L;

			public void buttonClick(ClickEvent event) {				
				doDeleteDecisionPointChoiseList();				
			}});
		launchDecisionPointChoiseListActionContainer.addComponent(deletelaunchDecisionPointChoiseListButton);		
			
		launchDecisionPointChoiseListLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem launchDecisionPointChoiseListPropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_launchDecisionChoiseListPropertyLabel"),launchDecisionPointChoiseListLabel,launchDecisionPointChoiseListActionContainer);		
		containerPanel.addComponent(launchDecisionPointChoiseListPropertyItem);
		/*
		HorizontalLayout launchProcessVariableListActionContainer=new HorizontalLayout();		
		launchProcessVariableListActionContainer.setWidth("60px");		
		Button editLaunchProcessVariableListActionContainerButton=new Button();		
		editLaunchProcessVariableListActionContainerButton.setCaption(null);
		editLaunchProcessVariableListActionContainerButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editLaunchProcessVariableListActionContainerButton.setStyleName(BaseTheme.BUTTON_LINK);
		editLaunchProcessVariableListActionContainerButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));		
		editLaunchProcessVariableListActionContainerButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = 7768582432731394619L;

			public void buttonClick(ClickEvent event) {				
				//renderSetLaunchProcessVariableListUI();				
			}});
		launchProcessVariableListActionContainer.addComponent(editLaunchProcessVariableListActionContainerButton);
		Button deletelaunchProcessVariableListButton=new Button();		
		deletelaunchProcessVariableListButton.setCaption(null);
		deletelaunchProcessVariableListButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deletelaunchProcessVariableListButton.setStyleName(BaseTheme.BUTTON_LINK);
		deletelaunchProcessVariableListButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_deleteButtonDescLabel"));	
		deletelaunchProcessVariableListButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = -7273424244388197981L;

			public void buttonClick(ClickEvent event) {				
				//doDeleteLaunchProcessVariable();				
			}});
		launchProcessVariableListActionContainer.addComponent(deletelaunchProcessVariableListButton);					
		launchProcessVariableListLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>" ,Label.CONTENT_XHTML);		
		PropertyItem launchProcessVariableListPropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,"Launch Process Variable List",launchProcessVariableListLabel,launchProcessVariableListActionContainer);		
		containerPanel.addComponent(launchProcessVariableListPropertyItem);
		*/
		HorizontalLayout launchUserIdentityAttributeNameActionContainer=new HorizontalLayout();		
		launchUserIdentityAttributeNameActionContainer.setWidth("60px");		
		Button editlaunchUserIdentityAttributeNameActionContainerButton=new Button();		
		editlaunchUserIdentityAttributeNameActionContainerButton.setCaption(null);
		editlaunchUserIdentityAttributeNameActionContainerButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editlaunchUserIdentityAttributeNameActionContainerButton.setStyleName(BaseTheme.BUTTON_LINK);
		editlaunchUserIdentityAttributeNameActionContainerButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));		
		editlaunchUserIdentityAttributeNameActionContainerButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = 4596525308240163248L;

			public void buttonClick(ClickEvent event) {				
				renderSetLaunchUserIdentityAttributeNameUI();				
			}});
		launchUserIdentityAttributeNameActionContainer.addComponent(editlaunchUserIdentityAttributeNameActionContainerButton);
		Button deletelaunchUserIdentityAttributeNameButton=new Button();		
		deletelaunchUserIdentityAttributeNameButton.setCaption(null);
		deletelaunchUserIdentityAttributeNameButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deletelaunchUserIdentityAttributeNameButton.setStyleName(BaseTheme.BUTTON_LINK);
		deletelaunchUserIdentityAttributeNameButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_deleteButtonDescLabel"));
		deletelaunchUserIdentityAttributeNameButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 4355310314068741413L;

			public void buttonClick(ClickEvent event) {				
				doDeleteLaunchUserIdentityAttributeName();				
			}});
		launchUserIdentityAttributeNameActionContainer.addComponent(deletelaunchUserIdentityAttributeNameButton);		
		launchUserIdentityAttributeNameLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem launchUserIdentityAttributeNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_launchUserIDPropertyLabel"),launchUserIdentityAttributeNameLabel,launchUserIdentityAttributeNameActionContainer);	
		containerPanel.addComponent(launchUserIdentityAttributeNamePropertyItem);		
		
		//containerPanel.addComponent(new Label("Activity Steps Properties:"));		
		//containerPanel.addComponent(new Label("Activity Datafields Properties:"));		
		this.addComponent(containerPanel);
	}	
	
	private void renderSetDefinitionResourceFileUI(){
		VerticalLayout setStatusLayout = new VerticalLayout();
		setStatusLayout.addComponent(new ActivityDefinitionResourceFileUploader(this.activitySpaceName,this.userClientInfo,this));		        
		String windowTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setActivityTypeNameWindowTitle");
		String windowDesc =userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setActivityTypeNameWindowDesc")+" <span style='color:#ce0000;font-weight:bold;'>"+this.activitySpaceName+"</span>";
		setpropertyWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, 550,setStatusLayout);
        setpropertyWindow.setResizable(false);
        setpropertyWindow.center();
        setpropertyWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(setpropertyWindow);	
	}
	
	private void removeSetPropertyWindow(){
		this.getApplication().getMainWindow().removeWindow(setpropertyWindow);
		setpropertyWindow=null;
	}
	
	private void renderSetLaunchDecisionPointAttributeNameUI(){		
		String windowTitle =userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchDesicPointWindowTitle");
        String windowDesc =userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchDesicPointWindowDesc");
        VerticalLayout setStatusLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);
		final TextField attributeName=new TextField();
		attributeName.setWidth("250px");
		attributeName.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_launchDesicPointInputFieldPrompt"));
		inputFieldListLayout.addComponent(attributeName);
		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchDesicPoint_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 4396510812966384308L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if(attributeName.getValue()==null){
					return;
				}
				String launchDecisionPointAttributeName=attributeName.getValue().toString();				
				doSetLaunchDecisionPointAttributeName(launchDecisionPointAttributeName);				
			}
	     });  
	    
	    Button cancelButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchDesicPoint_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = 720552753574674375L;

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
	
	private void doSetLaunchDecisionPointAttributeName(String launchDecisionPointAttributeName){
		this.launchDecisionPointAttributeNameLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+launchDecisionPointAttributeName+"</span>");
		removeSetPropertyWindow();
		this.launchDecisionPointAttributeName=launchDecisionPointAttributeName;
	}
	
	private void doDeleteLaunchDecisionPointAttributeName(){
		this.launchDecisionPointAttributeNameLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
		this.launchDecisionPointAttributeName=null;		
	}
	
	private void renderSetLaunchUserIdentityAttributeNameUI(){
		String windowTitle =userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchUserIdWindowTitle");
        String windowDesc =userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchUserIdWindowDesc");
        VerticalLayout setStatusLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);
		final TextField attributeName=new TextField();
		attributeName.setWidth("250px");
		attributeName.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_launchUserIdInputFieldPrompt"));							
		inputFieldListLayout.addComponent(attributeName);		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchUserId_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 4923111113858373175L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if(attributeName.getValue()==null){
					return;
				}
				String launchUserIdentityAttributeName=attributeName.getValue().toString();				
				doSetLaunchUserIdentityAttributeName(launchUserIdentityAttributeName);				
			}
	     });  
	    
	    Button cancelButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchUserId_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 8043110981603206697L;

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
	
	private void doSetLaunchUserIdentityAttributeName(String launchUserIdentityAttributeName){
		this.launchUserIdentityAttributeNameLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+launchUserIdentityAttributeName+"</span>");
		removeSetPropertyWindow();
		this.launchUserIdentityAttributeName=launchUserIdentityAttributeName;	
	}
	
	private void doDeleteLaunchUserIdentityAttributeName(){
		this.launchUserIdentityAttributeNameLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
		this.launchUserIdentityAttributeName=null;			
	}
	
	private void renderSetlaunchDecisionPointChoiseListUI(){
		String windowTitle =userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchDesicPointChoiseListWindowTitle");
        String windowDesc =userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchDesicPointChoiseListWindowDesc");
        VerticalLayout setDecisionPointChoiseListLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);			
		final DecisionPointChoiseListEditor decisionPointChoiseListEditor=new DecisionPointChoiseListEditor(null,this.userClientInfo);	
		inputFieldListLayout.addComponent(decisionPointChoiseListEditor);		
		setDecisionPointChoiseListLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchDesicPointChoiseList_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -6652245076982298227L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				boolean checkResult=decisionPointChoiseListEditor.validateDateField();
				if(checkResult){
					String[] choiseListValue=decisionPointChoiseListEditor.getChoistList();
					doSetDecisionPointChoiseList(choiseListValue);				
				}
			}
	     });  
	    
	    Button cancelButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_NewBusinessActivityDefinitionEditor_setLaunchDesicPointChoiseList_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -8583999612453600702L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
				removeSetPropertyWindow();
	           }
	     });	
	    cancelButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmButton);
        buttonList.add(cancelButton); 
        setDecisionPointChoiseListLayout.addComponent(new BaseButtonBar(120, 30, Alignment.MIDDLE_RIGHT, buttonList));
        setpropertyWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, 350,setDecisionPointChoiseListLayout);
        setpropertyWindow.setResizable(true);
        setpropertyWindow.center();
        setpropertyWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(setpropertyWindow);			
	}
	
	private void doSetDecisionPointChoiseList(String[] choiseListValue){
		this.launchDecisionPointChoiseListLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+formatStringArrayData(choiseListValue)+"</span>");
		removeSetPropertyWindow();	
		this.launchDecisionPointChoiseList=choiseListValue;
	}
	
	private void doDeleteDecisionPointChoiseList(){
		this.launchDecisionPointChoiseListLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
		this.launchDecisionPointChoiseList=null;
	}
	
	private String formatStringArrayData(String[] strArry){
		if(strArry==null||strArry.length==0){
			return "";
		}
		StringBuffer sb=new StringBuffer("");
		for(String currentString:strArry){
			sb.append(currentString);
			sb.append(";");
		}		
		return sb.toString();		
	}
}