package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.IOTestRun;
import com.example.rpl.RPL.model.TestRun;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.repository.IOTestRepository;
import com.example.rpl.RPL.repository.IOTestRunRepository;
import com.example.rpl.RPL.repository.UnitTestRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TestService {

    private IOTestRepository iOTestRepository;
    private UnitTestRepository unitTestsRepository;
    private IOTestRunRepository iOTestRunRepository;

    @Autowired
    public TestService(IOTestRepository iOTestRepository,
        UnitTestRepository unitTestsRepository,
        IOTestRunRepository iOTestRunRepository) {
        this.iOTestRepository = iOTestRepository;
        this.unitTestsRepository = unitTestsRepository;
        this.iOTestRunRepository = iOTestRunRepository;
    }


    public List<IOTest> getAllIOTests(Long activityId) {
        return iOTestRepository.findAllByActivityId(activityId);
    }

    public UnitTest getUnitTests(Long activityId) {
        return unitTestsRepository.findByActivityId(activityId).orElse(null);
    }

    /**
     * *
     *
     * @param activityId Activity to grade
     * @param testRun the run with the stdout of test run WITH LOGGING
     * @return list of IOTestRun entities
     */
    @Transactional
    public List<IOTestRun> parseAndSaveStdout(Long activityId, TestRun testRun) {
        List<String> results = this.parseTestRunStdout(testRun.getStdout());
        List<IOTest> ioTests = this.getAllIOTests(activityId);

        List<IOTestRun> ioTestRuns = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            IOTestRun ioTestRun = new IOTestRun(testRun, ioTests.get(i).getTestIn(),
                ioTests.get(i).getTestOut(), results.get(i));
            ioTestRuns.add(ioTestRun);
        }

        return iOTestRunRepository.saveAll(ioTestRuns);
    }

    /**
     * Checks if all the tests passed.
     *
     * @return if all the tests passed
     */
    boolean checkIfTestsPassed(Long activityId, List<IOTestRun> ioTestRuns) {
        List<IOTest> ioTests = this.getAllIOTests(activityId);

        // Not all tests were run
        if (ioTestRuns.size() != ioTests.size()) {
            return false;
        }
        for (IOTestRun ioTestRun : ioTestRuns) {
            if (!ioTestRun.getExpectedOutput().equals(ioTestRun.getRunOutput())) {
                log.error("EXPECTED: {}\n RUN: {}", ioTestRun.getExpectedOutput(),
                    ioTestRun.getRunOutput());
                return false;
            }
        }
        return true;
    }


    /**
     * Parses the stdout of the test run to identify ONLY the student's program output. these are
     * identified by the tags start_RUN and end_RUN.
     *
     * @param testRunStdout stdout of the tun INCLUDING the logging
     * @return list of test run's output. May be various runs in case of IO testing with multiple
     * cases.
     */
    private List<String> parseTestRunStdout(String testRunStdout) {
        List<String> results = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        for (String line : testRunStdout.split("\n")) {
            if (line.contains("end_RUN")) {
                results.add(result.toString().substring(0, result.length() - 1 )); // removing last /n as it was an EOF originally
            } else if (line.contains("start_RUN")) {
                result = new StringBuilder();
            } else if (line.contains("assignment_main.py") || line.contains("./main")) {
            } else {
                result.append(line).append("\n");
            }
        }
        return results;
    }

    @Transactional
    public IOTest createIOTest(Long activityId, String in, String out) {
        IOTest ioTest = new IOTest(activityId, in, out);

        return iOTestRepository.save(ioTest);
    }

    @Transactional
    public IOTest updateUnitTest(Long ioTestId, String in, String out) {
        IOTest ioTest = iOTestRepository.findById(ioTestId)
            .orElseThrow(() -> new NotFoundException("IO test not found",
                "iotest_not_found"));

        ioTest.update(in, out);

        return iOTestRepository.save(ioTest);
    }

    @Transactional
    public void deleteUnitTest(Long ioTestId) {
        iOTestRepository.deleteById(ioTestId);
    }


}
