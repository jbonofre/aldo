package main.int04_int92_0_12;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringBufferInputStream;

public class INT04_INT92_Test extends CamelTestSupport {

    @Override
    public String isMockEndpoints() {
        return "*";
    }

    @Override
    public RouteBuilder createRouteBuilder() {
        INT04_INT92 route = new INT04_INT92();
        route.initUriMap();
        return route;
    }

    @Test
    @Ignore
    public void testXBRInfo() throws Exception {
        String cFile_2_URI = "file:///opt/talend/Talend-5.3.1/data/DEV/INT04_INT92?noop=false&bufferSize=128&filter=#myFilterTrg&delete=true";

        MockEndpoint xbrInfoMock = getMockEndpoint("direct:getXBRInfo");
        xbrInfoMock.setExpectedMessageCount(1);

        StringBufferInputStream stream = new StringBufferInputStream("fixed size");
        template.sendBody(cFile_2_URI, stream);

        assertMockEndpointsSatisfied();
    }

}
