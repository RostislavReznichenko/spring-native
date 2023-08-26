package com.example.demo.api;

import com.example.demo.models.TestRecord;
import com.example.demo.services.TestRecordsService;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final TestRecordsService testRecordsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Observed(
        name = "test.name",
        contextualName = "test-name"
    )
    public Map<String, String> test(@RequestBody TestRecord object) {
        log.info("POST called with req body [{}]", object);
        var savedId = testRecordsService.save(object);
        return Map.of("id", String.valueOf(savedId));
    }

    @ExceptionHandler
    ProblemDetail handle(Throwable throwable) {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail(throwable.getMessage());
        return problemDetail;
    }
}
