package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ActivityDefinitionResourceFileUploader extends VerticalLayout implements Receiver {
	private static final long serialVersionUID = -2021090806987581148L;
	
	private Label state;
    private Label fileName = new Label("", Label.CONTENT_XHTML);
    private Label textualProgress = new Label("", Label.CONTENT_XHTML);
    private Label fileNameLabel;
    private Label uploadingStatueLabel;
    private ProgressIndicator pi = new ProgressIndicator();
    private Upload upload;
    private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
    private HorizontalLayout stateStatueContainerLayout = new HorizontalLayout();
    private VerticalLayout statusContainerLayout = new VerticalLayout();
    private HorizontalLayout controlButtonContainerLayout = new HorizontalLayout();
    
    private UserClientInfo userClientInfo;
	private String activitySpaceName;
	private NewBusinessActivityDefinitionEditor newBusinessActivityDefinitionEditor;		
	
    public ActivityDefinitionResourceFileUploader(String activitySpaceName,UserClientInfo userClientInfo,NewBusinessActivityDefinitionEditor newBusinessActivityDefinitionEditor) {
    	this.activitySpaceName=activitySpaceName;
		this.userClientInfo=userClientInfo;		
		this.newBusinessActivityDefinitionEditor=newBusinessActivityDefinitionEditor;			
		state = new Label(" <b style='color:#ce0000;'> "+userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_idleStatusLabel")+" </b>", Label.CONTENT_XHTML);	
	    fileNameLabel = new Label(userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_fileNameLabel")+" ");
	    uploadingStatueLabel = new Label(userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_uploadingProgressLabel")+" ");			
        upload = new Upload(null, this);
        // make analyzing start immediatedly when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption(userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_setFileResourceButton"));
        final Button cancelProcessing = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_cancelSetFileResourceButton"));
        cancelProcessing.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 811820257115071779L;

			public void buttonClick(ClickEvent event) {
                upload.interruptUpload();
            }
        });
        cancelProcessing.setVisible(false);
        cancelProcessing.setStyleName(BaseTheme.BUTTON_LINK);

        controlButtonContainerLayout.setWidth("500px");
        controlButtonContainerLayout.addComponent(upload);
        controlButtonContainerLayout.addComponent(cancelProcessing);
        controlButtonContainerLayout.setComponentAlignment(upload, Alignment.MIDDLE_LEFT);
        controlButtonContainerLayout.setComponentAlignment(cancelProcessing, Alignment.MIDDLE_LEFT);
        addComponent(controlButtonContainerLayout);
        this.setComponentAlignment(controlButtonContainerLayout, Alignment.MIDDLE_LEFT);
        statusContainerLayout.setSpacing(true);

        stateStatueContainerLayout.setVisible(false);
        stateStatueContainerLayout.addComponent(new Label(userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_currentStatusLabel")+" "));
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
        uploadProgressContainerLayout.setComponentAlignment(pi, Alignment.MIDDLE_LEFT);

        statusContainerLayout.addComponent(uploadProgressContainerLayout);
        textualProgress.setVisible(false);
        HorizontalLayout uploadTextualProgressContainerLayout = new HorizontalLayout();
        uploadTextualProgressContainerLayout.addComponent(textualProgress);
        statusContainerLayout.addComponent(uploadTextualProgressContainerLayout);
        this.addComponent(statusContainerLayout);

        final String uploadingLabelString=userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_uploadingLabel");
        upload.addListener(new Upload.StartedListener() {
			private static final long serialVersionUID = -106425358639683463L;

			public void uploadStarted(StartedEvent event) {
                // this method gets called immediatedly after upload is started
                stateStatueContainerLayout.setVisible(true);
                pi.setValue(0f);
                uploadingStatueLabel.setVisible(true);
                pi.setVisible(true);
                pi.setPollingInterval(500);
                textualProgress.setVisible(true);
                // updates to client
                state.setValue(" <b style='color:#ce0000;'> "+uploadingLabelString+" </b>");
                fileNameLabel.setVisible(true);
                fileName.setVisible(true);
                fileName.setValue(" <b style='color:#ce0000;'> " + event.getFilename() + "</b>");
                cancelProcessing.setVisible(true);
            }
        });
        
        final String uploadProcessLabel1String=userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_uploadProcessLabel1");
        final String uploadProcessLabel2String=userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_uploadProcessLabel2");
        upload.addListener(new Upload.ProgressListener() {
			private static final long serialVersionUID = 2705074854725187699L;

			public void updateProgress(long readBytes, long contentLength) {
                // this method gets called several times during the update
                pi.setValue(new Float(readBytes / (float) contentLength));
                textualProgress.setValue(uploadProcessLabel1String+" <b style='color:#ce0000;'>" + readBytes + "</b> "+uploadProcessLabel2String+" <b style='color:#333333;'>" + contentLength + "</b>");
            }
        });

        upload.addListener(new Upload.SucceededListener() {
			private static final long serialVersionUID = 7351799177456403728L;

			public void uploadSucceeded(SucceededEvent event) {
            }
        });
        final String uploadFailString=userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_uploadFailLabel");
        upload.addListener(new Upload.FailedListener() {
			private static final long serialVersionUID = 6343250891564969765L;

			public void uploadFailed(FailedEvent event) {
                System.out.println(event.getFilename() + uploadFailString);
            }
        });
        
        final String messageString=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_idleStatusLabel");        
        final String successMessage1String=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_uploadSuccessLabel1");
        final String successMessage2String=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_uploadSuccessLabel2");
        final String errorMessage1String=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_activityTypeExistErrorMsg1");
        final String errorMessage2String=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_activityTypeExistErrorMsg2"); 
        final String errorMessage3String=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityDefinitionResourceFileUploader_notDefineFileErrorMsg");         
        upload.addListener(new Upload.FinishedListener() {
			private static final long serialVersionUID = 1941351986811969858L;			
			
			public void uploadFinished(FinishedEvent event) {
                state.setValue(" <b style='color:#ce0000;'> "+messageString+" </b>");
                state.setVisible(false);
                fileNameLabel.setVisible(false);
                fileName.setVisible(false);
                uploadingStatueLabel.setVisible(false);
                pi.setVisible(false);
                textualProgress.setVisible(false);
                cancelProcessing.setVisible(false);
                upload.setVisible(false);
                controlButtonContainerLayout.removeAllComponents();
                stateStatueContainerLayout.removeAllComponents();
                controlButtonContainerLayout.setWidth("500px");   
                Label confirmLabel =null;                
                if(event.getFilename().endsWith(".bpmn20.xml")){
                	String definitionFileName=event.getFilename();
                	String activityTpye=definitionFileName.replace(".bpmn20.xml", "");                	
                	if(!isAlreadyHasActivityType(activityTpye)){
                		setActivityTypeDefinitionData(activityTpye,new File(tempFileDir + event.getFilename()));                 		
                		confirmLabel = new Label(successMessage1String+" <b style='color:#ce0000;'>" + definitionFileName + "</b> "+successMessage2String, Label.CONTENT_XHTML);
                		controlButtonContainerLayout.addComponent(confirmLabel);
                        controlButtonContainerLayout.setComponentAlignment(confirmLabel, Alignment.MIDDLE_RIGHT);
                	}else{
                		setActivityTypeDefinitionData(null,null);
                		confirmLabel = new Label(errorMessage1String+" <b style='color:#ce0000;'>" + activityTpye + "</b> "+errorMessage2String, Label.CONTENT_XHTML);
                		controlButtonContainerLayout.addComponent(confirmLabel);
                        controlButtonContainerLayout.setComponentAlignment(confirmLabel, Alignment.MIDDLE_RIGHT);
                	}                	
                }else if(event.getFilename().endsWith(".bpmn")){
                	String definitionFileName=event.getFilename();
                	String activityTpye=definitionFileName.replace(".bpmn", "");                	
                	if(!isAlreadyHasActivityType(activityTpye)){
                		setActivityTypeDefinitionData(activityTpye,new File(tempFileDir + event.getFilename()));                 		
                		confirmLabel = new Label(successMessage1String+" <b style='color:#ce0000;'>" + definitionFileName + "</b> "+successMessage2String, Label.CONTENT_XHTML);
                		controlButtonContainerLayout.addComponent(confirmLabel);
                        controlButtonContainerLayout.setComponentAlignment(confirmLabel, Alignment.MIDDLE_RIGHT);
                	}else{
                		setActivityTypeDefinitionData(null,null);
                		confirmLabel = new Label(errorMessage1String+" <b style='color:#ce0000;'>" + activityTpye + "</b> "+errorMessage2String, Label.CONTENT_XHTML);
                		controlButtonContainerLayout.addComponent(confirmLabel);
                        controlButtonContainerLayout.setComponentAlignment(confirmLabel, Alignment.MIDDLE_RIGHT);
                	}                	
                	
                }else{
                	setActivityTypeDefinitionData(null,null);
                	confirmLabel = new Label("<b style='color:#ce0000;'>" + event.getFilename() + "</b> "+errorMessage3String, Label.CONTENT_XHTML);
                	controlButtonContainerLayout.addComponent(confirmLabel);
                    controlButtonContainerLayout.setComponentAlignment(confirmLabel, Alignment.MIDDLE_RIGHT);
                }
            }
        });    }

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

    private boolean isAlreadyHasActivityType(String activityType){
    	ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);
    	try {
			String[] activityTypeArray=activitySpace.getBusinessActivityTypes();
			if(activityTypeArray==null||activityTypeArray.length==0){
				return false;
			}else{
				for(String curActivityType:activityTypeArray){
					if(curActivityType.equals(activityType)){
						return true;
					}
				}				
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}    	
    	return false; 	
    } 
    
    private void setActivityTypeDefinitionData(String activityType,File definitionFile){
    	newBusinessActivityDefinitionEditor.activityType=activityType;
    	newBusinessActivityDefinitionEditor.activityDefinitionResourceFile=definitionFile;
    	if(activityType!=null){   		
    		newBusinessActivityDefinitionEditor.activityTypeLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+activityType+"</span>");
    		newBusinessActivityDefinitionEditor.activityDefinitionResourceFileLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+activityType+".bpmn20.xml"+"</span>");
    	}else{
    		newBusinessActivityDefinitionEditor.activityTypeLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
    		newBusinessActivityDefinitionEditor.activityDefinitionResourceFileLabel.setValue("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+""+"</span>");
    	}    	
    }    
}