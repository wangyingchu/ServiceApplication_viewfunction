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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.terminal.FileResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
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

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
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

public class CommonDocumentsEditor extends VerticalLayout{
	private static final long serialVersionUID = 3665106290548448965L;
	private UserClientInfo userClientInfo;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");		
	private String currentContentObjectAbsPath;			
	private String contentSpaceName;
	private String attchmentFolderRootObjectAbsPath;
	private VerticalLayout attachmentElementsContainer;		
	private LightContentWindow lightContentWindow;	
	private DialogWindow deleteRootContentObjectWindow;
	private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();	
	private LightContentWindow attachmentContentWindow;		
	private VerticalLayout previewPicLayout;	
	private HorizontalLayout navigationLinkLayout;	
	private LinkedList navigationLinkList;
	
	public CommonDocumentsEditor(String activitySpaceName,String documentFolderParentAbsPath,String documentFolderName,UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;		
		this.contentSpaceName=activitySpaceName;			
		this.navigationLinkList=new LinkedList();
		
		HorizontalLayout DocumentEditorContainerLayout=new HorizontalLayout();
		DocumentEditorContainerLayout.setWidth("100%");				
		VerticalLayout documentListLayout=new VerticalLayout();				
		HorizontalLayout spaceDivLayout=new HorizontalLayout();
		spaceDivLayout.setWidth("40px");			
		VerticalLayout documentPropertyLayout=new VerticalLayout();
		documentPropertyLayout.setWidth("200px");	
		
		DocumentEditorContainerLayout.addComponent(documentListLayout);
		DocumentEditorContainerLayout.addComponent(spaceDivLayout);			
		DocumentEditorContainerLayout.addComponent(documentPropertyLayout);			
		DocumentEditorContainerLayout.setExpandRatio(documentListLayout, 1.0F);	
		DocumentEditorContainerLayout.setComponentAlignment(documentPropertyLayout, Alignment.TOP_CENTER);
		this.addComponent(DocumentEditorContainerLayout);
		
		this.previewPicLayout=new VerticalLayout();					
		previewPicLayout.setWidth("200px");		
		documentPropertyLayout.addComponent(previewPicLayout);		
		
		HorizontalLayout attachmentHeaderLayout=new HorizontalLayout();
		attachmentHeaderLayout.setWidth("100%");		
		Label attachmentsLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_documentListLabel")+"</span>",Label.CONTENT_XHTML);
		attachmentHeaderLayout.addComponent(attachmentsLabel);
		
		HorizontalLayout actionButtonLayout=new HorizontalLayout();
		actionButtonLayout.setWidth("40px");
		Button addFolderButton=new Button();		
		addFolderButton.setCaption(null);
		addFolderButton.setIcon(UICommonElementDefination.ICON_attachmentDefine_AddFolder);
		addFolderButton.setStyleName(BaseTheme.BUTTON_LINK);
		addFolderButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFolderButton"));	
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
		addFileButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFileButton"));
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
		documentListLayout.addComponent(attachmentHeaderLayout);
		
		HorizontalLayout directoryBrowserLayout=new HorizontalLayout();
		directoryBrowserLayout.setHeight("23px");
		
		Button rootObjectLinkButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_docRootLabel"));
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
		
		navigationLinkLayout=new HorizontalLayout();		
		directoryBrowserLayout.addComponent(navigationLinkLayout);
		
		HorizontalLayout spaceDivLayout2=new HorizontalLayout();
		spaceDivLayout2.setWidth("5px");
		directoryBrowserLayout.addComponent(spaceDivLayout2);
		
		documentListLayout.addComponent(directoryBrowserLayout);	
		
		directoryBrowserLayout.setComponentAlignment(rootObjectLinkButton, Alignment.MIDDLE_LEFT);
		directoryBrowserLayout.setComponentAlignment(navigationLinkLayout, Alignment.MIDDLE_LEFT);	
		
		attachmentElementsContainer=new VerticalLayout();
		attachmentElementsContainer.setWidth("100%");		
		ContentSpace activityContentSpace = null;		
		try {			
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);			
			BaseContentObject spaceDocFolderParentObject=activityContentSpace.getContentObjectByAbsPath(documentFolderParentAbsPath);				
			BaseContentObject spaceDocFolderObject=spaceDocFolderParentObject.getSubContentObject(documentFolderName);
			if(spaceDocFolderObject==null){
				spaceDocFolderObject=spaceDocFolderParentObject.addSubContentObject(documentFolderName, null, false);
			}		
			this.attchmentFolderRootObjectAbsPath=documentFolderParentAbsPath+"/"+documentFolderName;
			this.currentContentObjectAbsPath=this.attchmentFolderRootObjectAbsPath;					
			List<BaseContentObject> subContentObjList=spaceDocFolderObject.getSubContentObjects(null);			
			ComparatorBaseContentObject comparator=new ComparatorBaseContentObject();
			Collections.sort(subContentObjList, comparator);					
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDocumentDefination(spaceDocFolderObject,baseContentObject));
			}			
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}		
		documentListLayout.addComponent(attachmentElementsContainer);
	}
	
	private void reloadRootFolder(){
		this.previewPicLayout.removeAllComponents();
		ContentSpace activityContentSpace = null;
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(attchmentFolderRootObjectAbsPath);	
			this.currentContentObjectAbsPath=this.attchmentFolderRootObjectAbsPath;			
			navigationLinkLayout.removeAllComponents();
			this.navigationLinkList.clear();			
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			ComparatorBaseContentObject comparator=new ComparatorBaseContentObject();
			Collections.sort(subContentObjList, comparator);
			attachmentElementsContainer.removeAllComponents();
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDocumentDefination(activityInstanceAttachmentFolderObject,baseContentObject));
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
			String curObjPath;
			if(currentContentObjectAbsPath.endsWith("/")){
				curObjPath=currentContentObjectAbsPath+subFolderName;
			}else{
				curObjPath=currentContentObjectAbsPath+"/"+subFolderName;
			}						
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(curObjPath);			
			cType = coh.getContentObjectType(activityInstanceAttachmentFolderObject);			
			if (cType.equals(ContentOperationHelper.CONTENTTYPE_BINARTCONTENT)) {
				showFileAttachment(subFolderName);
				return;
	        } else if(cType.equals(ContentOperationHelper.CONTENTTYPE_TEXTBINARY)){
	        	showFileAttachment(subFolderName);
	        	return;
	        }						
			Button subLevelButton=new Button(subFolderName);
			subLevelButton.setStyleName(BaseTheme.BUTTON_LINK);				
			subLevelButton.addListener(new Button.ClickListener() {			
				private static final long serialVersionUID = 3959719921666046166L;

				public void buttonClick(ClickEvent event) {	 
					reloadNewFolder(event.getButton());
	            }
	        });		
			navigationLinkLayout.addComponent(subLevelButton);
			this.navigationLinkList.add(subLevelButton);				
			HorizontalLayout spaceDivLayout=new HorizontalLayout();
			spaceDivLayout.setWidth("5px");			
			navigationLinkLayout.addComponent(spaceDivLayout);
			this.navigationLinkList.add(spaceDivLayout);
			
			if(currentContentObjectAbsPath.endsWith("/")){
				currentContentObjectAbsPath=currentContentObjectAbsPath+subFolderName;
			}else{
				currentContentObjectAbsPath=currentContentObjectAbsPath+"/"+subFolderName;
			}			
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			ComparatorBaseContentObject comparator=new ComparatorBaseContentObject();
			Collections.sort(subContentObjList, comparator);
			attachmentElementsContainer.removeAllComponents();
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDocumentDefination(activityInstanceAttachmentFolderObject,baseContentObject));
			}		
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
	}
	
	private void reloadNewFolder(Object buttonObject){		
		int clickedButtonIndex=this.navigationLinkList.indexOf(buttonObject);		
		StringBuffer fullObjectPath=new StringBuffer();		
		for(int i=0;i<clickedButtonIndex+1;i++){
			Object currentObj=this.navigationLinkList.get(i);
			if(currentObj instanceof Button){			
				if(!((Button) currentObj).getCaption().equals("")){
					fullObjectPath.append(((Button) currentObj).getCaption());
					fullObjectPath.append("/");
				}				
			}
		}
		
		String parentPath=this.attchmentFolderRootObjectAbsPath+"/"+fullObjectPath;		
		String newParentPath=currentContentObjectAbsPath+"/";
		if(parentPath.equals(newParentPath)){			
			return;
		}		
		
		int remainObj=clickedButtonIndex+2;
		while(remainObj<this.navigationLinkList.size()){
			Object lastObj=this.navigationLinkList.pollLast();
			navigationLinkLayout.removeComponent(((Component)lastObj));
		}		
		
		String newFolderABSPath=this.attchmentFolderRootObjectAbsPath+"/"+fullObjectPath;			
		currentContentObjectAbsPath=newFolderABSPath;		
		ContentSpace activityContentSpace = null;		
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);				
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(newFolderABSPath);
			attachmentElementsContainer.removeAllComponents();
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			ComparatorBaseContentObject comparator=new ComparatorBaseContentObject();
			Collections.sort(subContentObjList, comparator);
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDocumentDefination(activityInstanceAttachmentFolderObject,baseContentObject));
			}	
		
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}		
		renderDocumentProperty(null);
	}
	
	private void renderDocumentProperty(String contentName){
		ContentSpace activityContentSpace = null;
		ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
		String cType=null;
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);			
			String curObjPath;			
			if(contentName!=null){
				if(currentContentObjectAbsPath.endsWith("/")){
					curObjPath=currentContentObjectAbsPath+contentName;
				}else{
					curObjPath=currentContentObjectAbsPath+"/"+contentName;
				}					
			}else{
				curObjPath=currentContentObjectAbsPath;
			}					
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(curObjPath);			
			
			cType = coh.getContentObjectType(activityInstanceAttachmentFolderObject);	
			String nameTxt="<span style='color:#333333;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;font-size:smaller;-webkit-text-size-adjust:none;'>";
			String valueTxt="<span style='color:#0099ff;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;font-size:smaller;-webkit-text-size-adjust:none;'>";
			if (cType.equals(ContentOperationHelper.CONTENTTYPE_BINARTCONTENT)) {				
				this.previewPicLayout.removeAllComponents();				
				BaseContentObject parentObjectFolder=activityContentSpace.getContentObjectByAbsPath(currentContentObjectAbsPath);			
				BinaryContent binaryContent=coh.getBinaryContent(parentObjectFolder, contentName);				
				String mimeType=binaryContent.getMimeType();				
				if(mimeType.contains("image/bmp")||mimeType.contains("image/gif")||mimeType.contains("image/jpeg")||mimeType.contains("image/png")){
					try {
						InputStream picStream=binaryContent.getContentInputStream();					
						Image image = new Image(picStream, true);						
						//int windowWidth=image.getImage().getWidth();						
						FilterOperation op = FilterOperation.getByName(FilterOperation.FITINTO);
				        FitIntoFilter fif = (FitIntoFilter) op.getFilter();				       
				        fif.setWidth(190);
				        image.addOperation(op);	
				        image.applyOperations();
						picStream.close();						
						image.setStyleName("ui_userClient_documentSnapshot");
						this.previewPicLayout.addComponent(image);							
					} catch (IOException e) {
						e.printStackTrace();
					}						
				}else if(mimeType.contains("text/")){
					Embedded textTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Text);
					textTypeIcon.setStyleName("ui_userClient_documentSnapshot");
					this.previewPicLayout.addComponent(textTypeIcon);
				}else if(mimeType.contains("pdf")){
					Embedded fileTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Pdf);
					fileTypeIcon.setStyleName("ui_userClient_documentSnapshot");
					this.previewPicLayout.addComponent(fileTypeIcon);
				}				
				else if(mimeType.contains("msword")){
					Embedded fileTypeIcon=null;					
					if(mimeType.contains("msexcel")){
						fileTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Excel);
					}else if(mimeType.contains("powerpoint")){
						fileTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Ppt);
					}else{
						fileTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Word);
					}					
					fileTypeIcon.setStyleName("ui_userClient_documentSnapshot");
					this.previewPicLayout.addComponent(fileTypeIcon);				
				}else if(mimeType.contains("officedocument.spreadsheetml.sheet")){
					Embedded fileTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Excel);
					fileTypeIcon.setStyleName("ui_userClient_documentSnapshot");
					this.previewPicLayout.addComponent(fileTypeIcon);					
				}else if(mimeType.contains("officedocument.wordprocessingml.document")){
					Embedded fileTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Word);
					fileTypeIcon.setStyleName("ui_userClient_documentSnapshot");
					this.previewPicLayout.addComponent(fileTypeIcon);
				}					
				else{
					Embedded genTypeIcon;
					if(contentName.endsWith(".pptx")){
						genTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Ppt);
					}else{
						genTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Gener);
					}					
					genTypeIcon.setStyleName("ui_userClient_documentSnapshot");
					this.previewPicLayout.addComponent(genTypeIcon);					
				}				
				
				Label nameLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_nameLabel")+
						" </span>"+valueTxt+binaryContent.getContentName()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(nameLabel);
				Label versionLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_versionLabel")+
						" </span>"+valueTxt+binaryContent.getCurrentVersion()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(versionLabel);
				Label sizeLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_sizeLabel")+
						" </span>"+valueTxt+binaryContent.getContentSize()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(sizeLabel);
				Label lastModifyLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_lastModifyLabel")+
						" </span>"+valueTxt+  formatter.format(binaryContent.getLastModified().getTime())+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(lastModifyLabel);
				if(binaryContent.isLocked()){
					Label lockedLabel=new Label(valueTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_lockedDocLabel")+"</span>",Label.CONTENT_XHTML);
					this.previewPicLayout.addComponent(lockedLabel);
				}
				//if(binaryContent.isLinkObject()){
				if(false){
					Label linkedLabel=new Label(valueTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_linkDocLabel")+"</span>",Label.CONTENT_XHTML);
					this.previewPicLayout.addComponent(linkedLabel);
				}					
				return;
	        } else if(cType.equals(ContentOperationHelper.CONTENTTYPE_TEXTBINARY)){
	        	this.previewPicLayout.removeAllComponents();				
				BaseContentObject parentObjectFolder=activityContentSpace.getContentObjectByAbsPath(currentContentObjectAbsPath);			
				BinaryContent binaryContent=coh.getBinaryContent(parentObjectFolder, contentName);	
				Embedded textTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Text);
				textTypeIcon.setStyleName("ui_userClient_documentSnapshot");
				this.previewPicLayout.addComponent(textTypeIcon);
				Label nameLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_nameLabel")+
						" </span>"+valueTxt+binaryContent.getContentName()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(nameLabel);
				Label versionLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_versionLabel")+
						" </span>"+valueTxt+binaryContent.getCurrentVersion()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(versionLabel);
				Label sizeLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_sizeLabel")+
						" </span>"+valueTxt+binaryContent.getContentSize()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(sizeLabel);
				Label lastModifyLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_lastModifyLabel")+
						" </span>"+valueTxt+  formatter.format(binaryContent.getLastModified().getTime())+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(lastModifyLabel);
				if(binaryContent.isLocked()){
					Label lockedLabel=new Label(valueTxt+valueTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_lockedDocLabel")+"</span>",Label.CONTENT_XHTML);
					this.previewPicLayout.addComponent(lockedLabel);
				}
				//if(binaryContent.isLinkObject()){
				if(false){
					Label linkedLabel=new Label(valueTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_linkDocLabel")+"</span>",Label.CONTENT_XHTML);
					this.previewPicLayout.addComponent(linkedLabel);
				}	        	
	        	return;
	        }else{
	        	Embedded folderTypeIcon=new Embedded(null, UICommonElementDefination.ICON_documentType_Folder);
	        	folderTypeIcon.setStyleName("ui_userClient_documentSnapshot");
	        	this.previewPicLayout.removeAllComponents();	        	
	        	this.previewPicLayout.addComponent(folderTypeIcon);
	        	Label nameLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_nameLabel")+
	        			" </span>"+valueTxt+activityInstanceAttachmentFolderObject.getContentObjectName()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(nameLabel);
				Label versionLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_versionLabel")+
						" </span>"+valueTxt+activityInstanceAttachmentFolderObject.getCurrentVersion().getCurrentVersionNumber()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(versionLabel);				
				Label childNumberLabel=new Label(nameTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_childrenNoLabel")+
						" </span>"+valueTxt+activityInstanceAttachmentFolderObject.getSubContentObjectsCount()+"</span>",Label.CONTENT_XHTML);
				this.previewPicLayout.addComponent(childNumberLabel);				
				if(activityInstanceAttachmentFolderObject.isLocked()){
					Label lockedLabel=new Label(valueTxt+valueTxt+valueTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_lockedDocLabel")+"</span>",Label.CONTENT_XHTML);
					this.previewPicLayout.addComponent(lockedLabel);
				}
				//if(activityInstanceAttachmentFolderObject.isLinkContentObject()){
				if(false){
					Label linkedLabel=new Label(valueTxt+valueTxt+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_linkDocLabel")+"</span>",Label.CONTENT_XHTML);
					this.previewPicLayout.addComponent(linkedLabel);
				}	
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
			
			final String fileURL=tempFileDir+binaryContent.getContentName();
			Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
			Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_attachmentFileLabel")+
					" <span style='color:#ce0000;'>"+binaryContent.getContentName()+"</span></b> ", Label.CONTENT_XHTML);							
			InputStream is=binaryContent.getContentInputStream();				    
		    File f=new File(tempFileDir+binaryContent.getContentName());	  
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
			
			Label modifyDateLabel=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_attachmentLastModifyLabel")+
					" "+formatter.format(binaryContent.getLastModified().getTime()));
			picInfoLayout.addComponent(modifyDateLabel);
			
			HorizontalLayout spaceDiv_2=new HorizontalLayout();
			spaceDiv_2.setWidth("5px");					
			picInfoLayout.addComponent(spaceDiv_2);					
			
			FileResource fs= new FileResource(f,this.getApplication());					
			Link downloadLink = new Link(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_downloadFileLink"),fs);

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
			if(mimeType.contains("image/bmp")||mimeType.contains("image/gif")||mimeType.contains("image/jpeg")||mimeType.contains("image/png")){
				downloadLink.setCaption(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_fullSizeFileLink"));
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
	
	private void reLoadCurrentFolder(){
		ContentSpace activityContentSpace = null;
		try {
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.contentSpaceName);			
			BaseContentObject activityInstanceAttachmentFolderObject=activityContentSpace.getContentObjectByAbsPath(currentContentObjectAbsPath);			
			List<BaseContentObject> subContentObjList=activityInstanceAttachmentFolderObject.getSubContentObjects(null);
			ComparatorBaseContentObject comparator=new ComparatorBaseContentObject();
			Collections.sort(subContentObjList, comparator);
			attachmentElementsContainer.removeAllComponents();			
			for(BaseContentObject baseContentObject:subContentObjList){
				attachmentElementsContainer.addComponent(renderDocumentDefination(activityInstanceAttachmentFolderObject,baseContentObject));
			}		
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
	}
	
	private void renderAddNewFileUI(){
		Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFileWindowTitle")+"</b> ", Label.CONTENT_XHTML);
		lightContentWindow=new LightContentWindow(dataFieldIcon,propertyNameLable,createAddBinaryContentForm(),"500px");		
		lightContentWindow.center();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderAddNewFolderUI(){
		Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFolderWindowTitle")+"</b> ", Label.CONTENT_XHTML);
		HorizontalLayout formContainerLayout=new HorizontalLayout();
		
		final TextField folderNameDataValue=new TextField();		
		folderNameDataValue.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFolderPrompt"));
		folderNameDataValue.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFolderErrMsg"));
		folderNameDataValue.setWidth("200px");
		folderNameDataValue.setRequired(true);
		formContainerLayout.addComponent(folderNameDataValue);
		
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFolder_confirmButton"));
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
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_deleteAttachmentWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_deleteAttachmentWindowDesc")+" "+ " "+CommonStyleUtil.formatCurrentItemStyle(attachmentName.toString());
		
        Button confirmDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_deleteAttachment_confirmButton"));
        confirmDeleteButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 6839637754493576820L;

			public void buttonClick(ClickEvent event) {	
				deleteAttachment(attachmentName);
            }
        });	
        
        Button cancelDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_deleteAttachment_cancelButton"));        
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
	
	public PropertyItem renderDocumentDefination(BaseContentObject attachmentFolderObject,final BaseContentObject attachmentFileObject){
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
				deleteObjectButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_deleteFolderButton"));
				typeIcon=new Embedded(null, UICommonElementDefination.ICON_attachmentDefine_Folder);
	        } else if(cType.equals(ContentOperationHelper.CONTENTTYPE_STANDALONEOBJECT)){
	        	deleteObjectButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_deleteFolderButton"));
				typeIcon=new Embedded(null, UICommonElementDefination.ICON_attachmentDefine_Folder);
	        }else{	        	
	        	deleteObjectButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_deleteFileButton"));	        	
	        	isFileObject=true;
	        }			
		} catch (ContentReposityException e1) {			
			e1.printStackTrace();
		}		
		HorizontalLayout valueContainerLayout=new HorizontalLayout();
		
		valueContainerLayout.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = -8081523788326372035L;

			public void layoutClick(LayoutClickEvent event) {					
				if(event.getButton()==event.BUTTON_RIGHT){
					return;
				}
				try {
					renderDocumentProperty(attachmentFileObject.getContentObjectName());
				} catch (ContentReposityException e1) {					
					e1.printStackTrace();
				}					
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
			objectName.setValue("<span style='margin-left: 10px;margin-right:5px;color:#1360a8;text-shadow: 1px 1px 1px #eeeeee;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;cursor:pointer;'>"+
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
	        	objectName.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_sizeLabel2")+" "+bco.getContentSize()+"B");	        	
	        	Label lastModifyDateLabel=new Label("<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size:7pt;-webkit-text-size-adjust:none;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;cursor:pointer;'>"
						+formatter.format(bco.getLastModified().getTime())+" -v <span style='color:#ce0000;'>"+bco.getCurrentVersion()+"</span></span>" ,Label.CONTENT_XHTML);
				valueContainerLayout.addComponent(lastModifyDateLabel);	        	
			} catch (ContentReposityException e) {			
				e.printStackTrace();
			}
        }else{
        	try {				
				Label subObjecNum=new Label("<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size: 7pt;-webkit-text-size-adjust:none;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;cursor:pointer;'> ("
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
	     final Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFile_confirmButton"));
	     final Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_CommonDocumentsEditor_addFile_cancelButton"), new Button.ClickListener() {	
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

class ComparatorBaseContentObject implements Comparator{
	private ContentOperationHelper coh=ContentComponentFactory.getContentOperationHelper();
	public int compare(Object arg0, Object arg1) {
		BaseContentObject contentObj1=(BaseContentObject)arg0;
		BaseContentObject contentObj2=(BaseContentObject)arg1;		
		boolean obj1IsFolder;
		boolean obj2IsFolder;
		try {
			String cType1 = coh.getContentObjectType(contentObj1);			
			if (cType1.equals(ContentOperationHelper.CONTENTTYPE_BINARTCONTENT)||cType1.equals(ContentOperationHelper.CONTENTTYPE_TEXTBINARY)) {
				obj1IsFolder=false;
	        } else {
	        	obj1IsFolder=true;
	        }			
			String cType2 = coh.getContentObjectType(contentObj2);
			if (cType2.equals(ContentOperationHelper.CONTENTTYPE_BINARTCONTENT)||cType2.equals(ContentOperationHelper.CONTENTTYPE_TEXTBINARY)) {
				obj2IsFolder=false;
	        } else {
	        	obj2IsFolder=true;
	        }			
			if(obj1IsFolder){				
				if(obj2IsFolder){					
					String obj1Name=contentObj1.getContentObjectName();
					String obj2Name=contentObj2.getContentObjectName();				
					return obj1Name.compareTo(obj2Name);					
				}else{					
					return -1;		
				}					
			}else{
				if(obj2IsFolder){
					return 1;
				}else{
					String obj1Name=contentObj1.getContentObjectName();
					String obj2Name=contentObj2.getContentObjectName();				
					return obj1Name.compareTo(obj2Name);
				}					
			}
		} catch (ContentReposityException e) {			
			e.printStackTrace();
		}
		return 0;
	}
}