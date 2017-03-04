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
package com.karuslabs.commons.menu.regions;

import org.bukkit.event.inventory.InventoryType;


/**
 * <code>BoundedRegion</code> related utility methods.
 */
public class BoundedRegionUtility {
    
    /**
     * Determines the length associated with an inventory based on its type.
     * 
     * @param type the inventory type
     * @return the length of the inventory
     * @throws IllegalArgumentException if the inventory is not supported
     */
    public static int getLength(InventoryType type) {
        switch (type) {                
            case CRAFTING:
                return 2;
                
            case DISPENSER:
            case DROPPER:
                return 3;
                
            case CHEST:
            case ENDER_CHEST:
                return 9;
            
            default:
                throw new IllegalArgumentException("Type: " + type + " is not supported");
        }
    }
    
}
