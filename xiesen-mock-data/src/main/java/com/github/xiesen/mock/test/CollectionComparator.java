package com.github.xiesen.mock.test;

import java.util.*;

public class CollectionComparator {

    /**
     * 比较两个集合是否包含相同的元素，并且每个元素的数量也相同。
     *
     * @param collection1 第一个集合
     * @param collection2 第二个集合
     * @return 如果两个集合的数据完全相同，则返回true；否则返回false。
     */
    public static boolean areCollectionsEqual(Collection<?> collection1, Collection<?> collection2) {
        if (collection1 == null || collection2 == null) {
            return collection1 == collection2; // 如果两者都是null，则认为相等
        }

        if (collection1.size() != collection2.size()) {
            return false; // 如果两个集合的大小不同，则直接返回false
        }

        Map<Object, Integer> frequencyMap = new HashMap<>();

        // 统计第一个集合中每个元素的出现频率
        for (Object item : collection1) {
            frequencyMap.merge(item, 1, Integer::sum);
        }

        // 减去第二个集合中每个元素的出现频率
        for (Object item : collection2) {
            Integer count = frequencyMap.get(item);
            if (count == null || count == 0) {
                return false; // 如果元素不存在或已被减到0，则返回false
            } else {
                frequencyMap.put(item, count - 1);
            }
        }

        // 如果所有元素都匹配并且数量一致，则frequencyMap中的所有值都应该为0
        return frequencyMap.values().stream().allMatch(count -> count == 0);
    }

    public static void main(String[] args) {
        // 测试用例
        List<String> topics = Arrays.asList("a","b");
        Set<String> statusTopic = new HashSet<>();
        statusTopic.add("a");
        statusTopic.add("c");
        System.out.println(areCollectionsEqual(topics, statusTopic)); // true
    }
}