package com.example.demo.services;

import com.example.demo.models.TestRecord;
import com.example.demo.models.TestRecordEntity;
import com.example.demo.repositories.TestRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestRecordsService {


    private final TestRecordRepository repository;
    private final KafkaTemplate<String, TestRecord> kafkaTemplate;

    @Transactional
    @SneakyThrows
    public String save(TestRecord testRecord) {
        var saved = repository.save(new TestRecordEntity(null, testRecord.name()));
        var forSave = new TestRecord(saved.getId(), saved.getName());
        log.info("Saved test record [{}]", forSave);
        kafkaTemplate.send("kafka-topic-with-compression", forSave).get(2, TimeUnit.SECONDS);
        return forSave.id();
    }

}
