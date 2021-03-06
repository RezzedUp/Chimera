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
package com.karuslabs.commons.command.synchronization;

import com.karuslabs.commons.command.tree.Mapper;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;

import net.minecraft.server.v1_13_R2.*;

import org.checkerframework.checker.nullness.qual.Nullable;


class SynchronizationMapper extends Mapper<CommandListenerWrapper, ICompletionProvider> {
    
    static final SynchronizationMapper MAPPER = new SynchronizationMapper();
    
    @Override
    protected @Nullable SuggestionProvider<ICompletionProvider> suggestions(ArgumentCommandNode<CommandListenerWrapper, ?> command) {
        // Fucking nasty workaround using raw types which Mojang abused.
        // It only works because CommandListenerWrapper is the sole implementation of ICompleteionProvider.
        SuggestionProvider provider = command.getCustomSuggestions();
        return provider == null ? null: CompletionProviders.b(provider);
    }
    
}
