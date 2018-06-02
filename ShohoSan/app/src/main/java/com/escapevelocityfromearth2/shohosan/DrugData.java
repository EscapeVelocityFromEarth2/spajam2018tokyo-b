package com.escapevelocityfromearth2.shohosan;

public class DrugData {

    public enum MEDICAL_TIME {
        WAKE_UP,        // 起床時
        BEFORE_MEAL,    // 食前
        AFTER_MEAL,     // 食後
        SLEEP           // 就寝前
        // and more
    }

    // 名前
    public String name;
    // 処方タイミング
    public MEDICAL_TIME timing;
    // 残数
    public int count;
    // １回の摂取数
    public int onceCount;

}
