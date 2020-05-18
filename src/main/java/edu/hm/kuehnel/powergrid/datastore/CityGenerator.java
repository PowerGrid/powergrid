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

import edu.hm.cs.rs.powergrid.datastore.City;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/** Eine Stadt auf dem Spielplan.
 * @author Stefan Kuehnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-16
 */
class CityGenerator implements City {
    /**
     * Der Name der Stadt.
     */
    private final String name;

    /**
     * Das Gebiet, in dem die Stadt liegt.
     */
    private final int region;

    /**
     * Bestimmt, ob Stadt geschlossen wurde, oder nicht.
     */
    private boolean closed;

    /**
     * Die Verbindungen der Stadt zu anderen nicht identischen Staedten auf dem Spielbrett.
     */
    private final Map<City, Integer> connections = new HashMap<>();

    /**
     * Initialisiert eine neue Stadt.
     * @param name Name der Stadt. Nicht leer, nicht null.
     * @param region Gebiet, in dem die Stadt liegt. Wenigstens 1.
     *
     * @throws IllegalArgumentException wenn der Name der Stadt null oder leer ist.
     * @throws IllegalArgumentException wenn das Gebiet, in dem die Stadt liegt, nicht wenigstens 1 ist.
     */
    CityGenerator(final String name, final int region) {
        this.name = Optional.ofNullable(name) // Name der Stadt darf nicht null sein.
                            .filter(Predicate.not(String::isBlank)) // Name der Stadt darf nicht leer sein.
                            .orElseThrow(
                                    () -> new IllegalArgumentException("Name der Stadt darf nicht leer oder null sein.")
                            );

        this.region = Optional.of(region)
                            .filter(reg -> reg >= 1) // Das Gebiet der Stadt muss wenigstens 1 sein.
                            .orElseThrow(
                                    () -> new IllegalArgumentException("Das Gebiet der Stadt muss wenigstens 1 sein.")
                            );
    }

    /**
     * Name der Stadt.
     * @return Name. Nicht leer, nicht null.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gebiet, in dem die Stadt liegt.
     * @return Gebiet. Wenigstens 1.
     */
    @Override
    public int getRegion() {
        return region;
    }

    /**
     * Fuegt eine Verbindung von dieser Stadt zu einer anderen ein.
     * Aendert nur diese Stadt, nicht die andere.
     * Nur vor dem ersten close-Aufruf erlaubt.
     * @param to   Eine andere Stadt. Bleibt unveraendert. Nicht null, nicht diese.
     * @param cost Verbindungskosten. Nicht negativ.
     * @throws IllegalStateException    wenn diese Stadt geschlossen ist.
     * @throws IllegalArgumentException wenn es schon eine Verbindung zu to gibt.
     */
    @Override
    public void connect(final City to, final int cost) {
        if(closed)
            throw new IllegalStateException("Die Stadt wurde bereits geschlossen.");

        final City connectTo = Optional.ofNullable(to) // Die zu verbindende Stadt ist nicht null.
                .filter(cityToConnect -> !cityToConnect.equals(this) && !connections.containsKey(cityToConnect)) // Die zu verbindenden Staedte sind nicht identisch und noch nicht verbunden.
                .orElseThrow(
                        () -> new IllegalArgumentException("Die zu verbindenden Staedte dürfen nicht identisch und noch nicht verbunden sein.")
                );
        final int connectCost = Optional.of(cost)
                .filter(costToConnect -> costToConnect >= 0) // Die Verbindungskosten muessen grösser gleich 0 sein.
                .orElseThrow(
                        () -> new IllegalArgumentException("Die Verbindungskosten muessen grösser gleich 0 sein.")
                );

        connections.put(connectTo, connectCost);
    }

    /**
     * Verbindungen zu anderen Staedten.
     * Veraenderlich bis zum ersten close-Aufruf, dann unveraenderlich.
     * Jeder Eintrag bildet eine andere Stadt auf die Verbindungskosten dort hin ab.
     * @return Verbindungen. Nicht null und nicht leer.
     *
     * @throws IllegalStateException wenn die Verbindungen der Stadt leer sind.
     */
    @Override
    public Map<City, Integer> getConnections() {
        // Prueft, ob mindestens eine Verbindung zu einer anderen Stadt existiert.
        if(connections.isEmpty())
            throw new IllegalStateException("Die Stadt muss mit mindestens einer anderen Stadt verbunden werden.");

        // Prueft, ob die Stadt schon geschlossen ist und liefert eine unveraenderliche Map zurück.
        if(closed)
            return Collections.unmodifiableMap(connections);

        return connections;
    }

    /**
     * Schliesst die Verbindungen dieser Stadt ab.
     * connect-Aufrufe sind nicht mehr erlaubt, dafuer getConnections.
     * @throws IllegalStateException wenn die Stadt geschlossen ist.
     * @throws IllegalStateException wenn die Verbindungen der Stadt leer sind.
     */
    @Override
    public void close() {
        if (closed)
            throw new IllegalStateException("Die Stadt wurde bereits geschlossen.");

        if (connections.isEmpty())
            throw new IllegalStateException("Die Stadt muss mit mindestens einer anderen Stadt verbunden werden.");

        closed = true;
    }

    /**
     * Gibt natürliche Ordnung zweier Stadtnamen zurück.
     * @param that Eine andere Stadt.
     * @return Gibt die natürliche Ordnung der Städte zurück.
     */
    @Override
    public int compareTo(City that) {
        final String currentCityName = this.getName();
        final String externalCityName = that.getName();
        return currentCityName.compareTo(externalCityName);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final CityGenerator that = (CityGenerator) object;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}