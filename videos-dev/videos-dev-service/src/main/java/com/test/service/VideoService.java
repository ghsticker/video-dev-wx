package com.test.service;

import java.util.List;

import com.test.pojo.Comments;
import com.test.pojo.Videos;
import com.test.utils.PagedResult;

/**
 * @Description
 * @author ghsticker 
 * 2019年3月6日
 */
public interface VideoService {

	/**
	 * 
	* @Description: 保存视频
	* @param @param video
	* @param @return      
	* @return String
	* @throws
	 */
	public String saveVideo(Videos video);

	/**
	 * 
	* @Description: 更新视频
	* @param @param videoId
	* @param @param coverPath      
	* @return void
	* @throws
	 */
	public void updateVideo(String videoId, String coverPath);

	/**
	 * 
	* @Description: 根据分页查询所有视频
	* @param @param video
	* @param @param isSaveRecord
	* @param @param page
	* @param @param pageSize
	* @param @return      
	* @return PagedResult
	* @throws
	 */
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);

	/**
	 * 
	* @Description: 获得热搜词
	* @param @return      
	* @return List<String>
	* @throws
	 */
	public List<String> getHotWords();

	/**
	 * 
	* @Description: 用户喜欢视频
	* @param @param userId
	* @param @param videoId
	* @param @param videoCreateId      
	* @return void
	* @throws
	 */
	public void userLikeVideo(String userId, String videoId, String videoCreateId);

	/**
	 * 
	* @Description: 用户取消赞/不喜欢视频
	* @param @param userId
	* @param @param videoId
	* @param @param videoCreateId      
	* @return void
	* @throws
	 */
	public void userUnLikeVideo(String userId, String videoId, String videoCreateId);

	/**
	 * 
	* @Description: 查询我关注人的视频
	* @param @param userId
	* @param @param page
	* @param @param pageSize
	* @param @return      
	* @return PagedResult
	* @throws
	 */
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);


	/**
	 * 
	* @Description: 查询我喜欢的视频
	* @param @param userId
	* @param @param page
	* @param @param pageSize
	* @param @return      
	* @return PagedResult
	* @throws
	 */
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);

	/**
	 * 
	* @Description: 保存评论
	* @param @param comment      
	* @return void
	* @throws
	 */
	public void saveComment(Comments comment);

	/**
	 * 
	* @Description: 分页查询所有的评论
	* @param @param videoId
	* @param @param page
	* @param @param pageSize
	* @param @return      
	* @return PagedResult
	* @throws
	 */
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
}
