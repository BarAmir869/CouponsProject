package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

import beans.Category;

public class TablesConstructing {

	private static ConnectionPool connectionPool = ConnectionPool.getInstance();

	public static void dropAllTables() {
		try {
			dropTable(Tables.COMPANIES);
			dropTable(Tables.CATEGORIES);
			dropTable(Tables.COUPONS);
			dropTable(Tables.CUSTOMERS);
			dropTable(Tables.CUSTOMERS_VS_COUPONS);
			addAllCategories(Tables.CATEGORIES);
		} catch (SQLException e) {
			System.out.println("Query is wrong, ");
		}
		System.out.println("***********************************************************************");
		System.err.println("***************\t\tTables dropping done\t\t***************");
		System.out.println("***********************************************************************");
	}

	private static void dropTable(Tables table) throws SQLException {
		Connection connection = connectionPool.getConnection();
		String sql = "DROP TABLE `coupon_project`.`" + table.getName() + "`;";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			connectionPool.restoreConnection(connection);
		}
	}

	public static void construct() throws SQLException {
		try {
			createNewTable(Tables.COMPANIES);
			createNewTable(Tables.CATEGORIES);
			createNewTable(Tables.COUPONS);
			createNewTable(Tables.CUSTOMERS);
			createNewTable(Tables.CUSTOMERS_VS_COUPONS);
			addAllCategories(Tables.CATEGORIES);
		} catch (SQLException e) {
			throw new SQLException("***************       Tables already constructed        ***************");
		}
		System.err.println("***************\t\tTables Constructing done\t***************");
	}

	public static void createNewTable(Tables table) throws SQLException {
		Connection connection = connectionPool.getConnection();
		String sql = "CREATE TABLE `coupon_project`.`" + table.getName() + "` (";
		PreparedStatement preparedStatement;
		for (Columns key : table.getMyColumnsSet()) {
			sql += "`" + key.getColumn() + "` " + key.getType() + " ";
			sql += key.getIsNotNull() ? "NOT NULL" : "NULL";
			if (table.getName().equals("customers_vs_coupons") && key.getColumn().equals("id"))
				sql += " AUTO_INCREMENT";
			sql += ",\n";
		}
		if (table.getMyColumnsSet().contains(table.getPrimaryKey())) {
			sql += "PRIMARY KEY (`" + table.getPrimaryKey().getColumn() + "`));";
		} else {
			throw new SQLException("Set not contains the primarykey");
		}
		preparedStatement = connection.prepareStatement(sql);
		try {
			preparedStatement.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			connectionPool.restoreConnection(connection);
		}

	}

	private static void addAllCategories(Tables categories) {
		{

			String sql;
			for (Category category : Category.getAllCategories()) {
				sql = "INSERT INTO `coupon_project`.`categories` (`";
				sql += String.join("`,`",
						categories.getMyColumnsSet().stream().map(c -> c.getColumn()).collect(Collectors.toList()));
				sql += "`) values ('";
				sql += String.join("','", category + "", category.getId() + "");
				sql += "');\n";
				Connection connection = connectionPool.getConnection();
				try {

					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.execute();
				} catch (SQLException e) {
					// System.out.println("category = " + category + " already exist");
				} finally {
					connectionPool.restoreConnection(connection);
				}
			}

		}
	}
}
