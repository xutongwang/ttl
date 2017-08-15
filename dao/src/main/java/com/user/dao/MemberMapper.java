package com.user.dao;

import com.common.readwriteseparate.DataSource;
import com.user.bean.Member;

public interface MemberMapper {	
	@DataSource("master")
    int insert(Member record);
	@DataSource("master")
    int insertSelective(Member record);
	@DataSource("master")
    int deleteByPrimaryKey(Integer id);
    @DataSource("master")
    int updateByPrimaryKeySelective(Member record);
    @DataSource("master")
    int updateByPrimaryKey(Member record);
    @DataSource("slave")
    Member selectByPrimaryKey(Integer id);    
    @DataSource("slave")
    Member selectByUsername(String username);
}