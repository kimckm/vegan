package rz.dingtalk.controller;

import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rz.dingtalk.config.DingTalkProperties;
import rz.dingtalk.controller.dto.DingTalkCallbackDto;

import java.util.Map;

@RestController
@RequestMapping("/ding_talk")
public class DingTalkController {

    private static final Logger log = LoggerFactory.getLogger(DingTalkController.class);

    @Autowired
    private DingTalkProperties properties;

    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";

    /**
     * 钉钉审批回调接口
     */
    @PostMapping("/callback")
    public Object callback(@RequestParam String signature,
                           @RequestParam String timestamp,
                           @RequestParam String nonce,
                           @RequestBody DingTalkCallbackDto dingTalkCallbackDto) {
        log.info("signature={}, timestamp={}, nonce={}, json={}", signature, timestamp, nonce, dingTalkCallbackDto);

        try {
            // 获取回调信息的加密数据进行解密处理
            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(properties.getToken(), properties.getEncodingAESKey(), properties.getCorpId());
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, dingTalkCallbackDto.getEncrypt());

            log.info("收到审批实例状态更新plainText: " + plainText);

            // TODO 业务处理

            // 返回success的加密信息表示回调处理成功
            Map<String, String> res = dingTalkEncryptor.getEncryptedMap(SUCCESS, Long.valueOf(timestamp), nonce);
            log.info("返回加密信息给钉钉={}", res);

            return res;
        } catch (Exception e) {
            log.error("钉钉回调异常", e);
            return FAIL;
        }
    }

}
