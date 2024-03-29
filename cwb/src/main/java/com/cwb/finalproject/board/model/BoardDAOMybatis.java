package com.cwb.finalproject.board.model;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDAOMybatis implements BoardDAO{
	private String namespace = "config.mybatis.mapper.oracle.board.";
	
	@Autowired SqlSessionTemplate sqlSession;
	
	@Override
	public int boardListInsert(BoardListVO boardListVo) {
		return sqlSession.insert(namespace+"boardListInsert", boardListVo);
	}

	@Override
	public List<BoardListVO> selectBoardList() {
		return sqlSession.selectList(namespace+"selectBoardList");
	}

	@Override
	public int delectBoardList(int bdlistNo) {
		return sqlSession.delete(namespace+"delectBoardList", bdlistNo);
	}

	@Override
	public BoardListVO selectBoardListByNo(int bdlistNo) {
		return sqlSession.selectOne(namespace+"selectBoardListByNo", bdlistNo);
	}

	@Override
	public int updateBoardList(BoardListVO boardlistVo) {
		return sqlSession.update(namespace+"updateBoardList", boardlistVo);
	}

	@Override
	public List<BoardVO> selectBoardByListNo(BoardVO boradVo) {
		return sqlSession.selectList(namespace+"selectBoardByListNo", boradVo);
	}

	@Override
	public int insertBoard(BoardVO boardVo) {
		return sqlSession.insert(namespace+"insertBoard", boardVo);
	}

	@Override
	public BoardVO selectboard(int boardNo) {
		return sqlSession.selectOne(namespace+"selectboard", boardNo);
	}

	@Override
	public int updateboard(BoardVO boardVo) {
		return sqlSession.update(namespace+"updateboard", boardVo);
	}

	@Override
	public int deleteboard(int boardNo) {
		return sqlSession.delete(namespace+"deleteboard", boardNo);
	}

	@Override
	public int deleteboardByListNo(int bdlistNo) {
		return sqlSession.delete(namespace+"deleteboardByListNo", bdlistNo);
	}

	@Override
	public int updateReadCount(int boardNo) {
		return sqlSession.update(namespace+"updateReadCount", boardNo);
	}

	@Override
	public int updateCommend(int boardNo) {
		return sqlSession.update(namespace+"updateCommend", boardNo);
	}

	@Override
	public int boardTotalrecord(BoardVO boardVo) {
		return sqlSession.selectOne(namespace+"boardTotalrecord", boardVo);
	}   
	
   
}
