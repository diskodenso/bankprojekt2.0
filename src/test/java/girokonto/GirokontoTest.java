package girokonto;

import bankprojekt.verarbeitung.Geldbetrag;
import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Methode {@link Girokonto#abheben(Geldbetrag)}.
 */
class GirokontoTest {

    private Girokonto girokonto;

    /**
     * Initialisiert ein neues Girokonto und zahlt 1000€ ein.
     */
    @BeforeEach
    void setUp() {
        girokonto = new Girokonto();
        girokonto.einzahlen(new Geldbetrag(1000)); // Startguthaben
    }

    /**
     * Testet, ob ein gültiger Betrag erfolgreich abgehoben werden kann.
     */
    @Test
    void testAbhebenErfolgreich() throws GesperrtException {
        Assertions.assertTrue(girokonto.abheben(new Geldbetrag(200)));
        Assertions.assertEquals(new Geldbetrag(800), girokonto.getKontostand());
    }

    /**
     * Testet, ob eine Abhebung bis zum Dispolimit möglich ist.
     */
    @Test
    void testAbhebenBisZumDispoMoeglich() throws GesperrtException {
        Assertions.assertTrue(girokonto.abheben(new Geldbetrag(1400))); // 1000 + 500 Dispo = 1500 -> 1400 geht

        Assertions.assertEquals(new Geldbetrag(-400), girokonto.getKontostand());
    }

    /**
     * Testet, dass eine Abhebung, die den Dispo überschreiten würde, abgelehnt wird.
     */
    @Test
    void testAbhebenMehrAlsDispoNichtMoeglich() throws GesperrtException {
        Assertions.assertFalse(girokonto.abheben(new Geldbetrag(1600))); // 1600 > 1000 + 500
        Assertions.assertEquals(new Geldbetrag(1000), girokonto.getKontostand());
    }

    /**
     * Testet, ob bei einem negativen Abhebebetrag eine
     * {@link IllegalArgumentException} geworfen wird.
     */
    @Test
    void testAbhebenMitNegativemBetragWirftException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> girokonto.abheben(new Geldbetrag(-50)));
    }

    /**
     * Testet, ob bei einem gesperrten Konto eine
     * {@link GesperrtException} geworfen wird.
     */
    @Test
    void testAbhebenWennGesperrtWirftException() {
        girokonto.sperren();
        Assertions.assertThrows(GesperrtException.class, () -> girokonto.abheben(new Geldbetrag(50)));
    }

    /**
     * Testet, ob bei Übergabe von {@code null} eine
     * {@link IllegalArgumentException} geworfen wird.
     */
    @Test
    void testAbhebenMitNullWirftException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> girokonto.abheben(null));
    }
}
