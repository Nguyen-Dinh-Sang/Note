package com.example.mynote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynote.R;
import com.example.mynote.model.NoteItem;

import java.io.Serializable;

public class Main2Activity extends AppCompatActivity {
    private ImageView imageViewBack, imageViewSave;
    private TextView textViewTitle;
    private EditText editTextTitle, editTextContent;
    private enum WORK { CREATE, DETAIL, EDIT};
    private WORK current;
    private NoteItem noteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        getData();
        initEvent();
        initData();
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("create")) {
                current = WORK.CREATE;
                setView(intent.getStringExtra("create"), null);
            }

            if (intent.hasExtra("detail")) {
                current = WORK.DETAIL;
                noteItem = (NoteItem) intent.getSerializableExtra("detail");
                setView("Detail", noteItem);
            }

            if (intent.hasExtra("edit")) {
                current = WORK.EDIT;
                noteItem = (NoteItem) intent.getSerializableExtra("edit");
                setView("Edit", noteItem);
            }
        }
    }

    private void initData() {
    }

    private void initEvent() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (current) {
                    case CREATE: {
                        String title = editTextTitle.getText().toString().trim();
                        String content = editTextContent.getText().toString().trim();

                        if (title.equals("") || content.equals("")) {
                            Toast.makeText(Main2Activity.this, "title and content must not be blank", Toast.LENGTH_SHORT).show();
                        } else {
                            Long time = System.currentTimeMillis();

                            NoteItem newNote = new NoteItem(time, title, content);

                            Intent intent = new Intent();
                            intent.putExtra("create", newNote);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                        break;
                    }

                    case DETAIL: {
                        editTextTitle.setEnabled(true);
                        editTextContent.setEnabled(true);
                        imageViewSave.setImageResource(R.drawable.ic_save);
                        textViewTitle.setText("Edit");
                        current = WORK.EDIT;
                        break;
                    }

                    case EDIT: {
                        String title = editTextTitle.getText().toString().trim();
                        String content = editTextContent.getText().toString().trim();

                        if (title.equals("") || content.equals("")) {
                            Toast.makeText(Main2Activity.this, "title and content must not be blank", Toast.LENGTH_SHORT).show();
                        } else {
                            NoteItem newNote = new NoteItem(noteItem.getTimeAndId(), title, content);

                            Intent intent = new Intent();
                            intent.putExtra("edit", newNote);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                        break;
                    }
                }
            }
        });
    }

    private void initView() {
        imageViewBack = findViewById(R.id.iv_back);
        imageViewSave = findViewById(R.id.iv_save);
        textViewTitle = findViewById(R.id.tv_title_toolbar);

        editTextTitle = findViewById(R.id.ed_title);
        editTextContent = findViewById(R.id.ed_content);
    }

    private void setView(String view, NoteItem noteItem) {
        switch (view) {
            case "create": {
                textViewTitle.setText(view);
                break;
            }

            case "Detail": {
                textViewTitle.setText(view);

                editTextTitle.setText(noteItem.getTitle());
                editTextTitle.setEnabled(false);

                editTextContent.setText(noteItem.getContent());
                editTextContent.setEnabled(false);

                imageViewSave.setImageResource(R.drawable.ic_edit2);
            }

            case "Edit": {
                textViewTitle.setText(view);
                editTextTitle.setText(noteItem.getTitle());
                editTextContent.setText(noteItem.getContent());
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
