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

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/** Die Testklasse fuer den BoardGenerator.
 * @author Stefan Kühnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-16
 */
public class BoardGeneratorTest {
    /** Verhindert unendliche Schleifen. */
    @Rule
    public final Timeout globalTimeout = Timeout.seconds(1); // Maximale Anzahl an Sekunden pro Testfall.

    /** Factory. */
    private final Factory factory;

    /** Edition. */
    private final Edition edition;

    /** Initialisiert die Factory. */
    public BoardGeneratorTest() throws IOException {
        final String factoryFQCN = "edu.hm.kuehnel.powergrid.datastore.FactoryProvider";
        factory = Factory.newFactory(factoryFQCN);
        edition = new EditionGermany();
    }

    /** Instanziiert einen neuen Spielplan mit fallbezogenen oder falschen Parametern.
     * @return Spielplan.
     */
    public Board getSUT() {
        return factory.newBoard(edition);
    }

    @Test
    public void testCloseRegionsVerifyFunctionalityNumberOfRegionalCitiesOnBoardAndEditionAreEqual() {
        final Board sut = getSUT();

        // Definition der Regionen deren Städte auf dem Spielbrett verbleiben sollen.
        final int remaining = 1;

        // Zählen aller Städte der Edition, die sich in der Region 1 befinden.
        final long numberOfCitiesInRegionInEdition = edition.getCitySpecifications().stream()
                .filter(citySpec -> {
                    final int cityRegion = Integer.parseInt(citySpec.split("\\s+")[1]);
                    return cityRegion <= remaining;
                }).count();

        // Schließen aller Städte auf dem Board, die nicht in Region 1 liegen.
        sut.closeRegions(remaining);

        final long want = numberOfCitiesInRegionInEdition;
        final long have = sut.getCities().size();

        assertEquals(want, have);

    }

    @Test (expected = IllegalArgumentException.class)
    public void testCloseRegionsRequireArgumentNonNegativeRemaining() {
        final Board sut = getSUT();
        sut.closeRegions(-1);
    }

    @Test (expected = IllegalStateException.class)
    public void testCloseRegionsDenyActionCloseRegionsWhenBoardIsClosed() {
        final Board sut = getSUT();
        sut.close();
        sut.closeRegions(1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFindCityRequireArgumentNonEmptyName() {
        final Board sut = getSUT();
        sut.findCity("");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testFindCityRequireArgumentNonNullName() {
        final Board sut = getSUT();
        sut.findCity(null);
    }

    @Test
    public void testFindCityRequireReturnNullIfCityIsNotFoundOnBoard() {
        final Board sut = getSUT();

        assertNull(sut.findCity("Atlantis"));
    }

    @Test
    public void testFindCityVerifyFunctionalityRandomCityFromEditionIsFoundOnBoard() {
        final Board sut = getSUT();

        final String[] editionCityProperties = edition.getCitySpecifications().get(0).split("\\s+");
        final String cityNameFromEdition = editionCityProperties[0];

        final String want = cityNameFromEdition;
        final String have = sut.findCity(cityNameFromEdition).getName();

        assertEquals(want, have);
    }

    @Test
    public void testGetCitiesVerifyFunctionalityCompareNumberOfCitiesWithEdition() {
        final Board sut = getSUT();

        final int want = edition.getCitySpecifications().size();
        final int have = sut.getCities().size();

        assertEquals(want, have);
    }

    @Test (expected = UnsupportedOperationException.class)
    public void testGetCitiesRequireReturnImmutableSetWhenBoardIsClosed() {
        final Board sut = getSUT();
        final City city = factory.newCity("Entenhausen", 1);

        sut.close();

        // Diese Liste ist nun immutable und nimmt keine neuen Städte mehr entgegen.
        sut.getCities().add(city);
    }

    @Test (expected = IllegalStateException.class)
    public void testCloseDenyActionMultipleBoardCloseRequests() {
        final Board sut = getSUT();

        // Der Spielplan wird das erste Mal geschlossen.
        sut.close();

        // Der Spielplan wird das zweite Mal geschlossen.
        sut.close(); // Wird verweigert.
    }

    @Test (expected = IllegalStateException.class)
    public void testCloseDenyActionClosedCityOnBoardIsClosedAgain() {
        final Board sut = getSUT();

        // Der Spielplan wird das erste Mal geschlossen.
        sut.close();

        // Fordere eine zufällige Stadt an.
        final City alreadyClosedCity = sut.getCities().iterator().next();

        // Eine weitere Schließung wird fehlschlagen.
        alreadyClosedCity.close();
    }
}