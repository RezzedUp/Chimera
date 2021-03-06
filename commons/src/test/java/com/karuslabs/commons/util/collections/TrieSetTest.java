/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.util.collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class TrieSetTest {
    
    TrieSet set = new TrieSet();
    
    
    TrieSetTest() {
        set.add("app");
        set.add("apple");
        set.add("application");
    }
    
    
    @Test
    void startsWith() {
        var prefixed = set.startsWith("appl");
        
        assertEquals(2, prefixed.size());
        assertTrue(prefixed.contains("apple"));
        assertTrue(prefixed.contains("application"));
    }
    
    
    @Test
    void add() {
        assertTrue(set.add("lol"));
        assertFalse(set.add("lol"));
    }
    
    
    @Test
    void contains() {
        set.add("lol");
        
        assertTrue(set.contains("lol"));
        assertFalse(set.contains("lo"));
    }
    
    
    @Test
    void remove() {
        assertTrue(set.remove("app"));
        
        assertEquals(2, set.size());
        assertFalse(set.remove("app"));
    }
    
    
    @Test
    void iterator() {
        set = new TrieSet();
        set.add("lol");
        
        for (var string : set) {
            assertEquals("lol", string);
        }
    }

} 
