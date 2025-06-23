package omsu.mim.imit.school.registry.util.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.xml.Calendar;
import omsu.mim.imit.school.registry.data.entity.xml.Day;
import omsu.mim.imit.school.registry.data.entity.xml.HolidayEntity;
import omsu.mim.imit.school.registry.data.repository.HolidayRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HolidaysUpdater {

    private final HolidayRepository holidayRepository;

    @Scheduled(cron = "0 0 5 20 12,6 *") // Execute at 05:00 AM, on day 20 of the month, only in December and June
    public void update() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("https://xmlcalendar.ru/data/ru/2025/calendar.xml", String.class);

        XmlMapper mapper = new XmlMapper();
        Calendar calendar = mapper.readValue(response, Calendar.class);
        List<Day> days = calendar.getDays();

        var year = calendar.getYear();

        saveToDb(year, days);
    }

    private void saveToDb(String year, List<Day> days) {

        var movedDays = days.stream()
                .map(Day::getF)
                .filter(Objects::nonNull)
                .toList();

        var finalHolidays = days
                .stream()
                .filter(day -> !movedDays.contains(day.getD()))
                .map(day -> day.getD() + "." + year)
                .map(day -> new HolidayEntity(UUID.randomUUID(), LocalDate.parse(day, DateTimeFormatter.ofPattern("MM.dd.yyyy"))))
                .toList();

        holidayRepository.saveAll(finalHolidays);
    }

}
