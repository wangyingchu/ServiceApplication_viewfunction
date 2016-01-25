package com.viewfunction.vfbpm.adminCenter.UI.contentManagement.contentBrowserTree;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.Action;
import com.vaadin.ui.Window;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.util.ContentObjectUIElementCreator;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.util.RootContentObjectUIElementCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentSpaceDataProvider;

public class ContentSpaceBrowserTreeContextMenuActionHandler  implements Action.Handler {	
	private static final long serialVersionUID = -632024130338666526L;	
    private ContentSpaceBrowserTree contentSpaceBrowserTree;
    private HierarchicalContainer contentSpace_DataContainer;
    private UserClientInfo userClientInfo; 
    
    private RootContentObjectUIElementCreator rootContentObjectUIElementCreator;
    private ContentObjectUIElementCreator contentObjectUIElementCreator;
    
    // Actions for the context menu
    private final static Action ACTION_ADD_RootContentObject=new Action(""); 
    private final static Action ACTION_DELETE_RootContentObject =new Action("");
    private final static Action ACTION_ADD_SubContentObject =new Action("");
    private final static Action ACTION_DELETE_SubContentObject=new Action("");
    private final static Action ACTION_ADD_BINARY_CONTENT =new Action("");
    private final static Action ACTION_ADD_TEXT_CONTENT=new Action("");   
    private static final Action[] ContentSpace_ACTIONS = new Action[]{
        ACTION_ADD_RootContentObject
    };
    private static final Action[] RootContentObject_ACTIONS = new Action[]{
        ACTION_DELETE_RootContentObject, ACTION_ADD_SubContentObject
    };
    private static final Action[] SubContentObject_ACTIONS = new Action[]{
        ACTION_DELETE_SubContentObject, ACTION_ADD_SubContentObject, ACTION_ADD_BINARY_CONTENT, ACTION_ADD_TEXT_CONTENT
    };

