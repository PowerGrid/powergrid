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

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.Set;

/** Ein Kraftwerk.
 * @author Stefan Kuehnel, stefan.kuehnel@hm.edu
 * @version last-modified 2020-05-16
 */
class PlantGenerator implements Plant {

    /**
     * Eindeutige Nummer des Kraftwerks.
     */
    private final int uniqueNumber;

    /**
     * Anzahl Staedte, die dieses Kraftwerk mit Strom versorgen kann.
     */
    private final int numberOfSuppliableCities;

    /**
     * Anzahl Rohstoffe, die dieses Kraftwerk braucht, egal welcher Art.
     */
    private final int numberOfResources;

    /**
     * Rohstoffe, die dieses Kraftwerk braucht, um Strom zu produzieren.
     */
    private final Type typeOfResources;

    /**
     * Rohstoffsammlungen, mit denen dieses Kraftwerk laufen kann.
     */
    private Bag<Resource> resources;

    /**
     * Bestimmt, ob Kraftwerk Strom produziert hat, oder nicht.
     */
    private Boolean operated = false;


    /**
     * Instanziiert ein neues Kraftwerk.
     *
     * @param number      Eindeutige Nummer. Nicht negativ.
     * @param type        Rohstoffe, die dieses Kraftwerk zur Stromproduktion benoetigt.
     * @param resources   Anzahl notwendiger Rohstoffe zur Stromproduktion. Nicht negativ.
     * @param cities      Anzahl mit Strom versorgbarer Staedte. Nicht negativ.
     *
     * @throws IllegalArgumentException wenn eindeutige Nummer negativ ist.
     * @throws IllegalArgumentException wenn Anzahl notwendiger Rohstoffe negativ ist.
     * @throws IllegalArgumentException wenn Anzahl mit Strom versorgbarer Staedte negativ ist.
     */
    public PlantGenerator(int number, Type type, int resources, int cities) {
        if (number < 0)
            throw new IllegalArgumentException("Eindeutige Nummer des Kraftwerks darf nicht negativ sein.");

        if (resources < 0)
            throw new IllegalArgumentException("Anzahl notwendiger Rohstoffe darf nicht negativ sein.");

        if (cities < 0)
            throw new IllegalArgumentException("Anzahl mit Strom versorgbarer Staedte darf nicht negativ sein.");

        this.uniqueNumber = number;
        this.numberOfSuppliableCities = cities;
        this.numberOfResources = resources;
        this.typeOfResources = type;
    }

    /**
     * Eindeutige Nummer.
     *
     * @return Nummer. Nicht negativ.
     */
    @Override
    public int getNumber() {
        return uniqueNumber;
    }

    /**
     * Anzahl Staedte, die dieses Kraftwerk mit Strom versorgen kann.
     *
     * @return Anzahl Staedte. Nicht negativ.
     */
    @Override
    public int getCities() {
        return numberOfSuppliableCities;
    }

    /**
     * Anzahl Rohstoffe, die dieses Kraftwerk braucht, egal welcher Art.
     *
     * @return Anzahl Rohstoffe. Nicht negativ.
     */
    @Override
    public int getNumberOfResources() {
        return numberOfResources;
    }

    /**
     * Rohstoffe, mit denen das Kraftwerk Strom produzieren kann.
     * @return Type.
     */
    @Override
    public Type getType() {
        return typeOfResources;
    }

    /**
     * Test, ob dieses Kraftwerk Strom produziert hat.
     *
     * @return true genau dann, wenn dieses Kraftwerk gelaufen ist.
     */
    @Override
    public boolean hasOperated() {
        return operated;
    }

    /**
     * Legt fest, ob  dieses Kraftwerk Strom produziert hat.
     *
     * @param operated true genau dann, wenn dieses Kw Strom produziert hat.
     */
    @Override
    public void setOperated(boolean operated) {
        this.operated = operated;
    }

    /**
     * Rohstoffsammlungen, mit denen dieses Kraftwerk laufen kann.
     * * Wenn das Kw nichts braucht, enthaelt die Menge eine leere Sammlung als einziges Element,
     * das heisst {[]}.
     * * Wenn das Kw nur eine Rohstoffsorte akzeptiert, hat die Menge ein Element,
     * beispielsweise {[Coal=3]}.
     * * Wenn das Kw verschiedene Sorten akzeptiert, enthaelt die Menge alle zulaessigen Kombinationen,
     * beispielsweise {[Coal=2], [Coal, Oil], [Oil=2]}.
     *
     * @return Verschiedene Rohstoffsammlungen. Nicht null und nicht leer.
     * Menge und Elemente unveraenderlich.
     */
    @Override
    public Set<Bag<Resource>> getResources() {
        throw new UnsupportedOperationException("Diese Exception wird in Aufgabe 3 entfernt.");
    }
}
