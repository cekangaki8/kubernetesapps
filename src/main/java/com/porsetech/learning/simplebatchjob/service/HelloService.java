/* 
created by cekangaki 
created on 9/11/22 
inside the package - com.porsetech.learning.simplebatchjob 
*/

package com.porsetech.learning.simplebatchjob.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloService {

    public void sayHello() {
        log.info("Hello World!");
    }

}
