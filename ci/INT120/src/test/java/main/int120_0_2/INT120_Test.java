package main.int120_0_2;

import beans.FilterFileRetekTrg;
import beans.INT120_FilterFile;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class INT120_Test extends CamelTestSupport {

    @Override
    public String isMockEndpoints() {
        return "file:*";
    }

    @Override
    public RouteBuilder createRouteBuilder() {
        INT120 route = new INT120();
        route.readContextValues("Default");
        route.initUriMap();
        return route;
    }

    @Override
    public CamelContext createCamelContext() throws Exception {
        DefaultCamelContext camelContext = (DefaultCamelContext) super.createCamelContext();
        INT120_Registry registry = new INT120_Registry(camelContext.getRegistry());

        // extractException bean
        beans.ExtractExceptionBean bean = new beans.ExtractExceptionBean();
        registry.register("extractException", bean);

        // filterFile bean
        beans.INT120_FilterFile filterFile = new INT120_FilterFile();
        registry.register("filterFile", filterFile);

        // trgFilter bean
        beans.FilterFileRetekTrg trgFilter = new FilterFileRetekTrg();
        registry.register("trgFilter", trgFilter);

        camelContext.setRegistry(registry);

        // JMS component
        javax.jms.ConnectionFactory jmsConnectionFactory = new org.apache.activemq.ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("cJMSConnectionFactory1", org.apache.camel.component.jms.JmsComponent.jmsComponent(jmsConnectionFactory));

        return camelContext;
    }

    @Test
    public void testRoute() throws Exception {
        // TODO
    }

}
