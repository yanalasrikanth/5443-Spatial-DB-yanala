package com.battleship.game.service;

import com.battleship.game.model.PolygonCoordinates;
import com.battleship.game.repository.CardinalRepository;
import com.battleship.game.repository.MovingShipRepository;
import com.battleship.game.repository.ShipDetailsRepository;
import com.battleship.game.repository.ShipRepository;
import com.battleship.game.response.BattleShipResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ShipLocationGenerateServiceImpl implements ShipLocationGenerateService {
    long degrees = createRandomNumber();

    int index = 0;

    @Autowired
    CardinalRepository cardinalRepository;

    @Autowired
    ShipRepository shipRepository;

    @Autowired
    ShipDetailsRepository shipDetailsRepository;

    @Autowired
    MovingShipRepository movingShipRepository;

    private double region_zero = 0;
    private double region_one = 0;
    private double region_two = 0;
    private double region_three = 0;

    String[] cardinalList = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
    double[] cardinalDegree = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};
    double[] cardinalMax = {348.75, 11.25, 33.75, 56.25, 78.75, 101.25, 123.75, 146.25, 168.75, 191.25, 213.75, 236.25, 258.75, 281.25, 303.75, 326.25};
    double[] cardinalMin = {11.25, 33.75, 56.25, 78.75, 101.25, 123.75, 146.25, 168.75, 191.25, 213.75, 236.25, 258.75, 281.25, 303.75, 326.25, 348.75};

    public static long createRandomNumber(){
        double val = Math.random() * 360;
        long degrees = (long)val;
        return degrees;

    }
    public BattleShipResponse generateShipLocation() {

        degrees = (int) ((float) degrees);
        index = (int) ((degrees + 11.25) / 22.5);
        String direction = cardinalList[index % 16];

        //used to place boats facing opposite direction to the edge of the map
        String opposite = cardinalList[(index + 8) % 16];
        double oppositeBearing = cardinalDegree[(index + 8) % 16];

        //left and rightmost of area
        double left = cardinalMax[index % 16];
        double right = cardinalMin[index % 16];

        cardinalRepository.findBoundingBox(2);
        cardinalRepository.findCenterPoint("");
        cardinalRepository.findextremesShipRegionP1("", "", "");
        cardinalRepository.findextremesShipRegionRightP2("", "", "");
        cardinalRepository.findCenterAndExtremes("", "", "");
        cardinalRepository.findBoatsAroundBackBoat("");

        int number_Of_Boats = shipRepository.noOfBoatsCount();


        for (int m = 4; m <= number_Of_Boats; m++) {
            if (m % 4 == 0) {
                PolygonCoordinates polygonCoordinates = movingShipRepository.movingRadiansPosition("");
                polygonCoordinates.getCoordinates().forEach(item ->
                        this.region_one = item
                );
                movingShipRepository.updateshipMovingPosition(region_one, m);

            } else if (m % 4 == 1) {
                PolygonCoordinates polygonCoordinates = movingShipRepository.movingRadiansPosition("");
                polygonCoordinates.getCoordinates().forEach(item ->
                        this.region_two = item
                );
                movingShipRepository.updateshipMovingPosition(region_two, m);
            } else if (m % 4 == 2) {
                PolygonCoordinates polygonCoordinates = movingShipRepository.movingRadiansPosition("");
                polygonCoordinates.getCoordinates().forEach(item ->
                        this.region_three = item
                );
                movingShipRepository.updateshipMovingPosition(region_three, m);
            } else {
                PolygonCoordinates polygonCoordinates = movingShipRepository.movingRadiansPosition("");
                polygonCoordinates.getCoordinates().forEach(item ->
                        this.region_zero = item
                );
                movingShipRepository.updateshipMovingPosition(region_three, m);
            }
        }

        BattleShipResponse battleShipResponse = shipDetailsRepository.getBattleshipDetails();

        return battleShipResponse;
    }
}
