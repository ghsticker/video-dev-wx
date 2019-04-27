package com.test.pojo.vo;

import java.util.Date;

/**
 * @Description
 * @author ghsticker 
 * 2019年4月22日
 */
public class VideoVo {

	private String id;
	private String username;

	private String videoDesc;

	private String videoPath;
	private Long likeCounts;

	private Integer status;

	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVideoDesc() {
		return videoDesc;
	}

	public void setVideoDesc(String videoDesc) {
		this.videoDesc = videoDesc;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public Long getLikeCounts() {
		return likeCounts;
	}

	public void setLikeCounts(Long likeCounts) {
		this.likeCounts = likeCounts;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
	
}
