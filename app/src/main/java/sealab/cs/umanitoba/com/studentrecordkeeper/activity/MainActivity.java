package sealab.cs.umanitoba.com.studentrecordkeeper.activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import sealab.cs.umanitoba.com.studentrecordkeeper.R;
import sealab.cs.umanitoba.com.studentrecordkeeper.controller.StudentController;
import sealab.cs.umanitoba.com.studentrecordkeeper.dao.StudentRepo;

public class MainActivity extends ListActivity  implements android.view.View.OnClickListener{

    String filename = "studentData";
    FloatingActionButton btnAdd;
    Button btnShowFileAction;//btnGetAll,btnReadFromFile,btnWriteToFile;
    TextView student_Id;

    //We binded all the click actions with onClick function, inside which we can differentiate among sender widgets using the id property of view
    @Override  public void onClick(View view) {
        FileOutputStream outputStream;
        if (view== findViewById(R.id.btnAdd)){
            //Moving to another activity
            Intent intent = new Intent(this,StudentDetail.class);
            intent.putExtra("student_Id", 0);
            startActivity(intent);
            //For adding animation to activity transition
            overridePendingTransition(R.anim.abc_fade_in, 100000);
        }
        else if(view == findViewById(R.id.btnShowFileAction)) {
            Intent intent = new Intent(MainActivity.this, FileWriting.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //attaching listeners to widgets
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnShowFileAction= (Button) findViewById(R.id.btnShowFileAction);
        btnShowFileAction.setOnClickListener(this);
    }

    private void ListStudents()
    {
        StudentController repo = new StudentRepo(this);
        ArrayList<HashMap<String, String>> studentList =  repo.getStudentList();

        if(studentList.size()!=0) {
                //since this Activity extends ListActivity, getListView will give the reference to List on the page
                ListView lv = getListView();
                //here we are attaching a listener (for deletion) on list item click action
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        //since we have a textview in view_student_entry.xml as student_Id, we will fetch that and confirm if user wants to delete the entry.
                        student_Id = (TextView) view.findViewById(R.id.student_Id);
                        String studentId = student_Id.getText().toString();
                        Intent objIndent = new Intent(getApplicationContext(),StudentDetail.class);
                        objIndent.putExtra("student_Id", Integer.parseInt( studentId));
                        startActivity(objIndent);
                    }
                });
                //we are now binding the actual data with the List
                ListAdapter adapter = new SimpleAdapter( MainActivity.this,studentList, R.layout.view_student_entry, new String[]{"id","name","age","email"}, new int[] {R.id.student_Id, R.id.student_name, R.id.student_age,R.id.student_email});
                setListAdapter(adapter);
        }else{
                //in case of no data found in file
                ListView lv = getListView();
                ListAdapter adapter = new SimpleAdapter( MainActivity.this,studentList, R.layout.view_student_entry, new String[]{"id","name","age","email"}, new int[] {R.id.student_Id, R.id.student_name, R.id.student_age,R.id.student_email});
                setListAdapter(adapter);
                Toast.makeText(this,"No student!",Toast.LENGTH_SHORT).show();
        }
    }

    //calls in the start too, or if you change orientation
    protected void onResume(){
        super.onResume();
        ListStudents();
    }
}
