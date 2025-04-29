package bankprojekt.geld;

/**
 * Enum zur Darstellung verschiedener Währungen.
 */
public enum Waehrung {
    /**
     * Euro-Währung (Referenzkurs 1.0)
     */
    EURO(1.0),
    /**
     * Alte portugiesische Währung
     */
    ESCUDO(109.8269),
    /**
     * São-Tomé-Dobra
     */
    DOBRA(24304.7429),
    /**
     * Westafrikanischer Franc
     */
    FRANC(490.92);

    // Attribut um Umrechnungskurs zu speichern
    private final double kursZuEuro;

    private Waehrung(double kursZuEuro) {
        this.kursZuEuro = kursZuEuro;
    }

    /**
     * Liefert den Umrechnungskurs zu Euro (Anzahl Einheiten dieser Währung pro 1 EUR).
     * @return den Kurs zum Euro
     */
    public double getKursZuEuro() {
        return kursZuEuro;
    }
}
