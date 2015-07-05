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

import me.vienne.philippe.officeadmin.api.DirectoryObject;
import javafx.collections.ModifiableObservableListBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philippe Vienne on 05/07/2015.
 */
public class DirectoryObjectListProperty extends ModifiableObservableListBase<DirectoryObject> {

    private final Class<? extends DirectoryObject> propertyClass;

    public DirectoryObjectListProperty(Class<? extends DirectoryObject> propertyClass){
        super();
        this.propertyClass = propertyClass;
    }

    public Class getPropertyClass(){
        return propertyClass;
    }

    private final List<DirectoryObject> delegate = new ArrayList<>();

    public DirectoryObject get(int index) {
        return delegate.get(index);
    }

    public int size() {
        return delegate.size();
    }

    protected void doAdd(int index, DirectoryObject element) {
        delegate.add(index, element);
    }

    protected DirectoryObject doSet(int index, DirectoryObject element) {
        return delegate.set(index, element);
    }

    protected DirectoryObject doRemove(int index) {
        return delegate.remove(index);
    }
}
