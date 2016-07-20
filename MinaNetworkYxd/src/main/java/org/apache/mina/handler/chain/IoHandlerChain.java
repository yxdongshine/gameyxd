/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.handler.chain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.session.IoSession;

public class IoHandlerChain
  implements IoHandlerCommand
{
  private static volatile int nextId = 0;

  private final static int id = nextId++;

  private final String NEXT_COMMAND = IoHandlerChain.class.getName() + '.' + IoHandlerChain.id + ".nextCommand";

  private final Map<String, Entry> name2entry = new ConcurrentHashMap();
  private final Entry head;
  private final Entry tail;

  public IoHandlerChain()
  {
    this.head = new Entry(null, null, "head", createHeadCommand());
    this.tail = new Entry(this.head, null, "tail", createTailCommand());
    this.head.nextEntry = this.tail;
  }

  private IoHandlerCommand createHeadCommand() {
    return new IoHandlerCommand() {
      public void execute(IoHandlerCommand.NextCommand next, IoSession session, Object message) throws Exception {
        next.execute(session, message);
      }
    };
  }

  private IoHandlerCommand createTailCommand() {
    return new IoHandlerCommand() {
      public void execute(IoHandlerCommand.NextCommand next, IoSession session, Object message) throws Exception {
        next = (IoHandlerCommand.NextCommand)session.getAttribute(IoHandlerChain.this.NEXT_COMMAND);
        if (next != null)
          next.execute(session, message);
      }
    };
  }

  public Entry getEntry(String name)
  {
    Entry e = (Entry)this.name2entry.get(name);
    if (e == null) {
      return null;
    }
    return e;
  }

  public IoHandlerCommand get(String name) {
    Entry e = getEntry(name);
    if (e == null) {
      return null;
    }

    return e.getCommand();
  }

  public IoHandlerCommand.NextCommand getNextCommand(String name) {
    Entry e = getEntry(name);
    if (e == null) {
      return null;
    }

    return e.getNextCommand();
  }

  public synchronized void addFirst(String name, IoHandlerCommand command) {
    checkAddable(name);
    register(this.head, name, command);
  }

  public synchronized void addLast(String name, IoHandlerCommand command) {
    checkAddable(name);
    register(this.tail.prevEntry, name, command);
  }

  public synchronized void addBefore(String baseName, String name, IoHandlerCommand command) {
    Entry baseEntry = checkOldName(baseName);
    checkAddable(name);
    register(baseEntry.prevEntry, name, command);
  }

  public synchronized void addAfter(String baseName, String name, IoHandlerCommand command) {
    Entry baseEntry = checkOldName(baseName);
    checkAddable(name);
    register(baseEntry, name, command);
  }

  public synchronized IoHandlerCommand remove(String name) {
    Entry entry = checkOldName(name);
    deregister(entry);
    return entry.getCommand();
  }

  public synchronized void clear() throws Exception {
    Iterator it = new ArrayList(this.name2entry.keySet()).iterator();
    while (it.hasNext())
      remove((String)it.next());
  }

  private void register(Entry prevEntry, String name, IoHandlerCommand command)
  {
    Entry newEntry = new Entry(prevEntry, prevEntry.nextEntry, name, command);
    prevEntry.nextEntry.prevEntry = newEntry;
    prevEntry.nextEntry = newEntry;

    this.name2entry.put(name, newEntry);
  }

  private void deregister(Entry entry) {
    Entry prevEntry = entry.prevEntry;
    Entry nextEntry = entry.nextEntry;
    prevEntry.nextEntry = nextEntry;
    nextEntry.prevEntry = prevEntry;

    this.name2entry.remove(entry.name);
  }

  private Entry checkOldName(String baseName)
  {
    Entry e = (Entry)this.name2entry.get(baseName);
    if (e == null) {
      throw new IllegalArgumentException("Unknown filter name:" + baseName);
    }
    return e;
  }

  private void checkAddable(String name)
  {
    if (this.name2entry.containsKey(name))
      throw new IllegalArgumentException("Other filter is using the same name '" + name + "'");
  }

  public void execute(IoHandlerCommand.NextCommand next, IoSession session, Object message) throws Exception
  {
    if (next != null) {
      session.setAttribute(this.NEXT_COMMAND, next);
    }
    try
    {
      callNextCommand(this.head, session, message);
    } finally {
      session.removeAttribute(this.NEXT_COMMAND);
    }
  }

  private void callNextCommand(Entry entry, IoSession session, Object message) throws Exception {
    entry.getCommand().execute(entry.getNextCommand(), session, message);
  }

  public List<Entry> getAll() {
    List list = new ArrayList();
    Entry e = this.head.nextEntry;
    while (e != this.tail) {
      list.add(e);
      e = e.nextEntry;
    }

    return list;
  }

  public List<Entry> getAllReversed() {
    List list = new ArrayList();
    Entry e = this.tail.prevEntry;
    while (e != this.head) {
      list.add(e);
      e = e.prevEntry;
    }
    return list;
  }

  public boolean contains(String name) {
    return (getEntry(name) != null);
  }

  public boolean contains(IoHandlerCommand command) {
    Entry e = this.head.nextEntry;
    while (e != this.tail) {
      if (e.getCommand() == command) {
        return true;
      }
      e = e.nextEntry;
    }
    return false;
  }

  public boolean contains(Class<? extends IoHandlerCommand> commandType) {
    Entry e = this.head.nextEntry;
    while (e != this.tail) {
      if (commandType.isAssignableFrom(e.getCommand().getClass())) {
        return true;
      }
      e = e.nextEntry;
    }
    return false;
  }

  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    buf.append("{ ");

    boolean empty = true;

    Entry e = this.head.nextEntry;
    while (e != this.tail) {
      if (!(empty))
        buf.append(", ");
      else {
        empty = false;
      }

      buf.append('(');
      buf.append(e.getName());
      buf.append(':');
      buf.append(e.getCommand());
      buf.append(')');

      e = e.nextEntry;
    }

    if (empty) {
      buf.append("empty");
    }

    buf.append(" }");

    return buf.toString();
  }

  public class Entry
  {
    private Entry prevEntry;
    private Entry nextEntry;
    private String name = null;
    private final IoHandlerCommand command;
    private final IoHandlerCommand.NextCommand nextCommand;

    private Entry(Entry paramEntry1, Entry paramEntry2, String paramString, IoHandlerCommand paramIoHandlerCommand)
    {
      if (paramIoHandlerCommand == null) {
        throw new IllegalArgumentException("command");
      }
      if (name == null) {
        throw new IllegalArgumentException("name");
      }

      this.prevEntry = paramEntry1;
      this.nextEntry = paramEntry2;
      this.name = name;
      this.command = paramIoHandlerCommand;
      this.nextCommand = new IoHandlerCommand.NextCommand() {
        public void execute(IoSession session, Object message) throws Exception {
          IoHandlerChain.Entry nextEntry = IoHandlerChain.Entry.this.nextEntry;
          IoHandlerChain.this.callNextCommand(nextEntry, session, message);
        }
      };
    }

    public String getName()
    {
      return this.name;
    }

    public IoHandlerCommand getCommand()
    {
      return this.command;
    }

    public IoHandlerCommand.NextCommand getNextCommand()
    {
      return this.nextCommand;
    }
  }
}