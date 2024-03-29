package com.cwb.finalproject.confirm.model;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmServiceImpl implements ConfirmService{
	@Autowired
	private ConfirmDAO confirmDAO;

	private Logger logger = LoggerFactory.getLogger(ConfirmServiceImpl.class);
	
	@Transactional
	public int insertDoc(ConfirmVO vo, List<Map<String, Object>> fileList) {
		int cnt = 0;
		try {
		
			cnt = confirmDAO.insertDoc(vo);
			logger.info("문서 내용등록 결과 cnt = ", cnt);
			
			if(vo.getCfFile() == "Y") {
				
				logger.info("매개변수 List사이즈 = {}", fileList.size());
				
				for(Map<String, Object> map : fileList) {
					String fileName = (String)map.get("fileName");
					String originalFileName = (String)map.get("originalFileName");
					long fileSize = (Long)map.get("fileSize");
					
					logger.info("파일이름 = {}", fileName);
					
					ConfirmFileVO cfFileVo = new ConfirmFileVO();
					cfFileVo.setFileName(fileName);
					cfFileVo.setFileOriginalName(originalFileName);
					cfFileVo.setFileSize(fileSize);
					cfFileVo.setCfNo(vo.getCfNo());
					
					logger.info("파일이름vo = {}", cfFileVo);
					
					cnt = confirmDAO.insertDocFile(cfFileVo);
					logger.info("문서 파일등록 결과 cnt = {}", cnt);
				}
			}
		
		}catch (RuntimeException e) {
			e.printStackTrace();
			cnt = -1;
		}
		
		return cnt;
	}
	
	public int countWBList(Map<String, Integer> map) {
		return confirmDAO.countWBList(map);
	}
	// 대기, 반려 문서 조회
	public List<Map<String, Object>> selectWaitOrBackList(Map<String,Integer> map){
		return confirmDAO.selectWaitOrBackList(map);
		//map key => memNo, state = 1 or state = 3
	}
	public int countOkList(int ranksNo) {
		return confirmDAO.countOkList(ranksNo);
	}
	// 완료 문서 조회
	public List<Map<String, Object>> selectOkList(Map<String,Integer> map){
		return confirmDAO.selectOkList(map);
	}
	public int countTmpList(int memNo) {
		return confirmDAO.countTmpList(memNo);
	}
	// 임시 저장 문서 조회
	public List<Map<String, Object>> selectTmpList(Map<String,Integer> map){
		return confirmDAO.selectTmpList(map);
	}
	
	public ConfirmVO confirmDetail(int cfNo) {
		return confirmDAO.confirmDetail(cfNo);
	}
	
	public List<ConfirmFileVO> selectDocFiles(int cfNo){
		return confirmDAO.selectDocFiles(cfNo);
	}
	
	public int deleteDocFile(String fileName) {
		return confirmDAO.deleteDocFile(fileName);
	}
	
	public int checkFile(int cfNo) {
		int cnt = confirmDAO.selectFileCount(cfNo);
		logger.info("저장된 파일 개수 = {}",cnt);
		if(cnt == 0) {
			cnt = confirmDAO.updateCFFileN(cfNo);
			logger.info("문서 파일여부 update 결과 cnt = {}",cnt);
		}
		
		return cnt;
	}
	
	@Transactional
	public int updateConfirm(ConfirmVO vo, List<Map<String, Object>> fileList) {
		int cnt = 0;
		try {
		
			cnt = confirmDAO.updateConfirm(vo);
			logger.info("문서 내용수정 결과 cnt = ", cnt);
			
			if(vo.getCfFile() == "Y") {
				logger.info("매개변수 List사이즈 = {}", fileList.size());
				
				for(Map<String, Object> map : fileList) {
					String fileName = (String)map.get("fileName");
					String originalFileName = (String)map.get("originalFileName");
					long fileSize = (Long)map.get("fileSize");
					
					logger.info("파일이름 = {}", fileName);
					
					ConfirmFileVO cfFileVo = new ConfirmFileVO();
					cfFileVo.setFileName(fileName);
					cfFileVo.setFileOriginalName(originalFileName);
					cfFileVo.setFileSize(fileSize);
					cfFileVo.setCfNo(vo.getCfNo());
					
					logger.info("파일이름vo = {}", cfFileVo);
					
					cnt = confirmDAO.insertDocFile(cfFileVo);
					logger.info("문서 파일등록 결과 cnt = ", cnt);
				}
			}
		}catch (RuntimeException e) {
			cnt = -1;
		}
		
		return cnt;
	}
	
	public int deleteConfirm(int cfNo) {
		return confirmDAO.deleteConfirm(cfNo);
	}
	
	public int deleteDocFileAll(int cfNo) {
		return confirmDAO.deleteDocFileAll(cfNo);
	}
	
	public int docBack(int cfNo) {
		return confirmDAO.docBack(cfNo);
	}
	
	public int docOk(int cfNo) {
		return confirmDAO.docOk(cfNo);
	}
	
	public int docOkComplete(int cfNo) {
		return confirmDAO.docOkComplete(cfNo);
	}
	
	public List<Map<String, Object>> indexConfirmWaitList(int memNo){
		return confirmDAO.indexConfirmWaitList(memNo);
	}
	
	public List<Map<String, Object>> recentConfirmList(int memNo){
		return confirmDAO.recentConfirmList(memNo);
	}
}
