package com.github.xiesen.mock.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HomeDataParse {


    public static void main(String[] args) {
        String filePath = "/Users/xiesen/Downloads/TXT_20240918_101322.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseLine(String line) {
        String[] fields = line.split("\\s+"); // 使用空格或制表符分割
        if (fields.length >= 10) { // 确保有足够的字段
            String district = fields[0];
            String transactionType = fields[1];
            String communityName = fields[2];
            String area = fields[3];
            int listingPrice = Integer.parseInt(fields[4].replaceAll("[^\\d]", ""));
            int transactionPrice = Integer.parseInt(fields[5].replaceAll("[^\\d]", ""));
            int totalTransactionPrice = Integer.parseInt(fields[6].replaceAll("[^\\d]", ""));
            int numberOfUnits = Integer.parseInt(fields[7].replaceAll("[^\\d]", ""));

            System.out.printf("District: %s, Transaction Type: %s, Community Name: %s, Area: %s, Listing Price: %d, Transaction Price: %d, Total Transaction Price: %d, Number of Units: %d%n",
                    district, transactionType, communityName, area, listingPrice, transactionPrice, totalTransactionPrice, numberOfUnits);
        }
    }

}
