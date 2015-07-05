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

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Identity token for the Office 365 service.
 */
public class Token {

    private String resource, accessToken, refreshToken, idToken;
    private Integer expiresIn, expiresOn, notBefore;
    private String[] scope;

    public Token(String jsonObject) throws ApiException {
        JSONObject o = new JSONObject(jsonObject);
        loadFromJSON(o);
    }

    private void loadFromJSON(JSONObject o) throws ApiException {
        for (Field field : Token.class.getDeclaredFields()) {
            try {
                if (field.getType().isArray()) {
                    field.set(this, (o.getString(toUnderscoredCase(field.getName()))).split(" "));
                } else {
                    if(field.getType() == String.class)
                        field.set(this, o.getString(toUnderscoredCase(field.getName())));
                    if(field.getType() == Integer.class)
                        field.set(this, o.getInt(toUnderscoredCase(field.getName())));
                }
            }catch (IllegalAccessException e) {
                throw new ApiException("Can not access to field "+field.getName(),e);
            }
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void save(File file) throws IOException {
        FileUtils.forceMkdir(file.getParentFile());
        try {
            FileUtils.write(file,toJSONObject().toString(),"UTF-8");
        } catch (ApiException e) {
            throw new IOException("Can not save the token",e);
        }
    }

    public JSONObject toJSONObject() throws ApiException {
        JSONObject o = new JSONObject();
        for (Field field : Token.class.getDeclaredFields()) {
            try {
                if (field.getType().isArray()) {
                    Object[] array = (Object[]) field.get(this);
                    StringBuilder fullScope = new StringBuilder();
                    for (Object object : array) {
                        fullScope.append(object.toString()).append(' ');
                    }
                    fullScope.deleteCharAt(fullScope.length()-1);
                    o.put(toUnderscoredCase(field.getName()),fullScope.toString());
                } else {
                    o.put(toUnderscoredCase(field.getName()), field.get(this));
                }
            }catch (IllegalAccessException e) {
                throw new ApiException("Can not access to field "+field.getName(),e);
            }
        }
        return o;
    }

    public static Token load(File file) throws IOException, ApiException {
        String token = FileUtils.readFileToString(file, "UTF-8");
        return new Token(token);
    }

    public Token() {
    }

    public String getResource() {
        return resource;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public int getExpiresOn() {
        return expiresOn;
    }

    public int getNotBefore() {
        return notBefore;
    }

    public String[] getScopes() {
        return scope;
    }

    public boolean exists() {
        long time = System.currentTimeMillis() / 1000;
        return getAccessToken()!=null && time < expiresOn && time > notBefore;
    }

    public boolean renew() throws ApiException {
        HttpClient httpClient = HttpClientBuilder.create().disableAuthCaching().disableRedirectHandling().disableCookieManagement().build();
        HttpPost request = new HttpPost("https://login.microsoftonline.com/"+OfficeServer.directory+"/oauth2/token");
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type","refresh_token"));
        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:1337/oauth/callback"));
        params.add(new BasicNameValuePair("client_id",AppToken.getInstance().getAppKey()));
        params.add(new BasicNameValuePair("client_secret",AppToken.getInstance().getAppSecret()));
        params.add(new BasicNameValuePair("resource", resource));
        params.add(new BasicNameValuePair("refresh_token", refreshToken));
        try {
            request.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(request);

            if(response.getStatusLine().getStatusCode() == 200) {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                loadFromJSON(new JSONObject(result.toString()));
            } else {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuilder result = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                throw new ApiException(result.toString());
            }
        } catch (ApiException | IOException e) {
            throw new ApiException("Can not get new token",e);
        }
        return true;
    }

    public static String getAuthenticationURL(String resource){
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("response_type","code"));
        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:1337/oauth/callback"));
        params.add(new BasicNameValuePair("client_id",AppToken.getInstance().getAppKey()));
        params.add(new BasicNameValuePair("resource", resource));
        return "https://login.microsoftonline.com/"+OfficeServer.directory+"/oauth2/authorize?"+URLEncodedUtils.format(params,"UTF-8");
    }

    public static String extractCodeFromURL(String url){
        List<NameValuePair> values = URLEncodedUtils.parse(url.split("\\?",2)[1], Charset.defaultCharset());
        for (NameValuePair value : values) {
            if(value.getName().equals("code"))
                return value.getValue();
        }
        return "";
    }

    public static Token getToken(String code, String resource) throws IOException, ApiException {
        HttpClient httpClient = HttpClientBuilder.create().disableAuthCaching().disableRedirectHandling().disableCookieManagement().build();
        HttpPost request = new HttpPost("https://login.microsoftonline.com/"+OfficeServer.directory+"/oauth2/token");
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type","authorization_code"));
        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:1337/oauth/callback"));
        params.add(new BasicNameValuePair("client_id",AppToken.getInstance().getAppKey()));
        params.add(new BasicNameValuePair("client_secret",AppToken.getInstance().getAppSecret()));
        params.add(new BasicNameValuePair("resource", resource));
        params.add(new BasicNameValuePair("code", code));
        request.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse response = httpClient.execute(request);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        if(response.getStatusLine().getStatusCode() == 200) {

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return new Token(result.toString());
        } else {
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            throw new ApiException(result.toString());
        }
    }

    private static String toUnderscoredCase(String string){
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        return (string.replaceAll(regex, replacement).toLowerCase());
    }

    private static String toCamelCase(String s){
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    private static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
