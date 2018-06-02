package com.escapevelocityfromearth2.shohosan;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class DrugData {

    public DrugData(@Nullable Bitmap drugImage,
                    @Nullable Bitmap ocrImage,
                    String name,
                    MEDICAL_TIME timing,
                    int count,
                    int onceCount,
                    long date) {
        this(drugImage, ocrImage, -1, name, timing, count, onceCount, date);
    }

    public DrugData(@Nullable Bitmap drugImage,
                    @Nullable Bitmap ocrImage,
                    int id,
                    String name,
                    MEDICAL_TIME timing,
                    int count,
                    int onceCount,
                    long date) {
        if (drugImage != null) this.drugImage = drugImage;
        if (ocrImage != null) this.ocrImage = ocrImage;
        this.databaseId = id;
        this.name = name;
        this.timing = timing;
        this.count = count;
        this.onceCount = onceCount;
        this.date = date;
    }

    public enum MEDICAL_TIME {
        WAKE_UP,        // 起床時
        BEFORE_MEAL,    // 食前
        AFTER_MEAL,     // 食後
        SLEEP           // 就寝前
        // and more
    }

    // DatabaseId
    public int databaseId;

    // 薬の画像
    public Bitmap drugImage;
    // OCR画像
    public Bitmap ocrImage;

    // 名前
    public String name;
    // 処方タイミング
    public MEDICAL_TIME timing;
    // 残数
    public int count;
    // １回の摂取数
    public int onceCount;
    // 調剤日
    public long date;


    public String getMedicalTimeText() {
        return getMedicalTimeText(this.timing.ordinal());
    }

    public static String getMedicalTimeText(int time) {
        String timingStr = "";
        if (time == MEDICAL_TIME.WAKE_UP.ordinal()) {
            timingStr = "起床時";
        } else if (time == MEDICAL_TIME.BEFORE_MEAL.ordinal()) {
            timingStr = "食前";
        } else if (time == MEDICAL_TIME.AFTER_MEAL.ordinal()) {
            timingStr = "食後";
        } else if (time == MEDICAL_TIME.SLEEP.ordinal()) {
            timingStr = "就寝前";
        }
        return timingStr;
    }

    public static ArrayList<DrugData> getSampleData() {
        ArrayList<DrugData> list = new ArrayList<>();

        list.add(new DrugData(null, null, "Drug1", MEDICAL_TIME.WAKE_UP, 7, 1, System.currentTimeMillis()));
        list.add(new DrugData(null, null, "Drug2", MEDICAL_TIME.BEFORE_MEAL, 10, 1, System.currentTimeMillis()));
        list.add(new DrugData(null, null, "Drug3", MEDICAL_TIME.AFTER_MEAL, 5, 1, System.currentTimeMillis()));
        list.add(new DrugData(null, null, "Drug4", MEDICAL_TIME.SLEEP, 11, 2, System.currentTimeMillis()));

        return list;
    }
}
