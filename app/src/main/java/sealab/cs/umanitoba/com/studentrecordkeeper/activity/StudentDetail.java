package sealab.cs.umanitoba.com.studentrecordkeeper.activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sealab.cs.umanitoba.com.studentrecordkeeper.R;
import sealab.cs.umanitoba.com.studentrecordkeeper.controller.StudentController;
import sealab.cs.umanitoba.com.studentrecordkeeper.dao.StudentRepo;
import sealab.cs.umanitoba.com.studentrecordkeeper.model.Student;

public class StudentDetail extends AppCompatActivity implements android.view.View.OnClickListener{
    Button btnSave, btnDelete, btnClose;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextAge;
    private int _Student_Id=0;
    String filename = "studentData";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        //attaching click listener to button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnClose = (Button) findViewById(R.id.btnClose);

        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        //disabling the button for validation, this will be enabled if all the fields on the page are validated
        btnSave.setEnabled(false);
        //findViewById gets reference to the widget by ID
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        //since we are going to get control to this Activity through another Activity, we can receive the data passed by the previous activity by intent.getInExtra("key", defaultValue)
        _Student_Id =0;
        Intent intent = getIntent();
        _Student_Id =intent.getIntExtra("student_Id", 0);
        StudentController repo = new StudentRepo(this);
        Student student = new Student();
        student = repo.getStudentById(_Student_Id);

        editTextAge.setText(String.valueOf(student.age));
        editTextName.setText(student.name);
        editTextEmail.setText(student.email);
        //attaching text changed listener, so every time you write something in the textbox, validation will occur and if passed button will be enable. Currently the validation is only checking if the fields are empty and the email is in correct format. You can write custom validations here.
        TextWatcher listener = new TextWatcher(){
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s){
                btnSave.setEnabled(!editTextAge.getText().toString().isEmpty() && !editTextName.getText().toString().isEmpty() && isEmailValid(editTextEmail.getText().toString()));
            }
        };
        editTextAge.addTextChangedListener(listener);
        editTextName.addTextChangedListener(listener);
        editTextEmail.addTextChangedListener(listener);
    }

    public void onClick(View view) {
        if (view == findViewById(R.id.btnSave)){
            StudentController repo = new StudentRepo(this);
            Student student = new Student();

            student.age= Integer.parseInt(editTextAge.getText().toString());
            student.email=editTextEmail.getText().toString();
            student.name=editTextName.getText().toString();
            student.student_ID=_Student_Id;

            if (_Student_Id==0){
                _Student_Id = repo.insert(student);

                Toast.makeText(this,"New Student Insert",Toast.LENGTH_SHORT).show();
            }else{

                repo.update(student);
                Toast.makeText(this,"Student Record updated",Toast.LENGTH_SHORT).show();

            }
        }else if (view== findViewById(R.id.btnDelete)){

            confirm(_Student_Id, this);
        }else if (view== findViewById(R.id.btnClose)){
            finish();
        }
    }

    private void confirm(final int student_ID, final StudentDetail reference) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this record?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        StudentController repo = new StudentRepo(reference);
                        repo.delete(_Student_Id);
                        Toast.makeText(reference, "Student Record Deleted", Toast.LENGTH_SHORT);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public void finish(){
        super.finish();
        //if you want to do some tasks like garbage removal or need to close any open adapters or connection, you should do that here.
    }
    //utility method
    public boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}