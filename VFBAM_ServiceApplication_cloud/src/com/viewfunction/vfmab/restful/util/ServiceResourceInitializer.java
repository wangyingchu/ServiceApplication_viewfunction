package com.viewfunction.vfmab.restful.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.viewfunction.contentRepository.util.RuntimeEnvironmentHandler;

public class ServiceResourceInitializer implements ServletContextListener{
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {		
		ServiceResourceHolder.getOfficeManager().stop();		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {		
		String documentFormatConvertsConfigFile=RuntimeEnvironmentHandler.getApplicationRootPath()+"DocumentsFormatConvertsCfg.properties";		
		Properties _properties=new Properties();
		try {
			_properties.load(new FileInputStream(documentFormatConvertsConfigFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}				
		String openOfficeHome=_properties.getProperty("OPENOFFICE_INSTALL_LOCATION");		
		OfficeManager officeManager=null;	
		DefaultOfficeManagerConfiguration defaultOfficeManagerConfiguration=new DefaultOfficeManagerConfiguration();
		if(openOfficeHome!=null){
			officeManager = defaultOfficeManagerConfiguration.setOfficeHome(openOfficeHome).buildOfficeManager();
		}else{
			officeManager = new DefaultOfficeManagerConfiguration().buildOfficeManager();
		}		
		ServiceResourceHolder.setOfficeManager(officeManager);
		officeManager.start();	
	}
}