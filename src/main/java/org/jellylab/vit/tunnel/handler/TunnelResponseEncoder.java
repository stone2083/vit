package org.jellylab.vit.tunnel.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.jellylab.vit.tunnel.protocol.TunnelMessage;

/**
 * @author jinli Apr 2, 2015
 */
public class TunnelResponseEncoder extends MessageToByteEncoder<TunnelMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TunnelMessage msg, ByteBuf out) throws Exception {
        msg.encode(out);
    }

}
