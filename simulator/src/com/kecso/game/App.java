/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kecso.game;

import com.jme3.system.AppSettings;

/**
 *
 * @author david
 */
public class App {

    public static void main(String[] args) {

        Simulator app = new Simulator();

        app.setShowSettings(true);
        app.setDisplayStatView(true);

        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setFullscreen(false);

        app.setSettings(settings);
        app.start();
    }
}
