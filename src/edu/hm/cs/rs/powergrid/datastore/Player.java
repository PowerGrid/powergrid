package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import java.util.Set;

/**
 * Ein Spieler.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-02-22
 */
public interface Player  {
    /**
     * Farbe dieses Spielers.
     * @return Farbe. Nicht null.
     */
    String getColor();

    /**
     * Staedte, die dieser Spieler an sein Netz angeschlossen hat.
     * @return Menge der Staedte. Nicht null.
     */
    Set<City> getCities();

    /**
     * Die Kraftwerke dieses Spielers.
     * @return Menge der Kraftwerke. Nicht null.
     */
    Set<Plant> getPlants();

    /**
     * Die Rohstoffe, die der Spieler in seinen Kraftwerken lagert.
     * @return  Rohstoffe. Nicht null.
     */
    Bag<Resource> getResources();

    /**
     * Das Vermoegen.
     * @return Anzahl Elektro. Nicht negativ.
     */
    int getElectro();

    /**
     * Legt das Vermoegen neu fest.
     * @param electro Anzahl Elektro. Nicht negativ.
     */
    void setElectro(int electro);

    /**
     * Test, ob der Spieler schon an der Reihe war.
     * @return true genau dann, wenn der Spieler an der Reihe war.
     */
    boolean hasPassed();

    void setPassed(boolean passed);

    /**
     * Liefert das Geheimnis dieses Spielers.
     * Nur der erste Aufruf liefert das Geheimnis.
     * Der zweite und alle weiteren liefern null.
     * @return Geheimnis oder null ab dem zweiten Aufruf.
     */
    String getSecret();

    /** Test, ob dieser Spieler das gegebene Geheimis hat.
     * @param secret Ein String.
     * @return true, wenn der String das Geheimnis ist; false ansonsten.
     */
    boolean hasSecret(String secret);

}
