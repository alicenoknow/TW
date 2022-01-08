package lab7;

import org.jcsp.lang.*;

import java.util.ArrayList;
import java.util.Random;

public class Producer implements CSProcess {
    private final AltingChannelInputInt[] buffersInputs;
    private final ChannelOutputInt[] buffersOutputs;
    private final int buffersCount;
    private final int id;
    private int trials = 0;
    private int successes = 0;

    public Producer (int id, AltingChannelInputInt[] buffersInputs, ChannelOutputInt[] buffersOutputs, int buffersCount) {
        this.buffersOutputs = buffersOutputs;
        this.buffersInputs = buffersInputs;
        this.buffersCount = buffersCount;
        this.id = id;
    }

    public void run () {
        int item, selectedBuffer;
        Random random = new Random(78);

        while (true) {
            selectedBuffer = random.nextInt(buffersCount);
            buffersOutputs[selectedBuffer].write(1);
            this.trials++;

            final Alternative alt = new Alternative(this.buffersInputs);
            int index = alt.select();
            item = buffersInputs[index].read();
            if (item < 0) {
//                System.out.println("Producer " + this.id + " unable to put item to: " + index);
                System.out.println("Producer " + this.id + " average waiting time: " + (float)this.trials/(float)(Math.max(1, this.successes)));
                continue;
            }
            this.successes++;
            System.out.println("Producer " + this.id + " average waiting time: " + (float)this.trials/(float)this.successes);
        }
    }
}

