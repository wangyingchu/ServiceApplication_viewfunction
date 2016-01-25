package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
public class ParticipantTreeOnClickListener  implements Property.ValueChangeListener {
	private static final long serialVersionUID = -2654649523769046310L;

	private ActivityParticipantTree activityParticipantTree;
	
	public ParticipantTreeOnClickListener(ActivityParticipantTree activityParticipantTree){
		this.activityParticipantTree=activityParticipantTree;		
	}	
	
	public void valueChange(ValueChangeEvent event) {
		//the second time click on the same tree node will get a null event.getProperty().getValue()
        //this means unselect current tree node		
        if (event.getProperty().getValue() != null) {     
        	activityParticipantTree.handleTreeItemClick(event.getProperty().getValue());
        }		
	}
}