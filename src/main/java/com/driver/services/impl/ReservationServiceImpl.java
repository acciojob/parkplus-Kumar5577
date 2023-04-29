package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        SpotType spotType;
        if(numberOfWheels==2)
            spotType = SpotType.TWO_WHEELER;
        if(numberOfWheels==4)
            spotType = SpotType.FOUR_WHEELER;
        if(numberOfWheels>4)
            spotType = SpotType.OTHERS;
        else
            throw new Exception("Cannot make reservation");
        if(userRepository3.findById(userId).get()==null ||parkingLot == null)
            throw new Exception("Cannot make reservation");
       List<Spot>spots = parkingLot.getSpots();
       Spot spot = new Spot();
       for (Spot spot1:spots){
           if(spot1.getOccupied()==false && spot1.getSpotType()==spotType) {
               spot = spot1;
               break;
           }
           else
               throw new Exception("Cannot make reservation");
       }
       Reservation reservation = new Reservation();
       reservation.setNumberOfHours(timeInHours);
       reservation.setUser(userRepository3.findById(userId).get());
       reservation.setSpot(spot);
       spot.getReservations().add(reservation);
       User user = userRepository3.findById(userId).get();
       user.getReservations().add(reservation);
       spotRepository3.save(spot);
       userRepository3.save(user);
       return  reservation;



    }
}
