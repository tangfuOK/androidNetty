package com.cclx.mobile.client;

import com.cclx.mobile.client.pojo.UnixTime;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TimeDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int lenght = byteBuf.readableBytes();
        if (lenght > 0) {
            String dateTime = new String(byteBuf.readBytes(lenght).array());
            list.add(new UnixTime(dateTime));
        }
    }
}
