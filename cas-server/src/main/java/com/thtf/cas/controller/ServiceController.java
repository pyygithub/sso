package com.thtf.cas.controller;

import org.apereo.cas.services.RegexRegisteredService;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.ReturnAllAttributeReleasePolicy;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/27
 * Time：14:56
 * Version: v1.0
 * ========================
 */
@RestController
public class ServiceController {
    private Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    @Qualifier("servicesManager")
    private ServicesManager servicesManager;

    /**
     * 注册service
     * @param serviceId 域名
     * @param id 顺序
     * @return
     */
    @GetMapping("/addClient/{serviceId}/{id}")
    public Object addClient(@PathVariable("serviceId") String serviceId, @PathVariable("id") int id) {
        try {
            String a="^(https|imaps|http)://"+serviceId+".*";
            RegexRegisteredService service = new RegexRegisteredService();
            ReturnAllAttributeReleasePolicy re = new ReturnAllAttributeReleasePolicy();
            service.setServiceId(a);
            service.setId(id);
            service.setAttributeReleasePolicy(re);
            service.setName("login");
            //这个是为了单点登出而作用的
            service.setLogoutUrl(new URL("http://"+serviceId));
            servicesManager.save(service);
            //执行load让他生效
            servicesManager.load();
            ReturnMessage returnMessage = new ReturnMessage();
            returnMessage.setCode(200);
            returnMessage.setMessage("添加成功");
            return returnMessage;
        } catch (Exception e) {
            logger.error("注册service异常",e);
            ReturnMessage returnMessage = new ReturnMessage();
            returnMessage.setCode(500);
            returnMessage.setMessage("添加失败");
            return returnMessage;
        }
    }

    /**
     * 删除service异常
     * @param serviceId
     * @return
     */
    @GetMapping("/deleteClient/{serviceId}/{id}")
    public Object deleteClient(@PathVariable("serviceId") String serviceId,@PathVariable("id") int id) {
        try {

//            String a="^(https|imaps|http)://"+serviceId+".*";
//            String a="^(https|imaps|http)://"+serviceId+".*";
//            RegexRegisteredService service = new RegexRegisteredService();
//            ReturnAllAttributeReleasePolicy re = new ReturnAllAttributeReleasePolicy();
//            service.setServiceId(a);
//            service.setId(id);
//            service.setAttributeReleasePolicy(re);
//            service.setName("login");
//            //这个是为了单点登出而作用的
//            service.setLogoutUrl(new URL("http://"+serviceId));
            String aa = "http://app2.cas.com:8082";
            RegisteredService service = servicesManager.findServiceBy(aa);
            servicesManager.delete(service);
            //执行load生效
            servicesManager.load();

            ReturnMessage returnMessage = new ReturnMessage();
            returnMessage.setCode(200);
            returnMessage.setMessage("删除成功");
            return returnMessage;
        } catch (Exception e) {
            logger.error("删除service异常",e);
            ReturnMessage returnMessage = new ReturnMessage();
            returnMessage.setCode(500);
            returnMessage.setMessage("删除失败");
            return returnMessage;
        }
    }

    public class ReturnMessage{

        private Integer code;

        private String message;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
