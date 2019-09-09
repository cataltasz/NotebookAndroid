package com.cataltas.notebookum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cataltas.notebookum.adapters.NotesRecyclerAdapter;
import com.cataltas.notebookum.models.Note;
import com.cataltas.notebookum.persistance.NoteRepository;
import com.cataltas.notebookum.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NotesRecyclerAdapter.OnNoteListener, View.OnClickListener
{

    private static final String TAG ="MainActivity";

    //UI components
    private RecyclerView recyclerView;


    //Vars
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNoteRepository = new NoteRepository(this);
        setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));

        //Connecting UI components
        recyclerView = findViewById(R.id.recyclerView);
        notesRecyclerAdapter = new NotesRecyclerAdapter(mNotes, this);
        initRecyclerView();
        retrieveNotes();
        mNotes.add(new Note("deneme", "deneme", "deneme"));

        findViewById(R.id.fab).setOnClickListener(this);
    }

    private void retrieveNotes(){
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(mNotes.size()>0)
                    mNotes.clear();
                if (mNotes != null)
                    mNotes.addAll(notes);
                notesRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
    private void insertNotes(){
        for(int i=0;i<1000;i++)
            mNotes.add(new Note(("title #"+i),"content #"+i,"Jan 2019"));
        notesRecyclerAdapter.notifyDataSetChanged();
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notesRecyclerAdapter);
        VerticalSpacingItemDecorator v = new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration(v);
        new ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(MainActivity.this,NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this,NoteActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Note note){
        mNotes.remove(note);
        mNoteRepository.deleteNote(note);
        notesRecyclerAdapter.notifyDataSetChanged();
    }
    private ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
