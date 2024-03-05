package com.github.xiesen.mock.test;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.mapred.FsInput;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class ReadAvroFromHDFSWithCompression {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://cdh-2:8020");
            conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
            FileSystem fs = FileSystem.get(conf);
            Path inputPath = new Path("hdfs://cdh-2:8020/xiesen/datawarehouse/test/20240301/default_analysis_template/zhlc/part-0-0.avro");
            InputStream is = fs.open(inputPath);
            FsInput fsInput = new FsInput(inputPath, conf);
            FileInputStream fis = new FileInputStream("path_to_your_deflated_file");
            InflaterInputStream inflaterInputStream = new InflaterInputStream(fis);
            SeekableInput input = new FsInput(inputPath, conf);
            DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>();
            DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>( input, datumReader);

            while (dataFileReader.hasNext()) {
                GenericRecord record = dataFileReader.next();
                System.out.println(record);
            }

            dataFileReader.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
