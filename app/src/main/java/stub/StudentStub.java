package stub;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sealab.cs.umanitoba.com.studentrecordkeeper.controller.StudentController;
import sealab.cs.umanitoba.com.studentrecordkeeper.model.Student;

/**
 * Created by Admin on 2/29/2016.
 */
public class StudentStub implements StudentController{

    ArrayList<HashMap<String, String>> studentList;
    public StudentStub(Context context){
        studentList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> student = new HashMap<String, String>();
        student.put("id", "1");
        student.put("name", "Mark");
        student.put("age", "22");
        student.put("email", "Mark@cs.umanitoba.ca");
        studentList.add(student);
        student.put("id", "2");
        student.put("name", "Adam");
        student.put("age", "23");
        student.put("email", "Adam@cs.umanitoba.ca");
        studentList.add(student);
        student.put("id", "3");
        student.put("name", "Betsy");
        student.put("age", "23");
        student.put("email", "Betsy@cs.umanitoba.ca");
        studentList.add(student);
        student.put("id", "4");
        student.put("name", "Julian");
        student.put("age", "24");
        student.put("email", "Julian@cs.umanitoba.ca");
        studentList.add(student);
        student.put("id", "5");
        student.put("name", "Ahmad");
        student.put("age", "21");
        student.put("email", "Ahmad@cs.umanitoba.ca");
        studentList.add(student);
        student.put("id", "6");
        student.put("name", "Sarah");
        student.put("age", "26");
        student.put("email", "Sarah@cs.umanitoba.ca");
        studentList.add(student);
        student.put("id", "7");
        student.put("name", "Eva");
        student.put("age", "28");
        student.put("email", "Eva@cs.umanitoba.ca");
        studentList.add(student);
    }
    @Override
    public int insert(Student student) {

    return (int) student.student_ID;
}

    @Override
    public void delete(int student_Id) {
    }

    @Override
    public void update(Student student) {
    }

    @Override
    public ArrayList<HashMap<String, String>> getStudentList() {
        return studentList;

    }

    @Override
    public Student getStudentById(int Id){

        Student student = new Student();

                student.student_ID =7;
                student.name ="Eva";
                student.email  ="Eva@cs.umanitoba.ca";
                student.age =28;

        return student;
    }
}
