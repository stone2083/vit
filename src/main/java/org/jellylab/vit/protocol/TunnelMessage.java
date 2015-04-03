package org.jellylab.vit.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author jinli Apr 2, 2015
 */
public abstract class TunnelMessage {

    public abstract void encode(ByteBuf byteBuf);

}
