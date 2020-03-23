package org.sct.lock.enumeration;

import lombok.Getter;

public enum LangType {

    /**
     * 语言文件的路径
     */
    LANGUAGE_COMMANDHELP("Language.CommandHelp"),
    LANG_NOPERMISSION("Language.NoPermission"),
    LANG_NOTAPLAYER("Language.NotAPlayer"),
    LANG_RELOAD("Language.Reload"),
    LANG_CREATE("Language.Create"),
    LANG_COMMANDERROR("Language.CommandError"),
    LANG_ENTER("Language.Enter"),
    LANG_LEAVE("Language.Leave"),
    LANG_NOTENOUGHMONEY("Language.NotEnoughMoney"),
    LANG_DENYDIRECTION("Language.DenyDirection"),
    LANG_DENYBREAK("Language.DenyBreak"),
    LANG_DENYNOTEMPTYINV("Language.DenyNotEmptyInv"),
    LANG_DENYMONEY("Language.DenyMoney"),
    LANG_DENYHAVEEFFECT("Language.DenyHaveEffect"),
    LANG_ADDTYPE("Language.addType"),
    LANG_INVALIDTYPE("Language.InvalidType"),
    LANG_ADDTYPESUCCESS("Language.AddTypeSuccess"),
    LANG_DoorDetail("Language.DoorDetail"),
    LANG_BANREDSTONE("Language.BanRedstone");

    @Getter String path;

    LangType(String path) {
        this.path = path;
    }

}
