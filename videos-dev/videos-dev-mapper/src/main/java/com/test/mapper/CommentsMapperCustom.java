package com.test.mapper;


import java.util.List;

import com.test.pojo.Comments;
import com.test.pojo.vo.CommentsVO;
import com.test.utils.MyMapper;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}