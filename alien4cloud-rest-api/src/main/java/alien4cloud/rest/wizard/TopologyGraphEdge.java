package alien4cloud.rest.wizard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopologyGraphEdge {
    private String source;
    private String target;
    private String id;
    private String label;
}