package com.github.xiesen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xiesen.utils.GtjaqljUtil;
import com.gtja.apiajax.thrift.SLinkOAService;
import com.gtja.sg.remote.thrift.support.RemoteProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.modified.Response;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class Main {
    public static final String ssoKey = "";
    public static final String ssoIv = "";
    // 调用系统，使用事务中心文种汇总中的系统缩写
    public static final String source = "";

    // todo  oa服务在服务治理平台注册服务名为:SLinkOAService
    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        String appId = "capacity";
        String fileNo = GtjaqljUtil.generateFormattedDate() + "-000001";
        String todoNo = UUID.randomUUID().toString().replace("-", "");
        String todoId = appId + "_" + GtjaqljUtil.generateFormattedDate() + "_" + todoNo;
        String userIds = "pengtao026241";
        String submitter = "pengtao026241";
        String todoUrl = "http://localhost:8080/capacity";
        String todoTitle = "您的系统【锐智网上交易系统】在容量工作台上存在【指标设置】类型的未处理事件,请尽快登录容量平台进行查看和处理";
        String fileClass = "0";
        String instancyLv = "0";
        String secrecyLv = "0";
        String deptName = "数据中心-综合运维管理组";

        addTodo("0", todoId, userIds, fileNo, todoNo, submitter, todoUrl, todoTitle, fileClass, instancyLv, secrecyLv, deptName);
    }

    /**
     * 添加待办
     *
     * @param todoFlag   待办类型: 待办/待阅/草稿/我创建的标识，0：代表待办；1：代表待阅；2：代表草稿；3：代表我创建的。
     * @param todoId     待办ID: 待办或待阅的唯一标识，由调用者产生。标识的规则为系统标识_YYYYMM_
     * @param userIds    待办接收人. 待办所属的人员数组或已办的处理人员，人员为登录名（短名），非姓名。todo userIDs 是否可以填写多个人
     * @param fileNo     文号
     * @param todoNo     待办的主表单编号，todoNo 相同的草稿、我创建的、待办、待阅、已办都是针对同一个审批表单的。
     * @param submitter  待办的提交人，取登录名（短名），非姓名。
     * @param todoUrl    待办/已办/草稿/我创建的 URL。通过该 URL 可以打开待办/已办/草稿/我创建的。最大长度为 200 个字符。
     * @param todoTitle  待办/已办/草稿/我创建的标题，最大长度为 500 个字符
     * @param fileClass  文种类型
     * @param instancyLv 紧急程度，"0"为"一般"，"1"为"紧急", "2"为"特急"
     * @param secrecyLv  保密程度，"0"为"一般"，"1"为"秘密"。
     * @param deptName   起草部门名称
     * @return
     * @throws Exception
     */
    public static void addTodo(String todoFlag, String todoId, String userIds, String fileNo, String todoNo, String submitter, String todoUrl, String todoTitle, String fileClass, String instancyLv, String secrecyLv, String deptName) {
        JSONObject jo = new JSONObject();
        jo.put("todoId", todoId);
        jo.put("userIds", userIds);
        jo.put("fileNo", fileNo);
        jo.put("todoNo", todoNo);
        jo.put("submitter", submitter);
        jo.put("todoUrl", todoUrl);
        jo.put("todoTitle", todoTitle);
        jo.put("genDate", GtjaqljUtil.getFormattedCurrentDateTime());
        jo.put("fileClass", fileClass);
        jo.put("todoflag", todoFlag);
        /**
         * 代表要执行的操作。该元素的具体值及其含义根据 todoflag 实体参数值而不同
         * todoflag 0：代表待办；1：代表待阅；2：代表草稿；3：代表我创建的。
         *      Todoflag 为 0、1 时（待办待阅）， operatetype  的值可以为 addtodo（创建待办）、
         *           modifytodo（处理待办,删除待办，status=0表示删除待办，1表示处理待办）
         *      Todoflag 为 2 时（我的草稿处理），operatetype 的值可以为 1（创建）、2（更新）、3（删除）、4（草稿转我创建的）
         *      Todoflag 为 3 时（我创建的处理），operatetype 的值可以为 1（创建）、2（更新）、3（删除）
         *      创建代办: operatetype = addtodo
         */

        jo.put("operatetype", "addtodo");
        jo.put("instancyLv", instancyLv);
        jo.put("secrecyLv", secrecyLv);
        jo.put("deptName", deptName);
        JSONArray ja = new JSONArray();
        ja.add(jo);

        // 发送待办
        sendTodo(ja);
    }

    public static void sendTodo(JSONArray jaToSend) {
        try {
            JSONObject dataparam = new JSONObject();
            dataparam.put("errorhandletype", "1");
            dataparam.put("applys", jaToSend);
            log.info("发送待办参数: {}", dataparam.toJSONString());
            String encStr = GtjaqljUtil.encode3DES(JSON.toJSONStringWithDateFormat(dataparam, "yyyy-MM-dd HH:mm:ss"), ssoKey, ssoIv);
            log.info("encStr: {}", encStr);

            Map<String, String> request = new HashMap<>();
            request.put("encStr", encStr);
            SLinkOAService.Iface f = (SLinkOAService.Iface) RemoteProxy.getClient(SLinkOAService.class);
            Response rsp = f.fApiCall("OA_AGENCY_S0001", source, request);
            if (rsp == null) {
                log.error("call SLinkOAService.fApiCall error, response is null.");
            }
            if (rsp.isSuccess() == false) {
                log.error("call SLinkOAService.fApiCall error, {}", rsp.getErrMsg());
            }
            JSONObject.parseObject(rsp.getResult());

        } catch (Exception e) {
            log.error("sendTodo 发送待办失败 ", e);
        }

    }

}