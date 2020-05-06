package org.sct.lock.enumeration;

public enum ConfigType {

    SETTING_LANGUAGE("Setting.Language"),
    SETTING_ISOTHERALLOWEDOPEN("Setting.IsOtherAllowedOpen"),
    SETTING_ISOTHERALLOWEDBREAK("Setting.IsOtherAllowedBreak"),
    SETTING_WORLDS("Setting.Worlds"),
    SETTING_TAXALLOWED("Setting.TaxAllowed"),
    SETTING_TAXPERCENT("Setting.TaxPercent"),
    SETTING_VIPALLOWED("Setting.VipAllowed"),
    SETTING_TAXCANCELABLE("Setting.TaxCancelable"),
    SETTING_LOCKSYMBOL("Setting.LockSymbol"),
    SETTING_SYMBOLREPLACE("Setting.SymbolReplace"),
    SETTING_FLAGENTER("Setting.FlagEnter"),
    SETTING_FLAGLEAVE("Setting.FlagLeave"),
    SETTING_ENTERREPLACE("Setting.EnterReplace"),
    SETTING_LEAVEREPLACE("Setting.LeaveReplace"),
    SETTING_DOORTYPE("Setting.DoorType"),
    SETTING_CHARGE("Setting.Charge"),
    SETTING_ENTERDELAY("Setting.EnterDelay"),
    SETTING_BANREDSTONEACTIVE("Setting.BanRedstoneActive"),
    SETTING_BANREDSTONEACTIVERADIUS("Setting.BanRedstoneActiveRadius"),
    SETTING_FLAGEMPTY("Setting.FlagEmpty"),
    SETTING_FLAGMONEY("Setting.FlagMoney"),
    SETTING_FLAGEFFECT("Setting.FlagEffect"),
    SETTING_EMPTYREPLACE("Setting.EmptyReplace"),
    SETTING_MONEYREPLACE("Setting.MoneyReplace"),
    SETTING_EFFECTREPLACE("Setting.EffectReplace");


    String path;

    public String getPath() {
        return path;
    }

    ConfigType(String path) {
        this.path = path;
    }

}
