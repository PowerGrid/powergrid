/*
 * Copyright (c) 2020 Stefan Kuehnel - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Stefan Kuehnel <stefan.kuehnel@hm.edu>, May 2020
 *
 * DISCLAIMER. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OR CONDITION,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. THE AUTHOR HEREBY DISCLAIMS
 * ALL LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE.
 */

package edu.hm.kuehnel.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Die Testklasse fuer den PlayerGenerator.
 * @author Stefan Kühnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-23
 */
public class PlayerGeneratorTest {
    /** Verhindert unendliche Schleifen. */
    @Rule
    public final Timeout globalTimeout = Timeout.seconds(1); // Maximale Anzahl an Sekunden pro Testfall.

    /** Factory. */
    private final Factory factory;

    /** Initialisiert die Factory. */
    public PlayerGeneratorTest() throws IOException {
        factory = Factory.newFactory();
    }

    /** Instanziiert einen neuen Spieler mit korrekten Parametern.
     * @return Player.
     */
    public Player getSUT() {
        final String color = "Blau";
        final String secret = "Psst, nicht verraten.";
        return factory.newPlayer(secret, color);
    }

    /** Instanziiert einen neuen Spieler mit fallbezogenen oder falschen Parametern.
     * @param color  Die Farbe des Spielers.
     * @param secret Das Geheimnis des Spielers.
     *
     * @return Player.
     */
    public Player getSUT(String color, String secret) {
        return factory.newPlayer(secret, color);
    }

    @Test
    public void testNewPlayerIllegalParameters() {
        final String correctColor = "Blau";
        final String correctSecret = "Psst, nicht verraten.";

        // Das Geheimnis darf nicht null sein.
        assertThrows(IllegalArgumentException.class, () -> getSUT(correctColor, null));

        // Das Geheimnis darf nicht leer sein.
        assertThrows(IllegalArgumentException.class, () -> getSUT(correctColor, ""));

        // Die Farbe darf nicht null sein.
        assertThrows(IllegalArgumentException.class, () -> getSUT(null, correctSecret));

        // Die Farbe darf nicht leer sein.
        assertThrows(IllegalArgumentException.class, () -> getSUT("", correctSecret));
    }

    @Test
    public void testGetColor() {
        Player sut = getSUT();

        final String want = "Blau";
        final String have = sut.getColor();

        assertEquals("Uebergebene und erhaltene Farbe stimmen ueberein.", want, have);
    }

    @Test
    public void testGetCities() {
        Player sut = getSUT();
        City city = factory.newCity("Entenhausen", 1);

        sut.getCities().add(city);

        assertTrue("Die Stadt wurde an das Netz des Spielers angeschlossen.", sut.getCities().contains(city));
    }

    @Test (expected = NullPointerException.class)
    public void testGetCitiesDenyActionAddNullValue() {
        Player sut = getSUT();
        sut.getCities().add(null);
    }

    @Test
    public void testGetPlants() {
        Player sut = getSUT();
        Plant plant = factory.newPlant(1, Plant.Type.Coal, 1, 1);

        sut.getPlants().add(plant);

        assertTrue("Spieler besitzt das Kraftwerk.", sut.getPlants().contains(plant));
    }

    @Test (expected = NullPointerException.class)
    public void testGetPlantsDenyActionAddNullValue() {
        Player sut = getSUT();
        sut.getPlants().add(null);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testGetResourcesRequireReturnDummyException() {
        getSUT().getResources();
    }

    @Test
    public void testSetElectroIllegalParameters() {
        // Die Anzahl Electro darf nicht negativ sein.
        assertThrows(IllegalArgumentException.class, () -> getSUT().setElectro(-1));
    }

    @Test
    public void testGetElectro() {
        Player sut = getSUT();
        sut.setElectro(1);

        final int want = 1;
        final int have = sut.getElectro();

        assertEquals("Uebergebene und erhaltene Electro stimmen ueberein.", want, have);
    }

    @Test
    public void testHasPassedRequireReturnFalseIfNotOperated() {
        assertFalse("Spieler war noch nicht an der Reihe.", getSUT().hasPassed());
    }

    @Test
    public void testHasPassedRequireReturnTrueIfOperated() {
        Player sut = getSUT();

        sut.setPassed(true);

        assertTrue("Spieler war schon an der Reihe.", sut.hasPassed());
    }

    @Test
    public void testGetSecretRequireReturnUnmaskedSecretOnFirstDisclosure() {
        Player sut = getSUT();

        final String want = "Psst, nicht verraten.";

        // Bei erster Abfrage wird das unmaskierte Geheimnis zurückgegeben.
        final String have = sut.getSecret();

        assertEquals("Das Geheimnis wurde noch nicht unkenntlich gemacht.", want, have);
    }

    @Test
    public void testGetSecretRequireReturnNullAsMaskedSecretOnSecondDisclosureRequest() {
        Player sut = getSUT();

        // Hierbei wird das korrekte unmaskierte Geheimnis ausgegeben.
        sut.getSecret();

        // Das nun ausgegebene Geheimnis ist maskiert und deshalb null.
        final String secondDisclosure = sut.getSecret();

        assertNull("Das Geheimnis wurde unkenntlich gemacht.", secondDisclosure);
    }

    @Test
    public void testHasSecretRequireReturnTrueIfSecretIsCorrect() {
        Player sut = getSUT();

        final String correctSecret = "Psst, nicht verraten.";

        assertTrue("Das uebergebene Geheimnis ist korrekt.", sut.hasSecret(correctSecret));
    }

    @Test
    public void testHasSecretRequireReturnFalseIfSecretIsNotCorrect() {
        Player sut = getSUT();

        final String notCorrectSecret = "Kann man verraten, ist sowieso falsch.";

        assertFalse("Das uebergebene Geheimnis ist nicht korrekt.", sut.hasSecret(notCorrectSecret));
    }
}