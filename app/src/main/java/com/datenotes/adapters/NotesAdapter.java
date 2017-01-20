package com.datenotes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.datenotes.R;
import com.datenotes.data.Note;

import java.util.List;

public class NotesAdapter extends ArrayAdapter<Note> {
    List<Note> notes;

    public NotesAdapter(Context context, int resource) {
        super(context, resource);
    }

    public NotesAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.note_list_item, null);
        }

        Note note = getItem(position);

        if (note != null) {
            TextView tv_note = (TextView) view.findViewById(R.id.listItemNote);
            TextView tv_date = (TextView) view.findViewById(R.id.listItemDate);

            if (tv_note != null) {
                tv_note.setText(note.getNote());
            }

            if (tv_date != null) {
                tv_date.setText(note.getFomattedDate());
            }
        }

        return view;
    }

    public void updateAdapter(List<Note> notes) {
        this.notes = notes;
        this.notifyDataSetChanged();
    }
}
