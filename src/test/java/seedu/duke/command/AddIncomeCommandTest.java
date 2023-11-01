package seedu.duke.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.duke.classes.Income;
import seedu.duke.classes.StateManager;
import seedu.duke.classes.TransactionRecurrence;
import seedu.duke.exception.DukeException;
import seedu.duke.parser.Parser;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddIncomeCommandTest {
    private static final DukeException MISSING_DESC_EXCEPTION = new DukeException("Description cannot be empty...");
    private static final DukeException MISSING_AMT_EXCEPTION = new DukeException("Amount cannot be empty...");
    private static final DukeException BAD_AMOUNT_EXCEPTION = new DukeException("Invalid amount value specified...");
    private static final DukeException BAD_DATE_EXCEPTION = new DukeException("Invalid date specified...");
    private static final DukeException MISSING_GOAL_EXCEPTION = new DukeException("Goal cannot be empty...");
    private static final DukeException BAD_RECURRENCE = new DukeException("Invalid recurrence period specified...");
    private static final DukeException BAD_RECURRENCE_DATE_EXCEPTION = new DukeException(
            "Cannot specify date for recurring transaction" +
                    "to be larger than 1 period in the past..."
    );

    @BeforeEach
    void addCategories() {
        assertDoesNotThrow(() -> {
            new CommandTestCase("goal /add car /amount 5000").evaluate();
            new CommandTestCase("goal /add PS5 /amount 300").evaluate();
        });
    }

    @AfterEach
    void clearStateManager() {
        StateManager.clearStateManager();
    }

    void validInputs() {
        LocalDate date = LocalDate.now();
        CommandTestCase[] testCases = new CommandTestCase[]{
            new CommandTestCase(
                    "in part-time job /amount 500 /goal car",
                    "Nice! The following income has been tracked:\n" +
                            "Description                      Date          Amount        Goal          Recurrence\n" +
                            "part-time job                    2023-10-31    500.00        car           none\n"
            ),
            new CommandTestCase(
                    "in red packet money /amount 50 /goal PS5",
                    "Nice! The following income has been tracked:\n" +
                            "Description                      Date          Amount        Goal          Recurrence\n" +
                            "red packet money                 2023-10-31    50.00         PS5           none\n"
            ),
            new CommandTestCase(
                    "in red packet money /amount 50 /goal PS5 /date 12102000",
                    "Nice! The following income has been tracked:\n" +
                            "Description                      Date          Amount        Goal          Recurrence\n" +
                            "red packet money                 2000-10-12    50.00         PS5           none\n"
            ),
            new CommandTestCase(
                    "in pocket money /amount 50 /goal PS5 /recurrence weekly",
                    "Nice! The following income has been tracked:\n" +
                            "Description                      Date          Amount        Goal          Recurrence\n" +
                            "pocket money                     " + date + "    50.00         PS5           weekly\n",
                    () -> {
                        ArrayList<Income> incomes = StateManager.getStateManager().getAllIncomes();
                        Income lastAddedIncome = incomes.get(incomes.size() - 1);
                        assertEquals(lastAddedIncome.getTransaction().getRecurrence(), TransactionRecurrence.WEEKLY);
                    }
            )
        };
        CommandTestCase.runTestCases(testCases);
    }

    @Test
    void missingGoal() {
        String goal = "missing";
        CommandTestCase tc = new CommandTestCase(
                "in part-time job /amount 500 /goal " + goal,
                new DukeException("Please add '" + goal + "' as a goal first.")
        );
        tc.evaluate();
    }

    @Test
    void missingDescription() {
        CommandTestCase[] testCases = new CommandTestCase[]{
            new CommandTestCase(
                    "in",
                    MISSING_DESC_EXCEPTION
            ),
            new CommandTestCase(
                    "in     ",
                    MISSING_DESC_EXCEPTION
            ),
            new CommandTestCase(
                    "in     /amount -1",
                    MISSING_DESC_EXCEPTION
            ),
            new CommandTestCase(
                    "in /amount 500",
                    MISSING_DESC_EXCEPTION
            ),
            new CommandTestCase(
                    "in /amount 500 /goal car",
                    MISSING_DESC_EXCEPTION
            ),
        };
        CommandTestCase.runTestCases(testCases);
    }

    @Test
    void missingAmount() {
        CommandTestCase[] testCases = new CommandTestCase[]{
            new CommandTestCase(
                    "in part-time job",
                    MISSING_AMT_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /amount",
                    MISSING_AMT_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /amount   ",
                    MISSING_AMT_EXCEPTION
            ),
        };
        CommandTestCase.runTestCases(testCases);
    }

    @Test
    void badAmount() {
        CommandTestCase[] testCases = new CommandTestCase[]{
            new CommandTestCase(
                    "in part-time job /amount -1",
                    BAD_AMOUNT_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /amount -1 /goal car",
                    BAD_AMOUNT_EXCEPTION
            )
        };
        CommandTestCase.runTestCases(testCases);
    }

    @Test
    void badDate() {
        CommandTestCase[] testCases = new CommandTestCase[]{
            new CommandTestCase(
                    "in part-time job /amount 10 /date",
                    BAD_DATE_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /amount 10 /date 32102000",
                    BAD_DATE_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /amount 10 /date 12-10-2000",
                    BAD_DATE_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /amount 10 /date     ",
                    BAD_DATE_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /date     /amount 10",
                    BAD_DATE_EXCEPTION
            ),
        };
        CommandTestCase.runTestCases(testCases);
    }

    @Test
    void missingClassification() {
        CommandTestCase[] testCases = new CommandTestCase[]{
            new CommandTestCase(
                    "in part-time job /amount 500",
                    MISSING_GOAL_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /goal   /amount 500",
                    MISSING_GOAL_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /amount 500 /goal",
                    MISSING_GOAL_EXCEPTION
            ),
            new CommandTestCase(
                    "in part-time job /amount 500 /goal    ",
                    MISSING_GOAL_EXCEPTION
            )
        };
        CommandTestCase.runTestCases(testCases);
    }

    @Test
    void badRecurrence() {
        CommandTestCase[] testCases = new CommandTestCase[]{
            new CommandTestCase(
                    "in pocket money /amount 50 /recurrence   /goal PS5",
                    BAD_RECURRENCE
            ),
            new CommandTestCase(
                    "in pocket money /amount 50 /goal PS5 /recurrence random",
                    BAD_RECURRENCE
            )
        };
        CommandTestCase.runTestCases(testCases);
    }

    void badRecurrenceDate() {
        LocalDate date = LocalDate.now();
        String goodDate = date.minusDays(6).format(Parser.DATE_INPUT_FORMATTER);
        String badDate = date.minusWeeks(1).format(Parser.DATE_INPUT_FORMATTER);
        CommandTestCase[] testCases = new CommandTestCase[] {
            new CommandTestCase(
                    "in pocket money /amount 50 /goal PS5 /recurrence weekly /date " + goodDate,
                    "Nice! The following income has been tracked:\n" +
                            "Description                      Date          Amount        Goal          Recurrence\n" +
                            "pocket money                     2023-10-25    50.00         PS5           weekly\n"
            ),
            new CommandTestCase(
                    "in pocket money /amount 50 /goal PS5 /recurrence weekly /date " + badDate,
                    BAD_RECURRENCE_DATE_EXCEPTION
            ),
        };
        CommandTestCase.runTestCases(testCases);
    }
}
