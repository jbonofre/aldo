package main.int06_1_3;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import javax.jms.JMSException;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;

public class INT06_Test extends CamelTestSupport {

    @Override
    public String isMockEndpoints() {
        return "cJMSConnectionFactory1:*";
    }

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        INT06 route = new INT06();

        route.readContextValues("Default");

        route.initUriMap();

        return route;
    }

    @Override
    public CamelContext createCamelContext() throws Exception {
        DefaultCamelContext camelContext = (DefaultCamelContext) super.createCamelContext();
        INT06_Registry contextRegister = new INT06_Registry(camelContext.getRegistry());

        // extractException bean
        beans.ExtractExceptionBean bean = new beans.ExtractExceptionBean();
        contextRegister.register("extractException", bean);

        camelContext.setRegistry(contextRegister);

        javax.jms.ConnectionFactory jmsConnectionFactory = new org.apache.activemq.ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        camelContext.addComponent("cJMSConnectionFactory1", org.apache.camel.component.jms.JmsComponent.jmsComponent(jmsConnectionFactory));

        return camelContext;
    }

    @Test
    public void testCorrectUsAldoFileNameHeader() throws Exception {
        MockEndpoint omsQueueMock = getMockEndpoint("mock:cJMSConnectionFactory1:queue:ALDO.OMS.ITEM.MANAGE.IN");

        omsQueueMock.setMinimumExpectedMessageCount(1);
        omsQueueMock.expectedBodiesReceived();

        omsQueueMock.whenAnyExchangeReceived(new Processor() {
            public void process(Exchange exchange) throws Exception {
                throw new JMSException("FOO");
            }
        });

        String enterpriseNameHeader = "USALDO";
        List<List<String>> body = new ArrayList<List<String>>();

        // create headers
        List<String> headers = new ArrayList<String>();
        headers.add("sizes");
        headers.add("ProductPageUrl");
        headers.add("publishToAmazon");
        body.add(headers);

        // create item
        List<String> item = new ArrayList<String>();
        item.add("95229069-12-37:37,95229069-12-37-:37");
        body.add(item);

        // I should have XML + numberOfLines header + banner header
        // CamelFileName header

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("SampleFile.csv");

        template.sendBody("cJMSConnectionFactory1:queue:ALDO.INT06.FILE.IN", stream);

        Map<String, Object> camelHeaders = new HashMap<String, Object>();
        camelHeaders.put("enterpriseName", enterpriseNameHeader);
        camelHeaders.put("CamelFileName", "/tmp/foobar.csv");
        template.sendBodyAndHeaders("cJMSConnectionFactory1:queue:ALDO.INT06.FILE.IN", body, camelHeaders);

        FileReader reader = new FileReader(new File(this.getClass().getClassLoader().getResource("OutFile.cvs").toURI()));
        StringBuilder builder = new StringBuilder();
        int c = reader.read();
        while (c != 0) {
            builder.append(c);
            c = reader.read();
        }


        assertMockEndpointsSatisfied();

        assertEquals(omsQueueMock.getExchanges().get(0).getIn().getBody(String.class), builder.toString());


        for (Exchange exchange : omsQueueMock.getExchanges()) {
            System.out.println(exchange);
        }
    }


}
