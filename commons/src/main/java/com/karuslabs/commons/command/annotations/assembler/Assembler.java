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
package com.karuslabs.commons.command.annotations.assembler;

import com.karuslabs.commons.command.*;
import com.karuslabs.commons.command.annotations.*;
import com.karuslabs.commons.util.collections.TokenMap;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.*;

import java.lang.invoke.*;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.*;


public class Assembler<T> {
    
    static final MethodType COMMAND_SIGNATURE = MethodType.methodType(int.class, CommandContext.class);
    static final Class<?>[] COMMAND_PARAMETERS = new Class<?>[] {CommandContext.class};
    
    static final MethodType EXECUTABLE_SIGNATURE = MethodType.methodType(void.class, DefaultableContext.class);
    static final Class<?>[] EXECUTABLE_PARAMETERS = new Class<?>[] {DefaultableContext.class};
    
    
    private Lookup lookup;
    private CommandAssembler assembler;
    private RootCommandNode<T> container;
    private TokenMap<String, Object> bindings;
    
    
    public Assembler() {
        lookup = MethodHandles.lookup();
        assembler = new CommandAssembler(container = new RootCommandNode<>(), bindings = TokenMap.of());
    }
    
    
    public Map<String, CommandNode<T>> assemble(Object annotated) {
        try {
            var type = annotated.getClass();
            
            bind(annotated);
            assembler.assemble(type, type.getAnnotationsByType(Literal.class), null);
            assembler.assemble(type, type.getAnnotationsByType(Argument.class), null);
            generate(annotated);
            
            return null;
          
        } finally {
            bindings.map().clear();
        }
    }
    
    
    protected void bind(Object annotated) {
        try {
            for (var field : annotated.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                
                var annotation = field.getAnnotation(Bind.class);
                if (annotation == null) {
                    continue;
                }
                
                var type = field.getType();
                if (!type.isAssignableFrom(ArgumentType.class) && !type.isAssignableFrom(SuggestionProvider.class)) {
                    throw new IllegalArgumentException("Invalid @Bind annotated field:  " + field.getName() + ", field must be an ArgumentType or SuggestionProvider");
                }
                
                var keyType = type.isAssignableFrom(ArgumentType.class) ? ArgumentType.class : SuggestionProvider.class;
                var name = annotation.value().isEmpty() ? field.getName() : annotation.value();
                
                if (bindings.map().put(TokenMap.key(name, keyType), field.get(annotated)) != null) {
                    throw new IllegalArgumentException("@Bind(" + annotation.value() + ") on the same type already exists");
                }
            }
            
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to access fields in: " + annotated.getClass(), e);
        }
    }
    
    
    protected void generate(Object annotated) {
        var type = annotated.getClass();
        for (var method : type.getDeclaredMethods()) {
            method.setAccessible(true);

            var literals = method.getAnnotationsByType(Literal.class);
            var arguments = method.getAnnotationsByType(Argument.class);
            if (literals.length == 0 && arguments.length == 0) {
                continue;
            }
            
            var command = emit(annotated, method); 
            assembler.assemble(type, literals, command);
            assembler.assemble(type, arguments, command);
        }
    }
    
    
    protected Command<T> emit(Object annotated, Method method) {
        var returned = method.getReturnType();
        var parameters = method.getParameterTypes();
        
        if (returned == int.class && Arrays.equals(parameters, COMMAND_PARAMETERS)) {
            return emit(annotated, method, COMMAND_SIGNATURE, Command.class, "run");
            
        } else if (returned == void.class && Arrays.equals(parameters, EXECUTABLE_PARAMETERS)) {
            return emit(annotated, method, EXECUTABLE_SIGNATURE, Executable.class, "execute");
            
        } else {
            throw new IllegalArgumentException("Invalid signature: " + method.getName() + " in " + annotated.getClass() + ", signaure must match Command or Executable");
        }
    }
    
    protected Command<T> emit(Object annotated, Method method, MethodType signature, Class<?> target, String targetMethod) {
        try {
            var handle = lookup.unreflect(method);
            var conversion = MethodType.methodType(target, annotated.getClass());
            var lambda = LambdaMetafactory.metafactory(lookup, targetMethod, conversion, signature, handle, signature).getTarget();
            
            return (Command<T>) lambda.invoke(annotated);
            
        } catch (Throwable e) {
            throw new RuntimeException("Failed to generate lambda from " + annotated.getClass(), e);
        }
    }
    
}
