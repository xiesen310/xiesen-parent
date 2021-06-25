package com.github.xiesen.hashmap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 谢森
 * @since 2021/6/16
 */
public class HashMapTest4 {

    private static ThreadLocal<SimpleDateFormat> msgSdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }
    };


    /**
     * 2021 06 17 10 28 54 842
     *
     * @param ts
     * @return
     * @throws ParseException
     */
    private static Long date2ts(String ts) throws ParseException {
        return msgSdf.get().parse(ts).getTime();
    }


    public static final Map<Integer, BdDataRelationShip> map = new HashMap(16);

    static {
        for (int i = 0; i < 100; i++) {
            BdDataRelationShip relationShip = new BdDataRelationShip(i, "aa", 1, "bb", "bizName", "ModuleName",
                    "datatName", "saa", "topic", "atopic",
                    "index", "groupid", "agroupid", "status", 1, "YYYYMM", 10);
            map.put(i, relationShip);
        }
    }

    public static void main(String[] args) throws ParseException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            BdDataRelationShip relationShip = map.get(1);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start));

    }
}
