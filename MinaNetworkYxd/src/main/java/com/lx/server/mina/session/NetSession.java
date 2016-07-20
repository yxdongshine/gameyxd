/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.session;

import com.google.protobuf.Message;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class NetSession
{
  protected IoSession session;
  private long sessionid;
  int size = 0;

  private ConcurrentHashMap<Integer, Integer> securityHashMap = new ConcurrentHashMap();

  public IoSession getSession() {
    return this.session;
  }

  public long getSessionid() {
    return this.sessionid;
  }

  public SocketAddress getRomoteAddress() {
    return this.session.getRemoteAddress();
  }

  public void setSession(IoSession session) {
    this.session = session;
    this.sessionid = session.getId();
  }

  public void send(Message p) {
    if ((this.session == null) || (!(this.session.isConnected())))
      return;
    this.session.write(p);
  }

  public void send(byte[] byteArray)
  {
    if ((byteArray != null) && (this.session != null)) {
      IoBuffer io = IoBuffer.wrap(byteArray);
      this.session.write(io);
      io.free();
    }
  }

  public void exit()
  {
    if ((this.session != null) && (this.session.isConnected()))
      this.session.close(true);
    cleanClientKey();
  }

  public void putClientKey(int id, int val)
  {
    this.securityHashMap.put(Integer.valueOf(id), Integer.valueOf(val));
    this.size += 1;
  }

  public boolean compareClientKey(int id, int val)
  {
    Integer v = (Integer)this.securityHashMap.get(Integer.valueOf(id));
    if ((v != null) && (v.intValue() == val)) {
      this.size -= 1;
      if (this.size < 0) {
        this.size = 0;
      }
      this.securityHashMap.remove(Integer.valueOf(id));
      return true;
    }
    return false;
  }

  public void cleanClientKey() {
    this.securityHashMap.clear();
    this.size = 0;
  }

  public int getSize() {
    return this.size;
  }

  public void setSize(int size) {
    this.size = size;
  }
}