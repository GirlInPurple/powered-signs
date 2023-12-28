package xyz.poweredsigns.utils;

public enum SignColorEnum {
    BLACK("§f"),
    RED("§c"),
    GREEN("§2"),
    BROWN("§8"),
    BLUE("§1"),
    PURPLE("§5"),
    CYAN("§3"),
    LIGHT_GRAY("§7"),
    GRAY("§8"),
    PINK("§d"),
    LIME("§a"),
    YELLOW("§e"),
    LIGHT_BLUE("§9"),
    MAGENTA("§5"),
    ORANGE("§6"),
    WHITE("§0"),
    RESET("§r");

    private final String value;

    SignColorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SignColorEnum ColorFromString(String color) {
        for (SignColorEnum enumValue : SignColorEnum.values()) {
            if (enumValue.name().equalsIgnoreCase(color)) {
                return enumValue;
            }
        }
        return BLACK;
    }
}
