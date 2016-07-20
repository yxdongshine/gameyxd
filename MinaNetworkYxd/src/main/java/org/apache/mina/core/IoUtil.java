/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

public class IoUtil
{
  private static final IoSession[] EMPTY_SESSIONS = new IoSession[0];

  public static List<WriteFuture> broadcast(Object message, Collection<IoSession> sessions)
  {
    List answer = new ArrayList(sessions.size());
    broadcast(message, sessions.iterator(), answer);
    return answer;
  }

  public static List<WriteFuture> broadcast(Object message, Iterable<IoSession> sessions)
  {
    List answer = new ArrayList();
    broadcast(message, sessions.iterator(), answer);
    return answer;
  }

  public static List<WriteFuture> broadcast(Object message, Iterator<IoSession> sessions)
  {
    List answer = new ArrayList();
    broadcast(message, sessions, answer);
    return answer;
  }

  public static List<WriteFuture> broadcast(Object message, IoSession[] sessions)
  {
    if (sessions == null) {
      sessions = EMPTY_SESSIONS;
    }

    List answer = new ArrayList(sessions.length);
    if (message instanceof IoBuffer) {
      for (IoSession s : sessions)
        answer.add(s.write(((IoBuffer)message).duplicate()));
    }
    else {
      for (IoSession s : sessions) {
        answer.add(s.write(message));
      }
    }
    return answer;
  }

  private static void broadcast(Object message, Iterator<IoSession> sessions, Collection<WriteFuture> answer) {
    if (message instanceof IoBuffer)
      while (sessions.hasNext()) {
        IoSession s = (IoSession)sessions.next();
        answer.add(s.write(((IoBuffer)message).duplicate()));
      }
    else
      do {
        IoSession s = (IoSession)sessions.next();
        answer.add(s.write(message));
      }
      while (sessions.hasNext());
  }

  public static void await(Iterable<? extends IoFuture> futures)
    throws InterruptedException
  {
    for (IoFuture f : futures)
      f.await();
  }

  public static void awaitUninterruptably(Iterable<? extends IoFuture> futures)
  {
    for (IoFuture f : futures)
      f.awaitUninterruptibly();
  }

  public static boolean await(Iterable<? extends IoFuture> futures, long timeout, TimeUnit unit) throws InterruptedException
  {
    return await(futures, unit.toMillis(timeout));
  }

  public static boolean await(Iterable<? extends IoFuture> futures, long timeoutMillis) throws InterruptedException {
    return await0(futures, timeoutMillis, true);
  }

  public static boolean awaitUninterruptibly(Iterable<? extends IoFuture> futures, long timeout, TimeUnit unit) {
    return awaitUninterruptibly(futures, unit.toMillis(timeout));
  }

  public static boolean awaitUninterruptibly(Iterable<? extends IoFuture> futures, long timeoutMillis) {
    try {
      return await0(futures, timeoutMillis, false);
    } catch (InterruptedException e) {
      throw new InternalError();
    }
  }

  private static boolean await0(Iterable<? extends IoFuture> futures, long timeoutMillis, boolean interruptable) throws InterruptedException {
    long startTime = (timeoutMillis <= 0L) ? 0L : System.currentTimeMillis();
    long waitTime = timeoutMillis;

    boolean lastComplete = true;
    Iterator i = futures.iterator();
    while (i.hasNext()) {
      IoFuture f = (IoFuture)i.next();
      do {
        if (interruptable)
          lastComplete = f.await(waitTime);
        else {
          lastComplete = f.awaitUninterruptibly(waitTime);
        }

        waitTime = timeoutMillis - (System.currentTimeMillis() - startTime);

        if (lastComplete) break; if (waitTime <= 0L)
          break;
      }
      while (!(
        lastComplete));

      if (waitTime <= 0L) {
        break;
      }
    }

    return ((lastComplete) && (!(i.hasNext())));
  }
}