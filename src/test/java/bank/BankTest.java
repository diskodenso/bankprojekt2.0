package bank;

import bankprojekt.verarbeitung.*;
import bankprojekt.exceptions.KontonummerNichtVorhandenException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

import org.mockito.InOrder;
import org.mockito.Mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.BaseStubbing;

public class BankTest {

    private Bank bank;
    private Konto src;
    private Konto dst;

    @BeforeEach
    void setUp() {
        // Mock Objekte müssen erzeugt werden
        src = Mockito.mock(Konto.class);
        dst = Mockito.mock(Konto.class);

        bank = new Bank(12345678);

    }

    // region getKontostand Methode testen
    // Happy Path - Vorhandenes Konto-Mock liefert korrekten Kontostand
    @Test
    public void getKontostandKorrektTest(){
        // setup
        long kontonummer = bank.mockEinfuegen(src);
        Geldbetrag erwarteterBetrag = new Geldbetrag(1000);
        // Stub
        Mockito.when(src.getKontostand()).thenReturn(erwarteterBetrag);

        // execute
        Geldbetrag tatsächlicherBetrag = bank.getKontostand(kontonummer);
        // Verify
        assertEquals(erwarteterBetrag, tatsächlicherBetrag, "Kontostand muss 1000 sein und somit vom Mock kommen");
        Mockito.verify(src).getKontostand();
        // Stelle sicher das NUR die Methode auf den Mock aufgerufen wird die
        // man auch wirklich aufgerufen hat
        Mockito.verifyNoMoreInteractions(src);
    }


    // Unbekannte Kontonummer wirft KontonummerNichtVorhandenException
    @Test
    public void getKontostandWirftException(){

        assertThrows(
                KontonummerNichtVorhandenException.class,
                () -> bank.getKontostand(66666666),
                "Kontonummer nicht vorhanden"
        );
        Mockito.verifyNoInteractions(src, dst);
    }

    // Mehrere Konten aber es wird nur von dem richtigen abgebucht
    @Test
    public void getKontostandVomRichtigenKonto(){
        long kontonummerA = bank.mockEinfuegen(src);
        long kontonummerB = bank.mockEinfuegen(dst);

        Geldbetrag erwarteterBetrag = new Geldbetrag(666);
        Mockito.when(src.getKontostand()).thenReturn(erwarteterBetrag);

        // Exercise
        Geldbetrag tatsächlicherGeldbetrag = bank.getKontostand(kontonummerA);

        // Verify
        assertEquals(erwarteterBetrag, tatsächlicherGeldbetrag, "getKontostand soll nur auf Konto src getriggert werden");
        Mockito.verify(src).getKontostand();
        Mockito.verifyNoMoreInteractions(dst);

    }

    // Negativer Betrag
    @Test
    public void getKontostandMinusBetrag(){
        long kontonummer = bank.mockEinfuegen(src);
        Geldbetrag erwarteterBetrag = new Geldbetrag(222);
        Mockito.when(src.getKontostand()).thenReturn(erwarteterBetrag);

        // Exercise
        Geldbetrag tatsächlicherBetrag = bank.getKontostand(kontonummer);

        // Verify
        assertEquals(erwarteterBetrag, tatsächlicherBetrag, "Konto mit negativem Betrag gibt negativen Betrag zuück");
        Mockito.verify(src).getKontostand();

    }

    //endregion

    //region geldUeberweisen Methode testen

    // Happy Path
    @Test
    public void geldUeberweisenKorrekt() throws GesperrtException {
        //setup
        long kontoA = bank.mockEinfuegen(src);
        long kontoB = bank.mockEinfuegen(dst);
        Geldbetrag betrag = new Geldbetrag(1234);
        Mockito.when(src.isGesperrt()).thenReturn(false);
        Mockito.when(dst.isGesperrt()).thenReturn(false);
        Mockito.when(src.abheben(betrag)).thenReturn(true);

        // exercise
        boolean result = bank.geldUeberweisen(kontoA, kontoB, betrag, "Ebay");

        // verify
        assertTrue(result, "Erfolgreiche Ueberweisung");
        // check methodenrufe Reihenfolge
        InOrder ord = Mockito.inOrder(src, dst);
        ord.verify(src).abheben(betrag);
        ord.verify(dst).einzahlen(betrag);

    }

