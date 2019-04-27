package com.test.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.test.pojo.Users;
import com.test.pojo.UsersReport;
import com.test.pojo.vo.PublisherVideo;
import com.test.pojo.vo.UsersVO;
import com.test.service.UserService;
import com.test.utils.JSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @Description 用户业务相关接口
 * @author ghsticker 
 * 2019年3月3日
 */
@RestController
@Api(value = "用户业务相关的接口", tags = "用户业务相关的controller")
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "用户头像上传", notes = "用户头像上传的接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/uploadFace")
	public JSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws IOException {
		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("用户id不能为空...");
		}
		// 保存文件的命名空间
		//String fileSpace = "D:/videos-dev";
		// 保存数据库的相对路径
		String uploadPathDB = "/" + userId + "/face";

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;

		try {
			if (files != null && files.length > 0) {
				String fileName = files[0].getOriginalFilename();
				if (StringUtils.isNoneBlank(fileName)) {
					// 文件最终保存的路径
					String finalFacePath = FILE_SPACE + uploadPathDB + "/" + fileName;

					uploadPathDB += ("/" + fileName);

					File outFile = new File(finalFacePath);
					if (outFile.getParentFile() != null && !outFile.getParentFile().isDirectory()) {
						// 创建父目录
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);

				}
			} else {
				return JSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResult.errorMsg("上传出错...");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}

		Users users = new Users();
		users.setId(userId);
		users.setFaceImage(uploadPathDB);
		userService.updateUserInfo(users);

		return JSONResult.ok(uploadPathDB);
	}

	@ApiOperation(value = "用户信息查询", notes = "用户信息查询的接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/query")
	public JSONResult query(String userId, String fanId) throws IOException {
		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("用户id不能为空...");
		}
		Users user = userService.queryUsersInfo(userId);
		UsersVO userVo = new UsersVO();
		BeanUtils.copyProperties(user, userVo);

		boolean follow = userService.queryIfFollow(userId, fanId);
		userVo.setFollow(follow);

		return JSONResult.ok(userVo);
	}

	@ApiOperation(value = "发布视频用户信息查询", notes = "发布视频用户信息查询的接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "loginUserId", value = "登陆用户id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "videoId", value = "视频id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "publishUserId", value = "发布视频用户id", required = true, dataType = "String", paramType = "query"), })

	@PostMapping("/queryPublisher")
	public JSONResult queryPublisher(String loginUserId, String videoId, String publishUserId) throws Exception {
		if (StringUtils.isBlank(publishUserId)) {
			return JSONResult.errorMsg("");
		}
		// 查询上传人信息
		Users user = userService.queryUsersInfo(publishUserId);
		UsersVO userVo = new UsersVO();
		BeanUtils.copyProperties(user, userVo);
		// 查询登陆用户与视频是否点赞
		Boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);

		PublisherVideo publisherVideo = new PublisherVideo();
		publisherVideo.setPublisher(userVo);
		publisherVideo.setUserLikeVideo(userLikeVideo);

		return JSONResult.ok(publisherVideo);
	}

	@ApiOperation(value = "关注用户", notes = "关注用户的接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "视频用户id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "query") })
	@PostMapping("/beyourfans")
	public JSONResult beyourfans(String userId, String fanId) throws IOException {
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(fanId)) {
			return JSONResult.errorMsg("");
		}

		userService.saveUserFanRelation(userId, fanId);

		return JSONResult.ok("关注成功...");
	}

	@ApiOperation(value = "取消关注用户", notes = "取消关注用户的接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "视频用户id", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "fanId", value = "粉丝id", required = true, dataType = "String", paramType = "query") })

	@PostMapping("/dontbeyourfans")
	public JSONResult dontbeyourfans(String userId, String fanId) throws IOException {
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(fanId)) {
			return JSONResult.errorMsg("");
		}
		userService.deleteUserFanRelation(userId, fanId);

		return JSONResult.ok("取消关注成功...");
	}

	@PostMapping("/reportUser")
	public JSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {

		// 保存举报信息
		userService.reportUser(usersReport);

		return JSONResult.errorMsg("举报成功...有你平台变得更美好...");
	}
}
