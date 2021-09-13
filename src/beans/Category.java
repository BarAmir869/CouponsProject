package beans;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import Exeptions.NoSuchCategoryIdException;

public enum Category {

	FOOD(), ELECTRICITY(), RESTAURANT(), VACATION(), SHOES();

	private int id;

	private Category() {
		this(Counter.nextValue);
	}

	private Category(int id) {
		this.id = id;
		Counter.nextValue = id + 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static Set<Category> getAllCategories() {
		Set<Category> set = new HashSet<>(Arrays.asList(Category.values()));
		return set;
	}

	public static Category getCategoryByID(int id) throws NoSuchCategoryIdException {
		Optional<Category> optional = Arrays.stream(values()).filter(Category -> Category.id == id).findFirst();
		if (optional.get() == null) {
			System.out.println("No category with id = " + id);// need to throw customize exception
			throw new NoSuchCategoryIdException();
		}
		return optional.get();
	}

	private static class Counter {
		private static int nextValue = 1;
	}

	public static Category getRandom() throws NoSuchCategoryIdException {
		Set<Category> categoriesSet = getAllCategories();
		Random rnd = new Random();
		int id = rnd.nextInt(categoriesSet.size() - 1) + 1;
		return getCategoryByID(id);
	}
}
