package com.example.demo.repositories;

import com.example.demo.models.TestRecordEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRecordRepository extends CrudRepository<TestRecordEntity, String> {
}
