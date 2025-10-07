package etsisi.upm.models;

/// ESTO HAY QUE USARLO EN MENU, HAY QUE CHECKEAR SI EXISTE LA CATEGORIA O NO ANTES
/// DE CREAR UN PRODUCTO
public enum Categories {
    MERCH,
    STATIONERY,
    CLOTHING,
    BOOK,
    ELECTRONICS;

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


