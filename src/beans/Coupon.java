package beans;

import java.sql.Timestamp;

public class Coupon {

	private int id;
	private int companyID;
	private Category category;
	private String title;
	private String description;
	private Timestamp startDate;
	private Timestamp endDate;
	private int amount;
	private double price;
	private String image;

	public Coupon(int id, int companyID, Category category, String title, String description, Timestamp startDate,
			Timestamp endDate, int amount, double price, String image) {
		super();
		this.id = id;
		this.companyID = companyID;
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
	}

	/**
	 * @return int id of the coupon
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return int id of the company that own the coupon
	 */
	public int getCompanyID() {
		return companyID;
	}

	/**
	 * @param companyID
	 */
	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	/**
	 * @return Category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Timestamp
	 */
	public Timestamp getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 */
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return Timestamp
	 */
	public Timestamp getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return int
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return double
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return String
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/**
	 * @param obj
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coupon other = (Coupon) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		return "Coupon id=" + id + " \n[ companyID=" + companyID + ", category=" + category + ", title=" + title
				+ ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate + ", amount="
				+ amount + ", price=" + price + ", image=" + image + "]\n";
	}

}
