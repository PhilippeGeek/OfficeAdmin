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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

/**
 * Assigned plan to an Object.
 */
public class AssignedPlan extends DirectoryObject {

    /**
     * The date and time at which the plan was assigned; for example: 2013-01-02T19:32:30Z.
     */
    public final ObjectProperty<Date> assignedTimestamp = new SimpleObjectProperty<>();

    /**
     * For example, “Enabled”.
     */
    public final SimpleStringProperty capabilityStatus = new SimpleStringProperty();

    /**
     * The name of the service; for example, “AccessControlServiceS2S”.
     */
    public final SimpleStringProperty service = new SimpleStringProperty();

    /**
     * A GUID that identifies the service plan.
     */
    public final SimpleStringProperty servicePlanId = new SimpleStringProperty();

}
