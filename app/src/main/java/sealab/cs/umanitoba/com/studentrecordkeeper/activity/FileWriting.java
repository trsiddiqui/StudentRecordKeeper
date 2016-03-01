package sealab.cs.umanitoba.com.studentrecordkeeper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import sealab.cs.umanitoba.com.studentrecordkeeper.R;

public class FileWriting extends AppCompatActivity   implements android.view.View.OnClickListener{
    Button btnSave,btnShow,btnCancel;
    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.btnSave)){

            String filename = "myfile";
            String string = ((EditText)findViewById(R.id.editTextName)).getText().toString();
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
                Toast.makeText(this, "Writing Successful.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error while writing.",Toast.LENGTH_SHORT).show();
            }

        }
        else if(view == findViewById(R.id.btnShow)){
            String filename = "myfile";
            try {
                FileInputStream f = openFileInput(filename);
                StringBuffer fileContent = new StringBuffer("");

                byte[] buffer = new byte[1024];
                int n = 0;
                while ((n = f.read(buffer)) != -1)
                {
                    fileContent.append(new String(buffer, 0, n));
                }
                ((TextView)findViewById(R.id.textView5)).setText(fileContent);
            } catch (java.io.IOException e) {
                e.printStackTrace();
                Toast.makeText(this,"No file exists in mobile. Use the text area above to write to a file and press save.",Toast.LENGTH_SHORT).show();
            }

        }
        else if(view == findViewById(R.id.btnCancel)){
            finish();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_writing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnShow = (Button) findViewById(R.id.btnShow);
        btnShow.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        ((TextView)findViewById(R.id.textView5)).setMovementMethod(new ScrollingMovementMethod());

        String filename = "myfile";
        try {
            FileInputStream f = openFileInput(filename);
            StringBuffer fileContent = new StringBuffer("");

            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = f.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }
            Toast.makeText(this,fileContent,Toast.LENGTH_SHORT).show();
            ((TextView)findViewById(R.id.textView5)).setText(fileContent);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
        }
    }

}
