package main.fileerrorhandlersample_0_1;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import javax.xml.bind.ValidationException;
import java.io.IOException;

public class FileErrorHandlerSampleTest extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        FileErrorHandlerSample route = new FileErrorHandlerSample();
        route.initUriMap();
        return route;
    }

    @Test
    @Ignore
    public void testNoError() throws Exception {
        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpoints("file:*");
            }
        });

        MockEndpoint fileMock = getMockEndpoint("mock:file:/tmp");
        fileMock.setExpectedMessageCount(1);
        fileMock.expectedBodiesReceived("Foobar");

        template.sendBody("vm:start", "Foobar");

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testRedeliverableErrors() throws Exception {
        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpoints("file:*");
            }
        });

        MockEndpoint fileMock = getMockEndpoint("mock:file:/tmp");
        fileMock.setExpectedMessageCount(0);

        fileMock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                throw new IOException("Can not create target file");
            }
        });

        template.sendBody("vm:start", "Foobar");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testContentErrors() throws Exception {
        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                mockEndpoints("file:*");
                mockEndpoints("direct:*");
            }
        });

        MockEndpoint fileMock = getMockEndpoint("mock:file:/tmp");
        MockEndpoint mailDirectMock = getMockEndpoint("mock:direct:mail");

        fileMock.setExpectedMessageCount(0);
        mailDirectMock.setExpectedMessageCount(1);
        mailDirectMock.expectedBodiesReceived("Content Error Notification");

        fileMock.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                throw new ValidationException("Bad content");
            }
        });

        template.sendBody("vm:start", "Foobar");

        assertMockEndpointsSatisfied();
    }

}
