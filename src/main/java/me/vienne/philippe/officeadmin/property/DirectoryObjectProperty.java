/*
 * Copyright (C) 2015  Philippe Vienne
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

package me.vienne.philippe.officeadmin.property;

import javafx.beans.property.ObjectPropertyBase;
import me.vienne.philippe.officeadmin.api.DirectoryObject;

/**
 * Property which contain a DirectoryObject.
 */
public class DirectoryObjectProperty extends ObjectPropertyBase<DirectoryObject> {

    private final Class<? extends DirectoryObject> propertyClass;

    public DirectoryObjectProperty(Class<? extends DirectoryObject> propertyClass){
        this.propertyClass = propertyClass;
    }

    public Class getPropertyClass(){
        return propertyClass;
    }

    @Override
    public Object getBean() {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
