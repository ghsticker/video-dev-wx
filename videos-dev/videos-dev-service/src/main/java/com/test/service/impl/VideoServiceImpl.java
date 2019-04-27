package com.test.service.impl;

import java.util.Date;
import java.util.List;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.mapper.CommentsMapper;
import com.test.mapper.CommentsMapperCustom;
import com.test.mapper.SearchRecordsMapper;
import com.test.mapper.UsersLikeVideosMapper;
import com.test.mapper.UsersMapper;
import com.test.mapper.VideosMapper;
import com.test.mapper.VideosMapperCustom;
import com.test.pojo.Comments;
import com.test.pojo.SearchRecords;
import com.test.pojo.UsersLikeVideos;
import com.test.pojo.Videos;
import com.test.pojo.vo.CommentsVO;
import com.test.pojo.vo.VideosVO;
import com.test.service.VideoService;
import com.test.utils.PagedResult;
import com.test.utils.TimeAgoUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @Description
 * @author ghsticker 
 * 2019年3月6日
 */
@Service
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideosMapper videoMapper;

	@Autowired
	private VideosMapperCustom videosMapperCustom;

	@Autowired
	private SearchRecordsMapper searchRecordsMapper;

	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;

	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	private CommentsMapper commentsMapper;
	@Autowired
	private CommentsMapperCustom commentsMapperCustom;

	@Autowired
	private Sid sid;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveVideo(Videos video) {
		String videoId = sid.nextShort();
		video.setId(videoId);

		videoMapper.insertSelective(video);
		return videoId;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateVideo(String videoId, String coverPath) {

		Videos video = new Videos();

		video.setId(videoId);
		video.setCoverPath(coverPath);

		videoMapper.updateByPrimaryKeySelective(video);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

		String desc = video.getVideoDesc();
		String userId = video.getUserId();
		if (isSaveRecord != null && isSaveRecord == 1) {

			SearchRecords record = new SearchRecords();
			String id = sid.nextShort();

			record.setId(id);
			record.setContent(desc);

			searchRecordsMapper.insert(record);
		}

		PageHelper.startPage(page, pageSize);
		List<VideosVO> List = videosMapperCustom.queryAllVideos(desc,userId);
		// 包装pageResult
		PageInfo<VideosVO> pageList = new PageInfo<>(List);

		PagedResult pageResult = new PagedResult();
		pageResult.setPage(page);
		pageResult.setTotal(pageList.getPages());
		pageResult.setRows(List);
		pageResult.setRecords(pageList.getTotal());

		return pageResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotWords() {
		return searchRecordsMapper.getHotWords();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
		// 1. 保存用户和视频的喜欢点赞关联关系表
		String likeId = sid.nextShort();
		UsersLikeVideos ulv = new UsersLikeVideos();
		ulv.setId(likeId);
		ulv.setUserId(userId);
		ulv.setVideoId(videoId);
		usersLikeVideosMapper.insert(ulv);

		// 2. 视频喜欢数量累加
		videosMapperCustom.addVideoLikeCount(videoId);

		// 3. 用户受喜欢数量的累加
		usersMapper.addReceiveLikeCount(videoCreaterId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
		// 1. 删除用户和视频的喜欢点赞关联关系表

		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();

		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("videoId", videoId);

		usersLikeVideosMapper.deleteByExample(example);

		// 2. 视频喜欢数量累减
		videosMapperCustom.reduceVideoLikeCount(videoId);

		// 3. 用户受喜欢数量的累减
		usersMapper.reduceReceiveLikeCount(videoCreaterId);

	}
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyFollowVideos(userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVO> list = videosMapperCustom.queryMyLikeVideos(userId);

		PageInfo<VideosVO> pageList = new PageInfo<>(list);

		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());

		return pagedResult;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveComment(Comments comment) {
		String id = sid.nextShort();
		comment.setId(id);
		comment.setCreateTime(new Date());
		commentsMapper.insert(comment);
		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
		
		PageHelper.startPage(page, pageSize);
		List<CommentsVO> list = commentsMapperCustom.queryComments(videoId);
		for (CommentsVO commentsVO : list) {
			String timeAgo = TimeAgoUtils.format(commentsVO.getCreateTime());
			commentsVO.setTimeAgoStr(timeAgo);
		}
		
		PageInfo<CommentsVO> pageList = new PageInfo<>(list);
		
		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());
		
		return grid;
	}

}
