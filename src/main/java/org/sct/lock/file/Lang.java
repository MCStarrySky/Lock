package org.sct.lock.file;

import org.sct.easylib.util.function.file.AbstractFile;
import org.sct.lock.Lock;
import org.sct.lock.enumeration.ConfigType;

import java.io.File;

public class Lang extends AbstractFile {
    static {
        load(new File(Config.getString(ConfigType.SETTING_LANGUAGE.getPath()) + ".yml"), Lock.getInstance());
    }
}
