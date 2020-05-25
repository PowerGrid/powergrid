/*
 * Copyright (c) 2020 Stefan Kuehnel - All Rights Reserved
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Die Testklasse fuer den CityGenerator.
 * @author Stefan Kuehnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-23
 */
public class CityGeneratorTest {
    /** Verhindert unendliche Schleifen. */
    @Rule
    public final Timeout globalTimeout = Timeout.seconds(1); // Maximale Anzahl an Sekunden pro Testfall.

    /** Factory. */
    private final Factory factory;

    /** Initialisiert die Factory. */
    public CityGeneratorTest() throws IOException {
        factory = Factory.newFactory();
    }

    /** Instanziiert eine neue Stadt mit fallbezogenen oder falschen Parametern.
     * @param name Name der Stadt. Nicht leer, nicht null.
     * @param region Gebiet, in dem die Stadt liegt. Wenigstens 1.
     *
     * @return City.
     */
    public City getSUT(String name, int region) {
        return factory.newCity(name, region);
    }

    /** Instanziiert eine neue Stadt mit korrekten Parametern.
     * @return City.
     */
    public City getSUT() {
        final String cityName = "Entenhausen";
        final int region = 1;
        return factory.newCity(cityName, region);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewCityRequireArgumentNonNullName() {
        final int correctCityRegion = 1;
        getSUT(null, correctCityRegion);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewCityRequireArgumentNonEmptyName() {
        final int correctCityRegion = 1;
        getSUT("", correctCityRegion);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewCityRequireArgumentNonZeroRegion() {
        final String correctCityName = "Entenburg";
        getSUT(correctCityName, 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewCityRequireArgumentNonNegativeRegion() {
        final String correctCityName = "Entenburg";
        getSUT(correctCityName, -1);
    }

    @Test
    public void testGetName() {
        final City sut = getSUT();
        final String want = "Entenhausen";
        final String have = sut.getName();
        assertEquals("Uebergebener Name stimmt mit erhaltenem Namen ueberein.", want, have);
    }

    @Test
    public void testGetRegion() {
        final City sut = getSUT();
        final int want = 1;
        final int have = sut.getRegion();
        assertEquals("Uebergebene Region stimmt mit erhaltener Region ueberein.", want, have);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConnectRequireArgumentNonNullCity() {
        final City sut = getSUT();

        final int correctCost = 1;

        // Die zu verbindende Stadt darf nicht null sein.
        sut.connect(null, correctCost);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConnectRequireArgumentNonNegativeCosts() {
        final City sut = getSUT();

        final City correctCity = getSUT("Entenburg", 2);

        // Die Verbindungskosten duerfen nicht negativ sein.
        sut.connect(correctCity, -1);
    }

    @Test (expected = IllegalStateException.class)
    public void testConnectRequireArgumentNonClosedCity() {
        final City sut = getSUT();

        // Initialisieren der zu verbindenden Stadt.
        final City city = getSUT("Entenburg", 2);
        final int costs = 1;

        // Die Stadt wird geschlossen.
        sut.close();

        // Die Verbindung kann aufgrund der geschlossenen Stadt jetzt nicht mehr gelegt werden.
        sut.connect(city, costs);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConnectRequireArgumentNotThisCity() {
        final City sut = getSUT();
        sut.connect(sut, 1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConnectRequireArgumentNonIdenticalCity() {
        final City sut = getSUT();

        final City city = getSUT("Entenhausen", 1);
        final int costs = 1;

        sut.connect(city, costs);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConnectRequireArgumentNonConnectedCity() {
        final City sut = getSUT();

        final City city = getSUT("Entenhausen", 1);
        final int costs = 1;

        // Verbindet die Stadt ein erstes Mal.
        sut.connect(city, costs); // Hier funktioniert es.

        // Verbindet die Stadt ein zweites Mal.
        sut.connect(city, costs); // Hier funktioniert es nicht mehr.
    }

    @Test (expected = IllegalStateException.class)
    public void testConnectDenyActionCityIsAlreadyClosed() {
        final City sut = getSUT();

        // Definition der zu verbindenden Stadt
        final City city = getSUT("Entenburg", 2);
        final int costs = 1;

        // Die Stadt wird verbunden.
        sut.connect(city, costs);

        // Die Stadt wird geschlossen.
        sut.close();

        // Neuer Verbindungsversuch scheiter jetzt.
        sut.connect(city, costs);
    }

    @Test
    public void testConnect() {
        final City sut = getSUT();

        // Die zu verbindende Stadt wird erstellt.
        final City city = getSUT("Entenburg", 2);

        // Die Staedte werden verbunden.
        sut.connect(city, 1);

        // Prueft, ob die Stadt tatsächlich verbunden wurde.
        assertTrue("Entenburg wurde mit Entenhausen verbunden.", sut.getConnections().containsKey(city));
    }

    @Test (expected = IllegalStateException.class)
    public void testGetConnectionsRequireReturnNonEmptyMap() {
        final City sut = getSUT();

        // Das Auslesen der leeren Verbindungsliste wird einen Fehler verursachen.
        sut.getConnections();
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testGetConnectionsRequireReturnImmutableMapWhenCityIsClosed() {
        final City sut = getSUT();

        // Die zu verbindende Stadt wird erstellt.
        final City city = getSUT("Entenburg", 2);
        final int costs = 1;

        // Die Staedte werden verbunden.
        sut.connect(city, costs);

        // Die Stadt wird geschlossen.
        sut.close();

        // Pruefung der Map auf Unveraenderlichkeit.
        sut.getConnections().put(city, costs);
    }

    @Test (expected = IllegalStateException.class)
    public void testCloseDenyActionEmptyCityConnections() {
        final City sut = getSUT();

        // Die Stadt wird geschlossen ohne dass Verbindungen zu anderen Städten existieren.
        sut.close();
    }

    @Test (expected = IllegalStateException.class)
    public void testCloseDenyActionTwoSubsequentCloseRequests() {
        final City sut = getSUT();

        // Erstellt eine Beispielstadt.
        final City connectCity = factory.newCity("Entenburg", 1);

        // Verbindet die Städte.
        sut.connect(connectCity, 1);

        // Schließt die Stadt.
        sut.close();

        // Schließt die Stadt erneut.
        sut.close(); // Hier wird ein Fehler auftreten.
    }

    @Test
    public void testCompareTo() {
        final City sut = getSUT();

        final City city = getSUT("Entenburg", 1);

        final int want = 6;
        final int have = sut.compareTo(city);

        assertEquals("Die natuerliche Ordnung von Entenburg ist korrekt.", want, have);
    }
}
