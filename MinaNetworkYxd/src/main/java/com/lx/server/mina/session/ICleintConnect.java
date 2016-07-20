/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.session;

public abstract interface ICleintConnect extends IConnect {
	public abstract int getGameId();

	public abstract void setSessId(int paramInt);

	public abstract int getSessId();

	public abstract void setGameId(int paramInt);

	public abstract long getPlayerId();

	public abstract void setPlayerId(long paramLong);

	public abstract String getUserName();

	public abstract void setUserName(String paramString);

	public abstract int requestFreqCheck(long paramLong, int paramInt1,
			int paramInt2);

	public abstract boolean isPlaying();

	public abstract void play();

	public abstract boolean isLogin();

	public abstract void login();

	public abstract int getBelongServer();

	public abstract void setBelongServer(int paramInt);
}