package bank;

import bankprojekt.verarbeitung.*;
import bankprojekt.exceptions.KontonummerNichtVorhandenException;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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



    //endregion

}
