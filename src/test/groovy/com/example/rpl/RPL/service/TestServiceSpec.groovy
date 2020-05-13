package com.example.rpl.RPL.service

import com.example.rpl.RPL.model.IOTest
import com.example.rpl.RPL.model.IOTestRun
import com.example.rpl.RPL.model.TestRun
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.*
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class TestServiceSpec extends Specification {
    private FileRepository fileRepository
    private IOTestRepository iOTestRepository;
    private IOTestRunRepository iOTestRunRepository;
    private UnitTestRepository unitTestsRepository;
    private UnitTestRunRepository unitTestRunRepository;

    private TestService testService

    @Shared
    User user

    def setup() {
        iOTestRepository = Mock(IOTestRepository)
        fileRepository = Mock(FileRepository)
        iOTestRunRepository = Mock(IOTestRunRepository)
        unitTestRunRepository = Mock(UnitTestRunRepository)

        testService = new TestService(iOTestRepository, unitTestsRepository, iOTestRunRepository, unitTestRunRepository, fileRepository)

        user = new User(
                'some-name',
                'some-surname',
                'some-student-id',
                'username',
                'some@mail.com',
                'supersecret',
                'some-university',
                'some-hard-degree'
        )
    }

    void "test parseTestRunStdout"() {
        given: "a testRun stdout"
            String stdout = "2020-01-12 15:56:33,876 RPL-2.0      INFO     Build Started\n2020-01-12 15:56:33,879 RPL-2.0      INFO     Building\n2020-01-12 15:56:33,879 RPL-2.0      INFO     start_BUILD\ngcc -g -O2 -std=c99 -Wall -Wformat=2 -Wshadow -Wpointer-arith -Wunreachable-code -Wconversion -Wno-sign-conversion -Wbad-function-cast -DCORRECTOR   -c -o test_file.o test_file.c\ngcc -o main test_file.o\n2020-01-12 15:56:34,044 RPL-2.0      INFO     end_BUILD\n2020-01-12 15:56:34,044 RPL-2.0      INFO     Build Ended\n2020-01-12 15:56:34,044 RPL-2.0      INFO     Run Started\n2020-01-12 15:56:34,048 RPL-2.0      INFO     IO TEST: IO_test_0.txt\n2020-01-12 15:56:34,048 RPL-2.0      INFO     start_RUN\n./main\n07:16:04.0000\n2020-01-12 15:56:34,048 RPL-2.0      INFO     end_RUN\n2020-01-12 15:56:34,048 RPL-2.0      INFO     IO TEST: IO_test_1.txt\n2020-01-12 15:56:34,048 RPL-2.0      INFO     start_RUN\n./main\n07:16:05.0000\n2020-01-12 15:56:34,049 RPL-2.0      INFO     end_RUN\n2020-01-12 15:56:34,049 RPL-2.0      INFO     RUN OK\n2020-01-12 15:56:34,049 RPL-2.0      INFO     Run Ended\n"

        when: "parsing"
            List result = testService.parseTestRunStdout(stdout)

        then:
            assert result == ["07:16:04.0000", "07:16:05.0000"]
    }

    void "test parseTestRunStdout when it's empty"() {
        given: "a testRun stdout"
            String stdout = "2020-01-12 15:56:33,876 RPL-2.0      INFO     Build Started\n2020-01-12 15:56:33,879 RPL-2.0      INFO     Building\n2020-01-12 15:56:33,879 RPL-2.0      INFO     start_BUILD\ngcc -g -O2 -std=c99 -Wall -Wformat=2 -Wshadow -Wpointer-arith -Wunreachable-code -Wconversion -Wno-sign-conversion -Wbad-function-cast -DCORRECTOR   -c -o test_file.o test_file.c\ngcc -o main test_file.o\n2020-01-12 15:56:34,044 RPL-2.0      INFO     end_BUILD\n2020-01-12 15:56:34,044 RPL-2.0      INFO     Build Ended\n2020-01-12 15:56:34,044 RPL-2.0      INFO     Run Started\n2020-01-12 15:56:34,048 RPL-2.0      INFO         INFO     Run Ended\n"

        when: "parsing"
            List result = testService.parseTestRunStdout(stdout)

        then:
            assert result == []
    }


    @Unroll
    void "test checkIfTestsPassed for an Activity with IO Tests"() {
        given: "activity tests"
            IOTestRun ioTestRun1 = new IOTestRun(null, "bla",
                    expectedOutput, runOutput);

            List<IOTestRun> ioTestRuns = [ioTestRun1]

            IOTest io1 = new IOTest(1, "1", expectedOutput)

            1 * iOTestRepository.findAllByActivityId(1) >> [io1]

        when:
            boolean result = testService.checkIfTestsPassed(1, ioTestRuns)

        then:
            assert result == expected

        where:
            expectedOutput  | runOutput       | expected
            "07:16:04.0000" | "07:16:04.0000" | true
            "07:16:04.0000" | "456"           | false
            "123"           | "07:16:05.0000" | false
            "123"           | "456"           | false
    }

    @Unroll
    void "test checkIfTestsPassed when all tests were not executed should return false"() {
        given: "activity tests"
            IOTestRun ioTestRun1 = new IOTestRun(null, "bla",
                    expectedOutput, runOutput);

            List<IOTestRun> ioTestRuns = [ioTestRun1]

            IOTest io1 = new IOTest(1, "1", expectedOutput)
            IOTest io2 = new IOTest(1, "2", expectedOutput)

            1 * iOTestRepository.findAllByActivityId(1) >> [io1, io2]

        and: "a dummy testRun stdout"
            String stdout = "2020-01-12 15:56:33,876 RPL-2.0      INFO     Build Started\n"

        when:
            boolean result = testService.checkIfTestsPassed(1, ioTestRuns)

        then:
            assert !result

        where:
            expectedOutput  | runOutput       | expected
            "07:16:04.0000" | "07:16:04.0000" | false
            "07:16:04.0000" | "456"           | false
            "123"           | "07:16:05.0000" | false
            "123"           | "456"           | false
    }

    void "test parseAndSaveStdout"() {
        given: "a testRun stdout"
            String stdout = "2020-01-12 15:56:33,876 RPL-2.0      INFO     Build Started\n2020-01-12 15:56:33,879 RPL-2.0      INFO     Building\n2020-01-12 15:56:33,879 RPL-2.0      INFO     start_BUILD\ngcc -g -O2 -std=c99 -Wall -Wformat=2 -Wshadow -Wpointer-arith -Wunreachable-code -Wconversion -Wno-sign-conversion -Wbad-function-cast -DCORRECTOR   -c -o test_file.o test_file.c\ngcc -o main test_file.o\n2020-01-12 15:56:34,044 RPL-2.0      INFO     end_BUILD\n2020-01-12 15:56:34,044 RPL-2.0      INFO     Build Ended\n2020-01-12 15:56:34,044 RPL-2.0      INFO     Run Started\n2020-01-12 15:56:34,048 RPL-2.0      INFO     IO TEST: IO_test_0.txt\n2020-01-12 15:56:34,048 RPL-2.0      INFO     start_RUN\n./main\n07:16:04.0000\n2020-01-12 15:56:34,048 RPL-2.0      INFO     end_RUN\n2020-01-12 15:56:34,048 RPL-2.0      INFO     IO TEST: IO_test_1.txt\n2020-01-12 15:56:34,048 RPL-2.0      INFO     start_RUN\n./main\n07:16:05.0000\n2020-01-12 15:56:34,049 RPL-2.0      INFO     end_RUN\n2020-01-12 15:56:34,049 RPL-2.0      INFO     RUN OK\n2020-01-12 15:56:34,049 RPL-2.0      INFO     Run Ended\n"

            TestRun testRun = new TestRun(null, true,
                    "testRunExitMessage", "testRunStderr", stdout);

        and: "the unit tests"
            IOTest ioTest1 = new IOTest(1, "1", "1")
            IOTest ioTest2 = new IOTest(1, "2", "2")
            1 * iOTestRepository.findAllByActivityId(1) >> [ioTest1, ioTest2]

        when: "parsing"
            List<IOTestRun> result = testService.parseAndSaveStdout(1, testRun)

        then:
            1 * iOTestRunRepository.saveAll(_ as List<IOTestRun>) >> { List<IOTestRun> runs -> return runs }

            assert result[0][0].getTestIn() == "1"
            assert result[0][0].getExpectedOutput() == "1"
            assert result[0][0].getRunOutput() == "07:16:04.0000"

            assert result[0][1].getTestIn() == "2"
            assert result[0][1].getExpectedOutput() == "2"
            assert result[0][1].getRunOutput() == "07:16:05.0000"

    }
}
