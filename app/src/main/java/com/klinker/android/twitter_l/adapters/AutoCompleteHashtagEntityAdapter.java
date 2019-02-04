package com.klinker.android.twitter_l.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.data.roomdb.entities.Hashtag;
import com.klinker.android.twitter_l.utils.AutoCompleteHelper;

import java.util.List;

import androidx.annotation.NonNull;

public class AutoCompleteHashtagEntityAdapter extends ArrayAdapter<Hashtag> {

    private ListPopupWindow listPopupWindow;

    protected Context context;
    private EditText editText;


    public AutoCompleteHashtagEntityAdapter(ListPopupWindow listPopup, Context context, EditText editText, List<Hashtag> hashtags) {
        super(context, R.layout.text, hashtags);

        this.listPopupWindow = listPopup;
        this.editText = editText;
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        Hashtag hashtag = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder(listPopupWindow, editText);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.text, parent, false);
            viewHolder.text = convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);

            this.listPopupWindow = null;
            this.editText = null;

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (hashtag == null) {
            viewHolder.text.setText("");

        } else {
            viewHolder.text.setText(hashtag.getTag());
        }

        return convertView;

    }


    public static class ViewHolder {
        public TextView text;

        public ViewHolder(ListPopupWindow listPopupWindow, EditText editText) {

            text.setTextSize(24);
            AutoCompleteHelper helper = new AutoCompleteHelper();

            text.setOnClickListener(v -> {
                helper.completeTweet(editText, text.getText().toString(), '#');
                editText.setText(editText.getText().toString().replace("# #", "#"));
                if (listPopupWindow != null && listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                }

            });


        }


    }

}
