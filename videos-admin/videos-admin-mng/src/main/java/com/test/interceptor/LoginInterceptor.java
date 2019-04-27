package com.test.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description
 * @author ghsticker
 * 2019年3月12日
 */
public class LoginInterceptor implements HandlerInterceptor {

	private List<String> unCheckUrls;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		
		String requestURl = request.getRequestURI();
		requestURl = requestURl.replaceAll(request.getContextPath(), "");
		if(unCheckUrls.contains(requestURl)){
			return true;
		}
		if(null == request.getSession().getAttribute("sessionUser")){
			response.sendRedirect(request.getContextPath()+"/users/login.action");
			return false;
		}
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav)
			throws Exception {

	}

	public List<String> getUnCheckUrls() {
		return unCheckUrls;
	}
	public void setUnCheckUrls(List<String> unCheckUrls) {
		this.unCheckUrls = unCheckUrls;
	}

}

