package com.test.service;

import com.test.pojo.Users;
import com.test.utils.PagedResult;

/**
 * @Description
 * @author ghsticker
 * 2019年3月14日
 */
public interface UserService {
	
	public PagedResult queryUsers(Users user, Integer page, Integer pageSize);

}

