package com.github.xiesen.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeValidationRule {
    /**
     * 唯一标识
     */
    private String ruleId;
    /**
     * 属性名称 (字段名)
     */
    private String attributeName;
    /**
     * 属性类型,String,Double,Integer
     */
    private String attributeType = "String";
    /**
     * 属性值 (字段值)
     */
    private String attributeRuleType;
}
