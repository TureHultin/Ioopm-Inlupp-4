// import static org.junit.jupiter.api.Assertions.assertEquals;

// import java.io.*;
// import org.junit.jupiter.api.Test;
// import java.util.Scanner;
// import java.nio.file.*;
// import org.ioopm.calculator.Calculator;

// public class TextfileTests {

//     @Test
//     void textfileTest() throws FileNotFoundException, IOException {

//         File inputFile = new File("../resources/inputForTest.txt");
//         Scanner in = new Scanner(inputFile);

//         ByteArrayOutputStream buf = new ByteArrayOutputStream();
//         PrintStream out = new PrintStream(buf);

//         Calculator calc = new Calculator(in, out);
//         calc.runEventLoop();

//         String output = buf.toString();
//         String expectedOutput = new String(Files.readAllBytes(Paths.get("../resources/expectedOutputForTest.txt")));

//         assertEquals(output, expectedOutput);
//         assertEquals(1, 2);
//     }

// }