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

import com.karuslabs.commons.util.collections.Trie;

import com.mojang.brigadier.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.*;
import com.mojang.brigadier.suggestion.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import org.bukkit.enchantments.Enchantment;


public class EnchantmentType implements WordType<Enchantment> {
    
    static final Trie<Enchantment> ENCHANTMENTS;
    static final DynamicCommandExceptionType EXCEPTION = new DynamicCommandExceptionType(enchantment -> new LiteralMessage("Unknown enchantment: " + enchantment));
    static final Collection<String> EXAMPLES = List.of("arrow_damage", "channeling");
    
    static {
        ENCHANTMENTS = new Trie<>();
        for (var enchantment : Enchantment.values()) {
            ENCHANTMENTS.put(enchantment.getKey().getKey(), enchantment);
        }
    }
    
    
    @Override
    public Enchantment parse(StringReader reader) throws CommandSyntaxException {
        var name = reader.readUnquotedString().toLowerCase();
        var enchantment = ENCHANTMENTS.get(name);
        
        if (enchantment == null) {
            throw EXCEPTION.createWithContext(reader, name);
        }
        
        return enchantment;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (var enchantment : ENCHANTMENTS.prefixedKeys(builder.getRemaining())) {
            builder.suggest(enchantment);
        }
        
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
    
}
