package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
public class ProcessQueueTreeOnClickListener implements Property.ValueChangeListener{
	private static final long serialVersionUID = 5267137243063779781L;

	private ActivityProcessQueueTree activityProcessQueueTree;
	
	public ProcessQueueTreeOnClickListener(ActivityProcessQueueTree activityProcessQueueTree){
		this.activityProcessQueueTree=activityProcessQueueTree;		
	}
	
	public void valueChange(ValueChangeEvent event) {
		//the second time click on the same tree node will get a null event.getProperty().getValue()
        //this means unselect current tree node		
        if (event.getProperty().getValue() != null) {     
        	activityProcessQueueTree.handleTreeItemClick(event.getProperty().getValue());
        }		
	}
}