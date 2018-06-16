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
package com.karuslabs.commons.command.annotation.processors;

import com.karuslabs.annotations.processors.AnnotationProcessor;
import com.karuslabs.commons.command.CommandExecutor;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.lang.model.type.*;



@SupportedAnnotationTypes({
    "com.karuslabs.commons.command.annotation.Information",
    "com.karuslabs.commons.command.annotation.Namespace", "com.karuslabs.commons.command.annotation.Namespaces",
    "com.karuslabs.commons.command.annotation.Literal", "com.karuslabs.commons.command.annotation.Literals",
    "com.karuslabs.commons.command.annotation.Registered", "com.karuslabs.commons.command.annotation.Registrations"
})
public class CommandProcessor extends AnnotationProcessor {
    
    TypeMirror expected;
    
    
    @Override
    protected void initialise(ProcessingEnvironment environment) {
        expected = environment.getElementUtils().getTypeElement(CommandExecutor.class.getName()).asType();
    }

    
    @Override
    protected void process(Element element) { 
        if (!types.isAssignable(element.asType(), expected)) {
            error(element, "Invalid annotated type: " + element.asType().toString() + ", type must implement " + CommandExecutor.class.getName());
        }
    }

}
