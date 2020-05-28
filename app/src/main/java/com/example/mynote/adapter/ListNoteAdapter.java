package com.example.mynote.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.R;
import com.example.mynote.activity.Main2Activity;
import com.example.mynote.activity.MainActivity;
import com.example.mynote.model.NoteItem;
import com.example.mynote.utils.DataUtils;

import java.util.ArrayList;

public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<NoteItem> noteItemArrayList;
    private ArrayList<NoteItem> noteItemArrayListFull;

    public ListNoteAdapter(Context context, ArrayList<NoteItem> noteItemArrayList) {
        this.context = context;
        this.noteItemArrayList = noteItemArrayList;
        noteItemArrayListFull = new ArrayList<>(noteItemArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteItem noteItem = noteItemArrayList.get(position);
        holder.textViewContent.setText(noteItem.getContent());
        holder.textViewTitle.setText(noteItem.getTitle());
        holder.textViewTime.setText(DataUtils.dateFromLong(noteItem.getTimeAndId()));
    }

    @Override
    public int getItemCount() {
        return noteItemArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<NoteItem> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(noteItemArrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (NoteItem item : noteItemArrayListFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern) || item.getContent().toLowerCase().contains(filterPattern)) {
                        filterList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteItemArrayList.clear();
            noteItemArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewTime, textViewContent;
        LinearLayout linearLayoutMenuOption;
        ImageView imageViewEdit, imageViewDelete, imageViewClose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tv_title);
            textViewTime = itemView.findViewById(R.id.tv_time);
            textViewContent = itemView.findViewById(R.id.tv_content);

            linearLayoutMenuOption = itemView.findViewById(R.id.ll_menu_option);
            imageViewClose = itemView.findViewById(R.id.iv_close);
            imageViewEdit = itemView.findViewById(R.id.iv_edit);
            imageViewDelete = itemView.findViewById(R.id.iv_delete);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    linearLayoutMenuOption.setVisibility(View.VISIBLE);
                    new CountDownTimer(3000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            linearLayoutMenuOption.setVisibility(View.GONE);
                        }
                    }.start();
                    return true;
                }
            });

            imageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutMenuOption.setVisibility(View.GONE);
                }
            });

            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Main2Activity.class);
                    intent.putExtra("edit", noteItemArrayList.get(getPosition()));
                    ((Activity) context).startActivityForResult(intent, 56789);
                }
            });

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteItemArrayList.remove(noteItemArrayList.get(getPosition()));
                    notifyDataSetChanged();
                    DataUtils dataUtils = new DataUtils();
                    dataUtils.saveData(noteItemArrayList, context);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Main2Activity.class);
                    intent.putExtra("detail", noteItemArrayList.get(getPosition()));
                    ((Activity) context).startActivityForResult(intent, 56789);
                }
            });
        }
    }
}
