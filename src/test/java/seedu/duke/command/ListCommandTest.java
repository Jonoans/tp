package seedu.duke.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import seedu.duke.classes.StateManager;
import seedu.duke.exception.DukeException;
import seedu.duke.parser.Parser;
import seedu.duke.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListCommandTest {
    @AfterEach
    void clearStateManager() {
        StateManager.clearStateManager();
    }

    @Test
    void invalidList() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        String userInput = "list";
        HashMap<String, String> args = parser.getArguments(userInput);
        String commandWord = parser.getDescription(userInput);
        ListCommand command = new ListCommand(commandWord, args);
        assertThrows(DukeException.class, () -> {
            command.execute(ui);
        });
    }

    @Test
    void invalidListType() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        String userInput = "list /type abc";
        HashMap<String, String> args = parser.getArguments(userInput);
        String commandWord = parser.getDescription(userInput);
        ListCommand command = new ListCommand(commandWord, args);
        assertThrows(DukeException.class, () -> {
            command.execute(ui);
        });
    }

    @Test
    void invalidGoal() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        String userInput = "list /type in /goal ABC";
        HashMap<String, String> args = parser.getArguments(userInput);
        String commandWord = parser.getDescription(userInput);
        ListCommand command = new ListCommand(commandWord, args);
        assertThrows(DukeException.class, () -> {
            command.execute(ui);
        });
    }

    @Test
    void invalidCategory() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        String userInput = "list /type out /category DEF";
        HashMap<String, String> args = parser.getArguments(userInput);
        String commandWord = parser.getDescription(userInput);
        ListCommand command = new ListCommand(commandWord, args);
        assertThrows(DukeException.class, () -> {
            command.execute(ui);
        });
    }
    @Test
    void invalidCategoryGoal() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        String userInput = "list /type in /goal ABC /category DEF";
        HashMap<String, String> args = parser.getArguments(userInput);
        String commandWord = parser.getDescription(userInput);
        ListCommand command = new ListCommand(commandWord, args);
        assertThrows(DukeException.class, () -> {
            command.execute(ui);
        });
    }

    private static void addInEntries() {
        Parser parser = new Parser();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(outputStream);
        try {
            parser.parse("goal /add car /amount 5000").execute(ui);
            parser.parse("goal /add PS5 /amount 300").execute(ui);
            parser.parse("in part-time job /amount 500 /goal car").execute(ui);
            parser.parse("in red packet money /amount 50 /goal PS5 /date 18092023").execute(ui);
        } catch (DukeException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void addOutEntries() {
        Parser parser = new Parser();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(outputStream);
        try {
            parser.parse("out dinner /amount 10.50 /category food").execute(ui);
            parser.parse("out pokemon card pack /amount 10.50 /category games /date 18092023").execute(ui);
        } catch (DukeException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void addInEntriesWithDates() {
        Parser parser = new Parser();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(outputStream);
        try {
            parser.parse("goal /add car /amount 5000").execute(ui);
            parser.parse("in part-time job /amount 500 /goal car /date 30102023").execute(ui);
            parser.parse("in allowance job /amount 300 /goal car /date 23102023").execute(ui);
            parser.parse("in red packet money /amount 150 /goal car /date 23092023").execute(ui);
        } catch (DukeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addOutEntriesWithDates() {
        Parser parser = new Parser();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Ui ui = new Ui(outputStream);
        try {
            parser.parse("out lunch /amount 7.50 /category food /date 30102023").execute(ui);
            parser.parse("out dinner /amount 10.50 /category food /date 23102023").execute(ui);
            parser.parse("out pokemon card pack /amount 10.50 /category games /date 18092023").execute(ui);
        } catch (DukeException e) {
            System.out.println(e.getMessage());
        }

    }


    void validInList() throws DukeException {
        addInEntries();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        LocalDate currentDate = LocalDate.now();
        Command command = parser.parse("list /type in");
        command.execute(ui);
        assertEquals("Alright! Displaying 2 transactions.\n" +
                        "====================================== IN TRANSACTIONS ===========================" +
                        "===========\n" +
                        "ID    Description                      Date         Amount       Goal         Recurrence\n" +
                        "1     part-time job                    2023-10-31   500.00       car          none\n" +
                        "2     red packet money                 2023-09-18   50.00        PS5          none\n" +
                        "====================================== IN TRANSACTIONS ================================" +
                        "======\n"
                , outputStream.toString());

    }

    void validFilteredInList() throws DukeException {
        addInEntries();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type in /goal car");
        command.execute(ui);
        assertEquals("Alright! Displaying 1 transaction.\n" +
                        "====================================== IN TRANSACTIONS ==================================" +
                        "====\n" +
                        "ID    Description                      Date         Amount       Goal         Recurrence\n" +
                        "1     part-time job                    2023-10-31   500.00       car          none\n" +
                        "====================================== IN TRANSACTIONS ==================================" +
                        "====\n"
                , outputStream.toString());

    }

    void validOutList() throws DukeException {
        addOutEntries();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type out");
        command.execute(ui);
        assertEquals("Alright! Displaying 2 transactions.\n" +
                        "===================================== OUT TRANSACTIONS ===================================" +
                        "===\n" +
                        "ID    Description                      Date         Amount       Category     Recurrence\n" +
                        "1     dinner                           2023-10-31   10.50        food         none\n" +
                        "2     pokemon card pack                2023-09-18   10.50        games        none\n" +
                        "===================================== OUT TRANSACTIONS ===================================" +
                        "===\n"
                , outputStream.toString());

    }

    @Test
    void validFilteredOutList() throws DukeException {
        addOutEntries();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type out /category games");
        command.execute(ui);
        assertEquals("Alright! Displaying 1 transaction.\n" +
                        "===================================== OUT TRANSACTIONS =================================" +
                        "=====\n" +
                        "ID    Description                      Date         Amount       Category     Recurrence\n" +
                        "1     pokemon card pack                2023-09-18   10.50        games        none\n" +
                        "===================================== OUT TRANSACTIONS ================================" +
                        "======\n"
                , outputStream.toString());

    }

    @Test
    void execute_listIncomeByWeek_printCurrentWeekTransactions() throws DukeException {
        clearStateManager();
        addInEntriesWithDates();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type in /week");
        command.execute(ui);
        assertEquals("Alright! Displaying 1 transaction.\n" +
                        "====================================== IN TRANSACTIONS ================================" +
                        "======\n" +
                        "ID    Description                      Date         Amount       Goal         Recurrence\n" +
                        "1     part-time job                    2023-10-30   500.00       car          none\n" +
                        "====================================== IN TRANSACTIONS ================================" +
                        "======\n"
                , outputStream.toString());
    }

    @Test
    void execute_listExpenseByWeek_printCurrentWeekTransactions() throws DukeException {
        clearStateManager();
        addOutEntriesWithDates();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type out /week");
        command.execute(ui);
        assertEquals("Alright! Displaying 1 transaction.\n" +
                        "===================================== OUT TRANSACTIONS ==================================" +
                        "====\n" +
                        "ID    Description                      Date         Amount       Category     Recurrence\n" +
                        "1     lunch                            2023-10-30   7.50         food         none\n" +
                        "===================================== OUT TRANSACTIONS ==================================" +
                        "====\n"
                , outputStream.toString());
    }

    void execute_listIncomeByMonth_printCurrentMonthTransactions() throws DukeException {
        clearStateManager();
        addInEntriesWithDates();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type in /month");
        command.execute(ui);
        assertEquals("Alright! Displaying 2 transactions.\n" +
                        "====================================== IN TRANSACTIONS =================================" +
                        "=====\n" +
                        "ID    Description                      Date         Amount       Goal         Recurrence\n" +
                        "1     part-time job                    2023-10-30   500.00       car          none\n" +
                        "2     allowance job                    2023-10-23   300.00       car          none\n" +
                        "====================================== IN TRANSACTIONS ================================" +
                        "======\n"
                , outputStream.toString());
    }

    void execute_listExpenseByMonth_printCurrentMonthTransactions() throws DukeException {
        clearStateManager();
        addOutEntriesWithDates();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type out /month");
        command.execute(ui);
        assertEquals("Alright! Displaying 2 transactions.\n" +
                        "===================================== OUT TRANSACTIONS =================================" +
                        "=====\n" +
                        "ID    Description                      Date         Amount       Category     Recurrence\n" +
                        "1     lunch                            2023-10-30   7.50         food         none\n" +
                        "2     dinner                           2023-10-23   10.50        food         none\n" +
                        "===================================== OUT TRANSACTIONS ===============================" +
                        "=======\n"
                , outputStream.toString());
    }

    @Test
    void execute_listIncomeByWeekAndMonth_printCurrentWeekTransactions() throws DukeException {
        clearStateManager();
        addInEntriesWithDates();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type in /week /month");
        command.execute(ui);
        assertEquals("Alright! Displaying 1 transaction.\n" +
                        "====================================== IN TRANSACTIONS =================================" +
                        "=====\n" +
                        "ID    Description                      Date         Amount       Goal         Recurrence\n" +
                        "1     part-time job                    2023-10-30   500.00       car          none\n" +
                        "====================================== IN TRANSACTIONS ================================" +
                        "======\n"
                , outputStream.toString());
    }

    @Test
    void execute_listExpenseByWeekAndMonth_printCurrentWeekTransactions() throws DukeException {
        clearStateManager();
        addOutEntriesWithDates();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Parser parser = new Parser();
        Ui ui = new Ui(outputStream);
        Command command = parser.parse("list /type out /week /month");
        command.execute(ui);
        assertEquals("Alright! Displaying 1 transaction.\n" +
                        "===================================== OUT TRANSACTIONS =================================" +
                        "=====\n" +
                        "ID    Description                      Date         Amount       Category     Recurrence\n" +
                        "1     lunch                            2023-10-30   7.50         food         none\n" +
                        "===================================== OUT TRANSACTIONS =================================" +
                        "=====\n"
                , outputStream.toString());
    }

}
