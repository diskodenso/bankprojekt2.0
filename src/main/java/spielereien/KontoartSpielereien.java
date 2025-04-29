package spielereien;

import bankprojekt.verarbeitung.Kontoart;

/**
 * probiert die Enum Kontoart aus
 */
public class KontoartSpielereien {

	/**
	 * zeigt Möglichkeiten einer Enum
	 * @param args wird nicht verwendet
	 */
	public static void main(String[] args) {
		Kontoart art;
		//geht nicht: art = new Kontoart();
		art = Kontoart.SPARBUCH;
		System.out.println(art + " " + art.ordinal());
		
		art = Kontoart.valueOf("FESTGELDKONTO");
		System.out.println(art.getWerbung());
		
		System.out.println("Alle unsere Kontoarten:");
		Kontoart[] alle = Kontoart.values();
		for(Kontoart a: alle) {
			System.out.println(a + ": " + a.getWerbung());
		}
	}

}
