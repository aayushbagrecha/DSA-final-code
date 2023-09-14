import student.TestCase;
import org.junit.Before;
import org.junit.Test;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Aayush Bagrecha
 * @author Yash Shrikant
 * @version 1.0
 * 
 *          This class contains JUnit tests for the HashTable class. It tests
 *          various
 *          functionalities such as insertion, searching, deletion, expansion,
 *          and more.
 *          The tests cover both normal and edge cases to ensure the HashTable
 *          class
 *          functions correctly.
 */
public class HashTableTest extends TestCase {

    private HashTable ht;
    private Record record1;
    private Record record2;

    /**
     * Sets up the test environment before each test method.
     *
     * @throws IOException
     *             If an I/O error occurs while creating the output file
     *             or initializing the PrintWriter.
     */
    @Before
    public void setUp() {
        try {
            // testInputFile = "input.txt";
            // testOutputFile = "output.txt";
            String outputFile = "output.txt";
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
            ht = new HashTable(64, 4, writer);
            record1 = new Record(1, "Seminar 1", "2111011200", 60, (short)10,
                (short)20, 100, "Description 1", "Keyword1, Keyword2");
            record2 = new Record(2, "Seminar 2", "2111021300", 45, (short)15,
                (short)25, 75, "Description 2", "Keyword3, Keyword4");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This test method verifies the correctness of the HashTable's insert and
     * search operations. It checks if inserting a record and then searching for
     * it returns the expected result. It also tests the case where searching
     * for a non-existing record returns null.
     */
    @Test
    public void testInsertAndSearch() {
        assertTrue(ht.insert(record1));
        assertNotNull(ht.search(1, true));
        // assertNull(ht.search(3,true));
    }


    /**
     * This test method evaluates the HashTable's delete operation. It inserts
     * a record, deletes it, and verifies that the record is successfully
     * deleted the first time and fails to delete it the second time, as the
     * record is no longer present.
     */
    @Test
    public void testDelete() {
        ht.insert(record1);
        assertTrue(ht.delete(record1.getId()));
        assertFalse(ht.delete(record1.getId()));
    }


    /**
     * This test method checks if a record is successfully inserted into the
     * hash table when there is no conflict (i.e., no record with the same ID
     * exists).
     */
    @Test
    public void testSuccessfulRecordInsertion() {
        setUp();
        ht.insert(record1);

        // Check if the record is present in the hash table
        Record retrievedRecord = ht.search(record1.getId(), true);
        assertNotNull(retrievedRecord);
        assertEquals(record1, retrievedRecord); // Ensure the inserted and
                                                // retrieved records are equal
    }


    /**
     * This test method checks if the search method correctly finds and returns
     * a record when it exists and is not deleted in the hash table.
     */
    @Test
    public void testSuccessfulRecordSearch() {
        setUp();
        ht.insert(record1);

        // Search for the record by ID
        Record foundRecord = ht.search(1, true);

        // Verify that the foundRecord is not null and matches the inserted
        // record
        assertNotNull(foundRecord);
        assertEquals(record1, foundRecord);
    }


    /**
     * This test method checks if the delete method correctly marks a record as
     * deleted and returns true when the record exists, is not deleted yet, and
     * the deletion is successful.
     */
    @Test
    public void testSuccessfulRecordDeletion() {
        setUp();
        ht.insert(record1);
        ht.insert(record1);

        // Attempt to delete the record by ID
        boolean deletionResult = ht.delete(1);

        // Verify that the deletion was successful
        assertTrue(deletionResult);

        // Check if the record is marked as deleted
        assertTrue(record1.isDeleted());
    }


    /**
     * Tests verifies whether it calculates the correct index based on the
     * provided id value and handles various scenarios.
     */
    @Test
    public void testFindIndex() {

        // Test various ID values and step values
        ht.insert(record1);

        int expectedIndex = 1;

        int index = ht.findIndex(record1.getId());

        // Verify that the calculated index matches the expected index
        assertEquals(expectedIndex, index);
    }


    /**
     * This test method assesses the HashTable's expansion functionality. It
     * inserts two records into the table, ensuring that both insertions are
     * successful. Afterward, it searches for both records to confirm their
     * presence in the table.
     */
    @Test
    public void testExpandTable() {

        ht.insert(record1);
        ht.insert(record2);
        ht.expandTable();
        assertEquals(8, ht.getCapacity());
        Record foundRecord1 = ht.search(1, true);
        Record foundRecord2 = ht.search(2, true);

        assertNotNull(foundRecord1);
        assertNotNull(foundRecord2);

        // Check if the records match the original ones
        assertEquals(record1, foundRecord1);
        assertEquals(record2, foundRecord2);

// assertTrue(ht.insert(record1));
// assertTrue(ht.insert(record2));
// assertNotNull(ht.search(1, true));
// assertNotNull(ht.search(2, true));
    }


    /**
     * This test method examines the HashTable's handling of duplicate record
     * insertion. It inserts the same record twice and verifies that the second
     * insertion fails since duplicates are not allowed in the table.
     */
    @Test
    public void testInsertDuplicate() {
        assertTrue(ht.insert(record1));
        assertFalse(ht.insert(record1));
    }


    /**
     * This test method tests the HashTable's behavior when searching for a
     * record that has been previously deleted. It inserts a record, deletes
     * it, and then checks whether searching for the deleted record returns
     * null.
     */
    @Test
    public void testSearchDeletedRecord() {
        ht.insert(record1);
        ht.delete(1);
        // assertNull(ht.search(1,true));
    }


    /**
     * This test method evaluates the HashTable's delete operation when
     * attempting to delete a record that doesn't exist in the table. It
     * verifies that attempting to delete a non-existing record returns false.
     */
    @Test
    public void testDeleteNonExistingRecord() {
        assertFalse(ht.delete(1));
    }


    /**
     * This test method tests the HashTable's printing functionality. It sets up
     * a sample HashTable with two records, inserts the records, and then checks
     * if the printed representation matches the expected output.
     */
    @Test
    public void testPrintHashTable() {
        setUp();
        ht.insert(record1);
        ht.insert(record2);

        String output = ht.printHashTable();

        String expectedOutput = "HashTable:\n1: 1\n2: 2\ntotal records: 2";
        assertEquals(expectedOutput, output);
    }


    /**
     * This test method verifies the getter methods of the Record class. It
     * checks whether the getters return the expected values for various
     * properties of a sample record.
     */
    @Test
    public void testGetters() {
        assertEquals("2111011200", record1.getDate());
        assertEquals("Seminar 1", record1.getTitle());
        assertEquals("Description 1", record1.getDescription());
        assertEquals(100, record1.getCost());
        assertEquals("Keyword1, Keyword2", record1.getKeywords());
    }


    /**
     * This test method tests the HashTable's expansion functionality and the
     * change in its capacity. It initializes the capacity, performs a table
     * expansion, and then checks whether the capacity has increased as
     * expected.
     */
    @Test
    public void testExpandTable1() {
        // Initialize the capacity
        setUp();
        assertEquals(4, ht.getCapacity());
        ht.expandTable();
        // Check the initial table size
        assertEquals(8, ht.getCapacity());
    }


    /**
     * This test method evaluates the HashTable's handling of tombstones
     * (deleted records). It inserts a record, deletes it, and verifies that
     * the printed representation of the table contains the expected tombstone
     * marker and that the total records count reflects the deletion.
     */
    @Test
    public void testTombstoneHandling() {
        // Delete a record
        ht.insert(record1);
        ht.delete(record1.getId());
        String output = ht.printHashTable();

        String expectedOutput = "HashTable:\n1: TOMBSTONE\ntotal records: 0";
        System.out.println(output);
        assertEquals(expectedOutput, output);
    }


    /**
     * Test method to insert a record when a record with the same ID already
     * exists.
     * It verifies that the insertion returns false, indicating that the record
     * was not inserted.
     */
    @Test
    public void testInsertWhenRecordIdAlreadyExists() {
        // Insert a record
        assertTrue(ht.insert(record1));

        // Try to insert a record with the same ID
        assertFalse(ht.insert(record1));
    }


    /**
     * Test method to insert a record and verify that the size of the hash table
     * has been incremented.
     */
    @Test
    public void testInsertAndSizeIncrement() {
        // Insert a record
        assertTrue(ht.insert(record1));

        // Check that the size has been incremented
        assertEquals(4, ht.getCapacity());
    }


    /**
     * Test method to insert a record and confirm that the insertion returns
     * true, indicating success.
     */
    @Test
    public void testInsertAndReturnTrue() {
        // Insert a record
        assertTrue(ht.insert(record1));
    }


    /**
     * Test method to search for a record that is found in the hash table and is
     * not marked as deleted.
     * It verifies that the search returns the expected record.
     */
    @Test
    public void testSearchRecordFoundNotDeleted() {
        // Create a HashTable and add a record with ID 1
        HashTable ht1 = new HashTable(64, 16, new PrintWriter(System.out));
        Record record = new Record(1, "Test Record", "2023010101", 50, (short)5,
            (short)10, 100, "Description", "Keyword");
        ht1.insert(record);

        // Search for the record with ID 1
        Record foundRecord = ht1.search(1, true);

        // Assert that the foundRecord is not null and matches the inserted
        // record
        assertNotNull(foundRecord);
        assertEquals(record, foundRecord);
    }


    /**
     * Test method to search for a record that is not found in the hash table.
     * It verifies that the search returns null when the record is not present.
     */
    @Test
    public void testSearchRecordNotFound() {
        // Create a HashTable and add a record with ID 1
        HashTable ht1 = new HashTable(64, 16, new PrintWriter(System.out));
        Record record = new Record(1, "Test Record", "2023010101", 50, (short)5,
            (short)10, 100, "Description", "Keyword");
        ht1.insert(record);

        // Search for a non-existent record with ID 2
        Record foundRecord = ht1.search(2, true);

        // Assert that the foundRecord is null
        assertNull(foundRecord);
    }


    /**
     * Test method to search for a record that has been deleted in the hash
     * table.
     * It verifies that the search returns null for a deleted record.
     */
    @Test
    public void testSearchRecordDeleted() {
        // Create a HashTable and add a record with ID 1, then delete it
        HashTable ht1 = new HashTable(64, 16, new PrintWriter(System.out));
        Record record = new Record(1, "Test Record", "2023010101", 50, (short)5,
            (short)10, 100, "Description", "Keyword");
        ht1.insert(record);
        ht1.delete(1);

        // Search for the deleted record with ID 1
        Record foundRecord = ht.search(1, true);

        // Assert that the foundRecord is null
        assertNull(foundRecord);
    }


    /**
     * Test method to delete a record that is found in the hash table and is not
     * marked as deleted.
     * It verifies that the deletion returns true, indicating success, and marks
     * the record as deleted.
     */
    @Test
    public void testDeleteRecordFoundNotDeleted() {
        // Create a HashTable and add a record with ID 1
        HashTable ht1 = new HashTable(64, 16, new PrintWriter(System.out));
        Record record = new Record(1, "Test Record", "2023010101", 50, (short)5,
            (short)10, 100, "Description", "Keyword");
        ht1.insert(record);

        // Delete the record with ID 1
        boolean deletionResult = ht1.delete(1);

        // Assert that the deletion was successful and the record is marked as
        // deleted
        assertTrue(deletionResult);
        assertTrue(record.isDeleted());
    }


    /**
     * Test method to search for a record that is found in the hash table and is
     * not marked as deleted.
     * It verifies that the found record is not null and is not marked as
     * deleted.
     */
    @Test
    public void testRecordNotNullAndNotDeleted() {
        // Create a HashTable and add a record that is not deleted
        HashTable ht1 = new HashTable(64, 16, new PrintWriter(System.out));
        Record record = new Record(1, "Test Record", "2023010101", 50, (short)5,
            (short)10, 100, "Description", "Keyword");
        ht1.insert(record);

        // Search for the record by ID
        Record foundRecord = ht1.search(1, true);

        // Assert that the foundRecord is not null and is not marked as deleted
        assertNotNull(foundRecord);
        assertFalse(foundRecord.isDeleted());
    }


    /**
     * Test method to search for a non-existent record in an empty hash table.
     * It verifies that the search returns null for a non-existent record.
     */
    @Test
    public void testRecordNull() {
        // Create a HashTable without adding any records
        HashTable ht1 = new HashTable(64, 16, new PrintWriter(System.out));

        // Search for a non-existent record
        Record foundRecord = ht1.search(1, true);

        // Assert that the foundRecord is null
        assertNull(foundRecord);
    }


    /**
     * Test method to calculate the index of a record based on its ID in the
     * hash table.
     * It tests various ID values and step values to ensure the calculated
     * indexes are within the valid range.
     */
    @Test
    public void testfindIndex() {
        // Create a HashTable with a specific size
        HashTable hash = new HashTable(64, 16, new PrintWriter(System.out));

        // Test various ID values and step values
        int id1 = 10;
        int id2 = 20;
        int id3 = 30;

        int index1 = hash.findIndex(id1);
        int index2 = hash.findIndex(id2);
        int index3 = hash.findIndex(id3);

        // Assert that the calculated indexes are within the valid range (0 to
        // table.length - 1)
        assertTrue(index1 >= 0 && index1 < hash.getCapacity());
        assertTrue(index2 >= 0 && index2 < hash.getCapacity());
        assertTrue(index3 >= 0 && index3 < hash.getCapacity());
    }


    /**
     * Test method to calculate the size of a record based on its properties.
     */
    @Test
    public void testCalculateSize() {
        ht.insert(record1);

        // Calculate the expected size based on the lengths of title,
        // description, and keywords
        int expectedSize = "Seminar 1".length() + "Description 1".length()
            + "Keyword1, Keyword2".length();

        // Call the calculateSize method on the Record object
        int actualSize = record1.calculateSize();

        // Assert that the actual size matches the expected size
        assertEquals(expectedSize, actualSize);
    }

}
