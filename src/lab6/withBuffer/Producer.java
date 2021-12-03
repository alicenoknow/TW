package lab6.withBuffer;

import org.jcsp.lang.*;

public class Producer implements CSProcess {
    private final ChannelOutputInt channel;
    private final int start;

    public Producer (final ChannelOutputInt out, int start) {
        channel = out;
        this.start = start;
    }

    public void run () {
        int item;
        for (int k = 0; k < 100; k++) {
            item = (int)(Math.random() * 100) + 1 + start;
            channel.write(item);
        }
        channel.write(-1);
        System.out.println("Producer " + start + " ended.");
    }
}
