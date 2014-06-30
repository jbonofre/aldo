package main.int52_0_7;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.talend.camel.TalendComponent;

public class INT52_Test extends CamelTestSupport {

    @Override
    public String isMockEndpoints() {
        return "cJMSConnectionFactory1:*";
    }

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        INT52 route = new INT52();
        route.readContextValues("Default");
        route.initUriMap();
        return route;
    }

    @Override
    public CamelContext createCamelContext() throws Exception {
        DefaultCamelContext camelContext = (DefaultCamelContext) super.createCamelContext();

        // talend component (for the cTalendJob)
        TalendComponent talendComponent = new TalendComponent();
        camelContext.addComponent("talend", talendComponent);

        // cJMSConnectionFactory1 component (for jms)
        javax.jms.ConnectionFactory jmsConnectionFactory = new org.apache.activemq.ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("cJMSConnectionFactory1", org.apache.camel.component.jms.JmsComponent.jmsComponent(jmsConnectionFactory));

        INT52_Registry registry = new INT52_Registry(camelContext.getRegistry());

        // extractException bean
        beans.ExtractExceptionBean bean = new beans.ExtractExceptionBean();
        registry.register("extractException", bean);

        // myFileFilter bean
        beans.Int52FilterFileRetek myFileFilter = new beans.Int52FilterFileRetek();
        registry.register("myFileFilter", myFileFilter);

        camelContext.setRegistry(registry);

        return camelContext;
    }

    @Test
    public void testRoute() throws Exception {
        // TODO
    }

}
