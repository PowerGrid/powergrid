package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import java.util.List;

/**
 * Produziert neue Bausteine des Spieles.
 * Kapselt die konkreten Implementierungen.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-10
 */
public interface Factory {
    /**
     * Liefert eine neue Factory,
     * deren FQCN die Systemproperty powergrid.factory definiert.
     * Wenn diese Systemproperty undefiniert ist,
     * gilt die Umgebungsvariable POWERGRID_FACTORYTYPE.
     * @return neue Factory.
     * @throws RuntimeException wenn weder Systemproperty noch Umgebungsvariable definiert sind.
     */
    static Factory newFactory() {
        return newFactory(System.getProperty("powergrid.factory",
                                             System.getenv("POWERGRID_FACTORY")));
    }

    /**
     * Liefert eine neue Factory des gegebenen Typs.
     * @param fqcn Fully qualified Classname der konkreten Factoryklasse.
     * @return neue Factory.
     * @throws RuntimeException wenn die Methode kein Objekt des Typs erzeugen kann.
     */
    static Factory newFactory(String fqcn) {
        try {
            return (Factory)Class.forName(fqcn)
                    .getConstructor()
                    .newInstance();
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Eine Stadt.
     * @param name   Name. Nicht null und nicht leer.
     * @param region Gebiet, in dem diese Stadt liegt. Wenigstens 1.
     * @return neue Stadt.
     */
    City newCity(String name, int region);

    /**
     * Ein Spieler.
     * @param secret Geheimnis des Spielers. Nicht null.
     * @param color  Farbe des Spielers. Nicht null.
     * @return  Neuer Spieler.
     */
    Player newPlayer(String secret, String color);

    /**
     * Ein Kraftwerk.
     * @param number    Nummer des Kraftwerks. Nicht negativ.
     * @param type      Kraftwerkstyp. Nicht null.
     * @param resources Anzahl Rohstoffe, die das Kraftwerk verbraucht. Nicht negativ.
     * @param cities    Anzahl Staedte, die das Kraftwerk versorgen kann. Echt positiv.
     * @return          Neues Kraftwerk.
     */
    Plant newPlant(int number, Plant.Type type, int resources, int cities);

    /**
     * Ein Kraftwerksmarkt.
     * Erzeugt alle Kraftwerke, die es in dieser Ausgabe gibt.
     * Die Kraftwerke liegen alle im Stapel mit den verborgenen Kraftwerken.
     * Die Reihenfolge spielt keine Rolle.
     * Der aktuelle und der zukuenftige Markt sind leer.
     * Die Karte "Stufe 3" ist noch nicht dabei.
     * @param edition Ausgabe des Spieles.
     * @return Kraftwerksmarkt. Nicht null.
     */
    PlantMarket newPlantMarket(Edition edition);

    /**
     * Ein Rohstoffmarkt.
     * Erzeugt alle Rohstoffe gemaess Ausgabe.
     * Macht davon so viele verfuegbar, wie es die Ausgabe festlegt.
     * Der Rest bleibt im Vorrat.
     * @param edition Ausgabe des Spieles.
     * @return Rohstoffmarkt. Nicht null.
     */
    ResourceMarket newResourceMarket(Edition edition);

    /**
     * Ein Spielplan.
     * Fuegt die Staedte der Edition und ihre Verbindungen in den Spielplan ein.
     * Der Spielplan ist noch offen.
     * @param edition Ausgabe des Spieles.
     * @return Spielplan.
     */
    Board newBoard(Edition edition);

    /**
     * Eine Auktion.
     * Das Hoechstgebot ist gleich der Nummer des Kraftwerkes.
     * Der erste Spieler der Liste ist der Hoechstbietende.
     * @param plant   Kraftwerk, das zum Verkauf steht. Nicht null.
     * @param players Spieler, die an der Auktion teilnehmen. Nicht null, nicht leer.
     *                Die Spieler bieten in der Reihenfolge dieser Liste.
     * @return Auktion. Nicht null.
     */
    Auction newAuction(Plant plant, List<Player> players);

    /**
     * Ein Spiel mit einem neuen Spielbrett, Kraftwerks- und Rohstoffmarkt.
     * Es gibt noch keine Spieler und keine Auktion.
     * Spielstufe (Index), Runden- und Zugnummer sind 0.
     * Die Phase ist Opening.
     * @param edition Ausgabe des Spieles.
     * @return Spiel. Nicht null.
     * @see Phase
     */
    Game newGame(Edition edition);
}
