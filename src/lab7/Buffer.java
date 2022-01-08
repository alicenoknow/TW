package lab7;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInputInt;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutputInt;

public class Buffer implements CSProcess {
    private int buffer = 0;
    private AltingChannelInputInt[] inputs;
    private ChannelOutputInt[] outputs;

    public Buffer(AltingChannelInputInt[] inputs, ChannelOutputInt[] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public void run () {
        final AltingChannelInputInt[] guards = inputs;

        final Alternative alt = new Alternative(guards);

        while(true){
            int index = alt.select();
            int item = inputs[index].read();

            // Consumer
            if (item == 1) {
                if (this.buffer == 0) {
                    outputs[index].write(-1);
                } else {
                    outputs[index].write(0);
                    this.buffer--;
                }

            } else {
                // Producer
                if (this.buffer == 0) {
                    outputs[index].write(0);
                    this.buffer++;
                } else {
                    outputs[index].write(-1);
                }
            }
        }

    }
}
