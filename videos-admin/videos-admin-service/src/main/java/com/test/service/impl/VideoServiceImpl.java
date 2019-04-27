package com.test.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.enums.BGMOperatorTypeEnum;
import com.test.mapper.BgmMapper;
import com.test.mapper.UsersReportMapperCustom;
import com.test.mapper.VideosMapper;
import com.test.mapper.VideosMapperCustom;
import com.test.pojo.Bgm;
import com.test.pojo.BgmExample;
import com.test.pojo.Videos;
import com.test.pojo.vo.Reports;
import com.test.pojo.vo.VideoVo;
import com.test.service.VideoService;
import com.test.utils.JsonUtils;
import com.test.utils.PagedResult;
import com.test.web.utils.ZKCurator;

/**
 * @Description
 * @author ghsticker
 * 2019年3月13日
 */
@Service
public class VideoServiceImpl implements VideoService{

	@Autowired
	private BgmMapper bgmMapper;
	
	@Autowired
	private Sid sid;
	
	@Autowired
	private ZKCurator zkCurator;
	
	@Autowired
	private UsersReportMapperCustom usersReportMapperCustom;
	
	@Autowired
	private VideosMapper videosMapper;
	
	@Autowired
	private VideosMapperCustom videosMapperCustom;
	
	
	@Override
	public PagedResult queryBgmList(Integer page, Integer pageSize) {

		PageHelper.startPage(page, pageSize);
		BgmExample example = new BgmExample();
		List<Bgm> list = bgmMapper.selectByExample(example);
		PageInfo<Bgm> pageList = new PageInfo<>(list);
		
		PagedResult result = new PagedResult();
		result.setPage(page);
		result.setRows(list);
		result.setTotal(pageList.getPages());
		result.setRecords(pageList.getTotal());
		
		return result;
	}
	
	
	@Override
	public void addBgm(Bgm bgm) {
		String bgmId = sid.nextShort();
		bgm.setId(bgmId);
		bgmMapper.insert(bgm);
		Map<String, String> map = new HashMap<>();
		map.put("operType", BGMOperatorTypeEnum.ADD.type);
		map.put("path", bgm.getPath());
		
		zkCurator.sendBgmOperator(bgmId, JsonUtils.objectToJson(map));
	}


	@Override
	public void deleteBgm(String id) {
		Bgm bgm = bgmMapper.selectByPrimaryKey(id);
		bgmMapper.deleteByPrimaryKey(id);
		
		Map<String,String> map = new HashMap<>();
		map.put("operType", BGMOperatorTypeEnum.DELETE.type);
		map.put("path", bgm.getPath());
		zkCurator.sendBgmOperator(id, JsonUtils.objectToJson(map));
	}


	@Override
	public PagedResult queryReportList(Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);

		List<Reports> reportsList = usersReportMapperCustom.selectAllVideoReport();

		PageInfo<Reports> pageList = new PageInfo<Reports>(reportsList);

		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(reportsList);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());

		return grid;
	}


	@Override
	public void updateVideoStatus(String videoId, Integer status) {
		Videos video = new Videos();
		video.setId(videoId);
		video.setStatus(status);
		videosMapper.updateByPrimaryKeySelective(video);
	}


	@Override
	public PagedResult queryVideosList(Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);

		List<VideoVo> videosList = videosMapperCustom.selectAllVideos();

		PageInfo<VideoVo> pageList = new PageInfo<VideoVo>(videosList);

		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(videosList);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());

		return grid;
		
	}


	@Override
	public void deleteVides(String id) {
		videosMapper.deleteByPrimaryKey(id);
		
	}

}

