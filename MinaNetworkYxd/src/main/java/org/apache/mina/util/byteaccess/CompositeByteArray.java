/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util.byteaccess;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.apache.mina.core.buffer.IoBuffer;

public final class CompositeByteArray extends AbstractByteArray
{
  private final ByteArrayList bas;
  private ByteOrder order;
  private final ByteArrayFactory byteArrayFactory;

  public CompositeByteArray()
  {
    this(null);
  }

  public CompositeByteArray(ByteArrayFactory byteArrayFactory)
  {
    this.bas = new ByteArrayList();

    this.byteArrayFactory = byteArrayFactory;
  }

  public ByteArray getFirst()
  {
    if (this.bas.isEmpty()) {
      return null;
    }

    return this.bas.getFirst().getByteArray();
  }

  public void addFirst(ByteArray ba)
  {
    addHook(ba);
    this.bas.addFirst(ba);
  }

  public ByteArray removeFirst()
  {
    ByteArrayList.Node node = this.bas.removeFirst();
    return ((node == null) ? null : node.getByteArray());
  }

  public ByteArray removeTo(int index)
  {
    if ((index < first()) || (index > last())) {
      throw new IndexOutOfBoundsException();
    }

    CompositeByteArray prefix = new CompositeByteArray(this.byteArrayFactory);
    int remaining = index - first();

    while (remaining > 0) {
      ByteArray component = removeFirst();

      if (component.last() <= remaining)
      {
        prefix.addLast(component);
        remaining -= component.last();
      }
      else
      {
        IoBuffer bb = component.getSingleIoBuffer();

        int originalLimit = bb.limit();

        bb.position(0);

        bb.limit(remaining);

        IoBuffer bb1 = bb.slice();

        bb.position(remaining);

        bb.limit(originalLimit);

        IoBuffer bb2 = bb.slice();

        ByteArray ba1 = new BufferByteArray(bb1)
        {
          public void free()
          {
          }
        };
        prefix.addLast(ba1);
        remaining -= ba1.last();

        ByteArray componentFinal = component;
        ByteArray ba2 = new BufferByteArray(bb2)
        {
          public void free() {
           
          }
        };
        addFirst(ba2);
      }

    }

    return prefix;
  }

  public void addLast(ByteArray ba)
  {
    addHook(ba);
    this.bas.addLast(ba);
  }

  public ByteArray removeLast()
  {
    ByteArrayList.Node node = this.bas.removeLast();
    return ((node == null) ? null : node.getByteArray());
  }

  public void free()
  {
    while (!(this.bas.isEmpty())) {
      ByteArrayList.Node node = this.bas.getLast();
      node.getByteArray().free();
      this.bas.removeLast();
    }
  }

  private void checkBounds(int index, int accessSize) {
    int lower = index;
    int upper = index + accessSize;

    if (lower < first()) {
      throw new IndexOutOfBoundsException("Index " + lower + " less than start " + first() + ".");
    }

    if (upper > last())
      throw new IndexOutOfBoundsException("Index " + upper + " greater than length " + last() + ".");
  }

  public Iterable<IoBuffer> getIoBuffers()
  {
    if (this.bas.isEmpty()) {
      return Collections.emptyList();
    }

    Collection result = new ArrayList();
    ByteArrayList.Node node = this.bas.getFirst();

    for (IoBuffer bb : node.getByteArray().getIoBuffers()) {
      result.add(bb);
    }

    while (node.hasNextNode()) {
      node = node.getNextNode();

      for (IoBuffer bb : node.getByteArray().getIoBuffers()) {
        result.add(bb);
      }
    }

    return result;
  }

