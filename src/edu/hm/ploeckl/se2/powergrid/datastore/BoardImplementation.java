package edu.hm.ploeckl.se2.powergrid.datastore;

import edu.hm.cs.rs.powergrid.datastore.Board;
import edu.hm.cs.rs.powergrid.datastore.City;

import java.util.List;
import java.util.Set;
//git boch ein test
public class BoardImplementation implements Board {
    private Set<City> cities;

    public BoardImplementation(Set<City> cities){
        this.cities=cities;
    }
    @Override
    public void closeRegions(int remaining) {

    }

    @Override
    public City findCity(String name) {
        return null;
    }

    @Override
    public Set<City> getCities() {
        return this.cities;
    }

    @Override
    public void close() {

    }
}
