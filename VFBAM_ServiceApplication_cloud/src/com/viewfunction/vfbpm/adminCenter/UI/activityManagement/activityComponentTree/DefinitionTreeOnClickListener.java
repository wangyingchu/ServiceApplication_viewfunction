package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
public class DefinitionTreeOnClickListener  implements Property.ValueChangeListener{
	private static final long serialVersionUID = 4687810634084575946L;
	
	private ActivityDefinitionTree activityDefinitionTree;
	
	public DefinitionTreeOnClickListener(ActivityDefinitionTree activityDefinitionTree){
		this.activityDefinitionTree=activityDefinitionTree;
	}	

	public void valueChange(ValueChangeEvent event) {
		//the second time click on the same tree node will get a null event.getProperty().getValue()
        //this means unselect current tree node		
        if (event.getProperty().getValue() != null) {     
        	activityDefinitionTree.handleTreeItemClick(event.getProperty().getValue());
        }	
	}

}
