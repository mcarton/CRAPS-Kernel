package org.mmek.craps.crapsdb;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpCommand implements Command {
    ArrayList<Command> commands;

    Pattern pattern = Pattern.compile("help +(.+)");

    HelpCommand(ArrayList<Command> commands) { this.commands = commands; }

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
            for (Command command : commands) {
                if (cmd.equals(command.name())) {
                    System.out.println(command.help());
                }
            }
        }
        else {
            System.out.println("Available commands:");
            for (Command command : commands) {
                System.out.println("\t- " + command.name());
            }

            System.out.println("\t- exit");
        }
    }
}
