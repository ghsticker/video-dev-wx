package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.utils.RedisOperator;

/**
 * @Description
 * @author ghsticker
 * 2019年3月3日
 */

public class BaseController {
	
	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION = "user_redis_session";
	
	//保存文件的命名空间
	public static final String FILE_SPACE = "C:/videos-dev";
	//ffmpeg.exe的目录
	public static final String FFMPEG_EXE = "C:\\ffmpeg\\bin\\ffmpeg.exe";
	
	public static final Integer PAGE_SIZE = 5;
	
	
}

