package org.jellylab.vit.tunnel.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.Charset;
import java.util.List;

import org.jellylab.vit.protocol.TunnelAddressType;
import org.jellylab.vit.protocol.TunnelInitRequest;
import org.jellylab.vit.protocol.TunnelRequest.IntranetTunnelRequestType;
import org.jellylab.vit.protocol.TunnelVersion;
import org.jellylab.vit.tunnel.handler.TunnelInitDecoder.State;
import org.jellylab.vit.utils.ProtocolUtil;

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
            msg.setAddressType(TunnelAddressType.valueOf(in.readByte()));
            if (TunnelAddressType.IPv4 != msg.getAddressType()) {
                break;
            }
            checkpoint(State.ADDRESS);
        case ADDRESS:
            msg.setEip(ProtocolUtil.intToIpv4(in.readInt()));
            msg.setEport(in.readUnsignedShort());
            checkpoint(State.SIGN);
        case SIGN:
            msg.setSign(in.readBytes(in.readByte()).toString(Charset.forName("UTF-8")));
        }
        ctx.pipeline().remove(this);
        out.add(msg);
    }

    static enum State {
        VERSION,        // version
        HEADER,         // header
        ADDRESS,        // address
        SIGN;           // sign
    }

}
