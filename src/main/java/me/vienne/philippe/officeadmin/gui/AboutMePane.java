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

package me.vienne.philippe.officeadmin.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Pane used to display information about the current user.
 */
public class AboutMePane extends Pane {

    public AboutMePane(){
        try {
            getChildren().add(FXMLLoader.load(AboutMePane.class.getResource("about_me.fxml")));
        } catch (IOException e) {
            getChildren().add(new Label("Can not load UI: "+ e.getMessage()));
            e.printStackTrace();
        }
    }

    public static class AboutMeController{



    }

}
