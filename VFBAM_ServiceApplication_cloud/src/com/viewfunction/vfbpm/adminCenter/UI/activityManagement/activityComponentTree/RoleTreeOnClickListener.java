package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
public class RoleTreeOnClickListener  implements Property.ValueChangeListener  {
	private static final long serialVersionUID = -4679807911958546730L;
	
	private ActivityRoleTree activityRoleTree;
	
	public RoleTreeOnClickListener(ActivityRoleTree activityRoleTree){
		this.activityRoleTree=activityRoleTree;		
	}	
	
	public void valueChange(ValueChangeEvent event) {
		//the second time click on the same tree node will get a null event.getProperty().getValue()
        //this means unselect current tree node		
        if (event.getProperty().getValue() != null) {     
        	activityRoleTree.handleTreeItemClick(event.getProperty().getValue());
        }		
	}
}