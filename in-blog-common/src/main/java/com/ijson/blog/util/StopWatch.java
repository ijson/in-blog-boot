package com.ijson.blog.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StopWatch {

    private long startTime;

    private long lapStartTime;

    private String tagName;

    private List<String> steps = new ArrayList<>();

    public static StopWatch create(String tagName) {

        return new StopWatch(tagName);
    }

    private StopWatch(String tagName) {
        this.tagName = tagName;

        long start = System.nanoTime();

        this.startTime = start;
        this.lapStartTime = start;
    }

    public void lap(String stepName) {

        long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - lapStartTime);
        int index = steps.size() + 1;
        String step = String.format("/T%d(%s):%d", index, stepName, elapsedTime);
        steps.add(step);

        //reset
        this.lapStartTime = System.nanoTime();
    }

    public void log() {
        StringBuilder stringBuilder = createLog();
        log.warn(stringBuilder.toString());
    }

    public void logSlow(long slow) {

        long totalElapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        if (totalElapsedTime > slow) {
            StringBuilder stringBuilder = createLog();
            log.warn(stringBuilder.toString());
        }
    }

    private StringBuilder createLog() {
        long totalElapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

        StringBuilder stringBuilder = new StringBuilder(tagName);
        stringBuilder.append(' ');
        stringBuilder.append(" Total:");
        stringBuilder.append(totalElapsedTime);
        stringBuilder.append(' ');

        for (String step : steps) {
            stringBuilder.append(step);
            stringBuilder.append(' ');
        }
        return stringBuilder;
    }

}
