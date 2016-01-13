package sealab.cs.umanitoba.com.studentrecordkeeper;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
        File file = new File(filename);
        if(file != null) {
            //reading from file
            StringBuffer fileContent = new StringBuffer("");
            try {
                FileInputStream f = openFileInput(filename);
                byte[] buffer = new byte[1024];
                int n = 0;
                while ((n = f.read(buffer)) != -1)
                {
                    fileContent.append(new String(buffer, 0, n));
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
                //showing toast notification
                Toast.makeText(this, "No Data file currently existing in mobile.",Toast.LENGTH_SHORT).show();
                //showing yes no confirmation alert
                new AlertDialog.Builder(this)
                        .setMessage("Do you want to create a new record?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(MainActivity.this,StudentDetail.class);
                                intent.putExtra("student_Id", 0);
                                startActivity(intent);
                                overridePendingTransition(R.anim.abc_fade_in, 100000);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return;
            }
            //since we store our results in a file in csv format, we are going to just read the file and perform split operations to see the records
            String[] data = fileContent.toString().split("~");
            ArrayList<HashMap<String, String>> studentList = new ArrayList<HashMap<String, String>>();
            for(int a = 0 ; a < data.length; a++) {
                HashMap<String, String> student = new HashMap<String, String>();
                if(data[a].split(",").length > 1){
                    String[] thisData = data[a].split(",");
                    student.put("id", thisData[0]);
                    student.put("name", thisData[1]);
                    student.put("age", thisData[2]);
                    student.put("email", thisData[3]);
                    studentList.add(student);
                }
            }
            if(studentList.size()!=0) {
                //since this Activity extends ListActivity, getListView will give the reference to List on the page
                ListView lv = getListView();
                //here we are attaching a listener (for deletion) on list item click action
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        //since we have a textview in view_student_entry.xml as student_Id, we will fetch that and confirm if user wants to delete the entry.
                        int student_Id = Integer.parseInt(((TextView)view.findViewById(R.id.student_Id)).getText().toString());
                        confirm(student_Id);
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
    }

    private void confirm(final int student_ID) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this record?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
                        }
                        String[] data = fileContent.toString().split("~");
                        String dataString = "";
                        for (int i = 0; i < data.length; i++) {
                            if (!data[i].isEmpty()) {
                                if (Integer.parseInt(data[i].split(",")[0]) != student_ID) {
                                    dataString += TextUtils.join(",", data[i].split(",")) + "~";
                                }
                            }
                        }
                        FileOutputStream outputStream;
                        try {
                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(dataString.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ListStudents();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    //calls in the start too, or if you change orientation
    protected void onResume(){
        super.onResume();
        ListStudents();
    }
}
