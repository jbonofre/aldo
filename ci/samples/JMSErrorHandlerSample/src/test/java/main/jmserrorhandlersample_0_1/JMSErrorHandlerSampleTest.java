package main.jmserrorhandlersample_0_1;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

public class JMSErrorHandlerSampleTest extends CamelTestSupport {

    @Override
    public String isMockEndpoints() {
        return "cJMSConnectionFactory1:*";
    }

    @Override
    public RouteBuilder createRouteBuilder() {
        JMSErrorHandlerSample route = new JMSErrorHandlerSample();
        route.initUriMap();
        return route;
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        DefaultCamelContext camelContext = (DefaultCamelContext) super.createCamelContext();

        //SpringCamelContextFactory contextFactory = new SpringCamelContextFactory();
        //contextFactory.setApplicationContext(new ClassPathXmlApplicationContext("META-INF/spring/jmserrorhandlersample.xml"));
        //camelContext = contextFactory.createContext();
        //camelContext.setName("JMSErrorHandlerSample-ctx");

        JMSErrorHandlerSample_Registry contextRegister = new JMSErrorHandlerSample_Registry(camelContext.getRegistry());
        camelContext.setRegistry(contextRegister);

        javax.jms.ConnectionFactory jmsConnectionFactory = new org.apache.activemq.ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("cJMSConnectionFactory1", org.apache.camel.component.jms.JmsComponent.jmsComponent(jmsConnectionFactory));

        return camelContext;
    }

    @Test
    @Ignore
    public void testNoError() throws Exception {
        MockEndpoint jmsStopMock = getMockEndpoint("mock:cJMSConnectionFactory1:queue:stop");

        jmsStopMock.expectedMessageCount(1);
        jmsStopMock.expectedBodiesReceived("Foobar");

        template.sendBody("cJMSConnectionFactory1:queue:start", "Foobar");

        assertMockEndpointsSatisfied();
    }

}
