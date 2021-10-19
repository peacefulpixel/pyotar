package org.example.corp.engine.graphics.font.bitmap.map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import java.util.ArrayList;
import java.util.List;

public class Pages {
    @XmlElement(name = "page")
    public List<Page> pages = new ArrayList<>();
}
