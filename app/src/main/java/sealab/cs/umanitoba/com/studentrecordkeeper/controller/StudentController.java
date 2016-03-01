package sealab.cs.umanitoba.com.studentrecordkeeper.controller;

import java.util.ArrayList;
import java.util.HashMap;

import sealab.cs.umanitoba.com.studentrecordkeeper.model.Student;

/**
 * Created by Admin on 2/29/2016.
 */
public interface StudentController {
    public int insert(Student student);
    public void update(Student student);
    public void delete(int student_Id);
    public ArrayList<HashMap<String, String>> getStudentList();
    public Student getStudentById(int Id);
}
