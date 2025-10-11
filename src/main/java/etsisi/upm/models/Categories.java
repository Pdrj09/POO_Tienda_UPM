package etsisi.upm.models;


public enum Categories {
    //Every cathegory type and its discounts
    MERCH(0.0), //0%
    STATIONERY(0.05),//5%
    CLOTHES(0.07),//7%
    BOOK(0.1),//10%
    ELECTRONICS(0.03);//3%
    private final double discount;

    Categories(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return this.discount;
    }

    //verifies if a category exists
    public static boolean existCategory(String category) {
        boolean exist = false;
        for (Categories a : Categories.values()) {
            if (a.name().equals(category)) {
                exist = true;
                break;
            }
        }
        return exist;
    }
}


