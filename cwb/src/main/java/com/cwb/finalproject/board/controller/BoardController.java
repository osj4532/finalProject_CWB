package com.cwb.finalproject.board.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cwb.finalproject.board.model.BoardListVO;
import com.cwb.finalproject.board.model.BoardService;
import com.cwb.finalproject.board.model.BoardVO;
import com.cwb.finalproject.common.FileUploadUtil;
import com.cwb.finalproject.common.PaginationInfo;
import com.cwb.finalproject.common.ResImgUploadUtility;
import com.cwb.finalproject.common.WebUtility;
import com.cwb.finalproject.confirm.model.ConfirmFileVO;
import com.cwb.finalproject.member.model.MemberService;
import com.cwb.finalproject.member.model.MemberVO;
import com.cwb.finalproject.ranks.model.RanksService;
import com.cwb.finalproject.ranks.model.RanksVO;
import com.cwb.finalproject.reply.model.ReplyService;
import com.cwb.finalproject.reply.model.ReplyVO;
import com.cwb.finalproject.useResource.model.UseResourceVO;

@Controller
@RequestMapping("/Board")
public class BoardController {
	Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired MemberService memberService;
	@Autowired BoardService boardService;
	@Autowired RanksService ranksService;
	@Autowired ResImgUploadUtility upLoadUtil;
	@Autowired ReplyService replyService;
	
	
	@RequestMapping("/BoardAllList.do")
	public String boardAllListshow(Model model) {
		logger.info("전체 게시판 관리 화면 ");
		List<RanksVO> rankList = ranksService.selectAll();
		List<BoardListVO> bdLlist = boardService.selectBoardList();
		logger.info("전체 게시판 관리 게시판 리스트 bdLlist={}",bdLlist);
		 
		
		model.addAttribute("bdLlist", bdLlist);
		model.addAttribute("rankList", rankList);
		return "Board/BoardAllList"; 
	}
	
	@RequestMapping("/BoardListInsert.do")
	public String boardListinsert(@ModelAttribute BoardListVO boardlistVo
			,Model model) {
		logger.info("게시판 리스트 추가 boardlistVo={}",boardlistVo);
		
		
		int cnt =boardService.boardListInsert(boardlistVo);
		
		String msg="",url="/Board/BoardAllList.do";
		if(cnt>0) {
			msg="게시판 리스트 추가 완료";
		}else {
			msg="게시판 리스트 추가 실패"; 
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url); 
		return "common/message";
	}
	
	@RequestMapping("/BoardListDel.do")
	public String boardListDel(@RequestParam int bdlistNo
			,Model model) {
		logger.info("게시판 리스트 삭제 bdlistNo={}",bdlistNo);
		
		
		replyService.deleteAllReplyByBdList(bdlistNo); 
		int cnt =boardService.delectBoardList(bdlistNo);
		
		String msg="",url="/Board/BoardAllList.do";
		if(cnt>0) {
			msg="게시판 리스트 삭제 성공";
		}else {
			msg="게시판 리스트 삭제 실패"; 
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url); 
		return "common/message";
	}
	
	@RequestMapping("/BoardListByNo.do")
	@ResponseBody
	public BoardListVO boardListByno(int bdlistNo) {
		logger.info("ajax 게시판 수정 보여주기 bdlistNo={}",bdlistNo);
		
		BoardListVO boardlistVo = boardService.selectBoardListByNo(bdlistNo);
		
		return boardlistVo;
	}
	
	@RequestMapping("/BoardListEdit.do")
	public String boardListEdit(@ModelAttribute BoardListVO boardListVo
			,Model model){
		logger.info("게시판 리스트 수정 boardListVo={}",boardListVo);
		
		int cnt =boardService.updateBoardList(boardListVo);
		
		String msg="",url="/Board/BoardAllList.do";
		if(cnt>0) {
			msg="게시판 리스트 수정 성공";
		}else {
			msg="게시판 리스트 수정 실패"; 
		}
		 
		model.addAttribute("msg", msg);
		model.addAttribute("url", url); 
		return "common/message";

	} 
	
	@RequestMapping("/BoardTopIns.do") 
	public String boardTopIns(Model model) {
		logger.info("top 게시판 보여주기");
		List<BoardListVO> bdLlist = boardService.selectBoardList();
		
		model.addAttribute("bdLlist", bdLlist);
		
		  
		return "Board/BoardTopIns";
	}
	
