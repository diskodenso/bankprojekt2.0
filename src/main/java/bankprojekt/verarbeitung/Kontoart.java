package bankprojekt.verarbeitung;

/**
 * alle zur Verfügung stehenden Kontoarten
 * Achtung: kein gutes Beispiel, eigentlich sind die Kontoarten erweiterbar
 */
public enum Kontoart {
	//ERST die Konstanten:
	/**
	 * ein Girokonto
	 */
	GIROKONTO("mit hohem Dispo"),
	/**
	 * ein Sparbuch
	 */
	SPARBUCH("mit gaaaanz vielen Zinsen"),
	/**
	 * ein Festgeldkonto
	 */
	FESTGELDKONTO("Ham wa eigentlich gar nich");
	
	//dann den Rest:
	private String werbebotschaft;
	
	Kontoart(String werbe) {   //automatisch private
		this.werbebotschaft = werbe;
	}
	
	/**
	 * liefert den Werbetext zu dieser Konroart
	 * @return Werbetext
	 */
	public String getWerbung() {
		return this.werbebotschaft;
	}
	
	@Override
	public String toString() {
		return this.name().charAt(0) 
				+ this.name().substring(1).toLowerCase();
	}
	
}
