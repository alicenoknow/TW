package lab6.withBuffer;

import org.jcsp.lang.*;

public final class Main
{
    public static void main (String[] args) {
        final One2OneChannelInt[] prodChan = { Channel.one2oneInt(), Channel.one2oneInt()};
        final One2OneChannelInt[] consReq = { Channel.one2oneInt(), Channel.one2oneInt() };
        final One2OneChannelInt[] consChan = { Channel.one2oneInt(), Channel.one2oneInt() };
        CSProcess[] procList = { new Producer(prodChan[0].out(), 0),
                new Producer(prodChan[1].out(), 100),
                new Buffer(prodChan, consReq, consChan),
                new Consumer(consReq[0].out(), consChan[0].in()),
                new Consumer(consReq[1].out(), consChan[1].in())
        };
        Parallel par = new Parallel(procList);
        par.run();
    }
}

