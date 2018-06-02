package com.escapevelocityfromearth2.shohosan;

import io.flic.lib.FlicManager;

/**
 * Created by tsukamotohiroshinozomi on 2018/06/03.
 */

public class Config {
    static void setFlicCredentials() {
        FlicManager.setAppCredentials("[flic-background-example]", "[app-secret]", "Flic Background Example");
    }
}
