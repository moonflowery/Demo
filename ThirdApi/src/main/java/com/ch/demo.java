package com.ch;

import org.apache.commons.collections4.MapUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class demo {
    public static void main(String[] args) {
        System.out.println(1 << 30);
        System.out.println( 10 / 3);
    }

   /* private static Map dateMap = new ConcurrentSkipListMap<>();

    public static void dateHandle(Date startTime, Date endTime){

        if(startTime.after(endTime)) return;

        if (MapUtils.isEmpty(dateMap)) {

            dateMap.put(startTime, endTime);

            return;

        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String startTimeFor = sdf.format(startTime);

        String endTimeFor = sdf.format(endTime);

        Map tempMap = new TreeMap<>();

        //TreeMap能自动根据Key排序，只需要合并交集

        //1、有交集则合并

        //2、没有交集则添加

        Iterator iterator = dateMap.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry entry = (Map.Entry) iterator.next();

            Date start = entry.getKey();

            Date end = entry.getValue();

            String startFor = sdf.format(start);

            String endFor = sdf.format(end);

            if(startTime.before(start) && (endTime.after(start) ||

                    endTime.equals(start)) && (endTime.before(end) || endTime.equals(end))) {

                iterator.remove();

                dateMap.put(startTime, end);

            } else if(startTime.before(start) && endTime.after(end)) {

                iterator.remove();

                dateMap.put(startTime, endTime);

            } else if(endTime.after(end) && (startTime.after(start) ||

                    startTime.equals(start)) && (startTime.before(end) || startTime.equals(end))) {

                iterator.remove();

                dateMap.put(start, endTime);

            } else if(endTime.before(start) || startTime.after(end)) {

                if(!iterator.hasNext())

                    dateMap.put(startTime, endTime);

            }

        }

    }
*/

}
