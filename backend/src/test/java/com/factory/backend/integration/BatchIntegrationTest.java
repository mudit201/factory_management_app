package com.factory.backend.integration;

import com.factory.backend.dto.BatchDto;
import com.factory.backend.repository.BatchRepo;
import com.factory.backend.model.Batch;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BatchIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BatchRepo batchRepo;

    private String baseUrl;

    @Before
    public void setup(){
        baseUrl = "http://localhost:" + port + "/api/batches";
    }

    @After
    public void tearDown() {
        // Clean up any data if necessary
//        restTemplate.delete(baseUrl);
        batchRepo.deleteAll();
    }

    @Test
    public void testGetAllBatches_Empty() throws Exception {
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
    @Test
    public void testGetAllBatches() throws Exception {
        // Add a batch to the database
        BatchDto batch = new BatchDto();
        batch.setProduct("CO-600");
        batch.setOperator("OperatorX");
        batch.setStart_time(LocalTime.of(8, 0));
        batch.setEnd_time(LocalTime.of(16, 0));

        ResponseEntity<String> postResponse = restTemplate.postForEntity(baseUrl, batch, String.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().size(), 1);
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testAddBatchForProductDoesNotExist() throws Exception {
        BatchDto batch = new BatchDto();
        batch.setProduct("TP-100");
        batch.setOperator("OperatorX");
        batch.setStart_time(LocalTime.of(8, 0));
        batch.setEnd_time(LocalTime.of(16, 0));
        batch.setDate(LocalDate.of(2025, 8, 4));

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, batch, String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetBatchById_NotFound() {
        ResponseEntity<BatchDto> response = restTemplate.getForEntity(baseUrl + "/9999", BatchDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddAndGetBatchById() {
        BatchDto batch = new BatchDto();
        batch.setProduct("CO-600");
        batch.setOperator("OperatorY");
        batch.setStart_time(LocalTime.of(9, 0));
        batch.setEnd_time(LocalTime.of(17, 0));
        batch.setDate(LocalDate.of(2025, 8, 5));

        ResponseEntity<String> postResponse = restTemplate.postForEntity(baseUrl, batch, String.class);
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

        List<Batch> batches = batchRepo.findAll();
        assertFalse(batches.isEmpty());
        Long batchId = batches.get(0).getBatch();

        ResponseEntity<BatchDto> getResponse = restTemplate.getForEntity(baseUrl + "/" + batchId, BatchDto.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("CO-600", getResponse.getBody().getProduct());
    }

    @Test
    public void testDeleteBatchById() {
        BatchDto batch = new BatchDto();
        batch.setProduct("CO-600");
        batch.setOperator("OperatorZ");
        batch.setStart_time(LocalTime.of(10, 0));
        batch.setEnd_time(LocalTime.of(18, 0));
        batch.setDate(LocalDate.of(2025, 8, 6));
        restTemplate.postForEntity(baseUrl, batch, String.class);

        List<Batch> batches = batchRepo.findAll();
        assertFalse(batches.isEmpty());
        Long batchId = batches.get(0).getBatch();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/batch/" + batchId,
                HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        assertFalse(batchRepo.findById(batchId).isPresent());
    }

    @Test
    public void testDeleteBatchById_NotFound() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/batch/9999",
                HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.NOT_FOUND, deleteResponse.getStatusCode());
    }

    @Test
    public void testPatchBatch() {
        BatchDto batch = new BatchDto();
        batch.setProduct("CO-600");
        batch.setOperator("OperatorPatch");
        batch.setStart_time(LocalTime.of(11, 0));
        batch.setEnd_time(LocalTime.of(19, 0));
        batch.setDate(LocalDate.of(2025, 8, 7));
        restTemplate.postForEntity(baseUrl, batch, String.class);

        List<Batch> batches = batchRepo.findAll();
        assertFalse(batches.isEmpty());
        Long batchId = batches.get(0).getBatch();

        Batch patch = new Batch();
        patch.setOperator("OperatorPatched");
        HttpEntity<Batch> requestEntity = new HttpEntity<>(patch);

        ResponseEntity<BatchDto> patchResponse = restTemplate.exchange(
                baseUrl + "/" + batchId, HttpMethod.PATCH, requestEntity, BatchDto.class);
        assertEquals(HttpStatus.OK, patchResponse.getStatusCode());
        assertEquals("OperatorPatched", patchResponse.getBody().getOperator());
    }

    @Test
    public void testGetBatchByOperator() {
        BatchDto batch = new BatchDto();
        batch.setProduct("CO-600");
        batch.setOperator("OperatorA");
        batch.setStart_time(LocalTime.of(12, 0));
        batch.setEnd_time(LocalTime.of(20, 0));
        batch.setDate(LocalDate.of(2025, 8, 8));
        restTemplate.postForEntity(baseUrl, batch, String.class);

        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/operators/OperatorA/batches", List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    public void testGetBatchByOperator_NotFound() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/operators/NonExistentOperator/batches", List.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddBatch_MissingRequiredFields() {
        BatchDto batch = new BatchDto();
        // Missing product, operator, start_time, end_time
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, batch, String.class);
        assertTrue(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
    }

}