package omsu.mim.imit.school.registry.data.entity.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class Day {
    @JacksonXmlProperty(localName = "d")
    String d;
    @JacksonXmlProperty(localName = "t")
    String t;
    @JacksonXmlProperty(localName = "h")
    String h;
    @JacksonXmlProperty(localName = "f")
    String f;
}
