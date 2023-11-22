import org.ioopm.calculator.Calculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Scanner;
import java.util.stream.Stream;

public class SnapshotTests {


    static Stream<Path> snapshots() throws IOException, URISyntaxException {
        final Path snapshotDirectory = Path.of(ClassLoader.getSystemResource("Snapshots").toURI());

        return Files.list(snapshotDirectory);
    }

    void assertSameLines(Path expected, ByteArrayOutputStream buffer) throws IOException {
        Iterator<String> expectedLines = Files.readAllLines(expected).iterator();
        Iterator<String> providedLines = buffer.toString().lines().iterator();

        // Lines are counted starting at 1 in most editors
        int line = 1;
        while (expectedLines.hasNext() && providedLines.hasNext()) {
            String expectedLine = expectedLines.next();
            String providedLine = providedLines.next();
            Assertions.assertEquals(expectedLine, providedLine, "Non matching lines\n in (" + expected + ":" + line + ")");
            line += 1;
        }

        Assertions.assertFalse(expectedLines.hasNext(), () -> "Expected  '" + expectedLines.next() + "' but program stopped outputting\n in" + expected);
        Assertions.assertFalse(providedLines.hasNext(), () -> "The program outputted '" + providedLines.next() + "' but no more lines are expected\n in " + expected);

    }

    @ParameterizedTest
    @MethodSource("snapshots")
    void checkSnapshots(Path snapshot) throws IOException {
        Path inputFile = snapshot.resolve("input.txt");
        Path outputFile = snapshot.resolve("output.txt");
        Scanner input = new Scanner(inputFile.toFile());

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer);

        Calculator calculator = new Calculator(input, out);
        calculator.runEventLoop();


        assertSameLines(outputFile, buffer);
    }
}