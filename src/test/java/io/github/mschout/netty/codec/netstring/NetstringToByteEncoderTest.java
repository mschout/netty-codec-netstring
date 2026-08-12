package io.github.mschout.netty.codec.netstring;

import static org.junit.jupiter.api.Assertions.*;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class NetstringToByteEncoderTest {
  private final Charset charset = StandardCharsets.UTF_8;

  @Test
  public void encodeNetstring() {
    var testStrings = List.of("a", "foo", "netstring,with,commas", "netstring-with\nembedded\nnewlines");

    var channel = new EmbeddedChannel(new NetstringToByteEncoder(StandardCharsets.UTF_8));

    for (var value : testStrings) {
      channel.writeOutbound(value);
    }

    for (var value : testStrings.stream().map(i -> String.format("%d:%s,", i.length(), i)).toList()) {
      ByteBuf actual = channel.readOutbound();
      assertEquals(value, actual.toString(charset), "Encoded value " + value);
    }

    assertFalse(channel.finish(), "Closed channel, all bytes read");
  }
}
