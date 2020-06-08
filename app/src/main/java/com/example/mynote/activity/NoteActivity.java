package com.example.mynote.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.mynote.R;
import com.example.mynote.model.NoteItem;
import com.example.mynote.utils.BitmapUtils;
import com.example.mynote.view.DrawingView;

import java.io.ByteArrayOutputStream;

public class NoteActivity extends AppCompatActivity {
    private ImageView imageViewBack, imageViewSave;
    private TextView textViewTitle;
    private EditText editTextTitle, editTextContent;
    private CardView cardView;
    private DrawingView drawingView;
    private enum WORK { CREATE, DETAIL, EDIT};
    private WORK current;
    private NoteItem noteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
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
                        if(drawingView.getVisibility()==View.GONE) {
                            String title = editTextTitle.getText().toString().trim();
                            String content = editTextContent.getText().toString().trim();

                            if (title.equals("") || content.equals("")) {
                                Toast.makeText(NoteActivity.this, "title and content must not be blank", Toast.LENGTH_SHORT).show();
                            } else {
                                Long time = System.currentTimeMillis();

                                NoteItem newNote = new NoteItem(time, title, content, NoteItem.TYPE.NormalNote);

                                Intent intent = new Intent();
                                intent.putExtra("create", newNote);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }else {
                            String title = editTextTitle.getText().toString().trim();
                            Bitmap b = BitmapUtils.getBitmapFromView(NoteActivity.this,drawingView);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream .toByteArray();
                            String content =  Base64.encodeToString(byteArray, Base64.DEFAULT);

                            if (title.equals("") || content.equals("")) {
                                Toast.makeText(NoteActivity.this, "title and content must not be blank", Toast.LENGTH_SHORT).show();
                            } else {
                                Long time = System.currentTimeMillis();

                                NoteItem newNote = new NoteItem(time, title, content, NoteItem.TYPE.DrawNote);

                                Intent intent = new Intent();
                                intent.putExtra("create", newNote);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                        break;
                    }

                    case DETAIL: {
                        editTextTitle.setEnabled(true);
                        editTextContent.setEnabled(true);
                        if(noteItem.getType()== NoteItem.TYPE.DrawNote)
                            drawingView.setCanDraw(true);
                        imageViewSave.setImageResource(R.drawable.ic_save);
                        textViewTitle.setText("Edit");
                        current = WORK.EDIT;
                        break;
                    }

                    case EDIT: {
                        String title = editTextTitle.getText().toString().trim();
                        String content = editTextContent.getText().toString().trim();
                        if(noteItem.getType()== NoteItem.TYPE.DrawNote){
                            Bitmap b = BitmapUtils.getBitmapFromView(NoteActivity.this,drawingView);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream .toByteArray();
                            content =  Base64.encodeToString(byteArray, Base64.DEFAULT);
                        }

                        if (title.equals("") || content.equals("")) {
                            Toast.makeText(NoteActivity.this, "title and content must not be blank", Toast.LENGTH_SHORT).show();
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
        drawingView = findViewById(R.id.dv_note);
        cardView = findViewById(R.id.cv_note);
    }

    private void setView(String view, NoteItem noteItem) {
        switch (view) {
            case "Normal note": {
                textViewTitle.setText(view);

                    cardView.setVisibility(View.VISIBLE);
                    drawingView.setVisibility(View.GONE);
                break;
            }
            case "Draw note": {
             textViewTitle.setText(view);
            cardView.setVisibility(View.GONE);
            drawingView.setVisibility(View.VISIBLE);
            drawingView.setStrokeWidth(12);
                break;
            }

            case "Detail": {
                textViewTitle.setText(view);

                editTextTitle.setText(noteItem.getTitle());
                editTextTitle.setEnabled(false);
                imageViewSave.setImageResource(R.drawable.ic_edit2);

                if(noteItem.getType()== NoteItem.TYPE.DrawNote){
                    cardView.setVisibility(View.GONE);
                    drawingView.setVisibility(View.VISIBLE);
                    drawingView.setCanDraw(false);
                    byte[] item =Base64.decode(noteItem.getContent(),Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(item, 0, item.length);
                    drawingView.setImageBitmap(bitmap);
                }else {
                    editTextContent.setText(noteItem.getContent());
                    editTextContent.setEnabled(false);
                }
                break;
            }

            case "Edit": {
                editTextTitle.setText(noteItem.getTitle());

                if(noteItem.getType()== NoteItem.TYPE.DrawNote){
                    cardView.setVisibility(View.GONE);
                    drawingView.setVisibility(View.VISIBLE);
                    drawingView.setCanDraw(true);

                }else {
                    editTextContent.setText(noteItem.getContent());
                }
                break;

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
