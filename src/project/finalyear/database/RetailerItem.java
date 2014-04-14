package project.finalyear.database;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RetailerItem implements Serializable {
	private long id;
	private long userId;
	private String retailername;
	private String product;
	private String latitude ;
	private String longitude;
	private long dueTime;
	private boolean checked;
	private boolean noDueTime;
	private long discount;

	public String toSyncString() {
		return id+"\t"+retailername+"\t"+product+"\t"+dueTime+"\t"+noDueTime+"\t"+checked+"\t"+discount;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return retailername;
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getRetailername() {
		return retailername;
	}

	public void setRetailername(String name) {
		this.retailername = name;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public long getDueTime() {
		return dueTime;
	}

	public void setDueTime(long dueTime) {
		this.dueTime = dueTime;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isNoDueTime() {
		return noDueTime;
	}

	public void setNoDueTime(boolean noDueTime) {
		this.noDueTime = noDueTime;
	}

	public long getDiscount() {
		return discount;
	}

	public void setDiscount(long priority) {
		this.discount = priority;
	}
}
