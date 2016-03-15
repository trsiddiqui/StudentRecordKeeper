package sealab.cs.umanitoba.com.studentrecordkeeper;

import android.test.ActivityInstrumentationTestCase2;

import sealab.cs.umanitoba.com.studentrecordkeeper.activity.FileWriting;
import com.robotium.solo.Solo;
/**
 * Created by Admin on 3/15/2016.
 */
public class FileReadWriteTests extends ActivityInstrumentationTestCase2<FileWriting> {

    private String textToBeWritten = "Sample text to write and read";
    public Solo solo;
    public FileReadWriteTests() {
        super(FileWriting.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
//        super.tearDown();
        solo.finishOpenedActivities();
    }

    public void testFileWriting() throws Exception {

        //Unlock the lock screen
        solo.unlockScreen();

        solo.clearEditText(0);
        solo.enterText(0, textToBeWritten);


        //Click on action menu item add
        solo.clickOnView(solo.getView(R.id.btnSave));
        //Click on action menu item add
        solo.clickOnView(solo.getView(R.id.btnShow));

        boolean textFound = solo.searchText(textToBeWritten);
        assertTrue("Written text found", textFound);
    }
}
