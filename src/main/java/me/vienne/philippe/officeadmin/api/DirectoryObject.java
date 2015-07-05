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
import javafx.collections.ObservableList;
import me.vienne.philippe.officeadmin.property.DirectoryObjectListProperty;
import me.vienne.philippe.officeadmin.property.DirectoryObjectProperty;
import me.vienne.philippe.officeadmin.property.SimpleDateProperty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;

/**
 * Represents an Azure Active Directory object.
 * The DirectoryObject type is the base type for most of the other directory entity types.
 */
public class DirectoryObject {

    /**
     * A Guid that is the unique identifier for the object; for example, 12345678-9abc-def0-1234-56789abcde.
     */
    public final SimpleStringProperty objectId = new SimpleStringProperty();

    /**
     * A string that identifies the object type. For example, for groups the value is always “Group”.
     */
    public final SimpleStringProperty objectType = new SimpleStringProperty();

    /**
     * The time at which the directory object was deleted.
     * It only applies to those directory objects which can be restored.
     * Currently it is only supported for deleted Application objects;
     * all other entities return null for this property.
     */
    public final SimpleDateProperty deletionTimeStamp = new SimpleDateProperty();

    public void loadFromJSON(JSONObject object) throws ApiException {
        assert getClass().getSimpleName().equals(object.getString("objectType")) : "Wrong object type !";
        for (Field field : getClass().getFields()) {
            String property = field.getName();
            if(!object.has(property) || object.isNull(property)) continue;
            try {
                Object value = field.get(this);
                    if (value instanceof SimpleStringProperty) {
                        if(object.has(property)){
                            ((SimpleStringProperty) value).setValue(object.getString(property));
                        }
                    } else if (value instanceof SimpleBooleanProperty) {
                        if(object.has(property)){
                            ((SimpleBooleanProperty) value).setValue(object.getBoolean(property));
                        }
                    } else if (value instanceof ObjectProperty) {
                        if(value instanceof SimpleDateProperty){
                            try {
                                ((SimpleDateProperty) value).setString(object.getString(property));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if(value instanceof DirectoryObjectProperty){
                            JSONObject childObject = object.getJSONObject(property);
                            try {
                                DirectoryObject newInstance = (DirectoryObject) value.getClass().newInstance();
                                newInstance.loadFromJSON(childObject);
                                ((DirectoryObjectProperty)value).setValue(newInstance);
                            } catch (InstantiationException|ApiException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try{
                                //noinspection unchecked
                                ((ObjectProperty) value).setValue(object.get(property));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } else if (value instanceof DirectoryObject) {
                        ((DirectoryObject) value).loadFromJSON(object.getJSONObject(property));
                    } else if (value instanceof DirectoryObjectListProperty){
                        DirectoryObjectListProperty list = (DirectoryObjectListProperty) value;
                        JSONArray jsonArray = object.getJSONArray(property);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                DirectoryObject directoryObject = (DirectoryObject) list.getPropertyClass().newInstance();
                                directoryObject.loadFromJSON(jsonArray.getJSONObject(i));
                                ((DirectoryObjectListProperty) value).add(directoryObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (value instanceof ObservableList){
                        if(object.get(property) instanceof JSONArray){
                            JSONArray jsonArray = object.getJSONArray(property);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Object listObject = jsonArray.get(i);
                                if (listObject == null) {
                                    //noinspection unchecked
                                    ((ObservableList) value).add(null);
                                } else {
                                    //noinspection unchecked
                                    ((ObservableList) value).add(listObject);
                                }
                            }
                        }
                    } else {
                        field.set(this,object.get(property));
                    }
            }catch (IllegalAccessException e) {
                throw new ApiException("Can not access to field "+ property,e);
            }
        }
    }

    public JSONObject toJSONObject() throws ApiException {
        JSONObject o = new JSONObject();
        for (Field field : Token.class.getDeclaredFields()) {
            try {
                Object value = field.get(this);
                do {
                    if (value == null) {
                        o.put(field.getName(), JSONObject.NULL);
                    } else if (value instanceof SimpleStringProperty) {
                        o.put(field.getName(), ((SimpleStringProperty) value).getValue());
                    } else if (value instanceof SimpleBooleanProperty) {
                        o.put(field.getName(), ((SimpleBooleanProperty) value).get());
                    } else if (value instanceof ObjectProperty) {
                        ObjectProperty property = (ObjectProperty) value;
                        value = property.get();
                        continue;
                    } else if (value instanceof DirectoryObject) {
                        o.put(field.getName(),((DirectoryObject) value).toJSONObject());
                    } else if (value instanceof ListProperty){
                        JSONArray array = new JSONArray();
                        for(Object listObject:(ListProperty)value){
                            if (listObject == null) {
                                array.put(JSONObject.NULL);
                            } else if (listObject instanceof DirectoryObject){
                                array.put(((DirectoryObject) listObject).toJSONObject());
                            } else {
                                array.put(listObject.toString());
                            }
                        }
                    } else {
                        o.put(field.getName(),value.toString());
                    }
                    value = null; // Managed
                } while (value != null);
            }catch (IllegalAccessException e) {
                throw new ApiException("Can not access to field "+field.getName(),e);
            }
        }
        return o;
    }
}
