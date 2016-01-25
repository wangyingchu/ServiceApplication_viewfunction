package com.viewfunction.vfbpm.adminCenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.ConsoleBanner;
import com.viewfunction.vfbpm.adminCenter.UI.ConsoleTitle;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.UserClientConsoleContent;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration.StepEditorRecode;
import com.viewfunction.vfbpm.adminCenter.util.ApplicationI18nPerportyHandler;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;

public class VFBPM_UserClientApplication extends Application implements HttpServletRequestListener{
	private static final long serialVersionUID = -2820272361162456023L;
	
	private HttpServletResponse response;
	private String COOKIE_InitActivitySpaceName="ACTIVITYSPACENAME";
	private String COOKIE_InitParticipantName="PARTICIPANTNAME";
	private String COOKIE_InitRememberLogin="REMEMBERLOGIN";	
	private String activitySpaceNameInCookie;
	private String participantNameInCookie;
	private String rememberLoginSwitchInCookie;
	
	private CheckBox rememberChoose;	
	@Override
	public void init() {		
		WebApplicationContext context =(WebApplicationContext) this.getContext();			
        WebBrowser webBrowser = context.getBrowser();         
        Locale userBrowserlocale=webBrowser.getLocale();	        
        Properties i18NProperties=ApplicationI18nPerportyHandler.loadI18nProperties(userBrowserlocale); 
		setTheme(UICommonElementDefination.APPLICATION_CLIENT_GLOBAL_STYLE);
		final UserClientInfo userClientInfo=new UserClientInfo(i18NProperties,userBrowserlocale);
		userClientInfo.setWebBrowser(webBrowser);		
		StepEditorRecode stepEditorRecode=new StepEditorRecode();	
		userClientInfo.setStepEditorRecode(stepEditorRecode);		
        final Window root = new Window(i18NProperties.getProperty("userApplicationWindowTitle"));          
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
        ConsoleTitle consoleTitle=new ConsoleTitle(userClientInfo,userClientInfo.getI18NProperties().getProperty("userApplicationTitle"));
        rootLayout.addComponent(consoleTitle);           
        UserClientConsoleContent consoleContent=new UserClientConsoleContent(userClientInfo);
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
			Embedded userloginTxtEmbedded=new Embedded(null, UICommonElementDefination.UserLogin_Text_cn);
			loginFormFieldLayout.addComponent(userloginTxtEmbedded);
		}else{
			Embedded loginTxtEmbedded=new Embedded(null, UICommonElementDefination.Login_Text);			
			loginFormFieldLayout.addComponent(loginTxtEmbedded);
			Embedded userloginTxtEmbedded=new Embedded(null, UICommonElementDefination.UserLogin_Text);
			loginFormFieldLayout.addComponent(userloginTxtEmbedded);
		}
		
		BaseForm userLoginForm=new BaseForm();		
		final ComboBox participantChoise = new ComboBox(userClientInfo.getI18NProperties().getProperty("userClientLogin_userNameField"));
		participantChoise.setNullSelectionAllowed(false);
		final PasswordField passwordField = new PasswordField(userClientInfo.getI18NProperties().getProperty("userClientLogin_passwordField"));
		
		final ComboBox activitySpaceChoise = new ComboBox(userClientInfo.getI18NProperties().getProperty("userClientLogin_activitySpaceField"));	
		activitySpaceChoise.setNullSelectionAllowed(false);
		activitySpaceChoise.setWidth("250px");
		activitySpaceChoise.setInputPrompt(userClientInfo.getI18NProperties().getProperty("userClientLogin_activitySpaceFieldPrompt"));		
		userLoginForm.addField("activitySpaceChoise", activitySpaceChoise);		
		try {
			ActivitySpace[] activitySpaceArray=ActivityComponentFactory.getActivitySpaces();
			if(activitySpaceArray!=null){
				for(int i=0;i<activitySpaceArray.length;i++){
					ActivitySpace currentActivitySpace=activitySpaceArray[i];				
					activitySpaceChoise.addItem(currentActivitySpace.getActivitySpaceName());
				}	
			}					
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
		}
		activitySpaceChoise.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		activitySpaceChoise.setImmediate(true);	
		
