package com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.service.BgmService;
import com.test.utils.JSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description
 * @author ghsticker
 * 2019年3月5日
 */
@RestController
@Api(value="背景音乐业务的接口",tags="背景音乐业务的controller")
@RequestMapping("/bgm")
public class BgmController {

	@Autowired
	private BgmService bgmService;
	
	@ApiOperation(value="获取音乐列表",notes="获取音乐列表的接口")
	@PostMapping("/list")
	public JSONResult list(){
		return JSONResult.ok(bgmService.queryBgmList()); 
	}
}