  public IoBuffer getSingleIoBuffer()
  {
    if (this.byteArrayFactory == null) {
      throw new IllegalStateException("Can't get single buffer from CompositeByteArray unless it has a ByteArrayFactory.");
    }

    if (this.bas.isEmpty()) {
      ByteArray ba = this.byteArrayFactory.create(1);
      return ba.getSingleIoBuffer();
    }

    int actualLength = last() - first();

    ByteArrayList.Node node = this.bas.getFirst();
    ByteArray ba = node.getByteArray();

    if (ba.last() == actualLength) {
      return ba.getSingleIoBuffer();
    }

    ByteArray target = this.byteArrayFactory.create(actualLength);
    IoBuffer bb = target.getSingleIoBuffer();
    ByteArray.Cursor cursor = cursor();
    cursor.put(bb);

    while (!(this.bas.isEmpty())) {
      ByteArrayList.Node node1 = this.bas.getLast();
      ByteArray component = node1.getByteArray();
      this.bas.removeLast();
      component.free();
    }

    this.bas.addLast(target);
    return bb;
  }

  public ByteArray.Cursor cursor()
  {
    return new CursorImpl();
  }

  public ByteArray.Cursor cursor(int index)
  {
    return new CursorImpl(index);
  }

  public ByteArray.Cursor cursor(CursorListener listener)
  {
    return new CursorImpl(listener);
  }

  public ByteArray.Cursor cursor(int index, CursorListener listener)
  {
    return new CursorImpl(index, listener);
  }

  public ByteArray slice(int index, int length)
  {
    return cursor(index).slice(length);
  }

  public byte get(int index)
  {
    return cursor(index).get();
  }

  public void put(int index, byte b)
  {
    cursor(index).put(b);
  }

  public void get(int index, IoBuffer bb)
  {
    cursor(index).get(bb);
  }

  public void put(int index, IoBuffer bb)
  {
    cursor(index).put(bb);
  }

  public int first()
  {
    return this.bas.firstByte();
  }

  public int last()
  {
    return this.bas.lastByte();
  }

  private void addHook(ByteArray ba)
  {
    if (ba.first() != 0) {
      throw new IllegalArgumentException("Cannot add byte array that doesn't start from 0: " + ba.first());
    }

    if (this.order == null)
      this.order = ba.order();
    else if (!(this.order.equals(ba.order())))
      throw new IllegalArgumentException("Cannot add byte array with different byte order: " + ba.order());
  }

  public ByteOrder order()
  {
    if (this.order == null) {
      throw new IllegalStateException("Byte order not yet set.");
    }
    return this.order;
  }

  public void order(ByteOrder order)
  {
    if ((order == null) || (!(order.equals(this.order)))) {
      this.order = order;

      if (!(this.bas.isEmpty()))
        for (ByteArrayList.Node node = this.bas.getFirst(); node.hasNextNode(); node = node.getNextNode())
          node.getByteArray().order(order);
    }
  }

  public short getShort(int index)
  {
    return cursor(index).getShort();
  }

  public void putShort(int index, short s)
  {
    cursor(index).putShort(s);
  }

  public int getInt(int index)
  {
    return cursor(index).getInt();
  }

  public void putInt(int index, int i)
  {
    cursor(index).putInt(i);
  }

  public long getLong(int index)
  {
    return cursor(index).getLong();
  }

  public void putLong(int index, long l)
  {
    cursor(index).putLong(l);
  }

  public float getFloat(int index)
  {
    return cursor(index).getFloat();
  }

  public void putFloat(int index, float f)
  {
    cursor(index).putFloat(f);
  }

  public double getDouble(int index)
  {
    return cursor(index).getDouble();
  }

  public void putDouble(int index, double d)
  {
    cursor(index).putDouble(d);
  }

  public char getChar(int index)
  {
    return cursor(index).getChar();
  }

  public void putChar(int index, char c)
  {
    cursor(index).putChar(c);
  }

