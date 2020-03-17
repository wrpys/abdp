package com.newland.spring.request.log.kafka;

import com.newland.spring.platcore.utils.JsonUtils;
import com.newland.spring.request.log.CustomThreadPoolExecutor;
import com.newland.spring.request.log.ResponseThreadLocal;
import com.newland.spring.request.log.StartInit;
import com.newland.spring.request.log.model.CmLog;
import com.newland.spring.request.log.model.logex.MgrLog;
import com.newland.spring.request.model.result.CommonCodeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;


/**
 * Created by garfield on 2018/1/23.
 */
@Component
public class KafkaLog {

    private final Logger log = LoggerFactory.getLogger(KafkaLog.class);


    /**
     * if logTopic variable is not existed,
     * then kafka log will be sent to the topic by the name of services.
     */
    @Value("${spring.application.name}")
    private String serviceName;


    @Value("${log.topic}")
    private String logTopic;

    @Value("${log.topic.custom: #{null}}")
    private String logTopicCustom;

    @Value("${log.thread:15}")
    private int threadNum;

    @Value("${log.thread.async:true}")
    private boolean asyncMode;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    CustomThreadPoolExecutor execute;

    @Autowired
    ThreadPoolExecutor pool;

    ForkJoinPool forkJoinPool;

    int i = 0;

    @PostConstruct
    public void init(){
        forkJoinPool = new ForkJoinPool(threadNum, defaultForkJoinWorkerThreadFactory, null, asyncMode);
    }
    //发送消息方法
    private void send(String topic, CmLog cmLog) {
        if (StartInit.LOG_KAFKA) {
            String finalTopic = topic;
            if (StringUtils.isEmpty(cmLog.getServiceName()))
                cmLog.setServiceName(serviceName);
            CmLog finalCmLog = cmLog.clone();
            /**
             * 23700-8000
             *
             * if core thread pool size is set between 600-1000
             * max thread pool size is set between 1000-2000
             * queue size is 1000
             * tps:16000
             * which more than the situation(100/200/100) and situation(1500/2000/1000)
             *
             * and another ex:
             * if use ThreadPoolExecutor bean, tps is limited by 10000
             *
             * continue optimizing params, it seems queue size that core factor affect tps
             * 10000 is fine
             */
//            execute.getCustomThreadPoolExecutor().execute(() -> {
//                log.info("send to topic:" + finalTopic + " :" + finalCmLog.toString());
////                kafkaTemplate.send(finalTopic, "hello", JsonUtils.getString(finalCmLog));
//            });
            /**
             * continue optimizing performance
             * big thing!!!
             * ForkJoinPool.commonPool() is fast than execute pool(??)
             *
             * question:
             *
             * 16000 -> 20000
             *
             *
             * coz the common pool is shared to every parallel stream,
             * and pool size is set relying on cpu core size by default,
             * if the performance is limited,
             * considering using
             * ForkJoinPool forkJoinPool = new ForkJoinPool(<numThreads>);
             */

            CompletableFuture.runAsync(() -> {
                        log.info("send to topic:" + finalTopic + " :" + finalCmLog.toString());
                        kafkaTemplate.send(finalTopic, (int) (3 * Math.random()),"hello", JsonUtils.getString(finalCmLog));
                    },forkJoinPool
            );
            /**
             * case optimizing forkJoinPool params
             * another performance bottleneck is kafka I/O
             * optimize kafka params
             * batch size
             * memory
             * linger ms etc
             *
             * or split as multi topics!!!
             */
//            i++;
//            if(i%2 == 0){
//                CompletableFuture.runAsync(() -> {
//                            log.info("send to topic:" + finalTopic + " :" + finalCmLog.toString());
//                            kafkaTemplate.send("kkk", (int) (3 * Math.random()),"hello", JsonUtils.getString(finalCmLog));
//                        },forkJoinPool
//                );
//            }else{
//                CompletableFuture.runAsync(() -> {
//                            log.info("send to topic:" + finalTopic + " :" + finalCmLog.toString());
//                            kafkaTemplate.send("mmm", (int) (3 * Math.random()),"hello", JsonUtils.getString(finalCmLog));
//                        },forkJoinPool
//                );
//            }


        }
    }


    /**
     * 如果没有初始化将报错
     *
     * @param strings
     */
    public void debug(String... strings) {
        normalLog(1, strings);
    }

    public void info(String... strings) {
        normalLog(2, strings);
    }

    public void warn(String... strings) {
        normalLog(3, strings);
    }

