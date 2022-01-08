package lab7;

import org.jcsp.lang.*;

/***
 * Communication:
 *  producers and consumers randomly pick a buffer and send request:
 *      - producer sends message with code 1 as a request to put item
 *      - consumer sends message with code 2 as a request to take item
 *
 *  buffer replies with:
 *      - message with code 0 if operation succeeded
 *      - message with code -1 if operation failed
 */


public final class Main {
    public static void main(String[] args) {
        int consumers = 20;
        int producers = 20;
        int buffers = 50;

        // ------------------------ Channels ------------------------

        // Producer <-> Buffer
        final One2OneChannelInt[][] producer2BufferChannels = new One2OneChannelInt[producers][buffers];
        final One2OneChannelInt[][] buffer2ProducerChannels = new One2OneChannelInt[producers][buffers];
        for (int i = 0; i < producers; i++) {
            for (int j = 0; j < buffers; j++) {
                producer2BufferChannels[i][j] = Channel.one2oneInt();
                buffer2ProducerChannels[i][j] = Channel.one2oneInt();
            }
        }

        // Consumer <-> Buffer
        final One2OneChannelInt[][] consumer2BufferChannels = new One2OneChannelInt[consumers][buffers];
        final One2OneChannelInt[][] buffer2ConsumerChannels = new One2OneChannelInt[consumers][buffers];
        for (int i = 0; i < consumers; i++) {
            for (int j = 0; j < buffers; j++) {
                consumer2BufferChannels[i][j] = Channel.one2oneInt();
                buffer2ConsumerChannels[i][j] = Channel.one2oneInt();
            }
        }

        // ------------------------ Processes ------------------------

        CSProcess[] processes = new CSProcess[buffers + producers + consumers];

        // Buffers processes
        for (int i = 0; i < buffers; i++) {
            AltingChannelInputInt[] inputs = new AltingChannelInputInt[producers+consumers];
            ChannelOutputInt[] outputs = new ChannelOutputInt[producers+consumers];
            for (int j = 0; j < producers; j++) {
                inputs[j] = producer2BufferChannels[j][i].in();
                outputs[j] = buffer2ProducerChannels[j][i].out();
            }

            for (int j = 0; j < consumers; j++) {
                inputs[j+producers] = consumer2BufferChannels[j][i].in();
                outputs[j+producers] = buffer2ConsumerChannels[j][i].out();
            }
            processes[i] = new Buffer(inputs, outputs);
        }

        // Consumers processes
        for (int i = 0; i < consumers; i++) {
            AltingChannelInputInt[] buffersInputs = new AltingChannelInputInt[buffers];
            ChannelOutputInt[] buffersOutputs = new ChannelOutputInt[buffers];
            for (int j = 0; j < buffers; j++) {
                buffersInputs[j] = buffer2ConsumerChannels[i][j].in();
                buffersOutputs[j] = consumer2BufferChannels[i][j].out();
            }
            processes[i+buffers] = new Consumer(
                    i,
                    buffersInputs,
                    buffersOutputs,
                    buffers
            );
        }

        // Producers processes
        for (int i = 0; i < producers; i++) {
            AltingChannelInputInt[] buffersInputs = new AltingChannelInputInt[buffers];
            ChannelOutputInt[] buffersOutputs = new ChannelOutputInt[buffers];
            for (int j = 0; j < buffers; j++) {
                buffersInputs[j] = buffer2ProducerChannels[i][j].in();
                buffersOutputs[j] = producer2BufferChannels[i][j].out();
            }
            processes[i + buffers + consumers] = new Producer(
                    i,
                    buffersInputs,
                    buffersOutputs,
                    buffers
            );
        }

        // Start
        Parallel par = new Parallel(processes);
        par.run();
    }
}

