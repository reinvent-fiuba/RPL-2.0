package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.repository.IOTestRepository;
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

    @Autowired
    public TestService(IOTestRepository iOTestRepository,
        UnitTestRepository unitTestsRepository) {
        this.iOTestRepository = iOTestRepository;
        this.unitTestsRepository = unitTestsRepository;
    }


    public List<IOTest> getAllIOTests(Long activityId) {
        return iOTestRepository.findAllByActivityId(activityId);
    }

    public UnitTest getUnitTests(Long activityId) {
        return unitTestsRepository.findByActivityId(activityId).orElse(null);
    }


    /**
     * Checks if, for a given Activity and a submission's test run, all the tests passed.
     *
     * @param activityId Activity to grade
     * @param testRunStdout stdout of test run WITH LOGGING
     * @return if all the tests passed
     */
    boolean checkIfTestsPassed(Long activityId, String testRunStdout) {
        List<String> results = this.parseTestRunStdout(testRunStdout);
        List<IOTest> ioTests = this.getAllIOTests(activityId);

        if (results.size() != ioTests.size()) {
            // All the tests weren't executed...
            return false;
        }

        for (int i = 0; i < ioTests.size(); i++) {
            if (!ioTests.get(i).getTestOut().equals(results.get(i))) {
                return false;
            }
        }
        //TODO: think if we might want to save which tests did not passed to later show the student

        //TODO: check unit tests if necessary
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
                results.add(result.toString().strip().replace("./main", ""));
            } else if (line.contains("start_RUN")) {
                result = new StringBuilder();
            } else {
                result.append(line);
            }
        }
        return results;
    }

    @Transactional
    public IOTest createUnitTest(Long activityId, String in, String out) {
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
