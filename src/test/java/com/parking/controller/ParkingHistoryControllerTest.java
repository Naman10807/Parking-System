package com.parking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.dto.request.ParkingSlotRequest;
import com.parking.dto.request.VehicleEntryRequest;
import com.parking.entity.SlotStatus;
import com.parking.repository.ParkingRecordRepository;
import com.parking.repository.ParkingSlotRepository;
import com.parking.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:historymoduledb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ParkingHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingRecordRepository parkingRecordRepository;

    @BeforeEach
    void setUp() {
        parkingRecordRepository.deleteAll();
        vehicleRepository.deleteAll();
        parkingSlotRepository.deleteAll();
    }

    @Test
    void shouldReturnAllHistorySortedByLatestEntry() throws Exception {
        createAndExitVehicle("UP32AB1234");

        mockMvc.perform(get("/api/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].recordId").exists())
                .andExpect(jsonPath("$[0].vehicleNumber").value("UP32AB1234"))
                .andExpect(jsonPath("$[0].ownerName").value("Naman"))
                .andExpect(jsonPath("$[0].vehicleType").value("CAR"))
                .andExpect(jsonPath("$[0].slotNumber").value("A1"))
                .andExpect(jsonPath("$[0].entryTime").exists())
                .andExpect(jsonPath("$[0].exitTime").exists())
                .andExpect(jsonPath("$[0].parkingFee").value(20.0));
    }

    @Test
    void shouldReturnHistoryByVehicleNumber() throws Exception {
        createAndExitVehicle("UP32AB1234");

        mockMvc.perform(get("/api/history/vehicle/UP32AB1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vehicleNumber").value("UP32AB1234"));
    }

    @Test
    void shouldReturnHistoryBetweenDates() throws Exception {
        createAndExitVehicle("UP32AB1234");

        mockMvc.perform(get("/api/history/date-range")
                        .param("startDate", "2020-01-01")
                        .param("endDate", "2099-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldRejectInvalidDateRange() throws Exception {
        mockMvc.perform(get("/api/history/date-range")
                        .param("startDate", "2026-06-30")
                        .param("endDate", "2026-06-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Start date must not be after end date")));
    }

    private void createAndExitVehicle(String vehicleNumber) throws Exception {
        ParkingSlotRequest slotRequest = new ParkingSlotRequest("A1", "CAR", SlotStatus.AVAILABLE);
        mockMvc.perform(post("/api/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(slotRequest)));

        VehicleEntryRequest entryRequest = new VehicleEntryRequest(vehicleNumber, "Naman", "CAR");
        mockMvc.perform(post("/api/parking/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryRequest)));

        mockMvc.perform(post("/api/parking/exit/" + vehicleNumber));
    }
}
