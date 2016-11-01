package com.etcomm.dcare.netresponse;

import java.util.List;

public class StructureContent extends Content {
	private String customer;

	private List<StructureItems> structure;
	private String islevel;

	public String getIslevel() {
		return islevel;
	}

	public void setIslevel(String islevel) {
		this.islevel = islevel;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCustomer() {
		return this.customer;
	}

	public void setStructure(List<StructureItems> structure) {
		this.structure = structure;
	}

	public List<StructureItems> getStructure() {
		return this.structure;
	}

	@Override
	public String toString() {
		return "StructureContent [customer=" + customer + ", structure=" + structure + "]";
	}

}
