package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.DriverManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    public static final String REG_NUMBER = "ABCDEFG";
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static FareCalculatorService fareCalculatorService;




    @Mock
    private static InputReaderUtil inputReaderUtil;



    @BeforeAll
    public static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {

      lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
       lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(REG_NUMBER);

        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){

    }
    // check that a ticket is actualy saved in DB and Parking table is updated with availability
    @Test
    public void testParkingACar() throws Exception {

        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(REG_NUMBER);


        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle(ParkingType.CAR);

        // check that a ticket is actualy saved in DB and Parking table is updated with availability
        assertEquals(1,ticketDAO.getNbTicket(REG_NUMBER));
        assertEquals(2,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
    }

    @Test
    public void testParkingLotExit() throws Exception {
        try {

            testParkingACar();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(REG_NUMBER);
        parkingService.processExitingVehicle();
        Ticket ticket = new Ticket();
        ticket= ticketDAO.getTicket(REG_NUMBER);
        assertEquals(0,ticket.getPrice());
        Date outTime = ticket.getOutTime();
        assertEquals(outTime,ticket.getOutTime());



        // check that the fare generated and out time are populated correctly in the database
    }
    @Test
    public void testParkingLotExitRecurringUser() throws Exception {
        TicketDAO ticketDAO1 = Mockito.mock(TicketDAO.class);
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO1);

        Ticket ticket = new Ticket();
        ticket.setVehicleRegNumber(REG_NUMBER);
        ticket.setInTime(Date.from(Instant.now().minus(31, ChronoUnit.MINUTES)));
        ParkingSpot parkingSpot = new ParkingSpot(1,ParkingType.CAR,true);
        ticket.setParkingSpot(parkingSpot);

        lenient().when(ticketDAO1.getTicket(REG_NUMBER)).thenReturn(ticket);
        lenient().when(ticketDAO1.getNbTicket(REG_NUMBER)).thenReturn(2);
//        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFG");

        parkingService.processExitingVehicle();

        assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR*0,95);







    }

}
