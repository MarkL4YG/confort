package de.mlessmann.confort.api;

import java.util.List;

public interface IConfigNodeList {

    void append(IConfigNode child);

    void prepend(IConfigNode child);

    IConfigNode remove(Integer index);

    List<IConfigNode> asList();
}
