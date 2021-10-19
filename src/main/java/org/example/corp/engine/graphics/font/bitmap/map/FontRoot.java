package org.example.corp.engine.graphics.font.bitmap.map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "font")
public class FontRoot {
    @XmlElement public Info info;
    @XmlElement public Common common;
    @XmlElement public Pages pages;
    @XmlElement public Chars chars;
}
