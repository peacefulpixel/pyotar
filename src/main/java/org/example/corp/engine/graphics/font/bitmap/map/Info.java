package org.example.corp.engine.graphics.font.bitmap.map;

import javax.xml.bind.annotation.XmlAttribute;

public class Info {
    @XmlAttribute public String face;
    @XmlAttribute public float size;
    @XmlAttribute public float bold;
    @XmlAttribute public float italic;
    @XmlAttribute public String charset;
    @XmlAttribute public float unicode;
    @XmlAttribute public float stretchH;
    @XmlAttribute public float smooth;
    @XmlAttribute public float aa;
    @XmlAttribute public String padding;
    @XmlAttribute public String spacing;
}
