package seedu.duke;

import seedu.duke.classes.StateManager;
import seedu.duke.classes.TransactionRecurrence;
import seedu.duke.command.Command;
import seedu.duke.command.ExitCommand;
import seedu.duke.exception.DukeException;
import seedu.duke.parser.Parser;
import seedu.duke.storage.Storage;
import seedu.duke.ui.Ui;

public class Duke {

    private static Ui ui;
    private static Storage storage;

    public Duke() {
        ui = new Ui();
        storage = new Storage();
    }

    public void load() {
        try {
            storage.load();
            syncTransactions();
        } catch (DukeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void save() throws DukeException {
        syncTransactions();
        storage.save();
    }

    public void syncTransactions() {
        TransactionRecurrence.generateRecurrentTransactions(
                StateManager.getStateManager().getAllIncomes(),
                StateManager.getStateManager().getAllExpenses()
        );
    }

    public void run() {
        ui.printGreeting();
        String userInput;
        while (true) {
            userInput = ui.readUserInput();
            try {
                Command command = new Parser().parse(userInput);
                command.execute(ui);

                save();
                if (command instanceof ExitCommand) {
                    break;
                }
            } catch (DukeException e) {
                System.out.println(e.getMessage());
            }
        }
        ui.close();
    }

    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        System.setProperty("line.separator", "\n");
        Duke duke = new Duke();
        duke.load();
        duke.run();
    }
}
