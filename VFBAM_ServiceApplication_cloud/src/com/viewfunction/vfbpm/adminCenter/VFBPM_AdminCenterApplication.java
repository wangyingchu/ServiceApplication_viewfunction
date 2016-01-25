package com.viewfunction.vfbpm.adminCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;

import com.viewfunction.vfbpm.adminCenter.UI.ConsoleBanner;
import com.viewfunction.vfbpm.adminCenter.UI.ConsoleContent;
import com.viewfunction.vfbpm.adminCenter.UI.ConsoleTitle;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.ActivityDefinitionsChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.ActivityDefinitionsChangeEvent.ActivityDefinitionsChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsChangeEvent.ParticipantsChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesChangeEvent.RoleQueuesChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesChangeEvent.RolesChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster.RostersChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster.RostersChangeEvent.RostersChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.processManagement.ProcessObjectQueryEvent;
import com.viewfunction.vfbpm.adminCenter.UI.processManagement.ProcessObjectQueryEvent.ProcessObjectQueryListener;
import com.viewfunction.vfbpm.adminCenter.util.ApplicationI18nPerportyHandler;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VFBPM_AdminCenterApplication extends Application {	 
	
	private static final long serialVersionUID = -3199344730592411568L;	

	@Override
	public void init() {		
		WebApplicationContext context =(WebApplicationContext) this.getContext();			
        WebBrowser webBrowser = context.getBrowser();         
        Locale userBrowserlocale=webBrowser.getLocale();	        
        Properties i18NProperties=ApplicationI18nPerportyHandler.loadI18nProperties(userBrowserlocale); 
		setTheme(UICommonElementDefination.APPLICATION_GLOBAL_STYLE);
		final UserClientInfo userClientInfo=new UserClientInfo(i18NProperties,userBrowserlocale);
		userClientInfo.setWebBrowser(webBrowser);
		
		Blackboard BLACKBOARD = new Blackboard();
		BLACKBOARD.enableLogging();
		BLACKBOARD.register(ParticipantsChangeListener.class, ParticipantsChangeEvent.class);
		BLACKBOARD.register(RolesChangeListener.class, RolesChangeEvent.class);
		BLACKBOARD.register(RoleQueuesChangeListener.class, RoleQueuesChangeEvent.class);
		BLACKBOARD.register(RostersChangeListener.class, RostersChangeEvent.class);
		BLACKBOARD.register(ActivityDefinitionsChangeListener.class, ActivityDefinitionsChangeEvent.class);
		BLACKBOARD.register(ProcessObjectQueryListener.class, ProcessObjectQueryEvent.class);
		userClientInfo.setEventBlackboard(BLACKBOARD);		
		
        final Window root = new Window(i18NProperties.getProperty("applicationWindowTitle"));          
        root.setImmediate(true);
        setMainWindow(root);         
        root.addStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_UserLogin);       
        root.addComponent(createUserLoginForm(userClientInfo,userBrowserlocale));        
	}	
	
	private void loadBusinessLogicUI(UserClientInfo userClientInfo){
		Window mainWindow=this.getMainWindow();		
		mainWindow.removeStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_UserLogin);
		VerticalLayout rootLayout = new VerticalLayout();		
		// sure it's 100% sized, and remove unwanted margins        
        rootLayout.setSizeFull();
        rootLayout.setMargin(false);
        ConsoleBanner consoleBanner = new ConsoleBanner(userClientInfo);
        rootLayout.addComponent(consoleBanner);
        ConsoleTitle consoleTitle=new ConsoleTitle(userClientInfo);
        rootLayout.addComponent(consoleTitle);           
        ConsoleContent consoleContent=new ConsoleContent(userClientInfo);
        rootLayout.addComponent(consoleContent);        
        rootLayout.setExpandRatio(consoleContent, 1.0F); 
		mainWindow.setContent(rootLayout);
	}
	
	private VerticalLayout createUserLoginForm(final UserClientInfo userClientInfo,Locale userBrowserlocale){
		VerticalLayout loginFormLayout = new VerticalLayout();	
		VerticalLayout heightSpaceDivLayout_0 = new VerticalLayout();	
		heightSpaceDivLayout_0.setHeight("100px");
		loginFormLayout.addComponent(heightSpaceDivLayout_0);		
		HorizontalLayout formContainerLayout=new HorizontalLayout();		
		Panel loginPanel=new Panel();		
		HorizontalLayout formLayout=new HorizontalLayout();		
		loginPanel.addComponent(formLayout);		
		
		if(userBrowserlocale.toString().endsWith("zh_CN")){
			Embedded loginPicEmbedded=new Embedded(null, UICommonElementDefination.Login_picture_cn);
			formLayout.addComponent(loginPicEmbedded);			
		}else{
			Embedded loginPicEmbedded=new Embedded(null, UICommonElementDefination.Login_picture);
			formLayout.addComponent(loginPicEmbedded);
		}		
		HorizontalLayout spaceDivLayout=new HorizontalLayout();
		spaceDivLayout.setWidth("20px");
		formLayout.addComponent(spaceDivLayout);		
		VerticalLayout loginFormFieldLayout = new VerticalLayout();		
		formLayout.addComponent(loginFormFieldLayout);		
		if(userBrowserlocale.toString().endsWith("zh_CN")){
			Embedded loginTxtEmbedded=new Embedded(null, UICommonElementDefination.Login_Text_cn);		
			loginFormFieldLayout.addComponent(loginTxtEmbedded);		
			Embedded userloginTxtEmbedded=new Embedded(null, UICommonElementDefination.AdminLogin_Text_cn);
			loginFormFieldLayout.addComponent(userloginTxtEmbedded);			
		}else{
			Embedded loginTxtEmbedded=new Embedded(null, UICommonElementDefination.Login_Text);		
			loginFormFieldLayout.addComponent(loginTxtEmbedded);		
			Embedded userloginTxtEmbedded=new Embedded(null, UICommonElementDefination.AdminLogin_Text);
			loginFormFieldLayout.addComponent(userloginTxtEmbedded);
		}		
		
		BaseForm userLoginForm=new BaseForm();				
		
		final TextField adminUserNameField = new TextField(userClientInfo.getI18NProperties().getProperty("adminClientLogin_adminNameField"));
		adminUserNameField.setWidth("250px");
		adminUserNameField.setEnabled(false);		
		adminUserNameField.setValue(userClientInfo.getI18NProperties().getProperty("adminClientLogin_adminNameValue"));
		userLoginForm.addField("administrator", adminUserNameField);
	
		final PasswordField passwordField = new PasswordField(userClientInfo.getI18NProperties().getProperty("adminClientLogin_adminPWDField"));		
		passwordField.setWidth("250px");
		passwordField.setEnabled(true);
		passwordField.setDescription(userClientInfo.getI18NProperties().getProperty("adminClientLogin_adminPWDFieldDesc"));
		userLoginForm.addField("password", passwordField);			
		loginFormFieldLayout.addComponent(userLoginForm);		
		
		Button loginButton=new Button(userClientInfo.getI18NProperties().getProperty("adminClientLogin_adminLoginButton"));
		loginButton.addListener(new ClickListener(){ 		
			private static final long serialVersionUID = -7462367839988998032L;

			public void buttonClick(ClickEvent event) {					
				loadBusinessLogicUI(userClientInfo);
			}
		}); 
		
		List<Button> buttonList = new ArrayList<Button>();		   
	    buttonList.add(loginButton);	   
	    BaseButtonBar loginActionButtonBar = new BaseButtonBar(190, 45, Alignment.MIDDLE_CENTER, buttonList);
	    loginFormFieldLayout.addComponent(loginActionButtonBar);
	    loginFormFieldLayout.setComponentAlignment(loginActionButtonBar, Alignment.MIDDLE_RIGHT);		
		formContainerLayout.addComponent(loginPanel);		
		loginFormLayout.addComponent(formContainerLayout);		
		loginFormLayout.setComponentAlignment(formContainerLayout, Alignment.MIDDLE_CENTER);
		return loginFormLayout;
	}	
}