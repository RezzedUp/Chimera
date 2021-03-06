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
package com.karuslabs.commons.command;

import com.karuslabs.commons.command.synchronization.Synchronizer;
import com.karuslabs.commons.command.tree.nodes.*;

import net.minecraft.server.v1_13_R2.*;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.scheduler.CraftScheduler;
import org.bukkit.plugin.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DispatcherTest {
    
    Dispatcher dispatcher;
    Plugin plugin = when(mock(Plugin.class).getName()).thenReturn("test").getMock();
    CraftServer craftserver = mock(CraftServer.class);
    MinecraftServer server = mock(MinecraftServer.class);
    SimpleCommandMap map = when(mock(SimpleCommandMap.class).register(any(String.class), any())).thenReturn(true).getMock();
    CraftScheduler scheduler = mock(CraftScheduler.class);
    PluginManager manager = mock(PluginManager.class);
    ServicesManager services = mock(ServicesManager.class);
    CommandDispatcher wrapper = mock(CommandDispatcher.class);
    com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> internal = new com.mojang.brigadier.CommandDispatcher();
    
    
    @BeforeEach
    void before() {
        when(plugin.getServer()).thenReturn(craftserver);
        when(craftserver.getServer()).thenReturn(server);
        when(craftserver.getCommandMap()).thenReturn(map);
        when(craftserver.getScheduler()).thenReturn(scheduler);
        when(craftserver.getPluginManager()).thenReturn(manager);
        when(craftserver.getServicesManager()).thenReturn(services);
        
        server.server = craftserver;
        server.commandDispatcher = when(wrapper.a()).thenReturn(internal).getMock();
        
        dispatcher = spy(Dispatcher.of(plugin));
    }

    
    @Test
    void of() {
        reset(manager);
        
        var dispatcher = Dispatcher.of(plugin);
        assertSame(dispatcher, ((Root) dispatcher.getRoot()).dispatcher());
    }
    
    
    @Test
    void register() {
        var a = dispatcher.register(Literal.of("a"));
        
        assertSame(a, dispatcher.getRoot().getChild("a"));
    }
    
    
    @Test
    void update() {
        dispatcher.getRoot().addChild(Literal.of("a").build());
        dispatcher.synchronizer = mock(Synchronizer.class);
        
        dispatcher.update();
        
        assertNotNull(dispatcher.dispatcher.getRoot().getChild("a"));
        verify(dispatcher.synchronizer()).synchronize();
    }
    
    
    @Test
    void update_server_reload() {
        dispatcher.getRoot().addChild(Literal.of("a").build());
        dispatcher.dispatcher = null;
        
        dispatcher.update(null);
        
        assertNotNull(dispatcher.dispatcher.getRoot().getChild("a"));
        assertSame(internal, dispatcher.dispatcher);
    }
    
} 
