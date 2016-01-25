package com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class StepEditorConfigurationUI extends VerticalLayout{
	private static final long serialVersionUID = 9202442788738373384L;

	private TabSheet activitySpaceTabsheet;	
	private StepEditorRecode stepEditorRecode;
	private DialogWindow setpropertyWindow;
	private UserClientInfo userClientInfo;
	
	public StepEditorConfigurationUI(UserClientInfo userClientInfo){	
		this.userClientInfo=userClientInfo;
		activitySpaceTabsheet=new TabSheet();	     
	    this.addComponent(activitySpaceTabsheet);	     
	    activitySpaceTabsheet.setSizeFull();	
	    activitySpaceTabsheet.setStyleName(Reindeer.TABSHEET_SMALL);
	    stepEditorRecode=new StepEditorRecode();
	    loadStepEditorData();
	}
	
	private void loadStepEditorData(){			
		try {							
			ActivitySpace[] activitySpaceArray=ActivityComponentFactory.getActivitySpaces();			
			for(ActivitySpace activitySpace:activitySpaceArray){				
				TabSheet activityTypeTabsheet=new TabSheet();
				activityTypeTabsheet.setSizeFull();	
				activityTypeTabsheet.setStyleName(Reindeer.TABSHEET_SMALL);				
				String[] activityTypeArray=activitySpace.getBusinessActivityTypes();
				if(activityTypeArray!=null){
					for(String activityType:activityTypeArray){								
						Panel containerPanel=new Panel();
						containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
						containerPanel.setScrollable(true);
						containerPanel.setSizeFull();		
						VerticalLayout stepEditorConfigsLayout=new VerticalLayout();
						BusinessActivityDefinition bad=activitySpace.getBusinessActivityDefinition(activityType);
						String[] activityStepsArray=bad.getExposedSteps();						
						Embedded stepEditorIconEmbedded=new Embedded(null, UICommonElementDefination.ICON_systemConfig_stepEditor);
						SectionTitleBar stepEditorSectionTitleBar=new SectionTitleBar(stepEditorIconEmbedded,
								userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_stepEditorConfigTitle"),SectionTitleBar.MIDDLEFONT,null);
						stepEditorConfigsLayout.addComponent(stepEditorSectionTitleBar);						
						if(activityStepsArray!=null){
							for(String activityStep:activityStepsArray){								
								PropertyItem namePropertyItem=getStepEditorPropertyUI(activitySpace.getActivitySpaceName(),activityType,activityStep);	
								stepEditorConfigsLayout.addComponent(namePropertyItem);
							}							
						}										
						containerPanel.addComponent(stepEditorConfigsLayout);
						activityTypeTabsheet.addTab(containerPanel,activityType);
					}
				}				
				HorizontalLayout spaceDivHorizontalLayout=new HorizontalLayout();
				spaceDivHorizontalLayout.setWidth("100%");	
				spaceDivHorizontalLayout.setHeight("6px");		
				spaceDivHorizontalLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_ContentManagementNavTitleText);				
				VerticalLayout valueContainerLayout=new VerticalLayout();
				valueContainerLayout.addComponent(spaceDivHorizontalLayout);
				valueContainerLayout.addComponent(activityTypeTabsheet);				
				activitySpaceTabsheet.addTab(valueContainerLayout,activitySpace.getActivitySpaceName());
			}					
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
		}		
	}
	
	private PropertyItem getStepEditorPropertyUI(final String activitySpaceName,final String activityType,final String activityStep){
		HorizontalLayout stepActionContainer=new HorizontalLayout();		
		stepActionContainer.setWidth("30px");		
		Button editStepButton=new Button();		
		editStepButton.setCaption(null);
		editStepButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editStepButton.setStyleName(BaseTheme.BUTTON_LINK);		
		editStepButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_setStepEditor_button"));
		stepActionContainer.addComponent(editStepButton);		
		final Label propertyValueLabel=new Label("");
		String stepEditorName=this.stepEditorRecode.getStepEditorName(activitySpaceName,activityType,activityStep);
		if(stepEditorName!=null){
			propertyValueLabel.setValue(stepEditorName);			
		}	
		editStepButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -8427725705885098982L;

			public void buttonClick(ClickEvent event) {				
				updateStepEditorValue(activitySpaceName,activityType,activityStep,propertyValueLabel);	
			}});
		PropertyItem resultPropertyItem= new PropertyItem(PropertyItem.POSTION_ODD,null,activityStep,propertyValueLabel,stepActionContainer);		 
		return resultPropertyItem;			
	}
	
	private void updateStepEditorValue(String activitySpaceName,String activityType,String activityStep,Label eitorDisplayLabel){
		renderSetStepEditorUI(activitySpaceName,activityType,activityStep,eitorDisplayLabel);
	}
	
	private void removeSetPropertyWindow(){
		this.getApplication().getMainWindow().removeWindow(setpropertyWindow);
		setpropertyWindow=null;
	}
	
	private void renderSetStepEditorUI(final String activitySpaceName,final String activityType,final String activityStep,final Label eitorDisplayLabel){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_setStepEditorWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_setStepEditorWindowDesc")+" "+	
        " <span style='color:#ce0000;font-weight:bold;'>"+activityStep+"</span>";
        VerticalLayout setStatusLayout = new VerticalLayout();        
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);		
		final TextField stepEditorField=new TextField(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_stepEditorField"));	
		stepEditorField.setWidth("300px");
		stepEditorField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_stepEditorPrompt"));	
		inputFieldListLayout.addComponent(stepEditorField);		
		String currentEditorValue=this.stepEditorRecode.getStepEditorName(activitySpaceName,activityType,activityStep);
		if(currentEditorValue!=null){
			stepEditorField.setValue(currentEditorValue);
		}			
		HorizontalLayout spaceDiv_1=new HorizontalLayout();
	    spaceDiv_1.setHeight("10px");
	    setStatusLayout.addComponent(spaceDiv_1);
		setStatusLayout.addComponent(inputFieldListLayout);			
		HorizontalLayout spaceDiv_2=new HorizontalLayout();
	    spaceDiv_2.setHeight("10px");
	    setStatusLayout.addComponent(spaceDiv_2);	   	
		
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_setStepEditor_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -3841825888535545606L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if(stepEditorField.getValue()==null||stepEditorField.getValue().toString().equals("")){
					doDeleteStepEditor(activitySpaceName,activityType,activityStep,eitorDisplayLabel);
				}else{
					String stepEditor=stepEditorField.getValue().toString();					
					doSetStepEditor(activitySpaceName,activityType,activityStep,stepEditor,eitorDisplayLabel);
				}				
			}			
	     }); 		    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_setStepEditor_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -2907997109110215791L;

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
	
	private void doDeleteStepEditor(final String activitySpaceName,final String activityType,final String activityStep,final Label eitorDisplayLabel){		
		ContentSpace systemConfigSpace=null;				
		try {
			systemConfigSpace=ContentComponentFactory.connectContentSpace(SystemConfigurationConstant.SYSTEMCONFIG_CONTENTSPACE);
			RootContentObject stepEditorConfigRoot=systemConfigSpace.getRootContentObject(SystemConfigurationConstant.SYSTEMCONFIG_ActivityMetaDataConfig);				
			if(stepEditorConfigRoot!=null){
				BaseContentObject activitySpaceObj=stepEditorConfigRoot.getSubContentObject(activitySpaceName);
				if(activitySpaceObj!=null){
					BaseContentObject activityTypeObj=activitySpaceObj.getSubContentObject(activityType);
					if(activityTypeObj!=null){
						BaseContentObject activityStepObj=activityTypeObj.getSubContentObject(activityStep);
						if(activityStepObj!=null){
							ContentObjectProperty stepEditorProperty=activityStepObj.getProperty(SystemConfigurationConstant.SYSTEMCONFIG_StepEditorName);
							if(stepEditorProperty!=null){
								activityStepObj.removeProperty(SystemConfigurationConstant.SYSTEMCONFIG_StepEditorName, false);								
							}							
						}
					}						
				}					
			}				
		} catch (ContentReposityException e) {
				e.printStackTrace();
		}finally{
			if(systemConfigSpace!=null){
				systemConfigSpace.closeContentSpace();
			}
		}
		this.stepEditorRecode.removeActivityStepData(activitySpaceName, activityType,activityStep);
		eitorDisplayLabel.setValue("");	
		removeSetPropertyWindow();
		String resultMessage=this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_setStepEditor_removeSuccessMsg");
		getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);		
	}
	
	private void doSetStepEditor(final String activitySpaceName,final String activityType,final String activityStep,final String stepEditor,final Label eitorDisplayLabel){
		ContentSpace systemConfigSpace=null;
		try {
			systemConfigSpace=ContentComponentFactory.connectContentSpace(SystemConfigurationConstant.SYSTEMCONFIG_CONTENTSPACE);
			RootContentObject stepEditorConfigRoot=systemConfigSpace.getRootContentObject(SystemConfigurationConstant.SYSTEMCONFIG_ActivityMetaDataConfig);				
			if(stepEditorConfigRoot==null){
				RootContentObject stepConfigRootObj=ContentComponentFactory.createRootContentObject(SystemConfigurationConstant.SYSTEMCONFIG_ActivityMetaDataConfig);			
				stepEditorConfigRoot=systemConfigSpace.addRootContentObject(stepConfigRootObj);
			}
			BaseContentObject activitySpaceObj=stepEditorConfigRoot.getSubContentObject(activitySpaceName);
			if(activitySpaceObj==null){
				activitySpaceObj=stepEditorConfigRoot.addSubContentObject(activitySpaceName,null,false);
			}
			BaseContentObject activityTypeObj=activitySpaceObj.getSubContentObject(activityType);
			if(activityTypeObj==null){
				activityTypeObj=activitySpaceObj.addSubContentObject(activityType,null,false);
			}
			BaseContentObject activityStepObj=activityTypeObj.getSubContentObject(activityStep);
			if(activityStepObj==null){
				activityStepObj=activityTypeObj.addSubContentObject(activityStep,null,false);
			}			
			ContentObjectProperty stepEditorProperty=activityStepObj.getProperty(SystemConfigurationConstant.SYSTEMCONFIG_StepEditorName);
			if(stepEditorProperty!=null){				
				stepEditorProperty.setPropertyValue(stepEditor);	
				activityStepObj.updateProperty(stepEditorProperty,false);
			}else{
				activityStepObj.addProperty(SystemConfigurationConstant.SYSTEMCONFIG_StepEditorName, stepEditor, false);
			}			
		} catch (ContentReposityException e) {
				e.printStackTrace();
		}finally{
			if(systemConfigSpace!=null){
				systemConfigSpace.closeContentSpace();
			}
		}		
		this.stepEditorRecode.addActivityStepData(activitySpaceName, activityType,activityStep,stepEditor);		
		eitorDisplayLabel.setValue(stepEditor);	
		removeSetPropertyWindow();
		String resultMessage=this.userClientInfo.getI18NProperties().getProperty("SystemConfig_StepEditorConfigurationUI_setStepEditor_setSuccessMsg");
		getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);		
	}
}