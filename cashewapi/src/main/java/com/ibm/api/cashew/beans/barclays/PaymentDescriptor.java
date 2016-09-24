package com.ibm.api.cashew.beans.barclays;

public class PaymentDescriptor {
	private String id;

	private String logo;

	private String groupId;

	private Address address;

	private String name;
	private String paymentDescriptorType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ClassPojo [id = " + id + ", logo = " + logo + ", groupId = " + groupId + ", address = " + address
				+ ", name = " + name + "]";
	}

	public String getPaymentDescriptorType() {
		return paymentDescriptorType;
	}

	public void setPaymentDescriptorType(String paymentDescriptorType) {
		this.paymentDescriptorType = paymentDescriptorType;
	}
}
