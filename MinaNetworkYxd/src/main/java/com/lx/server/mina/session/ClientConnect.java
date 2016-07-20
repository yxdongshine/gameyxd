/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.session;

public class ClientConnect extends MinaConnect implements ICleintConnect {
	private int gameId;
	private int sessId;
	private long playerId;
	private String userName;
	public int msgFreq;
	public long lastSameMsgTime;
	public int lastMsg;
	private int state;
	private int belongServer;

	public int requestFreqCheck(long nowMill, int backId, int cmd) {
		int msg = (backId << 16) + cmd;
		if (msg == this.lastMsg) {
			if (this.lastSameMsgTime == 0L) {
				this.lastSameMsgTime = nowMill;
			}
			if (nowMill - this.lastSameMsgTime < 5000L) {
				this.msgFreq += 1;
			} else {
				this.lastSameMsgTime = nowMill;
				this.msgFreq = 0;
			}
			if (this.msgFreq >= 100) {
				this.lastSameMsgTime = nowMill;
				this.msgFreq = 0;
				return -1;
			}
		} else {
			this.lastMsg = msg;
			this.msgFreq = 0;
			this.lastSameMsgTime = 0L;
		}
		return 1;
	}

	public long getPlayerId() {
		return this.playerId;
	}

	public void setPlayerId(long onlyId) {
		this.playerId = onlyId;
	}

	public int getSessId() {
		return this.sessId;
	}

	public void setSessId(int sessId) {
		this.sessId = sessId;
	}

	public int getGameId() {
		return this.gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public boolean isPlaying() {
		return (this.state == 2);
	}

	public void play() {
		this.state = 2;
	}

	public boolean isLogin() {
		return (this.state == 1);
	}

	public void login() {
		this.state = 1;
	}

	public void setBelongServer(int belongServer) {
		this.belongServer = belongServer;
	}

	public int getBelongServer() {
		return this.belongServer;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}