package etsisi.upm;

import java.util.Scanner;

public class Main {
    private static final String CURSOR = "tUPM> ";
    public static void main(String [] args) {
        Scanner sc = new Scanner(System.in);
        int status = 0;
        Menu menu = new Menu();
        do {
            System.out.print(CURSOR);
            status = menu.newQuery(sc.nextLine());
         } while (status == 0);
    }
}
