package com.viewfunction.vfmab.restful.util;

import org.artofsolving.jodconverter.office.OfficeManager;

public class ServiceResourceHolder {
	private static OfficeManager officeManager;

	public static OfficeManager getOfficeManager() {
		return officeManager;
	}

	public static void setOfficeManager(OfficeManager officeManager) {
		ServiceResourceHolder.officeManager = officeManager;
	}

}
