package com.datenotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.datenotes.adapters.ListsAdapter;
import com.datenotes.data.List;
import com.datenotes.data.ListDao;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 21;

    private java.util.List<List> lists;
    private ListView listView;
    private TextView txtNolists;
    private ListDao listDao;
    private ListsAdapter listsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MainActivity thisActivity = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(this.getResources().getColorStateList(R.color.colorPrimary));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                LayoutInflater inflater = thisActivity.getLayoutInflater();

                final View inflatedView = inflater.inflate(R.layout.dialog_create_list, null);
                builder.setView(inflatedView);
                builder.setPositiveButton(R.string.dialog_create_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) inflatedView.findViewById(R.id.idNewListsName);
                        String name = editText.getText().toString();
                        addList(new List(name));
                    }
                });
                builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        listDao = ((App) getApplication()).getDaoSession().getListDao();
        lists = listDao.queryBuilder().orderDesc(ListDao.Properties.Id).list();

        txtNolists = (TextView) findViewById(R.id.txtNoLists);
        if (lists.size() > 0) {
            txtNolists.setVisibility(View.GONE);
        }
        listView = (ListView) findViewById(R.id.listNotesLists);
        listsAdapter = new ListsAdapter(this, R.layout.note_lists_item, lists);
        listView.setAdapter(listsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), NotesActivity.class);
                List list = ((ListsAdapter) adapterView.getAdapter()).getItem(i);
                intent.putExtra(List.KEY, list);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void addList(List list) {
        if (lists.size() == 0) {
            txtNolists.setVisibility(View.GONE);
        }
        listDao.insert(list);
        lists.add(0, list);
        listsAdapter.updateAdapter(lists);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            List list = data.getParcelableExtra(List.KEY);
            for (List l : lists) {
                if (l.getId() == list.getId()) {
                    l.setName(list.getName());
                    l.setNotes(list.getNotes());
                    break;
                }
            }
            listsAdapter.updateAdapter(lists);
        }
    }

}
