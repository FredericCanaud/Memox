package com.example.memox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memox.R;
import com.example.memox.callbacks.NoteEventListener;
import com.example.memox.model.Note;
import com.example.memox.helpers.NoteHelpers;

import java.util.ArrayList;
import java.util.List;

/**********************         AFFICHAGE DES NOTES DANS LE MENU     *************************/

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private Context context;
    private ArrayList<Note> notes;
    private NoteEventListener listener;
    private boolean multiCheckMode = false;


    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false);
        return new NoteHolder(v);
    }

    /**********************         ATTRIBUTION DES DONNEES DANS LE HOLDER    *************************/

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        final Note note = getNote(position);
        if (note != null) {
            holder.noteText.setText(note.getTexte());
            holder.noteDate.setText(NoteHelpers.dateFromLong(note.getDate()));
            holder.itemView.setOnClickListener(view -> listener.onNoteClick(note));

            holder.itemView.setOnLongClickListener(view -> {
                listener.onNoteLongClick(note);
                return false;
            });

            if (multiCheckMode) {
                holder.checkBox.setVisibility(View.VISIBLE);
            } else holder.checkBox.setVisibility(View.GONE);


        }
    }

    /**********************         RECUPERATION DES NOTES     *************************/

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private Note getNote(int position) {
        return notes.get(position);
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        for (Note n : this.notes) {
            notes.add(n);
        }

        return notes;
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView noteText, noteDate;
        CheckBox checkBox;

        public NoteHolder(View itemView) {
            super(itemView);
            noteDate = itemView.findViewById(R.id.note_date);
            noteText = itemView.findViewById(R.id.note_text);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }

    public void setMultiCheckMode(boolean multiCheckMode) {
        this.multiCheckMode = multiCheckMode;
        if (!multiCheckMode)
            for (Note note : this.notes) {
                note.setChecked(false);
            }
        notifyDataSetChanged();
    }
}
