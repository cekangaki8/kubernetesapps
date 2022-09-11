/* 
created by cekangaki 
created on 9/11/22 
inside the package - PACKAGE_NAME 
*/
package com.porsetech.learning.simplebatchjob.configuration;

import com.porsetech.learning.simplebatchjob.service.HelloService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public HelloService helloService;

    @Bean
    public Job helloJob() {
        return this.jobBuilderFactory
                .get("helloJob")
                .start(sayHelloStep())
                .build();
    }

    private Step sayHelloStep() {
        return this.stepBuilderFactory
                .get("helloStep")
                .tasklet(sayHelloTasklet())
                .build();
    }

    private MethodInvokingTaskletAdapter sayHelloTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(helloService);
        adapter.setTargetMethod("sayHello");

        return adapter;
    }




}
