package org.jellylab.vit.socks.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.socks.SocksAuthRequestDecoder;
import io.netty.handler.codec.socks.SocksAuthResponse;
import io.netty.handler.codec.socks.SocksAuthScheme;
import io.netty.handler.codec.socks.SocksAuthStatus;
import io.netty.handler.codec.socks.SocksCmdRequest;
import io.netty.handler.codec.socks.SocksCmdRequestDecoder;
import io.netty.handler.codec.socks.SocksCmdResponse;
import io.netty.handler.codec.socks.SocksCmdStatus;
import io.netty.handler.codec.socks.SocksCmdType;
import io.netty.handler.codec.socks.SocksInitResponse;
import io.netty.handler.codec.socks.SocksRequest;

import org.jellylab.vit.IntranetTunnelConnection;
import org.jellylab.vit.SocksConnection;
import org.jellylab.vit.tunnel.IntranetTunnel;
import org.jellylab.vit.tunnel.handler.IntranetTunnelRelayHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Mar 24, 2015
 */
public class SocksHandler extends SimpleChannelInboundHandler<SocksRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocksHandler.class);

    private IntranetTunnel intranetTunnel;

    public SocksHandler(IntranetTunnel intranetTunnel) {
        this.intranetTunnel = intranetTunnel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SocksRequest msg) throws Exception {
        switch (msg.requestType()) {
        case UNKNOWN:
            ctx.close();
            break;
        case INIT:
            ctx.pipeline().addFirst(new SocksCmdRequestDecoder());
            ctx.writeAndFlush(new SocksInitResponse(SocksAuthScheme.NO_AUTH));
            LOGGER.debug("sokcs init. remote address: {}", ctx.channel().remoteAddress());
            break;
        case AUTH:
            ctx.pipeline().addFirst(new SocksAuthRequestDecoder());
            ctx.writeAndFlush(new SocksAuthResponse(SocksAuthStatus.SUCCESS));
            LOGGER.debug("sokcs auth. remote address: {}", ctx.channel().remoteAddress());
            break;
        case CMD:
            // tcp only
            SocksCmdRequest req = (SocksCmdRequest) msg;
            if (SocksCmdType.CONNECT != req.cmdType()) {
                ctx.close();
            }
            LOGGER.debug("sokcs auth. remote address: {}", ctx.channel().remoteAddress());

            IntranetTunnelConnection tunnelConn = intranetTunnel.borrowIntranetTunnelConnection(req.host(), req.port());
            if (tunnelConn == null) {
                ctx.writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, req.addressType())).addListener(
                        ChannelFutureListener.CLOSE);
                return;
            }
            SocksConnection conn = new SocksConnection();
            conn.setEip(req.host());
            conn.setEport(req.port());
            conn.setChannel(ctx.channel());

            tunnelConn.getChannel().pipeline().addFirst(new IntranetTunnelRelayHandler(conn));
            ctx.pipeline().addFirst(new SocksRelayHandler(tunnelConn));
            ctx.pipeline().remove(this);

            ctx.writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, req.addressType()));
            break;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

}
