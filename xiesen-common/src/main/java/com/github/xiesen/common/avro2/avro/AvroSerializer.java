package com.github.xiesen.common.avro2.avro;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.util.Utf8;
import org.apache.commons.collections.MapUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiese
 * @Description 序列化
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:32
 */
public class AvroSerializer {
    public JSONObject jsonObject;
    public JSONArray jsonArray;
    public Schema schema;
    public List<String> filedsArrayList = new ArrayList<String>();

    public AvroSerializer(String schema) {
        getKeysFromjson(schema);
    }

    /**
     * @param schema ：Avro序列化所使用的schema
     * @return void 返回类型
     * @throws
     * @Title: getKeysFromjson
     * @Description:用于获取Avro的keys
     */
    void getKeysFromjson(String schema) {
        this.jsonObject = JSONObject.parseObject(schema);
        this.schema = new Schema.Parser().parse(schema);
        this.jsonArray = this.jsonObject.getJSONArray("fields");
        if (filedsArrayList != null && filedsArrayList.size() > 0) {
            filedsArrayList.clear();
        }
        for (int i = 0; i < this.jsonArray.size(); i++) {
            filedsArrayList.add(this.jsonArray.getJSONObject(i).get("name").toString());
        }
    }

    /**
     * @param
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: serializing
     * @Description: 用于Avro的序列化。
     */
    private synchronized byte[] serializing(List<String> temtuple) {
        byte[] returnstr = null;
        GenericRecord datum = new GenericData.Record(this.schema);
        // 将数据加到datum中
        for (int i = 0; i < filedsArrayList.size(); i++) {
            datum.put(filedsArrayList.get(i), temtuple.get(i));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // DatumWriter 将数据对象翻译成Encoder对象可以理解的类型
        DatumWriter<GenericRecord> write = new GenericDatumWriter<GenericRecord>(this.schema);
        // 然后由Encoder写到数据流。
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);

        try {
            write.write(datum, encoder);
            encoder.flush();

        } catch (IOException e) {
            System.out.println("序列化失败 " + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("序列化失败" + e);
                }
            }
        }
        try {
            returnstr = out.toByteArray();
        } catch (Exception e) {
            System.out.println("序列化失败" + e);
        }
        return returnstr;
    }

    /**
     * 序列化json串
     *
     * @param json
     * @return
     */
    private synchronized byte[] serializing(String json) {
        byte[] returnstr = null;
        JSONObject jsonObject = (JSONObject) JSONObject.parse(json);// new TypeReference<Object>() {}
        GenericRecord datum = new GenericData.Record(this.schema);
        // 将数据加到datum中
        for (int i = 0; i < filedsArrayList.size(); i++) {
            datum.put(filedsArrayList.get(i), new Utf8(String.valueOf(jsonObject.get(filedsArrayList.get(i)))));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // DatumWriter 将数据对象翻译成Encoder对象可以理解的类型
        DatumWriter<GenericRecord> write = new GenericDatumWriter<GenericRecord>(this.schema);
        // 然后由Encoder写到数据流。
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);

        try {
            write.write(datum, encoder);
            encoder.flush();
        } catch (IOException e) {
            System.out.println("序列化失败" + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("序列化失败" + e);
                }
            }
        }
        try {
            returnstr = out.toByteArray();
        } catch (Exception e) {
            System.out.println("序列化失败" + e);
        }
        return returnstr;
    }

    /**
     * 序列化json对象
     *
     * @param jsonObject
     * @return
     */
    private synchronized byte[] serializing(JSONObject jsonObject) {
        byte[] returnstr = null;
        GenericRecord datum = new GenericData.Record(this.schema);
        // 将数据加到datum中
        for (int i = 0; i < filedsArrayList.size(); i++) {
            datum.put(filedsArrayList.get(i), jsonObject.get(filedsArrayList.get(i)));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // DatumWriter 将数据对象翻译成Encoder对象可以理解的类型
        DatumWriter<GenericRecord> write = new GenericDatumWriter<GenericRecord>(this.schema);
        // 然后由Encoder写到数据流。
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);

        try {
            write.write(datum, encoder);
            encoder.flush();
        } catch (IOException e) {
            System.out.println("序列化失败" + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("序列化失败" + e);
                }
            }
        }
        try {
            returnstr = out.toByteArray();
        } catch (Exception e) {
            System.out.println("序列化失败" + e);
        }
        return returnstr;
    }

    /**
     * 序列化对象
     */
    public synchronized byte[] serializing(GenericRecord datum) {
        byte[] returnstr = null;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // DatumWriter 将数据对象翻译成Encoder对象可以理解的类型
        DatumWriter<GenericRecord> write = new GenericDatumWriter<GenericRecord>(this.schema);
        // 然后由Encoder写到数据流。
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);

        try {
            write.write(datum, encoder);
            encoder.flush();
        } catch (IOException e) {
            System.out.println("序列化失败" + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("序列化失败" + e);
                }
            }
        }
        try {
            returnstr = out.toByteArray();
        } catch (Exception e) {
            System.out.println("序列化失败" + e);
        }

        // GenericRecord s = AvroDeserializerFactory.getTopicmetadataDeserializer().deserializing(returnstr);
        return returnstr;
    }

    /**
     * 序列化对象
     */
    public synchronized byte[] serializingLog(String name, Long ts, String source,
                                              Long offset, Map<String, String> dimensions,
                                              Map<String, String> normalFields) {
        GenericRecord datum = new GenericData.Record(this.schema);
        // 将数据加到datum中
        datum.put(0, name);
        datum.put(1, ts);
        datum.put(2, source);
        datum.put(3, offset);
        datum.put(4, dimensions);
        datum.put(5, normalFields);

        return serializing(datum);
    }


    public synchronized byte[] serializingLog(Map<String, Object> data) {
        GenericRecord datum = new GenericData.Record(this.schema);
        final String name = MapUtils.getString(data, "name");
        final Long ts = MapUtils.getLong(data, "ts");
        final String source = MapUtils.getString(data, "source");
        final Long offset = MapUtils.getLong(data, "offset");
        Map<String, String> dimensions = MapUtils.getMap(data, "dimensions");
        Map<String, String> normalFields = MapUtils.getMap(data, "normalFields");

        // 将数据加到datum中
        datum.put(0, name);
        datum.put(1, ts);
        datum.put(2, source);
        datum.put(3, offset);
        datum.put(4, dimensions);
        datum.put(5, normalFields);

        return serializing(datum);
    }


    /**
     * 序列化对象
     */
    public synchronized byte[] serializingMetric(String name, Long ts, Map<String, String> dimensions, Double value) {
        GenericRecord datum = new GenericData.Record(this.schema);
        // 将数据加到datum中
        datum.put(0, name);
        datum.put(1, ts);
        datum.put(2, dimensions);
        datum.put(3, value);

        return serializing(datum);
    }


    /**
     * 序列化对象
     */
    public synchronized byte[] serializingMetric(Map<String, Object> data) {
        GenericRecord datum = new GenericData.Record(this.schema);

        String name = MapUtils.getString(data, "name");
        final Long ts = MapUtils.getLong(data, "ts");
        Map<String, String> dimensions = MapUtils.getMap(data, "dimensions");
        final Double value = MapUtils.getDouble(data, "value");
        // 将数据加到datum中
        datum.put(0, name);
        datum.put(1, ts);
        datum.put(2, dimensions);
        datum.put(3, value);

        return serializing(datum);
    }

    private synchronized byte[] serializing(GenericRecord genericRecord, String key[]) {
        byte[] returnstr = null;
        GenericRecord datum = new GenericData.Record(this.schema);
        // 将数据加到datum中
        for (int i = 0; i < filedsArrayList.size(); i++) {
            datum.put(filedsArrayList.get(i), new Utf8(String.valueOf(genericRecord.get(key[i]))));
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // DatumWriter 将数据对象翻译成Encoder对象可以理解的类型
        DatumWriter<GenericRecord> write = new GenericDatumWriter<GenericRecord>(this.schema);
        // 然后由Encoder写到数据流。
        Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);

        try {
            write.write(datum, encoder);
            encoder.flush();

        } catch (IOException e) {
            System.out.println("序列化失败" + e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("序列化失败" + e);
                }
            }
        }
        try {
            returnstr = out.toByteArray();
        } catch (Exception e) {
            System.out.println("序列化失败" + e);
        }
        return returnstr;
    }
}
