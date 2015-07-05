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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import me.vienne.philippe.officeadmin.api.OfficeServer;
import me.vienne.philippe.officeadmin.api.ApiException;
import me.vienne.philippe.officeadmin.api.Token;

import java.io.IOException;

/**
 * Pane showing a WebView to login to Office 365.
 */
public class O365LoginPane extends StackPane {

    private Pane overlay = getOverlay();
    private BooleanProperty logged = new SimpleBooleanProperty(OfficeServer.token.exists());

    public O365LoginPane(){
        super();
        WebView webview = new WebView();
        webview.getEngine().getLoadWorker().runningProperty().addListener((observable1, oldValue, newValue) -> showOverlay(newValue));
        webview.getEngine().load(Token.getAuthenticationURL("https://graph.microsoft.com"));
        webview.getEngine().locationProperty().addListener(observable -> {
            String loc = webview.getEngine().getLocation();
            if (loc.startsWith("http://localhost:1337/oauth/callback")) {
                handleAuth(loc);
            }
        });
        getChildren().addAll(webview, overlay);
    }

    private void showOverlay(Boolean newValue) {
        overlay.setVisible(newValue);
    }

    private void handleAuth(String url) {
        String code = Token.extractCodeFromURL(url);
        try {
            Token token = Token.getToken(code, "https://graph.microsoft.com");
            OfficeServer.setToken(token);
            loggedProperty().setValue(true);
        } catch (IOException |ApiException e) {
            e.printStackTrace();
        }
    }

    private Pane getOverlay() {
        StackPane p = new StackPane();
        ProgressIndicator p1 = new ProgressIndicator();
        p1.setMaxSize(50,50);
        p.getChildren().add(p1);
        return p;
    }

    public BooleanProperty loggedProperty() {
        return logged;
    }
}
