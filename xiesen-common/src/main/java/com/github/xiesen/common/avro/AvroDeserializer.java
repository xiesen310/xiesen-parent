package com.github.xiesen.common.avro;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiese
 * @Description 反序列化
 * @Email xiesen310@163.com
 * @Date 2020/6/28 9:31
 */
public class AvroDeserializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroDeserializer.class);
    public JSONObject jsonObject;
    public JSONArray jsonArray;
    public Schema schema;
    public String[] keys;

    public AvroDeserializer(String schema) {
        getKeysFromjson(schema);
    }

    /**
     * @param schema：Avro序列化所使用的schema
     * @return void    返回类型
     * @throws
     * @Title: getKeysFromjson
     * @Description:用于获取Avro的keys
     */
    void getKeysFromjson(String schema) {
        this.jsonObject = JSONObject.parseObject(schema);
        this.schema = new Schema.Parser().parse(schema);
        this.jsonArray = this.jsonObject.getJSONArray("fields");
        this.keys = new String[this.jsonArray.size()];
        for (int i = 0; i < this.jsonArray.size(); i++) {
            this.keys[i] = this.jsonArray.getJSONObject(i).get("name").toString();
        }
    }

    /**
     * @param body    参数：byte[] body：kafka消息。
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: deserializing
     * @Description: 用于Avro的反序列化。
     */

    public GenericRecord deserializing(byte[] body) {
        DatumReader<GenericData.Record> datumReader = new GenericDatumReader<GenericData.Record>(this.schema);
        Decoder decoder = DecoderFactory.get().binaryDecoder(body, null);
        GenericData.Record result = null;
        try {
            result = datumReader.read(null, decoder);
        } catch (Exception e) {
            LOGGER.error(String.format("error Avro反序列化"), e);
        }
        return result;
    }
}
