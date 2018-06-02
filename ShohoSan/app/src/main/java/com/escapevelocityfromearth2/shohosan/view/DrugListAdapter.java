package com.escapevelocityfromearth2.shohosan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.escapevelocityfromearth2.shohosan.DrugData;
import com.escapevelocityfromearth2.shohosan.R;

import java.util.List;

public class DrugListAdapter extends ArrayAdapter<DrugData> {

    private Context context;
    private List<DrugData> items;
    private LayoutInflater inflater;

    class ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView textView;
    }

    public DrugListAdapter(Context context, int textViewResourceId, List<DrugData> items) {
        super(context, textViewResourceId, items);

        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        DrugData item = getItem(position);

        if (convertView == null) {
            convertView =  inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.titleView = (TextView) convertView.findViewById(R.id.title_label);
            holder.textView = (TextView) convertView.findViewById(R.id.text_label);
            convertView.setTag(holder);
        }else{
            holder =  (ViewHolder)convertView.getTag();
        }

        if (item.drugImage != null) holder.imageView.setImageBitmap(item.drugImage);
        holder.titleView.setText(item.name);
        holder.textView.setText(item.getMedicalTimeText() + " あと" + item.count + "錠");

        return convertView;
    }



}
