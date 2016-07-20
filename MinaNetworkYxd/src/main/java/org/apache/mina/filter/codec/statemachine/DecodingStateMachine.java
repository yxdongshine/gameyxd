/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.statemachine;

import java.util.ArrayList;
import java.util.List;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DecodingStateMachine
  implements DecodingState
{
  private final Logger log = LoggerFactory.getLogger(DecodingStateMachine.class);

  private final List<Object> childProducts = new ArrayList();

  private final ProtocolDecoderOutput childOutput = new ProtocolDecoderOutput()
  {
    public void flush(IoFilter.NextFilter nextFilter, IoSession session) {
    }

    public void write(Object message) {
      DecodingStateMachine.this.childProducts.add(message);
    }
  };
  private DecodingState currentState;
  private boolean initialized;

  protected abstract DecodingState init()
    throws Exception;

  protected abstract DecodingState finishDecode(List<Object> paramList, ProtocolDecoderOutput paramProtocolDecoderOutput)
    throws Exception;

  protected abstract void destroy()
    throws Exception;

  public DecodingState decode(IoBuffer in, ProtocolDecoderOutput out)
    throws Exception
  {
    DecodingState state = getCurrentState();

    int limit = in.limit();
    int pos = in.position();
    try
    {
      Object localObject2;
      while (pos != limit)
      {
        DecodingState oldState = state;
        state = state.decode(in, this.childOutput);

        if (state == null) {
          localObject2 = finishDecode(this.childProducts, out);
          return (DecodingState) localObject2;
        }

        int newPos = in.position();

        if ((newPos == pos) && (oldState == state)) {
          break;
        }
        pos = newPos;
      }

      return this;
    }
    catch (Exception e) {
    }
    finally {
      this.currentState = state;

      if (state == null)
        cleanup();
    }
	return state;
  }

  public DecodingState finishDecode(ProtocolDecoderOutput out) throws Exception {
    DecodingState state = getCurrentState();
    DecodingState nextState;
    try {
      DecodingState oldState;
      do {
        oldState = state;
        state = state.finishDecode(this.childOutput);
        if (state == null);
      }
      while (oldState != state);
    }
    catch (Exception e)
    {
      state = null;
      this.log.debug("Ignoring the exception caused by a closed session.", e);
    }
    finally
    {
      this.currentState = state;
       nextState = finishDecode(this.childProducts, out);
      if (state == null) {
        cleanup();
      }
    }
    return nextState;
  }

  private void cleanup() {
    if (!(this.initialized)) {
      throw new IllegalStateException();
    }

    this.initialized = false;
    this.childProducts.clear();
    try {
      destroy();
    } catch (Exception e2) {
      this.log.warn("Failed to destroy a decoding state machine.", e2);
    }
  }

  private DecodingState getCurrentState() throws Exception {
    DecodingState state = this.currentState;
    if (state == null) {
      state = init();
      this.initialized = true;
    }
    return state;
  }
}