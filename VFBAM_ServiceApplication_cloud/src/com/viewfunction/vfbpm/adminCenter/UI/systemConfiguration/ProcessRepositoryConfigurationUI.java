package com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import com.viewfunction.processRepository.exception.ProcessRepositoryRuntimeException;
import com.viewfunction.processRepository.util.PerportyHandler;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ProcessRepositoryConfigurationUI extends VerticalLayout{
	private static final long serialVersionUID = 1042929640111957279L;
	
	private UserClientInfo userClientInfo;
	
	public ProcessRepositoryConfigurationUI(UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);
	
		Embedded processRepositoryIconEmbedded=new Embedded(null, UICommonElementDefination.ICON_systemConfig_processRepository);
		SectionTitleBar processRepositoryConfigSectionTitleBar=new SectionTitleBar(processRepositoryIconEmbedded,
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_processRepositoryConfig"),SectionTitleBar.MIDDLEFONT,null);
		containerPanel.addComponent(processRepositoryConfigSectionTitleBar);	
		
		try {
			PropertyItem DATA_PERSISTENCE_TYPE_PropertyItem = new PropertyItem(PropertyItem.POSTION_EVEN,null,"DATA_PERSISTENCE_TYPE",PerportyHandler.getPerportyValue(PerportyHandler.DATA_PERSISTENCE_TYPE),null);
			containerPanel.addComponent(DATA_PERSISTENCE_TYPE_PropertyItem);		
			PropertyItem ACTIVITI_databaseSchemaUpdate_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"ACTIVITI_databaseSchemaUpdate",PerportyHandler.getPerportyValue(PerportyHandler.ACTIVITI_databaseSchemaUpdate),null);		
			containerPanel.addComponent(ACTIVITI_databaseSchemaUpdate_PropertyItem);		
			PropertyItem ACTIVITI_jdbcUrl_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"ACTIVITI_jdbcUrl",PerportyHandler.getPerportyValue(PerportyHandler.ACTIVITI_jdbcUrl),null);		
			containerPanel.addComponent(ACTIVITI_jdbcUrl_PropertyItem);		
			PropertyItem ACTIVITI_jdbcDriver_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"ACTIVITI_jdbcDriver",PerportyHandler.getPerportyValue(PerportyHandler.ACTIVITI_jdbcDriver),null);		
			containerPanel.addComponent(ACTIVITI_jdbcDriver_PropertyItem);		
			PropertyItem ACTIVITI_jdbcUsername_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"ACTIVITI_jdbcUsername",PerportyHandler.getPerportyValue(PerportyHandler.ACTIVITI_jdbcUsername),null);		
			containerPanel.addComponent(ACTIVITI_jdbcUsername_PropertyItem);
			PropertyItem ACTIVITI_jdbcPassword_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"ACTIVITI_jdbcPassword",PerportyHandler.getPerportyValue(PerportyHandler.ACTIVITI_jdbcPassword),null);		
			containerPanel.addComponent(ACTIVITI_jdbcPassword_PropertyItem);
			PropertyItem ACTIVITI_jobExecutorActivate_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"ACTIVITI_jobExecutorActivate",PerportyHandler.getPerportyValue(PerportyHandler.ACTIVITI_jobExecutorActivate),null);		
			containerPanel.addComponent(ACTIVITI_jobExecutorActivate_PropertyItem);
		} catch (ProcessRepositoryRuntimeException e) {			
			e.printStackTrace();
		}		
	}
}