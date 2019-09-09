package com.cataltas.notebookum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cataltas.notebookum.models.Note;
import com.cataltas.notebookum.persistance.NoteRepository;
import com.cataltas.notebookum.util.Utility;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{

    //UI components
    private LinedEditText editText;
    private EditText titleEdit;
    private TextView titleView;
    private ImageButton checkButton;
    private ImageButton backButton;
    private RelativeLayout relativeLayoutBack;
    private RelativeLayout relativeLayoutCheck;

    //vars
    private boolean isNewNote;
    private Note note;
    private GestureDetector gestureDetector;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mNoteRepository = new NoteRepository(this);
        //UI components
        editText = findViewById(R.id.note_text);
        titleEdit = findViewById(R.id.note_title_edit);
        titleView = findViewById(R.id.note_title);
        checkButton = findViewById(R.id.toolbar_check_mark);
        backButton = findViewById(R.id.toolbar_back_arrow);
        relativeLayoutBack = findViewById(R.id.back_arrow_container);
        relativeLayoutCheck = findViewById(R.id.check_mark_container);

        setListeners();
        setStartingCondition();
        setNoteProperties();
    }

    private void setStartingCondition(){
        if(getIntent().hasExtra("selected_note")) {
            note = getIntent().getParcelableExtra("selected_note");
            setDisplayMode();
            titleView.setText(note.getTitle());
            editText.setText(note.getContent());
            isNewNote = false;
        }else{
            setEditMode();
            isNewNote = true;
            note = new Note();
        }
    }

    // to hide keyboard when it is not needed anymore.
    private void hideSoftKeyboard(){
        InputMethodManager inn = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if(view == null) view = titleEdit;
        inn.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setNote() {
        note.setContent(editText.getText().toString());
        note.setTitle(titleEdit.getText().toString());
        if(isNewNote) note.setTimeStamp(Utility.getCurrentTimeStamp());
    }

    private void disableEditText(){
        editText.setKeyListener(null);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setCursorVisible(false);
        editText.clearFocus();
    }

    private void enableEditText(){
        editText.setKeyListener(new EditText(this).getKeyListener());
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setCursorVisible(true);
    }

    //when text are editable
    private void setEditMode(){
        titleView.setVisibility(View.GONE);
        titleEdit.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.VISIBLE);
        relativeLayoutCheck.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        relativeLayoutBack.setVisibility(View.GONE);
        enableEditText();
    }

    // when text are not editable.
    private void setDisplayMode(){
        titleView.setVisibility(View.VISIBLE);
        titleEdit.setVisibility(View.GONE);
        checkButton.setVisibility(View.GONE);
        relativeLayoutCheck.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        relativeLayoutBack.setVisibility(View.VISIBLE);
        hideSoftKeyboard();
        disableEditText();
    }

    // Set properties of noteactivity when first opened.
    private void setNoteProperties(){
        if(!isNewNote){
            titleEdit.setText(note.getTitle());
            titleView.setText(note.getTitle());
            editText.setText(note.getContent());
        }else{
            titleEdit.setText("New Note");
        }
    }

    private void saveChanges(){
        if(isNewNote){
            saveNewNote();
        }else{
            updateNote();
        }
    }

    private void updateNote() {
        mNoteRepository.updateNote(note);
    }

    private void saveNewNote(){
        mNoteRepository.insertNoteTask(note);
    }

    // Set listeners of views
    private void setListeners(){
        editText.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this,this);

        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditMode();
                titleEdit.requestFocus();
                titleEdit.setSelection(titleEdit.getText().length());
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNote();
                saveChanges();
                isNewNote = false;
                setNoteProperties();
                setDisplayMode();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Double tap method util
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    // What happens when doubletapping to edittext
    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        setEditMode();
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
        return false;
    }

    //To control what will happen when back button of the system clicked
    @Override
    public void onBackPressed() {
        if(titleEdit.getVisibility()==View.VISIBLE)
            setDisplayMode();
        else
            super.onBackPressed();
    }

    //Two methods to provide screen rotation.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("mode", relativeLayoutCheck.getVisibility());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int mode = savedInstanceState.getInt("mode");

        if(mode == View.GONE) setDisplayMode();
        else setEditMode();
    }








    //Unused event methods
    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }



}
