package com.cwb.finalproject.board.model;

import java.util.List;

public interface BoardService {
	int boardListInsert(BoardListVO boardListVo);
	List<BoardListVO> selectBoardList();
	int delectBoardList(int bdlistNo);
	BoardListVO selectBoardListByNo(int bdlistNo);
	int updateBoardList(BoardListVO boardlistVo);
	List<BoardVO> selectBoardByListNo(int bdlistNo); 

}
