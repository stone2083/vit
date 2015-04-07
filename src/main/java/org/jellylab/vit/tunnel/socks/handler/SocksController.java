package org.jellylab.vit.tunnel.socks.handler;

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

import org.jellylab.vit.tunnel.Tunnel;
import org.jellylab.vit.tunnel.TunnelConnection;
import org.jellylab.vit.tunnel.handler.TunnelRelayHandler;
import org.jellylab.vit.tunnel.socks.SocksConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jinli Mar 24, 2015
 */
public class SocksController extends SimpleChannelInboundHandler<SocksRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocksController.class);

    private Tunnel intranetTunnel;

    public SocksController(Tunnel intranetTunnel) {
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

            TunnelConnection tunnelConn = intranetTunnel.borrowIntranetTunnelConnection(req.host(), req.port());
            if (tunnelConn == null) {
                ctx.writeAndFlush(new SocksCmdResponse(SocksCmdStatus.FAILURE, req.addressType())).addListener(
                        ChannelFutureListener.CLOSE);
                return;
            }
            SocksConnection conn = new SocksConnection();
            conn.setEhost(req.host());
            conn.setEport(req.port());
            conn.setChannel(ctx.channel());

            tunnelConn.getChannel().pipeline().addFirst(new TunnelRelayHandler(conn));
            ctx.pipeline().addFirst(new SocksRelayHandler(tunnelConn));
            ctx.pipeline().remove(this);

            ctx.writeAndFlush(new SocksCmdResponse(SocksCmdStatus.SUCCESS, req.addressType(), req.host(), req.port()));
            break;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("socks channel closed. remoteAddress: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.debug("socks channel exception. remoteAddress: {}", ctx.channel().remoteAddress());
        ctx.close();
    }

}