  private class CursorImpl
    implements ByteArray.Cursor
  {
    private int index;
    private CompositeByteArray.CursorListener listener = null;
    private ByteArrayList.Node componentNode;
    private int componentIndex;
    private ByteArray.Cursor componentCursor;

    public CursorImpl()
    {
      this(0, null);
    }

    public CursorImpl(int paramInt) {
      this(paramInt, null);
    }

    public CursorImpl(CompositeByteArray.CursorListener paramCursorListener) {
      this(0, paramCursorListener);
    }

    public CursorImpl(int paramInt, CompositeByteArray.CursorListener paramCursorListener) {
      this.index = paramInt;
      this.listener = listener;
    }

    public int getIndex()
    {
      return this.index;
    }

    public void setIndex(int index)
    {
      CompositeByteArray.this.checkBounds(index, 0);
      this.index = index;
    }

    public void skip(int length)
    {
      setIndex(this.index + length);
    }

    public ByteArray slice(int length)
    {
      CompositeByteArray slice = new CompositeByteArray(CompositeByteArray.this.byteArrayFactory);
      int remaining = length;
      while (remaining > 0) {
        prepareForAccess(remaining);
        int componentSliceSize = Math.min(remaining, this.componentCursor.getRemaining());
        ByteArray componentSlice = this.componentCursor.slice(componentSliceSize);
        slice.addLast(componentSlice);
        this.index += componentSliceSize;
        remaining -= componentSliceSize;
      }
      return slice;
    }

    public ByteOrder order()
    {
      return CompositeByteArray.this.order();
    }

    private void prepareForAccess(int accessSize)
    {
      if ((this.componentNode != null) && (this.componentNode.isRemoved())) {
        this.componentNode = null;
        this.componentCursor = null;
      }

      CompositeByteArray.this.checkBounds(this.index, accessSize);

      ByteArrayList.Node oldComponentNode = this.componentNode;

      if (this.componentNode == null) {
        int basMidpoint = (CompositeByteArray.this.last() - CompositeByteArray.this.first()) / 2 + CompositeByteArray.this.first();
        if (this.index <= basMidpoint)
        {
          this.componentNode = CompositeByteArray.this.bas.getFirst();
          this.componentIndex = CompositeByteArray.this.first();
          if (this.listener != null)
            this.listener.enteredFirstComponent(this.componentIndex, this.componentNode.getByteArray());
        }
        else
        {
          this.componentNode = CompositeByteArray.this.bas.getLast();
          this.componentIndex = (CompositeByteArray.this.last() - this.componentNode.getByteArray().last());
          if (this.listener != null) {
            this.listener.enteredLastComponent(this.componentIndex, this.componentNode.getByteArray());
          }
        }

      }

      while (this.index < this.componentIndex) {
        this.componentNode = this.componentNode.getPreviousNode();
        this.componentIndex -= this.componentNode.getByteArray().last();
        if (this.listener != null) {
          this.listener.enteredPreviousComponent(this.componentIndex, this.componentNode.getByteArray());
        }

      }

      while (this.index >= this.componentIndex + this.componentNode.getByteArray().length()) {
        this.componentIndex += this.componentNode.getByteArray().last();
        this.componentNode = this.componentNode.getNextNode();
        if (this.listener != null) {
          this.listener.enteredNextComponent(this.componentIndex, this.componentNode.getByteArray());
        }

      }

      int internalComponentIndex = this.index - this.componentIndex;
      if (this.componentNode == oldComponentNode)
      {
        this.componentCursor.setIndex(internalComponentIndex);
      }
      else
        this.componentCursor = this.componentNode.getByteArray().cursor(internalComponentIndex);
    }

    public int getRemaining()
    {
      return (CompositeByteArray.this.last() - this.index + 1);
    }

    public boolean hasRemaining()
    {
      return (getRemaining() > 0);
    }

    public byte get()
    {
      prepareForAccess(1);
      byte b = this.componentCursor.get();
      this.index += 1;
      return b;
    }

    public void put(byte b)
    {
      prepareForAccess(1);
      this.componentCursor.put(b);
      this.index += 1;
    }

    public void get(IoBuffer bb)
    {
      while (bb.hasRemaining()) {
        int remainingBefore = bb.remaining();
        prepareForAccess(remainingBefore);
        this.componentCursor.get(bb);
        int remainingAfter = bb.remaining();

        int chunkSize = remainingBefore - remainingAfter;
        this.index += chunkSize;
      }
    }

    public void put(IoBuffer bb)
    {
      while (bb.hasRemaining()) {
        int remainingBefore = bb.remaining();
        prepareForAccess(remainingBefore);
        this.componentCursor.put(bb);
        int remainingAfter = bb.remaining();

        int chunkSize = remainingBefore - remainingAfter;
        this.index += chunkSize;
      }
    }

    public short getShort()
    {
      prepareForAccess(2);
      if (this.componentCursor.getRemaining() >= 4) {
        short s = this.componentCursor.getShort();
        this.index += 2;
        return s;
      }
      byte b0 = get();
      byte b1 = get();
      if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
        return (short)(b0 << 8 | b1 << 0);
      }
      return (short)(b1 << 8 | b0 << 0);
    }

