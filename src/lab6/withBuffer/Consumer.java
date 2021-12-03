package lab6.withBuffer;

import org.jcsp.lang.*;

public class Consumer implements CSProcess {
    private final AltingChannelInputInt in;
    private final ChannelOutputInt req;

    public Consumer (final ChannelOutputInt req, final AltingChannelInputInt in) {
        this.req = req;
        this.in = in;
    }

    public void run () {
        int item;
        while (true) {
            req.write(0);
            item = in.read();
            if (item < 0)
                break;
            System.out.println(item);
        }
        System.out.println("Consumer ended.");
    }
}
