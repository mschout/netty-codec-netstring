package io.github.mschout.netty.codec.netstring;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.charset.Charset;

/**
 * Byte encoder for netstrings.
 */
public class NetstringToByteEncoder extends MessageToByteEncoder<String> {
  private final Charset charset;

  /**
   * Constructor
   * @param charset Charset to encode the strings
   */
  public NetstringToByteEncoder(Charset charset) {
    this.charset = charset;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
    out.writeBytes(String.format("%d:%s,", msg.length(), msg).getBytes(charset));
  }
}
