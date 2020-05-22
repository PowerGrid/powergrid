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

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

/**
 * Ein Spieler.
 *
 * @author Stefan Kuehnel, stefan.kuehnel@hm.edu
 * @version last modified 2020-05-23
 */
class PlayerGenerator implements Player {
    /**
     * Staedte, die der Spieler an sein Stromnetz angeschlossen hat.
     */
    private final Set<City> cities = new TreeSet<>();

    /**
     * Kraftwerke des Spielers.
     */
    private final Set<Plant> plants = new TreeSet<>();

    /**
     * Farbe dieses Spielers.
     */
    private final String color;

    /**
     * Geheimnis dieses Spielers.
     */
    private final String secret;

    /**
     * Prueft, ob das Geheimnis schon einmal preisgegeben wurde.
     */
    private Boolean secretDisclosedOnce = false;

    /**
     * Das Vermoegen.
     */
    private int electro;

    /**
     * Bestimmt, ob Spieler an der Reihe war, oder nicht.
     */
    private boolean passed;

    /**
     * Instanziiert einen neuen Spieler.
     *
     * @param color  Die Farbe des Spielers. Nicht null, nicht leer.
     * @param secret Das Geheimnis des Spielers. Nicht null, nicht leer.
     *
     * @throws IllegalArgumentException wenn die Farbe des Spielers null oder leer ist.
     * @throws IllegalArgumentException wenn das Geheimnis des Spielers null oder leer ist.
     */
    public PlayerGenerator(String color, String secret) {

        this.color = Optional.ofNullable(color) // Die Farbe des Spielers darf nicht null sein.
                .filter(Predicate.not(String::isBlank)) // Die Farbe des Spielers  darf nicht leer sein.
                .orElseThrow(
                        () -> new IllegalArgumentException("Die Farbe des Spielers darf nicht leer oder null sein.")
                );

        this.secret = Optional.ofNullable(secret) // Das Geheimnis des Spielers darf nicht null sein.
                .filter(Predicate.not(String::isBlank)) // Das Geheimnis des Spielers  darf nicht leer sein.
                .orElseThrow(
                        () -> new IllegalArgumentException("Das Geheimnis des Spielers darf nicht leer oder null sein.")
                );
    }

    /**
     * Farbe dieses Spielers.
     *
     * @return Farbe. Nicht null.
     */
    @Override
    public String getColor() {
        return color;
    }

    /**
     * Staedte, die dieser Spieler an sein Netz angeschlossen hat.
     *
     * @return Menge der Staedte. Nicht null.
     */
    @Override
    public Set<City> getCities() {
        return cities;
    }

    /**
     * Die Kraftwerke dieses Spielers.
     *
     * @return Menge der Kraftwerke. Nicht null.
     */
    @Override
    public Set<Plant> getPlants() {
        return plants;
    }

    /**
     * Die Rohstoffe, die der Spieler in seinen Kraftwerken lagert.
     *
     * @return Rohstoffe. Nicht null.
     */
    @Override
    public Bag<Resource> getResources() {
        throw new UnsupportedOperationException("Diese Exception wird in Aufgabe 3 entfernt.");
    }

    /**
     * Das Vermoegen.
     *
     * @return Anzahl Elektro. Nicht negativ.
     */
    @Override
    public int getElectro() {
        return electro;
    }

    /**
     * Legt das Vermoegen neu fest.
     *
     * @param electro Anzahl Elektro. Nicht negativ.
     */
    @Override
    public void setElectro(int electro) {
        if (electro < 0)
            throw new IllegalArgumentException("Das Vermoegen darf nicht negativ sein.");

        this.electro = electro;
    }

    /**
     * Test, ob der Spieler schon an der Reihe war.
     *
     * @return true genau dann, wenn der Spieler an der Reihe war.
     */
    @Override
    public boolean hasPassed() {
        return passed;
    }

    @Override
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    /**
     * Liefert das Geheimnis dieses Spielers.
     * Nur der erste Aufruf liefert das Geheimnis.
     * Der zweite und alle weiteren liefern null.
     *
     * @return Geheimnis oder null ab dem zweiten Aufruf.
     */
    @Override
    public String getSecret() {
        // Prueft, ob das Geheimnis nicht schon einmal preisgegeben wurde.
        if(secretDisclosedOnce)
            return null;

        // Speichert, dass das Geheimnis nachfolgend einmal preisgegeben wurde.
        secretDisclosedOnce = true;

        // Gibt das Geheimnis einmalig aus.
        return secret;
    }

    /**
     * Test, ob dieser Spieler das gegebene Geheimis hat.
     *
     * @param givenSecret Ein String.
     * @return true, wenn der String das Geheimnis ist; false ansonsten.
     */
    @Override
    public boolean hasSecret(String givenSecret) {
        // Das Geheimnis des Spielers.
        final String playerSecret = this.secret;

        // Ausgabe ob Geheimnisse uebereinstimmen, oder nicht.
        return playerSecret.equals(givenSecret);
    }
}