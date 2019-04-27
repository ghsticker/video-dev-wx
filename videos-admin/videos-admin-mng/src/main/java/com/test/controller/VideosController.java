package com.test.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.test.enums.VideoStatusEnum;
import com.test.pojo.Bgm;
import com.test.service.VideoService;
import com.test.utils.JSONResult;
import com.test.utils.PagedResult;

/**
 * @Description
 * @author ghsticker
 * 2019年3月12日
 */
@Controller
@RequestMapping("video")
public class VideosController {
	
	@Autowired
	private VideoService videoService;
	
	@Value("${FILE_SPACE}")
	private String FILE_SPACE;

	@GetMapping("/showAddBgm")
	public String showAddBgm(){
		return "video/addBgm";
	}
	
	@GetMapping("/showBgmList")
	public String showBgmList(){
		return "video/bgmList";
	}
	
	@GetMapping("/showReportList")
	public String showReportList() {
		return "video/reportList";
	}
	
	@GetMapping("/showVideosList")
	public String showVideosList() {
		return "video/videosList";
	}
	
	@PostMapping("/reportList")
	@ResponseBody
	public PagedResult reportList(Integer page){
		
		return videoService.queryReportList(page, 10);
	}
	
	@PostMapping("/videosList")
	@ResponseBody
	public PagedResult videosList(Integer page){
		return videoService.queryVideosList(page, 10);
	}
	
	
	@PostMapping("/deleteVideo")
	public JSONResult delVideo(String videoId){
		videoService.deleteBgm(videoId);
		return JSONResult.ok();
	}
	
	@PostMapping("/forbidVideo")
	@ResponseBody
	public JSONResult forbidVideo(String videoId){
		videoService.updateVideoStatus(videoId,  VideoStatusEnum.FORBID.value);
		return JSONResult.ok();
	}
	@PostMapping("/addBgm")
	@ResponseBody
	public JSONResult addBgm(Bgm bgm){
		videoService.addBgm(bgm);
		
		return JSONResult.ok();
	}
	
	@PostMapping("/queryBgmList")
	@ResponseBody
	public PagedResult queryBgmList(Integer page){
		return videoService.queryBgmList(page, 6);
	}
	
	@PostMapping("/delBgm")
	@ResponseBody
	public JSONResult delBgm(String bgmId){
		videoService.deleteBgm(bgmId);	
		return JSONResult.ok();
	}
	
	
	@PostMapping("/bgmUpload")
	@ResponseBody
	public JSONResult bgmUpload(@RequestParam("file") MultipartFile[] files) throws IOException {
		
		// 保存文件的命名空间
		//String fileSpace = File.separator + "imooc_videos_dev" + File.separator + "mvc-bgm";
		//String fileSpace = "C:"+File.separator+"videos-dev"+File.separator+"mvc-bgm";
		// 保存数据库的相对路径
		String uploadPathDB = File.separator+ "bgm";
		//System.out.println(fileSpace);

		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;

		try {
			if (files != null && files.length > 0) {
				String fileName = files[0].getOriginalFilename();
				if (StringUtils.isNoneBlank(fileName)) {
					// 文件最终保存的路径
					String finalPath = FILE_SPACE + uploadPathDB + "/" + fileName;

					uploadPathDB += (File.separator + fileName);

					File outFile = new File(finalPath);
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
		return JSONResult.ok(uploadPathDB);
	}
}

