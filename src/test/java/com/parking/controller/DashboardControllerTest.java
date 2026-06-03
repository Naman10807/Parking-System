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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:dashboarddb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class DashboardControllerTest {

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
    void shouldReturnDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSlots").value(0))
                .andExpect(jsonPath("$.availableSlots").value(0))
                .andExpect(jsonPath("$.occupiedSlots").value(0))
                .andExpect(jsonPath("$.activeVehicles").value(0))
                .andExpect(jsonPath("$.totalVehiclesParked").value(0))
                .andExpect(jsonPath("$.totalRevenue").value(0));
    }

    @Test
    void shouldReflectParkingActivityInDashboardSummary() throws Exception {
        ParkingSlotRequest slotA = new ParkingSlotRequest("A1", "CAR", SlotStatus.AVAILABLE);
        ParkingSlotRequest slotB = new ParkingSlotRequest("B1", "CAR", SlotStatus.AVAILABLE);

        mockMvc.perform(post("/api/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(slotA)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(slotB)))
                .andExpect(status().isCreated());

        VehicleEntryRequest entryRequest = new VehicleEntryRequest("UP32AB1234", "Naman", "CAR");
        mockMvc.perform(post("/api/parking/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSlots").value(2))
                .andExpect(jsonPath("$.availableSlots").value(1))
                .andExpect(jsonPath("$.occupiedSlots").value(1))
                .andExpect(jsonPath("$.activeVehicles").value(1))
                .andExpect(jsonPath("$.totalVehiclesParked").value(1))
                .andExpect(jsonPath("$.totalRevenue").value(0));

        mockMvc.perform(post("/api/parking/exit/UP32AB1234"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSlots").value(2))
                .andExpect(jsonPath("$.availableSlots").value(2))
                .andExpect(jsonPath("$.occupiedSlots").value(0))
                .andExpect(jsonPath("$.activeVehicles").value(0))
                .andExpect(jsonPath("$.totalVehiclesParked").value(1))
                .andExpect(jsonPath("$.totalRevenue").value(20.0));
    }
}
