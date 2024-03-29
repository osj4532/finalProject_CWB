package com.cwb.finalproject.message.model;

import java.util.List;
import java.util.Map;

public interface MessageService {

	public int insertMsg(MessageVO vo, int[] memNos);
	public List<Map<String, Object>> selectRevMsg(Map<String, Object> map);
	public int countRevMsg(Map<String, Object> map);
	public List<Map<String, Object>> selectSenMsg(Map<String, Object> map);
	public int countSenMsg(Map<String, Object> map);
	public int readCheck(int msgrevNo);
	public Map<String, Object> selectByNo(Map<String, Object> map);
	public Map<String, Object> selectByNoSen(Map<String, Object> map);
	
	public int msgPre(Map<String, Object> map);
	public int countMsgPre(Map<String, Object> map);
	public int msgNext(Map<String, Object> map);
	public int countMsgNext(Map<String, Object> map);
	public int senMsgPre(Map<String, Object> map);
	public int countSenMsgPre(Map<String, Object> map);
	public int senMsgNext(Map<String, Object> map);
	public int countSenMsgNext(Map<String, Object> map);
	
	public String selectMsgDel(int msgrevNo);
	public String selectMsgRevDel(int msgrevNo);
	public int msgRevChangeY(int msgrevNo);
	public int msgChangeY(int msgrevNo);
	public int selectMsgNo(int msgrevNo);
	public int deleteMsgRev(int msgrevNo);
	public int countMsgRev(int msgrevNo);
	public int deleteMsg(int msgrevNo);
	
	public List<Map<String, Object>> indexListMsg(int memNo);
}
