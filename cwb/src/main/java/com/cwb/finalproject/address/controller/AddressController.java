package com.cwb.finalproject.address.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cwb.finalproject.address.model.EmailService;
import com.cwb.finalproject.address.model.EmailVO;
import com.cwb.finalproject.common.FileUploadUtil;
import com.cwb.finalproject.common.PaginationInfo;
import com.cwb.finalproject.common.WebUtility;
import com.cwb.finalproject.dept.model.DeptService;
import com.cwb.finalproject.dept.model.DeptVO;
import com.cwb.finalproject.member.model.MemberService;
import com.fasterxml.jackson.annotation.JsonFormat;

@Controller
@RequestMapping("/address")
public class AddressController {

	private Logger logger = LoggerFactory.getLogger(AddressController.class);
	@Autowired
	private DeptService deptService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private FileUploadUtil fileUtil;
	@Autowired
	private EmailSender emailSender;
	@Autowired
	private EmailService emailService;
	
	@RequestMapping("/privateAddr.do")
	public String showPrivAddr() {
		logger.info("개인 주소록 보여주기");
		return "address/privateAddr";
	}
	
	@RequestMapping("/companyAddr.do")
	public String showCompAddr(Model model) {
		logger.info("회사 주소록 보여주기");
		
		List<DeptVO> depts = deptService.selectAll();
		List<Map<String, Object>> members = memberService.selectOrSearch(null);
		logger.info("부서 사이즈={}, member사이즈={}",depts.size(),members.size());
		
		model.addAttribute("depts", depts);
		model.addAttribute("members", members);
		
		return "address/companyAddr";
	}
	
	@RequestMapping("/companySearch.do")
	@ResponseBody
	public List<Map<String, Object>> searchMem(@RequestParam String keyword){
		logger.info("검색어 = {}", keyword);
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("keyword",keyword);
		
		List<Map<String,Object>> members = memberService.selectOrSearch(map);
		for(Map<String, Object> mem : members) {
			//Timestamp는 json으로 바꾸지 못함
			mem.put("MEM_JOINDATE", null);
		}
		logger.info("검색 결과 리스트 사이즈 = {}",members.size());
		return members;
	}
	
	@RequestMapping("/memInfo.do")
	@ResponseBody
	public Map<String, Object> memInfo(@RequestParam int memNo){
		logger.info("멤버 정보 보여주기 memNo = {}", memNo);
		Map<String, Object> member = memberService.selectByNo(memNo);
		String memberPic = (String)member.get("MEM_FILENAME"); 
		if(memberPic == null || memberPic.isEmpty()) {
			member.put("MEM_FILENAME","user.png");
		}
		member.put("MEM_JOINDATE",null);
		return member;
	}
	
	@RequestMapping(value="/sendEmail.do", method = RequestMethod.GET)
	public String sendEmail_get(@RequestParam(required = false) String email, 
			HttpSession session, Model model) {
		logger.info("이메일 보내기 화면 보여주기 매개변수 = {}", email);
		int memNo = (Integer)session.getAttribute("memNo");
		Map<String, Object> member = memberService.selectByNo(memNo);
		
		if((String)member.get("MEM_EMAIL1") != null) {
			String senderMail = member.get("MEM_EMAIL1")+"@"+member.get("MEM_EMAIL2");
			model.addAttribute("sender", senderMail);
		}
		
		model.addAttribute("memNo", memNo);
		model.addAttribute("receiver", email);
		
		return "address/sendEmail";
	}
	
