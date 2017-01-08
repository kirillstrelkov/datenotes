package com.datenotes;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class AddNote extends AppCompatActivity {
    public static final int ADD_NOTE_ID = 1;
    public static final int UPDATE_NOTE_ID = 2;
    private Calendar calendar;
    private EditText textDate;
    private EditText textNote;
    private Button btnAdd;
    private long dateNoteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        textDate = (EditText) findViewById(R.id.textDate);
        textNote = (EditText) findViewById(R.id.textNote);
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpd = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        setTime(hour, minutes);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                tpd.show();

                DatePickerDialog dpd = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        setDate(year, month, day);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        setDateToField();

        Intent intent = getIntent();
        String note = intent.getStringExtra(DateNote.KEY_NOTE);
        String date = intent.getStringExtra(DateNote.KEY_DATE);
        dateNoteID = intent.getLongExtra(DateNote.KEY_ID, DateNote.DEFAULT_ID);
        if (dateNoteID != DateNote.DEFAULT_ID) {
            textDate.setText(date);
            textNote.setText(note);
            btnAdd.setText(R.string.update_note);
        }
    }

    private void setTime(int hours, int minutes) {
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        setDateToField();
    }

    private void setDateToField() {
        textDate.setText(DateNote.DATE_TIME_FORMAT.format(calendar.getTime()));
    }

    private void setDate(int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        setDateToField();
    }

    protected void goBack(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    protected void addNewNote(View view) {
        Intent intent = new Intent();
        intent.putExtra(DateNote.KEY_NOTE, textNote.getText().toString());
        intent.putExtra(DateNote.KEY_DATE, textDate.getText().toString());
        intent.putExtra(DateNote.KEY_ID, dateNoteID);
        setResult(RESULT_OK, intent);
        finish();
    }
}
