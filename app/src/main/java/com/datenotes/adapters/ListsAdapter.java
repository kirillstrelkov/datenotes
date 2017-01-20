package com.datenotes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.datenotes.R;
import com.datenotes.data.List;

public class ListsAdapter extends ArrayAdapter<List> {
    java.util.List list;

    public ListsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ListsAdapter(Context context, int resource, java.util.List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.note_lists_item, null);
        }

        List note = getItem(position);

        if (note != null) {
            TextView tv_name = (TextView) view.findViewById(R.id.listItemNoteListsName);

            if (tv_name != null) {
                tv_name.setText(note.getName());
            }
        }

        return view;
    }

    public void updateAdapter(java.util.List list) {
        this.list = list;
        this.notifyDataSetChanged();
    }
}
