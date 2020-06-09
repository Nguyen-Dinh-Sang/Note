package com.example.mynote.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.R;
import com.example.mynote.adapter.ColorChooseAdapter;
import com.example.mynote.iterface.OnItemClickListener;
import com.example.mynote.model.NoteItem;
import com.example.mynote.utils.BitmapUtils;
import com.example.mynote.view.DrawingView;

import java.io.ByteArrayOutputStream;

import yuku.ambilwarna.AmbilWarnaDialog;

public class NoteActivity extends AppCompatActivity {
    private ImageView imageViewBack, imageViewSave;
    private TextView textViewTitle;
    private EditText editTextTitle, editTextContent;
    private CardView cardView,toolbar;
    private DrawingView drawingView;
    private ImageButton colorchoose,size;
    private enum WORK { CREATE, DETAIL, EDIT};
    private WORK current;
    private NoteItem noteItem;
    private Dialog dialogColorPicker,dialogSize;
    private float textsize;

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
                            int color =editTextContent.getTextColors().getDefaultColor();
                            float textsize = editTextContent.getTextSize();
                            if (title.equals("") || content.equals("")) {
                                Toast.makeText(NoteActivity.this, "title and content must not be blank", Toast.LENGTH_SHORT).show();
                            } else {
                                Long time = System.currentTimeMillis();

                                NoteItem newNote = new NoteItem(time, title, content,color,textsize, NoteItem.TYPE.NormalNote);

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
                            int color =editTextContent.getTextColors().getDefaultColor();
                            float textsize = editTextContent.getTextSize();

                            if (title.equals("") || content.equals("")) {
                                Toast.makeText(NoteActivity.this, "title and content must not be blank", Toast.LENGTH_SHORT).show();
                            } else {
                                Long time = System.currentTimeMillis();
                                NoteItem newNote = new NoteItem(time, title, content,color,textsize, NoteItem.TYPE.DrawNote);

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
                        toolbar.setVisibility(View.VISIBLE);
                        textViewTitle.setText("Edit");
                        current = WORK.EDIT;
                        break;
                    }

                    case EDIT: {
                        String title = editTextTitle.getText().toString().trim();
                        String content = editTextContent.getText().toString().trim();
                        int color =editTextContent.getTextColors().getDefaultColor();

                        NoteItem.TYPE type = NoteItem.TYPE.NormalNote;
                        if(noteItem.getType()== NoteItem.TYPE.DrawNote){
                            Bitmap b = BitmapUtils.getBitmapFromView(NoteActivity.this,drawingView);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream .toByteArray();
                            content =  Base64.encodeToString(byteArray, Base64.DEFAULT);
                            type= NoteItem.TYPE.DrawNote;
                        }

                        if (title.equals("") || content.equals("")) {
                            Toast.makeText(NoteActivity.this, "title and content must not be blank", Toast.LENGTH_SHORT).show();
                        } else {

                            Log.d("nhatnhat", "onClick: "+textsize);
                            NoteItem newNote = new NoteItem(noteItem.getTimeAndId(), title, content,color,textsize,type);

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

        colorchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogColor();
            }
        });
        size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSize();
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
        toolbar=findViewById(R.id.toolbar);
        colorchoose = findViewById(R.id.btnColorPicker);
        size =findViewById(R.id.btnSize);
    }

    private void setView(String view, NoteItem noteItem) {
        switch (view) {
            case "Normal note": {
                textViewTitle.setText(view);
                    cardView.setVisibility(View.VISIBLE);
                    drawingView.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                break;
            }
            case "Draw note": {
             textViewTitle.setText(view);
            cardView.setVisibility(View.GONE);
            drawingView.setVisibility(View.VISIBLE);
            drawingView.setStrokeWidth(12);
                toolbar.setVisibility(View.VISIBLE);
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
                    cardView.setVisibility(View.VISIBLE);
                    drawingView.setVisibility(View.GONE);
                    editTextContent.setText(noteItem.getContent());
                    editTextContent.setTextColor(noteItem.getContentTextColor());
                    Log.d("nhatnhat", "setView: "+noteItem.getContentTextSize());
                    textsize=noteItem.getContentTextSize();
                    editTextContent.setTextSize(noteItem.getContentTextSize());
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
                    editTextContent.setTextColor(noteItem.getContentTextColor());
                    editTextContent.setTextSize(noteItem.getContentTextSize());
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
    private void showDialogColor() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, Color.BLACK, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
                if(cardView.getVisibility()==View.GONE){
                    drawingView.setBrushColor(color);

                }else {
                    editTextContent.setTextColor(color);
                }
            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
            }
        });
        ambilWarnaDialog.show();
    }

    private void showDialogSize() {
        if (dialogSize == null) {
            dialogSize = new Dialog(this);
            dialogSize.setContentView(R.layout.dialog_size);
            final TextView textView = dialogSize.findViewById(R.id.tvCurrentSize);
            final SeekBar seekBar = dialogSize.findViewById(R.id.seekbarSize);
            Button btnOk = dialogSize.findViewById(R.id.btnBrushOK);

            seekBar.setMax(80);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    textView.setText(i + 10 + "");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cardView.getVisibility()==View.GONE){
                        drawingView.setStrokeWidth(seekBar.getProgress() == 0 ? 10:seekBar.getProgress());
                    }else {
                        textsize=seekBar.getProgress() == 0 ? 10:seekBar.getProgress();
                        editTextContent.setTextSize(textsize);
                    }
                    dialogSize.dismiss();
                }
            });
        }

        dialogSize.show();
    }


}
