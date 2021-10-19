package org.example.corp.engine.graphics.font.bitmap.map;

import javax.xml.bind.annotation.XmlAttribute;

public class Char {
    @XmlAttribute public float id;
    @XmlAttribute public float x;
    @XmlAttribute public float y;
    @XmlAttribute public float width;
    @XmlAttribute public float height;
    @XmlAttribute public float xoffset;
    @XmlAttribute public float yoffset;
    @XmlAttribute public float xadvance;
    @XmlAttribute public String page;
    @XmlAttribute public float chnl;
}
