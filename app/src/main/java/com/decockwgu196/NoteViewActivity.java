package com.decockwgu196;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.decockwgu196.model.NoteViewModel;

public class NoteViewActivity extends AppCompatActivity {
    NoteViewModel noteViewModel;

    TextView title;
    TextView text;
    ImageButton edit;
    ImageButton delete;

    private int id;
    private int courseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        title = findViewById(R.id.note_view_title);
        text = findViewById(R.id.note_view_text);
        edit = findViewById(R.id.note_view_edit);
        delete = findViewById(R.id.note_view_delete);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Note");
        actionBar.show();

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(NoteViewActivity.this
                .getApplication())
                .create(NoteViewModel.class);

        Bundle data = getIntent().getExtras();

        if(data != null){
            id = getIntent().getExtras().getInt("note_id");
            noteViewModel.get(id).observe(this, note -> {
                title.setText(note.getTitle());
                text.setText(note.getText());
                courseId = note.getCourseId();
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, CourseViewActivity.class);
                intent.putExtra("course_id", courseId);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editNote(View view){
        Intent intent = new Intent(this, UpdateNoteActivity.class);
        intent.putExtra("note_id", id);
        startActivity(intent);
    }

    public void deleteNote(View view){
        noteViewModel.get(id).observe(this, note -> {
            NoteViewModel.delete(note);
        });
        Intent intent = new Intent(this, NoteListActivity.class);
        startActivity(intent);
        finish();
    }
}