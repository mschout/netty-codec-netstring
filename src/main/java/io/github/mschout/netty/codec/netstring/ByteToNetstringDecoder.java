package io.github.mschout.netty.codec.netstring;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Netty decoder for netstarings.
 *
 * This decoder receives netstrings such as {@code 3:foo,} and decodes the values to the String value e.g. {@code foo}.
 * @see <a href="http://cr.yp.to/proto/netstrings.txt">Netstring Spec</a>
 */
public class ByteToNetstringDecoder extends ByteToMessageDecoder {
  private final Integer maxLength;

  private final Charset charset;

  /**
   * Constructor
   * @param maxLength Maximum allowed netstring length.  If a netstring is received with length field longer than
   *                  this, then {@link TooLongFrameException} will be thrown.
   * @param charset The charset for the received netstrings
   */
  public ByteToNetstringDecoder(int maxLength, Charset charset) {
    this.maxLength = maxLength;
    this.charset = charset;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    in.markReaderIndex();

    int readableBytes = in.readableBytes();

    if (readableBytes > maxLength) throw new TooLongFrameException("Frame too big " + readableBytes + " > " + maxLength);

    // Minimum netstring must be at least 3 chars long: "0:,"
    if (readableBytes < 3) return;

    final int sizeLength = in.bytesBefore((byte) ':');

    // if we haven't received the size delimiter yet return
    if (sizeLength < 0) return;

    final int dataLength = readLength(in, sizeLength);
    // NOTE: if we bail out early from here on out, we must call .resetReaderIndex()

    if (dataLength > maxLength) throw new TooLongFrameException("Frame too big " + dataLength + " > " + maxLength);

    if (readableBytes < dataLength + 2) { // ":" plus netstring value plus ","
      in.resetReaderIndex();
      return;
    }

    // we should have the entire payload now
    in.skipBytes(1); // skip the ':'

    String value = in.readCharSequence(dataLength, charset).toString();

    in.skipBytes(1); // skip the ','

    out.add(value);
  }

  private int readLength(ByteBuf buffer, final int length) {
    byte[] data = new byte[length];

    buffer.readBytes(data);

    return Integer.parseInt(new String(data, charset));
  }
}
