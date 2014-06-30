package main.int26_0_5;

import beans.JSONFaultGeneratorBean;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class INT26_Test extends CamelTestSupport {

    @Override
    public RouteBuilder createRouteBuilder() throws Exception {
        INT26 route = new INT26();
        route.readContextValues("Default");
        route.initUriMap();
        return route;
    }

    public CamelContext createCamelContext() throws Exception {
        DefaultCamelContext camelContext = (DefaultCamelContext) super.createCamelContext();

        INT26_Registry registry = new INT26_Registry(camelContext.getRegistry());

        // CXF_PAYLOAD_HEADER_FILTER bean
        CxfConsumerSoapHeaderFilter cxfConsumerSoapHeaderFilter = new CxfConsumerSoapHeaderFilter();
        registry.register("CXF_PAYLOAD_HEADER_FILTER", cxfConsumerSoapHeaderFilter);

        // extractException bean
        beans.ExtractExceptionBean extractExceptionBean = new beans.ExtractExceptionBean();
        registry.register("extractException", extractExceptionBean);

        // jsonErrorGenerator bean
        beans.JSONFaultGeneratorBean jsonFaultGeneratorBean = new JSONFaultGeneratorBean();
        registry.register("jsonErrorGenerator", jsonFaultGeneratorBean);

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
