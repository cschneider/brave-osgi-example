package bravetest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.LocalTracer;

@Component(immediate=true)
public class TraceMain {

    public static void main(String[] args) {
        TraceMain main = new TraceMain();
        BraveExporter exporter = new BraveExporter();
        main.brave = exporter.create();
        main.activate();
    }
    
    @Reference
    Brave brave;
    
    @Activate
    public void activate() {
        LocalTracer tracer = brave.localTracer();
        tracer.startNewSpan("TraceMain", "activate");
        tracer.finishSpan(10);
    }
}
