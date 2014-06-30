package main.int27b_0_5;

import beans.ExtractExceptionBean;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.talend.camel.TalendComponent;

public class INT27B_Test extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        INT27B route = new INT27B();
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

        // cJMSConnectionFactory2 component
        javax.jms.ConnectionFactory jmsConnectionFactory = new org.apache.activemq.ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("cJMSConnectionFactory2", org.apache.camel.component.jms.JmsComponent.jmsComponent(jmsConnectionFactory));

        INT27B_Registry registry = new INT27B_Registry(camelContext.getRegistry());

        // extractException bean
        beans.ExtractExceptionBean extractExceptionBean = new ExtractExceptionBean();
        registry.register("extractException", extractExceptionBean);

        camelContext.setRegistry(registry);

        return camelContext;
    }

    @Test
    public void testRoute() throws Exception {
        // TODO
    }

}
