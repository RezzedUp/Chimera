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
package com.karuslabs.commons.util;

import java.util.function.Supplier;


/**
 * Represents a type that is reusable in conjuction with functions which return instances of the same type.
 * <P>
 * For example:
 * {@code 
 *  
 * }
 */
public interface Memoisable {
    
    /**
     * Returns a {@code Supplier} which returns the same specified {@code value} when invoked.
     * 
     * @param <T> the type of the value
     * @param value the value to memoise
     * @return a supplier which returns the same specified value when invoked
     */
    public static<T extends Memoisable> Supplier<T> memoise(T value) {
        return () -> value;
    }
    
    /**
     * Returns a {@code Supplier} which returns the specified {@code value.
     * 
     * @param <T> the type of the value
     * @param value the value to be memoised
     * @return a supplier which will always return the specified value
     */
    public static<T> Supplier<T> memoiseUnchecked(T value) {
        return () -> value;
    }
    
}
