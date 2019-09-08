package loggingTutorial;

import java.util.Date;

/**
 * A domain class representing a single transaction.
 * @param jens dietrich
 */
public class Purchase {

	
	private String retailer = "";
	private double amount = 0.0;
	private Date date = null;
	
	public Purchase(String retailer, double amount, Date date) {
		super();
		this.retailer = retailer;
		this.amount = amount;
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Purchase [retailer=" + retailer + ", amount=" + amount
				+ ", date=" + date + "]";
	}

	public String getRetailer() {
		return retailer;
	}
	public void setRetailer(String retailer) {
		this.retailer = retailer;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((retailer == null) ? 0 : retailer.hashCode());
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
		Purchase other = (Purchase) obj;
		if (Double.doubleToLongBits(amount) != Double
				.doubleToLongBits(other.amount))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (retailer == null) {
			if (other.retailer != null)
				return false;
		} else if (!retailer.equals(other.retailer))
			return false;
		return true;
	}

		

}
