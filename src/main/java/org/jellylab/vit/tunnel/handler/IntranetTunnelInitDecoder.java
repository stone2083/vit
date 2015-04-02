package org.jellylab.vit.tunnel.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

import org.jellylab.vit.protocol.IntranetTunnelAddressType;
import org.jellylab.vit.protocol.IntranetTunnelInitRequest;
import org.jellylab.vit.protocol.IntranetTunnelRequest.IntranetTunnelRequestType;
import org.jellylab.vit.protocol.IntranetTunnelVersion;
import org.jellylab.vit.tunnel.handler.IntranetTunnelInitDecoder.State;
import org.jellylab.vit.utils.ProtocolUtil;

/**
 * @author jinli Apr 2, 2015
 */
public class IntranetTunnelInitDecoder extends ReplayingDecoder<State> {

    private IntranetTunnelInitRequest msg;

    public IntranetTunnelInitDecoder() {
        super(State.VERSION);
        msg = new IntranetTunnelInitRequest();
        msg.setRequestType(IntranetTunnelRequestType.INIT);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
        case VERSION:
            msg.setVersion(IntranetTunnelVersion.valueOf(in.readByte()));
            if (IntranetTunnelVersion.IntranetTunnelV1 != msg.getVersion()) {
                break;
            }
            checkpoint(State.HEADER);
        case HEADER:
            msg.setAddressType(IntranetTunnelAddressType.valueOf(in.readByte()));
            if (IntranetTunnelAddressType.IPv4 != msg.getAddressType()) {
                break;
            }
            checkpoint(State.ADDRESS);
        case ADDRESS:
            msg.setEip(ProtocolUtil.toIpv4(in.readInt()));
            msg.setEport(in.readUnsignedShort());
            checkpoint(State.SIGN);
        case SIGN:
            msg.setSign(in.readBytes(in.readByte()).toString());
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
