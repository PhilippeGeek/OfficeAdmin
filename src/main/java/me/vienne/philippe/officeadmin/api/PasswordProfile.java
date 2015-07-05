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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * PasswordProfile type contains the password profile associated with a user.
 */
public class PasswordProfile extends DirectoryObject {

    /**
     * The password for the user. This property is required when a user is created.
     * It can be updated, but the user will be required to change the password on the next login.
     * The password must satisfy minimum requirements as specified by the user’s PasswordPolicies
     * property. By default, a strong password is required.
     */
    public final SimpleStringProperty password = new SimpleStringProperty();

    /**
     * true if the user must change her password on the next login; otherwise false.
     */
    public final SimpleBooleanProperty forceChangePasswordNextLogin = new SimpleBooleanProperty();

}
