package com.github.xiesen.common.test;


public class AverageCalculator {
    public static double calculateAverage(long[] data) {
        int n = data.length;
        double[] differences = new double[n - 1];
        for (int i = 1; i < n; i++) {
            // 计算相邻元素之间的差值
            differences[i - 1] = data[i] - data[i - 1];
        }
        double sum = 0.0;
        for (double difference : differences) {
            // 计算差值的总和
            sum += difference;
        }

        // 计算差值的平均值
        double averageDifference = sum / (n - 1);
        // 针对结果向上取整后求绝对值
        return Math.abs(Math.ceil(averageDifference));
    }

    public static void main(String[] args) {
        long[] data = {1701164921460L, 1701164911460L, 1701164901460L, 1701164891460L, 1701164881460L};

        double result = calculateAverage(data);
        System.out.println("平均值: " + result + " ms");
    }
}
