package main.int19_0_3;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.talend.camel.TalendComponent;

public class INT19_Test extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        INT19 route = new INT19();
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

        INT19_Registry registry = new INT19_Registry(camelContext.getRegistry());

        // CXF_PAYLOAD_HEADER_FILTER bean
        CxfConsumerSoapHeaderFilter cxfConsumerSoapHeaderFilter = new CxfConsumerSoapHeaderFilter();
        registry.register("CXF_PAYLOAD_HEADER_FILTER", cxfConsumerSoapHeaderFilter);

        // jsonErrorGenerator bean
        beans.JSONFaultGeneratorBean jsonErrorGenerator = new beans.JSONFaultGeneratorBean();
        registry.register("jsonErrorGenerator", jsonErrorGenerator);

        camelContext.setRegistry(registry);

        return camelContext;
    }

    @Test
    public void testRoute() throws Exception {
        // TODO
    }

    class CxfConsumerSoapHeaderFilter extends org.apache.camel.component.cxf.common.header.CxfHeaderFilterStrategy {
        public boolean applyFilterToCamelHeaders(String headerName, Object headerValue, org.apache.camel.Exchange exchange) {
            if (org.apache.cxf.headers.Header.HEADER_LIST.equals(headerName)) {
                return true;
            }
            return super.applyFilterToCamelHeaders(headerName, headerValue,
                    exchange);
        }

        public boolean applyFilterToExternalHeaders(String headerName, Object headerValue, org.apache.camel.Exchange exchange) {
            if (org.apache.cxf.headers.Header.HEADER_LIST.equals(headerName)) {
                return true;
            }
            return super.applyFilterToExternalHeaders(headerName, headerValue,
                    exchange);
        }
    }

}
