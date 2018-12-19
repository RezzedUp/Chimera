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
package com.karuslabs.commons.command.caches;

import com.google.common.cache.*;

import com.mojang.brigadier.ParseResults;

import java.util.concurrent.TimeUnit;

import org.bukkit.command.CommandSender;

import org.checkerframework.checker.nullness.qual.Nullable;


public class SegmentedCache implements ResultCache {
    
    private Cache<String, ParseResults<CommandSender>> cache;
    private Cache<String, ParseResults<CommandSender>> frequent;
    
    
    public SegmentedCache() {
        this(CacheBuilder.newBuilder().build(), CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build());
    }
    
    public SegmentedCache(Cache<String, ParseResults<CommandSender>> cache, Cache<String, ParseResults<CommandSender>> frequent) {
        this.cache = cache;
        this.frequent = frequent;
    }
    
    
    
    @Override
    public @Nullable ParseResults<CommandSender> get(String input) {
        var results = cache.getIfPresent(input);
        if (results == null) {
            return frequent.getIfPresent(input);
        }
        
        return results;
    }

    @Override
    public void put(String input, ParseResults<CommandSender> results) {
        cache.put(input, results);
    }

    @Override
    public void remove(String input) {
        cache.invalidate(input);
        frequent.invalidate(input);
    }

    @Override
    public void cache(String input, ParseResults<CommandSender> results) {
        frequent.put(input, results);
    }
    
}
