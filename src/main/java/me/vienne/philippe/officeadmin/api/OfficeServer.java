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
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.AbstractHttpMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Represent the link with the Office 365 servers.
 */
public class OfficeServer {

    public static final String SERVER = "https://graph.microsoft.com/beta/";
    private static final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    public static String directory = "bde-insa-lyon.fr";
    public static Token token;

    private static File tokenFile;

    static {
        try {
            tokenFile = getTokenFile();
            if(tokenFile.exists()){
                token = Token.load(tokenFile);
                System.out.println(tokenFile.toString());
            } else {
                token = new Token();
            }
        } catch (ApiException|IOException e) {
            e.printStackTrace();
        } finally {
            if(token == null){
                token = new Token();
            }
        }
    }

    public static JSONObject get(String path) throws ApiException {

        if(path.startsWith("/"))
            path = path.substring(1,path.length()-1);

        // Create new getRequest with below mentioned URL
        HttpGet getRequest = new HttpGet(path.startsWith("http")?path:SERVER + (path.startsWith("me")?"":directory+"/") + path);
        addAccessToken(getRequest);

        // Execute your request and catch response
        HttpResponse response = null;
        try {
            response = httpClient.execute(getRequest);
        } catch (IOException e) {
            throw new ApiException("No connection ?",e);
        }

        // Check for HTTP response code: 200 = success
        if (response.getStatusLine().getStatusCode() != 200) {
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == 401){ // Token has expired
                token.renew();
                return get(path);
            }
            System.out.println(readResponse(response).toString());
            throw new ApiException("API Error - "+statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());
        }

        StringBuilder content = readResponse(response);

        JSONObject object = new JSONObject(content.toString());

        if(object.has("@odata.nextLink")){ // Server does not send all data, so call for all data
            JSONArray array = object.getJSONArray("value");
            String nextLink = object.getString("@odata.nextLink");
            JSONObject jsonObject = get(nextLink);
            if(jsonObject.has("value")){
                JSONArray nextArray = jsonObject.getJSONArray("value");
                for(int i=0; i<nextArray.length(); i++)
                    array.put(nextArray.get(i));
            }
        }

        object.remove("@odata.nextLink");

        return object;

    }

    private static StringBuilder readResponse(HttpResponse response) throws ApiException {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
        } catch (IOException e) {
            throw new ApiException("No answer ?",e);
        }

        StringBuilder content = new StringBuilder();
        String buffer;
        try {
            while ((buffer = br.readLine()) != null) {
                content.append(buffer);
            }
        } catch (IOException e) {
            throw new ApiException("Can not read the answer",e);
        }
        return content;
    }

    public static void get(final String path, final Callback callback){
        get(path, callback, Throwable::printStackTrace);
    }

    public static void get(final String path, Callback callback, ErrorCallback errorCallback){
        new Thread(() -> {
            try {
                JSONObject object = get(path);
                callback.action(object);
            } catch (ApiException e) {
                errorCallback.error(e);
            }

        }).start();
    }

    private static void addAccessToken(AbstractHttpMessage request) {
        if(token.exists()){
            request.addHeader("Authorization","bearer " + token.getAccessToken());
        }
    }

    public static void setToken(Token token) {
        OfficeServer.token = token;
        try {
            OfficeServer.token.save(tokenFile);
        } catch (IOException e) {
            System.err.println("Can not save the token file !");
            e.printStackTrace();
        }
    }

    private static File getTokenFile() throws IOException {
        String workingDirectory;
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            workingDirectory = System.getenv("AppData") + File.separator + OfficeServer.class.getPackage().getName();
        } else {
            if (OS.contains("MAC")) {
                workingDirectory = System.getProperty("user.home") + "/Library/Application Support/"+ OfficeServer.class.getPackage().getName();
            } else {
                workingDirectory = System.getProperty("user.home") + File.separator + "." + OfficeServer.class.getPackage().getName();
            }
        }
        workingDirectory += File.separator;
        workingDirectory += "token.json";
        File tokenFile = new File(workingDirectory);
        FileUtils.forceMkdir(tokenFile.getParentFile());
        return tokenFile;
    }
}
