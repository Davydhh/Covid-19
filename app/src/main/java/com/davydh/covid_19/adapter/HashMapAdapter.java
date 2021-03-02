package com.davydh.covid_19.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.res.ResourcesCompat;

import com.davydh.covid_19.R;

import java.util.ArrayList;
import java.util.Map;

public class HashMapAdapter extends BaseAdapter {
    private final ArrayList mData;
    private final Context mContext;

    public HashMapAdapter(Map<String, String> map, Context context) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        this.mContext = context;
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
            viewHolder.contentText = result.findViewById(R.id.text_list_item);
            viewHolder.infoText = result.findViewById(R.id.number_list_item);
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

        ForegroundColorSpan fcsRed =
                new ForegroundColorSpan(ResourcesCompat.getColor(mContext.getResources(),
                        R.color.redText, null));
        ForegroundColorSpan fcsGreen = new ForegroundColorSpan(ResourcesCompat.getColor(mContext.getResources(),
                R.color.greenText, null));

        if (key.contains("guariti")) {
            if (value.contains("+")) {
                int charPosition = value.indexOf('+');
                str.setSpan(fcsGreen,charPosition,value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (value.contains("-")) {
                int charPosition = value.lastIndexOf('-');
                str.setSpan(fcsRed,charPosition,value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            if (value.contains("+")) {
                int charPosition = value.indexOf('+');
                str.setSpan(fcsRed,charPosition,value.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (value.contains("-")) {
                int charPosition = value.lastIndexOf('-');
                str.setSpan(fcsGreen, charPosition,value.length()-1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        viewHolder.infoText.setText(str);

        return result;
    }
}
