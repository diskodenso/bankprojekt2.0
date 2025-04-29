package bankprojekt.verarbeitung;

import bankprojekt.nuetzliches.Kalender;

import java.time.LocalDate;

/**
 * ein Sparbuch, d.h. ein Konto, das nur recht eingeschränkt genutzt
 * werden kann. Insbesondere darf man monatlich nur höchstens 2000€
 * abheben, wobei der Kontostand nie unter 0,50€ fallen darf. Zur Abfrage
 * des heutigen Datums (was die Zuordnung zweier Abhebungen zu einem gemeinsamen
 * Monat bestimmt) kann ein Kalender-Objekt angegeben werden. 
 * @author Doro
 *
 */public class Sparbuch extends Konto {
	/**
	 * Zinssatz, mit dem das Sparbuch verzinst wird. 0,03 entspricht 3%
	 */
	private double zinssatz;
	
	/**
	 * Monatlich erlaubter Gesamtbetrag für Abhebungen
	 */
	public static final Geldbetrag ABHEBESUMME = new Geldbetrag(2000);
	
	/**
	 * Betrag, der nach einer Abhebung mindestens auf dem Konto bleiben muss
	 */
	public static final Geldbetrag MINIMUM = new Geldbetrag(0.5);
	
	/**
	 * Betrag, der im aktuellen Monat bereits abgehoben wurde
	 */
	private Geldbetrag bereitsAbgehoben = Geldbetrag.NULL_EURO;

	/**
	 * Kalender zur Abfrage des heutigen Datums
	 */
	private Kalender kalender;

	/**
	 * Monat und Jahr der letzten Abhebung
	 */
	private LocalDate zeitpunkt; 
	
	/**
	* ein Standard-Sparbuch
	*/
	public Sparbuch() {
		this.zinssatz = 0.03;
		this.kalender = new Kalender();
		zeitpunkt = kalender.getHeutigesDatum();
	}

	/**
	* ein Standard-Sparbuch, das inhaber gehört und die angegebene Kontonummer hat
	* Das heutige Datum wird an allen Stellen, wo es benötigt wird, vom Betriebssystem
	* abgefragt.
	* @param inhaber der Kontoinhaber
	* @param kontonummer die Wunsch-Kontonummer
	* @throws IllegalArgumentException wenn inhaber null ist
	*/
	public Sparbuch(Kunde inhaber, long kontonummer) {
		this(inhaber, kontonummer, new Kalender());
	}
	
	/**
	* ein Standard-Sparbuch, das inhaber gehört und die angegebene Kontonummer hat
	* @param inhaber der Kontoinhaber
	* @param kontonummer die Wunsch-Kontonummer
	* @param kalender der Kalender, der für die Abfrage des heutigen Datums benutzt wird
	* @throws IllegalArgumentException wenn inhaber oder kalender null ist
	*/
	public Sparbuch(Kunde inhaber, long kontonummer, Kalender kalender) {
		super(inhaber, kontonummer);
		this.zinssatz = 0.03;
		if(kalender == null)
			throw new IllegalArgumentException();
		this.kalender = kalender;
		zeitpunkt = kalender.getHeutigesDatum();
	}
	
	@Override
	public String toString()
	{
    	String ausgabe = "-- SPARBUCH --" + System.lineSeparator() +
    	super.toString()
    	+ "Zinssatz: " + this.zinssatz * 100 +"%" + System.lineSeparator();
    	return ausgabe;
	}

	@Override
	public boolean abheben (Geldbetrag betrag) throws GesperrtException{
		if (betrag == null || betrag.isNegativ()) {
			throw new IllegalArgumentException("Betrag ungültig");
		}
		if(this.isGesperrt())
		{
			GesperrtException e = new GesperrtException(this.getKontonummer());
			throw e;
		}
		LocalDate heute = kalender.getHeutigesDatum();
		if(heute.getMonth() != zeitpunkt.getMonth() || heute.getYear() != zeitpunkt.getYear())
		{
			this.bereitsAbgehoben = Geldbetrag.NULL_EURO;
		}
		Geldbetrag neu = getKontostand().minus(betrag);
		if (neu.compareTo(Sparbuch.MINIMUM) >= 0 && 
				 bereitsAbgehoben.plus(betrag).compareTo(Sparbuch.ABHEBESUMME)<= 0)
		{
			setKontostand(neu);
			bereitsAbgehoben = bereitsAbgehoben.plus(betrag);
			this.zeitpunkt = heute;
			return true;
		}
		else
			return false;
	}

}
