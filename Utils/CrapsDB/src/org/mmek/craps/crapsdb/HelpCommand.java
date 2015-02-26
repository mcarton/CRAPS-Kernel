package org.mmek.craps.crapsdb;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpCommand implements Command {
    List<Command> commands;

    Pattern pattern = Pattern.compile("help +(.+)");

    HelpCommand(List<Command> commands) { this.commands = commands; }

    public String help() {
        return "help [command]";
    }

    public String name() {
        return "help";
    }

    public void run(String cmd) {
        Matcher matcher = pattern.matcher(cmd);
        if (matcher.matches()) {
            cmd = matcher.group(1);

            if (cmd.equals("exit")) {
                System.out.println(
                    "exit crapsdb, the program will continue on the board"
                );
            }

            for (Command command : commands) {
                if (cmd.equals(command.name())) {
                    if (command.help() == null) {
                        System.out.println(
                            cmd + " is so simple it does not even need help"
                        );
                    }
                    else {
                        System.out.println(command.help());
                    }
                }
            }
        }
        else {
            System.out.println("Available commands:");
            for (Command command : commands) {
                System.out.println("\t- " + command.name());
            }

            System.out.println(
                "\t- exit"
              + "\n"
              + "See \"help command\" for details."
            );
        }
    }
}
