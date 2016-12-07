package bravetest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.cxf.feature.LoggingFeature;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;

@Component(immediate = true, service = TraceMain.class)
public class TraceMain {
    @Reference
    Brave brave;

    public static void main(String[] args) {
        TraceMain main = new TraceMain();
        BraveExporter exporter = new BraveExporter();
        main.brave = exporter.create();
        main.activate();
    }


    @Activate
    public void activate() {
        Client client = ClientBuilder.newBuilder()
            .register(BraveTracingFeature.create(brave))
            .register(new LoggingFeature())
            .build();
        try {
            brave.localTracer().startNewSpan("TraceMain", "activate");
            for (int c = 0; c < 3; c++) {
                try {
                    System.out.println("Read simple resource");
                    client.target("http://localhost:8181/cxf/simple").request().async().get(String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            brave.localTracer().finishSpan();
        }
    }
}
