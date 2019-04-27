package com.test.mapper;

import java.util.List;

import com.test.pojo.SearchRecords;
import com.test.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> getHotWords();
}