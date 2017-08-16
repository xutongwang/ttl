package com.user.service;

import com.user.bean.Member;

public interface MemberService {
	
	boolean saveMember(Member member);
	Member getMemberById(Integer id);
	public Member getMemberByUsername(String username);
}