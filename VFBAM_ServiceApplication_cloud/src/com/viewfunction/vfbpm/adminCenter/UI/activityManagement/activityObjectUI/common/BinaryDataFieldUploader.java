package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import com.vaadin.Application;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.OutputStream;

import javax.jcr.Binary;
import javax.jcr.RepositoryException;

@SuppressWarnings("serial")
public class BinaryDataFieldUploader extends VerticalLayout implements Receiver {
	private Label state = new Label(" <b style='color:#ce0000;'> Idle </b>",Label.CONTENT_XHTML);
	private Label fileName = new Label("", Label.CONTENT_XHTML);
	private Label textualProgress = new Label("", Label.CONTENT_XHTML);
	private Label fileNameLabel;
	private Label uploadingStatueLabel;
	private ProgressIndicator pi = new ProgressIndicator();
	private Upload upload;
	private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
	private boolean isArrayField;
	private List<String> muitlBinaryFieldValueList;
	private Map<String,ActivityData> activityDataMap;
	private DataFieldDefinition dataFieldDefinition;
	private List<Binary> existingBinaryDataList;
	private UserClientInfo userClientInfo;
	public BinaryDataFieldUploader(final Layout parentFileIndacateLayout,final List<String> muitlBinaryFieldValueList,final BaseForm newBinaryPropertyForm, DataFieldDefinition dataFieldDefinition,Map<String,ActivityData> activityDataMap,Application application,UserClientInfo userClientInfo) {
		this.muitlBinaryFieldValueList=muitlBinaryFieldValueList;
		this.isArrayField=dataFieldDefinition.isArrayField();
		this.activityDataMap=activityDataMap;
		this.dataFieldDefinition=dataFieldDefinition;
		this.existingBinaryDataList=new ArrayList<Binary>();
		this.userClientInfo=userClientInfo;		
		fileNameLabel = new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_fileNameLabel")+" ");
		uploadingStatueLabel = new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_uploadProgressLabel")+" ");		
		this.setMargin(true);
		this.setMargin(true, false, false, false);
		upload = new Upload(null, this);
		// make analyzing start immediatedly when file is selected
		upload.setImmediate(true);
		upload.setButtonCaption(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_selectFileButton"));
		final Button cancelProcessing = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_cancelUploadButton"));
		cancelProcessing.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				upload.interruptUpload();
			}
		});

		cancelProcessing.setVisible(false);
		cancelProcessing.setStyleName(BaseTheme.BUTTON_LINK);
		HorizontalLayout controlButtonContainerLayout = new HorizontalLayout();
		controlButtonContainerLayout.setWidth("300px");
		controlButtonContainerLayout.addComponent(upload);
		controlButtonContainerLayout.addComponent(cancelProcessing);
		controlButtonContainerLayout.setComponentAlignment(upload,Alignment.MIDDLE_LEFT);
		controlButtonContainerLayout.setComponentAlignment(cancelProcessing,Alignment.MIDDLE_LEFT);
		addComponent(controlButtonContainerLayout);

		VerticalLayout statusContainerLayout = new VerticalLayout();
		statusContainerLayout.setSpacing(true);

		HorizontalLayout stateStatueContainerLayout = new HorizontalLayout();
		stateStatueContainerLayout.addComponent(new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_statusLabel")+" "));
		stateStatueContainerLayout.addComponent(state);
		statusContainerLayout.addComponent(stateStatueContainerLayout);

		HorizontalLayout fileNameContainerLayout = new HorizontalLayout();
		fileNameContainerLayout.addComponent(fileNameLabel);
		fileNameLabel.setVisible(false);
		fileNameContainerLayout.addComponent(fileName);
		fileName.setVisible(false);

		statusContainerLayout.addComponent(fileNameContainerLayout);

		HorizontalLayout uploadProgressContainerLayout = new HorizontalLayout();
		uploadProgressContainerLayout.addComponent(uploadingStatueLabel);
		uploadProgressContainerLayout.addComponent(pi);
		uploadingStatueLabel.setVisible(false);
		pi.setVisible(false);
		uploadProgressContainerLayout.setComponentAlignment(uploadingStatueLabel, Alignment.MIDDLE_LEFT);
		uploadProgressContainerLayout.setComponentAlignment(pi,Alignment.MIDDLE_LEFT);

		statusContainerLayout.addComponent(uploadProgressContainerLayout);
		textualProgress.setVisible(false);
		HorizontalLayout uploadTextualProgressContainerLayout = new HorizontalLayout();
		uploadTextualProgressContainerLayout.addComponent(textualProgress);
		statusContainerLayout.addComponent(uploadTextualProgressContainerLayout);
		this.addComponent(statusContainerLayout);
        final String uploadingLabel=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_uploadingLabel");
		upload.addListener(new Upload.StartedListener() {

			public void uploadStarted(StartedEvent event) {
				// this method gets called immediatedly after upload is started
				pi.setValue(0f);
				uploadingStatueLabel.setVisible(true);
				pi.setVisible(true);
				pi.setPollingInterval(500);
				textualProgress.setVisible(true);
				// updates to client
				state.setValue(" <b style='color:#ce0000;'> "+uploadingLabel+" </b>");
				fileNameLabel.setVisible(true);
				fileName.setVisible(true);
				fileName.setValue(" <b style='color:#ce0000;'> "+ event.getFilename() + "</b>");
				cancelProcessing.setVisible(true);
			}
		});

		final String uploadedMsg1=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_uploadedMsg1");
		final String uploadedMsg2=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_uploadedMsg2");
		upload.addListener(new Upload.ProgressListener() {

			public void updateProgress(long readBytes, long contentLength) {
				// this method gets called several times during the update
				pi.setValue(new Float(readBytes / (float) contentLength));
				textualProgress.setValue(uploadedMsg1+" <b style='color:#ce0000;'>"	+ readBytes	+ "</b> "+uploadedMsg2+" <b style='color:#333333;'>"
						+ contentLength + "</b>");
			}
		});
		
		final String deleteUploadedFileButtonStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_deleteUploadedFileButton");
		final String uploadErrMsgStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_uploadErrMsg");
		upload.addListener(new Upload.SucceededListener() {

			public void uploadSucceeded(SucceededEvent event) {								
				final String uploadedFileName = event.getFilename();				
				muitlBinaryFieldValueList.add(uploadedFileName);
				HorizontalLayout addedBinaryFileLayout = new HorizontalLayout();
				addedBinaryFileLayout.setWidth("400px");
				Label uploadedFileNameLabel = new Label("<b style='color:#ce0000;'>" + uploadedFileName	+ "</b>", Label.CONTENT_XHTML);
				resetUploader();
				Button deleteUploadedFileButton = new Button(deleteUploadedFileButtonStr);
				deleteUploadedFileButton.setStyleName(BaseTheme.BUTTON_LINK);
				addedBinaryFileLayout.addComponent(uploadedFileNameLabel);
				addedBinaryFileLayout.addComponent(deleteUploadedFileButton);
				addedBinaryFileLayout.setComponentAlignment(uploadedFileNameLabel, Alignment.MIDDLE_LEFT);
				addedBinaryFileLayout.setComponentAlignment(deleteUploadedFileButton, Alignment.MIDDLE_RIGHT);
				deleteUploadedFileButton.addListener(new Button.ClickListener() {

							public void buttonClick(ClickEvent event) {
								Component parentLayout = event.getComponent().getParent();
								parentFileIndacateLayout.removeComponent(parentLayout);
								muitlBinaryFieldValueList.remove(uploadedFileName);
								new File(tempFileDir + uploadedFileName).delete();
								boolean validateResult = newBinaryPropertyForm.isValid();
								resetUploader();
								String errorMessage = "";
								if (validateResult) {
									if (muitlBinaryFieldValueList.size() == 0) {
										errorMessage = uploadErrMsgStr;
										newBinaryPropertyForm.setValidationVisible(true);
										newBinaryPropertyForm.setComponentError(new UserError(errorMessage));
										validateResult = false;
									} else {
										newBinaryPropertyForm.setValidationVisible(false);
										newBinaryPropertyForm.setComponentError(null);
									}
								}
							}
						});
				parentFileIndacateLayout.addComponent(addedBinaryFileLayout);
				boolean validateResult = newBinaryPropertyForm.isValid();
				String errorMessage = "";
				if (validateResult) {
					if (muitlBinaryFieldValueList.size() == 0) {
						errorMessage = uploadErrMsgStr;
						newBinaryPropertyForm.setValidationVisible(true);
						newBinaryPropertyForm.setComponentError(new UserError(errorMessage));
						validateResult = false;
					} else {
						newBinaryPropertyForm.setValidationVisible(false);
						newBinaryPropertyForm.setComponentError(null);
					}
				}
			}
		});

		upload.addListener(new Upload.FailedListener() {

			public void uploadFailed(FailedEvent event) {
				System.out.println(event.getFilename() + "fail");
			}
		});

		final String idleLabelStr=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_idleLabel");
		upload.addListener(new Upload.FinishedListener() {

			public void uploadFinished(FinishedEvent event) {
				state.setValue(" <b style='color:#ce0000;'> "+idleLabelStr+" </b>");
				fileNameLabel.setVisible(false);
				fileName.setVisible(false);
				uploadingStatueLabel.setVisible(false);
				pi.setVisible(false);
				textualProgress.setVisible(false);
				cancelProcessing.setVisible(false);
			}
		});		
		
		if(!this.isArrayField){
			String fieldName=this.dataFieldDefinition.getFieldName();			
			if(this.activityDataMap!=null&&this.activityDataMap.get(fieldName)!=null){
				final Binary existingBinaryData=(Binary)this.activityDataMap.get(fieldName).getDatFieldValue();				
				if(existingBinaryData!=null){					
					this.existingBinaryDataList.add(existingBinaryData);					
					final HorizontalLayout addedBinaryFileLayout = new HorizontalLayout();
					addedBinaryFileLayout.setWidth("450px");
					Label uploadedFileNameLabel;
					try {
						uploadedFileNameLabel = new Label("<b style='color:#ce0000;'>" + this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_sizeLabel")+
								"["+existingBinaryData.getSize()+"]"	+ "</b>", Label.CONTENT_XHTML);
						resetUploader();
						addedBinaryFileLayout.addComponent(uploadedFileNameLabel);						
						addedBinaryFileLayout.setComponentAlignment(uploadedFileNameLabel, Alignment.MIDDLE_LEFT);						
						HorizontalLayout actionButtonContainer=new HorizontalLayout();
						actionButtonContainer.setWidth("80px");
						addedBinaryFileLayout.addComponent(actionButtonContainer);
						addedBinaryFileLayout.setComponentAlignment(actionButtonContainer, Alignment.MIDDLE_RIGHT);						
						InputStream is = existingBinaryData.getStream();
						final String templeteFileName=tempFileDir+this.dataFieldDefinition.getFieldName()+"_"+existingBinaryData.getSize();
						File f=new File(templeteFileName);	  
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
						FileResource fs= new FileResource(f,application);					
						Link downloadLink = new Link(null,fs);
						
						downloadLink.setIcon(UICommonElementDefination.ICON_userClient_download);						
						downloadLink.setTargetName("_blank");
						downloadLink.setTargetBorder(Link.TARGET_BORDER_NONE);
						downloadLink.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_downloadFileLink"));
						actionButtonContainer.addComponent(downloadLink);					
						
						Button deleteUploadedFileButton = new Button();
						deleteUploadedFileButton.setCaption(null);
						deleteUploadedFileButton.setStyleName(BaseTheme.BUTTON_LINK);
						deleteUploadedFileButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_deleteFileButton"));
						actionButtonContainer.addComponent(deleteUploadedFileButton);
						deleteUploadedFileButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
						deleteUploadedFileButton.addListener(new Button.ClickListener() {									
							public void buttonClick(ClickEvent event) {
								Component parentLayout = event.getComponent().getParent().getParent();
								parentFileIndacateLayout.removeComponent(parentLayout);								
								new File(templeteFileName).delete();
								existingBinaryDataList.remove(existingBinaryData);
								boolean validateResult = newBinaryPropertyForm.isValid();
								resetUploader();
								String errorMessage = "";
								if (validateResult) {
									if (muitlBinaryFieldValueList.size() == 0) {
										errorMessage = uploadErrMsgStr;
										newBinaryPropertyForm.setValidationVisible(true);
										newBinaryPropertyForm.setComponentError(new UserError(errorMessage));
										validateResult = false;
									} else {
										newBinaryPropertyForm.setValidationVisible(false);
										newBinaryPropertyForm.setComponentError(null);
										}
								}
							}
						});	
					} catch (RepositoryException e) {						
						e.printStackTrace();
					}						
					parentFileIndacateLayout.addComponent(addedBinaryFileLayout);
				}
			}				
		}else{
			String fieldName=this.dataFieldDefinition.getFieldName();			
			if(this.activityDataMap!=null&&this.activityDataMap.get(fieldName)!=null){
				Binary[] existingBinarysData=(Binary[])this.activityDataMap.get(fieldName).getDatFieldValue();	
				if(existingBinarysData!=null){
					for(final Binary currentBinary:existingBinarysData){
						this.existingBinaryDataList.add(currentBinary);					
						final HorizontalLayout addedBinaryFileLayout = new HorizontalLayout();
						addedBinaryFileLayout.setWidth("450px");
						Label uploadedFileNameLabel;
						try {
							uploadedFileNameLabel = new Label("<b style='color:#ce0000;'>" + this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_sizeLabel")+
									"["+currentBinary.getSize()+"]"	+ "</b>", Label.CONTENT_XHTML);
							resetUploader();
							addedBinaryFileLayout.addComponent(uploadedFileNameLabel);						
							addedBinaryFileLayout.setComponentAlignment(uploadedFileNameLabel, Alignment.MIDDLE_LEFT);						
							HorizontalLayout actionButtonContainer=new HorizontalLayout();
							actionButtonContainer.setWidth("80px");
							addedBinaryFileLayout.addComponent(actionButtonContainer);
							addedBinaryFileLayout.setComponentAlignment(actionButtonContainer, Alignment.MIDDLE_RIGHT);						
							InputStream is = currentBinary.getStream();
							final String templeteFileName=tempFileDir+this.dataFieldDefinition.getFieldName()+"_"+currentBinary.getSize();
							File f=new File(templeteFileName);	  
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
							FileResource fs= new FileResource(f,application);					
							Link downloadLink = new Link(null,fs);
							
							downloadLink.setIcon(UICommonElementDefination.ICON_userClient_download);						
							downloadLink.setTargetName("_blank");
							downloadLink.setTargetBorder(Link.TARGET_BORDER_NONE);
							downloadLink.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_downloadFileLink"));
							actionButtonContainer.addComponent(downloadLink);					
							
							Button deleteUploadedFileButton = new Button();
							deleteUploadedFileButton.setCaption(null);
							deleteUploadedFileButton.setStyleName(BaseTheme.BUTTON_LINK);
							deleteUploadedFileButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BinaryDataFieldUploader_deleteFileButton"));
							actionButtonContainer.addComponent(deleteUploadedFileButton);
							deleteUploadedFileButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
							deleteUploadedFileButton.addListener(new Button.ClickListener() {									
								public void buttonClick(ClickEvent event) {
									Component parentLayout = event.getComponent().getParent().getParent();
									parentFileIndacateLayout.removeComponent(parentLayout);								
									new File(templeteFileName).delete();
									existingBinaryDataList.remove(currentBinary);
									boolean validateResult = newBinaryPropertyForm.isValid();
									resetUploader();
									String errorMessage = "";
									if (validateResult) {
										if (muitlBinaryFieldValueList.size() == 0) {
											errorMessage = uploadErrMsgStr;
											newBinaryPropertyForm.setValidationVisible(true);
											newBinaryPropertyForm.setComponentError(new UserError(errorMessage));
											validateResult = false;
										} else {
											newBinaryPropertyForm.setValidationVisible(false);
											newBinaryPropertyForm.setComponentError(null);
											}
									}
								}
							});	
						} catch (RepositoryException e) {						
							e.printStackTrace();
						}						
						parentFileIndacateLayout.addComponent(addedBinaryFileLayout);							
					}					
				}			
			}			
		}
	}
	
	private void resetUploader(){
		if(!this.isArrayField&&muitlBinaryFieldValueList.size()>0){
			upload.setEnabled(false);
		}
		if(!this.isArrayField&&muitlBinaryFieldValueList.size()==0){
			if(this.existingBinaryDataList.size()>0){
				upload.setEnabled(false);
			}else{
				upload.setEnabled(true);
			}			
		}
	}

	public OutputStream receiveUpload(String filename, String MIMEType) {
		FileOutputStream fos = null; // Output stream to write to
		File file = new File(tempFileDir + filename);
		try {
			// Open the file for writing.
			fos = new FileOutputStream(file);
		} catch (final java.io.FileNotFoundException e) {
			// Error while opening the file. Not reported here.
			e.printStackTrace();
			return null;
		}
		return fos;
	}
	
	public List<Binary> getExistingBinaryDataList(){
		return this.existingBinaryDataList;
	}
}