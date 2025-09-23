package com.factory.backend.repository;

import com.factory.backend.dto.ProductivityDto;
import com.factory.backend.model.Batch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
//@DataJpaTest
public class BatchRepoTest {
    @Mock
    private BatchRepo batchRepo;

    @InjectMocks
    private BatchRepoTest batchRepoTest;

    @Before
    public void setup() {
        // This method can be used to set up any common test data or configurations
        // if needed before each test runs.
        Batch batch1 = new Batch();
        batch1.setBatch(1L);
        batch1.setProduct("NP-100");
        batch1.setOperator("John");
        batch1.setStart_time(LocalTime.of(8, 0));
        batch1.setEnd_time(LocalTime.of(16, 0));
        batch1.setDate(LocalDate.of(2023, 10, 1));

        Batch batch2 = new Batch();
        batch2.setBatch(2L);
        batch2.setProduct("NP-200");
        batch2.setOperator("Jane");
        batch2.setStart_time(LocalTime.of(9, 0));
        batch2.setEnd_time(LocalTime.of(17, 0));
        batch2.setDate(LocalDate.of(2023, 10, 2));

        Batch batch3 = new Batch();
        batch3.setBatch(3L);
        batch3.setProduct("NP-300");
        batch3.setOperator("John");
        batch3.setStart_time(LocalTime.of(8, 0));
        batch3.setEnd_time(LocalTime.of(16, 0));
        batch3.setDate(LocalDate.of(2023, 10, 1));

        when(batchRepo.findAllOperators()).thenReturn(List.of("John", "Jane"));
        when(batchRepo.findBatchByOperator("John")).thenReturn(List.of(batch1, batch3));
        when(batchRepo.findBatchByOperator("Jane")).thenReturn(List.of(batch2));
        when(batchRepo.findOperatorByName("John")).thenReturn("John");
        when(batchRepo.findOperatorByName("Jane")).thenReturn("Jane");
        long minutes = LocalTime.of(8, 0).until(LocalTime.of(16, 0), ChronoUnit.MINUTES);
        Time durationTime = new Time(minutes * 60 * 1000);
        when(batchRepo.findBatchByBatchId(1L))
                .thenReturn(new ProductivityDto(1L, "NP-100", "John", durationTime, 60));
    }

    @Test
    public void testFindAllOperators() {
        List<String> res = batchRepo.findAllOperators();

        assertThat(res).contains("John", "Jane");
        assertThat(res).hasSize(2);
    }

    @Test
    public void testFindBatchByOperator() {
        List<Batch> batches = batchRepo.findBatchByOperator("John");
        assertThat(batches).hasSize(2);
        assertThat(batches.get(0).getOperator()).isEqualTo("John");
        assertThat(batches.get(1).getOperator()).isEqualTo("John");
        assertThat(batches.get(0).getProduct()).isEqualTo("NP-100");
        assertThat(batches.get(1).getProduct()).isEqualTo("NP-300");
        assertThat(batches.get(0).getBatch()).isEqualTo(1L);
        assertThat(batches.get(1).getBatch()).isEqualTo(3L);
    }

    @Test
    public void testFindOperatorByName() {
        String operator = batchRepo.findOperatorByName("John");
        assertThat(operator).isEqualTo("John");
    }

    @Test
    public void testFindBatchByBatchId() {
        ProductivityDto productivity = batchRepo.findBatchByBatchId(1L);
        assertThat(productivity).isNotNull();
        assertThat(productivity.getProduct()).isEqualTo("NP-100");
        assertThat(productivity.getOperator()).isEqualTo("John");
        long minutes = LocalTime.of(8, 0).until(LocalTime.of(16, 0), ChronoUnit.MINUTES);
        Time durationTime = new Time(minutes * 60 * 1000);
        assertThat(productivity.getTotal_time()).isEqualTo(durationTime);
        assertThat(productivity.getMin_batch_time()).isEqualTo(60);

    }
}