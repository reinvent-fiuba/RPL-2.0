package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.repository.IOTestRepository;
import com.example.rpl.RPL.repository.UnitTestRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

    private IOTestRepository iOTestRepository;
    private UnitTestRepository unitTestsRepository;

    @Autowired
    public TestService(IOTestRepository iOTestRepository,
        UnitTestRepository unitTestsRepository) {
        this.iOTestRepository = iOTestRepository;
        this.unitTestsRepository = unitTestsRepository;
    }


    public List<IOTest> getAllIOTests(Long activityId) {
        return iOTestRepository.findAllByActivity_Id(activityId);
    }

    public Optional<UnitTest> getUnitTests(Long activityId) {
        return unitTestsRepository.findByActivity_Id(activityId);
    }
}
