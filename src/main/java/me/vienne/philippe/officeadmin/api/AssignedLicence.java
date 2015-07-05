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

package me.vienne.philippe.officeadmin.api;

import javafx.beans.property.ListPropertyBase;
import javafx.beans.property.SimpleStringProperty;

/**
 * Assignation of licences to an Object.
 * The {@link User#assignedLicences} property of the User entity is a collection of AssignedLicense.
 */
public class AssignedLicence extends DirectoryObject {

    /**
     * A collection of the unique identifiers for plans that have been disabled.
     */
    public final ListPropertyBase<String> disabledPlans = new ListPropertyBase<String>() {
        @Override
        public Object getBean() {
            return AssignedLicence.this;
        }

        @Override
        public String getName() {
            return "disabledPlans";
        }
    };
    /**
     * The unique identifier for the SKU.
     */
    public final SimpleStringProperty skuId = new SimpleStringProperty();

}
