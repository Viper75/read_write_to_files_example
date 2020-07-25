package com.codewithsean.readwritetofilesexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.codewithsean.readwritetofilesexample.databinding.MainLayoutBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private MainLayoutBinding binding;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DEFAULT_FILE_NAME = "new_note";
    private boolean saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }
    private void saveNote() {
        String title = binding.titleInput.getText().toString();
        String note = binding.notesInput.getText().toString();

        try (FileOutputStream fos = this.openFileOutput(DEFAULT_FILE_NAME, Context.MODE_PRIVATE)) {
            Log.i(LOG_TAG, "Writing to file...");
            fos.write(title.getBytes());
            fos.write("\n".getBytes());
            fos.write(note.getBytes());
            saved = true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }

        binding.fileContents.setText(readFileContents(DEFAULT_FILE_NAME));
        binding.fileContents.setVisibility(View.VISIBLE);
    }

    private String readFileContents(String filename) {
        try {
            FileInputStream fis = this.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(isr)) {
                Log.i(LOG_TAG, "Retrieving file contents...");
                String line = br.readLine();

                while (line != null) {
                    sb.append(line).append("\n");
                    line = br.readLine();
                }

                return sb.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Exception: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}