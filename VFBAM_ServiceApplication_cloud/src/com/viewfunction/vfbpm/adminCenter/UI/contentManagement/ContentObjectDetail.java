package com.viewfunction.vfbpm.adminCenter.UI.contentManagement;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.ContentObjectInheritanceChain;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.util.ContentObjectUIElementCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentSpaceDataProvider;
import com.viewfunction.contentRepository.util.helper.ObjectCollectionHelper;

public class ContentObjectDetail  extends VerticalLayout{	
	private static final long serialVersionUID = -8338994964052751179L;
	
	private ContentManagementPanel contentManagementPanel;	
	
	public Label contentSpaceName;
	public Label currentObjecteName;
	public Button parentObjectLinkButton;

	public ContentObjectPropertyList contentObjectPropertyList;
	public SubContentObjectTable subContentObjectTable;
	public UserClientInfo userClientInfo;
	
	public Button addPropertyButton;
	public Button addSubObjectButton;
	
	public String currentContentObjectABSPath;
	public String currentContentSpace;
	public Object currentDataObjectItemID;
	
	private static ContentObjectUIElementCreator contentObjectUIElementCreator;

	public ContentObjectDetail(UserClientInfo userClientInfo){
		
		HorizontalLayout actionBarHorizontalLayout=new HorizontalLayout();
		actionBarHorizontalLayout.setWidth("100%");	
		actionBarHorizontalLayout.setHeight("23px");		
		actionBarHorizontalLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_ContentManagementNavTitleText);
		this.userClientInfo=userClientInfo;
		HorizontalLayout linksHorizontalLayout=new HorizontalLayout();			

		contentSpaceName=new Label("<span style='color:#333333;margin-left: 10px;'>"+
				userClientInfo.getI18NProperties().getProperty("contentManage_detailPanel_contentSpaceName")+"</span>", Label.CONTENT_XHTML);
		linksHorizontalLayout.addComponent(contentSpaceName);
		linksHorizontalLayout.setComponentAlignment(contentSpaceName, Alignment.MIDDLE_LEFT);
		
		Label contentSpaceNameDiv=new Label("<span style='margin-left: 5px;margin-right: 5px;'>:</span>" ,Label.CONTENT_XHTML);			
		linksHorizontalLayout.addComponent(contentSpaceNameDiv);
		linksHorizontalLayout.setComponentAlignment(contentSpaceNameDiv, Alignment.MIDDLE_LEFT);
		
