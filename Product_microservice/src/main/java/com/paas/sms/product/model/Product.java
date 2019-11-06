package com.paas.sms.product.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.gridfs.GridFSDBFile;

/*import lombok.Data;
@Data
*/
@Document(collection = "Product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long productId;
	@Column(unique = true)
	private String productName;
	private double productPrice;
	private String productDescription;
	private String productURL;
	private String productDuration;
	private String message;
	private GridFSDBFile gridFSDBFile;
	private String productImage;
	private Timestamp timeStamp;
	private String startDate;
	private String suiteName;
	private long suiteId;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public GridFSDBFile getGridFSDBFile() {
		return gridFSDBFile;
	}

	public void setGridFSDBFile(GridFSDBFile gridFSDBFile) {
		this.gridFSDBFile = gridFSDBFile;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getProductURL() {
		return productURL;
	}

	public void setProductURL(String productURL) {
		this.productURL = productURL;
	}

	public long getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(long suiteId) {
		this.suiteId = suiteId;
	}

	public String getProductDuration() {
		return productDuration;
	}

	public void setProductDuration(String productDuration) {
		this.productDuration = productDuration;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", productPrice=" + productPrice
				+ ", productDescription=" + productDescription + ", productURL=" + productURL + ", productDuration="
				+ productDuration + ", message=" + message + ", gridFSDBFile=" + gridFSDBFile + ", productImage="
				+ productImage + ", timeStamp=" + timeStamp + ", startDate=" + startDate + ", suiteName=" + suiteName
				+ ", suiteId=" + suiteId + "]";
	}

}
