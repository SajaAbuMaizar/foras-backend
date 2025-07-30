package portal.forasbackend.dto.response.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.domain.model.City;
import portal.forasbackend.domain.model.Industry;
import portal.forasbackend.domain.model.JobType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionsResponse {
    private List<City> cities;
    private List<Industry> industries;
    private List<JobType> jobTypes;
}