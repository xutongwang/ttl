package com.security.shiro.realm;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.user.bean.Member;
import com.user.service.MemberService;

public class TpsRealm extends AuthorizingRealm{


	@Autowired
    private MemberService memberService;

	@Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        return null;
    }

    /**
     *  认证回调函数,登录时调用.
     */
	@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        Member member = memberService.getMemberByUsername(username);
        if (null == member) {
            throw new UnknownAccountException();
        }
        System.out.println("------------------------   "+!password.endsWith(member.getPassword())+"  ------------------------------------"+password+"   ||||||||||||||||||||||          "+member.getPassword());
        if (!password.endsWith(member.getPassword())) {
            throw new IncorrectCredentialsException();
        }
/*        if (upmsUser.getLocked() == 1) {
            throw new LockedAccountException();
        }*/
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
