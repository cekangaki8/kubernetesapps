/* 
created by cekangaki 
created on 9/11/22 
inside the package - com.porsetech.learning.simplebatchjob 
*/

package com.porsetech.learning.simplebatchjob.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class HelloService {


    public void sayHello() {
        log.info("Hello World!");
        addingAWait(60);
    }

    /**
     * Method will wait for a given interval then print a log.
     *
     * @param delayInSeconds wait time in seconds.
     */
    private void addingAWait(int delayInSeconds) {
        try {
            log.info("Going to artificially wait for {} seconds", delayInSeconds);
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(this::waitMessage, delayInSeconds, TimeUnit.SECONDS);
            executorService.shutdown();
            executorService.awaitTermination(delayInSeconds*2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Exception occurred while waiting on process termination", e);
        }
    }

    private void waitMessage() {
        log.info("Completed wait, thanks.");
    }

}
