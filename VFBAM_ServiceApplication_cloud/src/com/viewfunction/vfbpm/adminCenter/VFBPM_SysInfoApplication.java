package com.viewfunction.vfbpm.adminCenter;

import com.vaadin.Application;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VFBPM_SysInfoApplication  extends Application{
	private static final long serialVersionUID = 8774488872217923320L;

	@Override
	public void init() {
		VerticalLayout rootLayout = new VerticalLayout();			
        final Window root = new Window("Viewfunction AAM System Info", rootLayout);          
        root.setImmediate(true);
        setMainWindow(root);          
        // sure it's 100% sized, and remove unwanted margins        
        rootLayout.setSizeFull();
        rootLayout.setMargin(false); 		
	}
}