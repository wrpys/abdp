package com.newland.spring.request.log.model;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by garfield on 2018/10/9.
 */
public class CommonLog {


    public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Gson gson = new Gson();

    public static final String LOG_APPLICATION = "qrcodeauth_service";
    public static final String LOG_SYSTEM = "ctid2.0";
    public static final String LOG_MODULE_1 = "generateCTIDCode";
    public static final String LOG_MODULE_2 = "validIDCode";



    public static final String LOG_MODULE_GENERATE = "generate";
    public static final String LOG_MODULE_VALID = "valid";


    public static final String LOG_RESULT_SUCCESS = "0";

    public static final String LOG_TABLE_NAME = "comm_log";







    private String uuid;

    private String system;

    private String application;

    private String module;
    private String server_info;
    private String bsn;
    private String request_str;
    private String decoded_request_str;
    private String exec_start_time;
    private String exec_end_time;
    private Long exec_cost_time;
    private String result;
    private String exception_code;
    private String exception_desc;
    private String third_call_info;
    private Long encdec_cost_time;
    private Long db_cost_time;
    private Long face_cost_time;
    private RemoteResult extend_attrs;

    public String getTableName() {
        return table;
    }

    public void setTableName(String table) {
        this.table = table;
    }

    private String table;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getServer_info() {
        return server_info;
    }

    public void setServer_info(String server_info) {
        this.server_info = server_info;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getRequest_str() {
        return request_str;
    }

    public void setRequest_str(String request_str) {
        this.request_str = request_str;
    }

    public String getDecoded_request_str() {
        return decoded_request_str;
    }

    public void setDecoded_request_str(String decoded_request_str) {
        this.decoded_request_str = decoded_request_str;
    }

    public String getExec_start_time() {
        return exec_start_time;
    }

    public void setExec_start_time(String exec_start_time) {
        this.exec_start_time = exec_start_time;
    }

    public String getExec_end_time() {
        return exec_end_time;
    }

    public void setExec_end_time(String exec_end_time) {
        this.exec_end_time = exec_end_time;
    }

    public Long getExec_cost_time() {
        return exec_cost_time;
    }

    public void setExec_cost_time(Long exec_cost_time) {
        this.exec_cost_time = exec_cost_time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getException_code() {
        return exception_code;
    }

    public void setException_code(String exception_code) {
        this.exception_code = exception_code;
    }

    public String getException_desc() {
        return exception_desc;
    }

    public void setException_desc(String exception_desc) {
        this.exception_desc = exception_desc;
    }

    public String getThird_call_info() {
        return third_call_info;
    }

    public void setThird_call_info(String third_call_info) {
        this.third_call_info = third_call_info;
    }

    public Long getEncdec_cost_time() {
        return encdec_cost_time;
    }

    public void setEncdec_cost_time(Long encdec_cost_time) {
        this.encdec_cost_time = encdec_cost_time;
    }

    public Long getDb_cost_time() {
        return db_cost_time;
    }

    public void setDb_cost_time(Long db_cost_time) {
        this.db_cost_time = db_cost_time;
    }

    public Long getFace_cost_time() {
        return face_cost_time;
    }

    public void setFace_cost_time(Long face_cost_time) {
        this.face_cost_time = face_cost_time;
    }

    public RemoteResult getExtend_attrs() {
        return extend_attrs;
    }

    public void setExtend_attrs(RemoteResult extend_attrs) {
        this.extend_attrs = extend_attrs;
    }



    public void initBaseInfo(){
        this.uuid = UUID.randomUUID().toString().replace("-","");
        this.system = LOG_SYSTEM;
        this.application = LOG_APPLICATION;
        this.table = LOG_TABLE_NAME;
    }
    private static Logger log = LoggerFactory.getLogger(CommonLog.class);

    @Override
    public String toString() {
        return "CommonLog{" +
                "uuid='" + uuid + '\'' +
                ", system='" + system + '\'' +
                ", application='" + application + '\'' +
                ", module='" + module + '\'' +
                ", server_info='" + server_info + '\'' +
                ", bsn='" + bsn + '\'' +
                ", request_str='" + request_str + '\'' +
                ", decoded_request_str='" + decoded_request_str + '\'' +
                ", exec_start_time='" + exec_start_time + '\'' +
                ", exec_end_time='" + exec_end_time + '\'' +
                ", exec_cost_time=" + exec_cost_time +
                ", result='" + result + '\'' +
                ", exception_code='" + exception_code + '\'' +
                ", exception_desc='" + exception_desc + '\'' +
                ", third_call_info='" + third_call_info + '\'' +
                ", encdec_cost_time=" + encdec_cost_time +
                ", db_cost_time=" + db_cost_time +
                ", face_cost_time=" + face_cost_time +
                ", extend_attrs=" + extend_attrs +
                ", table='" + table + '\'' +
                '}';
    }

    public void initResultInfoPre(CommonLog commonLog){
        RemoteResult result = commonLog.getExtend_attrs();
        String url = result.getUrl();
        log.info(url + LOG_MODULE_GENERATE + ":" + String.valueOf(url.contains(LOG_MODULE_GENERATE)));
        if(url.contains(LOG_MODULE_GENERATE)){
            this.module = LOG_MODULE_1;
        }else if(url.contains(LOG_MODULE_VALID)){
            this.module = LOG_MODULE_2;
        }
        this.bsn = result.getBusinessSeq();
        this.server_info = result.getRemoteAddr();
        this.request_str = result.getArgs();
        this.exec_start_time = result.getBeginTime();
    }

    public void initResultInfoPost(CommonLog commonLog){
        RemoteResult result1 = commonLog.getExtend_attrs();
        this.exec_end_time = result1.getEndTime();
        this.exec_cost_time = (long) result1.getTotalTime();

        if(!LOG_RESULT_SUCCESS.equals(result1.getResultCode())){
            this.exception_code = result1.getResultCode();
            this.exception_desc = result1.getResultContent();
        }
    }

    public void cloneFromCmLog(CmLog cmLog){
        this.initBaseInfo();
        String url = cmLog.getRequestUrl();
        if(url.contains(LOG_MODULE_GENERATE)){
            this.module = LOG_MODULE_1;
        }else if(url.contains(LOG_MODULE_VALID)){
            this.module = LOG_MODULE_2;
        }
        this.bsn = cmLog.getTraceId();
        this.server_info = cmLog.getRemoteAddr();
        this.request_str = cmLog.getContent();
        this.exec_start_time = cmLog.getStartTime();
        this.exec_end_time = cmLog.getEndTime();
        this.exec_cost_time = cmLog.getCostTime();

        if(!LOG_RESULT_SUCCESS.equals(cmLog.getResultCode())){
            this.exception_code = cmLog.getResultCode();
            this.exception_desc = cmLog.getResultDesc();
        }
    }

    public static String getFormatTime(){
        return format.format(new Date()) + "000";
    }

    public static void main(String[] args) {
        System.out.println("lb://Ac-Plat:generateCTIDCode".contains(LOG_MODULE_GENERATE));
        }

}
