package com.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.test.pojo.Videos;
import com.test.pojo.vo.VideosVO;
import com.test.utils.MyMapper;

public interface VideosMapperCustom extends MyMapper<Videos> {
	
	public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc
			,@Param("userId")String userId);
	/**
	 * 
	* @Description: 增加视频点赞
	* @param @param videoId      
	* @return void
	* @throws
	 */
	public void addVideoLikeCount(String videoId);
	/**
	 * 
	* @Description: 减少视频点赞/取消点赞
	* @param @param videoId      
	* @return void
	* @throws
	 */
	public void reduceVideoLikeCount(String videoId);
	/**
	 * 
	* @Description: 查询关注的视频
	* @param @param userId
	* @param @return      
	* @return List<VideosVO>
	* @throws
	 */
	public List<VideosVO> queryMyFollowVideos(String userId);
	/**
	 * 
	* @Description: 查询喜欢的视频
	* @param @param userId
	* @param @return      
	* @return List<VideosVO>
	* @throws
	 */
	public List<VideosVO> queryMyLikeVideos(@Param("userId") String userId);
	
}