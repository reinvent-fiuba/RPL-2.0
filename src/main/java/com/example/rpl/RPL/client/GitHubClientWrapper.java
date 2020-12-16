package com.example.rpl.RPL.client;

import com.example.rpl.RPL.model.IOTestRun;
import com.example.rpl.RPL.model.TestRun;
import com.example.rpl.RPL.model.UnitTestRun;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

public class GitHubClientWrapper {

    GitHubClient client;

    public GitHubClientWrapper(GitHubClient client) {
        this.client = client;
    }

    /**
     * Uses githubclient to create a gist with the submission files
     *
     * @return gist URL for anyone to access
     */
    public String postSubmissionGist(TestRun run, Map<String, String> submissionFiles)
        throws IOException {

        //TODO: Agregar enunciado del ejercicio

        // Create stderr log file
        GistFile fileStderrGF = new GistFile();
        fileStderrGF.setContent(run.getStderr());

        // Create stdout log file
        GistFile fileStdoutGF = new GistFile();
        fileStdoutGF.setContent(run.getStdout());

        // Create IO Tests log file
        GistFile fileIOTestResultGF = this.getIOTestsGistFile(run.getIoTestRunList());

        // Create Unit Tests log file
        GistFile fileUnitTestResultGF = this.getUnitTestGistFile(run.getUnitTestRunList());

        // Add only non-empty files
        Map<String, GistFile> filesGF = new HashMap<>();
        if (!fileStderrGF.getContent().isEmpty()) {
            filesGF.put("stderr.txt", fileStderrGF);
        }

        if (!fileStdoutGF.getContent().isEmpty()) {
            filesGF.put("stdout.txt", fileStdoutGF);
        }

        if (!run.getUnitTestRunList().isEmpty()) {
            filesGF.put("unit_test_run_result.md", fileUnitTestResultGF);
        }

        if (!run.getIoTestRunList().isEmpty()) {
            filesGF.put("io_test_run_result.md", fileIOTestResultGF);
        }

        // Add student files (without teacher tests or hidden files)
        // If present, remove metadata
        submissionFiles.remove("files_metadata");
        submissionFiles.forEach((filename, content) -> {
                GistFile gf = new GistFile();
                gf.setContent(content.isEmpty() ? "---" : content);
                filesGF.put(filename, gf);
            }
        );

        Gist gist = new Gist();
        gist.setDescription("Resultado de la entrega");
        gist.setFiles(filesGF);

        GistService service = new GistService(client);
        gist = service.createGist(gist);
        return gist.getHtmlUrl();
    }

    private GistFile getIOTestsGistFile(List<IOTestRun> ioTestRunList) {
        GistFile fileIOTestResultGF = new GistFile();
        fileIOTestResultGF.setContent(ioTestRunList.stream().map(
            IOTestRun::buildMarkdownRepresentation
        ).collect(Collectors.joining("\n\n----\n\n")));
        return fileIOTestResultGF;
    }

    private GistFile getUnitTestGistFile(List<UnitTestRun> unitTestRunList) {
        GistFile fileUnitTestResultGF = new GistFile();
        fileUnitTestResultGF.setContent(unitTestRunList.stream().map(
            UnitTestRun::buildMarkdownRepresentation
        ).collect(Collectors.joining("\n\n----\n\n")));
        return fileUnitTestResultGF;
    }
}
