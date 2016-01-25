package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
public class RoleQueueTreeOnClickListener implements Property.ValueChangeListener{
	private static final long serialVersionUID = -3425162780369924830L;

	private ActivityRoleQueueTree activityRoleQueueTree;
	
	public RoleQueueTreeOnClickListener(ActivityRoleQueueTree activityRoleQueueTree){
		this.activityRoleQueueTree=activityRoleQueueTree;
	}
	
	public void valueChange(ValueChangeEvent event) {
		//the second time click on the same tree node will get a null event.getProperty().getValue()
        //this means unselect current tree node		
        if (event.getProperty().getValue() != null) {     
        	activityRoleQueueTree.handleTreeItemClick(event.getProperty().getValue());
        }		
	}

}
