package com.test.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.bean.AdminUser;
import com.test.pojo.Users;
import com.test.service.UserService;

import com.test.utils.JSONResult;
import com.test.utils.PagedResult;
/**
 * @Description
 * @author ghsticker
 * 2019年3月12日
 */
@Controller
@RequestMapping("users")
public class UsersController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String login(){
		return "login";
	}
	
	@GetMapping("/showList")
	public String showList(){
		return "users/usersList";
	}
	
	@PostMapping("/list")
	@ResponseBody
	public PagedResult list(Users user , Integer page) {
		
		PagedResult result = userService.queryUsers(user, page == null ? 1 : page, 10);
		return result;
	}
	
	@PostMapping("/login")
	@ResponseBody
	public JSONResult userLogin(String username,String password,HttpServletRequest request,
			HttpServletResponse response){
		if(StringUtils.isBlank(username) && StringUtils.isBlank(password)){
			JSONResult.errorMap("用户名和密码不能为空....");
		}else if(username.equals("lee") && password.equals("123")){
			String token = UUID.randomUUID().toString();
			AdminUser adminUser = new AdminUser(username,password,token);
			request.getSession().setAttribute("sessionUser", adminUser);
			return JSONResult.ok();
		}
		
		return JSONResult.errorMsg("登陆失败，请重试...");
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response){
		request.getSession().removeAttribute("sessionUser");
		return "login";
	}

} 

