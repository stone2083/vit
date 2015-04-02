package org.jellylab.vit.tunnel.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.jellylab.vit.protocol.IntranetTunnelResponse;

/**
 * @author jinli Apr 2, 2015
 */
public class IntranetTunnelResponseEncoder extends MessageToByteEncoder<IntranetTunnelResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, IntranetTunnelResponse msg, ByteBuf out) throws Exception {
        msg.encode(out);
    }

}
