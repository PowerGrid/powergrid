package edu.hm.ploeckl.se2.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Bag;
import edu.hm.cs.rs.powergrid.datastore.City;
import edu.hm.cs.rs.powergrid.datastore.Plant;
import edu.hm.cs.rs.powergrid.datastore.Player;
import edu.hm.cs.rs.powergrid.datastore.Resource;

import java.util.Set;

public class PlayerImplementaion implements Player {
    private final String color;
    private final String secret;
    private int electro;
    private boolean passed;
    private boolean showSecret=true;
    private Set<City> cities;
    private Set<Plant> plants;

    public PlayerImplementaion(String secret, String color){
        this.secret=secret;
        this.color=color;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public Set<City> getCities() {
        return cities;
    }

    @Override
    public Set<Plant> getPlants() {
        return plants;
    }

    @Override
    public Bag<Resource> getResources() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getElectro() {
        return this.electro;
    }

    @Override
    public void setElectro(int electro) {
        this.electro=electro;
    }

    @Override
    public boolean hasPassed() {
        return passed;
    }

    @Override
    public void setPassed(boolean passed) {
        this.passed=passed;
    }

    @Override
    public String getSecret() {
        if(showSecret) {
            showSecret=false;
            return secret;
        }else{
            return null;
        }
    }

    @Override
    public boolean hasSecret(String secret) {
        return secret.equals(this.secret);
    }
}
