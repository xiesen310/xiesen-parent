package com.github.xiesen.flink.test;

import org.apache.flink.api.common.io.RichInputFormat;
import org.apache.flink.api.common.io.statistics.BaseStatistics;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.io.InputSplit;
import org.apache.flink.core.io.InputSplitAssigner;

import java.io.IOException;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/9/29 10:50
 */
public class CustomInputFormat extends RichInputFormat<byte[], InputSplit> {
    @Override
    public void configure(Configuration parameters) {

    }

    @Override
    public BaseStatistics getStatistics(BaseStatistics cachedStatistics) throws IOException {
        return null;
    }

    @Override
    public InputSplit[] createInputSplits(int minNumSplits) throws IOException {
        return new InputSplit[0];
    }

    @Override
    public InputSplitAssigner getInputSplitAssigner(InputSplit[] inputSplits) {
        return null;
    }

    @Override
    public void open(InputSplit split) throws IOException {

    }

    @Override
    public boolean reachedEnd() throws IOException {
        return false;
    }

    @Override
    public byte[] nextRecord(byte[] reuse) throws IOException {
        return new byte[0];
    }

    @Override
    public void close() throws IOException {

    }
}
