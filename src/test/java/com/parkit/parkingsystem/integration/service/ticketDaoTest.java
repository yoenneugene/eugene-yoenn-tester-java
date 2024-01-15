package com.parkit.parkingsystem.integration.service;

import com.google.protobuf.Any;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

public class ticketDaoTest {
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    private Ticket ticket;
    private static DataBasePrepareService dataBasePrepareService;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @AfterAll
    public static void tearDown(){

    }

    @BeforeAll
    public static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;

        dataBasePrepareService = new DataBasePrepareService();
    }
    @BeforeEach
    public void setUpB() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
    }
    @Test
    public void saveTickeTest() throws Exception {
        ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("test1");
        ticketDAO.saveTicket(ticket);
        assertEquals(1, ticketDAO.getNbTicket("test1"));


    }

    @Test
    public void getTicket() throws Exception {
        ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("test1");
        ticketDAO.saveTicket(ticket);
        ticket = new Ticket();
        ticket = ticketDAO.getTicket("test1");
        ticketDAO.getTicket("test1");
        assertEquals("test1", ticket.getVehicleRegNumber());


    }
@Test
    public void updateTicketTest() throws Exception {
    ticket = new Ticket();
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setVehicleRegNumber("test1");
    ticketDAO.saveTicket(ticket);
        ticket = new Ticket();
        ticket = ticketDAO.getTicket("test1");
        ticket.setPrice(14);
        ticketDAO.updateTicket(ticket);
        assertEquals(14, ticket.getPrice());


    }
    @Test
    public void getNbTicketTest() throws Exception {
        ticket = new Ticket();
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("test1");
        ticketDAO.saveTicket(ticket);
        ticketDAO.getNbTicket("test1");
        assertEquals(1, ticketDAO.getNbTicket("test1"));


    }

}
