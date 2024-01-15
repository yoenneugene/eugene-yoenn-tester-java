package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());

        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct


        long minute = (outHour - inHour)/1000/60 ;
        if (minute < 30 ){
            minute = 0 ;
        }
        float duration = getDuration(minute);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                if (!discount) {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                }
                else {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR*0.95);
                }
                break;
            }
            case BIKE: {
                if (!discount) {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                }
                else{
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR*0.95);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

     //TODO: Ameliorer la methode
    private static float getDuration( long durationMin ) {
        long durationHours = durationMin / 60 ;
        float durationMinute = (float)durationMin;
        float durationminHours = durationMinute % 60 ;
        float durationPourcentageHeure = durationminHours / 60 ;
        float duration = durationHours+durationPourcentageHeure;
        return duration;
    }
}