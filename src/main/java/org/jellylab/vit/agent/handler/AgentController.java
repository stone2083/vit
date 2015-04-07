package org.jellylab.vit.agent.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jellylab.vit.agent.AgentConnection;
import org.jellylab.vit.agent.AgentConnectionGroup;
import org.jellylab.vit.tunnel.protocol.TunnelInitRequest;
import org.jellylab.vit.tunnel.protocol.TunnelVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Apr 2, 2015
 */
public class AgentController extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);

    private AgentConnectionGroup group;
    private AgentConnection conn;

    public AgentController(AgentConnectionGroup group) {
        this.group = group;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("agent connected. remote address: {}", ctx.channel().remoteAddress());
        TunnelInitRequest req = new TunnelInitRequest();
        req.setVersion(TunnelVersion.IntranetTunnelV1);
        req.setEhost(group.getEhost());
        req.setEport(group.getEport());
        req.setSign(group.getSign());
        ctx.writeAndFlush(req).sync();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("agent read. remote address: {}", ctx.channel().remoteAddress());
        ByteBuf byteBuf = (ByteBuf) msg;
        if (byteBuf.readByte() == 0x00) {
            ctx.pipeline().remove(this);

            AgentConnection conn = new AgentConnection();
            conn.setChannel(ctx.channel());
            if (!group.addAgentConnection(conn)) {
                ctx.close();
                return;
            }

            this.setAgentConnection(conn);
            ctx.pipeline().addFirst(new AgentRelayHandler(conn));
        } else {
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("agent closed. remote address:", ctx.channel().remoteAddress());
        group.deleteAgentConnection(conn);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    public void setAgentConnection(AgentConnection conn) {
        this.conn = conn;
    }

}
