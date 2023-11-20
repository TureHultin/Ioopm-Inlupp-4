import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextfileTests {

    @Test
    void testCalculatorOutput() throws IOException {
    
        Path inputFilePath = Paths.get("src", "tests", "resources", "inputForTest.txt");
        Path outputFilePath = Paths.get("src", "tests", "resources", "output.txt");
        Path expectedOutputFilePath = Paths.get("src", "tests", "resources", "expectedOutputForTest.txt");

        ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", "src/main/java/org/ioopm", "calculator.Calculator");
        processBuilder.redirectInput(inputFilePath.toFile());
        processBuilder.redirectOutput(outputFilePath.toFile());
        
        Process process = processBuilder.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(Files.isSameFile(outputFilePath, expectedOutputFilePath));
    }
}