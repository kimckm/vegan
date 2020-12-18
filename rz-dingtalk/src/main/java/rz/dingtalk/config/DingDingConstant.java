package rz.dingtalk.config;

public interface DingDingConstant {

    /**
     * 获取token请求地址
     */
    String URL_GET_TOKEN = "https://oapi.dingtalk.com/gettoken";

    /**
     * 根据手机号码获取用户ID
     */
    String GET_BY_MOBILE = "https://oapi.dingtalk.com/user/get_by_mobile";

    /**
     * 获取用户详情的接口url
     */
    String URL_USER_GET = "https://oapi.dingtalk.com/user/get";

    /**
     * 发起审批实例的接口url
     */
    String URL_PROCESS_INSTANCE_START = "https://oapi.dingtalk.com/topapi/processinstance/create";

}