		parentObjectLinkButton=new Button("");
		parentObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);
		
		parentObjectLinkButton.addListener(new ClickListener(){ 			
			private static final long serialVersionUID = -3605756437918727779L;

			public void buttonClick(ClickEvent event) {
				renderParentContentObjectData();
				
			}}); 		
		
		linksHorizontalLayout.addComponent(parentObjectLinkButton);
		linksHorizontalLayout.setComponentAlignment(parentObjectLinkButton, Alignment.MIDDLE_LEFT);
		
		Label parentObjectNameDiv=new Label("<span style='margin-left: 5px;margin-right: 5px;'>></span>" ,Label.CONTENT_XHTML);		
		linksHorizontalLayout.addComponent(parentObjectNameDiv);
		linksHorizontalLayout.setComponentAlignment(parentObjectNameDiv, Alignment.MIDDLE_LEFT);
		
		currentObjecteName=new Label("<b style='color:#ce0000;'>"+
				userClientInfo.getI18NProperties().getProperty("contentManage_detailPanel_currentObjectName")+"</b>", Label.CONTENT_XHTML);		
		linksHorizontalLayout.addComponent(currentObjecteName);
		linksHorizontalLayout.setComponentAlignment(currentObjecteName, Alignment.MIDDLE_LEFT);
		
		actionBarHorizontalLayout.addComponent(linksHorizontalLayout);
		actionBarHorizontalLayout.setComponentAlignment(linksHorizontalLayout, Alignment.MIDDLE_LEFT);		
		
		HorizontalLayout actionButtonsHorizontalLayout=new HorizontalLayout();		
		actionButtonsHorizontalLayout.setWidth("100px");
		actionButtonsHorizontalLayout.addComponent(new Embedded(null, UICommonElementDefination.AppPanel_editActionbarIcon));
		actionButtonsHorizontalLayout.addComponent(new Embedded(null, UICommonElementDefination.AppPanel_deleteActionbarIcon));
		actionButtonsHorizontalLayout.addComponent(new Embedded(null, UICommonElementDefination.AppPanel_advancedActionbarIcon));			
		
		actionBarHorizontalLayout.addComponent(actionButtonsHorizontalLayout);
		actionBarHorizontalLayout.setComponentAlignment(actionButtonsHorizontalLayout, Alignment.MIDDLE_RIGHT);		
		
		this.addComponent(actionBarHorizontalLayout);			
		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);				
		
		HorizontalLayout sectionTitleHorizontalLayout=new HorizontalLayout();
		sectionTitleHorizontalLayout.setWidth("100%");
		sectionTitleHorizontalLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_SectionTitleText);
		
		HorizontalLayout sectionTitleTextContainer=new HorizontalLayout();
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_propertySectionIcon);
		sectionTitleTextContainer.addComponent(iconEmbedded);
		sectionTitleTextContainer.setComponentAlignment(iconEmbedded, Alignment.MIDDLE_LEFT);			
		Label contentObjectPropertyLabel=new Label("<span style='margin-left: 3px;'>"+
				userClientInfo.getI18NProperties().getProperty("contentManage_contentObjectProperty")+"</span>",Label.CONTENT_XHTML);			
		sectionTitleTextContainer.addComponent(contentObjectPropertyLabel);
		sectionTitleTextContainer.setComponentAlignment(contentObjectPropertyLabel, Alignment.MIDDLE_LEFT);			
		sectionTitleHorizontalLayout.addComponent(sectionTitleTextContainer);
		sectionTitleHorizontalLayout.setComponentAlignment(sectionTitleTextContainer, Alignment.MIDDLE_LEFT);			
		
		HorizontalLayout sectionTitleActionbarContainer=new HorizontalLayout();
		sectionTitleActionbarContainer.setWidth("30px");
		addPropertyButton=new Button();
		addPropertyButton.setCaption(null);
		addPropertyButton.setIcon(UICommonElementDefination.AppPanel_addPropertySectionIcon);
		addPropertyButton.setStyleName(BaseTheme.BUTTON_LINK);
		addPropertyButton.setEnabled(false);
		addPropertyButton.setDescription("lalalalala");
		
		addPropertyButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = -2366435539977760731L;

			public void buttonClick(ClickEvent event) {				
				launchAddPropertytWindow();	
			}});
		sectionTitleActionbarContainer.addComponent(addPropertyButton);
		
		sectionTitleHorizontalLayout.addComponent(sectionTitleActionbarContainer);
		sectionTitleHorizontalLayout.setComponentAlignment(sectionTitleActionbarContainer, Alignment.MIDDLE_RIGHT);			
		
		containerPanel.addComponent(sectionTitleHorizontalLayout);			
		
		contentObjectPropertyList=new ContentObjectPropertyList(userClientInfo);
		containerPanel.addComponent(contentObjectPropertyList);			
		
		HorizontalLayout divHorizontalLayout=new HorizontalLayout();
		divHorizontalLayout.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout);			
		
		HorizontalLayout sectionTitleHorizontalLayout_1=new HorizontalLayout();
		sectionTitleHorizontalLayout_1.setWidth("100%");
		sectionTitleHorizontalLayout_1.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_SectionTitleText);
		
		HorizontalLayout sectionTitleTextContainer_1=new HorizontalLayout();
		Embedded iconEmbedded_1=new Embedded(null, UICommonElementDefination.AppPanel_propertySectionIcon);
		sectionTitleTextContainer_1.addComponent(iconEmbedded_1);
		sectionTitleTextContainer_1.setComponentAlignment(iconEmbedded_1, Alignment.MIDDLE_LEFT);		
		Label contentObjectPropertyLabel_1=new Label("<span style='margin-left: 3px;'>"+
				userClientInfo.getI18NProperties().getProperty("contentManage_contentObjectSubObject")+"</span>",Label.CONTENT_XHTML);			
		sectionTitleTextContainer_1.addComponent(contentObjectPropertyLabel_1);
		sectionTitleTextContainer_1.setComponentAlignment(contentObjectPropertyLabel_1, Alignment.MIDDLE_LEFT);		
		sectionTitleHorizontalLayout_1.addComponent(sectionTitleTextContainer_1);
		sectionTitleHorizontalLayout_1.setComponentAlignment(sectionTitleTextContainer_1, Alignment.MIDDLE_LEFT);
		
		
		HorizontalLayout sectionTitleActionbarContainer_1=new HorizontalLayout();		
		sectionTitleActionbarContainer_1.setWidth("30px");		
		addSubObjectButton=new Button();
		addSubObjectButton.setCaption(null);
		addSubObjectButton.setIcon(UICommonElementDefination.AppPanel_addPropertySectionIcon);
		addSubObjectButton.setStyleName(BaseTheme.BUTTON_LINK);
		addSubObjectButton.setEnabled(false);		
		addSubObjectButton.setDescription("balabalabala");
		
		addSubObjectButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -2366435539977760731L;			
			
			public void buttonClick(ClickEvent event) {				
				launchAddSubObjectWindow();	
			}});
		
		sectionTitleActionbarContainer_1.addComponent(addSubObjectButton);		
		sectionTitleHorizontalLayout_1.addComponent(sectionTitleActionbarContainer_1);
		sectionTitleHorizontalLayout_1.setComponentAlignment(sectionTitleActionbarContainer_1, Alignment.MIDDLE_RIGHT);		
		containerPanel.addComponent(sectionTitleHorizontalLayout_1);		
		subContentObjectTable=new SubContentObjectTable(userClientInfo,this);		
		containerPanel.addComponent(subContentObjectTable);		
	}
	
	private void launchAddSubObjectWindow(){
		HierarchicalContainer contentSpace_DataContainer=contentManagementPanel.contentObjectBrowser.contentSpace_DataContainer;
		Application application=contentManagementPanel.getApplication();	
		Object cuObjId=this.currentDataObjectItemID;
		if(contentObjectUIElementCreator==null){					
			contentObjectUIElementCreator=new ContentObjectUIElementCreator(contentSpace_DataContainer,userClientInfo,this.getContentManagementPanel());
		 }		
		Window addObjW=contentObjectUIElementCreator.createAddContentObjectFormWindow(cuObjId);			 
		addObjW.center();		
		application.getMainWindow().addWindow(addObjW);		
	}
	
	private void launchAddPropertytWindow(){
		HierarchicalContainer contentSpace_DataContainer=contentManagementPanel.contentObjectBrowser.contentSpace_DataContainer;
		Application application=contentManagementPanel.getApplication();	
		Object cuObjId=this.currentDataObjectItemID;
		if(contentObjectUIElementCreator==null){					
			contentObjectUIElementCreator=new ContentObjectUIElementCreator(contentSpace_DataContainer,userClientInfo,this.getContentManagementPanel());
		 }		
		Window addPropertyjW=contentObjectUIElementCreator.createAddPropertyFormWindow(cuObjId);			 
		addPropertyjW.center();
		application.getMainWindow().addWindow(addPropertyjW);		
	}
	
	public ContentManagementPanel getContentManagementPanel() {
		return contentManagementPanel;
	}

	public void setContentManagementPanel(ContentManagementPanel contentManagementPanel) {
		this.contentManagementPanel = contentManagementPanel;
		this.contentObjectPropertyList.setContentManagementPanel(contentManagementPanel);
	}
	
	public void loadContentObjectDetailInfo(Object objectID){			
		HierarchicalContainer contentSpace_DataContainer=this.getContentManagementPanel().contentObjectBrowser.contentSpace_DataContainer;		
	    renderContentObjectData(contentSpace_DataContainer, objectID);
	} 
	
	 public void renderContentObjectData(HierarchicalContainer contentSpace_DataContainer, Object itemID){
		 this.addPropertyButton.setEnabled(true);
		 this.addSubObjectButton.setEnabled(true);		 
		 Item currentItem = contentSpace_DataContainer.getItem(itemID);		  
		 this.currentContentSpace = currentItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).toString();
	     this.currentContentObjectABSPath = this.getContentManagementPanel().contentObjectBrowser.contentSpaceBrowserTree.getContentObjectABSPathByItemId(itemID);	
	     this.currentDataObjectItemID=itemID;
	     ContentSpace cs = null;	     
	     try {
	    	 cs = ContentComponentFactory.connectContentSpace(this.currentContentSpace);	     
	    	 BaseContentObject currentObject=cs.getContentObjectByAbsPath(this.currentContentObjectABSPath);
	    	 BaseContentObject parentObject=currentObject.getParentContentObject();	    	 
	    	 contentSpaceName.setValue("<span style='color:#333333;margin-left: 10px;'>"+this.currentContentSpace+"</span>");  
	    	 currentObjecteName.setValue("<b style='color:#ce0000;'>"+currentObject.getContentObjectName()+"</b> ("+currentObject.getCurrentVersion().getCurrentVersionNumber()+")");	    	 
	    	 if(parentObject!=null){
	    		 parentObjectLinkButton.setCaption(parentObject.getContentObjectName());
	    	 }else{	    		
	    		 parentObjectLinkButton.setCaption("");
	    	 }	    	 
	    	 
	    	//GET PROPERTIES
	    	 ContentOperationHelper coh = ContentComponentFactory.getContentOperationHelper();
	         if (coh.getContentObjectType(currentObject).equals(ContentOperationHelper.CONTENTTYPE_FOLDEROBJECT)
	                    || coh.getContentObjectType(currentObject).equals(ContentOperationHelper.CONTENTTYPE_STANDALONEOBJECT)) {
	                List<ContentObjectProperty> propertyList = currentObject.getProperties();
	                contentObjectPropertyList.renderPropertyItemList(propertyList);          
	         	}
	     
	        //GET SUBOBJECTS  
	         subContentObjectTable.refreshTableDataSource(currentObject.getSubContentObjects(null),coh);
	     }catch (ContentReposityException e) {
	            e.printStackTrace();
	     } finally {
	    	 if (cs != null) {
	    		 cs.closeContentSpace();
	         }
	     }	     
	 }
	 
	 public void renderSubContentObjectData(String subObjectName){		
	     this.currentContentObjectABSPath =  this.currentContentObjectABSPath+"/"+subObjectName;
	     HierarchicalContainer contentSpace_DataContainer=contentManagementPanel.contentObjectBrowser.contentSpace_DataContainer;
	     Object parentItemId = this.currentDataObjectItemID;      
	     ContentSpace cs = null;	
	     
	     try {	    	 
	    	 cs = ContentComponentFactory.connectContentSpace(this.currentContentSpace);	     
	    	 BaseContentObject currentObject=cs.getContentObjectByAbsPath(this.currentContentObjectABSPath);
	    	 BaseContentObject parentObject=currentObject.getParentContentObject();	    	 
	    	 contentSpaceName.setValue("<span style='color:#333333;margin-left: 10px;'>"+ this.currentContentSpace +"</span>");  
	    	 currentObjecteName.setValue("<b style='color:#ce0000;'>"+currentObject.getContentObjectName()+"</b> ("+currentObject.getCurrentVersion().getCurrentVersionNumber()+")");	    	 
	    	 if(parentObject!=null){  
	    		 parentObjectLinkButton.setCaption(parentObject.getContentObjectName());
	    	 }else{	    		
	    		 parentObjectLinkButton.setCaption("");
	    	 }
	    	 //LOAD CONTENT BROWSER DATA	    	 
	    	 if(contentSpace_DataContainer.getChildren(parentItemId) == null){
		    	 BaseContentObject currentContentObject;
		    	 String currentContentObjectName;
		         ContentOperationHelper coh = ContentComponentFactory.getContentOperationHelper();
		         String currentContentType;
		         List<BaseContentObject> childrenContentObjectList = parentObject.getSubContentObjects(null);
	         
		         String subContentObjectID;
		         for (int i = 0; i < childrenContentObjectList.size(); i++) {
		        	 currentContentObject = childrenContentObjectList.get(i);
		        	 currentContentObjectName = currentContentObject.getContentObjectName();
		        	 currentContentType = coh.getContentObjectType(currentContentObject);
		        	 // Add new sub content object item
		        	 subContentObjectID = parentItemId + ContentSpaceDataProvider.subContentObjectIdPerfix + i;
		        	 Item subContentObjectItem = contentSpace_DataContainer.addItem(subContentObjectID);
		        	 if(subContentObjectItem!=null){
		        		 subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(currentContentObjectName);
		        		 subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(this.currentContentSpace);
		        	 }
		        	 // setParetn node to current content space item
		        	 contentSpace_DataContainer.setParent(subContentObjectID, parentItemId);
		        	 if (currentContentType.equals(ContentOperationHelper.CONTENTTYPE_FOLDEROBJECT)) {
		        		 contentSpace_DataContainer.setChildrenAllowed(subContentObjectID, true);
		        		 contentManagementPanel.contentObjectBrowser.contentSpaceBrowserTree.collapseItemsRecursively(subContentObjectID);
		        	 } else {
		        		 contentSpace_DataContainer.setChildrenAllowed(subContentObjectID, false);
		        	 }
		         }
	    	 }
	    	 
	    	 Collection<?> chinldC=contentSpace_DataContainer.getChildren(parentItemId);	     
		     Iterator<?> it=chinldC.iterator();
		     while(it.hasNext()){
		    	Object currentObjId=it.next();
		    	Object currentNodeName=contentSpace_DataContainer.getItem(currentObjId).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).getValue();
		    	if(currentNodeName.toString().equals(subObjectName)){
		    		this.currentDataObjectItemID=currentObjId;
		    		break;
		    	}     	
		     }	
	    	 
	    	//GET PROPERTIES
	    	 ContentOperationHelper coh = ContentComponentFactory.getContentOperationHelper();
	         if (coh.getContentObjectType(currentObject).equals(ContentOperationHelper.CONTENTTYPE_FOLDEROBJECT)
	                    || coh.getContentObjectType(currentObject).equals(ContentOperationHelper.CONTENTTYPE_STANDALONEOBJECT)) {
	                List<ContentObjectProperty> propertyList = currentObject.getProperties();
	                contentObjectPropertyList.renderPropertyItemList(propertyList);          
	         	}
	     
	        //GET SUBOBJECTS  
	         subContentObjectTable.refreshTableDataSource(currentObject.getSubContentObjects(null),coh);
	     }catch (ContentReposityException e) {
	            e.printStackTrace();
	     } finally {
	    	 if (cs != null) {
	    		 cs.closeContentSpace();
	         }
	     }
	     this.contentManagementPanel.contentObjectBrowser.contentSpaceBrowserTree.select(null);
	 }
	 
	 public void renderParentContentObjectData(){		 
	     ContentSpace cs = null;	     
	     try {
	    	 cs = ContentComponentFactory.connectContentSpace( this.currentContentSpace );	 	   
	    	 BaseContentObject currentObject=cs.getContentObjectByAbsPath(this.currentContentObjectABSPath).getParentContentObject();	    			 
	    	 ObjectCollectionHelper ooh =ContentComponentFactory.getObjectOperationHelper();	    	 
	    	 ContentObjectInheritanceChain coic = ooh.getParentContentObjectsChain(cs, currentObject);
	    	 this.currentContentObjectABSPath = coic.getContentObjectSpacePath();	    	 
	    	 HierarchicalContainer contentSpace_DataContainer=contentManagementPanel.contentObjectBrowser.contentSpace_DataContainer;	    	 
	    	 Object parentID=contentSpace_DataContainer.getParent(this.currentDataObjectItemID);
	    	 this.currentDataObjectItemID=parentID;
	    	 BaseContentObject parentObject=currentObject.getParentContentObject();	    	 
	    	 contentSpaceName.setValue("<span style='color:#333333;margin-left: 10px;'>"+ this.currentContentSpace +"</span>");  
	    	 currentObjecteName.setValue("<b style='color:#ce0000;'>"+currentObject.getContentObjectName()+"</b> ("+currentObject.getCurrentVersion().getCurrentVersionNumber()+")");	    	 
	    	 if(parentObject!=null){
	    		 parentObjectLinkButton.setCaption(parentObject.getContentObjectName());
	    	 }else{	    		
	    		 parentObjectLinkButton.setCaption("");
	    	 }	    	 
	    	 
	    	//GET PROPERTIES
	    	 ContentOperationHelper coh = ContentComponentFactory.getContentOperationHelper();
	         if (coh.getContentObjectType(currentObject).equals(ContentOperationHelper.CONTENTTYPE_FOLDEROBJECT)
	                    || coh.getContentObjectType(currentObject).equals(ContentOperationHelper.CONTENTTYPE_STANDALONEOBJECT)) {
	                List<ContentObjectProperty> propertyList = currentObject.getProperties();
	                contentObjectPropertyList.renderPropertyItemList(propertyList);          
	         	}
	     
	        //GET SUBOBJECTS  
	         subContentObjectTable.refreshTableDataSource(currentObject.getSubContentObjects(null),coh);
	     }catch (ContentReposityException e) {
	            e.printStackTrace();
	     } finally {
	    	 if (cs != null) {
	    		 cs.closeContentSpace();
	         }
	     }
	     this.contentManagementPanel.contentObjectBrowser.contentSpaceBrowserTree.select(null);
	 }
}
