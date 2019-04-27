package com.test.mapper;

import com.test.utils.MyMapper;
import com.test.pojo.Users;

public interface UsersMapper extends MyMapper<Users> {
	/**
	 * 
	* @Description: 用户受到的赞
	* @param @param userId      
	* @return void
	* @throws
	 */
	public void addReceiveLikeCount(String userId);
	/**
	 * 
	* @Description: 用户取消赞
	* @param @param userId      
	* @return void
	* @throws
	 */
	public void reduceReceiveLikeCount(String userId);
	
	/**
	 * 
	* @Description: 增加粉丝数量
	* @param       
	* @return void
	* @throws
	 */
	public void addFansCount(String userId);
	
	
	/**
	 * 
	* @Description: 减少粉丝数量
	* @param       
	* @return void
	* @throws
	 */
	public void reduceFansCount(String userId);
	
	/**
	 * 
	* @Description: 增加关注数量
	* @param       
	* @return void
	* @throws
	 */
	public void addFollersCount(String userId);
	/**
	 * 
	* @Description: 减少关注数量
	* @param       
	* @return void
	* @throws
	 */
	public void reduceFollersCount(String userId);
	
	
}