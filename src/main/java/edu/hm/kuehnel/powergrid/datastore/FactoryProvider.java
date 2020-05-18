package edu.hm.kuehnel.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.Auction;
import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Factory;
import edu.hm.cs.rs.powergrid.datastore.Game;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.PlantMarket;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.ResourceMarket;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Produziert neue Bausteine des Spieles.
 * @author Stefan Kühnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-16
 */
public class FactoryProvider implements Factory {

    /**
     * Eine Stadt.
     *
     * @param name   Name. Nicht null und nicht leer.
     * @param region Gebiet, in dem diese Stadt liegt. Wenigstens 1.
     * @return neue Stadt.
     */
    @Override
    public City newCity(String name, int region) { // ToDo: Caching implementieren.
        return new CityGenerator(name, region);
    }

    /**
     * Ein Spieler.
     *
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     * @return Neuer Spieler.
     */
    @Override
    public Player newPlayer(String secret, String color) {
        return null;
    }

    /**
     * Ein Kraftwerk.
     *
     * @param number    Nummer des Kraftwerks. Nicht negativ.
     * @param type      Kraftwerkstyp. Nicht null.
     * @param resources Anzahl Rohstoffe, die das Kraftwerk verbraucht. Nicht negativ.
     * @param cities    Anzahl Staedte, die das Kraftwerk versorgen kann. Echt positiv.
     * @return Neues Kraftwerk.
     */
    @Override
    public Plant newPlant(int number, Plant.Type type, int resources, int cities) {
        return null;
    }

    /**
     * Ein Kraftwerksmarkt.
     * Erzeugt alle Kraftwerke, die es in dieser Ausgabe gibt.
     * Die Kraftwerke liegen alle im Stapel mit den verborgenen Kraftwerken.
     * Die Reihenfolge spielt keine Rolle.
     * Der aktuelle und der zukuenftige Markt sind leer.
     * Die Karte "Stufe 3" ist noch nicht dabei.
     *
     * @param edition Ausgabe des Spieles.
     * @return Kraftwerksmarkt. Nicht null.
     */
    @Override
    public PlantMarket newPlantMarket(Edition edition) {
        return null;
    }

    /**
     * Ein Rohstoffmarkt.
     * Erzeugt alle Rohstoffe gemaess Ausgabe.
     * Macht davon so viele verfuegbar, wie es die Ausgabe festlegt.
     * Der Rest bleibt im Vorrat.
     *
     * @param edition Ausgabe des Spieles.
     * @return Rohstoffmarkt. Nicht null.
     */
    @Override
    public ResourceMarket newResourceMarket(Edition edition) {
        return null;
    }

    /**
     * Ein Spielplan.
     * Fuegt die Staedte der Edition und ihre Verbindungen in den Spielplan ein.
     * Der Spielplan ist noch offen.
     *
     * @param edition Ausgabe des Spieles.
     * @return Spielplan.
     */
    @Override
    public Board newBoard(Edition edition) { // ToDo: Caching implementieren.
        final Board board = Optional.ofNullable(edition)
                .map(n -> new BoardGenerator())
                .orElseThrow(
                        () -> new IllegalArgumentException("Die Edition darf nicht null sein.")
                );

        final Set<City> boardCities = board.getCities();
        final List<String> citySpecifications = edition.getCitySpecifications();

        // Add cities to board
        for (String citySpec : citySpecifications) {
            final List<String> cityData = Stream.of(citySpec.split("\\s+"))
                    .map(String::trim)
                    .collect(Collectors.toList());

            final String cityName = cityData.get(0);
            final int cityRegion = Integer.parseInt(cityData.get(1));
            final City city = new CityGenerator(cityName, cityRegion);

            boardCities.add(city);
        }

        // Connect cities on board vice versa.
        for (String citySpec : citySpecifications) {
            final List<String> cityData = Stream.of(citySpec.split("\\s+"))
                    .map(String::trim)
                    .collect(Collectors.toList());

            // Get base city data.
            final String cityName = cityData.get(0);
            final String cityRegion = cityData.get(1);

            // Remove base city data, focus on connections.
            cityData.remove(cityName);
            cityData.remove(cityRegion);

            final City cityOnBoard = board.findCity(cityName);

            if(!cityData.isEmpty()) {
                for (int index = 0; index < cityData.size(); index++) {
                    final String cityToConnectName = cityData.get(index);
                    final int cityToConnectCosts = Integer.parseInt(cityData.get(index + 1));

                    final City cityToConnect = board.findCity(cityToConnectName);

                    // Connect cities vice versa
                    cityOnBoard.connect(cityToConnect, cityToConnectCosts);
                    cityToConnect.connect(cityOnBoard, cityToConnectCosts);

                    index++;
                }
            }
        }

        return board;
    }

    /**
     * Eine Auktion.
     * Das Hoechstgebot ist gleich der Nummer des Kraftwerkes.
     * Der erste Spieler der Liste ist der Hoechstbietende.
     *
     * @param plant   Kraftwerk, das zum Verkauf steht. Nicht null.
     * @param players Spieler, die an der Auktion teilnehmen. Nicht null, nicht leer.
     *                Die Spieler bieten in der Reihenfolge dieser Liste.
     * @return Auktion. Nicht null.
     */
    @Override
    public Auction newAuction(Plant plant, List<Player> players) {
        return null;
    }

    /**
     * Ein Spiel mit einem neuen Spielbrett, Kraftwerks- und Rohstoffmarkt.
     * Es gibt noch keine Spieler und keine Auktion.
     * Spielstufe (Index), Runden- und Zugnummer sind 0.
     * Die Phase ist Opening.
     *
     * @param edition Ausgabe des Spieles.
     * @return Spiel. Nicht null.
     */
    @Override
    public Game newGame(Edition edition) {
        return null;
    }
}
