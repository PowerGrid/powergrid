package edu.hm.ploeckl.se2.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.Set;
public class PlantImplementation implements Plant {
    private final int number;
    private final Type type;
    private final int numberCities;
    private final int numberRessources;
    private Bag<Resource> resources;
    private boolean operated;

    public PlantImplementation(int number, Type type, int numberRessources, int numberCities){
        this.number = number;
        this.type = type;
        this.numberCities = numberCities;
        this.numberRessources = numberRessources;
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    @Override
    public int getCities() {
        return numberCities;
    }

    @Override
    public int getNumberOfResources() {
        return numberRessources;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean hasOperated() {
        return operated;
    }

    @Override
    public void setOperated(boolean operated) {
        this.operated=operated;
    }

    @Override
    public Set<Bag<Resource>> getResources() {
        throw new UnsupportedOperationException();
    }
}
