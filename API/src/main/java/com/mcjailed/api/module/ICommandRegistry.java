package com.mcjailed.api.module;

public interface ICommandRegistry {

    /**
     * Registers a command handler.
     *
     * @param commandHandler The object to handle commands.
     */
    void registerCommandHandler(Object commandHandler);

    /**
     * Deregisters a command handler.
     *
     * @param commandHandler The object to be deregistered from handling commands.
     */
    void deregisterCommandHandler(Object commandHandler);

}
