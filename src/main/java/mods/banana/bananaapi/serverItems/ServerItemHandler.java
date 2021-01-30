package mods.banana.bananaapi.serverItems;

import java.util.ArrayList;
import java.util.List;

public class ServerItemHandler {
    private static final List<ServerItem> items = new ArrayList<>();

    public static void register(ServerItem item) { items.add(item); }
    public static List<ServerItem> getItems() { return items; }
}
