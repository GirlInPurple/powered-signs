package xyz.poweredsigns.utils;

public enum SignFormattingEnum {
    BOLD("§l"),
    OBFUSCATED("§k"),
    STRIKETHROUGH("§m"),
    UNDERLINE("§n"),
    ITALIC("§o"),
    RESET("§r");

    private final String value;

    SignFormattingEnum(String value) { this.value = value; }

    public String getValue() { return value; }

    public static SignFormattingEnum fromString(String value) {
        for (SignFormattingEnum e : SignFormattingEnum.values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return RESET;
    }
}
