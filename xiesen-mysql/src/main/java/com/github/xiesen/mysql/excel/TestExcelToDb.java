package com.github.xiesen.mysql.excel;

import com.github.xiesen.mysql.util.Db;

public class TestExcelToDb {

    public static void main(String[] args) throws Exception {
        Db db = new Db();
//        db.psBatchBizData();
        db.psBatchCmdb();
    }
}
