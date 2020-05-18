package edu.hm.kuehnel.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.EditionGermany;
import edu.hm.cs.rs.powergrid.datastore.Auction;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.PlantMarket;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.ResourceMarket;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Die Testklasse fuer den FactoryProvider.
 * @author Stefan Kühnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-18
 */
public class FactoryProviderTest {
    /** Verhindert unendliche Schleifen. */
    @Rule
    public final Timeout globalTimeout = Timeout.seconds(1); // Maximale Anzahl an Sekunden pro Testfall.

    /** FQCN der konkreten Factory Klasse */
    private final String factoryFQCN;

    /** Factory. */
    private final Factory factory;

    /** Initialisiert die Factory. */
    public FactoryProviderTest() throws IOException {
        factoryFQCN = "edu.hm.kuehnel.powergrid.datastore.FactoryProvider";
        factory = Factory.newFactory(factoryFQCN);
    }

    @Test
    public void testNewCityVerifyFunctionalityCompareCityNameWithActualName() {
        // arrange
        final String cityName = "Entenhausen";
        final int cityRegion = 1;

        // act
        City city = factory.newCity(cityName, cityRegion);

        // assert
        final String want = cityName;
        final String have = city.getName();

        assertEquals(want, have);
    }

    @Test
    public void testNewCityVerifyFunctionalityCompareCityRegionWithActualRegion() {
        // arrange
        final String cityName = "Entenhausen";
        final int cityRegion = 1;

        // act
        City city = factory.newCity(cityName, cityRegion);

        // assert
        final int want = cityRegion;
        final int have = city.getRegion();

        assertEquals(want, have);
    }

    @Test
    public void testNewBoardVerifyFunctionalityCitiesAreConnectedViceVersa() {
        Edition edition = new EditionGermany();
        Board sut = factory.newBoard(edition);

        String[] citySpecification = edition.getCitySpecifications().get(0).split("\\s+");

        String cityAName = citySpecification[0];
        String cityBName = citySpecification[2];

        City cityA = sut.findCity(cityAName);
        City cityB = sut.findCity(cityBName);

        assertTrue(cityA.getConnections().containsKey(cityB));
        assertTrue(cityB.getConnections().containsKey(cityA));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewBoardRequireArgumentNonNullEdition() {
        Board sut = factory.newBoard(null);
    }

    /** NACHFOLGEND WERDEN LEDIGLICH DUMMY TEST IMPLEMENTIERUNGEN OHNE MEHRWERT VERWENDET. */

    @Test
    public void testNewPlayerRequireReturnDummyNull() {
        String secret = "Geheimnis";
        String color = "Farbe";

        Player sut = factory.newPlayer(secret, color);

        assertNull(sut);
    }

    @Test
    public void testNewPlantRequireReturnDummyNull() {
        int number = 1;
        int resources = 1;
        int cities = 1;

        Plant sut = factory.newPlant(number, null, resources, cities);

        assertNull(sut);
    }

    @Test
    public void testNewPlantMarketRequireReturnDummyNull() {
        Edition edition = new EditionGermany();

        PlantMarket sut = factory.newPlantMarket(edition);

        assertNull(sut);
    }

    @Test
    public void testNewResourceMarketRequireReturnDummyNull() {
        Edition edition = new EditionGermany();

        ResourceMarket sut = factory.newResourceMarket(edition);

        assertNull(sut);
    }

    @Test
    public void testNewAuctionRequireReturnDummyNull() {
        Plant plant = null;
        List<Player> players = null;

        Auction sut = factory.newAuction(plant, players);

        assertNull(sut);
    }

    @Test
    public void testNewGameRequireReturnDummyNull() {
        Edition edition = new EditionGermany();

        Game sut = factory.newGame(edition);

        assertNull(sut);
    }
}
