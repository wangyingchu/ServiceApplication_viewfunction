package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
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
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class BusinessActivityDefinitionPropertyList extends VerticalLayout{
	private static final long serialVersionUID = 7985180001128836362L;
	
	private Panel containerPanel;	
	private UserClientInfo userClientInfo;
	private BusinessActivityDefinition businessActivityDefinition;
	private BusinessActivityDefinitionUI businessActivityDefinitionUI;
	private Label rosterNameLabel;
	private Label statusLabel;
	private Label launchDecisionPointAttributeNameLabel;
	private Label launchUserIdentityAttributeNameLabel;
	private Label launchProcessVariableListLabel;
	private Label launchDecisionPointChoiseListLabel;
	private DialogWindow setpropertyWindow;
	
	public BusinessActivityDefinitionPropertyList(BusinessActivityDefinition businessActivityDefinition,UserClientInfo userClientInfo,BusinessActivityDefinitionUI businessActivityDefinitionUI){
		this.businessActivityDefinition=businessActivityDefinition;
		this.userClientInfo=userClientInfo;
		this.businessActivityDefinitionUI=businessActivityDefinitionUI;		
		
		containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		VerticalLayout containPanelLayot=((VerticalLayout)containerPanel.getContent());		
		containPanelLayot.setMargin(false);
		containPanelLayot.setSpacing(false);		
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();			
		
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_activityTypeLabel"),this.businessActivityDefinition.getActivityType(),null);		
		containerPanel.addComponent(namePropertyItem);		
		HorizontalLayout resourcePropertyNameContainer=new HorizontalLayout();
		Button getResourceFileButton=new Button(businessActivityDefinition.getActivityType()+".bpmn20.xml");		
		getResourceFileButton.setStyleName(BaseTheme.BUTTON_LINK);
		getResourceFileButton.addListener(new ClickListener(){            		
			private static final long serialVersionUID = -3567064194516804327L;

			public void buttonClick(ClickEvent event) {	
				renderDefineFileContentUI();						
			}}); 
		resourcePropertyNameContainer.addComponent(getResourceFileButton);		
		PropertyItem resourcePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_activityDefinFileLabel"),null,resourcePropertyNameContainer,null);		
		containerPanel.addComponent(resourcePropertyItem);	
		
		HorizontalLayout rosterActionContainer=new HorizontalLayout();		
		rosterActionContainer.setWidth("30px");		
		Button editRosterButton=new Button();		
		editRosterButton.setCaption(null);
		editRosterButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editRosterButton.setStyleName(BaseTheme.BUTTON_LINK);
		editRosterButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));
		editRosterButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -6895387789660958811L;

			public void buttonClick(ClickEvent event) {				
				renderEditRosterUI();				
			}});		
		rosterActionContainer.addComponent(editRosterButton);		
		String rosterStr=this.businessActivityDefinition.getRosterName()!=null?this.businessActivityDefinition.getRosterName():"";		
		rosterNameLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+rosterStr+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem rosterNamePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_relatedRosterLabel"),rosterNameLabel,rosterActionContainer);		
		containerPanel.addComponent(rosterNamePropertyItem);	
		
		HorizontalLayout statusActionContainer=new HorizontalLayout();		
		statusActionContainer.setWidth("30px");		
		Button editStatusButton=new Button();		
		editStatusButton.setCaption(null);
		editStatusButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editStatusButton.setStyleName(BaseTheme.BUTTON_LINK);
		editStatusButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));	
		editStatusButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = -5866028763574533351L;

			public void buttonClick(ClickEvent event) {				
				renderSetActivityTypeStatusUI();				
			}});		
		statusActionContainer.addComponent(editStatusButton);
		statusLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+this.businessActivityDefinition.isEnabled()+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem activityStatusPropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_activityStatusLabel"),statusLabel,statusActionContainer);		
		containerPanel.addComponent(activityStatusPropertyItem);
		
		HorizontalLayout launchDecisionPointAttributeActionContainer=new HorizontalLayout();		
		launchDecisionPointAttributeActionContainer.setWidth("60px");		
		Button editLaunchDecisionPointAttributeNameButton=new Button();		
		editLaunchDecisionPointAttributeNameButton.setCaption(null);
		editLaunchDecisionPointAttributeNameButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editLaunchDecisionPointAttributeNameButton.setStyleName(BaseTheme.BUTTON_LINK);
		editLaunchDecisionPointAttributeNameButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));
		editLaunchDecisionPointAttributeNameButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = -4854867169341457366L;

			public void buttonClick(ClickEvent event) {				
				renderSetlaunchDecisionPointAttributeNameUI();				
			}});
		launchDecisionPointAttributeActionContainer.addComponent(editLaunchDecisionPointAttributeNameButton);			
		Button deleteDecisionPointAttributeNameButton=new Button();		
		deleteDecisionPointAttributeNameButton.setCaption(null);
		deleteDecisionPointAttributeNameButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deleteDecisionPointAttributeNameButton.setStyleName(BaseTheme.BUTTON_LINK);
		deleteDecisionPointAttributeNameButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_deleteButtonDescLabel"));
		deleteDecisionPointAttributeNameButton.addListener(new ClickListener(){				
			private static final long serialVersionUID = 8271101709818834510L;

			public void buttonClick(ClickEvent event) {				
				doDeleteLaunchDecisionPointAttributeName();				
			}});
		launchDecisionPointAttributeActionContainer.addComponent(deleteDecisionPointAttributeNameButton);			
			
		String launchDecisionPointAttributeStr=this.businessActivityDefinition.getLaunchDecisionPointAttributeName()!=null?this.businessActivityDefinition.getLaunchDecisionPointAttributeName():"";
		launchDecisionPointAttributeNameLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+launchDecisionPointAttributeStr+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem launchDecisionPointAttributeNamePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_launchDscisionPointAttrNameLabel"),launchDecisionPointAttributeNameLabel,launchDecisionPointAttributeActionContainer);		
		containerPanel.addComponent(launchDecisionPointAttributeNamePropertyItem);	
		
		HorizontalLayout launchDecisionPointChoiseListActionContainer=new HorizontalLayout();		
		launchDecisionPointChoiseListActionContainer.setWidth("60px");		
		Button editlaunchDecisionPointChoiseListButton=new Button();		
		editlaunchDecisionPointChoiseListButton.setCaption(null);
		editlaunchDecisionPointChoiseListButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editlaunchDecisionPointChoiseListButton.setStyleName(BaseTheme.BUTTON_LINK);
		editlaunchDecisionPointChoiseListButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));	
		editlaunchDecisionPointChoiseListButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = -3021763942942145318L;

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
			private static final long serialVersionUID = -8231517376909963986L;

			public void buttonClick(ClickEvent event) {				
				doDeleteDecisionPointChoiseList();				
			}});
		launchDecisionPointChoiseListActionContainer.addComponent(deletelaunchDecisionPointChoiseListButton);		
		String[] choiseList=this.businessActivityDefinition.getLaunchDecisionPointChoiseList();
		String choiseListContent=formatStringArrayData(choiseList);		
		launchDecisionPointChoiseListLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+choiseListContent+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem launchDecisionPointChoiseListPropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_launchDscisionPointChoiseListLabel"),launchDecisionPointChoiseListLabel,launchDecisionPointChoiseListActionContainer);		
		containerPanel.addComponent(launchDecisionPointChoiseListPropertyItem);
		
		HorizontalLayout launchProcessVariableListActionContainer=new HorizontalLayout();		
		launchProcessVariableListActionContainer.setWidth("60px");		
		Button editLaunchProcessVariableListActionContainerButton=new Button();		
		editLaunchProcessVariableListActionContainerButton.setCaption(null);
		editLaunchProcessVariableListActionContainerButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editLaunchProcessVariableListActionContainerButton.setStyleName(BaseTheme.BUTTON_LINK);
		editLaunchProcessVariableListActionContainerButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));		
		editLaunchProcessVariableListActionContainerButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = -3021763942942145318L;

			public void buttonClick(ClickEvent event) {				
				renderSetLaunchProcessVariableListUI();				
			}});
		launchProcessVariableListActionContainer.addComponent(editLaunchProcessVariableListActionContainerButton);
		Button deletelaunchProcessVariableListButton=new Button();		
		deletelaunchProcessVariableListButton.setCaption(null);
		deletelaunchProcessVariableListButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deletelaunchProcessVariableListButton.setStyleName(BaseTheme.BUTTON_LINK);
		deletelaunchProcessVariableListButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_deleteButtonDescLabel"));	
		deletelaunchProcessVariableListButton.addListener(new ClickListener(){						
			private static final long serialVersionUID = -8231517376909963986L;

			public void buttonClick(ClickEvent event) {				
				doDeleteLaunchProcessVariable();				
			}});
		launchProcessVariableListActionContainer.addComponent(deletelaunchProcessVariableListButton);			
		String[] variableList=this.businessActivityDefinition.getLaunchProcessVariableList();
		String variableListContent=formatStringArrayData(variableList);		
		launchProcessVariableListLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+variableListContent+"</span>" ,Label.CONTENT_XHTML);		
		PropertyItem launchProcessVariableListPropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_launchProcessVarLabel"),launchProcessVariableListLabel,launchProcessVariableListActionContainer);		
		containerPanel.addComponent(launchProcessVariableListPropertyItem);
		
		HorizontalLayout launchUserIdentityAttributeNameActionContainer=new HorizontalLayout();		
		launchUserIdentityAttributeNameActionContainer.setWidth("60px");		
		Button editlaunchUserIdentityAttributeNameActionContainerButton=new Button();		
		editlaunchUserIdentityAttributeNameActionContainerButton.setCaption(null);
		editlaunchUserIdentityAttributeNameActionContainerButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editlaunchUserIdentityAttributeNameActionContainerButton.setStyleName(BaseTheme.BUTTON_LINK);
		editlaunchUserIdentityAttributeNameActionContainerButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));		
		editlaunchUserIdentityAttributeNameActionContainerButton.addListener(new ClickListener(){				
			private static final long serialVersionUID = -3021763942942145318L;

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
			private static final long serialVersionUID = -8231517376909963986L;

			public void buttonClick(ClickEvent event) {				
				doDeleteLaunchUserIdentityAttributeName();				
			}});
		launchUserIdentityAttributeNameActionContainer.addComponent(deletelaunchUserIdentityAttributeNameButton);
		String launchUserIdentityAttributeNameStr=this.businessActivityDefinition.getLaunchUserIdentityAttributeName()!=null?this.businessActivityDefinition.getLaunchUserIdentityAttributeName():"";
		launchUserIdentityAttributeNameLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+launchUserIdentityAttributeNameStr+"</span>" ,Label.CONTENT_XHTML);
		PropertyItem launchUserIdentityAttributeNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_launchUserIdAttNameLabel"),launchUserIdentityAttributeNameLabel,launchUserIdentityAttributeNameActionContainer);	
		containerPanel.addComponent(launchUserIdentityAttributeNamePropertyItem);		
		this.addComponent(containerPanel);		
	}
	
	private void renderDefineFileContentUI(){		
		StringBuffer strBuf=new StringBuffer();			
		ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(businessActivityDefinition.getActivitySpaceName());
		try {
			BusinessActivityDefinition realtimeBusinessActivityDefinition=currentActivitySpace.getBusinessActivityDefinition(businessActivityDefinition.getActivityType());			
			Object definitionResource=realtimeBusinessActivityDefinition.getDefinitionResource();			
			InputStream businessActivityDefineFileInputStream=(InputStream)definitionResource;
			byte buf[]=new byte[1024];
			int len;
			try {
				while((len=businessActivityDefineFileInputStream.read(buf))>0){
					strBuf.append(new String(buf,0,len));										
				}
				businessActivityDefineFileInputStream.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
		VerticalLayout contentContainer=new VerticalLayout();
		Label contentLabel=new Label(strBuf.toString(),Label.CONTENT_PREFORMATTED);
		contentContainer.addComponent(contentLabel);
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceBusinessActivity_blackIcon);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_BPMNFileLabel")+" "			
				+"</b> <b style='color:#ce0000;'>" + businessActivityDefinition.getActivityType()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,contentContainer,"1000px");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderEditRosterUI(){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setRosterWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setRosterWindowDesc");
		try {
			String activitySpaceName=this.businessActivityDefinition.getActivitySpaceName();			
			ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
			Roster[] rosterArray=activitySpace.getRosters();		
			VerticalLayout setRosterLayout = new VerticalLayout();			
			HorizontalLayout dropdownListLayout=new HorizontalLayout();	
			HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
			leftSpaceLayout.setWidth("20px");
			dropdownListLayout.addComponent(leftSpaceLayout);
			final ComboBox rosterChoise = new ComboBox();
			rosterChoise.setNullSelectionAllowed(false);			
			rosterChoise.setWidth("270px");
			rosterChoise.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_selectRosterLabel"));			
			rosterChoise.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);			
			dropdownListLayout.addComponent(rosterChoise);
			IndexedContainer rosterContainer = new IndexedContainer();
			rosterContainer.addContainerProperty("ROSTER_DISPLAYNAME", String.class, null);
			rosterContainer.addContainerProperty("ROSTER", Roster.class, null);
			rosterChoise.setContainerDataSource(rosterContainer);
			rosterChoise.setItemCaptionPropertyId("ROSTER_DISPLAYNAME");
			rosterChoise.setEnabled(true);			
			for(int i=0;i<rosterArray.length;i++){				
				String id=""+i;
				Item item = rosterContainer.addItem(id);	
				item.getItemProperty("ROSTER_DISPLAYNAME").setValue(rosterArray[i].getDisplayName()+" ( "+rosterArray[i].getRosterName()+" )");
			    item.getItemProperty("ROSTER").setValue(rosterArray[i]);			  
			    if(this.businessActivityDefinition.getRosterName()!=null&&this.businessActivityDefinition.getRosterName().equals(rosterArray[i].getRosterName())){
			    	rosterChoise.select(id);			        	
			    }			        
			}						
			setRosterLayout.addComponent(dropdownListLayout);			
			Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setRoster_confirmButton"));
			confirmButton.addListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1005407312247454068L;

				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
					if(rosterChoise.getValue()==null){
						return;
					}
					IndexedContainer participantContainer =(IndexedContainer) rosterChoise.getContainerDataSource();
					Item selectedItem=participantContainer.getItem(rosterChoise.getValue().toString());				
					Roster selectedRoster=(Roster)selectedItem.getItemProperty("ROSTER").getValue();
					doSetRoster(selectedRoster);						
				}
		     });  
		    
		    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setRoster_cancelButton"));        
		    cancelButton.addListener(new Button.ClickListener() {	
				private static final long serialVersionUID = -4854867169341457366L;

				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
					removeSetPropertyWindow();
		           }
		     });	
		    cancelButton.setStyleName(BaseTheme.BUTTON_LINK);
	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(confirmButton);
	        buttonList.add(cancelButton); 
	        setRosterLayout.addComponent(new BaseButtonBar(120, 30, Alignment.MIDDLE_RIGHT, buttonList));
	        setpropertyWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, 420,setRosterLayout);
	        setpropertyWindow.setResizable(false);
	        setpropertyWindow.center();
	        setpropertyWindow.setModal(true);
			this.getApplication().getMainWindow().addWindow(setpropertyWindow);		     
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} 
	}
	
	private void removeSetPropertyWindow(){
		this.getApplication().getMainWindow().removeWindow(setpropertyWindow);
		setpropertyWindow=null;
	}
	
	private void doSetRoster(Roster selectedRoster){
		this.rosterNameLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+selectedRoster.getRosterName()+"</span>");
		removeSetPropertyWindow();
		this.businessActivityDefinitionUI.newRoster=selectedRoster;
		this.businessActivityDefinitionUI.activityDefinitionPropertyModified();		
	}
	
	private void renderSetActivityTypeStatusUI(){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setStatusWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setStatusWindowDesc");
        VerticalLayout setStatusLayout = new VerticalLayout();			
		HorizontalLayout dropdownListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		dropdownListLayout.addComponent(leftSpaceLayout);
		final ComboBox statusChoise = new ComboBox();
		statusChoise.setNullSelectionAllowed(false);			
		statusChoise.setWidth("150px");
		statusChoise.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_selectStatusPrompt"));			
		statusChoise.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);			
		dropdownListLayout.addComponent(statusChoise);
		statusChoise.addItem(true);
		statusChoise.addItem(false);		
		statusChoise.select(this.businessActivityDefinition.isEnabled());		
		setStatusLayout.addComponent(dropdownListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setStatus_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -4960143700121202703L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {				
				Boolean selectedStatus=(Boolean)statusChoise.getValue();				
				doSetStatus(selectedStatus.booleanValue());				
			}
	     });  
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setStatus_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {				
			private static final long serialVersionUID = -8231517376909963986L;

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
	
	private void doSetStatus(boolean newStatus){		
		this.statusLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+newStatus+"</span>");
		removeSetPropertyWindow();
		this.businessActivityDefinitionUI.newStatus=newStatus;
		this.businessActivityDefinitionUI.activityDefinitionPropertyModified();		
	}
	
	private void renderSetlaunchDecisionPointAttributeNameUI(){		
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchDPAttrNameWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchDPAttrNameWindowDesc");
        VerticalLayout setStatusLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);
		final TextField attributeName=new TextField();
		attributeName.setWidth("250px");
		attributeName.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_launchDPAttrNameInputPrompt"));	
		if(this.businessActivityDefinition.getLaunchDecisionPointAttributeName()!=null){			
			attributeName.setValue(this.businessActivityDefinition.getLaunchDecisionPointAttributeName());			
		}					
		inputFieldListLayout.addComponent(attributeName);
		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchDPAttrName_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 2874717039996505027L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if(attributeName.getValue()==null){
					return;
				}
				String launchDecisionPointAttributeName=attributeName.getValue().toString();				
				doSetLaunchDecisionPointAttributeName(launchDecisionPointAttributeName);				
			}
	     });  
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchDPAttrName_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -1950830140468915469L;

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
		this.businessActivityDefinition.setLaunchDecisionPointAttributeName(launchDecisionPointAttributeName);
		this.businessActivityDefinitionUI.activityDefinitionPropertyModified();			
	}
	
	private void doDeleteLaunchDecisionPointAttributeName(){
		if(this.businessActivityDefinition.getLaunchDecisionPointAttributeName()!=null){
			this.launchDecisionPointAttributeNameLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
			this.businessActivityDefinition.setLaunchDecisionPointAttributeName(null);
			this.businessActivityDefinitionUI.activityDefinitionPropertyModified();
		}
	}
	
	private void renderSetLaunchUserIdentityAttributeNameUI(){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchUserIDAttrNameWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchUserIDAttrNameWindowDesc");
        VerticalLayout setStatusLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);
		final TextField attributeName=new TextField();
		attributeName.setWidth("250px");
		attributeName.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_launchUserIdAttrNameInputPrompt"));	
		if(this.businessActivityDefinition.getLaunchUserIdentityAttributeName()!=null){			
			attributeName.setValue(this.businessActivityDefinition.getLaunchUserIdentityAttributeName());			
		}					
		inputFieldListLayout.addComponent(attributeName);		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchUserIDAttrName_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -775900956436130168L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if(attributeName.getValue()==null){
					return;
				}
				String launchUserIdentityAttributeName=attributeName.getValue().toString();				
				doSetLaunchUserIdentityAttributeName(launchUserIdentityAttributeName);				
			}
	     });  
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchUserIDAttrName_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -1815434669880817508L;

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
		this.businessActivityDefinition.setLaunchUserIdentityAttributeName(launchUserIdentityAttributeName);
		this.businessActivityDefinitionUI.activityDefinitionPropertyModified();			
	}
	
	private void doDeleteLaunchUserIdentityAttributeName(){
		if(this.businessActivityDefinition.getLaunchUserIdentityAttributeName()!=null){
			this.launchUserIdentityAttributeNameLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
			this.businessActivityDefinition.setLaunchUserIdentityAttributeName(null);
			this.businessActivityDefinitionUI.activityDefinitionPropertyModified();
		}
	}
	
	private void renderSetLaunchProcessVariableListUI(){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchProcessVLWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchProcessVLWindowDesc");
        VerticalLayout setStatusLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);
		
		final OptionGroup dataFieldsSelect = new OptionGroup("");
		dataFieldsSelect.setMultiSelect(true);
		dataFieldsSelect.setNullSelectionAllowed(true);			
		DataFieldDefinition[] dataFieldDefnArray= this.businessActivityDefinition.getActivityDataFields();		
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty("propertyName_DataFieldName", String.class,null);
        container.addContainerProperty("propertyName_DataFieldDisplayName", String.class,null);
        dataFieldsSelect.setContainerDataSource(container);
        dataFieldsSelect.setItemCaptionPropertyId("propertyName_DataFieldDisplayName");
		
        String[] currentLaunchProcessVariableList=this.businessActivityDefinition.getLaunchProcessVariableList();       			
		for(int i=0;i<dataFieldDefnArray.length;i++){				
			DataFieldDefinition currentDataField=dataFieldDefnArray[i];
			String id = ""+i;
			Item item = container.addItem(id);	
			String dataFieldName=currentDataField.getFieldName();
			String dataFieldCaption=currentDataField.getDisplayName()+" ( "+currentDataField.getFieldName()+" )";
			item.getItemProperty("propertyName_DataFieldName").setValue(dataFieldName);  
			item.getItemProperty("propertyName_DataFieldDisplayName").setValue(dataFieldCaption);		
			if(alreadyContainsDataField(dataFieldName,currentLaunchProcessVariableList)){
				dataFieldsSelect.select(id);					
			}
		}			
		
		inputFieldListLayout.addComponent(dataFieldsSelect);		
		setStatusLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchProcessVL_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 7066765175294769917L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				Set selectedDataFieldIdxSet=(Set)dataFieldsSelect.getValue();				
				Object[] idxArry=selectedDataFieldIdxSet.toArray();	
				String[] newDadaFieldNameArray=new String[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					newDadaFieldNameArray[i]=selecteItem.getItemProperty("propertyName_DataFieldName").getValue().toString();					
				}
				doSetLaunchProcessVariableList(newDadaFieldNameArray);
			}
	     });  
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchProcessVL_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 2299123995642851907L;

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
	
	private void doSetLaunchProcessVariableList(String[] dataFieldsArray){
		this.launchProcessVariableListLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+formatStringArrayData(dataFieldsArray)+"</span>");
		removeSetPropertyWindow();		
		if(dataFieldsArray!=null&&dataFieldsArray.length>0){
			this.businessActivityDefinition.setLaunchProcessVariableList(dataFieldsArray);
		}else{
			this.businessActivityDefinition.setLaunchProcessVariableList(null);
		}
		this.businessActivityDefinitionUI.activityDefinitionPropertyModified();	
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
	
	private void doDeleteLaunchProcessVariable(){
		if(this.businessActivityDefinition.getLaunchProcessVariableList()!=null){
			this.launchProcessVariableListLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
			this.businessActivityDefinition.setLaunchProcessVariableList(null);
			this.businessActivityDefinitionUI.activityDefinitionPropertyModified();
		}
	}
	
	private void renderSetlaunchDecisionPointChoiseListUI(){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchDPChoiseLWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchDPChoiseLWindowDesc");
        VerticalLayout setDecisionPointChoiseListLayout = new VerticalLayout();			
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);		
		String[] currentChoiseList=this.businessActivityDefinition.getLaunchDecisionPointChoiseList();
		final DecisionPointChoiseListEditor decisionPointChoiseListEditor=new DecisionPointChoiseListEditor(currentChoiseList,this.userClientInfo);	
		inputFieldListLayout.addComponent(decisionPointChoiseListEditor);		
		setDecisionPointChoiseListLayout.addComponent(inputFieldListLayout);			
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchDPChoiseL_confirmButton"));
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
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionPropertyList_setLaunchDPChoiseL_cancelButton"));        
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
		if(choiseListValue!=null&&choiseListValue.length>0){
			this.businessActivityDefinition.setLaunchDecisionPointChoiseList(choiseListValue);
		}else{
			this.businessActivityDefinition.setLaunchDecisionPointChoiseList(null);
		}
		this.businessActivityDefinitionUI.activityDefinitionPropertyModified();	
	}
	
	private void doDeleteDecisionPointChoiseList(){
		if(this.businessActivityDefinition.getLaunchDecisionPointChoiseList()!=null){
			this.launchDecisionPointChoiseListLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
			this.businessActivityDefinition.setLaunchDecisionPointChoiseList(null);
			this.businessActivityDefinitionUI.activityDefinitionPropertyModified();
		}
	}
}