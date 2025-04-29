package bankprojekt.verarbeitung;

import bankprojekt.geld.Waehrung;

/**
 * Ein Geldbetrag mit Währung
 */
public class Geldbetrag implements Comparable<Geldbetrag>{
	/**
	 * Betrag in der in waehrung angegebenen Währung
	 */
	private double betrag;
	/**
	 * Die Währung
	 */
	private Waehrung waehrung;

	/**
	 * 0 €
	 */
	public static final Geldbetrag NULL_EURO = new Geldbetrag(0);


	/**
	 * erstellt einen Geldbetrag in der Währung Euro
	 * @param betrag Betrag in €
	 * @throws IllegalArgumentException wenn betrag unendlich oder NaN ist
	 * TIPP - geht auch mit Konstruktoren Überladung
	 */
	public Geldbetrag(double betrag)
	{
		setBetrag(betrag);
		setWaehrung(Waehrung.EURO);
	}

	/**
	 * erstellt einen Geldbetrag in der jeweiligen Waehrung
	 * @param betrag wird setBetrag aufgerufen und der Betrag gesetzt
	 * @param waehrung wird setWaehrung aufgerufen und die Waehrung gesetzt
	 */
	public Geldbetrag(double betrag, Waehrung waehrung) {
		setBetrag(betrag);
		setWaehrung(waehrung);
	}

	/**
	 *
	 * @param waehrung wird waehrung gesetzt
	 * @throws NullPointerException wenn waehrung Parameter null ist
	 */
	private void setWaehrung(Waehrung waehrung) throws IllegalArgumentException{
		if (waehrung == null){
			throw new IllegalArgumentException("Waehrung kann nicht null sein");
		}
		this.waehrung = waehrung;
	}

	/**
	 *
	 * @param betrag wird der Betrag gesetzt
	 * @throws IllegalArgumentException
	 */
	private void setBetrag(double betrag) throws IllegalArgumentException {
		if (!Double.isFinite(betrag))
			throw new IllegalArgumentException();
		this.betrag = betrag;
	}

	/**
	 * Betrag von this
	 * @return Betrag in der Währung von this
	 */
	public double getBetrag() {
		return betrag;
	}

	/**
	 * Waehrung von this
	 * @return Waehrung von this
	 */
	public Waehrung getWaehrung() {
		return waehrung;
	}

	/**
	 * rechnet this + summand
	 * @param summand zu addierender Betrag
	 * @return this + summand in der Währung von this
	 * @throws IllegalArgumentException wenn summand null ist
	 */
	public Geldbetrag plus(Geldbetrag summand) throws IllegalArgumentException
	{
		if(summand == null)
			throw new IllegalArgumentException();
		// Summand in die Waehrung von this umrechnen
		double sum = summand.umrechnen(this.waehrung).getBetrag();
		return new Geldbetrag(this.betrag + sum, this.waehrung);
	}

	/**
	 * rechnet this - siúbtrahend
	 * @param subtrahend abzuziehender Betrag
	 * @return this - subtrahend in der Währung von this
	 * @throws IllegalArgumentException wenn subtrahend null ist
	 */
	public Geldbetrag minus(Geldbetrag subtrahend) throws IllegalArgumentException
	{
		if(subtrahend == null)
			throw new IllegalArgumentException();
		double sub = subtrahend.umrechnen(this.waehrung).getBetrag();
		return new Geldbetrag(this.betrag -  sub, this.waehrung);
	}

	/**
	 * multipliziert this mit faktor
	 * @param faktor Faktor der Multiplikation
	 * @return das faktor-Fache von this
	 * @throws IllegalArgumentException wenn faktor nicht finit ist
	 */
	public Geldbetrag mal(double faktor)
	{
		if(!Double.isFinite(faktor))
			throw new IllegalArgumentException();
		return new Geldbetrag(this.betrag * faktor);
	}

	/**
	 * Rechnet den Geldbetrag in eine andere Währung um.
	 *
	 * @param zielWaehrung die Zielwährung, in die umgerechnet werden soll
	 * @return ein neuer Geldbetrag in der Zielwährung
	 * @throws IllegalArgumentException wenn zielWaehrung null ist
	 */
	public Geldbetrag umrechnen(Waehrung zielWaehrung){
		if (zielWaehrung == null) throw new IllegalArgumentException("Zielwaehrung null!");
		if (this.waehrung == zielWaehrung) return this;
		double betragInEuro = this.betrag / this.waehrung.getKursZuEuro();
		double betragInZielWaehrung = betragInEuro * zielWaehrung.getKursZuEuro();

		// Verwende Doublerounder und round() 2 nachkommastellen
		double gerundeterBetrag = Doubleround.round(betragInZielWaehrung, 2);

		return new Geldbetrag(gerundeterBetrag, zielWaehrung);

	}

	@Override
	public int compareTo(Geldbetrag o) {
		return Double.compare(this.betrag, o.betrag);
	}

	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof Geldbetrag)) return false;
		if(o == this) return true;
		return this.compareTo((Geldbetrag) o) == 0;
	}

	/**
	 * prüft, ob this einen negativen Betrag darstellt
	 * @return true, wenn this negativ ist
	 */
	public boolean isNegativ()
	{
		return this.betrag < 0;
	}

	@Override
	public String toString()
	{
		return String.format("%,.2f %s", this.betrag, this.waehrung.name());

	}
}
