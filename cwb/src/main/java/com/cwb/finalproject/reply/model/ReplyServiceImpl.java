package com.cwb.finalproject.reply.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyServiceImpl implements ReplyService{

	
	@Autowired ReplyDAO replyDao;

	@Override
	public int insertOriginReply(ReplyVO replyVo) {
		return replyDao.insertOriginReply(replyVo);
	}

	@Override
	public List<ReplyVO> selectReplyByNo(int boardNo) {
		return replyDao.selectReplyByNo(boardNo);
	}

	@Override
	public int updateReplyByNo(ReplyVO replyVo) {
		return replyDao.updateReplyByNo(replyVo);
	}

	@Override
	public ReplyVO selectOriginReply(int repNo) {
		return replyDao.selectOriginReply(repNo);
	}

	@Override
	public int insertReply(ReplyVO replyVo) {
		return replyDao.insertReply(replyVo);
	}

	@Override
	public int updateSortNo(ReplyVO replyVo) {
		return replyDao.updateSortNo(replyVo);
	}

	@Override
	public List<ReplyVO> selectReplyGroup(int repNo) {
		return replyDao.selectReplyGroup(repNo);
	}

	@Override
	public int deleteReply(int repNo) {
		return replyDao.deleteReply(repNo);
	}

	@Override
	public int selectReplyMine(int repNo) {
		return replyDao.selectReplyMine(repNo);
	}

	@Override
	public int deleteAllReply(int repNo) {
		return replyDao.deleteAllReply(repNo);
	}

	@Override
	public int deletereplyByBoardno(int boardNo) {
		return replyDao.deletereplyByBoardno(boardNo);
	}

	@Override
	public int deleteAllReplyByBdList(int boardNo) {
		return replyDao.deleteAllReplyByBdList(boardNo);
	}
	
	
	
}
