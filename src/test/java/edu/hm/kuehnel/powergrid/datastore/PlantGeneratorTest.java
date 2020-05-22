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

import edu.hm.cs.rs.powergrid.datastore.Factory;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/** Die Testklasse fuer den PlantGenerator.
 * @author Stefan Kühnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-23
 */
public class PlantGeneratorTest {
    /** Verhindert unendliche Schleifen. */
    @Rule
    public final Timeout globalTimeout = Timeout.seconds(1); // Maximale Anzahl an Sekunden pro Testfall.

    /** Factory. */
    private final Factory factory;

    /** Initialisiert die Factory. */
    public PlantGeneratorTest() throws IOException {
        factory = Factory.newFactory();
    }

    /** Instanziiert ein neues Kraftwerk mit korrekten Parametern.
     * @return Plant.
     */
    public Plant getSUT() {
        final int number = 1;
        final Plant.Type type = Plant.Type.Coal;
        final int resources = 1;
        final int cities = 1;

        return factory.newPlant(number, type, resources, cities);
    }

    /** Instanziiert ein neues Kraftwerk mit fallbezogenen oder falschen Parametern.
     *
     * @param number      Eindeutige Nummer.
     * @param type        Rohstoffe, die dieses Kraftwerk zur Stromproduktion benoetigt.
     * @param resources   Anzahl notwendiger Rohstoffe zur Stromproduktion. Nicht negativ.
     * @param cities      Anzahl mit Strom versorgbarer Staedte.
     *
     * @return Plant.
     */
    public Plant getSUT(int number, Plant.Type type, int resources, int cities) {
        return factory.newPlant(number, type, resources, cities);
    }

    @Test
    public void testNewPlantIllegalParameters() {
        final int correctNumber = 1;
        final Plant.Type correctType = Plant.Type.Coal;
        final int correctResources = 1;
        final int correctCities = 1;

        // Eindeutige Nummer des Kraftwerks darf nicht negativ sein.
        assertThrows(IllegalArgumentException.class, () -> getSUT(-1, correctType, correctResources, correctCities));

        // Anzahl notwendiger Rohstoffe darf nicht negativ sein.
        assertThrows(IllegalArgumentException.class, () -> getSUT(correctNumber, correctType, -1, correctCities));

        // Anzahl mit Strom versorgbarer Staedte darf nicht negativ sein.
        assertThrows(IllegalArgumentException.class, () -> getSUT(correctNumber, correctType, correctResources, -1));
    }

    @Test
    public void testGetNumber() {
        final int want = 1;
        final int have = getSUT().getNumber();

        assertEquals("Uebergebene und erhaltene eindeutige Nummer stimmen ueberein.", want, have);
    }

    @Test
    public void testGetCities() {
        final int want = 1;
        final int have = getSUT().getCities();

        assertEquals("Uebergebene und erhaltene versorgbare Staedte stimmen ueberein.", want, have);
    }

    @Test
    public void testGetNumberOfResources() {
        final int want = 1;
        final int have = getSUT().getNumberOfResources();

        assertEquals("Uebergebene und erhaltene Anzahl Ressourcen stimmen ueberein.", want, have);
    }

    @Test
    public void testGetType() {
        final Plant.Type want = Plant.Type.Coal;
        final Plant.Type have = getSUT().getType();

        assertEquals("Uebergebener und erhaltener Ressourcentyp stimmen ueberein.", want, have);
    }

    @Test
    public void testHasOperatedRequireReturnFalseIfNotOperated() {
        assertFalse("Kraftwerk hat noch kein Strom erzeugt.", getSUT().hasOperated());
    }

    @Test
    public void testHasOperatedRequireReturnTrueIfOperated() {
        Plant sut = getSUT();

        sut.setOperated(true);

        assertTrue("Kraftwerk hat schon Strom erzeugt.", sut.hasOperated());
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testGetResourcesRequireReturnDummyException() {
        getSUT().getResources();
    }
}