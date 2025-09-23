package com.factory.backend.controller;

import com.factory.backend.dto.BatchDto;
import com.factory.backend.dto.OperatorDto;
import com.factory.backend.model.Batch;
import com.factory.backend.service.BatchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class BatchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BatchService batchService;

    @InjectMocks
    private BatchController batchController;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(batchController).build();
    }

    @Test
    public void testGetAllBatches() throws Exception {
        BatchDto dto = new BatchDto();
        dto.setBatch(1L);
        dto.setProduct("TP-100");
        dto.setOperator("OperatorX");
        dto.setStart_time(LocalTime.of(8, 0));
        dto.setEnd_time(LocalTime.of(16, 0));
        dto.setDate(LocalDate.of(2025, 8, 4));

        when(batchService.getAllBatches()).thenReturn(List.of(dto));


        mockMvc.perform(get("/api/batches")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].product").value("TP-100"))
                .andExpect(jsonPath("$[0].batch").value(1L))
                .andExpect(jsonPath("$[0].operator").value("OperatorX"));
    }

    @Test
    public void testGetBatchById() throws Exception {
        Long batchId = 1L;
        BatchDto dto = new BatchDto();
        dto.setBatch(batchId);
        dto.setProduct("TP-100");
        dto.setOperator("OperatorX");
        dto.setStart_time(LocalTime.of(8, 0));
        dto.setEnd_time(LocalTime.of(16, 0));
        dto.setDate(LocalDate.of(2025, 8, 4));

        when(batchService.getBatchById(batchId)).thenReturn(dto);

        try {
            mockMvc.perform(get("/api/batches/{batch}", batchId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.product").value("TP-100"))
                    .andExpect(jsonPath("$.batch").value(batchId))
                    .andExpect(jsonPath("$.operator").value("OperatorX"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred while testing getBatchById: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateBatchById() throws Exception {
        Long batchId = 1L;

        BatchDto existingDto = new BatchDto();
        existingDto.setBatch(batchId);
        existingDto.setProduct("OldProduct");
        existingDto.setOperator("OldOperator");
        existingDto.setStart_time(LocalTime.of(8, 0));
        existingDto.setEnd_time(LocalTime.of(16, 0));
        existingDto.setDate(LocalDate.of(2025, 8, 1));

        BatchDto updatedDto = new BatchDto();
        updatedDto.setBatch(batchId);
        updatedDto.setProduct("NewProduct");
        updatedDto.setOperator("NewOperator");
        updatedDto.setStart_time(LocalTime.of(9, 0));
        updatedDto.setEnd_time(LocalTime.of(17, 0));
        updatedDto.setDate(LocalDate.of(2025, 8, 1));

        Mockito.when(batchService.getBatchById(batchId)).thenReturn(existingDto);
        Mockito.when(batchService.saveBatch(Mockito.any(BatchDto.class))).thenReturn(updatedDto);

        String jsonPatch = """
        {
            "product": "NewProduct",
            "operator": "NewOperator",
            "start_time": "09:00",
            "end_time": "17:00"
        }
        """;

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/batches/{batch}", batchId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPatch))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("NewProduct"))
                .andExpect(jsonPath("$.operator").value("NewOperator"))
                .andExpect(jsonPath("$.start_time[0]").value(9))
                .andExpect(jsonPath("$.start_time[1]").value(0))
                .andExpect(jsonPath("$.end_time[0]").value(17))
                .andExpect(jsonPath("$.end_time[1]").value(0));
    }


    @Test
    public void testAddNewBatch() throws Exception {
        String jsonRequest = """
        {
          "product": "ProductA",
          "operator": "Operator1",
          "start_time": "08:00:00",
          "end_time": "16:00:00",
          "date": "2025-08-04"
        }
        """;

        Mockito.when(batchService.addBatchForExistingProduct(Mockito.any(Batch.class))).thenReturn("Success");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/batches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().string("Added new batch"));
    }

    @Test
    public void testAddNewBatch_ProductNotExist() throws Exception {
        String jsonRequest = """
        {
          "product": "UnknownProduct",
          "operator": "Operator1",
          "start_time": "08:00:00",
          "end_time": "16:00:00"
        }
        """;

        Mockito.when(batchService.addBatchForExistingProduct(Mockito.any(Batch.class))).thenReturn("Product does not exist");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/batches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteById() {
    }

    @Test
    public void testGetAllOperators() {
        OperatorDto opDto1 = new OperatorDto("Operator1");
        OperatorDto opDto2 = new OperatorDto("Operator2");
        OperatorDto opDto3 = new OperatorDto("Operator3");

        when(batchService.getAllOperator()).thenReturn(List.of(opDto1, opDto2, opDto3));

        try {
            mockMvc.perform(get("/api/operators")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0]").value(opDto1))
                    .andExpect(jsonPath("$[1]").value(opDto2))
                    .andExpect(jsonPath("$[2]").value(opDto3));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred while testing getAllOperators: " + e.getMessage());
        }
    }

    @Test
    public void testGetBatchByOperator() {
        String operatorName = "Operator1";
        BatchDto batchDto1 = new BatchDto();
        batchDto1.setBatch(1L);
        batchDto1.setProduct("TP-100");
        batchDto1.setOperator(operatorName);
        batchDto1.setStart_time(LocalTime.of(8, 0));
        batchDto1.setEnd_time(LocalTime.of(16, 0));
        batchDto1.setDate(LocalDate.of(2025, 8, 4));

        BatchDto batchDto2 = new BatchDto();
        batchDto2.setBatch(2L);
        batchDto2.setProduct("TP-200");
        batchDto2.setOperator(operatorName);
        batchDto2.setStart_time(LocalTime.of(9, 0));
        batchDto2.setEnd_time(LocalTime.of(17, 0));
        batchDto2.setDate(LocalDate.of(2025, 8, 5));

        when(batchService.findOperator(operatorName)).thenReturn(true);
        when(batchService.getBatchByOperator(operatorName)).thenReturn(List.of(batchDto1, batchDto2));

        try {
            mockMvc.perform(get("/api/operators/{operatorName}/batches", operatorName)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].operator").value(operatorName))
                    .andExpect(jsonPath("$[1].operator").value(operatorName));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred while testing getBatchByOperator: " + e.getMessage());
        }
    }

    @Test
    public void testGetProductivity() {
    }
}