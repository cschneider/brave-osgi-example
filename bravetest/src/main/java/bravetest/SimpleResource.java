package bravetest;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.jaxrs2.BraveTraceFeature;

@Component(immediate = true)
@Path("")
public class SimpleResource {
    private int current;
    private Server server;
    
    @Reference
    Brave brave;

    @Activate
    public void activate() {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setServiceClass(SimpleResource.class);
        factory.setResourceProvider(SimpleResource.class, new SingletonResourceProvider(this));
        factory.setProviders(Arrays.asList(new BraveTraceFeature(brave)));
        factory.setAddress("/simple");
        server = factory.create();
    }

    @Deactivate
    public void deactivate() {
        server.stop();
    }

    @GET
    public String get() {
        return brave.localTracer().trace(SimpleResource.class.getName(), "create value", () -> {
            sleep(100);
            brave.localTracer().submitAnnotation("Counter before call: " + current);
            sleep(200);
            return new Integer(current++).toString();
        });
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
