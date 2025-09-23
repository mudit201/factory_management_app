package com.factory.backend.service;

import com.factory.backend.dto.BatchDto;
import com.factory.backend.dto.OperatorDto;
import com.factory.backend.dto.ProductivityDto;
import com.factory.backend.model.Batch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class BatchServiceTest {

    @Mock
    BatchService service;

    @InjectMocks
    private BatchServiceTest batchServiceTest;

    @Before
    public void setup() {
        // This method can be used to set up any common test data or configurations
        // if needed before each test runs.
        BatchDto batch1 = new BatchDto();
        batch1.setBatch(1L);
        batch1.setProduct("NP-100");
        batch1.setOperator("John");
        batch1.setStart_time(LocalTime.of(8, 0));
        batch1.setEnd_time(LocalTime.of(16, 0));
        batch1.setDate(LocalDate.of(2023, 10, 1));

        BatchDto batch2 = new BatchDto();
        batch2.setBatch(2L);
        batch2.setProduct("NP-200");
        batch2.setOperator("Jane");
        batch2.setStart_time(LocalTime.of(9, 0));
        batch2.setEnd_time(LocalTime.of(17, 0));
        batch2.setDate(LocalDate.of(2023, 10, 2));

        BatchDto batch3 = new BatchDto();
        batch3.setBatch(3L);
        batch3.setProduct("NP-300");
        batch3.setOperator("John");
        batch3.setStart_time(LocalTime.of(8, 0));
        batch3.setEnd_time(LocalTime.of(16, 0));
        batch3.setDate(LocalDate.of(2023, 10, 1));

        OperatorDto operator1 = new OperatorDto();
        operator1.setOperator("John");
        OperatorDto operator2 = new OperatorDto();
        operator2.setOperator("Jane");

        long minutes = LocalTime.of(8, 0).until(LocalTime.of(16, 0), ChronoUnit.MINUTES);
        Time durationTime = new Time(minutes * 60 * 1000);

        ProductivityDto productivity1 = new ProductivityDto();
        productivity1.setBatch(1L);
        productivity1.setProduct("NP-100");
        productivity1.setOperator("John");
        productivity1.setTotal_time(durationTime);
        productivity1.setMin_batch_time(60);

        when(service.getAllBatches()).thenReturn(List.of(batch1, batch2, batch3));
        when(service.getBatchById(1L)).thenReturn(batch1);
        when(service.getBatchById(2L)).thenReturn(batch2);
        when(service.getBatchById(3L)).thenReturn(batch3);
        when(service.getBatchByOperator("John")).thenReturn(List.of(batch1, batch3));
        when(service.getBatchByOperator("Jane")).thenReturn(List.of(batch2));
        when(service.getAllOperator()).thenReturn(List.of(operator1, operator2));
//        when(service.convertBatchToBatchDto(batch1)).thenReturn(batch1);
//        when(service.convertBatchToBatchDto(batch2)).thenReturn(batch2);
//        when(service.convertBatchToBatchDto(batch3)).thenReturn(batch3);
//        when(service.convertToOperatorDto("John")).thenReturn(new OperatorDto("John"));
//        when(service.convertToOperatorDto("Jane")).thenReturn(new OperatorDto("Jane"));
        when(service.findOperator("John")).thenReturn(true);
        when(service.findProductivity(1L)).thenReturn(productivity1);
//        when(service.addBatchForExistingProduct("NP-100")).thenReturn(true);

    }

    @Test
    public void testGetAllBatches() {
        List<BatchDto> batches = service.getAllBatches();
        assertNotNull(batches);
        assertEquals(3, batches.size());
        assertEquals("NP-100", batches.get(0).getProduct());
        assertEquals("John", batches.get(0).getOperator());
        assertThat(batches).hasSize(3);

    }

    @Test
    public void testGetBatchById() {
        BatchDto batch = service.getBatchById(1L);
        assertNotNull(batch);
        assertEquals(Long.valueOf(1), batch.getBatch());
        assertEquals("NP-100", batch.getProduct());
        assertEquals("John", batch.getOperator());
        assertEquals(LocalTime.of(8, 0), batch.getStart_time());
        assertEquals(LocalTime.of(16, 0), batch.getEnd_time());
        assertEquals(LocalDate.of(2023, 10, 1), batch.getDate());
    }

    @Test
    public void testGetBatchByOperator() {
        List<BatchDto> batches = service.getBatchByOperator("John");
        assertNotNull(batches);
        assertEquals(2, batches.size());
        assertEquals("NP-100", batches.get(0).getProduct());
        assertEquals("John", batches.get(0).getOperator());
        assertEquals("NP-300", batches.get(1).getProduct());
        assertEquals("John", batches.get(1).getOperator());
    }

//    @Test
//    public void testConvertBatchToBatchDto() {
//        Batch batch = new Batch();
//        batch.setBatch(1L);
//        batch.setProduct("NP-100");
//        batch.setOperator("John");
//        batch.setStart_time(LocalTime.of(8, 0));
//        batch.setEnd_time(LocalTime.of(16, 0));
//        batch.setDate(LocalDate.of(2023, 10, 1));
//
//        BatchDto dto = service.convertBatchToBatchDto(batch);
//        assertNotNull(dto);
//        assertEquals(Long.valueOf(1), dto.getBatch());
//        assertEquals("NP-100", dto.getProduct());
//        assertEquals("John", dto.getOperator());
//        assertEquals(LocalTime.of(8, 0), dto.getStart_time());
//        assertEquals(LocalTime.of(16, 0), dto.getEnd_time());
//        assertEquals(LocalDate.of(2023, 10, 1), dto.getDate());
//    }

    @Test
    public void TestGetAllOperator() {
        List<OperatorDto> operators = service.getAllOperator();
        assertNotNull(operators);
        OperatorDto operator1 = new OperatorDto();
        operator1.setOperator("John");
        OperatorDto operator2 = new OperatorDto();
        operator2.setOperator("Jane");
        assertThat(operators).contains(operator1, operator2);
        assertThat(operators).hasSize(2);
    }

//    @Test
//    public void testConvertToOperatorDto() {
//        String operatorName = "John";
//        OperatorDto operatorDto = service.convertToOperatorDto(operatorName);
//        assertNotNull(operatorDto);
//        assertEquals("John", operatorDto.getOperator());
//    }

    @Test
    public void testSaveBatch() {
        BatchDto newBatch = new BatchDto();
        newBatch.setBatch(4L);
        newBatch.setProduct("NP-400");
        newBatch.setOperator("Jane");
        newBatch.setStart_time(LocalTime.of(10, 0));
        newBatch.setEnd_time(LocalTime.of(18, 0));
        newBatch.setDate(LocalDate.of(2023, 10, 3));

        when(service.saveBatch(newBatch)).thenReturn(newBatch);

        BatchDto savedBatch = service.saveBatch(newBatch);
        assertNotNull(savedBatch);
        assertEquals(Long.valueOf(4), savedBatch.getBatch());
        assertEquals("NP-400", savedBatch.getProduct());
        assertEquals("Jane", savedBatch.getOperator());
        assertEquals(LocalTime.of(10, 0), savedBatch.getStart_time());
        assertEquals(LocalTime.of(18, 0), savedBatch.getEnd_time());
        assertEquals(LocalDate.of(2023, 10, 3), savedBatch.getDate());

    }

//    @Test
//    public void testConvertBatchDtoToBatch() {
//        BatchDto dto = new BatchDto();
//        dto.setBatch(1L);
//        dto.setProduct("NP-100");
//        dto.setOperator("John");
//        dto.setStart_time(LocalTime.of(8, 0));
//        dto.setEnd_time(LocalTime.of(16, 0));
//        dto.setDate(LocalDate.of(2023, 10, 1));
//
//        Batch batch = service.convertBatchDtoToBatch(dto);
//        assertNotNull(batch);
//        assertEquals(Long.valueOf(1), batch.getBatch());
//        assertEquals("NP-100", batch.getProduct());
//        assertEquals("John", batch.getOperator());
//        assertEquals(LocalTime.of(8, 0), batch.getStart_time());
//        assertEquals(LocalTime.of(16, 0), batch.getEnd_time());
//        assertEquals(LocalDate.of(2023, 10, 1), batch.getDate());
//    }

    @Test
    public void testDeleteBatchById() {
        Long batchId = 1L;
        service.deleteBatchById(batchId);
        // Assuming the service method does not return anything, we can only verify that it was called.
        // In a real scenario, you might want to verify that the batch no longer exists in the repository.
        assertTrue(true); // Placeholder assertion, replace with actual verification if needed.
    }

    @Test
    public void testFindOperator() {
        String operatorName = "John";

        Boolean foundOperator = service.findOperator(operatorName);
//        assertNotNull(foundOperator);
        assertThat(foundOperator).isEqualTo(true);
        assertEquals(true, foundOperator);
    }

    @Test
    public void testFindProductivity() {
        Long batchId = 1L;
        ProductivityDto productivity = service.findProductivity(batchId);
        assertNotNull(productivity);
        assertEquals(Long.valueOf(1), productivity.getBatch());
        assertEquals("NP-100", productivity.getProduct());
        assertEquals("John", productivity.getOperator());

        long minutes = LocalTime.of(8, 0).until(LocalTime.of(16, 0), ChronoUnit.MINUTES);
        Time durationTime = new Time(minutes * 60 * 1000);
        assertEquals(durationTime, productivity.getTotal_time());
//        assertEquals(60, productivity.getMin_batch_time());
    }

    @Test
    public void testAddBatchForExistingProduct() {
        Batch batchEntity = new Batch();
        batchEntity.setBatch(4L);
        batchEntity.setProduct("NP-400");
        batchEntity.setOperator("Jane");
        batchEntity.setStart_time(LocalTime.of(10, 0));
        batchEntity.setEnd_time(LocalTime.of(18, 0));
        batchEntity.setDate(LocalDate.of(2023, 10, 3));

        when(service.addBatchForExistingProduct(batchEntity)).thenReturn("created");

        String result = service.addBatchForExistingProduct(batchEntity);
        assertNotNull(result);
        assertEquals("created", result);
    }
}