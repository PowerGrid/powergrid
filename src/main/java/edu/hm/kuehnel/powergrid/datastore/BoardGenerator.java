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

import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Der Spielplan.
 * @author Stefan Kuehnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-18
 */
public class BoardGenerator implements Board {
    /**
     * Eine Liste aller Staedte auf dem Spielplan.
     */
    private final Set<City> citiesOnBoard = new HashSet<>();

    /**
     * Bestimmt, ob Spielplan geschlossen wurde, oder nicht.
     */
    private boolean closed;

    /**
     * Entfernt alle Staedte mit einer Region ueber der Grenze.
     * Loescht auch alle Verbindungen von und zu entfernten Staedten.
     *
     * @param remaining Staedte in Regionen bis zu dieser Nummer bleiben bestehen.
     *                  Staedte darueber verschwinden.
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     */
    @Override
    public void closeRegions(int remaining) {
        requireNonClosedBoard();
        requireNonNegativeRemaining(remaining);

        final Iterator<City> cityIterator = citiesOnBoard.iterator();

        while (cityIterator.hasNext()) {
            final City city = cityIterator.next();

            final Iterator<City> connectedCityIterator = city.getConnections().keySet().iterator();

            while(connectedCityIterator.hasNext()) {
                final City connectedCity = connectedCityIterator.next();

                if(connectedCity.getRegion() > remaining)
                    connectedCityIterator.remove();
            }

            if (city.getRegion() > remaining)
                cityIterator.remove();
        }
    }

    /**
     * Sucht eine Stadt.
     *
     * @param name Name.
     * @return Stadt mit dem Namen oder null, wenn es keine mit diesem Namen gibt.
     */
    @Override
    public City findCity(String name) {
        final String findCityName = Optional.ofNullable(name) // Name darf nicht null sein
                .filter(Predicate.not(String::isBlank)) // Name darf nicht leer sein.
                .orElseThrow(
                        () -> new IllegalArgumentException("Name der zu findenden Stadt darf nicht null und nicht leer sein.")
                );

        // Gibt entweder die Stadt oder null zurück.
        return citiesOnBoard.stream() // Erstellt Stream aus den Staedten
                .filter(cityOnBoard -> cityOnBoard.getName().equals(findCityName)) // Vergleicht Namen beider Staedte.
                .findFirst()  // Findet das erste Element.
                .orElse(null);
    }

    /**
     * Menge aller Staedte.
     *
     * @return Staedte. Nicht null.
     * Veraenderlich bis zum ersten close-Aufruf. Dann unveraenderlich.
     */
    @Override
    public Set<City> getCities() { // ToDo: Pruefen, ob citiesOnBoard.isNull() erforderlich ist.
        // Gibt bei geschlossenem Spielbrett ein unveraenderliches Set zurück.
        if (closed)
            return Collections.unmodifiableSet(citiesOnBoard);

        // Gibt ein veraenderliches Set zurück wenn Spielbrett noch nicht geschlossen ist.
        return citiesOnBoard;
    }

    /**
     * Schliesst diesen Spielplan und alle Staedte darauf.
     *
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     */
    @Override
    public void close() {
        requireNonClosedBoard();

        // Schliesst jede Stadt auf dem Spielplan.
        for(City city : citiesOnBoard) {
            city.close();
        }

        // Schliesst diesen Spielplan.
        closed = true;
    }

    /**
     * Hilfsmethode, die prueft, ob der Spielplan bereits geschlossen wurde.
     * @throws IllegalStateException wenn der Spielplan bereits geschlossen ist.
     */
    private void requireNonClosedBoard() {
        if (closed)
            throw new IllegalStateException("Der Spielplan wurde bereits geschlossen");
    }

    /**
     * Hilfsmethode, die prueft, ob remaining negativ ist.
     * @param remaining Wert, der auf Negativitaet geprüft werden soll.
     * @throws IllegalArgumentException wenn remaining negativ ist.
     */
    private void requireNonNegativeRemaining(int remaining) {
        if (remaining < 0)
            throw new IllegalArgumentException("Das Gebiet der verbleibenden Städte darf nicht negativ sein.");
    }
}