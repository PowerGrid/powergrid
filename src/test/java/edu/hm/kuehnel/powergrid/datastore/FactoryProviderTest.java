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

/** Die Testklasse fuer den FactoryProvider.
 * @author Stefan Kühnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-23
 */
public class FactoryProviderTest {
    /** Verhindert unendliche Schleifen. */
    @Rule
    public final Timeout globalTimeout = Timeout.seconds(1); // Maximale Anzahl an Sekunden pro Testfall.

    /** Factory. */
    private final Factory factory;

    /** Initialisiert die Factory. */
    public FactoryProviderTest() throws IOException {
        factory = Factory.newFactory();
    }

    @Test
    public void testNewCity() {
        // arrange
        final String cityName = "Entenhausen";
        final int cityRegion = 1;

        // act
        final City city = factory.newCity(cityName, cityRegion);

        // assert
        assertEquals("Uebergebene Region stimmt mit erhaltener ueberein.", cityRegion, city.getRegion());
        assertEquals("Uebergebener Name stimmt mit erhaltenem ueberein.", cityName, city.getName());
    }

    @Test
    public void testNewBoard() {
        // arrange
        final Edition edition = new EditionGermany();
        final Board sut = factory.newBoard(edition);

        final String[] citySpecification = edition.getCitySpecifications().get(0).split("\\s+");

        final String cityAName = citySpecification[0];
        final String cityBName = citySpecification[2];

        // act
        final City cityA = sut.findCity(cityAName);
        final City cityB = sut.findCity(cityBName);

        // assert
        assertTrue("Stadt A wurde mit Stadt B verbunden.", cityA.getConnections().containsKey(cityB));
        assertTrue("Stadt B wurde mit Stadt A verbunden.", cityB.getConnections().containsKey(cityA));
    }

    @Test
    public void testNewPlayer() {
        // arrange
        final String secret = "Psst, nicht verraten";
        final String color = "Blau";

        // act
        final Player player = factory.newPlayer(secret, color);

        // assert
        assertEquals("Uebergebenes Geheimnis stimmt mit erhaltenem überein.", secret, player.getSecret());
        assertEquals("Uebergebene Farbe stimmt mit erhaltener ueberein.", color, player.getColor());
    }

    @Test
    public void testNewPlant() {
        // arrange
        final int number = 1;
        final Plant.Type type = Plant.Type.Coal;
        final int resources = 1;
        final int cities = 1;

        // act
        final Plant plant = factory.newPlant(number, type, resources, cities);

        // assert
        assertEquals("Uebergebene Nummer stimmt mit erhaltener ueberein.", number, plant.getNumber());
        assertEquals("Uebergebene Ressource stimmt mit erhaltener ueberein.", type, plant.getType());
        assertEquals("Uebergebene Anzahl Ressourcen stimmt mit erhaltener ueberein.", resources, plant.getNumberOfResources());
        assertEquals("Uebergebene Anzahl versorgbarer Staedte stimmt mit erhaltener ueberein.", cities, plant.getCities());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testNewBoardRequireArgumentNonNullEdition() {
        factory.newBoard(null);
    }

    /** NACHFOLGEND WERDEN LEDIGLICH DUMMY TEST IMPLEMENTIERUNGEN OHNE MEHRWERT VERWENDET. */

    @Test
    public void testNewPlantMarketRequireReturnDummyNull() {
        final Edition edition = new EditionGermany();

        final PlantMarket sut = factory.newPlantMarket(edition);

        assertNull(sut);
    }

    @Test
    public void testNewResourceMarketRequireReturnDummyNull() {
        final Edition edition = new EditionGermany();

        final ResourceMarket sut = factory.newResourceMarket(edition);

        assertNull(sut);
    }

    @Test
    public void testNewAuctionRequireReturnDummyNull() {
        final Plant plant = null;
        final List<Player> players = null;

        final Auction sut = factory.newAuction(plant, players);

        assertNull(sut);
    }

    @Test
    public void testNewGameRequireReturnDummyNull() {
        final Edition edition = new EditionGermany();

        final Game sut = factory.newGame(edition);

        assertNull(sut);
    }
}
