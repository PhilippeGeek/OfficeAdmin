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

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Describe a Date property
 */
public class SimpleDateProperty extends ObjectPropertyBase<Date> {

    /**
     * @see <a href="http://en.wikipedia.org/wiki/ISO_8601#Combined_date_and_time_representations">Combined Date and Time Representations</a>
     */
    public static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_8601_24H_FULL_FORMAT);

    public void setString(String date) throws ParseException {
        set(dateFormat.parse(date));
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
