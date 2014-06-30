package main.int17_0_6;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class INT17_Test extends CamelTestSupport {

    @Override
    public String isMockEndpoints() {
        return "*";
    }

    @Override
    public RouteBuilder createRouteBuilder() {
        INT17 route = new INT17();
        route.readContextValues("Default");
        route.initUriMap();
        return route;
    }

    @Override
    public CamelContext createCamelContext() throws Exception {
        DefaultCamelContext camelContext = (DefaultCamelContext) super.createCamelContext();
        INT17_Registry contextRegister = new INT17_Registry(camelContext.getRegistry());

        // extractException bean
        beans.ExtractExceptionBean bean = new beans.ExtractExceptionBean();
        contextRegister.register("extractException", bean);

        camelContext.setRegistry(contextRegister);

        javax.jms.ConnectionFactory jmsConnectionFactory = new org.apache.activemq.ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("cJMSConnectionFactory1", org.apache.camel.component.jms.JmsComponent.jmsComponent(jmsConnectionFactory));

        return camelContext;
    }

    @Test
    public void testRoute() throws Exception {
        // TODO
    }

}
