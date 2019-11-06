package com.paas.sms.tenantservice.model;

public class CountryDetails {

	private int extensionNumber;
	private String countryCode;
	private String countryName;

	public int getExtensionNumber() {
		return extensionNumber;
	}

	public void setExtensionNumber(int extensionNumber) {
		this.extensionNumber = extensionNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Override
	public String toString() {
		return "CountryDetails [extensionNumber=" + extensionNumber + ", countryCode=" + countryCode + ", countryName="
				+ countryName + "]";
	}

}
