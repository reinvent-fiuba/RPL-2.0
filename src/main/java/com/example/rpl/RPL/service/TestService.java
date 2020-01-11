package com.example.rpl.RPL.service;

import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.repository.IOTestRepository;
import com.example.rpl.RPL.repository.UnitTestRepository;
import java.util.ArrayList;
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


    public boolean checkIfTestsPassed(Long activityId, String testRunStdout) {
        //TODO: Awsome logic to check if stdout is equal to expected stdout.
        List<String> results = this.parseTestRunStdout(testRunStdout);
        List<IOTest> ioTests = this.getAllIOTests(activityId);

        for (int i = 0; i < ioTests.size(); i++) {
            if (!ioTests.get(i).getTestOut().equals(results.get(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Devuelve una lista de todas las salidas de las corridas SIN EL LOGGING. Se identifica como
     * salida del programa a todo el stdout entre el log start_RUN y end_RUN
     *
     * @param testRunStdout el stdout con logging incluido
     * @return lista de resultado por cada ejecucion (los IO Test pueden ser muchos)
     */
    private List<String> parseTestRunStdout(String testRunStdout) {
        List<String> results = new ArrayList<>();
        String result = "";
        for (String line : testRunStdout.split("\n")) {
            if (line.contains("end_RUN")) {
                results.add(result.strip().replace("./main", ""));
            } else if (line.contains("start_RUN")) {
                result = "";
            } else {
                result += line;
            }
        }
        return results;
    }

    /**
     * def parse_stdout(log_stdout):
     *     '''
     *     Devuelve una lista de todas las salidas de las corridas SIN EL LOGGING.
     *     Se identifica como salida del programa a todo el stdout entre el log start_RUN y end_RUN
     *     '''
     *     results = []
     *     result = ""
     *     for line in log_stdout.split('\n'):
     *         if "end_RUN" in line:
     *             results.append(result.strip("./main"))
     *
     *         elif "start_RUN" in line:
     *             result = ""
     *
     *         else:
     *             result += line
     *
     *     return results
     */
}
