package portal.forasbackend.application.service.setup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataSetupService {

    //    private final CitySeeder citySeeder;
//    private final IndustrySeeder industrySeeder;
    private final JobTypeSeeder workTypeSeeder;
//    private final AdminSetupService adminSetupService;

    @EventListener(ApplicationReadyEvent.class)
    public void setupData() {
        log.info("Starting data setup...");

        try {
//            citySeeder.seedCities();
//            industrySeeder.seedIndustries();
            workTypeSeeder.seedJobTypes();
//            adminSetupService.createDefaultAdmin();

            log.info("Data setup completed successfully");
        } catch (Exception e) {
            log.error("Error during data setup", e);
        }
    }
}