    public void putShort(short s)
    {
      prepareForAccess(2);
      if (this.componentCursor.getRemaining() >= 4) {
        this.componentCursor.putShort(s);
        this.index += 2;
      }
      else
      {
        byte b1;
        byte b0;
        if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
          b0 = (byte)(s >> 8 & 0xFF);
          b1 = (byte)(s >> 0 & 0xFF);
        } else {
          b0 = (byte)(s >> 0 & 0xFF);
          b1 = (byte)(s >> 8 & 0xFF);
        }
        put(b0);
        put(b1);
      }
    }

    public int getInt()
    {
      prepareForAccess(4);
      if (this.componentCursor.getRemaining() >= 4) {
        int i = this.componentCursor.getInt();
        this.index += 4;
        return i;
      }
      byte b0 = get();
      byte b1 = get();
      byte b2 = get();
      byte b3 = get();
      if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
        return (b0 << 24 | b1 << 16 | b2 << 8 | b3 << 0);
      }
      return (b3 << 24 | b2 << 16 | b1 << 8 | b0 << 0);
    }

    public void putInt(int i)
    {
      prepareForAccess(4);
      if (this.componentCursor.getRemaining() >= 4) {
        this.componentCursor.putInt(i);
        this.index += 4;
      }
      else
      {
        byte b3;
        byte b0;
        byte b1;
        byte b2;
        if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
           b0 = (byte)(i >> 24 & 0xFF);
           b1 = (byte)(i >> 16 & 0xFF);
           b2 = (byte)(i >> 8 & 0xFF);
          b3 = (byte)(i >> 0 & 0xFF);
        } else {
          b0 = (byte)(i >> 0 & 0xFF);
          b1 = (byte)(i >> 8 & 0xFF);
          b2 = (byte)(i >> 16 & 0xFF);
          b3 = (byte)(i >> 24 & 0xFF);
        }
        put(b0);
        put(b1);
        put(b2);
        put(b3);
      }
    }

    public long getLong()
    {
      prepareForAccess(8);
      if (this.componentCursor.getRemaining() >= 4) {
        long l = this.componentCursor.getLong();
        this.index += 8;
        return l;
      }
      byte b0 = get();
      byte b1 = get();
      byte b2 = get();
      byte b3 = get();
      byte b4 = get();
      byte b5 = get();
      byte b6 = get();
      byte b7 = get();
      if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
        return ((b0 & 0xFF) << 56 | (b1 & 0xFF) << 48 | (b2 & 0xFF) << 40 | (b3 & 0xFF) << 32 | (b4 & 0xFF) << 24 | (b5 & 0xFF) << 16 | (b6 & 0xFF) << 8 | (b7 & 0xFF) << 0);
      }
      return ((b7 & 0xFF) << 56 | (b6 & 0xFF) << 48 | (b5 & 0xFF) << 40 | (b4 & 0xFF) << 32 | (b3 & 0xFF) << 24 | (b2 & 0xFF) << 16 | (b1 & 0xFF) << 8 | (b0 & 0xFF) << 0);
    }

    public void putLong(long l)
    {
      prepareForAccess(8);
      if (this.componentCursor.getRemaining() >= 4) {
        this.componentCursor.putLong(l);
        this.index += 8;
      }
      else
      {
        byte b0;
        byte b1;
        byte b2;
        byte b3;
        byte b4;
        byte b5;
        byte b6;
        byte b7;
        if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
           b0 = (byte)(int)(l >> 56 & 0xFF);
           b1 = (byte)(int)(l >> 48 & 0xFF);
           b2 = (byte)(int)(l >> 40 & 0xFF);
           b3 = (byte)(int)(l >> 32 & 0xFF);
           b4 = (byte)(int)(l >> 24 & 0xFF);
           b5 = (byte)(int)(l >> 16 & 0xFF);
           b6 = (byte)(int)(l >> 8 & 0xFF);
          b7 = (byte)(int)(l >> 0 & 0xFF);
        } else {
          b0 = (byte)(int)(l >> 0 & 0xFF);
          b1 = (byte)(int)(l >> 8 & 0xFF);
          b2 = (byte)(int)(l >> 16 & 0xFF);
          b3 = (byte)(int)(l >> 24 & 0xFF);
          b4 = (byte)(int)(l >> 32 & 0xFF);
          b5 = (byte)(int)(l >> 40 & 0xFF);
          b6 = (byte)(int)(l >> 48 & 0xFF);
          b7 = (byte)(int)(l >> 56 & 0xFF);
        }
        put(b0);
        put(b1);
        put(b2);
        put(b3);
        put(b4);
        put(b5);
        put(b6);
        put(b7);
      }
    }

    public float getFloat()
    {
      prepareForAccess(4);
      if (this.componentCursor.getRemaining() >= 4) {
        float f = this.componentCursor.getFloat();
        this.index += 4;
        return f;
      }
      int i = getInt();
      return Float.intBitsToFloat(i);
    }

    public void putFloat(float f)
    {
      prepareForAccess(4);
      if (this.componentCursor.getRemaining() >= 4) {
        this.componentCursor.putFloat(f);
        this.index += 4;
      } else {
        int i = Float.floatToIntBits(f);
        putInt(i);
      }
    }

    public double getDouble()
    {
      prepareForAccess(8);
      if (this.componentCursor.getRemaining() >= 4) {
        double d = this.componentCursor.getDouble();
        this.index += 8;
        return d;
      }
      long l = getLong();
      return Double.longBitsToDouble(l);
    }

    public void putDouble(double d)
    {
      prepareForAccess(8);
      if (this.componentCursor.getRemaining() >= 4) {
        this.componentCursor.putDouble(d);
        this.index += 8;
      } else {
        long l = Double.doubleToLongBits(d);
        putLong(l);
      }
    }

    public char getChar()
    {
      prepareForAccess(2);
      if (this.componentCursor.getRemaining() >= 4) {
        char c = this.componentCursor.getChar();
        this.index += 2;
        return c;
      }
      byte b0 = get();
      byte b1 = get();
      if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
        return (char)(b0 << 8 | b1 << 0);
      }
      return (char)(b1 << 8 | b0 << 0);
    }

    public void putChar(char c)
    {
      prepareForAccess(2);
      if (this.componentCursor.getRemaining() >= 4) {
        this.componentCursor.putChar(c);
        this.index += 2;
      }
      else
      {
        byte b1;
        byte b0;
        if (CompositeByteArray.this.order.equals(ByteOrder.BIG_ENDIAN)) {
          b0 = (byte)(c >> '\b' & 0xFF);
          b1 = (byte)(c >> '\0' & 0xFF);
        } else {
          b0 = (byte)(c >> '\0' & 0xFF);
          b1 = (byte)(c >> '\b' & 0xFF);
        }
        put(b0);
        put(b1);
      }
    }
  }

  public static abstract interface CursorListener
  {
    public abstract void enteredFirstComponent(int paramInt, ByteArray paramByteArray);

    public abstract void enteredNextComponent(int paramInt, ByteArray paramByteArray);

    public abstract void enteredPreviousComponent(int paramInt, ByteArray paramByteArray);

    public abstract void enteredLastComponent(int paramInt, ByteArray paramByteArray);
  }
}