/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.contentManagement;

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
import com.viewfunction.vfbpm.adminCenter.util.RuntimeEnvironmentUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author wangychu
 */
public class BinaryContentFileProgressMonitoringUploader extends VerticalLayout implements Receiver {   
	private static final long serialVersionUID = -7316309727614973123L;
	
	private Label state = new Label(" <b style='color:#ce0000;'> Idle </b>", Label.CONTENT_XHTML);
    private Label fileName = new Label("", Label.CONTENT_XHTML);
    private Label textualProgress = new Label("", Label.CONTENT_XHTML);
    private Label fileNameLabel = new Label("File name : ");
    private Label uploadingStatueLabel = new Label("Uploading progress ");
    private ProgressIndicator pi = new ProgressIndicator();
    private Upload upload;
    private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
    private HorizontalLayout stateStatueContainerLayout = new HorizontalLayout();
    private VerticalLayout statusContainerLayout = new VerticalLayout();
    private HorizontalLayout controlButtonContainerLayout = new HorizontalLayout();

    public BinaryContentFileProgressMonitoringUploader(final List<File> fileList) {
        upload = new Upload(null, this);
        // make analyzing start immediatedly when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption("Select file to upload");
        final Button cancelProcessing = new Button("Cancel current upload");
        cancelProcessing.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -2891899843990153313L;

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
			private static final long serialVersionUID = 1600074189311237460L;

			public void uploadStarted(StartedEvent event) {
                // this method gets called immediatedly after upload is started
                stateStatueContainerLayout.setVisible(true);
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
			private static final long serialVersionUID = 2705074854725187699L;

			public void updateProgress(long readBytes, long contentLength) {
                // this method gets called several times during the update
                pi.setValue(new Float(readBytes / (float) contentLength));
                textualProgress.setValue("Processed <b style='color:#ce0000;'>" + readBytes + "</b> bytes of <b style='color:#333333;'>" + contentLength + "</b>");
            }
        });

        upload.addListener(new Upload.SucceededListener() {
			private static final long serialVersionUID = 7351799177456403728L;

			public void uploadSucceeded(SucceededEvent event) {
            }
        });

        upload.addListener(new Upload.FailedListener() {
			private static final long serialVersionUID = 6343250891564969765L;

			public void uploadFailed(FailedEvent event) {
                System.out.println(event.getFilename() + "fail");
            }
        });

        upload.addListener(new Upload.FinishedListener() {
			private static final long serialVersionUID = 1941351986811969858L;

			public void uploadFinished(FinishedEvent event) {
                state.setValue(" <b style='color:#ce0000;'> Idle </b>");
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
                Label confirmLabel = new Label("Confirm to add binary file <b style='color:#ce0000;'>" + event.getFilename() + "</b>.", Label.CONTENT_XHTML);
                controlButtonContainerLayout.addComponent(confirmLabel);
                controlButtonContainerLayout.setComponentAlignment(confirmLabel, Alignment.MIDDLE_RIGHT);
                fileList.add(new File(tempFileDir + event.getFilename()));
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
