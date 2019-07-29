package de.hhu.stups.codegenerator.cpp;

import org.junit.Test;

public class TestProjects extends TestCpp {

    @Test
    public void testProject() throws Exception {
        //This might be a bug in the parser in the scoping part
        testCpp("project1/A", "A", "AAddition.stCpp", true);
    }

    @Test
    public void testProject2() throws Exception {
        testCpp("project2/MachineA", "MachineA", "MachineAAddition.stCpp", true);
    }

    @Test
    public void testProject3() throws Exception {
        testCpp("project3/A");
    }

    @Test
    public void testProject5() throws Exception {
        testCpp("project5/Lift2");
    }

    @Test
    public void testProject6() throws Exception {
        testCpp("project6/Lift2");
    }

    @Test
    public void testProject7() throws Exception {
        testCpp("project7/Lift2");
    }

    @Test
    public void testProject8() throws Exception {
        testCpp("project8/Lift2");
    }

    @Test
    public void testProject9() throws Exception {
        testCpp("project9/Lift2");
    }

    @Test
    public void testProject10() throws Exception {
        testCpp("project10/Lift2");
    }
}
