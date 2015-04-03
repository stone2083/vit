package org.jellylab.vit.protocol;

import io.netty.buffer.ByteBuf;

/**
 * @author jinli Apr 2, 2015
 */
public class TunnelInitResponse extends TunnelMessage {

    private Status status;

    public TunnelInitResponse(Status status) {
        this.status = status;
    }

    @Override
    public void encode(ByteBuf byteBuf) {
        byteBuf.writeByte(status.b);
    }

    public static enum Status {
        OK((byte) 0x00),        // ok
        FAIL((byte) 0x01),      // fail
        FORBIDDEN((byte) 0x02); // firbidden

        private byte b;

        private Status(byte b) {
            this.b = b;
        }
    }

}
