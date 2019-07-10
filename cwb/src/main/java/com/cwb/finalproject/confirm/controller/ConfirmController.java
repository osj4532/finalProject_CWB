package com.cwb.finalproject.confirm.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.multipart.MultipartFile;

import com.cwb.finalproject.common.FileUploadUtil;
import com.cwb.finalproject.confirm.model.ConfirmService;
import com.cwb.finalproject.confirm.model.ConfirmVO;
import com.cwb.finalproject.confirmline.controller.ConfirmlineController;
import com.cwb.finalproject.confirmline.model.ConfirmlineService;
import com.cwb.finalproject.document.model.DocFormService;
import com.cwb.finalproject.document.model.DocTypeService;
import com.cwb.finalproject.line.model.LineService;
import com.cwb.finalproject.line.model.LineVO;
import com.cwb.finalproject.member.model.MemberService;

@Controller
@RequestMapping("/document")
public class ConfirmController {
	
	private Logger logger = LoggerFactory.getLogger(ConfirmlineController.class);
	@Autowired
	private DocTypeService docTypeService;
	@Autowired
	private DocFormService docFormService;
	@Autowired
	private LineService lineService;
	@Autowired
	private ConfirmlineService confirmlineService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ConfirmService confirmService;
	@Autowired
	private FileUploadUtil fileUtil;
	
	@RequestMapping(value="/docSel.do", method = RequestMethod.GET)
	public String docSel_get(@RequestParam(required = false, defaultValue = "0") int formNo, 
			@RequestParam(required = false, defaultValue = "0") int regNo, 
			HttpSession session, Model model) {
		session.setAttribute("userNo", 1);
		int userNo = (Integer)session.getAttribute("userNo");
		logger.info("문서양식 및 종류 선택 화면 보여주기 userNo = {}",userNo);
		
		//1 문서 종류, 문서 양식 종류
		List<Map<String, Object>> docTypeList = docTypeService.selectDocType();
		List<Map<String, Object>> docFormList = docFormService.selectDocForm();
		
		
		if(formNo != 0) {
			logger.info("문서 양식 번호 formNo = {}", formNo);
			Map<String, Object> formInfo = docFormService.selectDocFormByNo(formNo);
			model.addAttribute("formInfo",formInfo);
		}
		
		
		logger.info("docTypeList 사이즈 = {}, docFormList 사이즈 = {}", docTypeList.size(), docFormList.size());

		//2 그사람이 등록한 결재라인 목록
		List<LineVO> lineList = lineService.selectAll(userNo);
		
		if(regNo != 0) {
			logger.info("결재라인 번호 regNo = {}", regNo);
			List<Map<String, Object>> clList = confirmlineService.selectAll(regNo);
			model.addAttribute("clList", clList);
		}
		
		model.addAttribute("docTypeList", docTypeList);
		model.addAttribute("docFormList", docFormList);
		model.addAttribute("lineList", lineList);
		
		return "document/docsel";
	}
	
	@RequestMapping(value="/docSel.do", method = RequestMethod.POST)
	public String docSel_post(@ModelAttribute ConfirmVO vo, 
			HttpSession session, Model model) {
		// confirmVO
		// form_no, mem_no, dept_no, reg_no, cf_title, cf_content, cf_state
		// cf_file, cf_tmpstorage, cf_del, cf_order, cf_regdate, cf_okdate
		
		//form_no, mem_no, reg_no, 
		int memNo = (Integer)session.getAttribute("userNo");
		vo.setMemNo(memNo);
		logger.info("문서 작성화면으로 넘기기 매개변수 vo = {}",vo);
		
		Map<String, Object> member = memberService.selectByNo(memNo);
		List<Map<String, Object>> clList = confirmlineService.selectAll(vo.getRegNo());
		Map<String, Object> formInfo = docFormService.selectDocFormByNo(vo.getFormNo());
		
		model.addAttribute("member", member);
		model.addAttribute("clList",clList);
		model.addAttribute("formInfo",formInfo);
		model.addAttribute("today", new Date());
		
		return "document/docreg";
	}
	
	@RequestMapping("/docReg.do")
	public String docReg(HttpServletRequest request, @ModelAttribute ConfirmVO confirmVo, 
			@RequestParam("fileName") MultipartFile[] files, Model model) {
		logger.info("등록처리 confirmVo = {}", confirmVo);
		logger.info("첨부된 파일 크기= {}",files.length);
		
		List<Map<String, Object>> fileList = null;
		if(files.length > 0) { 
			confirmVo.setCfFile("Y"); 
			fileList = fileUtil.multipleUpload(request); 
		}else { 
			confirmVo.setCfFile("N"); 
		}
		
		
		int cnt = confirmService.insertDoc(confirmVo, fileList);
		logger.info("문서 등록 결과 cnt = {}",cnt);
		
		String url = "/document/docSel.do", msg = "";
		if(cnt > 0) {
			msg = "문서 등록 성공!";
			url = "/document/docList.do";
		}else {
			msg = "문서 등록 실패!";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url",url);
		
		return "common/message";
	}
	
	@RequestMapping("/docList.do")
	public String docList(@RequestParam(required = false, defaultValue = "1") int cfState, Model model) {
		logger.info("문서 리스트 보여주기");
		//1. 대기(기안자, 결재순서자)
		
		//2. 완료(등급에 따라, 모든 사용자)
		
		//3. 반려(기안자, 반려자)
		
		//4. 임시저장(기안자)
		
		return "document/doclist";
	}
	
	@RequestMapping("/docDetail.do")
	public String docDetail(@RequestParam int cfNo, Model model) {
		logger.info("문서 자세히 보여주기 문서번호 cfNo={}",cfNo);
		//결재 사인(현재 결재 순서 2)
		//결재 순서 < 2 인 멤버의 사인을 보여준다.
		
		
		return "document/docdetail";
	}
}
