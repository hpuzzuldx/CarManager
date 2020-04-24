package com.shumeipai.mobilecontroller.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolService {

    private static final ExecutorService SERVICE = Executors.newFixedThreadPool( 3 );

    private ThreadPoolService() {
    }

    public static void execute( Runnable task ) {
        SERVICE.execute( task );
    }
}