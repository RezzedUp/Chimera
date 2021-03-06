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
package com.karuslabs.commons.command.types;

import com.karuslabs.commons.util.Position;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


@ExtendWith(MockitoExtension.class)
class Position3DTypeTest {
    
    Position3DType type = new Position3DType();
    
    
    @Test
    void parse() throws CommandSyntaxException {
        assertEquals(new Position(1, 2, 3).relative(Position.Z, true), type.parse(new StringReader("1 2 ~3")));
    }
    
    
    @ParameterizedTest
    @MethodSource("suggest_parameters")
    void suggest(String[] parts, SuggestionsBuilder expected) {
        var suggestions = new SuggestionsBuilder(expected.getInput(), 0);
        type.suggest(suggestions, null, parts);
        
        assertEquals(expected.build(), suggestions.build());
    }
    
    static Stream<Arguments> suggest_parameters() {
        return Stream.of(
            of(new String[] {}, new SuggestionsBuilder("", 0).suggest("~").suggest("~ ~").suggest("~ ~ ~")),
            of(new String[] {}, new SuggestionsBuilder(" ", 0).suggest("~").suggest("~ ~").suggest("~ ~ ~")),
            of(new String[] {}, new SuggestionsBuilder("  ", 0).suggest("~").suggest("~ ~").suggest("~ ~ ~")),
            of(new String[] {"^1"}, new SuggestionsBuilder("^1", 0).suggest("^1 ^").suggest("^1 ^ ^")),
            of(new String[] {"~1"}, new SuggestionsBuilder("~1", 0).suggest("~1 ~").suggest("~1 ~ ~")),
            of(new String[] {"~1", "2"}, new SuggestionsBuilder("~1 2", 0).suggest("~1 2 ~"))
        );
    }
    
    
    @Test
    void getExamples() {
        assertEquals(List.of("0 0 0", "0.0 0.0 0.0", "^ ^ ^", "~ ~ ~"), type.getExamples());
    }

} 
