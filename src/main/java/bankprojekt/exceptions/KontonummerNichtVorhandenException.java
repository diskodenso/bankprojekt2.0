package bankprojekt.exceptions;

/**
 * Exception, die signalisiert, dass eine Kontonummer nicht in der Bank existiert.
 */
public class KontonummerNichtVorhandenException extends RuntimeException {
    /**
     * Erzeugt eine neue Ausnahme für eine nicht vorhandene Kontonummer.
     *
     * @param nummer die Kontonummer, die nicht gefunden wurde
     */
    public KontonummerNichtVorhandenException(long nummer) {
        super("Konto mit Nummer " + nummer + " nicht vorhanden");
    }
}