package org.jellylab.vit.agent.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import org.jellylab.vit.protocol.IntranetTunnelMessage;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentInitEncoder extends MessageToByteEncoder<IntranetTunnelMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, IntranetTunnelMessage msg, ByteBuf out) throws Exception {
        msg.encode(out);
    }

}
