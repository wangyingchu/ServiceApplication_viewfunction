package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;

import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.activityBureauImpl.CCRActivityEngineConstant;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.BinaryContentFileProgressMonitoringUploader;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.util.CommonStyleUtil;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentRepositoryDataUtil;

import org.vaadin.imagefilter.FilterOperation;
import org.vaadin.imagefilter.Image;
import org.vaadin.imagefilter.filters.FitIntoFilter;

public class ActivityAttachmentEditor extends VerticalLayout{
	private static final long serialVersionUID = 3665106290548448965L;
	private UserClientInfo userClientInfo;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
	private String currentContentObjectAbsPath;			
	private String contentSpaceName;
	private String attchmentFolderRootObjectAbsPath;
	private VerticalLayout attachmentElementsContainer;	
	private Label currentObjectLabel;
	private Button parentObjectLinkButton;	
	private String parentContentObjectName;
	private LightContentWindow lightContentWindow;	
	private DialogWindow deleteRootContentObjectWindow;
	private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
	private String activityInstanceID;
	private LightContentWindow attachmentContentWindow;	
	
	public ActivityAttachmentEditor(BusinessActivity businessActivity,UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;		
		HorizontalLayout attachmentHeaderLayout=new HorizontalLayout();
		attachmentHeaderLayout.setWidth("100%");		
		Label attachmentsLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_activityAttachmentLabel")+"</span>",Label.CONTENT_XHTML);
		attachmentHeaderLayout.addComponent(attachmentsLabel);
		
		HorizontalLayout actionButtonLayout=new HorizontalLayout();
		actionButtonLayout.setWidth("40px");
		Button addFolderButton=new Button();		
		addFolderButton.setCaption(null);
		addFolderButton.setIcon(UICommonElementDefination.ICON_attachmentDefine_AddFolder);
		addFolderButton.setStyleName(BaseTheme.BUTTON_LINK);
		addFolderButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addNewFolderButton"));	
		addFolderButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 3268501406356053754L;

			public void buttonClick(ClickEvent event) {	 
				renderAddNewFolderUI();
            }
        });				
		Button addFileButton=new Button();		
		addFileButton.setCaption(null);
		addFileButton.setIcon(UICommonElementDefination.ICON_attachmentDefine_AddFile);
		addFileButton.setStyleName(BaseTheme.BUTTON_LINK);
		addFileButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addNewFileButton"));
		addFileButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = -2410259710285624606L;

			public void buttonClick(ClickEvent event) {	 
				renderAddNewFileUI();
            }
        });				
		actionButtonLayout.addComponent(addFolderButton);
		actionButtonLayout.addComponent(addFileButton);		
		attachmentHeaderLayout.addComponent(actionButtonLayout);
		attachmentHeaderLayout.setComponentAlignment(actionButtonLayout, Alignment.MIDDLE_RIGHT);		
		this.addComponent(attachmentHeaderLayout);
		
		HorizontalLayout directoryBrowserLayout=new HorizontalLayout();
		directoryBrowserLayout.setHeight("23px");
		
		Button rootObjectLinkButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_attachmentRootLabel"));
		rootObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);
		rootObjectLinkButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 3268501406356053754L;

			public void buttonClick(ClickEvent event) {	 
				reloadRootFolder();
            }
        });		
		directoryBrowserLayout.addComponent(rootObjectLinkButton);		
		
		HorizontalLayout spaceDivLayout1=new HorizontalLayout();
		spaceDivLayout1.setWidth("5px");
		directoryBrowserLayout.addComponent(spaceDivLayout1);
		
		parentObjectLinkButton=new Button("");
		parentObjectLinkButton.setCaption("");
		parentObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);
		directoryBrowserLayout.addComponent(parentObjectLinkButton);
		parentObjectLinkButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = -2410259710285624606L;

			public void buttonClick(ClickEvent event) {	 
				loadParentFolder();
            }
        });		
		
		HorizontalLayout spaceDivLayout2=new HorizontalLayout();
		spaceDivLayout2.setWidth("5px");
		directoryBrowserLayout.addComponent(spaceDivLayout2);
		
		currentObjectLabel=new Label("",Label.CONTENT_XHTML);
		directoryBrowserLayout.addComponent(currentObjectLabel);		
		this.addComponent(directoryBrowserLayout);	
		
		directoryBrowserLayout.setComponentAlignment(rootObjectLinkButton, Alignment.MIDDLE_LEFT);
		directoryBrowserLayout.setComponentAlignment(parentObjectLinkButton, Alignment.MIDDLE_LEFT);
		directoryBrowserLayout.setComponentAlignment(currentObjectLabel, Alignment.MIDDLE_LEFT);	
		
		attachmentElementsContainer=new VerticalLayout();
		attachmentElementsContainer.setWidth("100%");		
		ContentSpace activityContentSpace = null;		
		try {
			this.contentSpaceName=businessActivity.getActivityDefinition().getActivitySpaceName();			
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);
			RootContentObject activityTypeRootObject=activityContentSpace.getRootContentObject(businessActivity.getActivityDefinition().getActivityType());	
			activityInstanceID=businessActivity.getActivityId();
			BaseContentObject activityInstanceObj=activityTypeRootObject.getSubContentObject(activityInstanceID);
			BaseContentObject activityInstanceAttachmentFolderObject=activityInstanceObj.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ActivityInstanceDefinition_attachment);			
			this.attchmentFolderRootObjectAbsPath="/"+businessActivity.getActivityDefinition().getActivityType()+"/"+businessActivity.getActivityId()+"/"+CCRActivityEngineConstant.ACTIVITYSPACE_ActivityInstanceDefinition_attachment;
			this.currentContentObjectAbsPath=this.attchmentFolderRootObjectAbsPath;
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDataFieldDefinition(activityInstanceAttachmentFolderObject,baseContentObject));
			}		
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {				
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {				
		e.printStackTrace();
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}		
		this.addComponent(attachmentElementsContainer);
	}
	
	private void reloadRootFolder(){
		ContentSpace activityContentSpace = null;
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(attchmentFolderRootObjectAbsPath);	
			currentContentObjectAbsPath=attchmentFolderRootObjectAbsPath;
			currentObjectLabel.setValue("");
			parentObjectLinkButton.setCaption("");
			parentContentObjectName=null;		
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			attachmentElementsContainer.removeAllComponents();
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDataFieldDefinition(activityInstanceAttachmentFolderObject,baseContentObject));
			}		
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
	}
	
	private void loadSubFolder_showFile(String subFolderName){
		ContentSpace activityContentSpace = null;
		ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
		String cType=null;
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);			
			String curObjPath=currentContentObjectAbsPath+"/"+subFolderName;			
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(curObjPath);
			
			cType = coh.getContentObjectType(activityInstanceAttachmentFolderObject);			
			if (cType.equals(ContentOperationHelper.CONTENTTYPE_BINARTCONTENT)) {
				showFileAttachment(subFolderName);
				return;
	        } else if(cType.equals(ContentOperationHelper.CONTENTTYPE_TEXTBINARY)){
	        	showFileAttachment(subFolderName);
	        	return;
	        }			
			if(parentContentObjectName!=null){
				parentObjectLinkButton.setCaption(parentContentObjectName);
			}			
			parentContentObjectName=subFolderName;
			currentContentObjectAbsPath=currentContentObjectAbsPath+"/"+subFolderName;
			
			currentObjectLabel.setValue("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+subFolderName+"</span>");
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			attachmentElementsContainer.removeAllComponents();
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDataFieldDefinition(activityInstanceAttachmentFolderObject,baseContentObject));
			}		
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
	}
	
	private void showFileAttachment(String attachmentFileName){
		ContentSpace activityContentSpace = null;
		ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);								
			BaseContentObject activityInstanceAttachmentObject=activityContentSpace.getContentObjectByAbsPath(currentContentObjectAbsPath);			
			BinaryContent binaryContent=coh.getBinaryContent(activityInstanceAttachmentObject, attachmentFileName);			
			
			final String fileURL=tempFileDir+activityInstanceID+"_"+binaryContent.getContentName();
			Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
			Label propertyNameLable = new Label("<b style='color:#333333;'>"+
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_attachmentFileLabel")+" <span style='color:#ce0000;'>"+binaryContent.getContentName()+"</span></b> ", Label.CONTENT_XHTML);
							
			InputStream is=binaryContent.getContentInputStream();				    
		    File f=new File(tempFileDir+activityInstanceID+"_"+binaryContent.getContentName());	  
		    OutputStream out;
			try {
				out = new FileOutputStream(f);
				byte buf[]=new byte[1024];
				int len;
				while((len=is.read(buf))>0){
				  	out.write(buf,0,len);
				}				    
				out.close();
				is.close();	
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		   
			HorizontalLayout picInfoLayout=new HorizontalLayout();					
			Label versionLabel=new Label("V"+binaryContent.getCurrentVersion());					
			picInfoLayout.addComponent(versionLabel);
			
			HorizontalLayout spaceDiv_1=new HorizontalLayout();
			spaceDiv_1.setWidth("5px");					
			picInfoLayout.addComponent(spaceDiv_1);
			
			Label modifyDateLabel=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_lastModifyLabel")+
					" "+formatter.format(binaryContent.getLastModified().getTime()));
			picInfoLayout.addComponent(modifyDateLabel);
			
			HorizontalLayout spaceDiv_2=new HorizontalLayout();
			spaceDiv_2.setWidth("5px");					
			picInfoLayout.addComponent(spaceDiv_2);					
			
			FileResource fs= new FileResource(f,this.getApplication());					
			Link downloadLink = new Link(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_downloadFileLink"),fs);

			downloadLink.setTargetName("_blank");
			downloadLink.setTargetBorder(Link.TARGET_BORDER_NONE);
			picInfoLayout.addComponent(downloadLink);
			
			VerticalLayout picContainerLayout=new VerticalLayout();					
			picContainerLayout.addComponent(picInfoLayout);
			
			HorizontalLayout spaceDiv_3=new HorizontalLayout();
			spaceDiv_2.setHeight("5px");					
			picContainerLayout.addComponent(spaceDiv_3);
			
			attachmentContentWindow=new LightContentWindow(dataFieldIcon,propertyNameLable,picContainerLayout,null);			
			attachmentContentWindow.setWidth("800px");				
			
			String mimeType=binaryContent.getMimeType();				
			if(mimeType.contains("image/")){
				downloadLink.setCaption(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_fullSizeFileLabel"));
				try{		
					FileInputStream picfs=new FileInputStream(f);
					Image image = new Image(picfs, true);	
					int windowWidth=image.getImage().getWidth();	
					if(image.getImage().getWidth()>=800){
						FilterOperation op = FilterOperation.getByName(FilterOperation.FITINTO);
						FitIntoFilter fif = (FitIntoFilter) op.getFilter();
					    fif.setHeight(800);
					    fif.setWidth(800);
					    image.addOperation(op);
					    image.applyOperations();	
					    windowWidth=800;
					 }
					picfs.close();
					attachmentContentWindow.setWidth(""+windowWidth+"px");					
					picContainerLayout.addComponent(image);							
				}catch (IOException e){					
					e.printStackTrace();
				}		
				
			}else if(mimeType.contains("text/")){				       
				StringBuffer strBuf=new StringBuffer();	
				byte texbuf[]=new byte[1024];
				int txtlen;
				try {
					InputStream textStream= new FileInputStream(f); 					
					while((txtlen=textStream.read(texbuf))>0){
						strBuf.append(new String(texbuf,0,txtlen));										
					}
					textStream.close();
				} catch (IOException e) {						
						e.printStackTrace();
				}
				Label contentLabel=new Label(strBuf.toString(),Label.CONTENT_PREFORMATTED);				
				picContainerLayout.addComponent(contentLabel);
			}									
			attachmentContentWindow.setModal(true);
			attachmentContentWindow.setResizable(false);					
			attachmentContentWindow.addListener(new Window.CloseListener() {			        
				private static final long serialVersionUID = -531788718710743964L;

				public void windowClose(CloseEvent e) {
					File f=new File(fileURL);
					f.delete();													
	            }
	        });					
			this.getApplication().getMainWindow().addWindow(attachmentContentWindow);
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}			
	}
	
	private void loadParentFolder(){			
		int objectNameIdx=currentContentObjectAbsPath.lastIndexOf("/");		
		String newCurrentObjAbsPath=currentContentObjectAbsPath.substring(0,objectNameIdx);		
		objectNameIdx=newCurrentObjAbsPath.lastIndexOf("/");		
		String newCurrentObjName=newCurrentObjAbsPath.substring(objectNameIdx+1,newCurrentObjAbsPath.length());		
		objectNameIdx=newCurrentObjAbsPath.lastIndexOf("/");		
		String newParentObjAbsPath=newCurrentObjAbsPath.substring(0,objectNameIdx);	
		objectNameIdx=newParentObjAbsPath.lastIndexOf("/");		
		String newParentObjName=newParentObjAbsPath.substring(objectNameIdx+1,newParentObjAbsPath.length());
		ContentSpace activityContentSpace = null;
		parentContentObjectName=newCurrentObjName;
		try {			
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);			
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(newCurrentObjAbsPath);				
			if(newParentObjAbsPath.equals(attchmentFolderRootObjectAbsPath)){
				currentObjectLabel.setValue("");
				parentObjectLinkButton.setCaption("");				
			}else{
				parentObjectLinkButton.setCaption(newParentObjName);
			}			
			currentObjectLabel.setValue("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+newCurrentObjName+"</span>");
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			attachmentElementsContainer.removeAllComponents();
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDataFieldDefinition(activityInstanceAttachmentFolderObject,baseContentObject));
			}				
			currentContentObjectAbsPath=newCurrentObjAbsPath;	
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}			
	}
	
	private void reLoadCurrentFolder(){
		ContentSpace activityContentSpace = null;
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);			
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(currentContentObjectAbsPath);			
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			attachmentElementsContainer.removeAllComponents();
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDataFieldDefinition(activityInstanceAttachmentFolderObject,baseContentObject));
			}		
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
	}
	
	private void renderAddNewFileUI(){
		Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addAttachmentWindowTitle")+"</b> ", Label.CONTENT_XHTML);
		lightContentWindow=new LightContentWindow(dataFieldIcon,propertyNameLable,createAddBinaryContentForm(),"500px");		
		lightContentWindow.center();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderAddNewFolderUI(){
		Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addFolderWindowTitle")+"</b> ", Label.CONTENT_XHTML);
		HorizontalLayout formContainerLayout=new HorizontalLayout();
		
		final TextField folderNameDataValue=new TextField();		
		folderNameDataValue.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addFolderNamePrompt"));
		folderNameDataValue.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addFolderNameErrMsg"));
		folderNameDataValue.setWidth("200px");
		folderNameDataValue.setRequired(true);
		formContainerLayout.addComponent(folderNameDataValue);
		
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addFolder_confirmButton"));
		okButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = 5897993606166694344L;

			public void buttonClick(ClickEvent event) {	 				
				if(folderNameDataValue.getValue()!=null&&!folderNameDataValue.getValue().toString().equals("")){
					String newFolderName=folderNameDataValue.getValue().toString();	
					addNewFolder(newFolderName);
				}
            }
        });	
		
		formContainerLayout.addComponent(okButton);		
		lightContentWindow=new LightContentWindow(dataFieldIcon,propertyNameLable,formContainerLayout,"300px");		
		lightContentWindow.center();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void addNewFolder(String newFolderName){
		ContentSpace activityContentSpace = null;
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);			
			BaseContentObject currentAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(currentContentObjectAbsPath);
			currentAttachmentFolderObject.addSubContentObject(newFolderName, null, true);	
			reLoadCurrentFolder();
			closeLightWindow();				
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
	}
	
	private void closeLightWindow(){
		this.getApplication().getMainWindow().removeWindow(lightContentWindow);
		lightContentWindow=null;
	}
	
	private void closeDialogWindow(){
		this.getApplication().getMainWindow().removeWindow(deleteRootContentObjectWindow);
		deleteRootContentObjectWindow=null;
	}
	
	private void deleteAttachment(String attachmentName){
		ContentSpace activityContentSpace = null;
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);			
			BaseContentObject currentAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(currentContentObjectAbsPath);
			currentAttachmentFolderObject.removeSubContentObject(attachmentName, true);				
			reLoadCurrentFolder();						
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
		closeDialogWindow();
	}
	
	private void deleteSubAttachment(final String attachmentName){		
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_deleteAttachmentWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_deleteAttachmentWindowDesc")+
        		" "+ " "+CommonStyleUtil.formatCurrentItemStyle(attachmentName.toString());
		
        Button confirmDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_deleteAttachment_confirmButton"));
        confirmDeleteButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 6839637754493576820L;

			public void buttonClick(ClickEvent event) {	
				deleteAttachment(attachmentName);
            }
        });	
        
        Button cancelDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_deleteAttachment_cancelButton"));        
        cancelDeleteButton.addListener(new Button.ClickListener() {				
			private static final long serialVersionUID = 6839637754493576820L;

			public void buttonClick(ClickEvent event) {	 
				closeDialogWindow();
            }
        });	
        
        cancelDeleteButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmDeleteButton);
        buttonList.add(cancelDeleteButton);        
        deleteRootContentObjectWindow=UIComponentCreator.createDialogWindow_DeleteData_Confirm(windowTitle, windowDesc, new BaseButtonBar(370, 45, Alignment.MIDDLE_RIGHT, buttonList));			
        deleteRootContentObjectWindow.center();
        deleteRootContentObjectWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(deleteRootContentObjectWindow);
	}
	
	public PropertyItem renderDataFieldDefinition(BaseContentObject attachmentFolderObject,final BaseContentObject attachmentFileObject){
		HorizontalLayout definationActionContainer=new HorizontalLayout();		
		definationActionContainer.setWidth("30px");		
		Button deleteObjectButton=new Button();		
		deleteObjectButton.setCaption(null);
		deleteObjectButton.setIcon(UICommonElementDefination.ICON_Button_deleteItem);
		deleteObjectButton.setStyleName(BaseTheme.BUTTON_LINK);		
		deleteObjectButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 6839637754493576820L;

			public void buttonClick(ClickEvent event) {	 
				try {
					deleteSubAttachment(attachmentFileObject.getContentObjectName());
				} catch (ContentReposityException e) {					
					e.printStackTrace();
				}
            }
        });			
		
		definationActionContainer.addComponent(deleteObjectButton);		
		Embedded typeIcon=null;				
		ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();			
		String cType;
		boolean isFileObject=false;
		try {
			cType = coh.getContentObjectType(attachmentFileObject);			
			if (cType.equals(ContentOperationHelper.CONTENTTYPE_FOLDEROBJECT)) {
				deleteObjectButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_deleteFolderButton"));
				typeIcon=new Embedded(null, UICommonElementDefination.ICON_attachmentDefine_Folder);
	        } else if(cType.equals(ContentOperationHelper.CONTENTTYPE_STANDALONEOBJECT)){
	        	deleteObjectButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_deleteFolderButton"));
				typeIcon=new Embedded(null, UICommonElementDefination.ICON_attachmentDefine_Folder);
	        }else{	        	
	        	deleteObjectButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_deleteFileButton"));	        	
	        	isFileObject=true;
	        }			
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
		}		
		HorizontalLayout valueContainerLayout=new HorizontalLayout();
		
		valueContainerLayout.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = -8081523788326372035L;

			public void layoutClick(LayoutClickEvent event) {
				if (event.isDoubleClick()) {					
					try {					
						loadSubFolder_showFile(attachmentFileObject.getContentObjectName());										
					} catch (ContentReposityException e) {						
						e.printStackTrace();
					}
				}				
			}});		
		
		Label objectName=new Label("" ,Label.CONTENT_XHTML);
		valueContainerLayout.addComponent(objectName);
		try {			
			objectName.setValue("<span style='margin-left: 10px;margin-right:5px;color:#1360a8;text-shadow: 1px 1px 1px #eeeeee;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;'>"+
					attachmentFileObject.getContentObjectName()+"</span>");			
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
		}
        if(isFileObject){
        	BinaryContent bco;
			try {
				bco = coh.getBinaryContent(attachmentFolderObject,attachmentFileObject.getContentObjectName());
				typeIcon=new Embedded(null, UICommonElementDefination.getAttachmentFileTypeIcon(bco.getMimeType(),attachmentFileObject.getContentObjectName()));
				typeIcon.setDescription(bco.getMimeType());						 
	        	objectName.setDescription(
	        			this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_fileSizeLabel")+" "+bco.getContentSize()+"B");	        	
	        	Label lastModifyDateLabel=new Label("<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size:7pt;-webkit-text-size-adjust:none;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;'>"
						+formatter.format(bco.getLastModified().getTime())+" -v <span style='color:#ce0000;'>"+bco.getCurrentVersion()+"</span></span>" ,Label.CONTENT_XHTML);
				valueContainerLayout.addComponent(lastModifyDateLabel);	        	
			} catch (ContentReposityException e) {			
				e.printStackTrace();
			}
        }else{
        	try {				
				Label subObjecNum=new Label("<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size: 7pt;-webkit-text-size-adjust:none;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;'> ("
						+attachmentFileObject.getSubContentObjectsCount()+")</span>" ,Label.CONTENT_XHTML);
				valueContainerLayout.addComponent(subObjecNum);
			} catch (ContentReposityException e) {				
				e.printStackTrace();
			}        	
        }        
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,typeIcon,valueContainerLayout,definationActionContainer);
		return namePropertyItem;		
	}
	
	private VerticalLayout createAddBinaryContentForm() {	
	     final VerticalLayout formContainer = new VerticalLayout();
	     final BaseForm newBinaryContentForm = new BaseForm();	     
	     final String nodeABSPath = currentContentObjectAbsPath;	     
	     newBinaryContentForm.setImmediate(true);
	     final List<File> uploadedFileList = new ArrayList<File>();
	     newBinaryContentForm.getLayout().addComponent(new BinaryContentFileProgressMonitoringUploader(uploadedFileList));
	     final Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addFile_confirmButton"));
	     final Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityAttachmentEditor_addFile_cancelButton"),
	    		 new Button.ClickListener() {	
			private static final long serialVersionUID = 1L;

				public void buttonClick(ClickEvent event) {	            	
	                if (!uploadedFileList.isEmpty()) {
	                    uploadedFileList.get(0).delete();
	                }
	                closeLightWindow();
	            }
	        });
	        cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);

	        okButton.addListener(new Button.ClickListener() { 
				private static final long serialVersionUID = 7223732929822357823L;

				public void buttonClick(ClickEvent event) {	               
					ContentRepositoryDataUtil.addBinaryContentFile(contentSpaceName.toString(), nodeABSPath, uploadedFileList.get(0),ContentRepositoryDataUtil.BINARY_CONTENT_TYPE_BINARY);	
					reLoadCurrentFolder();
					closeLightWindow();
					uploadedFileList.get(0).delete();					
	            }
	        });   

	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(okButton);
	        buttonList.add(cancelAddbutton);
	        BaseButtonBar addRootContentObjectButtonBar = new BaseButtonBar(270, 45, Alignment.MIDDLE_RIGHT, buttonList);
	        newBinaryContentForm.getFooter().addComponent(addRootContentObjectButtonBar);
	        formContainer.addComponent(newBinaryContentForm);
	        return formContainer;
	    }
}