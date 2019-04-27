package com.test.controller;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.pojo.Users;
import com.test.pojo.vo.UsersVO;
import com.test.service.UserService;
import com.test.utils.JSONResult;
import com.test.utils.MD5Utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @Description 注册，登陆用户
 * @author ghsticker
 * 2019年3月3日
 */
@RestController
@Api(value="用户注册登陆的接口",tags="注册和登陆的controller")
public class RegistLoginController extends BaseController{
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value="用户注册",notes="用户注册的接口")
	@PostMapping("/regist")
	public JSONResult regist(@RequestBody Users users) throws Exception{
		//判断用户密码账号是否为空
		if(StringUtils.isBlank(users.getUsername()) && StringUtils.isBlank(users.getPassword()))
			return JSONResult.errorMsg("用户或密码不能为空");
		
		//用户名是否存在
		boolean usernameIsExist = userService.queryUsernameIsExist(users.getUsername());
		
		//保存用户
		if(!usernameIsExist){
			users.setNickname(users.getUsername());
			users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
			users.setFansCounts(0);
			users.setFollowCounts(0);
			users.setReceiveLikeCounts(0);
			userService.saveUser(users);
			
		}else{
			return JSONResult.errorMsg("用户已经存在！！");
		}
		users.setPassword("");
		
		UsersVO usersVo = setUserRedisSessionToken(users);
		return JSONResult.ok(usersVo);
	}
	
	
	public UsersVO setUserRedisSessionToken(Users userModel){
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION+":"+userModel.getId(), uniqueToken, 1000 * 60 * 30);
		
		UsersVO usersVo = new UsersVO();
		
		BeanUtils.copyProperties(userModel, usersVo);
		usersVo.setUserToken(uniqueToken);
		return usersVo;
	}
	
	@ApiOperation(value="用户登陆",notes="用户登陆的接口")
	@PostMapping("/login")
	public JSONResult login(@RequestBody Users users) throws Exception{
		String username = users.getUsername();
		String password = users.getPassword();
		
		//判断是不是为空
		if(StringUtils.isBlank(users.getUsername()) && StringUtils.isBlank(users.getPassword()))
			return JSONResult.errorMsg("用户名密码不能为空！");
		//判断是否存在
		Users userResult = userService.queryUserForLogin(username, 
				MD5Utils.getMD5Str(password));
		
		//登陆
		if(userResult!=null){
			userResult.setPassword("");
			UsersVO userVo = setUserRedisSessionToken(userResult);
			return JSONResult.ok(userVo);
		}else{
			return JSONResult.errorMsg("用户名或密码不正确, 请重试...");
		}
		
	}
	
	@ApiOperation(value="用户注销",notes="用户注销的接口")
	@ApiImplicitParam(name="userId", value="用户id", required=true, 
						dataType="String", paramType="query")
	@PostMapping("/logout")
	public JSONResult logout(String userId){
		redis.del(USER_REDIS_SESSION+":"+userId);
		
		return JSONResult.ok();
	}

}

