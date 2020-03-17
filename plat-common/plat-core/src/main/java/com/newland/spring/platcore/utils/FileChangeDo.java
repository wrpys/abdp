package com.newland.spring.platcore.utils;

import java.net.URI;

@FunctionalInterface
public interface FileChangeDo {
    void changeDo(URI filePath);
}
