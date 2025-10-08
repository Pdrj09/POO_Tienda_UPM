package etsisi.upm.models;

/// ESTO HAY QUE USARLO EN MENU, HAY QUE CHECKEAR SI EXISTE LA CATEGORIA O NO ANTES
/// DE CREAR UN PRODUCTO
public enum Categories {
    MERCH(0.0),
    STATIONERY(0.05),
    CLOTHES(0.07),
    BOOK(0.1),
    ELECTRONICS(0.03);
    private final double discount;

    Categories(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return this.discount;
    }

    //verifies is a category exists
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


