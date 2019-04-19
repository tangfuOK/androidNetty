package com.cclx.mobile.hb_connect.net;

import android.widget.Toast;

import com.cclx.mobile.hb_connect.CCApplication;
import com.cclx.mobile.hb_connect.pojo.UnixTime;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = ((ByteBuf) msg);
        try {
            while (in.isReadable()) {
                Toast.makeText(CCApplication.getAppContext(), String.valueOf(in.readByte()), Toast.LENGTH_SHORT).show();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
