package com.klinker.android.twitter_l.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.data.sq_lite.HashtagSQLiteHelper;
import com.klinker.android.twitter_l.utils.AutoCompleteHelper;

/**
 * Created by luke on 6/19/14.
 */
public class AutoCompleteHashtagAdapter extends CursorAdapter {

    private Cursor cursor;
    private AutoCompleteHelper helper;

    public AutoCompleteHashtagAdapter(Context context, Cursor cursor, EditText text) {
        super(context, cursor);

        this.cursor = cursor;
        this.context = context;
        this.text = text;

        inflater = LayoutInflater.from(context);
        helper = new AutoCompleteHelper();
    }

    protected Context context;

    private EditText text;

    private LayoutInflater inflater;

    public static class ViewHolder {
        public TextView text;
    }

    @Override
    public int getCount() {
        try {
            return cursor.getCount();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            if (!cursor.moveToPosition(cursor.getCount() - 1 - position)) {
                throw new IllegalStateException("couldn't move cursor to position " + position);
            }
        } catch (Exception e) {
            ((Activity)context).recreate();
            return null;
        }

        View v;
        if (convertView == null) {
            v = newView(context, cursor, parent);
        } else {
            v = convertView;
        }

        bindView(v, context, cursor);

        return v;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v;
        final ViewHolder holder;

        v = inflater.inflate(R.layout.text, viewGroup, false);

        holder = new ViewHolder();

        holder.text = (TextView) v.findViewById(R.id.text);

        // sets up the font sizes
        holder.text.setTextSize(24);

        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        final String tag = cursor.getString(cursor.getColumnIndex(HashtagSQLiteHelper.COLUMN_TAG));

        holder.text.setText(tag);

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.completeTweet(text, tag, '#');
            }
        });
    }
}