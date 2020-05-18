package edu.hm.cs.rs.powergrid.datastore;

import java.util.Set;

/**
 * Der Spielplan.
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-04-13
 */
public interface Board {
    /**
     * Entfernt alle Staedte mit einer Region ueber der Grenze.
     * Loescht auch alle Verbindungen von und zu entfernten Staedten.
     * @param remaining Staedte in Regionen bis zu dieser Nummer bleiben bestehen.
     *                  Staedte darueber verschwinden.
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     */
    void closeRegions(int remaining);

    /**
     * Sucht eine Stadt.
     * @param name Name.
     * @return Stadt mit dem Namen oder null, wenn es keine mit diesem Namen gibt.
     */
    City findCity(String name);

    /**
     * Menge aller Staedte.
     * @return Staedte. Nicht null.
     * Veraenderlich bis zum ersten close-Aufruf. Dann unveraenderlich.
     */

    Set<City> getCities();

    /**
     * Schliesst diesen Spielplan und alle Staedte darauf.
     * @throws IllegalStateException wenn der Spielplan geschlossen ist.
     */
    void close();
}
