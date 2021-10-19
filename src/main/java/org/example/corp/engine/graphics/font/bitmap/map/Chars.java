package org.example.corp.engine.graphics.font.bitmap.map;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class Chars {
    @XmlElement(name = "char")
    public List<Char> chars = new ArrayList<>();
}
