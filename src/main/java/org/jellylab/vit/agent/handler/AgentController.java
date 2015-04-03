package org.jellylab.vit.agent.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.agent.AgentConnection;
import org.jellylab.vit.agent.AgentConnectionGroup;
import org.jellylab.vit.protocol.TunnelAddressType;
import org.jellylab.vit.protocol.TunnelInitRequest;
import org.jellylab.vit.protocol.TunnelVersion;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentController extends ChannelInboundHandlerAdapter {

    private AgentConnectionGroup group;

    public AgentController(AgentConnectionGroup group) {
        this.group = group;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        TunnelInitRequest req = new TunnelInitRequest();
        req.setVersion(TunnelVersion.IntranetTunnelV1);
        req.setAddressType(TunnelAddressType.IPv4);
        req.setEip(group.getEip());
        req.setEport(group.getEport());
        req.setSign(group.getSign());
        ctx.writeAndFlush(req).sync();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (byteBuf.readByte() == 0x00) {
            ctx.pipeline().remove(this);

            AgentConnection conn = new AgentConnection();
            conn.setChannel(ctx.channel());
            if (!group.addAgentConnection(conn)) {
                ctx.close();
                return;
            }


            ctx.pipeline().addFirst(new AgentRelayHandler(conn));
        } else {
            ctx.close();
        }
    }

}
