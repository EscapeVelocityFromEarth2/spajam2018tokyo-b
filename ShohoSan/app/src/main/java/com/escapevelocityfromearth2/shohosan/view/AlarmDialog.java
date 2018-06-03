package com.escapevelocityfromearth2.shohosan.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.escapevelocityfromearth2.shohosan.MyAlarmManager;
import com.escapevelocityfromearth2.shohosan.R;

public class AlarmDialog  extends DialogFragment {

    String tag = "";
    private static final int COUNTER_MAX = 5;
    double total;
    Dialog dialog;

    Activity parentActivity;

    int type = 0;
    LinearLayout alarmLayout;
    LinearLayout disconnectLayout;

    Button  closeButton;

    Button okButton;
    Button snooseButton;
    Button cancelButton;

    DialogInterface.OnDismissListener listener;


    public void setActivity(Activity activity) {
        this.parentActivity = activity;
    }

    /**
     * 距離設定を行う場所を示すタグをセットする
     * @param tag 距離設定を行う場所を示すタグ
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {

        dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCanceledOnTouchOutside(
                false);
        //this.setCancelable(false);

        dialog.setContentView(R.layout.dialog_layout);
        alarmLayout = dialog.findViewById(R.id.alarm_layout);
        alarmLayout.setVisibility(View.VISIBLE);

        disconnectLayout = dialog.findViewById(R.id.disconnect_layout);
        disconnectLayout.setVisibility(View.GONE);

        closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDismiss(null);
                dialog.dismiss();
            }
        });

        okButton = dialog.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                parentActivity.finish();
                listener.onDismiss(null);
                dialog.dismiss();
            }
        });

        snooseButton = dialog.findViewById(R.id.snoose_button);
        snooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAlarmManager.setAlarm(parentActivity, 10 * 1000);
                listener.onDismiss(null);
//                parentActivity.finish();
                dialog.dismiss();
            }
        });

        cancelButton = dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDismiss(null);
                dialog.dismiss();
            }
        });

        if (listener != null) dialog.setOnDismissListener(listener);


        if (type == 1) {
            alarmLayout.setVisibility(View.GONE);
            disconnectLayout.setVisibility(View.VISIBLE);
        } else {
            // デフォルト
            alarmLayout.setVisibility(View.VISIBLE);
            disconnectLayout.setVisibility(View.GONE);
        }

        return dialog;

    }

    public void setDismissListener(DialogInterface.OnDismissListener listener) {
       this.listener = listener;
    }

    public void setType(int type) {

        this.type = type;

    }

}
