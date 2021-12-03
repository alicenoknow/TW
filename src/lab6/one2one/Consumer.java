package lab6.one2one;

import org.jcsp.lang.AltingChannelInputInt;
import org.jcsp.lang.CSProcess;

public class Consumer implements CSProcess {
    private final AltingChannelInputInt in;

    public Consumer (final AltingChannelInputInt in) {
        this.in = in;
    }

    public void run () {
        int item;
        while (true) {
            item = in.read();
            if (item < 0)
                break;
            System.out.println(item);
        }
        System.out.println("Consumer ended.");
    }
}
