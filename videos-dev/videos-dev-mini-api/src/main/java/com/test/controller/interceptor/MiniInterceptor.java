package com.test.controller.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.test.utils.JSONResult;
import com.test.utils.JsonUtils;
import com.test.utils.RedisOperator;

/**
 * @Description 登陆上的拦截器
 * @author ghsticker
 * 2019年3月8日
 */
public class MiniInterceptor implements HandlerInterceptor {

	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION = "user_redis_session";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String userId = request.getHeader("headerUserId");
		String userToken = request.getHeader("headerUserToken");
		
		if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)){
			String uniqueToken = redis.get(USER_REDIS_SESSION+":"+userId);
			if(StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)){
				//System.out.println("请登录...");
				returnErrorResponse(response, JSONResult.errorTokenMsg("请登录..."));
				return false;
			}else{
				if(! uniqueToken.equals(userToken)){
					//System.out.println("账号已登录...");
					returnErrorResponse(response, JSONResult.errorTokenMsg("账号已登录..."));
				}
			}
		}else{
			returnErrorResponse(response, JSONResult.errorTokenMsg("请登录..."));
			return false;
		}
		
		return true;
	}

	public void returnErrorResponse(HttpServletResponse response, JSONResult result) 
			throws IOException, UnsupportedEncodingException {
		OutputStream out=null;
		try{
		    response.setCharacterEncoding("utf-8");
		    response.setContentType("text/json");
		    out = response.getOutputStream();
		    out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
		    out.flush();
		} finally{
		    if(out!=null){
		        out.close();
		    }
		}
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}

