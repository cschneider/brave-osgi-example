package bravetest;

import static java.util.Collections.singleton;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;

public class MyApplication extends Application {
    
    @Override
    public Set<Object> getSingletons() {
        Brave brave = new Brave.Builder().build();
        return singleton(BraveTracingFeature.create(brave));
    }
    
}
