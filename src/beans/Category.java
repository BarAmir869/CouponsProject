/**
 * This class represent a java object(enum) of Coupon's Category.
 * @version 19.09.2021
 * @author Bar Amir
 */
package beans;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import Exceptions.NoSuchCategoryIdException;

public enum Category {
	// Category types
	FOOD(), ELECTRICITY(), RESTAURANT(), VACATION(), SHOES();

	// instance variable
	private int id;

	// Default constructor
	/**
	 * Constructs a Category with a new id that's follow the last id that was used.
	 * This constructor uses the "id constructor".
	 */
	private Category() {
		this(Counter.nextValue);
	}

	// id constructor
	/**
	 * Constructs a Category with a new id and increment nextValue by 1;
	 * 
	 * @param id the id of the new Category
	 */
	private Category(int id) {
		this.id = id;
		Counter.nextValue = id + 1;
	}

	/**
	 * @return id the id of the Category
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @return a set of all categories enums
	 */
	public static Set<Category> getAllCategories() {
		Set<Category> set = new HashSet<>(Arrays.asList(Category.values()));
		return set;
	}

	/**
	 * 
	 * @param id the id of the requested Category
	 * @return the Category of @param id, throws exception if there is no such
	 *         Category
	 * @throws NoSuchCategoryIdException
	 */
	public static Category getCategoryByID(int id) throws NoSuchCategoryIdException {
		Optional<Category> optional = Arrays.stream(values()).filter(Category -> Category.id == id).findFirst();
		if (optional.isEmpty()) {
			throw new NoSuchCategoryIdException("There is no Category id: " + id);
		}
		return optional.get();
	}

	// private class for id's counter
	private static class Counter {
		private static int nextValue = 1;
	}

	// This method was built for test, it gives a random Category
	public static Category getRandom() {
		Set<Category> categoriesSet = getAllCategories();
		Random rnd = new Random();
		int id = rnd.nextInt(categoriesSet.size() - 1) + 1;
		try {
			return getCategoryByID(id);
		} catch (NoSuchCategoryIdException e) {
			e.getMessage();
		}
		return null;
	}
}
