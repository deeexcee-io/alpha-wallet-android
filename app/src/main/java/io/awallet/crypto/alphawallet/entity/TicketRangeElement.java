package io.awallet.crypto.alphawallet.entity;

import org.web3j.abi.datatypes.generated.Bytes32;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.awallet.crypto.alphawallet.ui.widget.adapter.TicketAdapter;

public class TicketRangeElement
{
    public Bytes32 id;
    public short category;
    public int ticketNumber;
    public short venue;
    public short match;

    public static void sortElements(List<TicketRangeElement> elementList)
    {
        Collections.sort(elementList, new Comparator<TicketRangeElement>() {
            public int compare(TicketRangeElement e1, TicketRangeElement e2) {
                long w1 = ((long)e1.venue<<32) + ((long)e1.match<<24) + ((long)e1.category<<16) + e1.ticketNumber;
                long w2 = ((long)e2.venue<<32) + ((long)e2.match<<24) + ((long)e2.category<<16) + e2.ticketNumber;
                if (w1 > w2) return 1;
                if (w1 < w2) return -1;
                return 0;
            }});
    }
}
