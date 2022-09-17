package com.jalgoz.encuesta;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

// Para poder obtener @Bean en las demás clases, en otras palabras creamos el contexto que nos permitira acceder al @Bean creado en EncuestaApplication.java
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static Object getBean(String beanName) {
        return CONTEXT.getBean(beanName);
    }
}
