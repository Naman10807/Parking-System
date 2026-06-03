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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:parkingdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ParkingControllerTest {

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
    void shouldRegisterVehicleEntrySuccessfully() throws Exception {
        ParkingSlotRequest slotRequest = new ParkingSlotRequest("A1", "CAR", SlotStatus.AVAILABLE);
        mockMvc.perform(post("/api/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(slotRequest)))
                .andExpect(status().isCreated());

        VehicleEntryRequest entryRequest = new VehicleEntryRequest("UP32AB1234", "Naman", "CAR");

        mockMvc.perform(post("/api/parking/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vehicleNumber").value("UP32AB1234"))
                .andExpect(jsonPath("$.slotNumber").value("A1"))
                .andExpect(jsonPath("$.entryTime").exists())
                .andExpect(jsonPath("$.message").value("Vehicle entered successfully"));
    }

    @Test
    void shouldReturnConflictWhenNoSlotAvailable() throws Exception {
        VehicleEntryRequest entryRequest = new VehicleEntryRequest("UP32AB1234", "Naman", "CAR");

        mockMvc.perform(post("/api/parking/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("No available parking slots at the moment"));
    }

    @Test
    void shouldRejectInvalidEntryRequest() throws Exception {
        mockMvc.perform(post("/api/parking/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void shouldReturnConflictWhenVehicleAlreadyParked() throws Exception {
        ParkingSlotRequest slotRequest = new ParkingSlotRequest("A1", "CAR", SlotStatus.AVAILABLE);
        mockMvc.perform(post("/api/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(slotRequest)));

        VehicleEntryRequest entryRequest = new VehicleEntryRequest("UP32AB1234", "Naman", "CAR");
        mockMvc.perform(post("/api/parking/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryRequest)));

        mockMvc.perform(post("/api/parking/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entryRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", is("Vehicle with number 'UP32AB1234' is already parked")));
    }

    @Test
    void shouldProcessVehicleExitSuccessfully() throws Exception {
        ParkingSlotRequest slotRequest = new ParkingSlotRequest("A1", "CAR", SlotStatus.AVAILABLE);
        mockMvc.perform(post("/api/slots")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(slotRequest)));

        VehicleEntryRequest entryRequest = new VehicleEntryRequest("UP32AB1234", "Naman", "CAR");
        mockMvc.perform(post("/api/parking/entry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entryRequest)));

        mockMvc.perform(post("/api/parking/exit/UP32AB1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleNumber").value("UP32AB1234"))
                .andExpect(jsonPath("$.slotNumber").value("A1"))
                .andExpect(jsonPath("$.entryTime").exists())
                .andExpect(jsonPath("$.exitTime").exists())
                .andExpect(jsonPath("$.durationInHours").value(1))
                .andExpect(jsonPath("$.parkingFee").value(20.0))
                .andExpect(jsonPath("$.message").value("Vehicle exited successfully"));
    }

    @Test
    void shouldReturnNotFoundWhenVehicleNotParked() throws Exception {
        mockMvc.perform(post("/api/parking/exit/UP32AB1234"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("No active parking record found for vehicle: UP32AB1234"));
    }
}
