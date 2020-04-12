package com.alphawallet.app.entity;

import android.content.Context;
import com.alphawallet.app.R;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 4/03/2018.
 *
 * A data structure that consists part of the transaction: it has the
 * parameters for a function call (currently, only transactions to a
 * contract is dealt in this class) and it is only returned by using
 * TransactionDecoder. Note that the address of the contract, the name
 * of the function called and the signature from transaction sender
 * are all not in this class.
 *
 */

public class TransactionInput
{
    public FunctionData functionData;
    public List<String> addresses;
    public List<BigInteger> paramValues;
    public List<String> sigData;
    public List<String> miscData;

    private final String ALL = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";

    public TransactionInput()
    {
        paramValues = new ArrayList<>();
        addresses = new ArrayList<>();
        sigData = new ArrayList<>();
        miscData = new ArrayList<>();
    }

    //Addresses are in 256bit format
    public boolean containsAddress(String address)
    {
        boolean hasAddr = false;
        //Scan addresses for this address
        address = Numeric.cleanHexPrefix(address);
        for (String thisAddr : addresses)
        {
            if (thisAddr != null && thisAddr.contains(address))
            {
                hasAddr = true;
                break;
            }
        }

        return hasAddr;
    }

    public String getFirstAddress() {
        String address = "";
        if (addresses.size() > 0)
        {
            address = addresses.get(0);
        }
        return address;
    }

    public String getAddress(int index)
    {
        String address = "";
        if (addresses.size() > index)
        {
            address = addresses.get(index);
        }

        return address;
    }

    //With static string as we don't have app context
    String getFirstValue()
    {
        String value = "0";
        if (miscData.size() > 0)
        {
            String firstVal = miscData.get(0);
            if (firstVal.equals(ALL))
            {
                value = "All"; //Can't localise here because no App context.
            }
            else
            {
                //convert to big integer
                BigInteger bi = new BigInteger(miscData.get(0), 16);
                value = bi.toString(10);
            }
        }
        return value;
    }
}
