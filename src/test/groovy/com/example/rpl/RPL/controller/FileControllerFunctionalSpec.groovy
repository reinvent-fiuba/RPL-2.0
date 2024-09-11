package com.example.rpl.RPL.controller


import com.example.rpl.RPL.model.RPLFile
import com.example.rpl.RPL.repository.FileRepository
import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Unroll

import static javax.servlet.http.HttpServletResponse.SC_OK

@ActiveProfiles("test-functional")
class FileControllerFunctionalSpec extends AbstractFunctionalSpec {

    @Autowired
    FileRepository fileRepository

    @Shared
    RPLFile submissionFile


    def setup() {

        File f = new File("./src/main/resources/db/testdata/la_submission.tar.xz")

        submissionFile = new RPLFile(
                "starting_files",
                "application/gzip",
                f.getBytes()
        )
        fileRepository.save(submissionFile)
    }

    def cleanup() {
        fileRepository.deleteAll()
    }


    /*****************************************************************************************
     ********** GET FILE *********************************************************************
     *****************************************************************************************/

    @Unroll
    void "test get file should retrieve it"() {
        when:
            def response = api.get(String.format("/api/files/%s", submissionFile.getId()))

        then:
            response.contentType == submissionFile.getFileType();
            response.statusCode == SC_OK
    }

}


