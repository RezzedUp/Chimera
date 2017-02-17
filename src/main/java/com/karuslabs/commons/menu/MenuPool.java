/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karuslabs.commons.menu;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;


public class MenuPool implements Listener {
    
    public static final MenuPool INSTANCE = new MenuPool();
    
    
    private ConcurrentHashMap<String, Menu> pooledMenus;
    private WeakHashMap<HumanEntity, Menu> menus;
    
    
    protected MenuPool() {
        pooledMenus = new ConcurrentHashMap<>();
        menus = new WeakHashMap<>();
    }
    
    
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (menus.containsKey(event.getWhoClicked())) {
            menus.get(event.getWhoClicked()).onClick(event);
        }
    }
    
    
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (menus.containsKey(event.getWhoClicked())) {
            menus.get(event.getWhoClicked()).onDrag(event);
        }
    }

    
    public ConcurrentHashMap<String, Menu> getPooledMenus() {
        return pooledMenus;
    }

    
    public Map<HumanEntity, Menu> getMenus() {
        return menus;
    }
    
}