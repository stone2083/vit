package org.jellylab.vit.tunnel.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.jellylab.vit.IntranetTunnelConnection;
import org.jellylab.vit.protocol.IntranetTunnelInitRequest;
import org.jellylab.vit.protocol.IntranetTunnelInitResponse;
import org.jellylab.vit.protocol.IntranetTunnelInitResponse.Status;
import org.jellylab.vit.protocol.IntranetTunnelRequest;
import org.jellylab.vit.tunnel.IntranetTunnel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Apr 2, 2015
 */
public class IntranetTunnelController extends SimpleChannelInboundHandler<IntranetTunnelRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntranetTunnelController.class);

    private IntranetTunnel intranetTunnel;

    public IntranetTunnelController(IntranetTunnel intranetTunnel) {
        this.intranetTunnel = intranetTunnel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IntranetTunnelRequest msg) throws Exception {
        switch (msg.getRequestType()) {
        case UNKNOWN:
            ctx.close();
            break;
        case INIT:
            IntranetTunnelInitRequest req = (IntranetTunnelInitRequest) msg;
            LOGGER.debug("IntranetTunnelInitRequest [req={}]", req);

            // auth: forbidden
            if (!intranetTunnel.auth(req.getEip(), req.getEport(), req.getSign())) {
                ctx.writeAndFlush(new IntranetTunnelInitResponse(Status.FORBIDDEN)).addListener(
                        ChannelFutureListener.CLOSE);
            }
            // auth: ok
            else {
                IntranetTunnelConnection conn = new IntranetTunnelConnection();
                conn.setChannel(ctx.channel());
                conn.setEip(req.getEip());
                conn.setEport(req.getEport());
                intranetTunnel.addIntranetTunnelConnection(conn);

                ctx.pipeline().remove(this);
                ctx.pipeline().addFirst(new IntranetTunnelIdleHandler(conn));

                ctx.writeAndFlush(new IntranetTunnelInitResponse(Status.OK));
            }
            break;
        }
    }
}
