package edu.hm.ploeckl.se2.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.City;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CityImplementation implements City {
    private int region;
    private String name;
    private boolean closed;
    private Map<City, Integer> connections = new HashMap<>();

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
        if(closed){
            throw new IllegalStateException();
        } else if(connections.containsKey(to)) {
            throw new IllegalArgumentException();
        } else {
            connections.put(to, cost);
        }
    }

    @Override
    public Map<City, Integer> getConnections() {
        if(closed){
            return Collections.unmodifiableMap(connections);
        }else {
            return connections;
        }
    }

    @Override
    public void close() {
        closed=true;
    }
}
