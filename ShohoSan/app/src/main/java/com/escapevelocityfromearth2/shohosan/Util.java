package com.escapevelocityfromearth2.shohosan;

import android.content.Context;
import android.util.Log;

import com.escapevelocityfromearth2.shohosan.database.DbManager;
import com.escapevelocityfromearth2.shohosan.database.DrugDbHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class Util {

    public static int NOTIFICATION_ID = 01;

    // 並び替えたうち、最初のもの（DB読み込みあり）
    public static DrugData getNextAlarmData(Context context) {
        return sortAlarmDataList(context).get(0);
    }

    // 並び替えたうち、最初のもの
    public static DrugData getNextAlarmData(ArrayList<DrugData> list) {
        return sortAlarmDataList(list).get(0);
    }

    public static ArrayList<DrugData> sortAlarmDataList(Context context) {
        return sortAlarmDataList(DbManager.loadData(context, DrugDbHelper.DB_COLUMN_TIMING));
    }

    public static ArrayList<DrugData> sortAlarmDataList(ArrayList<DrugData> dataList) {
        ArrayList<DrugData> sortedList = new ArrayList<>();

        long current = System.currentTimeMillis();

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(current);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        // todo 現在のデータをお知らせする順番に並び替える
        // (仮） 起床時：7時　朝食：8時　昼食：13時 夜食：19時 就寝前23時

        int count = 0;          // 登録した日数
        int completeCount = 0;  // 錠剤全て登録し終わった薬
        int hour = c.get(Calendar.HOUR_OF_DAY);    // セットした時間

        // DrugDbHelper.DB_COLUMN_TIMINGでソートされている前提
        while (completeCount < dataList.size()) {

            for (DrugData data : dataList) {

                DrugData addData = data.copy();
                Calendar alarmDate = Calendar.getInstance();
                alarmDate.setTimeInMillis(current);
                Log.d("test", "check timing:" + data.timing.ordinal() + " data.count:" + data.count + " count:" + count + " completeCount:" + completeCount);

                if (data.timing.ordinal() == 0) {
                    // 7時
                    if (count <= data.count) {
                        if ((count == 0 && hour < 7)) {
                            alarmDate.set(Calendar.HOUR_OF_DAY, 7);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if (count + 1 == data.count) completeCount++;
                        } else if (data.count > count && hour < 7) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 7);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if (count + 1 == data.count) completeCount++;
                        } else if (data.count >= count && hour >= 7) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 7);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if (count == data.count) completeCount++;
                        }
                    }

                } else if (data.timing.ordinal() == 1) {
                    // 食前は保留
                    completeCount++;
                } else if (data.timing.ordinal() == 2) {

                    int daily = hour < 8 ? 3 : (hour < 13 ? 2 : (hour < 19 ? 1 : 0));
                    Log.d("test", "check daily:" + daily + " result:" + (count - 1) * 3 + daily + 1);

                    // 8時
                    if (count == 0 || (count - 1) * 3 + daily < data.count) {
                        if ((count == 0 && daily >= 3)) {
                            alarmDate.set(Calendar.HOUR_OF_DAY, 8);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily + 1 == data.count) completeCount++;
                        } else if (data.count > (count - 1) * 3 + daily && hour < 8) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 8);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily + 1 == data.count) completeCount++;
                        } else if (data.count >= (count - 1) * 3 + daily && hour >= 8) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 8);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily == data.count) completeCount++;
                        }
                    }

                    // 13時
                    if (count == 0 || (count - 1) * 3 + daily + 1 < data.count) {

                        if ((count == 0 && daily >= 2)) {
                            alarmDate.set(Calendar.HOUR_OF_DAY, 13);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily + 1 == data.count) completeCount++;
                        } else if (data.count > (count - 1) * 3 + daily && hour < 13) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 13);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily + 2 == data.count) completeCount++;
                        } else if (data.count >= (count - 1) * 3 + daily && hour >= 13) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 13);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily + 1 == data.count) completeCount++;
                        }
                    }

                    // 19時
                    if (count == 0 || (count - 1) * 3 + daily + 2 < data.count) {

                        if ((count == 0 && daily >= 1)) {
                            alarmDate.set(Calendar.HOUR_OF_DAY, 19);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily + 1 == data.count) completeCount++;
                        } else if (data.count > (count - 1) * 3 + daily && hour < 19) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 19);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily + 3 == data.count) completeCount++;
                        } else if (data.count >= (count - 1) * 3 + daily && hour >= 19) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 19);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if ((count - 1) * 3 + daily + 2 == data.count) completeCount++;
                        }

                    }

                } else if (data.timing.ordinal() == 3) {
                    // 23時
                    if (count < data.count) {
                        if ((count == 0 && hour < 23)) {
                            alarmDate.set(Calendar.HOUR_OF_DAY, 23);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if (count + 1 == data.count) completeCount++;
                        } else if (data.count > count && hour < 23) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 23);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if (count + 1 == data.count) completeCount++;
                        } else if (data.count >= count && hour >= 23) {
                            alarmDate.add(Calendar.DATE, count);
                            alarmDate.set(Calendar.HOUR_OF_DAY, 23);
                            addData.alarmDate = alarmDate.getTimeInMillis();
                            sortedList.add(addData);
                            if (count == data.count) completeCount++;
                        }
                    }
                }
            }
            count++;
        }

        return sortedList;
    }

    public static String timeStringFormat(long data){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data);
        return (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日"
                + calendar.get(Calendar.HOUR_OF_DAY) + "時"
                + calendar.get(Calendar.MINUTE) + "分"
                + calendar.get(Calendar.SECOND) + "秒";
    }
}
