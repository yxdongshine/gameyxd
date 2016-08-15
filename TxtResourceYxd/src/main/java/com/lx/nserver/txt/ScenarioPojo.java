package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 剧情对话内容
 **/
public class ScenarioPojo {
	
	public ScenarioPojo() {
	}
	
	/** 剧本对话编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 说话NPC的名称 **/
	@TxtString
	private String NPCName;
	
	/** 说话NPC的头像 **/
	@TxtString
	private String NPCHeadPortrait;
	
	/** NPC说话内容 **/
	@TxtString
	private String Language;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getNPCName() {
		return NPCName;
	}
	
	public void setNPCName(String _NPCName) {
		NPCName = _NPCName;
	}
	
	public String getNPCHeadPortrait() {
		return NPCHeadPortrait;
	}
	
	public void setNPCHeadPortrait(String _NPCHeadPortrait) {
		NPCHeadPortrait = _NPCHeadPortrait;
	}
	
	public String getLanguage() {
		return Language;
	}
	
	public void setLanguage(String _Language) {
		Language = _Language;
	}
	
}