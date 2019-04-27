package com.test.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.test.mapper.UsersFansMapper;
import com.test.mapper.UsersLikeVideosMapper;
import com.test.mapper.UsersMapper;
import com.test.mapper.UsersReportMapper;
import com.test.pojo.Users;
import com.test.pojo.UsersFans;
import com.test.pojo.UsersLikeVideos;
import com.test.pojo.UsersReport;
import com.test.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @Description
 * @author ghsticker 2019年3月3日
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private Sid sid;
	
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;
	
	@Autowired
	private UsersFansMapper usersFansMapper;
	
	@Autowired
	private UsersReportMapper usersReportMapper;

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryUsernameIsExist(String username) {
		Users users = new Users();
		users.setUsername(username);

		Users usersResult = usersMapper.selectOne(users);

		return usersResult == null ? false : true;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveUser(Users users) {
		String userId = sid.nextShort();
		users.setId(userId);

		usersMapper.insert(users);

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserForLogin(String username, String password) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("username", username);
		criteria.andEqualTo("password", password);

		Users result = usersMapper.selectOneByExample(userExample);

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateUserInfo(Users users) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id", users.getId());
		usersMapper.updateByExampleSelective(users, userExample);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUsersInfo(String userId) {

		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id", userId);
		Users users = usersMapper.selectOneByExample(userExample);

		return users;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserLikeVideo(String userId, String videoId) {
		if(StringUtils.isBlank(userId) && StringUtils.isBlank(videoId)){
			return false;
		}
		
		Example exapmle = new Example(UsersLikeVideos.class);
		Criteria criteria = exapmle.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("videoId", videoId);
		
		List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(exapmle);
		if(list !=null && list.size()>0){
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveUserFanRelation(String userId, String fanId) {
		UsersFans usersFans = new UsersFans();
		String usersFansId = sid.nextShort();
		usersFans.setId(usersFansId);
		usersFans.setUserId(userId);
		usersFans.setFanId(fanId);
		
		usersFansMapper.insert(usersFans);
		usersMapper.addFansCount(userId);
		usersMapper.addFollersCount(fanId);
	}
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteUserFanRelation(String userId, String fanId) {
		Example exapmle = new Example(UsersFans.class);
		Criteria criteria = exapmle.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);
		
		usersFansMapper.deleteByExample(exapmle);
		usersMapper.reduceFansCount(userId);
		usersMapper.reduceFollersCount(fanId);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryIfFollow(String userId, String fanId) {
		Example exapmle = new Example(UsersFans.class);
		Criteria criteria = exapmle.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);
		
		List<UsersFans> list = usersFansMapper.selectByExample(exapmle);
		if(list!=null && ! list.isEmpty()&& list.size() >0){
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void reportUser(UsersReport userReport) {
		String urId = sid.nextShort();
		userReport.setId(urId);
		userReport.setCreateDate(new Date());
		usersReportMapper.insert(userReport);
	}

}
