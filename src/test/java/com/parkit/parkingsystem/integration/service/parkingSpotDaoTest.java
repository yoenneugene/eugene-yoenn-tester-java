package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.service.ParkingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

public class parkingSpotDaoTest {
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    @BeforeAll
    public static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;

        dataBasePrepareService = new DataBasePrepareService();
    }
    @BeforeEach
    public void setUpB() throws Exception {
        dataBasePrepareService.clearDataBaseEntries();
    }
    @Test
    public void getNextavAilableSlotTest() throws Exception {
        parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        assertEquals(1,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }
    @Test
    public void updateSlotTest() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        parkingSpotDAO.updateParking(parkingSpot);
        assertEquals(2,parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));

    }
}
