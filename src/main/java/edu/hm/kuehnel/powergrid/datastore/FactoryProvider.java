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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Produziert neue Bausteine des Spieles.
 * @author Stefan Kühnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-23
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
    public Player newPlayer(String secret, String color) { // ToDo: Caching implementieren.
        return new PlayerGenerator(color, secret);
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
        return new PlantGenerator(number, type, resources, cities);
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

        // Prüft, ob die Edition null ist.
        Optional.ofNullable(edition).orElseThrow(
                () -> new IllegalArgumentException("Die Edition darf nicht null sein.")
        );

        // Erstellt einen neuen Spielplan.
        final Board board = new BoardGenerator();

        // Fügt Städte auf dem Spielplan ein.
        edition.getCitySpecifications().stream()
                .forEach(citySpec -> {
                    final String[] splitSpec = citySpec.split("\\s+");

                    // Die Details der Stadt auf dem Spielplan.
                    final String cityName = splitSpec[0];
                    final int cityRegion = Integer.parseInt(splitSpec[1]);

                    // Erstellt eine neue Stadt.
                    final City city = new CityGenerator(cityName, cityRegion);

                    // Fügt die Stadt zum Spielplan hinzu.
                    board.getCities().add(city);
                });

        // Legt Verbindungen vice versa.
        edition.getCitySpecifications().stream()
                .forEach(citySpec -> {
                    final List<String> splitSpec = Pattern.compile("\\s+")
                            .splitAsStream(citySpec)
                            .collect(Collectors.toList());

                    // Die Details der Stadt auf dem Spielplan.
                    final String cityName = splitSpec.get(0);
                    final String cityRegion = splitSpec.get(1);

                    // Nur noch die Verbindungen sollen übrig bleiben.
                    splitSpec.remove(cityName);
                    splitSpec.remove(cityRegion);

                    // Findet Stadt auf dem Spielplan.
                    final City boardCity = board.findCity(cityName);

                    // Erstellt einen Iterator für die Verbindungen.
                    final Iterator<String> connectionIterator = splitSpec.iterator();

                    // Iteriert über die Verbindungen.
                    while (connectionIterator.hasNext()) {

                        // Der Name der zu verbindenden Stadt.
                        final String connectCityName = connectionIterator.next();

                        // Die Kosten der Verbindung.
                        final int connectCityCost = Integer.parseInt(connectionIterator.next());

                        // Findet zu verbindende Stadt auf dem Spielplan.
                        final City connectCity = board.findCity(connectCityName);

                        // Verbindet die Städte vice versa.
                        boardCity.connect(connectCity, connectCityCost);
                        connectCity.connect(boardCity, connectCityCost);
                    }
                });

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