		activitySpaceChoise.addListener(new ValueChangeListener(){
			private static final long serialVersionUID = 3272769860873463804L;

			public void valueChange(ValueChangeEvent event) {				
				String activitySpaceName=event.getProperty().toString();
				ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);				
				try {
					Participant[] participantArray=activitySpace.getParticipants();
					participantChoise.removeAllItems();					
					passwordField.setEnabled(false);					
					IndexedContainer participantContainer = new IndexedContainer();
					participantContainer.addContainerProperty("PARTICIPANT_DISPLAYNAME", String.class, null);
					participantContainer.addContainerProperty("PARTICIPANT", Participant.class, null);
					if(participantArray!=null){
						for(int i=0;i<participantArray.length;i++){
							Item item = participantContainer.addItem(""+i);						
					        item.getItemProperty("PARTICIPANT_DISPLAYNAME").setValue(participantArray[i].getDisplayName());
					        item.getItemProperty("PARTICIPANT").setValue(participantArray[i]);				       						
						}
					}					
					participantChoise.setContainerDataSource(participantContainer);
				    participantChoise.setItemCaptionPropertyId("PARTICIPANT_DISPLAYNAME");
					participantChoise.setEnabled(true);
				} catch (ActivityEngineRuntimeException e) {					
					e.printStackTrace();
				}
				
			}});		
		
		participantChoise.setWidth("250px");
		participantChoise.setInputPrompt(userClientInfo.getI18NProperties().getProperty("userClientLogin_userNameFieldPrompt"));		
		userLoginForm.addField("participantChoise", participantChoise);
		participantChoise.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		participantChoise.setImmediate(true);	
		participantChoise.setEnabled(false);
		
		participantChoise.addListener(new ValueChangeListener(){		
			private static final long serialVersionUID = 6521759424516618824L;

			public void valueChange(ValueChangeEvent event) {
				passwordField.setValue("");
				passwordField.setEnabled(true);				
			}});		
		
		passwordField.setWidth("250px");
		passwordField.setEnabled(false);
		passwordField.setDescription(userClientInfo.getI18NProperties().getProperty("userClientLogin_userPWDFieldPrompt"));
		userLoginForm.addField("password", passwordField);		
		
		rememberChoose=new CheckBox(userClientInfo.getI18NProperties().getProperty("userClientLogin_userLoginRememberCheckbox"));
		userLoginForm.addField("rememberChoise", rememberChoose);
		
		loginFormFieldLayout.addComponent(userLoginForm);		
		Button loginButton=new Button(userClientInfo.getI18NProperties().getProperty("userClientLogin_userLoginButton"));
		loginButton.addListener(new ClickListener(){ 
			private static final long serialVersionUID = 1631179748086849831L;

			public void buttonClick(ClickEvent event) {	
				if(participantChoise.getValue()==null){
					return;
				}				
				String activitySpaceName=activitySpaceChoise.getValue().toString();				
				IndexedContainer participantContainer =(IndexedContainer) participantChoise.getContainerDataSource();
				Item selectedItem=participantContainer.getItem(participantChoise.getValue().toString());				
				Participant selectedParticipant=(Participant)selectedItem.getItemProperty("PARTICIPANT").getValue();
				String passwordValue=passwordField.getValue().toString();				
				if(selectedParticipant!=null&&!selectedParticipant.equals("")){
					setLoginCookieInfo(activitySpaceName,selectedParticipant.getParticipantName());
					userClientInfo.setUserActivitySpace(activitySpaceName);
					userClientInfo.setUserParticipant(selectedParticipant);				
					loadBusinessLogicUI(userClientInfo);	
				}				
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
		
		if(rememberLoginSwitchInCookie!=null){
			if(rememberLoginSwitchInCookie.equals("true")){					
				if(activitySpaceNameInCookie!=null){					
					activitySpaceChoise.setValue(activitySpaceNameInCookie);
				}
				if(participantNameInCookie!=null){
					IndexedContainer participantContainer =(IndexedContainer) participantChoise.getContainerDataSource();					
					Collection participantIdCollection=participantContainer.getItemIds();					
					Iterator idIterator=participantIdCollection.iterator();					
					while(idIterator.hasNext()){
						Object currentId=idIterator.next();
						Item currentItem=participantContainer.getItem(currentId);
						Participant currentParticipant=(Participant)currentItem.getItemProperty("PARTICIPANT").getValue();
						if(currentParticipant.getParticipantName().equals(participantNameInCookie)){
							participantChoise.select(currentId);
							break;
						}						
					}
				}			
				rememberChoose.setValue(new Boolean(true));
			}			
		}			
		return loginFormLayout;
	}

	public void onRequestStart(HttpServletRequest request,HttpServletResponse response) {
		this.response = response;		
		Cookie[] cookies = request.getCookies();	
		if(cookies!=null){
			for (int i=0; i<cookies.length; i++) {	        	
	        	if(cookies[i].getName().equals(COOKIE_InitActivitySpaceName)){
	        		activitySpaceNameInCookie=cookies[i].getValue();
	        	}
	        	if(cookies[i].getName().equals(COOKIE_InitParticipantName)){
	        		participantNameInCookie=cookies[i].getValue();
	        	}
	        	if(cookies[i].getName().equals(COOKIE_InitRememberLogin)){
	        		rememberLoginSwitchInCookie=cookies[i].getValue();
	        	}
	        }			
		}
	}

	public void onRequestEnd(HttpServletRequest request,HttpServletResponse response) {
	}
	
	private void setLoginCookieInfo(String activitySpaceName,String participantName){
		boolean setCookieFlag=((Boolean)this.rememberChoose.getValue()).booleanValue();			
		if(setCookieFlag){					
			Cookie activitySpaceNamCookie = new Cookie(COOKIE_InitActivitySpaceName, activitySpaceName);
			activitySpaceNamCookie.setPath("/VFBAM/userClient/");
			activitySpaceNamCookie.setMaxAge(3600*24*30); // One month
			response.addCookie(activitySpaceNamCookie);
			
			Cookie participantNameCookie = new Cookie(COOKIE_InitParticipantName, participantName);
			participantNameCookie.setPath("/VFBAM/userClient/");
			participantNameCookie.setMaxAge(3600*24*30); // One month
			response.addCookie(participantNameCookie);
			
			Cookie rememberLoginCookie = new Cookie(COOKIE_InitRememberLogin, "true");
			rememberLoginCookie.setPath("/VFBAM/userClient/");
			rememberLoginCookie.setMaxAge(3600*24*30); // One month
			response.addCookie(rememberLoginCookie);			
		}else{			
			Cookie activitySpaceNamCookie = new Cookie(COOKIE_InitActivitySpaceName, activitySpaceName);
			activitySpaceNamCookie.setPath("/VFBAM/userClient/");
			activitySpaceNamCookie.setMaxAge(0); // One month
			response.addCookie(activitySpaceNamCookie);
			
			Cookie participantNameCookie = new Cookie(COOKIE_InitParticipantName, participantName);
			participantNameCookie.setPath("/VFBAM/userClient/");
			participantNameCookie.setMaxAge(0); // One month
			response.addCookie(participantNameCookie);
			
			Cookie rememberLoginCookie = new Cookie(COOKIE_InitRememberLogin, "false");
			rememberLoginCookie.setPath("/VFBAM/userClient/");
			rememberLoginCookie.setMaxAge(0); // One month
			response.addCookie(rememberLoginCookie);			
		}		
	}
}