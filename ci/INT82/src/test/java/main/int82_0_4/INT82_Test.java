package main.int82_0_4;

import beans.Int82FilterFileRetek;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.talend.camel.TalendComponent;

public class INT82_Test extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        INT82 route = new INT82();
        route.readContextValues("Default");
        route.initUriMap();
        return route;
    }

    @Override
    public CamelContext createCamelContext() throws Exception {
        DefaultCamelContext camelContext = (DefaultCamelContext) super.createCamelContext();

        // talend component
        TalendComponent talendComponent = new TalendComponent();
        camelContext.addComponent("talend", talendComponent);

        // cJMSConnectionFactory1 component
        javax.jms.ConnectionFactory jmsConnectionFactory = new org.apache.activemq.ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("cJMSConnectionFactory1", org.apache.camel.component.jms.JmsComponent.jmsComponent(jmsConnectionFactory));

        // registry
        INT82_Registry registry = new INT82_Registry(camelContext.getRegistry());

        // myFileFilter bean
        beans.Int82FilterFileRetek int82FilterFileRetek = new Int82FilterFileRetek();
        registry.register("myFileFilter", int82FilterFileRetek);

        camelContext.setRegistry(registry);

        return camelContext;
    }

    @Test
    public void testRoute() throws Exception {
        // TODO
    }

}
