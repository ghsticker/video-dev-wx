package com.test.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.test.mapper.BgmMapper;
import com.test.pojo.Bgm;
import com.test.service.BgmService;

/**
 * @Description
 * @author ghsticker
 * 2019年3月5日
 */
@Service
public class BgmServiceImpl implements BgmService {

	@Autowired
	private BgmMapper bgmMapper;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public List<Bgm> queryBgmList() {
		return bgmMapper.selectAll();
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	@Override
	public Bgm queryBgmById(String bgmId) {
		return bgmMapper.selectByPrimaryKey(bgmId);
	}

}

