package com.github.xiesen.hashmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/3/23
 */
public class HashMapTest2 {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        System.out.println("更新前 ====================== ");
        map.forEach((k, v) -> {
            System.out.println("k = " + k + " , v = " + v);
        });

        Map<String, Object> map1 = updateMap(map);
        System.out.println("更新后 ====================== ");
        map.forEach((k, v) -> {
            System.out.println("k = " + k + " , v = " + v);
        });

        map1.forEach((k, v) -> {
            System.out.println("k = " + k + " , v = " + v);
        });
    }

    private static Map<String, Object> updateMap(Map<String, Object> map) {
        Map<String, Object> result = clone(map);
        result.put("2", 22);
        return result;
    }

    /**
     * 深拷贝
     *
     * @param obj
     * @return
     */
    public static HashMap<String, Object> clone(Map<String, Object> obj) {
        HashMap<String, Object> clonedObj = null;
        if (obj.isEmpty()) {
            clonedObj = new HashMap<>(50);
        } else {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                oos.close();

                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                clonedObj = (HashMap<String, Object>) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return clonedObj;
    }
}
