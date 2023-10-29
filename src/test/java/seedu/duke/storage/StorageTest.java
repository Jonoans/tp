package seedu.duke.storage;

import org.junit.jupiter.api.*;
import seedu.duke.classes.StateManager;
import seedu.duke.command.HelpCommand;
import seedu.duke.command.ListCommand;
import seedu.duke.exception.DukeException;
import seedu.duke.parser.Parser;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StorageTest {
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private Storage storage;

    @BeforeEach
    void initialise() {
        storage = new Storage();
    }

    @Test
    void validRowWithEmptyValues() {
        String[] row = {"TEST1", ""};
        assertEquals(false, storage.validRow(row));
        row[0] = "";
        row[1] = "TEST";
        assertEquals(false, storage.validRow(row));
    }

    @Test
    void validRowWithBlankValues() {
        String[] row = {"TEST1", " "};
        assertEquals(false, storage.validRow(row));
        row[0] = "    ";
        row[1] = "Test1";
        assertEquals(false, storage.validRow(row));
    }

    @Test
    void validRowWithCorrectValues() {
        String[] row = {"TEST1", "TEST2"};
        assertEquals(true, storage.validRow(row));
    }

    @Test
    void validDateWithWrongFormat() {
        String dateStr = "25-10-2023";
        String testFileName = "filename";
        assertThrows(DukeException.class, () -> {
            storage.validDate(dateStr, testFileName);
        });
    }

    @Test
    void validDateWithNotDateString() {
        String dateStr = "TEST";
        String testFileName = "filename";
        assertThrows(DukeException.class, () -> {
            storage.validDate(dateStr, testFileName);
        });
    }

    @Test
    void validDateWithCorrectDateString()  throws DukeException {
        String dateStr = "25/10/2023";
        String testFileName = "filename";
        LocalDate date = LocalDate.parse("25/10/2023", FORMATTER);
        assertEquals(date ,storage.validDate(dateStr, testFileName));
    }

    @Test
    void validBooleanWithCorrectBoolString() {
        String input = "True";
        assertEquals(true, storage.validBoolean(input));
        input = "TRUE";
        assertEquals(true, storage.validBoolean(input));
        input = "true";
        assertEquals(true, storage.validBoolean(input));
        input = "False";
        assertEquals(true, storage.validBoolean(input));
        input = "FALSE";
        assertEquals(true, storage.validBoolean(input));
        input = "false";
        assertEquals(true, storage.validBoolean(input));
    }

    @Test
    void validBooleanWithWrongBoolString() {
        String input = "test";
        assertEquals(false, storage.validBoolean(input));
    }

    @Test
    void loadWithNoStorageFile() {
        assertThrows(DukeException.class, () -> {
            storage.load();
        });
    }

    @Nested
    class WithValidStorage {
        @BeforeEach
        void copyFiles() throws IOException {
            File src = new File("./TestCSV/valid/category-store.csv");
            File dst = new File("category-store.csv");
            Files.copy(src.toPath(), dst.toPath());
            src = new File("./TestCSV/valid/goal-store.csv");
            dst = new File("goal-store.csv");
            Files.copy(src.toPath(), dst.toPath());
            src = new File("./TestCSV/valid/expense-store.csv");
            dst = new File("expense-store.csv");
            Files.copy(src.toPath(), dst.toPath());
            src = new File("./TestCSV/valid/income-store.csv");
            dst = new File("income-store.csv");
            Files.copy(src.toPath(), dst.toPath());
        }

        @AfterEach
        void clearStateManager() {
            File file = new File("category-store.csv");
            file.delete();
            file = new File("goal-store.csv");
            file.delete();
            file = new File("expense-store.csv");
            file.delete();
            file = new File("income-store.csv");
            file.delete();
            StateManager.clearStateManager();
        }
        @Test
        void loadWithValidStorageFile() throws DukeException {
            storage.load();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Parser parser = new Parser();
            Ui ui = new Ui(outputStream);
            String userInput = "list /type in";
            HashMap<String, String> args = parser.getArguments(userInput);
            String commandWord = parser.getDescription(userInput);
            ListCommand command = new ListCommand(commandWord, args);
            command.execute(ui);
            userInput = "list /type out";
            args = parser.getArguments(userInput);
            commandWord = parser.getDescription(userInput);
            command = new ListCommand(commandWord, args);
            command.execute(ui);
            assertEquals("Alright! Displaying 3 transactions.\n"
                            + "=============================== IN TRANSACTIONS ================================\n"
                            + "ID    Description                      Date         Amount       Goal\n"
                            + "1     part-time job                    2023-10-29   1000.00      car\n"
                            + "2     allowance                        2023-10-29   500.00       car\n"
                            + "3     sell stuff                       2023-10-29   50.00        ps5\n"
                            + "=============================== IN TRANSACTIONS ================================\n"
                            + "Alright! Displaying 3 transactions.\n"
                            + "=============================== OUT TRANSACTIONS ===============================\n"
                            + "ID    Description                      Date         Amount       Category\n"
                            + "1     buy dinner                       2023-10-29   15.00        food\n"
                            + "2     popmart                          2023-10-29   12.00        toy\n"
                            + "3     grab                             2023-10-29   20.00        transport\n"
                            + "=============================== OUT TRANSACTIONS ===============================\n"
                    , outputStream.toString());


        }
    }
}
