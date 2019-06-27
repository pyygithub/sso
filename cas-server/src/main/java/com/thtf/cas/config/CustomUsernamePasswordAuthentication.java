package com.thtf.cas.config;

import com.thtf.cas.model.User;
import com.thtf.cas.util.SpringSecurityUtil;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.MessageDescriptor;
import org.apereo.cas.authentication.PreventedException;
import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.InvalidLoginLocationException;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.services.ServicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * ========================
 * 自定义认证校验策略
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/26
 * Time：16:05
 * Version: v1.0
 * ========================
 */
public class CustomUsernamePasswordAuthentication extends AbstractUsernamePasswordAuthenticationHandler {

    private final static Logger logger = LoggerFactory.getLogger(CustomUsernamePasswordAuthentication.class);

    private JdbcTemplate jdbcTemplate;

    public CustomUsernamePasswordAuthentication(String name, ServicesManager servicesManager,
                                                PrincipalFactory principalFactory, Integer order, JdbcTemplate jdbcTemplate) {
        super(name, servicesManager, principalFactory, order);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(
            UsernamePasswordCredential credential, String originalPassword)
            throws GeneralSecurityException, PreventedException {
        String username = credential.getUsername();
        String password = credential.getPassword();
        logger.info("认证用户 username = {}", username);

        String sql = "select id, username, password, expired, disable, email FROM sys_user where username = ?";
        User info = (User) jdbcTemplate.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper(User.class));
        if (info == null) {
            logger.info("用户不存在！");
            throw new AccountException("用户不存在!");
        } else if (!SpringSecurityUtil.checkpassword(password, info.getPassword())) {
            logger.info("密码错误!");
            throw new FailedLoginException("密码错误!");
        } else if (username.startsWith("lock")) {
            //用户锁定
            throw new AccountLockedException();
        } else if (info.getDisable().equals("1")) {
            //用户禁用
            throw new AccountDisabledException("用户被禁用");
        } else if (info.getExpired().equals("1")) {
            //用户过期，需要修改密码
            throw new AccountExpiredException("用户过期，需修改密码");
        } else {
            logger.info("校验成功");
            //可自定义返回给客户端的多个属性信息
            HashMap<String, Object> returnInfo = new HashMap<>();
            returnInfo.put("id", info.getId());
            returnInfo.put("username", info.getUsername());
            returnInfo.put("expired", info.getExpired());
            returnInfo.put("disable", info.getDisable());
            returnInfo.put("email", info.getEmail());
            final List<MessageDescriptor> list = new ArrayList<>();
            return createHandlerResult(credential, this.principalFactory.createPrincipal(username, returnInfo), list);
        }
    }
}
