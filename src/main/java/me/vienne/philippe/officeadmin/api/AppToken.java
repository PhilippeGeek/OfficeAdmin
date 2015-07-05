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

/**
 * Tokens for an Office 365 desktop application.
 * You must have one web app for permissions and a desktop app for keys.
 * This class is a singleton (one instance per JVM).
 */
public class AppToken {

    protected static final AppToken token = new AppToken();
    private final String appDomain;
    private final String appKey;
    private final String appSecret;

    protected AppToken(){
        this.appKey = "69cdfb4e-24c3-4f49-a314-a1f0f62e2912";
        this.appSecret = "I/qq0V/iGmXWvgrpF2bBswNco/iHQohvpOv+EdZN/fs=";
        this.appDomain = "http://orgaif.bde-insa-lyon.fr";
    }

    public String getAppDomain() {
        return appDomain;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public static AppToken getInstance(){
        return token;
    }

}
