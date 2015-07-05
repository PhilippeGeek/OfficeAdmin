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

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import me.vienne.philippe.officeadmin.property.DirectoryObjectListProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * It's a User from Office 365.
 * To get more details about data stored, see: https://msdn.microsoft.com/en-us/library/azure/hh974483.aspx
 */
public class User extends DirectoryObject{

    public final SimpleBooleanProperty accountEnabled = new SimpleBooleanProperty();
    public final DirectoryObjectListProperty assignedLicences = new DirectoryObjectListProperty(AssignedLicence.class);
    public final DirectoryObjectListProperty assignedPlans = new DirectoryObjectListProperty(AssignedPlan.class);
    public final SimpleStringProperty city = new SimpleStringProperty();
    public final SimpleStringProperty country = new SimpleStringProperty();
    public final SimpleStringProperty departement = new SimpleStringProperty();
    public final SimpleBooleanProperty dirSyncEnabled = new SimpleBooleanProperty();
    public final SimpleStringProperty givenName = new SimpleStringProperty();
    public final SimpleStringProperty displayName = new SimpleStringProperty();
    public final SimpleStringProperty facsimileTelephoneNumber = new SimpleStringProperty();
    public final SimpleStringProperty immutableId = new SimpleStringProperty();
    public final SimpleStringProperty jobTitle = new SimpleStringProperty();
    public final SimpleStringProperty mail = new SimpleStringProperty();
    public final SimpleStringProperty mailNickName = new SimpleStringProperty();
    public final SimpleStringProperty mobile = new SimpleStringProperty();
    public final SimpleStringProperty onPremisesSecurityIdentifier = new SimpleStringProperty();
    public final ObservableList<String> otherMails = FXCollections.observableList(new ArrayList<>());
    public final SimpleStringProperty passwordPolices = new SimpleStringProperty();
    public final ObjectProperty<PasswordProfile> passwordProfile = new SimpleObjectProperty<PasswordProfile>();
    public final SimpleStringProperty physicalDeliveryOfficeName = new SimpleStringProperty();
    public final SimpleStringProperty preferredLanguage = new SimpleStringProperty();
    public final DirectoryObjectListProperty provisionedPlans = new DirectoryObjectListProperty(ProvisionedPlan.class);
    public final DirectoryObjectListProperty provisioningErrors = new DirectoryObjectListProperty(ProvisioningError.class);
    public final ObservableList<String> proxyAddresses =FXCollections.observableList(new ArrayList<>());
    public final SimpleStringProperty sipProxyAddress = new SimpleStringProperty();
    public final SimpleStringProperty state = new SimpleStringProperty();
    public final SimpleStringProperty streetAddress = new SimpleStringProperty();
    public final SimpleStringProperty surname = new SimpleStringProperty();
    public final SimpleStringProperty telephoneNumber = new SimpleStringProperty();
    public final SimpleStringProperty usageLocation = new SimpleStringProperty();
    public final SimpleStringProperty userPrincipalName = new SimpleStringProperty();
    public final SimpleStringProperty userType = new SimpleStringProperty();

    public static final ObservableMap<String, User> usersByMail = FXCollections.observableHashMap();
    public static final ObservableMap<String, User> usersByGUID = FXCollections.observableHashMap();

    public static User load(String guid) {
        if(guid.equals("me")){
            try {
                JSONObject object = OfficeServer.get("me");
                User u = new User();
                u.loadFromJSON(object);
                return u;
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void loadAllUsers(Callback callback){
        OfficeServer.get("users", content -> {
            JSONArray usersArray = content.getJSONArray("value");
            for (int i = 0; i < usersArray.length(); i++) {
                User u = new User();
                try {
                    u.loadFromJSON(usersArray.getJSONObject(i));
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                usersByMail.put(u.mail.getValue(), u);
                usersByGUID.put(u.objectId.getValue(), u);
            }
            if(callback!=null)
                callback.action(content);
        });
    }
}
