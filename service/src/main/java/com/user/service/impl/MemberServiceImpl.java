package com.user.service.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import com.alibaba.dubbo.config.annotation.Service;
import com.user.bean.Member;
import com.user.dao.MemberMapper;
import com.user.service.MemberService;
@Service
@Transactional
public class MemberServiceImpl implements MemberService {	
    private static final Logger LOG = LoggerFactory.getLogger(MemberServiceImpl.class);    
    @Autowired
    private MemberMapper memeberDao;
	public boolean saveMember(Member member) {
		try {
			memeberDao.insert(member);
			return true;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return false;
	}
}