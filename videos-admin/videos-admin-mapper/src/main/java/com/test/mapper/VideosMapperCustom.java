package com.test.mapper;

import com.test.pojo.vo.VideoVo;

import java.util.List;


public interface VideosMapperCustom {
	
	public List<VideoVo> selectAllVideos();
}