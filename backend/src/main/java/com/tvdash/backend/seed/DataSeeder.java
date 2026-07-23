package com.tvdash.backend.seed;

import com.tvdash.backend.model.TableauCard;
import com.tvdash.backend.model.UrlOnlyItem;
import com.tvdash.backend.repository.TableauCardRepository;
import com.tvdash.backend.repository.UrlOnlyItemRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {
    private final TableauCardRepository tableauCardRepository;
    private final UrlOnlyItemRepository urlOnlyItemRepository;

    public DataSeeder(TableauCardRepository tableauCardRepository, UrlOnlyItemRepository urlOnlyItemRepository) {
        this.tableauCardRepository = tableauCardRepository;
        this.urlOnlyItemRepository = urlOnlyItemRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (tableauCardRepository.count() == 0) {
            TableauCard radio24 = new TableauCard();
            radio24.setName("Radio 24");
            radio24.setimageName("img/radio-24.avif");
            radio24.setUrl("https://www.radio24.ilsole24ore.com/");

            TableauCard rmf = new TableauCard();
            rmf.setName("RMF FM");
            rmf.setimageName("img/RMF.svg");
            rmf.setUrl("https://radio111.pl/stations/rmf-fm-radio-rmf-fm/");

            tableauCardRepository.saveAll(List.of(radio24, rmf));
        }

        if (urlOnlyItemRepository.count() == 0) {
            UrlOnlyItem weatherWidget = new UrlOnlyItem();
            weatherWidget.setUrl("https://api.wo-cloud.com/content/widget/?geoObjectKey=10828681&language=it&region=IT&timeFormat=HH:mm&windUnit=kmh&systemOfMeasurement=metric&temperatureUnit=celsius");
            urlOnlyItemRepository.save(weatherWidget);
        }
    }
}