	@RequestMapping("/BoardList.do")
	public String boardlist(@ModelAttribute BoardVO boardVo
			,Model model) {
 
		logger.info("게시판 boardVo={}",boardVo);
		PaginationInfo PagingInfo=new PaginationInfo();
		
		
		PagingInfo.setBlockSize(WebUtility.RES_BLOCK_SIZE);
		PagingInfo.setRecordCountPerPage(WebUtility.BLOCK_SIZE);
		PagingInfo.setCurrentPage(boardVo.getCurrentPage());
		 
		boardVo.setRecordCountPerPage(WebUtility.BLOCK_SIZE);
		boardVo.setFirstRecordIndex(PagingInfo.getFirstRecordIndex());
		
		
		List<BoardVO> Blist = boardService.selectBoardByListNo(boardVo);
		BoardListVO blVo = boardService.selectBoardListByNo(boardVo.getBdlistNo());
		
		int total = boardService.boardTotalrecord(boardVo);
		
		PagingInfo.setTotalRecord(total);
		int bdSize= Blist.size();  
		logger.info("게시판 개수 bdSize={}",bdSize);
		
		
		model.addAttribute("bVo", boardVo); 
		model.addAttribute("PagingInfo", PagingInfo); 
		model.addAttribute("Blist", Blist);
		model.addAttribute("blVo", blVo);
		model.addAttribute("bdSize", bdSize);
		
		return "Board/BoardList"; 
	}
	
	@RequestMapping(value = "/BoardWrite.do",method = RequestMethod.GET)
	public String boardWriteGet(@RequestParam int bdlistNo, Model model) {
		BoardListVO blVo = boardService.selectBoardListByNo(bdlistNo);
		
		model.addAttribute("blVo", blVo);
		
		return "Board/BoardWrite";
	}
	
