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

package me.vienne.philippe.officeadmin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import me.vienne.philippe.officeadmin.api.OfficeServer;
import me.vienne.philippe.officeadmin.gui.UserPaneController;
import me.vienne.philippe.officeadmin.api.User;
import me.vienne.philippe.officeadmin.gui.O365LoginPane;

import java.net.URL;

public class Main extends Application {

    private final O365LoginPane loginPane = new O365LoginPane();
    private final Scene loginScene = new Scene(loginPane, 400, 600);
    private Scene mainScene = new Scene(new Label("Hello !"), 200, 200);

    @Override
    public void start(Stage primaryStage) {
        loginPane.loggedProperty().addListener((observable, hasBeenLogged, isCurrentlyLogged) -> showHome(primaryStage, isCurrentlyLogged));
        showHome(primaryStage, OfficeServer.token.exists());
        primaryStage.show();
    }

    private void showHome(Stage stage, Boolean isLogged) {
        if(!isLogged)
            stage.setScene(loginScene);
        else{
            try {
                URL resource = getClass().getClassLoader().getResource("me/vienne/philippe/officeadmin/gui/user.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(resource);
                fxmlLoader.load();
                ScrollPane pane = fxmlLoader.getRoot();
                UserPaneController controller = fxmlLoader.getController();
                controller.loadUser(User.load("me"));
                mainScene = new Scene(pane, 500,600);
                stage.setScene(mainScene);
            } catch (Exception e) {
                e.printStackTrace();
                loginPane.loggedProperty().set(false);
            }
        }
    }

    public static void main(String[] args) {
        User.loadAllUsers(null);
        launch(args);
    }
}
