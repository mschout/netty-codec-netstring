package io.github.mschout.netty.codec.netstring;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class ByteToNetstringDecoderTest {
  private final Charset charset = StandardCharsets.UTF_8;

  @Test
  public void decodeNetstrings() {
    List<String> testStrings = ImmutableList.of("a", "foo", "netstring,with,commas", "netstring-with\nembedded\nnewlines");

    EmbeddedChannel channel = new EmbeddedChannel(new ByteToNetstringDecoder(1024, charset));

    ByteBuf buffer = Unpooled.buffer();

    // write the netstrings to the buffer
    testStrings
      .stream()
      .map(i -> String.format("%d:%s,", i.length(), i))
      .forEach(netString -> buffer.writeCharSequence(netString, StandardCharsets.UTF_8));

    ByteBuf input = buffer.duplicate();

    channel.writeInbound(input.retain());

    for (String value : testStrings) {
      String read = channel.readInbound();
      assertEquals(value, read, "read value " + value);
    }

    assertFalse(channel.finish(), "Closed channel, we read all of the bytes");
  }
}
