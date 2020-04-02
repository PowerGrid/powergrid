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
        Board returnBoard = new BoardImplementation();

        //Add cities with name and region to the board
        for (String cityInfo : edition.getCitySpecifications()){
            String[] elements = cityInfo.split("\\s+");
            String name = elements[0];
            int region = Integer.parseInt(elements[1]);
            returnBoard.getCities().add(new CityImplementation(name, region));
        }

        //connect cities (with cost) on the board according to the specifications
        for (String cityInfo : edition.getCitySpecifications()){
            String[] elements = cityInfo.split("\\s+");
            String name = elements[0];
            City city = returnBoard.findCity(name);
            for (int count=2; count<elements.length; count=count+2){
                City connectingCity = returnBoard.findCity(elements[count]);
                int connectingCost = Integer.parseInt(elements[count+1]);
                city.connect(connectingCity, connectingCost);
            }
        }
        return returnBoard;
    }

    @Override
    public Game newGame(Edition edition) {
        return null;
    }
}
