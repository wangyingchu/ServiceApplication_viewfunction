package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
public class RosterTreeOnClickListener implements Property.ValueChangeListener {
	private static final long serialVersionUID = 6652685222089015875L;

	private ActivityRosterTree activityRosterTree;
	
	public RosterTreeOnClickListener(ActivityRosterTree activityRosterTree){
		this.activityRosterTree=activityRosterTree;
	}
	
	public void valueChange(ValueChangeEvent event) {
		//the second time click on the same tree node will get a null event.getProperty().getValue()
        //this means unselect current tree node		
        if (event.getProperty().getValue() != null) {     
        	activityRosterTree.handleTreeItemClick(event.getProperty().getValue());
        }		
	}

}
