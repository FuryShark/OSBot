package data;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;

public abstract class Constants {

	private static final Area GUILD_BANK = new Area(1589, 3480, 1593, 3475);
	public static final Area[] BANK_AREAS = new Area[] { Banks.AL_KHARID, Banks.ARCEUUS_HOUSE, Banks.ARDOUGNE_NORTH,
			Banks.ARDOUGNE_SOUTH, Banks.CAMELOT, Banks.CANIFIS, Banks.CASTLE_WARS, Banks.CATHERBY, Banks.DRAYNOR,
			Banks.DUEL_ARENA, Banks.EDGEVILLE, Banks.FALADOR_EAST, Banks.FALADOR_WEST, Banks.GNOME_STRONGHOLD,
			Banks.GRAND_EXCHANGE, Banks.HOSIDIUS_HOUSE, Banks.LOVAKENGJ_HOUSE, Banks.LOVAKITE_MINE,
			Banks.LUMBRIDGE_UPPER, Banks.PEST_CONTROL, Banks.PISCARILIUS_HOUSE, Banks.SHAYZIEN_HOUSE, Banks.TZHAAR,
			Banks.VARROCK_EAST, Banks.VARROCK_WEST, Banks.YANILLE, GUILD_BANK };

	public static final String[] LOGS = { "Logs", "Achey tree logs", "Oak logs", "Willow logs", "Teak logs",
			"Maple logs", "Mahogany logs", "Arctic pine logs", "Yew logs", "Magic logs", "Redwood logs", "Bark",
			"Juniper logs" },
			AXES = { "Bronze axe", "Iron axe", "Steel axe", "Black axe", "Mithril axe", "Adamant axe", "Rune axe",
					"Dragon axe" };
	
	public static final int MIN_RUN = 20, MAX_RUN = 50;

}
