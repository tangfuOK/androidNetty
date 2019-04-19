package com.cclx.mobile.client;

import com.cclx.mobile.client.pojo.UnixTime;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private OnMessageListener onMessageListener;

    public TimeClientHandler(OnMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        UnixTime m = (UnixTime) msg;
        onMessageListener.sendMessage(m.getValue());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public interface OnMessageListener {
        void sendMessage(String message);
    }
}
