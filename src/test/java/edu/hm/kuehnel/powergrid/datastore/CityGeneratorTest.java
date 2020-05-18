/*
 * Copyright (c) 2020 Stefan Kuehnel - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Stefan Kuehnel <stefan.kuehnel@hm.edu>, May 2020
 *
 * DISCLAIMER. THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OR CONDITION,
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
 * @version last-modified 2020-05-16
 */
public class CityGeneratorTest {
    /** Verhindert unendliche Schleifen. */
    @Rule
    public final Timeout globalTimeout = Timeout.seconds(1); // Maximale Anzahl an Sekunden pro Testfall.

    /** FQCN der konkreten Factory Klasse */
    private final String factoryFQCN;

    /** Factory. */
    private final Factory factory;

    /** Initialisiert die Factory. */
    public CityGeneratorTest() throws IOException {
        factoryFQCN = "edu.hm.kuehnel.powergrid.datastore.FactoryProvider";
        factory = Factory.newFactory(factoryFQCN);
    }

    /** Instanziiert eine neue Stadt mit fallbezogenen oder falschen Parametern. */
    public City getSUT(String name, int region) {
        return factory.newCity(name, region);
    }

    /** Instanziiert eine neue Stadt mit korrekten Parametern. */
    public City getSUT() {
        final String cityName = "Entenhausen";
        final int region = 1;
        return factory.newCity(cityName, region);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewCityRequireArgumentNonNullName() {
        final String name = null;
        final int region = 1;
        getSUT(name, region);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewCityRequireArgumentNonEmptyName() {
        final String name = "";
        final int region = 1;
        getSUT(name, region);
    }

    @Test
    public void testGetNameVerifyFunctionalitySuppliedAndReturnedCityNameAreEqual() {
        final City sut = getSUT();
        final String want = "Entenhausen";
        final String have = sut.getName();
        assertEquals(want, have);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewCityRequireArgumentRegionNumberGreaterOrEqualOne() {
        final String name = "Entenhausen";
        final int region = -1;
        getSUT(name, region);
    }

    @Test
    public void testGetRegionVerifyFunctionalitySuppliedAndReturnedRegionNumbersAreEqual() {
        final City sut = getSUT();
        final int want = 1;
        final int have = sut.getRegion();
        assertEquals(want, have);
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

    @Test (expected = IllegalArgumentException.class)
    public void testConnectRequireArgumentNonNullCity() {
        final City sut = getSUT();

        final City city = null;
        final int costs = 1;

        sut.connect(city, costs);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConnectRequireArgumentNonNegativeCosts() {
        final City sut = getSUT();

        final City city = getSUT("Entenburg", 2);
        final int costs = -1;

        sut.connect(city, costs);
    }

    @Test
    public void testConnectVerifyFunctionalityCheckIfCityIsIndeedConnected() {
        final City sut = getSUT();

        // Die zu verbindende Stadt wird erstellt.
        final City city = getSUT("Entenburg", 2);
        final int costs = 1;

        // Die Staedte werden verbunden.
        sut.connect(city, costs);

        // Prueft, ob die Stadt tatsächlich verbunden wurde.
        final boolean cityHasBeenConnected = sut.getConnections().containsKey(city);

        assertTrue(cityHasBeenConnected);
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

    @Test
    public void testGetConnectionsVerifyFunctionalityCityIsProperlyConnected() {
        final City sut = getSUT();

        // Die zu verbindende Stadt wird erstellt.
        final City city = getSUT("Entenburg", 2);
        final int costs = 1;

        // Die Staedte werden verbunden.
        sut.connect(city, costs);

        // Pruefung, ob Stadt tatsaechlich verbunden wurde.
        final boolean cityIsConnected = sut.getConnections().containsKey(city);

        assertTrue(cityIsConnected);
    }

    @Test (expected = IllegalStateException.class)
    public void testCloseDenyActionMultipleCityCloseRequests() {
        final City sut = getSUT();

        // Die Stadt wird zum ersten Mal geschlossen.
        sut.close();

        // Die Stadt wird zum zweiten Mal geschlossen.
        sut.close(); // Wird verweigert.
    }

    @Test
    public void testCompareToVerifyFunctionalitySuppliedAndReturnedNaturalOrdersAreEqual() {
        final City sut = getSUT();

        final City city = getSUT("Entenburg", 1);

        final int want = 6;
        final int have = sut.compareTo(city);

        assertEquals(want, have);
    }
}