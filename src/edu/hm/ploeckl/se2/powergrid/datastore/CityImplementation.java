package edu.hm.ploeckl.se2.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.City;
import java.util.Map;

public class CityImplementation implements City {
    private int region;
    private String name;

    public CityImplementation(String name, int region){
        this.name=name;
        this.region=region;
    }
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getRegion() {
        return this.region;
    }

    @Override
    public void connect(City to, int cost) {

    }

    @Override
    public Map<City, Integer> getConnections() {
        return null;
    }

    @Override
    public void close() {

    }
}
