package com.example.mynote.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.example.mynote.R;
import com.example.mynote.adapter.ListNoteAdapter;
import com.example.mynote.model.NoteItem;
import com.example.mynote.utils.DataUtils;
import com.example.mynote.view.DialogChoose;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final int CREATENOTE = 12345;
    private ArrayList<NoteItem> noteItemArrayList = new ArrayList<>();
    private RecyclerView recyclerViewNote;
    private FloatingActionButton floatingActionButtonCreateNote;
    private ListNoteAdapter listNoteAdapter;
    private DataUtils dataUtils = new DataUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        recyclerViewNote = findViewById(R.id.rv_list_note);
        floatingActionButtonCreateNote = findViewById(R.id.fab_create_note);
    }

    private void initEvent() {
        floatingActionButtonCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> options = new ArrayList<>();
                options.add("Normal note");
                options.add("Draw note");
                DialogChoose.getInstance().showDialog(options,"Select Mode",MainActivity.this, new DialogChoose.DialogChooseListener() {
                    @Override
                    public void onItemSelected(String item) {
                        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                        intent.putExtra("create", item);
                        startActivityForResult(intent, CREATENOTE);
                    }

                    @Override
                    public void onDismiss() {

                    }
                });
            }
        });
    }

    private void initData() {
        if (dataUtils.getData(this) != null) {
            noteItemArrayList.addAll(dataUtils.getData(this));
        }
        listNoteAdapter = new ListNoteAdapter(this, noteItemArrayList);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerViewNote.setLayoutManager(manager);
        recyclerViewNote.setAdapter(listNoteAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_view, menu);

        MenuItem searchItem = menu.findItem(R.id.it_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listNoteAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listNoteAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CREATENOTE: {
                if (data.getSerializableExtra("create") != null) {
                    NoteItem noteItem = (NoteItem) data.getSerializableExtra("create");
                    noteItemArrayList.add(noteItem);
                    listNoteAdapter.notifyDataSetChanged();
                    listNoteAdapter.onDataChanged();
                    dataUtils.saveData(noteItemArrayList, this);
                }
                break;
            }
            case 56789: {
                if (data.getSerializableExtra("edit") != null) {
                    NoteItem noteItem = (NoteItem) data.getSerializableExtra("edit");
                    for (int i = 0; i < noteItemArrayList.size(); i++) {
                        if (noteItemArrayList.get(i).getTimeAndId() == noteItem.getTimeAndId()) {
                            Log.d("nhatnhat", "onActivityResult: "+noteItem.getType());
                            noteItemArrayList.get(i).setTitle(noteItem.getTitle());
                            noteItemArrayList.get(i).setContent(noteItem.getContent());
                            noteItemArrayList.get(i).setType(noteItem.getType());
                            noteItemArrayList.get(i).setContentTextColor(noteItem.getContentTextColor());
                            Log.d("nhatnhat", "onActivityResult: "+ noteItemArrayList.get(i).getContentTextSize()+"/"+noteItem.getContentTextSize());

                            noteItemArrayList.get(i).setContentTextSize(noteItem.getContentTextSize());
                            listNoteAdapter.notifyDataSetChanged();
                            dataUtils.saveData(noteItemArrayList, this);
                            break;
                        }
                    }
                }
                break;
            }
        }
    }
}