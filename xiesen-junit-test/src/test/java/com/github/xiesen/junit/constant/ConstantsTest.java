package com.github.xiesen.junit.constant;

import org.junit.Assert;
import org.junit.Test;

import static com.github.xiesen.junit.constant.Constants.CAPACITY_CONTROLLER_PATH;
import static com.github.xiesen.junit.constant.Constants.COMMUNICATION_CONTROLLER_PATH;

public class ConstantsTest {

    @Test
    public void testDefaultValues() {

        Assert.assertEquals("/v1/cs/capacity", CAPACITY_CONTROLLER_PATH);
        Assert.assertEquals("/v1/cs/communication", COMMUNICATION_CONTROLLER_PATH);
    }

}