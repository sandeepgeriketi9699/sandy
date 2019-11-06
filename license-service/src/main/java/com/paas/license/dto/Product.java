package com.paas.license.dto;

import java.sql.Timestamp;

import com.mongodb.gridfs.GridFSDBFile;

public class Product {

	private long productId;

	private String productName;
	private double productPrice;
	private String productDescription;
	private String productURL;
	private String message;
	private GridFSDBFile gridFSDBFile;
	private String productImage;
	private Timestamp timeStamp;
	private String startDate;
	private String suiteName;
	private long suiteId;
	private String productDuration;
	private boolean trial;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (productId ^ (productId >>> 32));
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + (trial ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (productId != other.productId)
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (trial != other.trial)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productPrice=" + productPrice
				+ ", productDescription=" + productDescription + ", productURL=" + productURL + ", message=" + message
				+ ", gridFSDBFile=" + gridFSDBFile + ", timeStamp=" + timeStamp + ", startDate=" + startDate
				+ ", suiteName=" + suiteName + ", suiteId=" + suiteId + ", productDuration=" + productDuration
				+ ", trial=" + trial + "]";
	}

}
