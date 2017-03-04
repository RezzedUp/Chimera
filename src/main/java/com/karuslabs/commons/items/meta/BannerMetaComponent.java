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
package com.karuslabs.commons.items.meta;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.*;
import org.bukkit.inventory.meta.BannerMeta;

import org.jdom2.Element;


/**
 * Represents a component for parsing <code>&ltbanner-meta&gt</code> nodes.
 */
public class BannerMetaComponent extends ItemMetaComponent<BannerMeta> {
    
    /**
     * Parses a <code>&ltbanner-meta&gt</code> node.
     * 
     * @param root the node
     * @param meta the meta to set
     */
    @Override
    public void parse(Element root, BannerMeta meta) {
        super.parse(root, meta);
        
        Element patterns = root.getChild("patterns");
        if (patterns != null) {
            patterns.getChildren("pattern").forEach(element -> {
                PatternType type = PatternType.valueOf(element.getAttribute("type").getValue());
                DyeColor color = DyeColor.valueOf(element.getAttribute("color").getValue());

                meta.addPattern(new Pattern(color, type));
            });
        }
    }
   
}
