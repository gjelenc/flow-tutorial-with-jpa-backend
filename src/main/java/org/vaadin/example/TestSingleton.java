package org.vaadin.example;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class TestSingleton {
    @Inject CustomerService customerService;

    @PostConstruct
    private void init(){
        customerService.ensureTestData();
    }
}
