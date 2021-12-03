package lab6.withBuffer;

import org.jcsp.lang.*;

public class Buffer implements CSProcess {
    private final One2OneChannelInt[] prod;
    private final One2OneChannelInt[] req;
    private final One2OneChannelInt[] cons;
    private final int[] buffer = new int[10];
    int hd = -1;
    int tl = -1;
    public Buffer (final One2OneChannelInt[] in, final One2OneChannelInt[] req, final One2OneChannelInt[] out) {
        this.prod = in;
        this.req = req;
        this.cons = out;
    }
    public void run () {
        final AltingChannelInputInt[] guards = {prod[0].in(), prod[1].in(), req[0].in(), req[1].in() };
        final Alternative alt = new Alternative(guards);
        int countdown = 4;
        while (countdown > 0) {
            int index = alt.select();
            switch (index) {
                case 0:
                case 1:
                    if (hd < tl + 11) {
                         int item = prod[index].in().read();
                        if (item < 0)
                            countdown--;
                        else {
                            hd++;
                            buffer[hd % buffer.length] = item;
                        }
                    }
                    break;
                case 2:
                case 3:
                    if (tl < hd) {
                        req[index-2].in().read();
                        tl++;
                        int item = buffer[tl % buffer.length];
                        cons[index-2].out().write(item);
                    }
                    else if (countdown <= 2) {
                        req[index-2].in().read();
                        cons[index-2].out().write(-1);
                        countdown--;
                    }
                break;
            }
        }
        System.out.println("Buffer ended.");
    }
}
