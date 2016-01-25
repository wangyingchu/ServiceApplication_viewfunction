package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.ui.VerticalLayout;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public abstract class BaseActivityStepEditor extends VerticalLayout{
	private static final long serialVersionUID = -184201252232492436L;	

	public abstract void setActivityStep(ActivityStep activityStep);
	public abstract void setUserClientInfo(UserClientInfo userClientInfo);
	public abstract void setReloadableUIElements(ReloadableUIElement[] reloadableUIElements);	
	public abstract void assembleStepEditor();
}
