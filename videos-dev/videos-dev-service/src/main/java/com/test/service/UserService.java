package com.test.service;

import com.test.pojo.Users;
import com.test.pojo.UsersReport;

/**
 * @Description
 * @author ghsticker
 * 2019年3月3日
 */
public interface UserService {
	/**
	 * 
	* @Description: 判断用户名是否存在
	* @param @return      
	* @return boolean
	* @throws
	 */
	public boolean queryUsernameIsExist(String username);
	
	/**
	 * 
	* @Description: 保存用户(注册)
	* @param @param users      
	* @return void
	* @throws
	 */
	public void saveUser(Users users);
	/**
	 * 
	* @Description: 查询用户是否存在（登陆）
	* @param @param username
	* @param @param password
	* @param @return      
	* @return Users
	* @throws
	 */
	public Users queryUserForLogin(String username,String password);

	/**
	 * 
	* @Description: 更新用户头像
	* @param @param users      
	* @return void
	* @throws
	 */
	public void updateUserInfo(Users users);
	/**
	 * 
	* @Description: 查询用户信息
	* @param @param userId
	* @param @return      
	* @return Users
	* @throws
	 */
	public Users queryUsersInfo(String userId);
	/**
	 * 
	* @Description: 判断是否有赞
	* @param @param userId
	* @param @param videoId
	* @param @return      
	* @return boolean
	* @throws
	 */
	public boolean isUserLikeVideo(String userId, String videoId);
	/**
	 * 
	* @Description: 保存用户与粉丝的关系
	* @param @param userId
	* @param @param fanId      
	* @return void
	* @throws
	 */
	public void saveUserFanRelation(String userId, String fanId);
	/**
	 * 
	* @Description: 取消用户与粉丝的关系
	* @param @param userId
	* @param @param fanId      
	* @return void
	* @throws
	 */
	public void deleteUserFanRelation(String userId, String fanId);
	/**
	 * 
	* @Description: 查询用户是否关注
	* @param @param userId
	* @param @param fanId
	* @param @return      
	* @return boolean
	* @throws
	 */
	public boolean queryIfFollow(String userId, String fanId); 
	/**
	 * 
	* @Description: 保存举报
	* @param @param userReport      
	* @return void
	* @throws	
	 */
	public void reportUser(UsersReport userReport);
}

