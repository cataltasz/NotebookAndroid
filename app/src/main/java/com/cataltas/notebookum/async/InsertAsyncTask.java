package com.cataltas.notebookum.async;

import android.os.AsyncTask;

import com.cataltas.notebookum.models.Note;
import com.cataltas.notebookum.persistance.NoteDAO;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDAO mNoteDAO;

    public InsertAsyncTask(NoteDAO noteDAO) {
        mNoteDAO = noteDAO;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDAO.insert(notes);
        return null;
    }
}