    // Konto überzogen - zu wenig Geld leider du armer Schlucker
    @Test
    public void geldUeberweisenUnzureichendeDeckung() throws GesperrtException {
        // setup
        long kontoA = bank.mockEinfuegen(src);
        long kontoB = bank.mockEinfuegen(dst);
        Geldbetrag betrag = new Geldbetrag(200);
        Mockito.when(src.isGesperrt()).thenReturn(false);
        Mockito.when(dst.isGesperrt()).thenReturn(false);
        Mockito.when(src.abheben(betrag)).thenReturn(false);

        // exercise
        boolean result = bank.geldUeberweisen(kontoA, kontoB, betrag, "Rechnung");

        // verify
        assertFalse(result, "Bei unzureichendem Guthaben muss false zurückkommen");
        Mockito.verify(src).abheben(betrag);
        Mockito.verify(dst, never()).einzahlen(any());
    }

    // Sender Konto gesperrt
    @Test
    public void geldUeberweisenSenderGesperrt() throws GesperrtException{
        // setup
        long kontoA = bank.mockEinfuegen(src);
        long kontoB = bank.mockEinfuegen(dst);
        Geldbetrag betrag = new Geldbetrag(50);
        Mockito.when(src.isGesperrt()).thenReturn(true);
        Mockito.when(dst.isGesperrt()).thenReturn(false);

        // exercise
        boolean result = bank.geldUeberweisen(kontoA, kontoB, betrag, "Rechnung");

        // verify
        assertFalse(result, "Gesperrtes Sender-Konto darf keine Überweisung ausführen");
        Mockito.verify(src, never()).abheben(any());
        Mockito.verify(dst, never()).einzahlen(any());
    }

    // Empfänger Konto gesperrt
    @Test
    public void geldUeberweisenEmpfängerGesperrt() throws GesperrtException {
        // setup
        long kontoA = bank.mockEinfuegen(src);
        long kontoB = bank.mockEinfuegen(dst);
        Geldbetrag betrag = new Geldbetrag(50);
        Mockito.when(src.isGesperrt()).thenReturn(false);
        Mockito.when(dst.isGesperrt()).thenReturn(true);
        // exercise
        boolean result = bank.geldUeberweisen(kontoA, kontoB, betrag, "Rechnung");

        // verify
        assertFalse(result, "Gesperrtes Empfängerkonto darf keine Überweisung empfangen");
        Mockito.verify(src, never()).abheben(any());
        Mockito.verify(dst, never()).einzahlen(any());

    }

        // Unbekannter Sender
        @Test
        public void geldUeberweisenSenderUnbekannt(){
        // setup

        long kontoB = bank.mockEinfuegen(dst);


        // exercise & verify
        assertThrows(
                KontonummerNichtVorhandenException.class,
                () -> bank.geldUeberweisen(9999L, kontoB, new Geldbetrag(10), "X"),
                "Unbekannter Sender muss KontonummerNichtVorhandenException werfen"
        );
        Mockito.verifyNoInteractions(src, dst);
    }

    // Empf#nger Unbekannt
    @Test
    public void geldUeberweisenEmpfaengerUnbekannt(){
        long kontoA = bank.mockEinfuegen(src);

        // Exercise & Verify
        assertThrows(
                KontonummerNichtVorhandenException.class,
                () -> bank.geldUeberweisen(kontoA, 8888L, new Geldbetrag(10), "Y"),
                "Unbekannter Empfänger muss KontonummerNichtVorhandenException werfen"
        );
        Mockito.verifyNoInteractions(src, dst);
    }
    //endregion
}
