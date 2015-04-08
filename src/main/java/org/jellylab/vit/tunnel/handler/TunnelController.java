package org.jellylab.vit.tunnel.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.jellylab.vit.tunnel.Tunnel;
import org.jellylab.vit.tunnel.TunnelConnection;
import org.jellylab.vit.tunnel.protocol.TunnelInitRequest;
import org.jellylab.vit.tunnel.protocol.TunnelInitResponse;
import org.jellylab.vit.tunnel.protocol.TunnelInitResponse.Status;
import org.jellylab.vit.tunnel.protocol.TunnelRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Apr 2, 2015
 */
public class TunnelController extends SimpleChannelInboundHandler<TunnelRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TunnelController.class);

    private Tunnel tunnel;

    public TunnelController(Tunnel tunnel) {
        this.tunnel = tunnel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TunnelRequest msg) throws Exception {
        LOGGER.debug("tunnel read. remote address: {}", ctx.channel().remoteAddress());
        switch (msg.getRequestType()) {
        case UNKNOWN:
            ctx.close();
            break;
        case INIT:
            TunnelInitRequest req = (TunnelInitRequest) msg;
            // auth: forbidden
            if (!tunnel.auth(req.getEhost(), req.getEport(), req.getSign())) {
                ctx.writeAndFlush(new TunnelInitResponse(Status.FORBIDDEN)).addListener(ChannelFutureListener.CLOSE);
            }
            // auth: ok
            else {
                TunnelConnection conn = new TunnelConnection();
                conn.setChannel(ctx.channel());
                conn.setEhost(req.getEhost());
                conn.setEport(req.getEport());
                tunnel.addIntranetTunnelConnection(conn);

                ctx.pipeline().remove(this);
                ctx.pipeline().addFirst(new TunnelIdleHandler(conn));

                ctx.writeAndFlush(new TunnelInitResponse(Status.OK));
            }
            break;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("tunnel closed. remote address: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.debug("tunnel exception. remote address: {}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }

}
