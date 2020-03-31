package com.davydh.covid_19.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.davydh.covid_19.R;

import java.util.ArrayList;
import java.util.Map;

public class HashMapAdapter extends BaseAdapter {
    private final ArrayList mData;

    public HashMapAdapter(Map<String, String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        ViewHolder viewHolder;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.contentText = ((TextView) result.findViewById(R.id.text_list_item));
            viewHolder.infoText = ((TextView) result.findViewById(R.id.number_list_item));
            result.setTag(viewHolder);
        } else {
            result = convertView;
            viewHolder = (ViewHolder) result.getTag();
        }

        Map.Entry<String, String> item = getItem(position);

        String key = item.getKey();

        viewHolder.contentText.setText(key);

        String value = item.getValue();

        SpannableString str = new SpannableString(value);

        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.parseColor("#008000"));

        if (key.contains("guariti")) {
            if (value.contains("+")) {
                int charPosition = value.indexOf("+");
                str.setSpan(fcsGreen,charPosition,value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                int charPosition = value.indexOf("-");
                str.setSpan(fcsRed,charPosition,value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            if (value.contains("+")) {
                int charPosition = value.indexOf("+");
                str.setSpan(fcsRed,charPosition,value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                int charPosition = value.indexOf("-");
                str.setSpan(fcsGreen,charPosition,value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        viewHolder.infoText.setText(str);

        return result;
    }
}
