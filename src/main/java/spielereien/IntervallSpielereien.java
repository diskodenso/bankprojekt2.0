package spielereien;

import mathematik.Intervall;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;


/**
 * Testprogramm für Intervall.
 * Führt nacheinander String Date und Time Intervalle aus und prüft Schnittmenge
 * @author Denis
 */
public class IntervallSpielereien {
    public static void main(String[] args) {

        // ein Intervall von Strings anlegen und alle drei
        // Methoden mindestens einmal dafür aufrufen.
        Intervall<String> texte = new Intervall<>("Apfel", "Birne");
        System.out.println("String Intervall: " + texte);
        System.out.println("Untere Grenze: " + texte.getUntergrenze());
        System.out.println("Obere Grenze: " + texte.getObergrenze());
        System.out.println("enthaelt Bananen?: " + texte.enthaelt("Aal"));
        System.out.println("isLeer " + texte.isLeer());

        // Schnitt mit einem zweiten String-Intervall
        Intervall<String> texte2 = new Intervall<>("Avocado", "Zitrone");
        System.out.println("String Intervall: " + texte2);
        System.out.println("String Intervall: " + texte.schnitt(texte2));

        System.out.println();

        // Date Intervalle - Jahr ab 1900, Monat ab 0
        Date d1 = new Date(93, 3, 29);
        Date d2 = new Date(123, 11, 19);
        Intervall<Date> datum01 = new Intervall<>(d1, d2);

        Date d3 = new Date(201, 5, 12);
        Date d4 = new Date(140, 7, 9);
        Intervall<Date> datum02 = new Intervall<>(d3, d4);
        System.out.println("Datum Intervall 1: " + datum01);
        System.out.println("Datum Intervall 2: " + datum02);
        System.out.println("Schnitt: " +  datum01.schnitt(datum02));

        System.out.println();

        Time t1 = Time.valueOf("08:00:00");
        Time t2 = Time.valueOf("12:00:00");
        Intervall<Time> zeit01 = new Intervall<>(t1, t2);


        System.out.println("Zeit01: " + zeit01);
        System.out.println("Schnitt Zeit Datum: " + datum01.schnitt(zeit01));

        System.out.println();

         // 5) Compiler‐geprüfte Unsinns-Aufrufe (auskommentiert)
        //         Intervall<Object> falsch = new Intervall<>(new Object(), new Object());
        //         texte.enthaelt(3.14);
        //         Intervall<Double> zahlen = new Intervall<>(1.2, 3.4);
        //         Intervall<String> texte3 = new Intervall<>("a","b");
        //         zahlen.schnitt(texte3);
    }
}
