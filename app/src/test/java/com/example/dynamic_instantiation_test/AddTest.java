package com.example.dynamic_instantiation_test;

import org.junit.Test;

import static com.example.dynamic_instantiation_test.MainActivity.Add;
import static org.junit.Assert.*;

public class AddTest {

    @Test
    public void add() {
        int actual = Add(2,3);
        int expected = 5;
        assertEquals("The required addition failed",actual,expected);
    }

    @Test
    public void addAgain(){
        int actual = Add(2,-3);
        int expected = -1;
        assertEquals("the addition has failed",actual,expected);
    }

    @Test
    public void addAThirdTime(){
        int actual = Add(-70,35);
        int expected = 35;
        assertEquals("you can't do negative maths",actual,expected);
    }
}