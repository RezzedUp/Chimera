/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.locale;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import static java.util.Arrays.asList;


public class Locales {
    
    private static final Map<String, Locale> CACHE = new ConcurrentHashMap<>();
    
    private static final Set<String> COUNTRIES = new HashSet<>(asList(Locale.getISOCountries()));
    private static final Set<String> LANGUAGES = new HashSet<>(asList(Locale.getISOLanguages()));
    
    
    public static @Nullable Locale parse(String locale) {
        return parse(locale, () -> null);
    }
    
    public static Locale parseOrDefault(String locale, Locale value) {
        return parse(locale, () -> value);
    }
    
    public static Locale parse(String locale, Supplier<Locale> value) {
        if (CACHE.containsKey(locale)) {
            return CACHE.get(locale);
        }
        
        String[] parts = locale.split("_");
        if (parts.length == 2 && isValidLanguage(parts[0]) && isValidCountry(parts[1])) {
            Locale aLocale = new Locale(parts[0], parts[1]);
            CACHE.put(locale, aLocale);
            return aLocale;
            
        } else {
            return value.get();
        }
    }
    
    
    public static boolean isValidCountry(String country) {
        country = country.toUpperCase();
        return country.length() == 2 && COUNTRIES.contains(country);
    }
    
    public static boolean isValidLanguage(String language) {
        language = language.toLowerCase();
        return language.length() == 2 && LANGUAGES.contains(language);
    }
    
}
