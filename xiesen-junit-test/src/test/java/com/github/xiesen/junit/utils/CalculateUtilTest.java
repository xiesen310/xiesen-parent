package com.github.xiesen.junit.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculateUtilTest {

    @Test
    public void add() {
        Assert.assertEquals(3, CalculateUtil.add(1, 2));
    }

    @Test
    public void subtract() {
        Assert.assertEquals(1, CalculateUtil.subtract(2, 1));
    }

    @Test
    /**
     *@Description testAdd
     *@Date 2020/10/20 14:34
     */
    public void testAdd() {
    }

    @Test
    /**
     *@Description testSubtract
     *@Date 2020/10/20 14:34
     */
    public void testSubtract() {
    }
}