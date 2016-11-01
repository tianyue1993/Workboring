package com.etcomm.dcare.netresponse;

public class MyPointsDetailItems extends Items {
	private String type;

	private String score;

	private String description;

	private String income_and_expenditure;

	private String created_at;

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScore() {
		return this.score;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setIncome_and_expenditure(String income_and_expenditure) {
		this.income_and_expenditure = income_and_expenditure;
	}

	public String getIncome_and_expenditure() {
		return this.income_and_expenditure;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getCreated_at() {
		return this.created_at;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if(o instanceof MyPointsDetailItems){
			MyPointsDetailItems cur = (MyPointsDetailItems)o;
			if(cur.getCreated_at().equals(this.created_at)&&(cur.getDescription().equals(this.description))&&(cur.getScore().equals(this.score))&&(cur.getType().equals(this.type))){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
}
