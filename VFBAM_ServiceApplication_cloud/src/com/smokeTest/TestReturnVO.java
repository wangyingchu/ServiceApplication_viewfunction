package com.smokeTest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TestReturnVO")
public class TestReturnVO {
	private String voName;
	private long tiemStamp;

	public String getVoName() {
		return voName;
	}

	public void setVoName(String voName) {
		this.voName = voName;
	}

	public long getTiemStamp() {
		return tiemStamp;
	}

	public void setTiemStamp(long tiemStamp) {
		this.tiemStamp = tiemStamp;
	}
}