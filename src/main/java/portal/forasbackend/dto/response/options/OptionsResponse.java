package portal.forasbackend.dto.response.options;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import portal.forasbackend.entity.City;
import portal.forasbackend.entity.Industry;
import portal.forasbackend.entity.JobType;

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