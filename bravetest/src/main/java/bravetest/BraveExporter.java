package bravetest;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.github.kristofa.brave.Brave;

import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.urlconnection.URLConnectionSender;

@Component(immediate=true)
public class BraveExporter {

    private AsyncReporter<Span> reporter;
    private ServiceRegistration<Brave> reg;
    
    @Activate
    public void activate(BundleContext context) {
        URLConnectionSender sender = URLConnectionSender.builder()
            .endpoint("http://localhost:9411/api/v1/spans").build();
        //KafkaSender sender = KafkaSender.builder().bootstrapServers("kafka:9092").build();
        reporter = AsyncReporter.builder(sender).build();
        Brave brave = create();
        reg = context.registerService(Brave.class, brave, null);
    }
    
    public Brave create() {
        return create("bravetest3");
    }
    
    public Brave create(String name) {
        return new Brave.Builder(name).reporter(reporter).build();
    }
    
    @Deactivate
    public void deactivate() {
        reg.unregister();
        reporter.close();
    }
}
