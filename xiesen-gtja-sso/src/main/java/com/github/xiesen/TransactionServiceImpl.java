//package com.github.xiesen;
//
//import java.security.SecureRandom;
//import java.security.Security;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESedeKeySpec;
//import javax.crypto.spec.IvParameterSpec;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.thrift.modified.Response;
//import org.omg.IOP.TransactionService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.gtja.apiajax.thrift.SLinkOAService;
//import com.gtja.framework.core.model.ResponseInfo;
//import com.gtja.maintain.bean.TransactionBean;
//import com.gtja.maintain.service.TransactionService;
//import com.gtja.sg.remote.thrift.support.RemoteProxy;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
//
//@Slf4j
//public class TransactionServiceImpl implements TransactionService {
//    // todo strKey和strlv对应各系统SSO分配的加密参数,如何获取
//    private String ssoKey;
//    private String ssoIv;
//
//    public TransactionServiceImpl() {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//    }
//
//    /**
//     * 添加待办
//     *
//     * @param todoflag   待办类型: 待办/待阅/草稿/我创建的标识，0：代表待办；1：代表待阅；2：代表草稿；3：代表我创建的。
//     * @param todoId     待办ID: 待办或待阅的唯一标识，由调用者产生。标识的规则为系统标识_YYYYMM_
//     * @param userIds    待办接收人. 待办所属的人员数组或已办的处理人员，人员为登录名（短名），非姓名。todo userIDs 是否可以填写多个人
//     * @param fileNo     文号
//     * @param todoNo     待办的主表单编号，todoNo 相同的草稿、我创建的、待办、待阅、已办都是针对同一个审批表单的。
//     * @param submitter  待办的提交人，取登录名（短名），非姓名。
//     * @param todoUrl    待办/已办/草稿/我创建的 URL。通过该 URL 可以打开待办/已办/草稿/我创建的。最大长度为 200 个字符。
//     * @param todoTitle  待办/已办/草稿/我创建的标题，最大长度为 500 个字符
//     * @param fileClass  文种类型
//     * @param instancyLv 紧急程度，"0"为"一般"，"1"为"紧急", "2"为"特急"
//     * @param secrecyLv  保密程度，"0"为"一般"，"1"为"秘密"。
//     * @param deptName   起草部门名称
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public ResponseInfo addTodo(String todoflag, String todoId, String userIds, String fileNo,
//                                String todoNo, String submitter, String todoUrl, String todoTitle,
//                                String fileClass, String instancyLv, String secrecyLv,
//                                String deptName) throws Exception {
//        JSONObject jo = new JSONObject();
//        jo.put("todoId", todoId);
//        jo.put("userIds", userIds);
//        jo.put("fileNo", fileNo);
//        jo.put("todoNo", todoNo);
//        jo.put("submitter", submitter);
//        jo.put("todoUrl", todoUrl);
//        jo.put("todoTitle", todoTitle);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        jo.put("genDate", sdf.format(new Date()));
//        jo.put("fileClass", fileClass);
//        jo.put("todoflag", todoflag);
//        /**
//         * 代表要执行的操作。该元素的具体值及其含义根据 todoflag 实体参数值而不同
//         * todoflag 0：代表待办；1：代表待阅；2：代表草稿；3：代表我创建的。
//         *      Todoflag 为 0、1 时（待办待阅）， operatetype  的值可以为 addtodo（创建待办）、
//         *           modifytodo（处理待办,删除待办，status=0表示删除待办，1表示处理待办）
//         *      Todoflag 为 2 时（我的草稿处理），operatetype 的值可以为 1（创建）、2（更新）、3（删除）、4（草稿转我创建的）
//         *      Todoflag 为 3 时（我创建的处理），operatetype 的值可以为 1（创建）、2（更新）、3（删除）
//         *      创建代办: operatetype = addtodo
//         */
//
//        jo.put("operatetype", "addtodo");
//        jo.put("instancyLv", instancyLv);
//        jo.put("secrecyLv", secrecyLv);
//        jo.put("deptName", deptName);
//        JSONArray ja = new JSONArray();
//        ja.add(jo);
//        return sendTrans(ja);
//    }
//
//    @Override
//    public ResponseInfo modifyTodo(String todoId, String userIds, String todoUrl,
//                                   String todoTitle) throws Exception {
//        JSONObject jo = new JSONObject();
//        jo.put("todoId", todoId);
//        jo.put("userIds", userIds);
//        jo.put("todoUrl", todoUrl);
//        jo.put("todoTitle", todoTitle);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        jo.put("processDate", sdf.format(new Date()));
//        jo.put("status", "1");
//        jo.put("operatetype", "modifytodo");
//        jo.put("todoflag", "0");
//        JSONArray ja = new JSONArray();
//        ja.add(jo);
//        return sendTrans(ja);
//    }
//
//    @Override
//    public ResponseInfo deleteTodo(String todoId, String userId) throws Exception {
//        JSONObject jo = new JSONObject();
//        jo.put("todoId", todoId);
//        jo.put("status", "0");
//        jo.put("userId", userId);
//        jo.put("operatetype", "modifytodo");
//        JSONArray ja = new JSONArray();
//        ja.add(jo);
//        return sendTrans(ja);
//    }
//
//    @Override
//    public ResponseInfo deleteComplete(String todoId, String userId) throws Exception {
//        JSONObject jo = new JSONObject();
//        jo.put("todoId", todoId);
//        jo.put("status", "0");
//        jo.put("userId", userId);
//        jo.put("operatetype", "delYb");
//        JSONArray ja = new JSONArray();
//        ja.add(jo);
//        return sendTrans(ja);
//    }
//
//    @Override
//    public ResponseInfo deleteMyCreate(String todoId, String userId) throws Exception {
//        JSONObject jo = new JSONObject();
//        jo.put("todoId", todoId);
//        jo.put("userId", userId);
//        jo.put("operatetype", "3");
//        jo.put("todoflag", "3");
//        JSONArray ja = new JSONArray();
//        ja.add(jo);
//        return sendTrans(ja);
//    }
//
//    @Override
//    public ResponseInfo deleteDraft(String todoId, String userId) throws Exception {
//        JSONObject jo = new JSONObject();
//        jo.put("todoId", todoId);
//        jo.put("userId", userId);
//        jo.put("operatetype", "3");
//        jo.put("todoflag", "2");
//        JSONArray ja = new JSONArray();
//        ja.add(jo);
//        return sendTrans(ja);
//    }
//
//    @Override
//    public ResponseInfo call(List<TransactionBean> beans) throws Exception {
//        return null;
//    }
//
//    public ResponseInfo sendTrans(JSONArray jaToSend) {
//        JSONObject dataparam = new JSONObject();
//        dataparam.put("errorhandletype", "1");
//        dataparam.put("applys", jaToSend);
//        log.info("推送事务中心，参数：" + dataparam.toJSONString());
//        return sendTransaction(encode3DES(
//                JSON.toJSONStringWithDateFormat(dataparam, "yyyy-MM-dd HH:mm:ss"), ssoKey, ssoIv));
//    }
//
//    public ResponseInfo sendTransaction(String encStr) {
//        ResponseInfo responseInfo = new ResponseInfo();
//        try {
//            Map<String, String> request = new HashMap<>();
//            request.put("encStr", encStr);
//            log.info("encStr:" + encStr);
//            SLinkOAService.Iface f = (SLinkOAService.Iface) RemoteProxy
//                    .getClient(SLinkOAService.class);
//            Response rsp = f.fApiCall("OA_AGENCY_S0001", "link", request);
//            if (rsp == null) {
//                responseInfo.setState(1);
//                responseInfo.setErrMsg("发送待办失败");
//                log.error("call SLinkOAService.fApiCall error");
//                return responseInfo;
//            }
//            if (rsp.isSuccess() == false) {
//                responseInfo.setState(1);
//                responseInfo.setErrMsg(rsp.getErrMsg());
//                log.error("call SLinkOAService.fApiCall error, " + responseInfo.getErrMsg());
//                return responseInfo;
//            }
//            responseInfo = JSONObject.parseObject(rsp.getResult(), ResponseInfo.class);
//            if (responseInfo == null) {
//                responseInfo = new ResponseInfo("发送待办错误");
//            }
//            return responseInfo;
//        } catch (Exception e) {
//            log.error("sendTransaction", e);
//            responseInfo.setState(1);
//            responseInfo.setErrMsg("发送待办失败");
//            return responseInfo;
//        }
//    }
//
//    private String encode3DES(String strEncode, String strKey, String StrIv) {
//        String result = null;
//        try {
//            BASE64Decoder base64decoder = new BASE64Decoder();
//            byte[] key = base64decoder.decodeBuffer(strKey);
//            byte[] iv = base64decoder.decodeBuffer(StrIv);
//            result = encryptWithIV(key, iv, strEncode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private String encryptWithIV(byte[] key, byte[] iv, String str) throws BadPaddingException,
//            Exception {
//        SecureRandom sr = new SecureRandom();
//        DESedeKeySpec dks = new DESedeKeySpec(key);
//        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
//        SecretKey securekey = keyFactory.generateSecret(dks);
//        IvParameterSpec ips = new IvParameterSpec(iv);
//        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS7Padding");
//        cipher.init(1, securekey, ips, sr);
//        byte[] bt = cipher.doFinal(str.getBytes("UTF-8"));
//        return new String(new BASE64Encoder().encode(bt));
//    }
//}