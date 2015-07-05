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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import me.vienne.philippe.officeadmin.api.ApiException;
import me.vienne.philippe.officeadmin.api.OfficeServer;
import me.vienne.philippe.officeadmin.api.Token;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public WebView webview;


    public void initialize(URL location, ResourceBundle resources) {
        webview.getEngine().load(Token.getAuthenticationURL("https://graph.microsoft.com"));
        webview.getEngine().locationProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                String loc = webview.getEngine().getLocation();
                if(loc.startsWith("http://localhost:1337/oauth/callback")){
                    handleAuth(loc);
                }
            }
        });
    }

    private void handleAuth(String url) {
        String code = Token.extractCodeFromURL(url);
        try {
            Token token = Token.getToken(code, "https://graph.microsoft.com");
            OfficeServer.token = token;
            System.out.println(token.toJSONObject().toString(4));
        } catch (IOException|ApiException e) {
            e.printStackTrace();
        }
    }
}
