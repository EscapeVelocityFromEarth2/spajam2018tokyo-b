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
                    int onceCount) {
        if (drugImage != null) this.drugImage = drugImage;
        if (ocrImage != null) this.ocrImage = ocrImage;
        this.name = name;
        this.timing = timing;
        this.count = count;
        this.onceCount = onceCount;
    }

    public enum MEDICAL_TIME {
        WAKE_UP,        // 起床時
        BEFORE_MEAL,    // 食前
        AFTER_MEAL,     // 食後
        SLEEP           // 就寝前
        // and more
    }

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


    public String getMedicalTimeText() {
        String timingStr = "";
        switch (this.timing) {
            case WAKE_UP:
                timingStr = "起床時";
                break;
            case BEFORE_MEAL:
                timingStr = "食前";
                break;
            case AFTER_MEAL:
                timingStr = "食後";
                break;
            case SLEEP:
                timingStr = "就寝前";
                break;
            default:
                break;
        }
        return timingStr;
    }

    public static ArrayList<DrugData> getSampleData() {
        ArrayList<DrugData> list = new ArrayList<>();

        list.add(new DrugData(null, null, "Drug1", MEDICAL_TIME.WAKE_UP, 7, 1));
        list.add(new DrugData(null, null, "Drug2", MEDICAL_TIME.BEFORE_MEAL, 10, 1));
        list.add(new DrugData(null, null, "Drug3", MEDICAL_TIME.AFTER_MEAL, 5, 1));
        list.add(new DrugData(null, null, "Drug4", MEDICAL_TIME.SLEEP, 11, 2));

        return list;
    }
}
