/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import junitparams.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class OptionFutureTest {
    
    private static Object object = new Object();;
    
    @Rule
    public ExpectedException exception;
    
    private OptionalFuture<Object> task;
    
    
    public OptionFutureTest() {
        exception = ExpectedException.none();
        task = spy(new OptionalFuture<>(() -> object));
    }
    
    
    @Test
    @Parameters({"true, 1", "false, 0"})
    public void ifDone(boolean done, int times) {
        doReturn(done).when(task).isDone();
        doReturn(object).when(task).getUnchecked();
        Consumer consumer = mock(Consumer.class);
        
        task.ifDone(consumer);
        
        verify(consumer, times(times)).accept(object);
    }
    
    
    @Test
    @Parameters
    public void getUnchecked(Exception thrown, RuntimeException expected) throws InterruptedException, ExecutionException {
        exception.expect(expected.getClass());
        
        doThrow(thrown).when(task).get();
        
        task.getUnchecked();
    }
    
    protected Object[] parametersForGetUnchecked() {
        return new Object[] {
            new Object[] {new InterruptedException(), new UncheckedInterruptedException()},
            new Object[] {new ExecutionException(null), new UncheckedExecutionException()}
        };
    }
    
    
    @Test
    @Parameters
    public void getIfDone(boolean done, Object object, Object expected) {
        doReturn(done).when(task).isDone();
        doReturn(object).when(task).getUnchecked();
        
        Object returned = task.getIfDone();
        
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForGetIfDone() {
        return new Object[] {
            new Object[] {true, object, object},
            new Object[] {false, object, null}
        };
    }
    
    
    @Test
    @Parameters
    public void getOrDefault(boolean done, Object object, Object defaultArgument, Object expected) throws InterruptedException, ExecutionException {
        doReturn(done).when(task).isDone();
        doReturn(object).when(task).get();
        
        Object returned = task.getOrDefault(defaultArgument);
        
        assertEquals(expected, returned);
    }
    
    protected Object[] parametersForGetOrDefault() {
        return new Object[] {
            new Object[] {true, object, null, object},
            new Object[] {false, null, object, object}
        };
    }
    
    
    @Test
    public void getOrThrow() {
        doReturn(true).when(task).isDone();
        doReturn(null).when(task).getUnchecked();
        
        task.getOrThrow(null);
        
        verify(task, times(1)).getUnchecked();
    }
    
    
    @Test
    public void getOrThrow_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        
        doReturn(false).when(task).isDone();
        
        task.getOrThrow(IllegalArgumentException::new);
    }
    
}
