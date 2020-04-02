package edu.hm.ploeckl.se2.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class BoardImplementation implements Board {
    private Set<City> cities = new HashSet<>();
    private boolean closed;

    @Override
    public void closeRegions(int remaining) {
        if(closed){
            throw new IllegalStateException();
        }
    }

    @Override
    public City findCity(String name) {
        for(City city : cities){
            if(city.getName().equals(name)){
                return city;
            }
        }
        return null;
    }

    @Override
    public Set<City> getCities() {
        if(closed){
            return Collections.unmodifiableSet(cities);
        } else {
            return this.cities;
        }
    }

    @Override
    public void close() {
        closed=true;
    }
}
