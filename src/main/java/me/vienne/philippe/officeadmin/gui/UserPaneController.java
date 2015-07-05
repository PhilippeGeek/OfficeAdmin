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

/**
 * Controller for the user pane.
 */
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import me.vienne.philippe.officeadmin.api.User;

public class UserPaneController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="lastnameField"
    private TextField lastnameField; // Value injected by FXMLLoader

    @FXML // fx:id="safemailField"
    private TextField safemailField; // Value injected by FXMLLoader

    @FXML // fx:id="emailField"
    private TextField emailField; // Value injected by FXMLLoader

    @FXML // fx:id="teamList"
    private ListView<?> teamList; // Value injected by FXMLLoader

    @FXML // fx:id="firstnameField"
    private TextField firstnameField; // Value injected by FXMLLoader

    @FXML
    void saveUser(ActionEvent event) {

    }

    @FXML
    void deleteUser(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lastnameField != null : "fx:id=\"lastnameField\" was not injected: check your FXML file 'about_me.fxml'.";
        assert safemailField != null : "fx:id=\"safemailField\" was not injected: check your FXML file 'about_me.fxml'.";
        assert emailField != null : "fx:id=\"emailField\" was not injected: check your FXML file 'about_me.fxml'.";
        assert teamList != null : "fx:id=\"teamList\" was not injected: check your FXML file 'about_me.fxml'.";
        assert firstnameField != null : "fx:id=\"firstnameField\" was not injected: check your FXML file 'about_me.fxml'.";

    }

    public void loadUser(User user){
        lastnameField.textProperty().bindBidirectional(user.givenName);
        firstnameField.textProperty().bindBidirectional(user.surname);
        emailField.textProperty().bindBidirectional(user.mail);
    }
}

