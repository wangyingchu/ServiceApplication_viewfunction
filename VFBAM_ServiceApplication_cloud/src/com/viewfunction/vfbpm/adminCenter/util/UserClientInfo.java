package com.viewfunction.vfbpm.adminCenter.util;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.github.wolfie.blackboard.Blackboard;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.messageEngine.messageService.RealTimeNotificationReceiver;
import com.viewfunction.vfbpm.adminCenter.UI.element.ReloadableUIElement;
import com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration.StepEditorRecode;

public class UserClientInfo implements Serializable{

	private static final long serialVersionUID = 3930971222892866428L;
	
	private Properties i18NProperties;
	private Locale userBrowserlocale;	
	private Participant userParticipant;
	private String userActivitySpace;
	private WebBrowser webBrowser;
	private Blackboard eventBlackboard;
	private StepEditorRecode stepEditorRecode;
	private RealTimeNotificationReceiver notificationReceiver;
	private RealTimeNotificationReceiver personalMessageReceiver;
	private RealTimeNotificationReceiver publicMessageReceiver;
	private List<ReloadableUIElement> refreshUIElementsList;
	
	public UserClientInfo(Properties pro,Locale local){
		this.i18NProperties=pro;
		this.userBrowserlocale=local;
	}	
	
	public Properties getI18NProperties() {
		return i18NProperties;
	}
	
	void setI18NProperties(Properties i18NProperties) {
		this.i18NProperties = i18NProperties;
	}
	
	public Locale getUserBrowserlocale() {
		return userBrowserlocale;
	}
	
	void setUserBrowserlocale(Locale userBrowserlocale) {
		this.userBrowserlocale = userBrowserlocale;
	}

	public Participant getUserParticipant() {
		return userParticipant;
	}

	public void setUserParticipant(Participant userParticipant) {
		this.userParticipant = userParticipant;
	}

	public String getUserActivitySpace() {
		return userActivitySpace;
	}

	public void setUserActivitySpace(String userActivitySpace) {
		this.userActivitySpace = userActivitySpace;
	}

	public WebBrowser getWebBrowser() {
		return webBrowser;
	}

	public void setWebBrowser(WebBrowser webBrowser) {
		this.webBrowser = webBrowser;
	}

	public Blackboard getEventBlackboard() {
		return eventBlackboard;
	}

	public void setEventBlackboard(Blackboard eventBlackboard) {
		this.eventBlackboard = eventBlackboard;
	}

	public StepEditorRecode getStepEditorRecode() {
		return stepEditorRecode;
	}

	public void setStepEditorRecode(StepEditorRecode stepEditorRecode) {
		this.stepEditorRecode = stepEditorRecode;
	}

	public RealTimeNotificationReceiver getNotificationReceiver() {
		return notificationReceiver;
	}

	public void setNotificationReceiver(RealTimeNotificationReceiver notificationReceiver) {
		this.notificationReceiver = notificationReceiver;
	}

	public RealTimeNotificationReceiver getPersonalMessageReceiver() {
		return personalMessageReceiver;
	}

	public void setPersonalMessageReceiver(RealTimeNotificationReceiver personalMessageReceiver) {
		this.personalMessageReceiver = personalMessageReceiver;
	}

	public RealTimeNotificationReceiver getPublicMessageReceiver() {
		return publicMessageReceiver;
	}

	public void setPublicMessageReceiver(RealTimeNotificationReceiver publicMessageReceiver) {
		this.publicMessageReceiver = publicMessageReceiver;
	}

	public List<ReloadableUIElement> getRefreshUIElementsList() {
		return refreshUIElementsList;
	}

	public void setRefreshUIElementsList(List<ReloadableUIElement> refreshUIElementsList) {
		this.refreshUIElementsList = refreshUIElementsList;
	}
}