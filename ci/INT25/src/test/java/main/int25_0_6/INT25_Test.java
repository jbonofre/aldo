package main.int25_0_6;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class INT25_Test extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        INT25 route = new INT25();
        route.readContextValues("Default");
        route.initUriMap();
        return route;
    }

    @Override
    public CamelContext createCamelContext() throws Exception {
        DefaultCamelContext camelContext = (DefaultCamelContext) super.createCamelContext();

        // cJMSConnectionFactory1 component
        javax.jms.ConnectionFactory jmsConnectionFactory = new org.apache.activemq.ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("cJMSConnectionFactory1", org.apache.camel.component.jms.JmsComponent.jmsComponent(jmsConnectionFactory));

        return camelContext;
    }

    @Test
    public void testRoute() throws Exception {
        // TODO
    }

}
