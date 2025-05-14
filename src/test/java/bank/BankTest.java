package bank;

import bankprojekt.verarbeitung.*;
import static org.junit.jupiter.api.Assertions.*;
// import Mockito
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// Import custom exception
import bankprojekt.exceptions.KontonummerNichtVorhandenException;

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

    @Test
    public void getKontostandTest(){
        // setup
        long kontonummer = bank.mockEinfuegen(src);
        Geldbetrag betrag = new Geldbetrag(1000);
        Mockito.when(src.getKontostand()).thenReturn(betrag);

        // execute
        Geldbetrag prüfeBetrag = bank.getKontostand(kontonummer);
        // Verify
        assertEquals(betrag, prüfeBetrag, "Kontostand muss 1000 sein und somit vom Mock kommen");
        Mockito.verify(src).getKontostand();
        Mockito.verifyNoMoreInteractions(src);


    }
}
