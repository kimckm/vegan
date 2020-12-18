package rz.dingtalk.manager;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import static com.dingtalk.api.request.OapiProcessinstanceCreateRequest.FormComponentValueVo;

import com.dingtalk.api.request.OapiProcessinstanceCreateRequest;
import com.dingtalk.api.request.OapiUserGetByMobileRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiProcessinstanceCreateResponse;
import com.dingtalk.api.response.OapiUserGetByMobileResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rz.dingtalk.config.DingDingConstant;
import rz.dingtalk.config.DingTalkProperties;

import java.util.ArrayList;
import java.util.List;

@Component
public class DingTalkManager {

    private static final Logger log = LoggerFactory.getLogger(DingTalkManager.class);

    @Autowired
    private DingTalkProperties properties;

    /**
     * 发起审批实例
     */
    public void process(String processCode, String mobile) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingDingConstant.URL_PROCESS_INSTANCE_START);

            OapiProcessinstanceCreateRequest req = new OapiProcessinstanceCreateRequest();
            req.setAgentId(properties.getAgentId());
            req.setProcessCode(processCode);

            String dingUserId = this.fetchUserId(mobile);
            log.info("根据手机号码获取钉钉userId={}", dingUserId);

            if (StringUtils.isEmpty(dingUserId)) {
                dingUserId = properties.getDefaultUserId();
            }
            log.info("最终dingUserId:{}", dingUserId);

            req.setOriginatorUserId(dingUserId);
            req.setDeptId(getDepId(dingUserId));

            List<FormComponentValueVo> componentValues = new ArrayList<>();

            FormComponentValueVo nameVo = new FormComponentValueVo();
            nameVo.setName("名称");
            nameVo.setValue("bkk");
            componentValues.add(nameVo);

            req.setFormComponentValues(componentValues);

            OapiProcessinstanceCreateResponse rsp = client.execute(req, token());
            // 流程实例ID
            String processInstanceId = rsp.getProcessInstanceId();

            log.info("实例ID={}", processInstanceId);
            log.info("调用创建审批返回数据：{}", rsp.getBody());
        } catch (ApiException e) {
            log.error("发起审批异常", e);
        }
    }

    /**
     * 根据钉钉 UserId获取部门Id
     */
    private Long getDepId(String userId) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingDingConstant.URL_USER_GET);
            OapiUserGetRequest request = new OapiUserGetRequest();

            request.setUserid(userId);
            request.setHttpMethod("GET");

            OapiUserGetResponse response = client.execute(request, this.token());
            log.info("getDepId获取用户详情={}", response.getBody());

            List<Long> departments = response.getDepartment();
            log.info("departments={}", departments);

            if (CollectionUtils.isEmpty(departments)) {
                return null;
            }

            return departments.get(0);
        } catch (ApiException e) {
            log.error("获取部门ID异常", e);
        }

        return null;
    }

    /**
     * 根据用户手机号码获取钉钉userId
     */
    public String fetchUserId(String mobile) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(DingDingConstant.GET_BY_MOBILE);
            OapiUserGetByMobileRequest req = new OapiUserGetByMobileRequest();

            req.setMobile(mobile);
            req.setHttpMethod("GET");

            OapiUserGetByMobileResponse rsp = client.execute(req, this.token());

            String body = rsp.getBody();
            log.info("getByTel获取用户信息返回数据={}", body);

            String userId = rsp.getUserid();
            log.info("userId={}", userId);

            return userId;
        } catch (ApiException e) {
            log.error("钉钉API异常", e);
        }

        return null;
    }

    /**
     * 获取钉钉token
     */
    public String token() {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(DingDingConstant.URL_GET_TOKEN);
            OapiGettokenRequest request = new OapiGettokenRequest();

            request.setAppkey(properties.getAppKey());
            request.setAppsecret(properties.getAppSecret());
            request.setHttpMethod("GET");

            OapiGettokenResponse rsp = client.execute(request);
            String body = rsp.getBody();
            log.info("getToken接口返回数据={}", body);

            String token = rsp.getAccessToken();
            log.info("token={}", token);

            return token;
        } catch (ApiException e) {
            log.error("钉钉API异常", e);
        }

        return null;
    }

}
