package database;

public enum Columns {
    ID("id", "int", true), NAME("name", "varchar(45)", true), EMAIL("email", "varchar(45)", true),
    PASSWORD("password", "varchar(45)", true), FIRST_NAME("first_name", "varchar(45)", true),
    LAST_NAME("last_name", "varchar(45)", true), COUPON_ID("coupon_id", "int", true),
    CATEGORY_ID("category_id", "int", true), COMPANY_ID("company_id", "int", true),
    CUSTOMER_ID("customer_id", "int", true), TITLE("title", "varchar(45)", true),
    DESCRIPTION("description", "varchar(45)", false), START_DATE("start_date", "Timestamp", true),
    END_DATE("end_date", "Timestamp", true), AMOUNT("amount", "int", true), PRICE("price", "double", true),
    IMAGE("image", "varchar(45)", true);

    private String column;
    private String type;
    private boolean isNotNull;

    private Columns(String column, String type, boolean isNotNull) {
        this.column = column;
        this.type = type;
        this.isNotNull = isNotNull;
    }

    public String getColumn() {
        return this.column;
    }

    public String getType() {
        return this.type;

    }

    public boolean getIsNotNull() {
        return this.isNotNull;
    }

}