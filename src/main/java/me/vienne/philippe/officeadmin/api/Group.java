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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.vienne.philippe.officeadmin.property.DirectoryObjectListProperty;
import me.vienne.philippe.officeadmin.property.SimpleDateProperty;

/**
 * Represents an Azure Active Directory group.
 */
public class Group extends DirectoryObject{

    /**
     * An optional description for the group.
     */
    public final SimpleStringProperty description = new SimpleStringProperty();

    /**
     * Indicates whether this object was synced from the on-premises directory.
     */
    public final SimpleBooleanProperty dirSyncEnabled = new SimpleBooleanProperty();

    /**
     * The display name for the group. This property is required when a group
     * is created and it cannot be cleared during updates.
     */
    public final SimpleStringProperty displayName = new SimpleStringProperty();

    /**
     * Indicates the last time at which the object was synced
     * with the on-premises directory.
     */
    public final SimpleDateProperty lastDirSyncTime = new SimpleDateProperty();

    /**
     * The SMTP address for the group,
     * for example, "serviceadmins@contoso.onmicrosoft.com".
     */
    public final SimpleStringProperty mail = new SimpleStringProperty();

    /**
     * Specifies whether the group is mail-enabled.
     * If the securityEnabled property is also true, the group is a mail-enabled
     * security group; otherwise, the group is a Microsoft Exchange distribution
     * group. Only (pure) security groups can be created using Azure AD Graph.
     * For this reason, the property must be set false when creating a group
     * and it cannot be updated using Azure AD Graph.
     */
    public final SimpleBooleanProperty mailEndabled = new SimpleBooleanProperty();

    /**
     * The mail alias for the group.
     * This property must be specified when a group is created.
     */
    public final SimpleStringProperty mailNickName = new SimpleStringProperty();

    /**
     * Contains the on-premises security identifier (SID) for the group that was
     * synchronized from on-premises to the cloud.
     */
    public final SimpleStringProperty onPremisesSecurityIdentifier = new SimpleStringProperty();

    /**
     * A collection of error details that are preventing this group from
     * being provisioned successfully.
     * Notes: not nullable.
     */
    public final DirectoryObjectListProperty provisioningErrors = new DirectoryObjectListProperty(ProvisioningError.class);

    public final ObservableList<String> proxyAddresses = FXCollections.observableArrayList();

    /**
     * Specifies whether the group is a security group.
     * If the mailEnabled property is also true, the group is a mail-enabled
     * security group; otherwise it is a security group. Only (pure) security
     * groups can be created using Azure AD Graph. For this reason, the
     * property must be set true when creating a group.
     */
    public final SimpleBooleanProperty securityEnabled = new SimpleBooleanProperty();

}
