package main.int21_int94_0_9;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.talend.camel.TalendComponent;

public class INT21_INT94_Test extends CamelTestSupport {

    @Override
    public String isMockEndpoints() {
        return "direct:*";
    }

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        INT21_INT94 route = new INT21_INT94();
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

        INT21_INT94_Registry registry = new INT21_INT94_Registry(camelContext.getRegistry());

        // myFileFilter bean
        beans.INT94_FilterFileRetek myFileFilter = new beans.INT94_FilterFileRetek();
        registry.register("myFileFilter", myFileFilter);

        // myTrgFilter bean
        beans.FilterFileRetekTrg myTrgFilter = new beans.FilterFileRetekTrg();
        registry.register("myTrgFilter", myTrgFilter);

        camelContext.setRegistry(registry);

        return camelContext;
    }

    @Test
    public void testRoute() throws Exception {
        // TODO
    }

}
