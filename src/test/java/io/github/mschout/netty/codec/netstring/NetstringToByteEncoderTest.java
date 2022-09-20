package io.github.mschout.netty.codec.netstring;

import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class NetstringToByteEncoderTest {
  private final Charset charset = StandardCharsets.UTF_8;

  @Test
  public void encodeNetstring() {
    List<String> testStrings = ImmutableList.of("a", "foo", "netstring,with,commas", "netstring-with\nembedded\nnewlines");

    EmbeddedChannel channel = new EmbeddedChannel(new NetstringToByteEncoder(StandardCharsets.UTF_8));

    for (String value : testStrings) {
      channel.writeOutbound(value);
    }

    for (String value : testStrings.stream().map(i -> String.format("%d:%s,", i.length(), i)).collect(Collectors.toList())) {
      ByteBuf actual = channel.readOutbound();
      assertEquals(value, actual.toString(charset), "Encoded value " + value);
    }

    assertFalse(channel.finish(), "Closed channel, all bytes read");
  }
}
