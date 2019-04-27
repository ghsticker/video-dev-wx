package com.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description
 * @author ghsticker
 * 2019年3月12日
 */
@Controller
public class HelloController {

	@GetMapping("hello")
	public String hello(){
		return "hello";
	}
	@GetMapping("center")
	public String center(){
		return "center";
	}
}

