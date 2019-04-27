package com.test.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.test.enums.VideoStatusEnum;
import com.test.pojo.Bgm;
import com.test.pojo.Comments;
import com.test.pojo.Videos;
import com.test.service.BgmService;
import com.test.service.VideoService;
import com.test.utils.FetchVideoCover;
import com.test.utils.JSONResult;
import com.test.utils.MergeVideoMp3;
import com.test.utils.PagedResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @Description
 * @author ghsticker
 * 2019年3月5日
 */
@RestController
@Api(value="视频上传业务相关的接口",tags="视频上传业务相关的controller")
@RequestMapping("/video")
public class VideoController extends BaseController{
	
	@Autowired
	private BgmService bgmService;
	
	@Autowired
	private VideoService videoService;

	@ApiOperation(value="上传视频",notes="用户头像上传的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId", value="用户id", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="bgmId", value="背景音乐的id", required=false, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="videoSeconds", value="背景音乐播放长度", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="videoWidth", value="视频宽度", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="videoHeight", value="视频高度", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="desc", value="视频描述", required=false, 
				dataType="String", paramType="form"),
	})
	@PostMapping(value="/upload",headers="content-type=multipart/form-data")
	public JSONResult upload(String userId,
			String bgmId, double videoSeconds, 
			int videoWidth, int videoHeight,
			String desc,
			@ApiParam(value="短视频",required=true)
			MultipartFile file) throws Exception{
		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("用户id不能为空...");
		}
		//保存文件的命名空间
		//String fileSpace = "D:/videos-dev";
		//保存数据库的相对路径
		String uploadPathDB = "/"+userId+"/video";
		String coverPathDB = "/"+userId+"/video";
		
		
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		String finalVideoPath = "";
		
		try {
			if(file !=null ){
				String fileName = file.getOriginalFilename();
				String coverName = fileName.split("\\.")[0]+UUID.randomUUID().toString();
				
				if(StringUtils.isNoneBlank(fileName)){
				 	//文件最终保存的路径
					finalVideoPath =  FILE_SPACE + uploadPathDB+"/"+fileName;
					
					uploadPathDB+=("/"+ fileName);
					coverPathDB = coverPathDB+"/"+coverName+".jpg";
					
					File outFile = new File(finalVideoPath);
					if(outFile.getParentFile() !=null && ! outFile.getParentFile().isDirectory()){
						//创建父目录
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
					
				}
			}else{
				return JSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResult.errorMsg("上传出错...");
		}finally{
			if(fileOutputStream != null){
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		//判断bgmId是否为空，若不为空则进行音频与视频 的合并
		if(StringUtils.isNotBlank(bgmId)){
			Bgm bgm = bgmService.queryBgmById(bgmId);
			String mp3InputPath = FILE_SPACE+bgm.getPath();
			
			MergeVideoMp3 tool =  new MergeVideoMp3(FFMPEG_EXE);
			
			String videoInputPath = finalVideoPath; 
			
			String outputVideoName = UUID.randomUUID().toString()+".mp4";
			uploadPathDB = "/"+userId+"/video"+"/"+outputVideoName;
			String videoOutputPath =FILE_SPACE+ uploadPathDB;
			
			tool.convertor(videoInputPath, mp3InputPath, videoSeconds, videoOutputPath);
			
		}
//		System.out.println(uploadPathDB);
//		System.out.println(videoOutputPath);
		//生成截图
		FetchVideoCover videoInfo = new FetchVideoCover(FFMPEG_EXE);
		videoInfo.getCover(finalVideoPath, FILE_SPACE+coverPathDB);
		
		//保存视频信息到数据库
		Videos video = new Videos();
		video.setUserId(userId);
		video.setAudioId(bgmId);
		video.setVideoHeight(videoHeight);
		video.setVideoWidth(videoWidth);
		video.setVideoDesc(desc);
		video.setVideoSeconds((float)videoSeconds);
		video.setVideoPath(uploadPathDB);
		video.setCreateTime(new Date());
		video.setCoverPath(coverPathDB);
		video.setStatus(VideoStatusEnum.SUCCESS.value);
		
		
		String videoId = videoService.saveVideo(video);
		
		
		return JSONResult.ok(videoId);
	}
	
	@ApiOperation(value="上传封面",notes="上传封面的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId", value="用户id", required=true, 
				dataType="String", paramType="form"),
		@ApiImplicitParam(name="videoId", value="视频的id", required=true, 
				dataType="String", paramType="form"),
	})
	@PostMapping(value="/uploadCover",headers="content-type=multipart/form-data")
	public JSONResult uploadCover(String userId,
			String videoId,
			@ApiParam(value="视频封面",required=true)
			MultipartFile file) throws IOException{
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
			return JSONResult.errorMsg("视频Id和用户id不能为空...");
		}
		//保存文件的命名空间
		//String fileSpace = "D:/videos-dev";
		//保存数据库的相对路径
		String uploadPathDB = "/"+userId+"/video";
		
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		String finalCoverPath = "";
		
		try {
			if(file !=null ){
				String fileName = file.getOriginalFilename();
				if(StringUtils.isNoneBlank(fileName)){
					//文件最终保存的路径
					finalCoverPath =  FILE_SPACE + uploadPathDB+"/"+fileName;
					
					uploadPathDB+=("/"+ fileName);
					
					File outFile = new File(finalCoverPath);
					if(outFile.getParentFile() !=null && ! outFile.getParentFile().isDirectory()){
						//创建父目录
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
					
				}
			}else{
				return JSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JSONResult.errorMsg("上传出错...");
		}finally{
			if(fileOutputStream != null){
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		
		videoService.updateVideo(videoId, uploadPathDB);
		
		return JSONResult.ok();
	}
	
	
	/*
	 * isSaveRecord 1 需要保存
	 *              0 不需要保存或者为空时
	 */
	@ApiOperation(value="分页查询",notes="分页查询的接口")	
	@ApiImplicitParam(name="page", value="起始页", required=true, 
				dataType="Integer", paramType="form")
	@PostMapping(value="/showAll")
	public JSONResult showAll(@RequestBody Videos video,Integer isSaveRecord,Integer page,
			Integer pageSize) throws Exception{
		if(page == null){
			page = 1;
		}
		if(pageSize == null){
			pageSize = PAGE_SIZE;
		}
		//String userId = video.getUserId();
		PagedResult result = videoService.getAllVideos(video,isSaveRecord,page, pageSize);
		
		return JSONResult.ok(result);
		
	}
	
	
	@ApiOperation(value="我关注的人发的视频分页查询",notes="我关注的人发的视频分页查询的接口")
	@PostMapping(value="/showMyFollow")
	public JSONResult showMyFollow(String userId,Integer page,
			Integer pageSize) throws Exception{
		
		if (StringUtils.isBlank(userId)) {
			return JSONResult.ok();
		}
		
		if (page == null) {
			page = 1;
		}

		//int pageSize = 6;
		if(pageSize == null){
			pageSize = 6;
		}
		
		PagedResult videosList = videoService.queryMyFollowVideos(userId, page, pageSize);
		
		return JSONResult.ok(videosList);
		
	}
	@PostMapping("/showMyLike")
	public JSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return JSONResult.ok();
		}
		
		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = 6;
		}
		
		PagedResult videosList = videoService.queryMyLikeVideos(userId, page, pageSize);
		
		return JSONResult.ok(videosList);
	}
	
	@PostMapping(value="/hot")
	public JSONResult hot() throws Exception{
		
		return JSONResult.ok(videoService.getHotWords());
		
	}
	
	
	@PostMapping(value="/userLike")
	public JSONResult userLike(String userId, String videoId, String videoCreaterId) 
			throws Exception{
		
		videoService.userLikeVideo(userId, videoId, videoCreaterId);
		return JSONResult.ok();
		
	}
	
	@PostMapping(value="/userUnLike")
	public JSONResult userUnLike(String userId, String videoId, String videoCreaterId) throws Exception{
		videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
		
		return JSONResult.ok();
		
	}
	@PostMapping(value="/saveComment")
	public JSONResult saveComment(@RequestBody Comments comment,
			String fatherCommentId, String toUserId) throws Exception{
		comment.setFatherCommentId(fatherCommentId);
		comment.setToUserId(toUserId);
		
		videoService.saveComment(comment);
		
		return JSONResult.ok();
	}
	
	@PostMapping(value="/getVideoComments")
	public JSONResult getVideoComments(String videoId,Integer page,Integer pageSize) throws Exception{
		
		if(StringUtils.isBlank(videoId)){
			return JSONResult.ok();
		}
		if(page ==null){
			page = 1;
		}
		if(pageSize == null){
			pageSize = 10;
		}
		PagedResult list = videoService.getAllComments(videoId, page, pageSize);
		
		return JSONResult.ok(list);
	}
}












