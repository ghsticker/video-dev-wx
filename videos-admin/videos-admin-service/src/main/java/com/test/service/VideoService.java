package com.test.service;

import com.test.pojo.Bgm;
import com.test.utils.PagedResult;

/**
 * @Description
 * @author ghsticker
 * 2019年3月13日
 */
public interface VideoService {
	/**
	 * 
	* @Description: 添加bgm
	* @param @param bgm      
	* @return void
	* @throws
	 */
	public void addBgm(Bgm bgm);
	/**
	 * 
	* @Description: 分页查询bgm列表
	* @param @param page
	* @param @param pageSize
	* @param @return      
	* @return PagedResult
	* @throws
	 */
	public PagedResult queryBgmList(Integer page,Integer pageSize);
	/**
	 * 
	* @Description: 删除BGM
	* @param @param id      
	* @return void
	* @throws
	 */
	public void deleteBgm(String id);
	/**
	 * 
	* @Description: 分页查询举报列表
	* @param @param page
	* @param @param pageSize
	* @param @return      
	* @return PagedResult
	* @throws
	 */
	public PagedResult queryReportList(Integer page, Integer pageSize);
	/**
	 * 
	* @Description: 改变视频状态
	* @param @param videoId
	* @param @param status      
	* @return void
	* @throws
	 */
	public void updateVideoStatus(String videoId, Integer status);
	/**
	 * 
	* @Description: 展示所有的视频
	* @param @param page
	* @param @param pageSize
	* @param @return      
	* @return PagedResult
	* @throws
	 */
	public PagedResult queryVideosList(Integer page, Integer pageSize);
	
	/**
	 * 
	* @Description: 删除视频
	* @param @param id      
	* @return void
	* @throws
	 */
	public void deleteVides(String id);

}

