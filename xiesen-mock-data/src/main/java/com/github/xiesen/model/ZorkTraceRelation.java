package com.github.xiesen.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author：hubinbin
 * @date：2022/6/16
 */
@Data
@Accessors(chain = true)
public class ZorkTraceRelation extends ZorkLogStruct implements Serializable {

    private RelationDimensions dimensions;
    private RelationNormalFields normalFields;
    private RelationMeasures measures;

    @Data
    public static class RelationDimensions implements Dimensions {
        /**
         * 0 正确; 1 错误
         */
        private int status;
        private String sourceendpointname;
        private String sourceservicename;
        private String sourceserviceinstancename;
        private String sourceip;

        private String destendpointname;
        private String destservicename;
        private String destserviceinstancename;
        private String destip;
    }

    @Data
    public static class RelationNormalFields implements NormalFields {
        private String traceid;
        private String code;
    }

    @Data
    public static class RelationMeasures implements Measures {
        private Long duration;
    }
}
