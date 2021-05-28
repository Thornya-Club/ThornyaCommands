package thornyaplugin.thornyaplugin.eventos;

import me.mattstudios.mfgui.gui.components.util.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import me.mattstudios.mfgui.gui.guis.ScrollingGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import thornyaplugin.thornyaplugin.ThornyaCommands;

import java.util.ArrayList;

public class Eventos implements CommandExecutor {

    private final ThornyaCommands pl;

    //Map<String, Long> cooldowns = new HashMap<String, Long>();

    public Eventos(ThornyaCommands pl) {
        this.pl = pl;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender snd, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if(!(snd instanceof Player)){
            Bukkit.getConsoleSender().sendMessage("§4Comandos somente para jogadores!");
        }else {
            Player p = (Player) snd;
            if (cmd.getName().equalsIgnoreCase("eventos")) {
                if (args.length == 0) {
                    if (p.hasPermission("eventos.usar")) {
                        ArrayList<String> lore = new ArrayList<String>();

                        ScrollingGui gui = new ScrollingGui(6, "§4Calendário de Eventos");
                        gui.setDefaultClickAction(event -> {
                            event.setCancelled(true);
                            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
                                return;
                            if (event.isShiftClick() && event.isLeftClick() || event.isLeftClick() || event.isRightClick()) {
                                event.setCancelled(true);
                            }
                            if (event.getCursor() != null) {
                                event.setCancelled(true);
                            }
                        });

                        setNavbar(gui);
                        gui.open(p);

                    }
                }
            }
        }
        return false;
    }

    private void setNavbar(ScrollingGui gui){
        GuiItem empty = ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem();

        gui.setItem(6,1, empty);
        gui.setItem(6,2, empty);
        gui.setItem(6,3, ItemBuilder.from(Material.ARROW).setName("§cVoltar").asGuiItem(event -> gui.previous()));
        gui.setItem(6,4, empty);
        gui.setItem(6, 5, ItemBuilder.from(Material.FEATHER).setName("§6Calendário de Eventos").asGuiItem());
        gui.setItem(6,6, empty);
        gui.setItem(6,7, ItemBuilder.from(Material.ARROW).setName("§cAvançar").asGuiItem(event -> gui.previous()));
        gui.setItem(6,8, empty);
        gui.setItem(6,9, empty);
    }
}
