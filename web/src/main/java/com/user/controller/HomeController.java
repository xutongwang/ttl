package com.user.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;  
import org.springframework.ui.ModelMap;  

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.common.constants.QjxtConstant;
import com.common.constants.QjxtResult;
import com.common.util.RedisUtil;
import com.user.bean.Member;
import com.user.service.MemberService;


@Controller  
public class HomeController extends BaseController{  
	
	// 全局会话key
    private final static String ISP_TPS_SERVER_SESSION_ID = "isp-tps-server-session-id";
 // 全局会话key列表
    private final static String ISP_TPS_SERVER_SESSION_IDS = "isp-tps-server-session-ids";
    // code key
    private final static String ISP_TPS_SERVER_CODE = "isp-tps-server-code";
	
	@Autowired
    private MemberService memberService;
	
	//private Member member = new Member();
	
    @RequestMapping(value="/hello",method = RequestMethod.POST) 
    public String printWelcome(@RequestBody Member member,ModelMap model) {  
    	Member m = memberService.getMemberById(2);
    	//System.out.println(username+" feeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    	//System.out.println(password+" geeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    	System.out.println(member.getUsername()+" feeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    	System.out.println(member.getPassword()+" geeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    	System.out.println(m.getUsername()+" feeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    	System.out.println(m.getPassword()+" geeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    	//member.setId(23);
    	//member.setUsername("ffffsdf");
    	//memberService.saveMember(member);
    	//model.addAttribute("id", member.getId());  
       	//model.addAttribute("name", member.getUsername());
 	 	model.addAttribute("id", "1");
    	model.addAttribute("name", "abc");
        return "hello";  
    }  
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Object login(HttpServletRequest request) {
    	System.out.println("tttttttttttttttttttttttttttttttttttttttttttttttttt");
/*        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String serverSessionId = session.getId().toString();
        // 判断是否已登录，如果已登录，则回跳
        String code = RedisUtil.get(ZHENG_UPMS_SERVER_SESSION_ID + "_" + serverSessionId);
        // code校验值
        if (StringUtils.isNotBlank(code)) {
            // 回跳
            String backurl = request.getParameter("backurl");
            String username = (String) subject.getPrincipal();
            if (StringUtils.isBlank(backurl)) {
                backurl = "/";
            } else {
                if (backurl.contains("?")) {
                    backurl += "&upms_code=" + code + "&upms_username=" + username;
                } else {
                    backurl += "?upms_code=" + code + "&upms_username=" + username;
                }
            }
            _log.debug("认证中心帐号通过，带code回跳：{}", backurl);
            return "redirect:" + backurl;
        }*/
    	return new QjxtResult(QjxtConstant.SUCCESS, "redirect:/login");
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestBody Member member, ModelMap model) {
    	//String username = request.getParameter("username");
        //String password = request.getParameter("password");
        //String rememberMe = request.getParameter("rememberMe");
        if (StringUtils.isBlank(member.getUsername())) {
            return new QjxtResult(QjxtConstant.EMPTY_USERNAME, "帐号不能为空！");
        	//System.out.println("帐号不能为空！");
        }
        if (StringUtils.isBlank(member.getPassword())) {
            return new QjxtResult(QjxtConstant.EMPTY_PASSWORD, "密码不能为空！");
        	//System.out.println("密码不能为空！");
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String sessionId = session.getId().toString();
        // 判断是否已登录，如果已登录，则回跳，防止重复登录
        String hasCode = RedisUtil.get(ISP_TPS_SERVER_SESSION_ID + "_" + sessionId);
        // code校验值
        if (StringUtils.isBlank(hasCode)) {
        	System.out.println("98777777777777777777777777777777777777777777777777777777777777");
        	// 使用shiro认证
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(member.getUsername(), member.getPassword());
            usernamePasswordToken.setRememberMe(false);
            try {
                subject.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                return new QjxtResult(QjxtConstant.INVALID_USERNAME, "帐号不存在！");
            	//System.out.println("帐号不存在！");
            } catch (IncorrectCredentialsException e) {
            	return new QjxtResult(QjxtConstant.INVALID_PASSWORD, "密码错误！");
            	//System.out.println("密码错误！");
            } catch (LockedAccountException e) {
               	return new QjxtResult(QjxtConstant.INVALID_ACCOUNT, "帐号已锁定！");
            	//System.out.println("帐号已锁定！");
            }//
         // 全局会话sessionId列表，供会话管理
            RedisUtil.lpush(ISP_TPS_SERVER_SESSION_IDS, sessionId.toString());
            // 默认验证帐号密码正确，创建code
            String code = UUID.randomUUID().toString();
            // 全局会话的code
            RedisUtil.set(ISP_TPS_SERVER_SESSION_ID + "_" + sessionId, code, (int) subject.getSession().getTimeout() / 1000);
            // code校验值
            RedisUtil.set(ISP_TPS_SERVER_CODE + "_" + code, code, (int) subject.getSession().getTimeout() / 1000);
            System.out.println("555555555555555555555555555555555555555555555555555555555");
        }
        
        return new QjxtResult(QjxtConstant.SUCCESS, "/");
    }
}
