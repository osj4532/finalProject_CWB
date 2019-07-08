package com.cwb.finalproject.resources.model;

import java.util.List;
import java.util.Map;

public interface ResourcesService {
	int insertResType(Map<String, String> map);
	List<RestypeVO> selectResType();
	int delResType(int typeNo);
	String selectResTypeByNo(int typeNo);
	int insertRes(ResourcesVO resourcesVo);
	List<List<ResourcesVO>> selectAllRes();
}
