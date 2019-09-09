package com.cataltas.notebookum.async;

import android.os.AsyncTask;

import com.cataltas.notebookum.models.Note;
import com.cataltas.notebookum.persistance.NoteDAO;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDAO mNoteDAO;

    public UpdateAsyncTask(NoteDAO mNoteDAO) {
        this.mNoteDAO = mNoteDAO;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDAO.update(notes);
        return null;
    }
}
