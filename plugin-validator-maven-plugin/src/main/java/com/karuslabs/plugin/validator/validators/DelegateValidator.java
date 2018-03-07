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
 * A concrete subclass of {@code ConfigurationProcessor} which delegates checking of 
 * each value in a {@code ConfigurationSection} to a {@code Processor}.
 */
public class DelegateValidator extends ConfigurationValidator {
    
    private Validator validator;
    
    
    /**
     * Constructs a {@code DelegateValidator} with the specified validator.
     * 
     * @param validator the validator to which processing is delegated
     */
    public DelegateValidator(Validator validator) {
        this.validator = validator;
    }
    
    
    /**
     * Delegates checking of each individual key in a {@code ConfigurationSection} to a {@code Validator}.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     */
    @Override
    protected void validateConfigurationSection(ConfigurationSection config, String key) {
        ConfigurationSection section = config.getConfigurationSection(key);
        section.getKeys(false).forEach(aKey -> {
            validator.validate(section, aKey);
        });
    }
    
}
