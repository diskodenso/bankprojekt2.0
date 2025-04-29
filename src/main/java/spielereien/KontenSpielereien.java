package spielereien;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;


import bankprojekt.verarbeitung.*;

/**
 * Testprogramm für Konten
 * @author Doro
 */
public class KontenSpielereien {

	/**
	 * Testprogramm für Konten
	 * @param args wird nicht benutzt
	 * @throws InputMismatchException wenn der Benutzer Unsinn eingibt
	 */
	public static void main(String[] args) {
		Kunde ich = new Kunde("Dorothea", "Hubrich", "zuhause", LocalDate.parse("1976-07-13"));

		Konto meinGiro = new Girokonto(ich, 1234,
				new Geldbetrag(1000));
		Konto meinSpar = new Sparbuch(ich, 9876);
			//Zuweisungskompatibilität
		
		meinGiro.ausgeben();
			//Hier toString aus Girokonto
		
		@SuppressWarnings("resource")
		Scanner tastatur = new Scanner(System.in);
		System.out.println("Konto 1 oder 2? ");
		int nr = tastatur.nextInt();
		switch(nr) {
			case 1 -> {
				meinGiro.einzahlen(new bankprojekt.verarbeitung.Geldbetrag(50));
				System.out.println(meinGiro);
			}
			case 2 -> {
				meinSpar.einzahlen(new bankprojekt.verarbeitung.Geldbetrag(50));
				try
				{
					boolean hatGeklappt = meinSpar.abheben(new bankprojekt.verarbeitung.Geldbetrag(70));
					System.out.println("Abhebung hat geklappt: " + hatGeklappt);
					System.out.println(meinSpar);
				}
				catch (GesperrtException e)
				{
					System.out.println("Zugriff auf gesperrtes Konto - Polizei rufen!");
				}
			}
		}
	}
}
