package com.github.xiesen.mock.ops;

import lombok.Getter;

import java.util.Objects;

/**
 * @author xiesen
 */
public interface KcbpFuncIdConstant {

    enum FuncEnum {
        FUNC_88853536(88853536, "财富账户资金调拨流水查询"),
        FUNC_90002000(90002000, "申万宏源另库恢复接口"),
        FUNC_90002001(90002001, "申万宏源融资行权接口"),
        FUNC_90002002(90002002, "SWHY客户可用股份查询"),
        FUNC_90002003(90002003, "SWHY全国股转证券转托管");
        @Getter
        private Long funcId;
        @Getter
        private String funcName;

        FuncEnum(long funcId, String funcName) {
            this.funcId = funcId;
            this.funcName = funcName;
        }


        public static String valueOf(Long funcId) {
            if (funcId == null) return "";
            for (FuncEnum func : FuncEnum.values()) {
                if (Objects.equals(func.getFuncId(), funcId)) {
                    return func.getFuncName();
                }
            }
            return "";
        }
    }

}
