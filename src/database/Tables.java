package database;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Tables {

    COMPANIES("companies",
            new HashSet<Columns>(Arrays.asList(Columns.ID, Columns.NAME, Columns.EMAIL, Columns.PASSWORD)), Columns.ID),
    CATEGORIES("categories", new HashSet<Columns>(Arrays.asList(Columns.ID, Columns.NAME)), Columns.ID),
    CUSTOMERS("customers",
            new HashSet<Columns>(
                    Arrays.asList(Columns.ID, Columns.FIRST_NAME, Columns.LAST_NAME, Columns.EMAIL, Columns.PASSWORD)),
            Columns.ID),
    COUPONS("coupons",
            new HashSet<Columns>(Arrays.asList(Columns.ID, Columns.COMPANY_ID, Columns.CATEGORY_ID, Columns.TITLE,
                    Columns.DESCRIPTION, Columns.START_DATE, Columns.END_DATE, Columns.AMOUNT, Columns.PRICE,
                    Columns.IMAGE)),
            Columns.ID),
    CUSTOMERS_VS_COUPONS("customers_vs_coupons",
            new HashSet<Columns>(Arrays.asList(Columns.ID, Columns.CUSTOMER_ID, Columns.COUPON_ID)), Columns.ID);

    private String name;
    private Columns primaryKey;
    private Set<Columns> myColumnsSet;

    private Tables(String name, Set<Columns> myColumnsSet, Columns primaryKey) {
        this.name = name;
        this.myColumnsSet = myColumnsSet;
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Columns> getMyColumnsSet() {
        return myColumnsSet;
    }

    public void setMyColumnsSet(Set<Columns> myColumnsSet) {
        this.myColumnsSet = myColumnsSet;
    }

    public Columns getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Columns primaryKey) {
        this.primaryKey = primaryKey;
    }

}