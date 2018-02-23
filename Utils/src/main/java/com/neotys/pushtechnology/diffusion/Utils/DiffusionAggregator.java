package com.neotys.pushtechnology.diffusion.Utils;

import com.neotys.rest.dataexchange.client.DataExchangeAPIClient;
import com.neotys.rest.dataexchange.model.EntryBuilder;
import com.neotys.rest.error.NeotysAPIException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * Created by hrexed on 23/02/18.
 */
public class DiffusionAggregator extends TimerTask {
     DataExchangeAPIClient client;
     EntryBuilder entry;
     DiffusionStat stat;

    public DiffusionAggregator( DataExchangeAPIClient c)
    {
        client=c;
        stat=DiffusionStat.instance;
    }

    private void CreateEntry(String Cat, String metricname, double value, String unit) throws GeneralSecurityException, IOException, URISyntaxException, NeotysAPIException
    {
        entry=new EntryBuilder(Arrays.asList("Diffusion", Cat, metricname), System.currentTimeMillis());
        entry.unit(unit);
        entry.value(value);
        client.addEntry(entry.build());
    }

    public void run() {

        HashMap<String, Double> map;
        if(stat!=null)
        {
            map=stat.GetAggregate();
            try {
                CreateEntry("Global", "NumberOfMessage",(double)map.get("NumberOfMessage"),"nb message");
                CreateEntry("Global","TotalBytes",(double)map.get("NumberOfBytes"),"byte");
                CreateEntry("JSON","NumnberOfJsonbytes",(double)map.get("NumnberOfJsonbytes"),"byte");
                CreateEntry("JSON","NumberOfJsonmessage",(double)map.get("NumberOfJsonmessage"),"nb message");
                CreateEntry("String","NumnberOfStringbytes",(double)map.get("NumnberOfStringbytes"),"byte");
                CreateEntry("String","NumberOfStringMessage",(double)map.get("NumberOfStringMessage"),"nb message");
                CreateEntry("Binary","NumberOfBinaryMessage",(double)map.get("NumberOfBinaryMessage"),"nb message");
                CreateEntry("Binary","NumnberOfBinarybytes",(double)map.get("NumnberOfBinarybytes"),"byte");
                CreateEntry("INT64","NumberOfInt64Message",(double)map.get("NumberOfInt64Message"),"nb message");
                CreateEntry("INT64","NumnberOfInt64bytes",(double)map.get("NumnberOfInt64bytes"),"byte");
                CreateEntry("Double","NumberOfDoubleMessage",(double)map.get("NumberOfDoubleMessage"),"nb message");
                CreateEntry("Double","NumnberOfDoublebytes",(double)map.get("NumnberOfDoublebytes"),"byte");

            } catch (GeneralSecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NeotysAPIException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