	@RequestMapping(value = "/BoardWrite.do",method = RequestMethod.POST)
	public String boardWritePost(@ModelAttribute BoardVO boardVo,
			HttpServletRequest request, Model model,HttpSession session) {
		logger.info("게시판 글작성 boardVo={}",boardVo);
		int memNo = (Integer)session.getAttribute("memNo");
		boardVo.setMemNo(memNo);
		
		List<Map<String, Object>> fileMap = upLoadUtil.fileUpload(request, upLoadUtil.BOARD_UPLOAD);
		
		String fileName= "", originalFileName="";
		Long fileSize= 0L;
		
		for (Map<String, Object> map : fileMap) {
			fileName = (String) map.get("fileName");
			fileSize = (Long)map.get("fileSize");
			originalFileName = (String) map.get("originalFileName");
		}
		boardVo.setBoardFilename(fileName);  
		boardVo.setBoardFilesize(fileSize);
		boardVo.setBoardOriginalfilename(originalFileName);
		int cnt = boardService.insertBoard(boardVo);
		
		String msg = "",url="/resources/close.do";
		if(cnt>0) { 
			msg="글쓰기 성공";
		}else { 
			msg="글쓰기 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "common/message";
	}
	
	@RequestMapping(value = "/BoardEdit.do", method = RequestMethod.GET)
	public String boardEditget(@RequestParam int boardNo,Model model) {
		logger.info("게시글 수정 boardno={}",boardNo);
		
		BoardVO bVo= boardService.selectboard(boardNo);
		
		model.addAttribute("bVo", bVo);
		
		return "Board/BoardEdit";
	}
	
	@RequestMapping(value = "/BoardEdit.do", method=RequestMethod.POST)
	public String boardEditpost(@ModelAttribute BoardVO boardVo,Model model,
			HttpServletRequest request,@RequestParam String oldFile) {
		logger.info("게시글 수정 BoardVO={},oldFile={}",boardVo,oldFile);
		
		List<Map<String, Object>> fileMap = upLoadUtil.fileUpload(request, upLoadUtil.BOARD_UPLOAD);
		
		String fileName= "", originalFileName="";
		Long fileSize= 0L;
		for (Map<String, Object> map : fileMap) {
			fileName = (String) map.get("fileName");
			fileSize = (Long)map.get("fileSize");
			originalFileName = (String) map.get("originalFileName");
		}
		boardVo.setBoardFilename(fileName);  
		boardVo.setBoardFilesize(fileSize);
		boardVo.setBoardOriginalfilename(originalFileName);
		
		int cnt = boardService.updateboard(boardVo);
		
		String msg="",url="/resources/close.do";
		if(cnt>0) {  
			msg="글수정 성공"; 
			if(fileName!=null && !fileName.isEmpty()) {
				String path =upLoadUtil.getUploadPath(request,upLoadUtil.BOARD_UPLOAD);
				File file = new File(path,oldFile);
				if(file.exists()) {
					boolean bool = file.delete();
					logger.info("변경 후 기존 파일 삭제 결과={}",bool);
				} 
			}
		}else { 
			msg="글수정 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "common/message";
	}
	
	@RequestMapping("/BoardDel.do")
	public String boardDelete(@RequestParam int boardNo,Model model
			,HttpServletRequest request) {
		logger.info("게시판 삭제 boardNo={}",boardNo);
		
		BoardVO bVo= boardService.selectboard(boardNo);
		replyService.deletereplyByBoardno(boardNo);
		int cnt =boardService.deleteboard(boardNo);
		
		
		String msg="",url="/Board/BoardList.do?bdlistNo="+bVo.getBdlistNo();
		if(cnt>0) {  
			msg="글삭제 성공"; 
				String path =upLoadUtil.getUploadPath(request,upLoadUtil.BOARD_UPLOAD);
				File file = new File(path,bVo.getBoardFilename());
				if(file.exists()) { 
					boolean bool = file.delete();
					logger.info("삭제 후 기존 파일 삭제 결과={}",bool);
				}   
		}else { 
			msg="글삭제 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		return "common/message";
	}
	
	@RequestMapping("/BoardDetail.do")
	public String boardDetail(@RequestParam int boardNo,Model model,
			HttpSession session,HttpServletResponse response
			,HttpServletRequest request) {
		logger.info("게시글 상세보기");
		String memId = (String)session.getAttribute("memId");
		
		Cookie[] getCookie = request.getCookies();
		String viewbdMem ="";
		if (getCookie != null) {
			for (int i = 0; i < getCookie.length; i++) {
				Cookie ck = getCookie[i];
				String name = ck.getName();
				if(name.equals("viewbdMem"+boardNo)) {
					viewbdMem=ck.getValue();
				}
			}
		}
		if(memId.equals(viewbdMem)) { 
			logger.info("이미 조회함");
		}else {
			int upcount = boardService.updateReadCount(boardNo);
			if (upcount>0) {
				Cookie setCookie = new Cookie("viewbdMem"+boardNo, memId); 
				setCookie.setMaxAge(60*60*24); // 기간을 하루로 지정
				response.addCookie(setCookie);
			}
		}
		BoardVO BVo= boardService.selectboard(boardNo);
		BoardListVO blVo = boardService.selectBoardListByNo(BVo.getBdlistNo());
		MemberVO mVo =  memberService.selectByMemNotoVo(BVo.getMemNo());
		List<ReplyVO> reList= replyService.selectReplyByNo(boardNo);
		
		
		String memName =mVo.getMemName();
		model.addAttribute("reList", reList); 
		model.addAttribute("blVo", blVo);  
		model.addAttribute("BVo", BVo);
		model.addAttribute("memName", memName);
		  
		return "Board/BoardDetail";
	}
	
	@RequestMapping("/upcommend.do")
	@ResponseBody
	public int upCommend(@RequestParam int boardNo
			,HttpSession session,HttpServletResponse response
			,HttpServletRequest request) {
		
		String memId = (String)session.getAttribute("memId");
		Cookie[] getCookie = request.getCookies();
		String commendbdMem ="";
		if (getCookie != null) {
			for (int i = 0; i < getCookie.length; i++) {
				Cookie ck = getCookie[i];
				String name = ck.getName();
				if(name.equals("commendbdMem"+boardNo)) {
					commendbdMem=ck.getValue();
				}
			}
		}
		if(memId.equals(commendbdMem)) { 
			logger.info("이미 추천함");
		}else {
			int upcommend =boardService.updateCommend(boardNo);
			if (upcommend>0) {
				Cookie setCookie = new Cookie("commendbdMem"+boardNo, memId); 
				setCookie.setMaxAge(60*60*24); // 기간을 하루로 지정
				response.addCookie(setCookie);
			}
		}
		
		BoardVO BVo= boardService.selectboard(boardNo);
		
		return BVo.getBoardRecommend();
	}
	
	@RequestMapping("/download.do")
	public ModelAndView download(@ModelAttribute BoardVO vo,HttpServletRequest request ) {
		logger.info("첨부파일 다운로드 하기 vo = {}",vo);
		
		String path = upLoadUtil.getUploadPath(request, upLoadUtil.BOARD_UPLOAD);
		 
		logger.info("path = {}",path);
		File file = new File(path,vo.getBoardFilename());
		File file1 = new File(vo.getBoardOriginalfilename());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("downFile",file);
		map.put("downFileName",file1);
		
		logger.info("첨부파일 다운로드 하기 map.size = {}",map.size());
		
		ModelAndView mav = new ModelAndView("downloadView",map);
		return mav;
	}
	
	
}

