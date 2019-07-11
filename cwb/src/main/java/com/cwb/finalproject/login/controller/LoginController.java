package com.cwb.finalproject.login.controller;

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

import com.cwb.finalproject.member.model.MemberService;
import com.cwb.finalproject.member.model.MemberVO;

@Controller
@RequestMapping("/login")
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private MemberService memberService;
	
	@RequestMapping(value="/login.do", method=RequestMethod.GET)
	public void login_get() {
		logger.info("로그인 화면 보여주기");
	}
	
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public String login_post(@ModelAttribute MemberVO memberVo, @RequestParam(required = false) String rememberId,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		logger.info("로그인 처리 파라미터 memberVo={}, rememberId={}", memberVo, rememberId);
		
		int result = memberService.loginCheck(memberVo.getMemId(), memberVo.getMemPwd());
		logger.info("로그인 처리 결과, result = {}", result);
		
		String msg="", url="/login/login.do";
		if(result==MemberService.LOGIN_OK) {
			//로그인 성공
			MemberVO memberVo2 = memberService.selectByUserid(memberVo.getMemId());
			
			//session에 저장
			HttpSession session = request.getSession();
			session.setAttribute("memId", memberVo.getMemId());
			session.setAttribute("memName", memberVo2.getMemName());
			session.setAttribute("memNo", memberVo2.getMemNo());
			session.setAttribute("ranksNo", memberVo2.getRanksNo());
			
			//cookie에 저장
			Cookie ck = new Cookie("ck_memid", memberVo.getMemId());
			ck.setPath("/");
			if(rememberId != null) {	//아이디 저장하기를 체크한 경우
				ck.setMaxAge(100*24*60*60);
				response.addCookie(ck);
			}else {
				ck.setMaxAge(0);	//쿠키 삭제
				response.addCookie(ck);
			}
			
			msg = memberVo2.getMemName()+"님 로그인 되었습니다.";
			url = "/index.do";
		}else if(result==MemberService.PWD_DISAGREE) {
			msg="비밀번호가 일치하지 않습니다.";
		}else if(result==MemberService.ID_NONE) {
			msg="아이디가 존재하지 않습니다.";
		}else {
			msg="로그인 처리 실패";
		}
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url);
		
		return "common/message";
	}
}