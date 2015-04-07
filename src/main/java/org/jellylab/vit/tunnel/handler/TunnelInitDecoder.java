package org.jellylab.vit.tunnel.handler;

import static org.jellylab.vit.utils.ProtocolUtil.ENCODING;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

import org.jellylab.vit.tunnel.handler.TunnelInitDecoder.State;
import org.jellylab.vit.tunnel.protocol.TunnelInitRequest;
import org.jellylab.vit.tunnel.protocol.TunnelRequest.IntranetTunnelRequestType;
import org.jellylab.vit.tunnel.protocol.TunnelVersion;

/**
 * @author jinli Apr 2, 2015
 */
public class TunnelInitDecoder extends ReplayingDecoder<State> {

    private TunnelInitRequest msg;

    public TunnelInitDecoder() {
        super(State.VERSION);
        msg = new TunnelInitRequest();
        msg.setRequestType(IntranetTunnelRequestType.INIT);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
        case VERSION:
            msg.setVersion(TunnelVersion.valueOf(in.readByte()));
            if (TunnelVersion.IntranetTunnelV1 != msg.getVersion()) {
                break;
            }
            checkpoint(State.HEADER);
        case HEADER:
            checkpoint(State.ADDRESS);
        case ADDRESS:
            msg.setEhost(in.readBytes(in.readByte()).toString(ENCODING));
            msg.setEport(in.readUnsignedShort());
            checkpoint(State.SIGN);
        case SIGN:
            msg.setSign(in.readBytes(in.readByte()).toString(ENCODING));
        }
        ctx.pipeline().remove(this);
        out.add(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    static enum State {
        VERSION,        // version
        HEADER,         // header
        ADDRESS,        // address
        SIGN;           // sign
    }

}
