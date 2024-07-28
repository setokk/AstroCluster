package edu.setokk.astrocluster.cluster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClusterRequestBody {
    private String gitUrl;
    private String lang;
    private List<String> extensions;
}
