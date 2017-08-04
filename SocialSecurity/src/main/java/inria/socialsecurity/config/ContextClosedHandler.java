/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inria.socialsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 *
 * @author adychka
 */
@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    @Autowired ThreadPoolTaskExecutor executor;
    @Autowired ThreadPoolTaskScheduler scheduler;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("closed event");
        scheduler.shutdown();
        executor.shutdown();
    }       
}
