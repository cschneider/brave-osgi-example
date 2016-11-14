package bravetest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.jaxrs2.BraveTraceFeature;

public class MyApplication extends Application {

    @Override
    public Set<Object> getSingletons() {
        Brave brave = new Brave.Builder().build();
        BraveTraceFeature traceFeature = new BraveTraceFeature(brave);
        return new HashSet<>(Arrays.asList(traceFeature));
    }
    
}
