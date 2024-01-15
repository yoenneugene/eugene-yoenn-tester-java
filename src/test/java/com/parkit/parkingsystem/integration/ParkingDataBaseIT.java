package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;


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
       lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){

    }

    @Test
    public void testParkingACar() throws Exception {

        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFG");


        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle(ParkingType.CAR);

        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        assertEquals(1,ticketDAO.getNbTicket("ABCDEFG"));
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
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFG");
        parkingService.processExitingVehicle();
        Ticket ticket = new Ticket();
        ticket= ticketDAO.getTicket("ABCDEFG");
        assertEquals(0,ticket.getPrice());
        Date outTime = ticket.getOutTime();
        assertEquals(outTime,ticket.getOutTime());



        //TODO: check that the fare generated and out time are populated correctly in the database
    }
    @Test
    public void testParkingLotExitRecurringUser() throws Exception {
        try {

            testParkingACar();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFG");
        parkingService.processExitingVehicle();

        parkingService.processIncomingVehicle(ParkingType.CAR);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEFG");
        parkingService.processExitingVehicle();


        assertEquals(2,ticketDAO.getNbTicket("ABCDEFG"));





    }

}
