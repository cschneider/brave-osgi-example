package bravetest;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.github.kristofa.brave.Brave;

import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.kafka08.KafkaSender;

@Component(immediate = true)
public class BraveExporter {

    private ServiceRegistration<Brave> reg;
    private AsyncReporter<Span> reporter;

    @Activate
    public void activate(BundleContext context, Map<String, String> config) {
        Brave brave = create();
        Dictionary<String, ?> props = new Hashtable<String, String>();
        reg = context.registerService(Brave.class, brave, props);
    }

    public Brave create() {
        /*
        URLConnectionSender sender = URLConnectionSender.builder()
            .endpoint("http://localhost:9411/api/v1/spans").build();
            */
        KafkaSender sender = KafkaSender.builder().bootstrapServers("kafka:9092").build();
        reporter = AsyncReporter.builder(sender).build();
        return new Brave.Builder("bravetest").reporter(reporter).build();
    }
    
    @Deactivate
    public void deactivate() {
        reg.unregister();
        reporter.close();
    }
}
