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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HolidaysUpdater {

    private final HolidayRepository holidayRepository;

    @Scheduled(cron = "0 0 0 1 * *") // Execute every 1 second
    public void update() throws JsonProcessingException {
        //RestTemplate restTemplate = new RestTemplate();
        //String response = restTemplate.getForObject("https://xmlcalendar.ru/data/ru/2025/calendar.xml", String.class);

        var str = """
                <?xml version="1.0" encoding="UTF-8"?>
                <calendar year="2025" lang="ru" date="2024.12.01">
                    <holidays>
                        <holiday id="1" title="ÐÐ¾Ð²Ð¾Ð³Ð¾Ð´Ð½Ð¸Ðµ ÐºÐ°Ð½Ð¸ÐºÑÐ»Ñ"/>
                        <holiday id="2" title="Ð Ð¾Ð¶Ð´ÐµÑÑÐ²Ð¾ Ð¥ÑÐ¸ÑÑÐ¾Ð²Ð¾"/>
                        <holiday id="3" title="ÐÐµÐ½Ñ Ð·Ð°ÑÐ¸ÑÐ½Ð¸ÐºÐ° ÐÑÐµÑÐµÑÑÐ²Ð°"/>
                        <holiday id="4" title="ÐÐµÐ¶Ð´ÑÐ½Ð°ÑÐ¾Ð´Ð½ÑÐ¹ Ð¶ÐµÐ½ÑÐºÐ¸Ð¹ Ð´ÐµÐ½Ñ"/>
                        <holiday id="5" title="ÐÑÐ°Ð·Ð´Ð½Ð¸Ðº ÐÐµÑÐ½Ñ Ð¸ Ð¢ÑÑÐ´Ð°"/>
                        <holiday id="6" title="ÐÐµÐ½Ñ ÐÐ¾Ð±ÐµÐ´Ñ"/>
                        <holiday id="7" title="ÐÐµÐ½Ñ Ð Ð¾ÑÑÐ¸Ð¸"/>
                        <holiday id="8" title="ÐÐµÐ½Ñ Ð½Ð°ÑÐ¾Ð´Ð½Ð¾Ð³Ð¾ ÐµÐ´Ð¸Ð½ÑÑÐ²Ð°"/>
                    </holidays>
                    <days>
                        <day d="01.01" t="1" h="1"/>
                        <day d="01.02" t="1" h="1"/>
                        <day d="01.03" t="1" h="1"/>
                        <day d="01.04" t="1" h="1"/>
                        <day d="01.05" t="1" h="1"/>
                        <day d="01.06" t="1" h="1"/>
                        <day d="01.07" t="1" h="2"/>
                        <day d="01.08" t="1" h="1"/>
                        <day d="02.23" t="1" h="3"/>
                        <day d="03.07" t="2"/>
                        <day d="03.08" t="1" h="4"/>
                        <day d="04.30" t="2"/>
                        <day d="05.01" t="1" h="5"/>
                        <day d="05.02" t="1" f="01.04"/>
                        <day d="05.08" t="1" f="02.23"/>
                        <day d="05.09" t="1" h="6"/>
                        <day d="06.11" t="2"/>
                        <day d="06.12" t="1" h="7"/>
                        <day d="06.13" t="1" f="03.08"/>
                        <day d="11.01" t="2"/>
                        <day d="11.03" t="1" f="11.01"/>
                        <day d="11.04" t="1" h="8"/>
                        <day d="12.31" t="1" f="01.05"/>
                    </days>
                </calendar>
                """;

        XmlMapper mapper = new XmlMapper();
        Calendar calendar = mapper.readValue(str, Calendar.class);
        List<Day> days = calendar.getDays();

        var year = calendar.getYear();

        saveToDb(year, days);

        System.out.println("HERE");

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
