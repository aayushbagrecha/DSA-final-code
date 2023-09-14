import student.TestCase;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.Before;
import org.junit.Test;

public class SemManagerTest {

    private static final String TEST_INPUT_FILE = "testInput.txt";
    private static final String TEST_OUTPUT_FILE = "testOutput.txt";

    @Before
    public void setup() {
        // Ensure the test output file is deleted before each test
        File outputFile = new File(TEST_OUTPUT_FILE);
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }


 @Test
 public void testInsertAndSearch() {
 // Prepare a test input file with an "insert" command followed by a
 // "search" command
 prepareTestInput(
 "insert 1\nSample Title\n2023-09-12 120 10 20 500 Sample,Keywords\nSample
 Description\nsearch 1");

 // Run SemManager with the test input
 SemManager.beginParsing(TEST_INPUT_FILE, 64, 4);

 // Read the test output file and check if it contains the expected
 // output
 String output = readTestOutput();
 assertTrue(output.contains("Successfully inserted record with ID 1"));
 assertTrue(output.contains("Found record with ID 1"));
 }


 @Test
 public void testInsertAndDelete() {
 // Prepare a test input file with an "insert" command followed by a
 // "delete" command
 prepareTestInput(
 "insert 1\nSample Title\n2023-09-12 120 10 20 500 Sample,Keywords\nSample
 Description\ndelete 1");

 // Run SemManager with the test input
 SemManager.beginParsing(TEST_INPUT_FILE, 64, 4);

 // Read the test output file and check if it contains the expected
 // output
 String output = readTestOutput();
 assertTrue(output.contains("Successfully inserted record with ID 1"));
 assertTrue(output.contains(
 "Record with ID 1 successfully deleted from the database"));
 }


 @Test
 public void testPrintTable() {
 // Prepare a test input file with an "insert" command followed by a
 // "print table" command
 prepareTestInput(
 "insert 1\nSample Title\n2023-09-12 120 10 20 500 Sample,Keywords\nSample
 Description\nprint table");

 // Run SemManager with the test input
 SemManager.beginParsing(TEST_INPUT_FILE, 64, 4);

 // Read the test output file and check if it contains the expected
 // output
 String output = readTestOutput();
 assertTrue(output.contains("HashTable:"));
 assertTrue(output.contains("ID: 1, Title: Sample Title"));
 }


    // Helper method to prepare a test input file
    private void prepareTestInput(String content) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(
            TEST_INPUT_FILE))) {
            writer.println(content);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Helper method to read the test output file
    private String readTestOutput() {
        try (BufferedReader reader = new BufferedReader(new FileReader(
            TEST_OUTPUT_FILE))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
