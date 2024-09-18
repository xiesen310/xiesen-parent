package com.github.xiesen.common.utils;

import cn.hutool.core.map.MapUtil;
import com.github.xiesen.common.model.AttributeRuleType;
import com.github.xiesen.common.model.AttributeValidationRule;
import org.apache.commons.collections.MapUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DataQualityAttributeValidation {
    private static List<AttributeValidationRule> rules = new ArrayList<>();

    static {
//        rules.add(AttributeValidationRule.builder().ruleId("test-rule-001").attributeName("email").attributeType("String").attributeRuleType(AttributeRuleType.EMAIL.name()).build());
//        rules.add(AttributeValidationRule.builder().ruleId("test-rule-002").attributeName("birthDate").attributeType("String").attributeRuleType(AttributeRuleType.DATE.name()).build());
//        rules.add(AttributeValidationRule.builder().ruleId("test-rule-003").attributeName("phoneNumber").attributeType("String").attributeRuleType(AttributeRuleType.PHONE_NUMBER.name()).build());
        rules.add(AttributeValidationRule.builder().ruleId("test-rule-004").attributeName("normalFields.ip").attributeType("String").attributeRuleType(AttributeRuleType.IP_ADDRESS.name()).build());
    }

    public static void main(String[] args) {
        List<Map<String, Object>> list = MockDataUtil.mockLogDataList("hzy_log", 100);
        for (Map<String, Object> map : list) {
            for (AttributeValidationRule rule : rules) {
                String attributeType = rule.getAttributeType();
                String attributeName = rule.getAttributeName();
                if (attributeName.contains(".")) {
                    String[] attrs = attributeName.split("\\.");
                    Map innerMap = MapUtils.getMap(map, attrs[0]);
                    Object attrValue = null;
                    if (innerMap != null) {
                        if (attributeType.equals("String")) {
                            attrValue = MapUtil.getStr(innerMap, attrs[1]);
                        } else if (attributeType.equals("Double")) {
                            attrValue = MapUtil.getDouble(innerMap, attrs[1]);
                        }
                    }

                    if (attrValue != null) {
                        String ruleType = rule.getAttributeRuleType();
                        if (ruleType.equals(AttributeRuleType.EMAIL.name())) {
                            if (!isValidEmail(attrValue.toString())) {
                                System.out.println("Invalid email: " + attrValue);
                            }
                        } else if (ruleType.equals(AttributeRuleType.PHONE_NUMBER.name())) {
                            if (!isValidPhoneNumber(attrValue.toString())) {
                                System.out.println("Invalid phone number: " + attrValue);
                            }
                        } else {
                            System.out.println("Invalid rule type: " + ruleType);
                        }

                    }
                }
            }
        }
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+?[1-9]\\d{1,14}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(phoneNumber).matches();
    }

    private static boolean isValidBirthDate(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(birthDate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isValidIpAddress(String ipAddress) {
        String regex = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(ipAddress).matches();
    }

}
