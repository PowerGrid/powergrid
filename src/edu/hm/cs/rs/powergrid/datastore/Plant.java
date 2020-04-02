package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import java.util.Set;

/**
 * Ein Kraftwerk.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-03-06
 */
public interface Plant {
    /**
     * Kraftwerks-Typen.
     */
    enum Type {
        Coal(Resource.Coal),
        Oil(Resource.Oil),
        Garbage(Resource.Garbage),
        Uranium(Resource.Uranium),
        /**
         * Kann Kohle und Oel verbrennen.
         * Einzelheiten setzt die Spiellogik um.
         */
        Hybrid(Resource.Coal, Resource.Oil),
        /** Braucht keine Rohstoffe. */
        Eco,
        /** Braucht keine Rohstoffe. */
        Fusion,
        /** Pseudo-Kraftwerk: Start von Stufe 3. */
        Level3;

        /**
         * Rohstoffe, die dieser Kraftwerkstyp verbrauchen kann.
         */
        private final Resource[] resources;

        Type(Resource... resources) {
            this.resources = resources;
        }

        /**
         * Verschiedene Rohstoffe, die dieser Kraftwerkstyp verbrauchen kann.
         * @return Rohstoffe fuer diesen Kraftwerkstyp.
         */
        public Set<Resource> getResources() {
            return Set.of(resources);
        }

    }

    /**
     * Eindeutige Nummer.
     * @return Nummer. Nicht negativ.
     */
    int getNumber();

    /**
     * Anzahl Staedte, die dieses Kraftwerk mit Strom versorgen kann.
     * @return Anzahl Staedte. Nicht negativ.
     */
    int getCities();

    /**
     * Anzahl Rohstoffe, die dieses Kraftwerk braucht, egal welcher Art.
     * @return Anzahl Rohstoffe. Nicht negativ.
     */
    int getNumberOfResources();

    Type getType();

    /**
     * Test, ob dieses Kraftwerk Strom produziert hat.
     * @return true genau dann, wenn dieses Kraftwerk gelaufen ist.
     */
    boolean hasOperated();

    /**
     * Legt fest, ob  dieses Kraftwerk Strom produziert hat.
     * @param operated true genau dann, wenn dieses Kw Strom produziert hat.
     */
    void setOperated(boolean operated);

    /**
     * Rohstoffsammlungen, mit denen dieses Kraftwerk laufen kann.
     * * Wenn das Kw nichts braucht, enthaelt die Menge eine leere Sammlung als einziges Element,
     * das heisst {[]}.
     * * Wenn das Kw nur eine Rohstoffsorte akzeptiert, hat die Menge ein Element,
     * beispielsweise {[Coal=3]}.
     * * Wenn das Kw verschiedene Sorten akzeptiert, enthaelt die Menge alle zulaessigen Kombinationen,
     * beispielsweise {[Coal=2], [Coal, Oil], [Oil=2]}.
     * @return Verschiedene Rohstoffsammlungen. Nicht null und nicht leer.
     * Menge und Elemente unveraenderlich.
     */
    Set<Bag<Resource>> getResources();
}
