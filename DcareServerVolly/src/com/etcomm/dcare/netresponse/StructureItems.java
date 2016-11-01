package com.etcomm.dcare.netresponse;

import java.io.Serializable;

public class StructureItems extends Items implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String structure_id;

	private String structure;

	private String childs;

	public String getChilds() {
		return childs;
	}

	public void setChilds(String childs) {
		this.childs = childs;
	}

	public void setStructure_id(String structure_id) {
		this.structure_id = structure_id;
	}

	public String getStructure_id() {
		return this.structure_id;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getStructure() {
		return this.structure;
	}

	@Override
	public String toString() {
		return "StructureItems [structure_id=" + structure_id + ", structure=" + structure + ", childs=" + childs + "]";
	}

}
