package edu.hm.cs.rs.powergrid.datastore;

import edu.hm.cs.rs.powergrid.EditionGermany;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * @author R. Schiedermeier, rs@cs.hm.edu
 * @version 2020-02-19
 */
public class Smoke1Test {
    @Rule public Timeout globalTimeout = Timeout.seconds(1); // max seconds per test

    private final String fqcn = "edu.hm.ploeckl.se2.powergrid.datastore.FactoryImplementation";

    private final Factory factory = Factory.newFactory(fqcn);

    @Test public void newCity() {
        // arrange
        City sut = factory.newCity("Entenhausen", 1);
        // act
        // assert
        Assert.assertEquals("Entenhausen", sut.getName());
        Assert.assertEquals(1, sut.getRegion());
    }

    @Test public void newBoard() {
        // arrange
        Board sut = factory.newBoard(new EditionGermany());
        // act
        sut.close();
        // assert
        assertFalse(sut.getCities().isEmpty());
    }
}
