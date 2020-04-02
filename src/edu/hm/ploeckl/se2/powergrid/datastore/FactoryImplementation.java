package edu.hm.ploeckl.se2.powergrid.datastore;

import edu.hm.cs.rs.powergrid.Edition;
import edu.hm.cs.rs.powergrid.datastore.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FactoryImplementation implements Factory {

    @Override
    public City newCity(String name, int region) {
        return new CityImplementation(name, region);
    }

    @Override
    public Player newPlayer(String secret, String color) {
        return new PlayerImplementaion(secret, color);
    }

    @Override
    public Plant newPlant(int number, Plant.Type type, int resources, int cities) {
        return new PlantImplementation(number, type, resources, cities);
    }

    @Override
    public PlantMarket newPlantMarket(Edition edition) {
        return null;
    }

    @Override
    public ResourceMarket newResourceMarket(Edition edition) {
        return null;
    }

    @Override
    public Board newBoard(Edition edition) {
        List<String> CityList = edition.getCitySpecifications();
        Set<City> cities = new HashSet<>();
        int regionCounter=0;
        for (String city : CityList) {
            String[] cityArray = city.split(" ");
            cities.add(newCity(cityArray[0], regionCounter));
            regionCounter++;
        }
        return new BoardImplementation(cities);
    }

    @Override
    public Game newGame(Edition edition) {
        return null;
    }
}
