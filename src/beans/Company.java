package beans;

import java.util.ArrayList;

public class Company {

	private int id;
	private String name;
	private String email;
	private String password;
	private ArrayList<Coupon> coupons = new ArrayList<>();

	public Company(int id, String name, String email, String password, ArrayList<Coupon> coupons) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.coupons = coupons;
	}

	/**
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return ArrayList<Coupon>
	 */
	public ArrayList<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * @param coupons
	 */
	public void setCoupons(ArrayList<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * @param coupon
	 */
	public void addCoupon(Coupon coupon) {
		coupons.add(coupon);
		System.out.println("Coupon id= " + coupon.getId() + " was added successfully");
	}

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		return "Company id=" + id + "[name=" + name + ", email=" + email + ", password=" + password + "\nCoupons=\n"
				+ coupons + "]";
	}

}
