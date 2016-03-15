package sealab.cs.umanitoba.com.studentrecordkeeper;

import android.test.ActivityInstrumentationTestCase2;

import sealab.cs.umanitoba.com.studentrecordkeeper.activity.MainActivity;
import sealab.cs.umanitoba.com.studentrecordkeeper.activity.StudentDetail;

import com.robotium.solo.Solo;


public class StudentRecordKeeperTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String NAME1 = "Brian Adams";
    private static final String EMAIL1 = "brian@adams.me";
    private static final String AGE1 = "20";
    private static final String NAME2 = "David Flemming";
    private static final String EMAIL2 = "david@flemming.me";
    private static final String AGE2 = "20";
    private static final String EDITTED_NAME1 = "Steve Waugh";

    private Solo solo;

    public StudentRecordKeeperTests() {
        super(MainActivity.class);
    }

    //override setUp and do all the stuff you are supposed to do in the beginning of an app, like creating DB if not present etc.
    @Override
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(getInstrumentation());
        //Getting activity reference to perform operation in the test cases below
        getActivity();
    }

    //override tearDown and do all the stuff you are supposed to do in the end, delete temp files etc
    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    //1. The number of public classes with test prepended to them you have, the number of times it is going to run your application and close it, and re run it for the next test
    //2. If you want to do it all in one go, make one public class for test, all the others private. And then call other methods in the public method.

    //I have chosen option 1, so I have 3 tests. For each test application restarts (due to tearDown method).
    public void testAAddStudent() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //solo.sleep(1000) inserts a delay of 1000ms. I have put it here so you can see what's happening.
        //You probably only going to need this if you wait for some execution in your app.
        solo.sleep(500);
        //Click on button add, you can either pass a view in the method as shown below, or call solo.clickOnButton(String text) where text is the text on button. The view you pass should be a part of current activity layout.
        solo.clickOnView(solo.getView(R.id.btnAdd));
        //Assert that StudentDetail activity is opened
        solo.assertCurrentActivity("Expected StudentDetail Activity", StudentDetail.class);
        //In text field 0, enter Name
        solo.sleep(500);
        solo.enterText(0, NAME1);
        //In text field 1, enter Email
        solo.sleep(500);
        solo.enterText(1, EMAIL1);
        //In text field 2, enter Age. We have to clear it since we already have a 0 there.
        solo.sleep(500);
        solo.clearEditText(2);
        solo.enterText(2, AGE1);
        //Click on button save
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.btnSave));
        //Click on button close, you can also call solo.goBack() here
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.btnClose));
        //Assert that MainActivity activity is opened
        solo.assertCurrentActivity("Expected MainActivity Activity", MainActivity.class);
        //Click on button add
        solo.clickOnView(solo.getView(R.id.btnAdd));
        //Assert that StudentDetail activity is opened
        solo.assertCurrentActivity("Expected StudentDetail Activity", StudentDetail.class);
        //In text field 0, enter Name
        solo.enterText(0, NAME2);
        //In text field 1, enter EMAIL2
        solo.enterText(1, EMAIL2);
        //In text field 2, enter AGE2
        solo.clearEditText(2);
        solo.enterText(2, AGE2);
        //Click on button save
        solo.clickOnView(solo.getView(R.id.btnSave));
        //Click on button close
        solo.clickOnView(solo.getView(R.id.btnClose));
        //Takes a screenshot and saves it in "/sdcard/Robotium-Screenshots/".
        solo.takeScreenshot();
        //Search for names of Student 1 and Student 2 on the whole screen
        boolean studentsFound = solo.searchText(NAME1) && solo.searchText(NAME2);
        //Assert that Student 1 & Student 2 are found
        assertTrue("Student 1 and Student 2 are found", studentsFound);
        //To clean up after the test case
        //If I had taken option 2 I would have declare the method below as private and call it here.
        //deleteStudents();
    }

    public void testBEditStudentName() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Click on first item of Student List
        solo.sleep(500);
        solo.clickInList(0);
        //Assert that StudentDetail activity is opened
        solo.assertCurrentActivity("Expected StudentDetail Activity", StudentDetail.class);
        //In text field 0, enter Name
        solo.sleep(500);
        solo.clearEditText(0);
        solo.enterText(0, EDITTED_NAME1);
        //Click on action menu item Save
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.btnSave));
        //Click on action menu item Add
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.btnClose));
        //Assert that StudentDetail activity is opened
        solo.assertCurrentActivity("Expected MainActivity Activity", MainActivity.class);
        //Takes a screenshot and saves it in "/sdcard/Robotium-Screenshots/".
        solo.takeScreenshot();
        //Search for Student 1 and Student 2
        boolean studentFound = solo.searchText(EDITTED_NAME1);
        //Assert that Student 1 & Student 2 are found
        assertTrue("Student 1 is found", studentFound);
    }

    public void testCDeleteStudent() throws Exception{
        //Unlock the lock screen
        solo.unlockScreen();
        //Click on first item of Student List
        solo.sleep(500);
        solo.clickInList(0);
        //Assert that StudentDetail activity is opened
        solo.assertCurrentActivity("Expected StudentDetail Activity", StudentDetail.class);
        //Click on action Delete
        solo.clickOnView(solo.getView(R.id.btnDelete));

        solo.waitForDialogToOpen();
        solo.clickOnButton("Yes");
        //Assert that MainActivity activity is opened
        solo.assertCurrentActivity("Expected MainActivity Activity", StudentDetail.class);
        //Click on first item of Student List
        solo.sleep(500);
        solo.clickInList(0);
        //Assert that StudentDetail activity is opened
        solo.assertCurrentActivity("Expected StudentDetail Activity", StudentDetail.class);
        //Click on action Delete
        solo.sleep(500);
        solo.clickOnView(solo.getView(R.id.btnDelete));

        solo.waitForDialogToOpen();
        solo.clickOnButton("Yes");
        //Assert that MainActivity activity is opened
        solo.assertCurrentActivity("Expected MainActivity Activity", StudentDetail.class);

        boolean studentsFound = solo.searchText(NAME1) && solo.searchText(NAME2);
        //Assert that Student 1 & Student 2 are found
        assertFalse("Note 1 and/or Note 2 are not found", studentsFound);
    }
}