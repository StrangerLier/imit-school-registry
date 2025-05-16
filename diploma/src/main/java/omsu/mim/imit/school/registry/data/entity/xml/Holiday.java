package omsu.mim.imit.school.registry.data.entity.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Holiday {
    @JacksonXmlProperty(localName = "id")
    String id;
    @JacksonXmlProperty(localName = "title")
    String title;
}