    private void normalLog(int type, String... strings) {
        if (!StartInit.LOG_KAFKA) return;
        Optional<CmLog> cmLogOptional = Optional.ofNullable(ResponseThreadLocal.getCmLog());
        if (cmLogOptional.isPresent()) {
            CmLog cmLog = cmLogOptional.get();

            switch (type) {
                case 1:
                    cmLog.initRuntimeDebugType(strings);
                    break;
                case 2:
                    cmLog.initRuntimeInfoType(strings);
                    break;
                case 3:
                    cmLog.initRuntimeWarnType(strings);
                    break;
            }
            send(getTopic(), cmLog);
            ResponseThreadLocal.orderRun();
        } else {
            exceptionLog();
            //todo 增加代码将消息仍然记录到日志，否则数据库将无法搜索到该日志记录
            //虽然请求在接收之前就报错，但是仍然要记录到数据库
        }
    }

    /**
     * throw errors but not interrupt running program
     *
     * @param errorCode
     * @param t
     * @param strings
     */
    public void minorError(String errorCode, String desc, Throwable t, String... strings) {
        error(errorCode, desc, t, 1, strings);

    }

    public void majorError(String errorCode, String desc, Throwable t, String... strings) {
        error(errorCode, desc, t, 2, strings);
    }

    private void error(String errorCode, String errorDesc, Throwable t, int type, String... strings) {
        if (!StartInit.LOG_KAFKA) return;
        Optional<CmLog> cmLogOptional = Optional.ofNullable(ResponseThreadLocal.getCmLog());
        if (cmLogOptional.isPresent()) {
            CmLog cmLog = cmLogOptional.get();
            ResponseThreadLocal.orderRun();
            switch (type) {
                case 1:
                    cmLog.initMinorErrorType(errorCode, errorDesc, t, strings);
                    break;
                case 2:
                    cmLog.initMajorErrorType(errorCode, errorDesc, t, strings);
                    break;
            }
            cmLog.setServiceName(serviceName);
            send(getTopic(), cmLog);
        } else {
            exceptionLog();
            //todo 增加代码将消息仍然记录到日志，否则数据库将无法搜索到该日志记录
        }
    }

    /**
     * 这个方法给网关单独调的，暂时不修改，后续应该改成统一格式，统一接口
     *
     * @param errorCode
     * @param errorDesc
     * @param seq
     * @param t
     * @param strings
     */

    public void sendErrorLog(CmLog cmLog, String errorCode, String errorDesc, String seq, Throwable t, String... strings) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : strings) {
            stringBuffer.append(string);
        }
        if (cmLog == null) {
            cmLog = new CmLog();
            cmLog.setTraceId(seq == null ? UUID.randomUUID().toString().replace("-", "") : seq);
        }
        cmLog.initMajorErrorType(errorCode, errorDesc, t, strings);
//        cmLog.setTraceId(seq);
        cmLog.setErrorCode(errorCode);
        cmLog.setResultCode(errorCode);
        cmLog.setResultDesc(errorDesc);
        cmLog.setOrderId(999);
        cmLog.setExceptionMessage(MgrLog.printStackTraceToString(t));
        cmLog.setContent(String.valueOf(stringBuffer));
        send(getTopic(), cmLog);
    }


    public void sendCmLog(CmLog cmLog) {
        if (!StartInit.LOG_KAFKA) return;
        cmLog.setServiceName(serviceName);
        send(getTopic(), cmLog);
    }

    /**
     * 设置一个开关，不必每次都去取topic
     */
    private boolean isSetTopic = false;

    private String getTopic() {
        if(!isSetTopic){
            logTopicCustom = StringUtils.isEmpty(logTopicCustom) || " ".equals(logTopicCustom) ? logTopic : logTopicCustom;
            isSetTopic = true;
        }
        return logTopicCustom;

    }

    private void exceptionLog() {
        CmLog cmLog = new CmLog();
        cmLog.initSystemErrorType();
        cmLog.initErrorLog(CommonCodeHandler.ERROR_CODE_LOG_SYSTEM, CommonCodeHandler.ERROR_DESC_LOG_SYSTEM, new Exception(CommonCodeHandler.ERROR_DESC_LOG_SYSTEM),
                CommonCodeHandler.ERROR_DESC_LOG_SYSTEM);
        if (StringUtils.isEmpty(cmLog.getTraceId()))
            cmLog.setTraceId(UUID.randomUUID().toString().replace("-", ""));
        send(getTopic(), cmLog);
    }
}
