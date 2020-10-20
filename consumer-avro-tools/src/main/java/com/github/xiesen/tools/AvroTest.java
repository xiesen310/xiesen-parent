package com.github.xiesen.tools;

import com.github.xiesen.common.avro.AvroDeserializer;
import com.github.xiesen.common.avro.AvroDeserializerFactory;
import com.github.xiesen.common.avro.LogAvroMacroDef;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import java.io.*;

/**
 * @author xiese
 * @Description TODO
 * @Email xiesen310@163.com
 * @Date 2020/9/29 9:54
 */
public class AvroTest {
    public static void main(String[] args) throws IOException {
        Schema schema = new Schema.Parser().parse(LogAvroMacroDef.metadata);
        File file = new File("D:\\tmp\\tmp\\part-0-1.avro");
        DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader);
        GenericRecord dept = null;
        while (dataFileReader.hasNext()) {
            dept = dataFileReader.next(dept);
            System.out.println(dept);
        }
    }


    private static void test01() throws IOException {
        Schema schema = new Schema.Parser().parse(new File("D:\\tmp\\tmp\\ZorkAvroLogData.txt"));
        File file = new File("D:\\tmp\\tmp\\part-0-1.avro");
        DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader);
        GenericRecord dept = null;
        while (dataFileReader.hasNext()) {
            dept = dataFileReader.next(dept);
            System.out.println(dept);
        }
    }
}
