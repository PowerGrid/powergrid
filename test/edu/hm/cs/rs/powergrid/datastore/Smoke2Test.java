package edu.hm.cs.rs.powergrid.datastore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version last modified 2020-03-29
 */
public class Smoke2Test {
    @Rule public final Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    /** FQCN der konkreten Factoryklasse. */
    private final String factoryFQCN;

    /** Factory. */
    private final Factory factory;

    /** Initialisiert die Factory . */
    public Smoke2Test() throws IOException {
        factoryFQCN = "edu.hm.ploeckl.se2.powergrid.datastore.FactoryImplementation";
        factory = Factory.newFactory(factoryFQCN);
    }

    @Test public void newPlant() {
        // arrange
        Plant plant = factory.newPlant(3, Plant.Type.Oil, 2, 1);
        // act
        // assert
        Assert.assertEquals(3, plant.getNumber());
        Assert.assertEquals(Plant.Type.Oil, plant.getType());
        Assert.assertEquals(2, plant.getNumberOfResources());
        Assert.assertEquals(1, plant.getCities());
    }

    @Test public void newPlayer() {
        // arrange
        Player sut = factory.newPlayer("hush - don't tell!", "red");
        // act
        // assert
        assertTrue(sut.hasSecret("hush - don't tell!"));
    }

}
