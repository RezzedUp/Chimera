/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.plugin.validator.validators;

import org.bukkit.configuration.ConfigurationSection;


/**
 * Represents a validator which checks if a value in the {@code ConfigurationSection} is of a type.
 */
@FunctionalInterface
public interface Validator {
    
    /**
     * Checks if the value associated with the specified key is of a type.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     * @throws ValidationException if the associated value is invalid
     */
    public void validate(ConfigurationSection config, String key);
    
    
    /**
     * A validator which checks if the value associated with the specified key is a boolean.
     */
    public static final Validator BOOLEAN = (config, key) -> {
        if (!config.isBoolean(key)) {
            throw new ValidationException(config, key, "boolean");
        }
    };
    
    /**
     * A validator which checks if the value associated with the specified key is a String.
     */
    public static final Validator STRING = (config, key) -> {
        if (!config.isString(key)) {
            throw new ValidationException(config, key, "String");
        }
    };
    
    /**
     * A validator which checks if the value associated with the specified key is a list of Strings.
     */
    public static final Validator STRING_LIST = (config, key) -> {
        if (!config.isList(key)) {
            throw new ValidationException(config, key, "List of Strings");
        }
    };
    
    /**
     * An empty validator.
     */
    public static final Validator NONE = (config, key) -> {};
    
}