    public ContentSpaceBrowserTreeContextMenuActionHandler(ContentSpaceBrowserTree tree, HierarchicalContainer dataContainer) {
        this.contentSpaceBrowserTree = tree;
        this.contentSpace_DataContainer = dataContainer;        
        this.userClientInfo=this.contentSpaceBrowserTree.contentObjectBrowser.userClientInfo;  
            
        ACTION_ADD_RootContentObject.setCaption(
        		userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuItem_addRootObject"));       
        ACTION_ADD_RootContentObject.setIcon(UICommonElementDefination.AppMenu_addRootObjectMenuIcon);
        
        ACTION_DELETE_RootContentObject.setCaption(
        		userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuItem_deleteCurrentRootObject"));
        ACTION_DELETE_RootContentObject.setIcon(UICommonElementDefination.AppMenu_deleteRootObjectMenuIcon);
        
        ACTION_ADD_SubContentObject.setCaption(
        		userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuItem_addSubObject"));
        ACTION_ADD_SubContentObject.setIcon(UICommonElementDefination.AppMenu_addSubObjectMenuIcon);
        
        ACTION_DELETE_SubContentObject.setCaption(
        		userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuItem_deleteCurrentObject"));
        ACTION_DELETE_SubContentObject.setIcon(UICommonElementDefination.AppMenu_deleteSubObjectMenuIcon);
        
        ACTION_ADD_BINARY_CONTENT.setCaption(
        		userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuItem_addBinaryObject"));
        ACTION_ADD_BINARY_CONTENT.setIcon(UICommonElementDefination.AppMenu_addBinaryObjectMenuIcon);
        
        ACTION_ADD_TEXT_CONTENT.setCaption(
        		userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuItem_addTextObject"));        
        ACTION_ADD_TEXT_CONTENT.setIcon(UICommonElementDefination.AppMenu_addTextObjectMenuIcon);        
        
    }
    
	public Action[] getActions(Object target, Object sender) {			
		if(target==null){
			return null;
		}		
		if (target.toString().startsWith(ContentSpaceDataProvider.contentSpaceIdPerfix)) {
			return ContentSpace_ACTIONS;
	    } else if (target.toString().contains(ContentSpaceDataProvider.subContentObjectIdPerfix)) {
	    	return SubContentObject_ACTIONS;
	    } else {
	        return RootContentObject_ACTIONS;
	    }
	}

	public void handleAction(Action action, Object sender, Object target) {
		 if (action == ACTION_ADD_RootContentObject) {
			 if(this.rootContentObjectUIElementCreator==null){
				 this.rootContentObjectUIElementCreator=
						 new RootContentObjectUIElementCreator(this.contentSpace_DataContainer,this.userClientInfo,this.contentSpaceBrowserTree.getApplication());
			 }		   
			 Window addRootObjW=rootContentObjectUIElementCreator.createAddRootContentObjectFormWindow(target);			 
			 addRootObjW.setPositionX(100);
			 addRootObjW.setPositionY(100);
			 this.contentSpaceBrowserTree.getApplication().getMainWindow().addWindow(addRootObjW);	
		 }else if(action == ACTION_DELETE_RootContentObject){
			 if(this.rootContentObjectUIElementCreator==null){
				 this.rootContentObjectUIElementCreator=
						 new RootContentObjectUIElementCreator(this.contentSpace_DataContainer,this.userClientInfo,this.contentSpaceBrowserTree.getApplication());
			 }		   
			 Window deleteRootObjW=rootContentObjectUIElementCreator.createDeleteRootContentObjectConfirmWindow(target);
			 deleteRootObjW.setPositionX(100);
			 deleteRootObjW.setPositionY(100);
			 this.contentSpaceBrowserTree.getApplication().getMainWindow().addWindow(deleteRootObjW);			 
		 }else if(action == ACTION_ADD_SubContentObject){
			 if(this.contentObjectUIElementCreator==null){				 
				 this.contentObjectUIElementCreator=
						 new ContentObjectUIElementCreator(this.contentSpace_DataContainer,this.userClientInfo,contentSpaceBrowserTree.contentObjectBrowser.getContentManagementPanel());
			 }	
			 Window addObjW=contentObjectUIElementCreator.createAddContentObjectFormWindow(target);			 
			 addObjW.setPositionX(100);
			 addObjW.setPositionY(100);
			 this.contentSpaceBrowserTree.getApplication().getMainWindow().addWindow(addObjW);			 
		 }else if(action == ACTION_DELETE_SubContentObject){
			 if(this.contentObjectUIElementCreator==null){
				 this.contentObjectUIElementCreator=
						 new ContentObjectUIElementCreator(this.contentSpace_DataContainer,this.userClientInfo,contentSpaceBrowserTree.contentObjectBrowser.getContentManagementPanel());
			 }	
			 Window deleteObjW=contentObjectUIElementCreator.createDeleteContentObjectConfirmWindow(target);
			 deleteObjW.setPositionX(100);
			 deleteObjW.setPositionY(100);
			 this.contentSpaceBrowserTree.getApplication().getMainWindow().addWindow(deleteObjW);				 
		 }else if(action == ACTION_ADD_BINARY_CONTENT){
			 if(this.contentObjectUIElementCreator==null){
				 this.contentObjectUIElementCreator=
						 new ContentObjectUIElementCreator(this.contentSpace_DataContainer,this.userClientInfo,contentSpaceBrowserTree.contentObjectBrowser.getContentManagementPanel());
			 }
			 Window addBinObjW=contentObjectUIElementCreator.createAddBinaryContentFormWindow(target);
			 addBinObjW.setPositionX(100);
			 addBinObjW.setPositionY(100);
			 this.contentSpaceBrowserTree.getApplication().getMainWindow().addWindow(addBinObjW);			 
		 }else if(action == ACTION_ADD_TEXT_CONTENT){
			 if(this.contentObjectUIElementCreator==null){
				 this.contentObjectUIElementCreator=
						 new ContentObjectUIElementCreator(this.contentSpace_DataContainer,this.userClientInfo,contentSpaceBrowserTree.contentObjectBrowser.getContentManagementPanel());
			 }
			 Window addTxtObjW=contentObjectUIElementCreator.createAddTextContentFormWindow(target);
			 addTxtObjW.setPositionX(100);
			 addTxtObjW.setPositionY(100);
			 this.contentSpaceBrowserTree.getApplication().getMainWindow().addWindow(addTxtObjW);	
		 }		
	}
}
