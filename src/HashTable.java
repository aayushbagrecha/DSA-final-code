
import java.io.PrintWriter;

/**
 * The `HashTable` class represents a data structure that allows for efficient
 * insertion, deletion, and searching of records based on their ID. It provides
 * methods for managing records in the hash table, such as insertion, search,
 * deletion, and expansion.
 * 
 * @author Aayush Bagrecha
 * @author Yash Shrikant
 * @version 1.0
 */
public class HashTable {

    private static final double LOAD_FACTOR_THRESHOLD = 0.5;

    private Record[] table;
    private int size;
    private int memoryPoolSize;
    private int[] freeBlocks;
    private PrintWriter writer;

    /**
     * Constructs a new `HashTable` object with the specified memory pool size,
     * initial capacity, and PrintWriter object.
     *
     * @param memoryPoolSize
     *            The size of the memory pool in bytes.
     * @param initialCapacity
     *            The initial capacity of the hash table.
     * @param writer
     *            The PrintWriter object used for output.
     */
    public HashTable(
        int memoryPoolSize,
        int initialCapacity,
        PrintWriter writer) {
        table = new Record[initialCapacity];
        size = 0;
        this.memoryPoolSize = memoryPoolSize;
        freeBlocks = new int[memoryPoolSize];

        for (int i = 0; i < memoryPoolSize; i++) {
            freeBlocks[i] = -1;
        }

        this.writer = writer;
    }


    /**
     * Inserts a record into the table if it doesn't already exist and expands
     * the table if it reaches a load factor threshold.
     *
     * @param record
     *            The record to be inserted into the table.
     * @return `true` if the record is inserted successfully, `false` if a
     *         record with the same ID already exists.
     */
    public boolean insert(Record record) {
        if (search(record.getId(), false) != null) {
            // Record with the same id already exists
            return false;
        }

        if (size >= table.length * LOAD_FACTOR_THRESHOLD) {
            expandTable();
        }
        int index = findIndex(record.getId());
        table[index] = record;
        size++;
        return true;
    }


    /**
     * Searches for a record with a given ID in the table.
     *
     * @param id
     *            The ID of the record to search for.
     * @param searchMode
     *            `true` to print a message if the search fails,
     *            `false` otherwise.
     * @return The found record if not deleted, `null` if not found or marked
     *         as deleted.
     */
    public Record search(int id, boolean searchMode) {
        int index = findIndex(id);
        if (table[index] != null && table[index].getId() == id && !table[index]
            .isDeleted()) {
            return table[index];
        }
        if (searchMode == true) {
            writer.println("Search FAILED -- There is no record with ID " + id);
        }
        return null;
    }


    /**
     * Deletes a record with a given ID by marking it as deleted.
     *
     * @param id
     *            The ID of the record to be deleted.
     * @return `true` if the record is found and successfully marked as deleted,
     *         `false` otherwise.
     */
    public boolean delete(int id) {
        int index = findIndex(id);
        if (table[index] != null && table[index].getId() == id && !table[index]
            .isDeleted()) {
            table[index].setDeleted(true); // Mark the record as deleted with a
                                           // tombstone
            size--;
            return true;
        }
        return false;
    }


    /**
     * Doubles the size of the hash table and rehashes the existing records into
     * the new table.
     * This method is used to maintain an efficient load factor and prevent hash
     * collisions.
     * After expansion, all non-deleted records are reinserted into the newly
     * resized hash table.
     * The expansion process involves creating a new hash table, copying the
     * existing records
     * into it, and updating the internal state of the hash table.
     *
     * @throws NullPointerException
     *             If the existing hash table or any of its
     *             records is null.
     * @throws IllegalStateException
     *             If the internal writer encounters an error
     *             while writing expansion information to its
     *             output.
     */
    public void expandTable() {
        Record[] oldTable = table;
        table = new Record[2 * oldTable.length];
        size = 0;

        writer.println("Hash table expanded to " + table.length + " records");

        for (Record record : oldTable) {
            if (record != null && !record.isDeleted()) {
                insert(record); // Reinsert non-deleted records
            }
        }
    }


    /**
     * Finds the index of a record with the given ID in the hash table.
     *
     * @param id
     *            The ID of the record to search for.
     * @return The index of the record in the hash table, or -1 if the record
     *         is not found.
     */
    public int findIndex(int id) {
        int index = id % table.length;
        int step = (((id / table.length) % (table.length / 2)) * 2) + 1;

        while (table[index] != null && table[index].getId() != id) {
            index = (index + step) % table.length;
        }

        return index;
    }


    /**
     * Prints the contents of the hash table, including the index and ID of
     * the record
     * 
     * @return the hash table as the output
     */
    public String printHashTable() {

        String output = "HashTable:\n";
        int count = 0;
        for (int i = 0; i < table.length; i++) {
            Record record = table[i];
            if (record != null) {
                if (record.isDeleted()) {
                    output += ((i + ": TOMBSTONE")) + "\n";
                }
                else {
                    output += ((i + ": " + record.getId())) + "\n";
                    count++;
                }
            }
        }
        output += ("total records: " + count);

        writer.print(output);

        return output; // Returns the Hash Table contents as the output.
    } // If the record is deleted, it returns TOMBSTONE instead


    /**
     * Prints the free blocks of the hash table
     */
    public void printMemoryBlocks() {
        writer.println("\nFreeBlock List:");
        for (int block : freeBlocks) {
            if (block == -1)
                continue;
            else
                writer.println(block + " ");
        }
        writer.println("There are no freeblocks in the memory pool");
    }


    /**
     * Returns the size of the hash table
     * 
     * @return Size of the Hash Table
     */
    public int getCapacity() {
        return table.length; // Returns the size of the Hash table
    }
}
