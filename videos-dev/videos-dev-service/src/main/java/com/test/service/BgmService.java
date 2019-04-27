package com.test.service;

import java.util.List;

import com.test.pojo.Bgm;

/**
 * @Description
 * @author ghsticker
 * 2019年3月5日
 */
public interface BgmService {
	/**
	 * 
	* @Description: 查询出所有的bgm
	* @param @return      
	* @return List<Bgm>
	* @throws
	 */
	public List<Bgm> queryBgmList();
	/**
	 * 
	* @Description: 根据id查询bgm
	* @param @param bgmId
	* @param @return      
	* @return Bgm
	* @throws
	 */
	public Bgm queryBgmById(String bgmId);
}

