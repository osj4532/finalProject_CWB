package com.cwb.finalproject.useResource.model;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UseResourceDAOMybatis implements UseResourceDAO{
	private String namespace="config.mybatis.mapper.oracle.resScheduler.";
	
	
	@Autowired SqlSessionTemplate sqlSession;
	
	@Override
	public List<UseResourceVO> selectMyUseRes(UseResourceVO useResourceVo) {
		return sqlSession.selectList(namespace+"selectMyUseRes", useResourceVo);
	}

	@Override
	public List<UseResourceVO> selectMyNotUseRes(int memNo) {
		return sqlSession.selectList(namespace+"selectMyNotUseRes", memNo);
	}

	@Override
	public List<UseResourceVO> selectMyNotWaitRes(int memNo) {
		return sqlSession.selectList(namespace+"selectMyNotWaitRes", memNo);
	}

	@Override
	public List<UseResourceVO> selectMyNotRefuseRes(int memNo) {
		return sqlSession.selectList(namespace+"selectMyNotRefuseRes", memNo);
	}

	@Override
	public int delUseResSchedule(int reservNo) {
		return sqlSession.delete(namespace+"delUseResSchedule",reservNo);
	}

	@Override
	public int updateUseResSchedule(int reservNo) {
		return sqlSession.update(namespace+"updateUseResSchedule",reservNo);
	}

	@Override
	public int selectUseRestotalCount(UseResourceVO useResourceVo) {
		return sqlSession.selectOne(namespace+"selectUseRestotalCount",useResourceVo);
	}

	@Override
	public List<UseResourceVO> selectAllUseRes(UseResourceVO useResourceVo) {
		return sqlSession.selectList(namespace+"selectAllUseRes", useResourceVo);
	}

	@Override
	public int selectAllUseRestotalCount(UseResourceVO useResourceVo) {
		return sqlSession.selectOne(namespace+"selectAllUseRestotalCount",useResourceVo);
	}

	@Override
	public List<UseResourceVO> selectAllNotUseRes(UseResourceVO useResourceVo) {
		return sqlSession.selectList(namespace+"selectAllNotUseRes",useResourceVo);
	}
 
	@Override
	public List<UseResourceVO> selectAllNotWaitRes(UseResourceVO useResourceVo) {
		return sqlSession.selectList(namespace+"selectAllNotWaitRes",useResourceVo);
	}

	@Override
	public List<UseResourceVO> selectAllNotRefuseRes(UseResourceVO useResourceVo) {
		return sqlSession.selectList(namespace+"selectAllNotRefuseRes",useResourceVo);
	}

	@Override
	public int selectAllNotUseRestotalCount(UseResourceVO useResourceVo) {
		return sqlSession.selectOne(namespace+"selectAllNotUseRestotalCount", useResourceVo);
	}

	@Override
	public int selectAllNotWaitRestotalCount(UseResourceVO useResourceVo) {
		return sqlSession.selectOne(namespace+"selectAllNotWaitRestotalCount", useResourceVo);
	}

	@Override
	public int selectAllNotRefuesRestotalCount(UseResourceVO useResourceVo) {
		return sqlSession.selectOne(namespace+"selectAllNotRefuesRestotalCount", useResourceVo);
	}

	@Override
	public List<UseResourceVO> selectResRow(int memNo) {
		return sqlSession.selectList(namespace+"selectResRow", memNo);
	}



	
	
}
