package com.here.zuki.imhere.Utils;

import android.content.Context;
import android.os.Message;

import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by zuki on 4/13/17.
 */

public abstract class MainHandler extends Handler {

    protected Context pContext;

    final Vector<Message> messageQueueBuffer = new Vector<Message>();

    private boolean isPause;

    public MainHandler(Context context)
    {
        super();
        pContext = context;
    }


    @Override
    public void publish(LogRecord record) {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }

    public final synchronized void resume()
    {
        isPause = false;

        while (messageQueueBuffer.size() > 0) {
            final Message msg = messageQueueBuffer.elementAt(0);
            messageQueueBuffer.removeElementAt(0);
            sendMessage(msg);
        }
    }

    final public synchronized void pause() {
        isPause = true;
    }

    protected abstract void sendMessage(Message msg);

    protected abstract void processMessage(Message message);

    protected abstract boolean storeMessage(Message message);

    final public synchronized void handleMessage(Message msg) {
        if (isPause) {
            if (storeMessage(msg)) {
                Message msgCopy = new Message();
                msgCopy.copyFrom(msg);
                messageQueueBuffer.add(msgCopy);
            }
        } else {
            processMessage(msg);
        }
    }

    public abstract Message obtainMessage(int whatcode, int arg1, Object obj);
}
