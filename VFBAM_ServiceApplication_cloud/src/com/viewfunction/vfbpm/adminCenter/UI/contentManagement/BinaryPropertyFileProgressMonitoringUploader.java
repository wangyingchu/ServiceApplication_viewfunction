/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.contentManagement;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.util.RuntimeEnvironmentUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.io.OutputStream;

@SuppressWarnings("serial")
/**
 *
 * @author wangychu
 */
public class BinaryPropertyFileProgressMonitoringUploader extends VerticalLayout implements Receiver {

    private Label state = new Label(" <b style='color:#ce0000;'> Idle </b>", Label.CONTENT_XHTML);
    private Label fileName = new Label("", Label.CONTENT_XHTML);
    private Label textualProgress = new Label("", Label.CONTENT_XHTML);
    private Label fileNameLabel = new Label("File name : ");
    private Label uploadingStatueLabel = new Label("Uploading progress ");
    private ProgressIndicator pi = new ProgressIndicator();
    private Upload upload;
    private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();

    public BinaryPropertyFileProgressMonitoringUploader(final Layout parentFileIndacateLayout, final List<String> muitlPropertyValueList, final BaseForm newBinaryPropertyForm) {
        this.setMargin(true);
        this.setMargin(true, false, false, false);
        upload = new Upload(null, this);
        // make analyzing start immediatedly when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption("Select file to upload");
        final Button cancelProcessing = new Button("Cancel current upload");
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
        controlButtonContainerLayout.setComponentAlignment(upload, Alignment.MIDDLE_LEFT);
        controlButtonContainerLayout.setComponentAlignment(cancelProcessing, Alignment.MIDDLE_LEFT);
        addComponent(controlButtonContainerLayout);

        VerticalLayout statusContainerLayout = new VerticalLayout();
        statusContainerLayout.setSpacing(true);

        HorizontalLayout stateStatueContainerLayout = new HorizontalLayout();
        stateStatueContainerLayout.addComponent(new Label("Current state : "));
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

        upload.addListener(new Upload.StartedListener() {

            public void uploadStarted(StartedEvent event) {
                // this method gets called immediatedly after upload is started
                pi.setValue(0f);
                uploadingStatueLabel.setVisible(true);
                pi.setVisible(true);
                pi.setPollingInterval(500);
                textualProgress.setVisible(true);
                // updates to client
                state.setValue(" <b style='color:#ce0000;'> Uploading </b>");
                fileNameLabel.setVisible(true);
                fileName.setVisible(true);
                fileName.setValue(" <b style='color:#ce0000;'> " + event.getFilename() + "</b>");
                cancelProcessing.setVisible(true);
            }
        });

        upload.addListener(new Upload.ProgressListener() {

            public void updateProgress(long readBytes, long contentLength) {
                // this method gets called several times during the update
                pi.setValue(new Float(readBytes / (float) contentLength));
                textualProgress.setValue("Processed <b style='color:#ce0000;'>" + readBytes + "</b> bytes of <b style='color:#333333;'>" + contentLength + "</b>");
            }
        });

        upload.addListener(new Upload.SucceededListener() {

            public void uploadSucceeded(SucceededEvent event) {
                final String uploadedFileName = event.getFilename();
                muitlPropertyValueList.add(uploadedFileName);
                HorizontalLayout addedBinaryFileLayout = new HorizontalLayout();
                addedBinaryFileLayout.setWidth("400px");
                Label uploadedFileNameLabel = new Label("<b style='color:#ce0000;'>" + uploadedFileName + "</b>", Label.CONTENT_XHTML);
                Button deleteUploadedFileButton = new Button("Delete");
                deleteUploadedFileButton.setStyleName(BaseTheme.BUTTON_LINK);
                addedBinaryFileLayout.addComponent(uploadedFileNameLabel);
                addedBinaryFileLayout.addComponent(deleteUploadedFileButton);
                addedBinaryFileLayout.setComponentAlignment(uploadedFileNameLabel, Alignment.MIDDLE_LEFT);
                addedBinaryFileLayout.setComponentAlignment(deleteUploadedFileButton, Alignment.MIDDLE_RIGHT);
                deleteUploadedFileButton.addListener(new Button.ClickListener() {

                    public void buttonClick(ClickEvent event) {
                        Component parentLayout = event.getComponent().getParent();
                        parentFileIndacateLayout.removeComponent(parentLayout);
                        muitlPropertyValueList.remove(uploadedFileName);                        
                        new File(tempFileDir + uploadedFileName).delete();
                        boolean validateResult = newBinaryPropertyForm.isValid();
                        String errorMessage = "";
                        if (validateResult) {
                            if (muitlPropertyValueList.size() == 0) {
                                errorMessage = "Please upload at least one binary file.";
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
                    if (muitlPropertyValueList.size() == 0) {
                        errorMessage = "Please upload at least one binary file.";
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

        upload.addListener(new Upload.FinishedListener() {

            public void uploadFinished(FinishedEvent event) {
                state.setValue(" <b style='color:#ce0000;'> Idle </b>");
                fileNameLabel.setVisible(false);
                fileName.setVisible(false);
                uploadingStatueLabel.setVisible(false);
                pi.setVisible(false);
                textualProgress.setVisible(false);
                cancelProcessing.setVisible(false);
            }
        });

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
}
