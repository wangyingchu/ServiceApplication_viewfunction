package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
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
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.BusinessActivityDefinitionsTable;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ExposedDataFieldsEditor;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster.RostersChangeEvent.RostersChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RostersTable extends Table implements RostersChangeListener{
	private static final long serialVersionUID = -6568078620053292199L;
	
	private Roster[] rosterArray;
	private UserClientInfo userClientInfo;
	private ActivityObjectDetail activityObjectDetail;
	
	private String columnName_RosterName="columnName_RosterName";
	private String columnName_RosterDisplayName="columnName_RosterDisplayName";
	private String columnName_RosterDescription="columnName_RosterDescription";
	private String columnName_RosterOperations="columnName_RosterOperations";
	
	public RostersTable(Roster[] rosterArray,UserClientInfo userClientInfo,ActivityObjectDetail activityObjectDetail){
		this.rosterArray=rosterArray;
		this.userClientInfo=userClientInfo;
		this.activityObjectDetail=activityObjectDetail;
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getRostersData(rosterArray));
		if(this.rosterArray!=null){
			if(this.rosterArray.length<=20){
				setPageLength(this.rosterArray.length);
			}else{
				setPageLength(20);
			}			
		}else{
			setPageLength(0);
		}			
		setColumnReorderingAllowed(false);
		
		setColumnHeaders(new String[] {				
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_rosterNameField"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_rosterDisplayNameField"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_rosterDescField"),
				"",
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_rosterActionField")
		});	
		setColumnAlignment(columnName_RosterName,Table.ALIGN_LEFT);			
		setColumnAlignment(columnName_RosterDisplayName,Table.ALIGN_LEFT);
		setColumnWidth(columnName_RosterDisplayName, 250);
		setColumnAlignment(columnName_RosterDescription,Table.ALIGN_LEFT);
		setColumnWidth(columnName_RosterDescription, 250);
		setColumnWidth("DIV", 0);
		setColumnAlignment(columnName_RosterOperations,Table.ALIGN_CENTER);
		setColumnWidth(columnName_RosterOperations, 160);
		addGeneratedColumns();		
	}
	
	private void renderRosterUI(String rosterIndex){		
		int index=Integer.parseInt(rosterIndex);
		Roster roster=rosterArray[index];
		this.activityObjectDetail.renderRosterUI(roster);		
	}
	
	private void renderBusinessActivitysUI(String rosterIndex){
		int index=Integer.parseInt(rosterIndex);
		Roster roster=rosterArray[index];
		BusinessActivitiesTable businessActivitiesTable=new BusinessActivitiesTable(roster,this.userClientInfo);
		Embedded activitiesIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceActivityInstance_blackIcon);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_businessActivityWindow")				
				+"</b> <b style='color:#ce0000;'>" + roster.getRosterName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(activitiesIcon,propertyNameLable,businessActivitiesTable,"1100px");		
		lightContentWindow.center();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);				
	}
	
	private void renderActivityTypesUI(String rosterIndex){
		int index=Integer.parseInt(rosterIndex);
		Roster roster=rosterArray[index];		
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
			BusinessActivityDefinitionsTable businessActivityDefinitionsTable=
					new BusinessActivityDefinitionsTable(businessActivityDefinitionArray,this.userClientInfo,this.activityObjectDetail);			
			Embedded activityTypesIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceBusinessActivity_blackIcon);
			Label propertyNameLable = new Label("<b style='color:#333333;'>"+
					userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_businessTypeWindow")			
					+"</b> <b style='color:#ce0000;'>" + roster.getRosterName()+ "</b>", Label.CONTENT_XHTML);		
			LightContentWindow lightContentWindow=new LightContentWindow(activityTypesIcon,propertyNameLable,businessActivityDefinitionsTable,"1100px");		
			lightContentWindow.center();
			this.getApplication().getMainWindow().addWindow(lightContentWindow);
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {
			e.printStackTrace();
		}
	}
	
	private void renderExposedDataFieldsUI(String rosterIndex){
		int index=Integer.parseInt(rosterIndex);
		Roster roster=rosterArray[index];	
		ExposedDataFieldsEditor exposedDataFieldsEditor=null;		
		DataFieldDefinition[] dataFieldDefinations=null;
		try {
			dataFieldDefinations=roster.getExposedDataFields();			
			exposedDataFieldsEditor=new ExposedDataFieldsEditor(roster,dataFieldDefinations,userClientInfo,true,null);
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		}	
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_exposedDataFieldsWindow_dataFieldsEditorLabel")				
				+"</b> <b style='color:#ce0000;'>" +roster.getRosterName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,exposedDataFieldsEditor,"1000px");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
	
	private IndexedContainer getRostersData(Roster[] rosterArray){	
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(columnName_RosterName, String.class,null);
        container.addContainerProperty(columnName_RosterDisplayName, String.class,null);
        container.addContainerProperty(columnName_RosterDescription, String.class,null);	
        container.addContainerProperty("DIV", String.class,null);	
        container.addContainerProperty(columnName_RosterOperations, String.class,null);	        
		if(rosterArray==null){
			return container;
		}		
		for(int i=0;i<rosterArray.length;i++){
			Roster roster=rosterArray[i];
			String id = ""+i;
			Item item = container.addItem(id);			
			item.getItemProperty(columnName_RosterName).setValue(roster.getRosterName());  
			item.getItemProperty(columnName_RosterDisplayName).setValue(roster.getDisplayName());
			item.getItemProperty(columnName_RosterDescription).setValue(roster.getDescription());
			item.getItemProperty("DIV").setValue("");
			item.getItemProperty(columnName_RosterOperations).setValue("");	
		}
		return container;
	}

	public void receiveRostersChange(RostersChangeEvent event) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();
		this.removeGeneratedColumn(columnName_RosterName);
		this.removeGeneratedColumn(columnName_RosterDisplayName);
		this.removeGeneratedColumn(columnName_RosterDescription);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_RosterOperations);
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(event.getActivitySpaceName());		
		try {
			this.rosterArray=activitySpace.getRosters();
			if(this.rosterArray!=null){
				if(this.rosterArray.length<=20){
					setPageLength(this.rosterArray.length);
				}else{
					setPageLength(20);
				}			
			}else{
				setPageLength(0);
			}
			if(rosterArray!=null){
				for(int i=0;i<rosterArray.length;i++){
					Roster roster=rosterArray[i];
					String id = ""+i;
					Item item = container.addItem(id);			
					item.getItemProperty(columnName_RosterName).setValue(roster.getRosterName());  
					item.getItemProperty(columnName_RosterDisplayName).setValue(roster.getDisplayName());
					item.getItemProperty(columnName_RosterDescription).setValue(roster.getDescription());
					item.getItemProperty("DIV").setValue("");
					item.getItemProperty(columnName_RosterOperations).setValue("");	
				}		
			}				
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}	
		addGeneratedColumns();
	}
	
	private void addGeneratedColumns(){
		addGeneratedColumn(columnName_RosterName, new Table.ColumnGenerator() {				
			private static final long serialVersionUID = 2678846800374270651L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	final String objectNameString=(String)item.getItemProperty(columnName_RosterName).getValue();		            	
            	Button objNameButton = new Button(objectNameString);
            	objNameButton.setStyleName(BaseTheme.BUTTON_LINK);
            	objNameButton.addListener(new ClickListener(){            		
					private static final long serialVersionUID = -3567064194516804327L;

					public void buttonClick(ClickEvent event) {	
						renderRosterUI(itemId.toString());						
					}}); 
            	return objNameButton;
            }			
		 });
		
		addGeneratedColumn(columnName_RosterDisplayName, new Table.ColumnGenerator() { 		
			private static final long serialVersionUID = -3210043411234857682L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString="";
	            	if(item.getItemProperty(columnName_RosterDisplayName).getValue()!=null){
	            		objectDisplayNameString=(String)item.getItemProperty(columnName_RosterDisplayName).getValue();
	            	}	            	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }
	        });	
		
		addGeneratedColumn(columnName_RosterDescription, new Table.ColumnGenerator() { 						
			private static final long serialVersionUID = 3246666958210387009L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString="";
	            	if(item.getItemProperty(columnName_RosterDescription).getValue()!=null){
	            		objectDisplayNameString=(String)item.getItemProperty(columnName_RosterDescription).getValue();
	            	}	            	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }
	        });	
		
		addGeneratedColumn("DIV", new Table.ColumnGenerator() { 
			private static final long serialVersionUID = 911755711057221205L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Embedded objectTypeIcon=new Embedded(null, UICommonElementDefination.AppTable_emptySpaceDiv);	            	
	            	return objectTypeIcon;
	            }
	        });	
		
		 final String getActivityTypesLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_businesTypeButton");		 
		 final String getExposedDatafieldsLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_dataFieldButton");
		 final String fetchBusinessActivitiessLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_RostersTable_businessActivityButton");
		 
		 addGeneratedColumn(columnName_RosterOperations, new Table.ColumnGenerator() { 	
			private static final long serialVersionUID = 8388682259498347242L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	 
	            	HorizontalLayout actionsContainerHorizontalLayout=new HorizontalLayout();
	            	actionsContainerHorizontalLayout.setWidth("120px");	  	            	
	            	Button getActivityTypesButton = new Button("");  
	            	getActivityTypesButton.setCaption(null);
	            	getActivityTypesButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceBusinessActivity_blackIcon);
	            	getActivityTypesButton.setStyleName(BaseTheme.BUTTON_LINK); 
	            	getActivityTypesButton.setDescription(getActivityTypesLabel);
	            	
	            	Button getExposedDataFieldsButton = new Button("");
	            	getExposedDataFieldsButton.setCaption(null);
	            	getExposedDataFieldsButton.setIcon(UICommonElementDefination.ICON_dataFieldDefine_DataFields);
	            	getExposedDataFieldsButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	getExposedDataFieldsButton.setDescription(getExposedDatafieldsLabel);
	            	
	            	Button getBusinessActivitysButton = new Button("");
	            	getBusinessActivitysButton.setCaption(null);
	            	getBusinessActivitysButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceActivityInstance_blackIcon);
	            	getBusinessActivitysButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	getBusinessActivitysButton.setDescription(fetchBusinessActivitiessLabel);
	            	
	            	getActivityTypesButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderActivityTypesUI(itemId.toString());
						}});	            	
	            	
	            	getExposedDataFieldsButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderExposedDataFieldsUI(itemId.toString());
						}});
	            	
	            	getBusinessActivitysButton.addListener(new ClickListener(){   
						private static final long serialVersionUID = -4128628066125743446L;

						public void buttonClick(ClickEvent event) {							
							renderBusinessActivitysUI(itemId.toString());
						}});
	            	actionsContainerHorizontalLayout.addComponent(getActivityTypesButton);	            	
	            	actionsContainerHorizontalLayout.setComponentAlignment(getActivityTypesButton, Alignment.MIDDLE_RIGHT);
	            	actionsContainerHorizontalLayout.addComponent(getExposedDataFieldsButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(getExposedDataFieldsButton, Alignment.MIDDLE_RIGHT);	
	            	actionsContainerHorizontalLayout.addComponent(getBusinessActivitysButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(getBusinessActivitysButton, Alignment.MIDDLE_RIGHT);
	            	return actionsContainerHorizontalLayout;
	            }
	        });		
	}
}