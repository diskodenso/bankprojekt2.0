package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung;

/**
 * stellt ein allgemeines Bank-Konto dar
 */
public abstract class Konto implements Comparable<Konto> {
	public void ausgeben() {
		System.out.println(this.toString());
	}

	/**
	 * der Kontoinhaber
	 */
	private Kunde inhaber;

	/**
	 * die Kontonummer
	 */
	private final long nummer;

	/**
	 * der aktuelle Kontostand
	 */
	private Geldbetrag kontostand;

	/**
	 * setzt den aktuellen Kontostand
	 *
	 * @param kontostand neuer Kontostand, darf nicht null sein
	 */
	protected void setKontostand(Geldbetrag kontostand) {
		if (kontostand != null)
			this.kontostand = kontostand;
	}

	/**
	 * Wenn das Konto gesperrt ist (gesperrt = true), können keine Aktionen daran mehr vorgenommen werden,
	 * die zum Schaden des Kontoinhabers wären (abheben, Inhaberwechsel)
	 */
	private boolean gesperrt;

	/**
	 * Setzt die beiden Eigenschaften kontoinhaber und kontonummer auf die angegebenen Werte,
	 * der anfängliche Kontostand wird auf 0 gesetzt.
	 *
	 * @param inhaber     der Inhaber
	 * @param kontonummer die gewünschte Kontonummer
	 * @throws IllegalArgumentException wenn der inhaber null ist
	 */
	public Konto(Kunde inhaber, long kontonummer) {
		if (inhaber == null)
			throw new IllegalArgumentException("Inhaber darf nicht null sein!");
		this.inhaber = inhaber;
		this.nummer = kontonummer;
		this.kontostand = Geldbetrag.NULL_EURO;
		this.gesperrt = false;
	}

	/**
	 * setzt alle Eigenschaften des Kontos auf Standardwerte
	 */
	public Konto() {
		this(Kunde.MUSTERMANN, 1234567);
	}

	/**
	 * liefert den Kontoinhaber zurück
	 *
	 * @return der Inhaber
	 */
	public Kunde getInhaber() {
		return this.inhaber;
	}

	/**
	 * setzt den Kontoinhaber
	 *
	 * @param kinh neuer Kontoinhaber
	 * @throws GesperrtException        wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException wenn kinh null ist
	 */
	public void setInhaber(Kunde kinh) throws GesperrtException {
		if (kinh == null)
			throw new IllegalArgumentException("Der Inhaber darf nicht null sein!");
		if (this.gesperrt)
			throw new GesperrtException(this.nummer);
		this.inhaber = kinh;

	}

	/**
	 * liefert den aktuellen Kontostand
	 *
	 * @return Kontostand
	 */
	public Geldbetrag getKontostand() {
		return kontostand;
	}

	/**
	 * liefert die Kontonummer zurück
	 *
	 * @return Kontonummer
	 */
	public long getKontonummer() {
		return nummer;
	}

	/**
	 * liefert zurück, ob das Konto gesperrt ist oder nicht
	 *
	 * @return true, wenn das Konto gesperrt ist
	 */
	public boolean isGesperrt() {
		return gesperrt;
	}

	/**
	 * Erhöht den Kontostand um den eingezahlten Betrag.
	 *
	 * @param betrag double
	 * @throws IllegalArgumentException wenn der betrag negativ ist
	 */
	public void einzahlen(Geldbetrag betrag) {
		if (betrag == null || betrag.isNegativ()) {
			throw new IllegalArgumentException("Falscher Betrag");
		}
		setKontostand(getKontostand().plus(betrag));
	}

	@Override
	public String toString() {
		String ausgabe;
		ausgabe = "Kontonummer: " + this.getKontonummerFormatiert()
				+ System.getProperty("line.separator");
		ausgabe += "Inhaber: " + this.inhaber;
		ausgabe += "Aktueller Kontostand: " + getKontostand() + " ";
		ausgabe += this.getGesperrtText() + System.getProperty("line.separator");
		return ausgabe;
	}

	/**
	 * Mit dieser Methode wird der geforderte Betrag vom Konto abgehoben, wenn es nicht gesperrt ist
	 * und die speziellen Abheberegeln des jeweiligen Kontotyps die Abhebung erlauben
	 *
	 * @param betrag abzuhebender Betrag
	 * @return true, wenn die Abhebung geklappt hat,
	 * false, wenn sie abgelehnt wurde
	 * @throws GesperrtException        wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException wenn der betrag negativ oder unendlich oder NaN ist
	 */
	public abstract boolean abheben(Geldbetrag betrag)
			throws GesperrtException;

	/**
	 * sperrt das Konto, Aktionen zum Schaden des Benutzers sind nicht mehr möglich.
	 */
	public void sperren() {
		this.gesperrt = true;
	}

	/**
	 * entsperrt das Konto, alle Kontoaktionen sind wieder möglich.
	 */
	public void entsperren() {
		this.gesperrt = false;
	}


	/**
	 * liefert eine String-Ausgabe, wenn das Konto gesperrt ist
	 *
	 * @return "GESPERRT", wenn das Konto gesperrt ist, ansonsten ""
	 */
	public String getGesperrtText() {
		if (this.gesperrt) {
			return "GESPERRT";
		} else {
			return "";
		}
	}

	/**
	 * liefert die ordentlich formatierte Kontonummer
	 *
	 * @return auf 10 Stellen formatierte Kontonummer
	 */
	public String getKontonummerFormatiert() {
		return String.format("%10d", this.nummer);
	}


	/**
	 * Wechselt die Währung des Kontos und passt Kontostand
	 * (und ggf. Dispo) an die neue Währung an.
	 * @param waehrung die neue Währung, in die gewechselt werden soll
	 * @throws IllegalArgumentException wenn die übergebene Währung null ist
	 */
	public void waehrungswechsel(Waehrung waehrung) throws IllegalArgumentException{
		if (waehrung == null)
			throw new IllegalArgumentException("Neue Währung null");
		// Kontostand konvertieren
		setKontostand(getKontostand().umrechnen(waehrung));
		// Dispo bei Girokonto mitnehmen
		if (this instanceof Girokonto) {
			Girokonto g = (Girokonto) this;
			g.setDispo(g.getDispo().umrechnen(waehrung));
		}
	}
	/**
	 * Vergleich von this mit other; Zwei Konten gelten als gleich,
	 * wen sie die gleiche Kontonummer haben
	 *
	 * @param other das Vergleichskonto
	 * @return true, wenn beide Konten die gleiche Nummer haben
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (this.getClass() != other.getClass())
			return false;
		if (this.nummer == ((Konto) other).nummer)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		return 31 + (int) (this.nummer ^ (this.nummer >>> 32));
	}

	@Override
	public int compareTo(Konto other) {
		if (other.getKontonummer() > this.getKontonummer())
			return -1;
		if (other.getKontonummer() < this.getKontonummer())
			return 1;
		return 0;
	}
}
