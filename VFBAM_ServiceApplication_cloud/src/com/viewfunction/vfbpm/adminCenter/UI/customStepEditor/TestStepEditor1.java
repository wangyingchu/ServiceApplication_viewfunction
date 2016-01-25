package com.viewfunction.vfbpm.adminCenter.UI.customStepEditor;

import com.vaadin.ui.Label;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseActivityStepEditor;
import com.viewfunction.vfbpm.adminCenter.UI.element.ReloadableUIElement;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class TestStepEditor1 extends BaseActivityStepEditor{
	private static final long serialVersionUID = -6567377862273250354L;

	public TestStepEditor1(){
		
		Label lb=new Label("testStepEditor");
		this.addComponent(lb);
	}

	@Override
	public void setActivityStep(ActivityStep activityStep) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserClientInfo(UserClientInfo userClientInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReloadableUIElements(
			ReloadableUIElement[] reloadableUIElements) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void assembleStepEditor() {
		// TODO Auto-generated method stub
		
	}

}
