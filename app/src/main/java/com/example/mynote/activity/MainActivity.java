package com.example.mynote.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.example.mynote.R;
import com.example.mynote.adapter.ListNoteAdapter;
import com.example.mynote.model.NoteItem;
import com.example.mynote.utils.DataUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
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
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("create", "Create");
                startActivityForResult(intent, 12345);
            }
        });
    }

    private void initData() {
        if (dataUtils.getData(this) != null) {
            noteItemArrayList.addAll(dataUtils.getData(this));
        }
        listNoteAdapter = new ListNoteAdapter(this, noteItemArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewNote.setLayoutManager(linearLayoutManager);
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
            case 12345: {
                if (data.getSerializableExtra("create") != null) {
                    NoteItem noteItem = (NoteItem) data.getSerializableExtra("create");
                    noteItemArrayList.add(noteItem);
                    listNoteAdapter.notifyDataSetChanged();
                    dataUtils.saveData(noteItemArrayList, this);
                }
                break;
            }
            case 56789: {
                if (data.getSerializableExtra("edit") != null) {
                    NoteItem noteItem = (NoteItem) data.getSerializableExtra("edit");
                    for (int i = 0; i < noteItemArrayList.size(); i++) {
                        if (noteItemArrayList.get(i).getTimeAndId() == noteItem.getTimeAndId()) {
                            noteItemArrayList.get(i).setTitle(noteItem.getTitle());
                            noteItemArrayList.get(i).setContent(noteItem.getContent());
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