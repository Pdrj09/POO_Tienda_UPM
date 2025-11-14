package etsisi.upm;

import etsisi.upm.io.CLI;

import java.util.Scanner;

public class Main {
    private static final String CURSOR = "tUPM> ";

    public static void main(String [] args) {
        //We call scanner
        Scanner sc = new Scanner(System.in);
        int status;
        CLI CLI = new CLI(); // we create a menu
        do {
            System.out.print(CURSOR);
            status = CLI.newQuery(sc.nextLine());
         } while (status == 1);
    }
}
