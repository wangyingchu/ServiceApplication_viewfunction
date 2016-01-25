package com.viewfunction.vfbpm.adminCenter.UI.processManagement;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

public class ProcessSpaceDetailTreeOnClickListener implements Property.ValueChangeListener{
	private static final long serialVersionUID = -247656505278640535L;
	
	private ProcessSpaceDetailTree processSpaceDetailTree;
	
	public ProcessSpaceDetailTreeOnClickListener(ProcessSpaceDetailTree processSpaceDetailTree){
		this.processSpaceDetailTree=processSpaceDetailTree;
	}	

	public void valueChange(ValueChangeEvent event) {
		//the second time click on the same tree node will get a null event.getProperty().getValue()
        //this means unselect current tree node		
        if (event.getProperty().getValue() != null) {     
        	this.processSpaceDetailTree.handleTreeItemClick(event.getProperty().getValue());
        }	
	}
}