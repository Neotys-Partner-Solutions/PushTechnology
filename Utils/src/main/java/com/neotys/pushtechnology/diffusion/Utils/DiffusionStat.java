package com.neotys.pushtechnology.diffusion.Utils;

import java.util.HashMap;

/**
 * Created by hrexed on 23/02/18.
 */
public class DiffusionStat {
    public static DiffusionStat instance=new DiffusionStat();
    private long NumberOfEntries=0;
    private long NumberOfGlobalMessage=0;
    private double NumnberOfbytes=0;
    private long NumberOfJsonmessage=0;
    private double NumnberOfJsonbytes=0;

    private long NumberOfBinaryMessage=0;
    private double NumnberOfBinarybytes=0;

    private long NumberOfStringMessage=0;
    private double NumnberOfStringbytes=0;

    private long NumberOfInt64Message=0;
    private double NumnberOfInt64bytes=0;

    private long NumberOfDoubleMessage=0;
    private double NumnberOfDoublebytes=0;


    private void init()
    {
        NumberOfEntries=0;
        NumberOfGlobalMessage=0;
        NumnberOfbytes=0;
        NumberOfJsonmessage=0;
        NumnberOfJsonbytes=0;

        NumberOfBinaryMessage=0;
        NumnberOfBinarybytes=0;

        NumberOfStringMessage=0;
        NumnberOfStringbytes=0;

        NumberOfInt64Message=0;
        NumnberOfInt64bytes=0;

        NumberOfDoubleMessage=0;
        NumnberOfDoublebytes=0;
    }

    public synchronized void  AddJsonStat(double bytes)
    {
        NumberOfEntries++;
        NumberOfGlobalMessage++;
        NumnberOfbytes+=bytes;
        NumberOfJsonmessage++;
        NumnberOfJsonbytes+=bytes;

    }
    public synchronized void AddStringStat(double bytes)
    {
        NumberOfEntries++;
        NumberOfGlobalMessage++;
        NumnberOfbytes+=bytes;
        NumberOfStringMessage++;
        NumnberOfStringbytes+=bytes;

    }
    public synchronized void AddBinaryStat(double bytes)
    {
        NumberOfEntries++;
        NumberOfGlobalMessage++;
        NumnberOfbytes+=bytes;
        NumberOfBinaryMessage++;
        NumnberOfBinarybytes+=bytes;

    }

    public synchronized void AddInt64Stat(double bytes)
    {
        NumberOfEntries++;
        NumberOfGlobalMessage++;
        NumnberOfbytes+=bytes;
        NumberOfInt64Message++;
        NumnberOfInt64bytes+=bytes;

    }
    public synchronized void AddDoubeStat(double bytes)
    {
        NumberOfEntries++;
        NumberOfGlobalMessage++;
        NumnberOfbytes+=bytes;
        NumberOfDoubleMessage++;
        NumnberOfDoublebytes+=bytes;

    }

    public synchronized  HashMap<String,Double> GetAggregate()
    {
        HashMap<String,Double> result=new HashMap<String, Double>();
        result.put("NumberOfMessage",(double)NumberOfGlobalMessage);
        if(NumberOfGlobalMessage>0)
           result.put("NumberOfBytes",NumnberOfbytes/NumberOfGlobalMessage);
        else
            result.put("NumberOfBytes",(double)0);

        result.put("NumberOfJsonmessage",(double)NumberOfJsonmessage);
        if(NumnberOfJsonbytes>0)
             result.put("NumnberOfJsonbytes",NumnberOfJsonbytes/NumberOfJsonmessage);
        else
            result.put("NumnberOfJsonbytes",(double)0);
        result.put("NumberOfStringMessage",(double)NumberOfStringMessage);
        if(NumnberOfStringbytes>0)
            result.put("NumnberOfStringbytes",NumnberOfStringbytes/NumberOfStringMessage);
        else
            result.put("NumnberOfStringbytes",(double)0);
        result.put("NumberOfBinaryMessage",(double)NumberOfBinaryMessage);
        if(NumnberOfBinarybytes>0)
          result.put("NumnberOfBinarybytes",NumnberOfBinarybytes/NumberOfBinaryMessage);
        else
        result.put("NumnberOfBinarybytes",(double)0);
        result.put("NumberOfInt64Message",(double)NumberOfInt64Message);
        if(NumnberOfInt64bytes>0)
            result.put("NumnberOfInt64bytes",NumnberOfInt64bytes/NumberOfInt64Message);
        else
            result.put("NumnberOfInt64bytes",(double)0);
        result.put("NumberOfDoubleMessage",(double)NumberOfDoubleMessage);
        if(NumberOfDoubleMessage>0)
            result.put("NumnberOfDoublebytes",NumnberOfDoublebytes/NumberOfDoubleMessage);
        else
            result.put("NumnberOfDoublebytes",(double)0);


        init();
        return result;
    }
}
