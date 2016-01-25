package com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.contentRepository.util.PerportyHandler;
import com.viewfunction.contentRepository.util.exception.ContentReposityRuntimeException;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ContentRepositoryConfigurationUI extends VerticalLayout{
	private static final long serialVersionUID = 5989385325369472811L;
	
	private UserClientInfo userClientInfo;
	
	public ContentRepositoryConfigurationUI(UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);
	
		Embedded contentRepositoryIconEmbedded=new Embedded(null, UICommonElementDefination.ICON_systemConfig_contentRepository);
		SectionTitleBar contentRepositoryConfigSectionTitleBar=new SectionTitleBar(contentRepositoryIconEmbedded,
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_contentRepositoryConfig"),SectionTitleBar.MIDDLEFONT,null);
		containerPanel.addComponent(contentRepositoryConfigSectionTitleBar);	
		
		try {
			PropertyItem USER_AUTHENTICATION_METHOD_PropertyItem = new PropertyItem(PropertyItem.POSTION_EVEN,null,"USER_AUTHENTICATION_METHOD",PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_METHOD),null);
			containerPanel.addComponent(USER_AUTHENTICATION_METHOD_PropertyItem);		
			PropertyItem BUILDIN_ADMINISTRATOR_ACCOUNT_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"BUILDIN_ADMINISTRATOR_ACCOUNT",PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT),null);		
			containerPanel.addComponent(BUILDIN_ADMINISTRATOR_ACCOUNT_PropertyItem);		
			PropertyItem BUILDIN__ADMINISTRATOR_ACCOUNT_PWD_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"BUILDIN__ADMINISTRATOR_ACCOUNT_PWD",PerportyHandler.getPerportyValue(PerportyHandler.BUILDIN_ADMINISTRATOR_ACCOUNT_PWD),null);		
			containerPanel.addComponent(BUILDIN__ADMINISTRATOR_ACCOUNT_PWD_PropertyItem);		
			PropertyItem USER_AUTHENTICATION_LDAP_SERVER_ADDRESS_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"USER_AUTHENTICATION_LDAP_SERVER_ADDRESS",PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_LDAP_SERVER_ADDRESS),null);		
			containerPanel.addComponent(USER_AUTHENTICATION_LDAP_SERVER_ADDRESS_PropertyItem);		
			PropertyItem USER_AUTHENTICATION_LDAP_SERVICE_PORT_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"USER_AUTHENTICATION_LDAP_SERVICE_PORT",PerportyHandler.getPerportyValue(PerportyHandler.USER_AUTHENTICATION_LDAP_SERVICE_PORT),null);		
			containerPanel.addComponent(USER_AUTHENTICATION_LDAP_SERVICE_PORT_PropertyItem);
		//	PropertyItem DATA_PERSISTENCE_TYPE_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"DATA_PERSISTENCE_TYPE",PerportyHandler.getPerportyValue(PerportyHandler.DATA_PERSISTENCE_TYPE),null);		
		//	containerPanel.addComponent(DATA_PERSISTENCE_TYPE_PropertyItem);
		} catch (ContentReposityRuntimeException e) {			
			e.printStackTrace();
		}		
	}
}