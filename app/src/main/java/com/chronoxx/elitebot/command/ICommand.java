package com.chronoxx.elitebot.command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    default List<String> getAliases() {  // Default parceque pas obligé d'avoir des alias
        return List.of(); // equivalent de Arrays.asList
    }
}