	@RequestMapping(value="/sendEmailProc.do")
	public String sendEmail_post(@ModelAttribute EmailVO vo,
			@RequestParam MultipartFile file,HttpServletRequest request) {
		logger.info("보낸 메일 저장하기 vo = {}",vo);
		
		if(file != null && !file.isEmpty()) {
			Map<String, Object> map = fileUtil.singleUpload(request, FileUploadUtil.MAIL_UPLOAD);
			vo.setMailFileName((String)map.get("fileName"));
			vo.setMailFileSize((Long)map.get("fileSize"));
			vo.setMailOriginalFileName((String)map.get("originalFileName"));
			logger.info("파일 넣은 후 vo = {}",vo);
		}
		
		int cnt = emailService.insertEmail(vo);
		logger.info("DB에 저장 결과 cnt = {}",cnt);
		
		String savePath = fileUtil.getUploadPath(request, FileUploadUtil.MAIL_UPLOAD);
		
		try {
			emailSender.init();
			emailSender.addMsg(vo.getMailContent());
			emailSender.addFile(savePath, vo.getMailFileName(), vo.getMailOriginalFileName());
			emailSender.sendEmail(vo.getMailTitle(), vo.getMailSenAddr(), vo.getMailRevAddr());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/address/emailList.do";
	}
	
	@RequestMapping("/emailList.do")
	public String emailList(@ModelAttribute EmailVO emailVo, HttpSession session, Model model) {
		int memNo = (Integer)session.getAttribute("memNo");
		logger.info("보낸 메일 리스트 보기 memNo={}",memNo);
		logger.info("현재 페이지 ={}",emailVo.getCurrentPage());
		
		emailVo.setMailRev(memNo);
		
		PaginationInfo pageInfo = new PaginationInfo();
		pageInfo.setBlockSize(10);
		pageInfo.setRecordCountPerPage(5);
		pageInfo.setCurrentPage(emailVo.getCurrentPage());
		
		emailVo.setRecordCountPerPage(5);
		emailVo.setFirstRecordIndex(pageInfo.getFirstRecordIndex());
		logger.info("셋팅후 ={}",emailVo);
		
		
		List<EmailVO> list = emailService.sendMailList(emailVo);
		logger.info("리스트 크기 ={}",list.size());
		
		int totalRec = 0;
		totalRec = emailService.totalCount(memNo);
		logger.info("총 레코드 개수 ={}",totalRec);
		
		pageInfo.setTotalRecord(totalRec);
		
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		
		return "address/emailList";
	}
	
	@RequestMapping("/emailDetail.do")
	public String emailDetail(@RequestParam int mailNo, Model model) {
		logger.info("보낸 메일 상세 보기 매개변수 mailNo={}",mailNo);
		EmailVO vo = emailService.selectDetail(mailNo);
		
		int count = emailService.countNext(mailNo);
		if(count > 0) {
			int next = emailService.selectNext(mailNo);
			model.addAttribute("next",next);
		}

		count = emailService.countPre(mailNo);
		if(count > 0) {
			int pre = emailService.selectPre(mailNo);
			model.addAttribute("pre",pre);
		}
		
		model.addAttribute("vo",vo);
		
		return "address/emailDetail";
	}
	
	@RequestMapping("/emailDelete.do")
	@ResponseBody
	public int emailDelete(@RequestParam(value = "sel[]") List<Integer> sel, HttpServletRequest request) {
		logger.info("메일 삭제 처리하기 삭제 할 개수 = {}", sel.size());
		int cnt = -1;
		if(sel.size() != 0) {
			for(int i : sel) {
				EmailVO vo = emailService.selectDetail(i);
				if(vo.getMailFileName() !=null) {
					String path = fileUtil.getUploadPath(request, FileUploadUtil.MAIL_UPLOAD);
					File file = new File(path, vo.getMailFileName());
					if(file.exists()) {
						file.delete();
					}
				}
				cnt = emailService.deleteMail(i);
			}
		}
		return cnt;
	}
	
	@RequestMapping("/sendMessage.do")
	public String sendMessage(@RequestParam(required = false) int memNo, Model model) {
		logger.info("쪽지 보내기 화면 보여주기 매개변수 = {}", memNo);
		
		model.addAttribute("memNo", memNo);
		return "address/sendMessage";
	}
	
	@RequestMapping("/selAddr.do")
	public String selAddr() {
		logger.info("연락처 선택창 보여주기");
		return "address/addrSel";
	}
}
