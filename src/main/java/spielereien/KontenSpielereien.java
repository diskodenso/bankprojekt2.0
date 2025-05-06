package spielereien;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Sparbuch;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Bank;
import bankprojekt.exceptions.KontonummerNichtVorhandenException;

/**
 * Testprogramm für Konten und Bank.
 * Führt nacheinander Einzelkonten- und Bank-Tests durch.
 * @author Denis
 */
public class KontenSpielereien {

	/**
	 * Hauptmethode: Initialisiert den Scanner und ruft die Testblöcke auf.
	 *
	 * @param args wird nicht benutzt
	 */
	public static void main(String[] args) {
		try (Scanner tastatur = new Scanner(System.in)) {
			testEinzelkonten(tastatur);
			testBankFunktionen(tastatur);
		} catch (InputMismatchException e) {
			System.out.println("Ungültige Eingabe: " + e.getMessage());
		} catch (GesperrtException e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Führt Tests auf Girokonto und Sparbuch durch.
	 *
	 * @param tastatur Scanner für Nutzereingaben
	 */
	private static void testEinzelkonten(Scanner tastatur) {
		System.out.println("===== Einzelkonten-Tests =====");
		Kunde ich = new Kunde("Dagobert", "Duck", "zuhause", LocalDate.parse("1976-07-13"));
		Girokonto meinGiro = new Girokonto(ich, 1234, new Geldbetrag(1000));
		Sparbuch meinSpar = new Sparbuch(ich, 9876);

		System.out.println("-- Girokonto --");
		meinGiro.ausgeben();
		System.out.println(meinGiro);

		System.out.println("-- Sparbuch --");
		meinSpar.ausgeben();
		System.out.println(meinSpar);

		System.out.print("Konto 1 (Giro) oder 2 (Spar)? ");
		int nr = tastatur.nextInt();
		switch (nr) {
			case 1 -> {
				System.out.println("Einzahlung 50 auf Girokonto...");
				meinGiro.einzahlen(new Geldbetrag(50));
				System.out.println("Nach Einzahlung: " + meinGiro);
			}
			case 2 -> {
				System.out.println("Einzahlung 50 auf Sparbuch...");
				meinSpar.einzahlen(new Geldbetrag(50));
				try {
					System.out.println("Abhebung 70 vom Sparbuch...");
					boolean hatGeklappt = meinSpar.abheben(new Geldbetrag(70));
					System.out.println("Abhebung erfolgreich? " + hatGeklappt);
				} catch (GesperrtException e) {
					System.out.println("Zugriff auf gesperrtes Konto: " + e.getMessage());
				}
				System.out.println("Nach Vorgängen: " + meinSpar);
			}
			default -> System.out.println("Ungültige Auswahl");
		}
	}

	/**
	 * Führt Tests auf der Bank-Klasse durch.
	 *
	 * @param tastatur Scanner für Nutzereingaben (Konto-Löschung)
	 */
	private static void testBankFunktionen(Scanner tastatur) throws GesperrtException {
		System.out.println("\n===== Bank-Tests =====");
		Kunde ich = new Kunde("Donald", "Duck", "arbeit", LocalDate.parse("1976-07-13"));
		Bank meineBank = new Bank(10020030);

		long giroNr = meineBank.girokontoErstellen(ich);
		long sparNr = meineBank.sparbuchErstellen(ich);

		System.out.println("Bankleitzahl: " + meineBank.getBankleitzahl());
		System.out.println("Konten nach Erstellung:");
		System.out.println(meineBank.getAlleKonten());

		// Einzahlen
		System.out.println("Einzahlung 500 auf Girokonto...");
		meineBank.geldEinzahlen(giroNr, new Geldbetrag(500));
		System.out.printf("Kontostand Giro(%d): %s%n", giroNr, meineBank.getKontostand(giroNr));

		// Abheben
		System.out.println("Abhebung 50 vom Sparbuch...");
		boolean abgehoben = meineBank.geldAbheben(sparNr, new Geldbetrag(50));
		System.out.printf("Sparbuch-Abhebung erfolgreich? %b%n", abgehoben);
		System.out.printf("Kontostand Spar(%d): %s%n", sparNr, meineBank.getKontostand(sparNr));

		// Überweisung
		try {
			System.out.println("Überweisung 200 von Giro->Spar...");
			boolean ueberwiesen = meineBank.geldUeberweisen(
					giroNr, sparNr, new Geldbetrag(200), "Test-Überweisung"
			);
			System.out.printf("Überweisung erfolgreich? %b%n", ueberwiesen);
		} catch (KontonummerNichtVorhandenException e) {
			System.out.println("Überweisung fehlgeschlagen: " + e.getMessage());
		}
		System.out.println("Konten nach Überweisung:");
		System.out.println(meineBank.getAlleKonten());

		// Konto löschen
		System.out.print("Gib eine Kontonummer zum Löschen ein: ");
		long del = tastatur.nextLong();
		boolean geloescht = meineBank.kontoLoeschen(del);
		System.out.printf("Konto %d gelöscht? %b%n", del, geloescht);
		System.out.println("Konten nach Löschung:");
		System.out.println(meineBank.getAlleKonten());
	}
}
