package sealab.cs.umanitoba.com.studentrecordkeeper;
import android.content.Intent;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentDetail extends AppCompatActivity implements android.view.View.OnClickListener{
    FloatingActionButton btnSave;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextAge;
    private int _Student_Id=0;
    String filename = "studentData";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        //attaching click listener to button
        btnSave = (FloatingActionButton) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
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
        //setting default texts
        editTextAge.setText("");
        editTextName.setText("");
        editTextEmail.setText("");
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
        if (view == findViewById(R.id.btnSave)) {
            //reading data to file. Since we have saved our data to file, we need to know the id of the new item to be inserted. So we will fetch the last item, and increment its id by 1 as the new items id.
            StringBuffer fileContent = new StringBuffer("");
            try {
                FileInputStream f = openFileInput(filename);

                byte[] buffer = new byte[1024];
                int n = 0;
                while ((n = f.read(buffer)) != -1) {
                    fileContent.append(new String(buffer, 0, n));
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Adding first record.", Toast.LENGTH_SHORT).show();
            }
            String[] data = fileContent.toString().split("~");
            int lastId = 0;
            if (!fileContent.toString().isEmpty()) {
                lastId = Integer.parseInt(data[data.length - 1].split(",")[0]);
            }
            Student student = new Student();
            student.student_ID = lastId + 1;
            student.age = Integer.parseInt(editTextAge.getText().toString());
            student.email = editTextEmail.getText().toString();
            student.name = editTextName.getText().toString();
            //preparing the string to be appended to the string for the new record
            String s = "";
            s = "~" + student.student_ID + "," + student.name + "," + student.age + "," + student.email + "~";
            //writing the final data string to file
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(filename, Context.MODE_APPEND);
                outputStream.write(s.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "First Record Added, Tada", Toast.LENGTH_SHORT).show();
            //finish will exit and destroy this activity and will go back to the one it is called from
            finish();
        }
    }
    @Override public void finish(){
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