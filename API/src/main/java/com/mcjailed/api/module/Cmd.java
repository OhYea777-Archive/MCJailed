package com.mcjailed.api.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
public @interface Cmd {

    /**
     * The name of the command.
     */
    String value();

    /**
     * Alternative aliases for the command.
     */
    String[] aliases();

}
