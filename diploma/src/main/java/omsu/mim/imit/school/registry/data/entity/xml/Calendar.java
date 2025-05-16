package omsu.mim.imit.school.registry.data.entity.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import java.util.List;

@Getter
@JacksonXmlRootElement(localName = "calendar")
public class Calendar {
    @JacksonXmlProperty(localName = "year")
    String year;
    @JacksonXmlProperty(localName = "lang")
    String lang;
    @JacksonXmlProperty(localName = "date")
    String date;

    @JacksonXmlProperty(localName = "holidays")
    List<Holiday> holidays;

    @JacksonXmlProperty(localName = "days")
    List<Day> days;
}
