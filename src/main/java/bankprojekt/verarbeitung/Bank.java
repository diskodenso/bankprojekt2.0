

package bankprojekt.verarbeitung;

import java.util.*;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Sparbuch;
import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.exceptions.KontonummerNichtVorhandenException;

/**
 * Verwaltet verschiedene Kontotypen einer Bank anhand ihrer Kontonummern.
 */
public class Bank {
    private final long bankleitzahl;
    private final Map<Long, Konto> konten = new HashMap<>();
    private long naechsteKontonummer = 1_000_000_0000L;

    /**
     * Erstellt eine neue Bank mit der angegebenen Bankleitzahl.
     *
     * @param bankleitzahl die eindeutige Bankleitzahl
     */
    public Bank(long bankleitzahl) {
        this.bankleitzahl = bankleitzahl;
    }

    /**
     * Liefert die Bankleitzahl dieser Bank zurück.
     *
     * @return die Bankleitzahl
     */
    public long getBankleitzahl() {
        return bankleitzahl;
    }

    /**
     * Erstellt ein neues Girokonto für den angegebenen Kunden.
     * Die Methode vergibt eine neue, noch nicht verwendete Kontonummer.
     *
     * @param inhaber der Kunde, dem das Girokonto gehört
     * @return die vergebene Kontonummer
     */
    public synchronized long girokontoErstellen(Kunde inhaber) {
        long nummer = generiereKontonummer();
        Girokonto konto = new Girokonto(inhaber, nummer, new Geldbetrag(0));
        konten.put(nummer, konto);
        return nummer;
    }

    /**
     * Erstellt ein neues Sparbuch für den angegebenen Kunden.
     * Die Methode vergibt eine neue, noch nicht verwendete Kontonummer.
     *
     * @param inhaber der Kunde, dem das Sparbuch gehört
     * @return die vergebene Kontonummer
     */
    public synchronized long sparbuchErstellen(Kunde inhaber) {
        long nummer = generiereKontonummer();
        Sparbuch konto = new Sparbuch(inhaber, nummer);
        konten.put(nummer, konto);
        return nummer;
    }

    /**
     * Generiert eine neue, eindeutige Kontonummer.
     * Diese Methode ist synchronisiert, um Doppelvergaben zu vermeiden.
     *
     * @return eine neue, noch nicht vorhandene Kontonummer
     */
    private synchronized long generiereKontonummer() {
        while (konten.containsKey(naechsteKontonummer)) {
            naechsteKontonummer++;
        }
        return naechsteKontonummer++;
    }

    /**
     * Liefert eine textuelle Auflistung aller Kontonummern und Kontostände.
     *
     * @return ein String mit jeder Kontonummer und dem zugehörigen Kontostand
     */
    public String getAlleKonten() {
        StringBuilder sb = new StringBuilder();
        for (Konto k : konten.values()) {
            sb.append(k.getKontonummer())
                    .append(": ")
                    .append(k.getKontostand())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Liefert eine Liste aller gültigen Kontonummern in dieser Bank.
     *
     * @return eine List mit allen Kontonummern
     */
    public List<Long> getAlleKontonummern() {
        return new ArrayList<>(konten.keySet());
    }

    /**
     * Hebt einen Betrag vom angegebenen Konto ab.
     *
     * @param von die Kontonummer des Kontos, von dem abgehoben wird
     * @param betrag der abzuhebende Betrag
     * @return true, wenn die Abhebung erfolgreich war, sonst false
     * @throws KontonummerNichtVorhandenException wenn das Konto nicht existiert
     */
    public boolean geldAbheben(long von, Geldbetrag betrag) throws KontonummerNichtVorhandenException, GesperrtException {
        Konto konto = konten.get(von);
        if (konto == null) {
            throw new KontonummerNichtVorhandenException(von);
        }
        if (konto.isGesperrt()) {
            throw new GesperrtException(konto.getKontonummer());
        }
        return konto.abheben(betrag);
    }

    /**
     * Zahlt einen Betrag auf das angegebene Konto ein.
     *
     * @param auf die Kontonummer des Zielkontos
     * @param betrag der einzuzahlende Betrag
     * @throws KontonummerNichtVorhandenException wenn das Konto nicht existiert
     */
    public void geldEinzahlen(long auf, Geldbetrag betrag) {
        Konto konto = konten.get(auf);
        if (konto == null) {
            throw new KontonummerNichtVorhandenException(auf);
        }
        konto.einzahlen(betrag);
    }

    /**
     * Löscht das Konto mit der angegebenen Nummer.
     *
     * @param nummer die zu löschende Kontonummer
     * @return true, wenn ein Konto gelöscht wurde, sonst false
     */
    public boolean kontoLoeschen(long nummer) {
        return konten.remove(nummer) != null;
    }

    /**
     * Liefert den Kontostand des angegebenen Kontos.
     *
     * @param nummer die Kontonummer
     * @return der Kontostand
     * @throws KontonummerNichtVorhandenException wenn das Konto nicht existiert
     */
    public Geldbetrag getKontostand(long nummer) {
        Konto konto = konten.get(nummer);
        if (konto == null) {
            throw new KontonummerNichtVorhandenException(nummer);
        }
        return konto.getKontostand();
    }

    /**
     * Führt eine interne Überweisung zwischen zwei Konten durch.
     *
     * @param sender die Kontonummer des Absenders
     * @param empfänger die Kontonummer des Empfängers
     * @param betrag der zu überweisende Betrag
     * @param verwendungszweck der Verwendungszweck der Überweisung
     * @return true, wenn die Überweisung erfolgreich war, sonst false
     * @throws KontonummerNichtVorhandenException wenn eine der Kontonummern nicht existiert
     */
    public boolean geldUeberweisen(long sender,
                                   long empfänger,
                                   Geldbetrag betrag,
                                   String verwendungszweck) throws GesperrtException {
        Konto src = konten.get(sender);
        Konto dst = konten.get(empfänger);

        if (src == null) {
            throw new KontonummerNichtVorhandenException(sender);
        }
        if (dst == null) {
            throw new KontonummerNichtVorhandenException(empfänger);
        }

        if (src.isGesperrt() || dst.isGesperrt()) {
            return false;
        }

        synchronized (this) {
            if (!src.abheben(betrag)) {
                return false;
            }
            dst.einzahlen(betrag);
            // optional: src.protokolliereVerwendungszweck(verwendungszweck);
            return true;
        }
    }
}
