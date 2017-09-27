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
package com.karuslabs.commons.util.concurrent;

import com.karuslabs.commons.annotation.Blocking;

import java.util.concurrent.*;
import javax.annotation.*;


public interface Promise<T> extends Future<T> {
    
    public static <T> Promise<T> of(Future<T> future) {
        return new ProxiedPromise<>(future);
    }
    
    
    @Blocking
    public default @Nullable T obtain() {
        try {
            return get();
            
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
            
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
        }
    }
    
    @Blocking
    public default @Nullable T obtain(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
            
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
            
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
            
        } catch (TimeoutException e) {
            throw new UncheckedTimeoutException(e);
        }
    }
    
    
    static class ProxiedPromise<T> implements Promise<T> {

        private final Future<T> future;

        ProxiedPromise(Future<T> future) {
            this.future = future;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return future.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return future.isCancelled();
        }

        @Override
        public boolean isDone() {
            return future.isDone();
        }

        @Override
        @Blocking
        public @Nullable T get() throws InterruptedException, ExecutionException {
            return future.get();
        }

        @Override
        @Blocking
        public @Nullable T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return future.get(timeout, unit);
        }

    }
    
}