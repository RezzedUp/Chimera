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
package com.karuslabs.commons.items;

import com.karuslabs.commons.xml.*;

import java.util.*;

import org.jdom2.Element;


/**
 * Represents a parser for parsing XML documents to <code>ItemStack</code>s.
 */
public class ItemParser extends Parser<Map<String, ValueStack>> {
    
    private Component<ValueStack> component;
    
     
    /**
     * Creates a new parer with the component for parsing <code>&lt;item&gt;</code> nodes.
     * 
     * @param component the component used to parse &lt;item&gt; nodes
     */
    public ItemParser(Component<ValueStack> component) {
        super(null);
        schemaPath = getClass().getClassLoader().getResource("items/items.xsd").getPath();
        this.component = component;
    }
    
    /**
     * Creates a new parser with the component for parsing <code>&lt;item&gt;</code> nodes and XML schema path specified.
     * 
     * @param component the component used to parse &lt;item&gt; nodes
     * @param schemaPath the XML schema path
     */
    public ItemParser(Component<ValueStack> component, String schemaPath) {
        super(schemaPath);
        this.component = component;
    }

    
    @Override
    protected Map<String, ValueStack> parse(Element root) {
        Map<String, ValueStack> items = new HashMap<>();
        
        root.getChildren("item").forEach(element -> {
            ValueStack item  = component.parse(element);
            items.put(item.getName(), item);
        });
        
        return items;
    }
    
}
