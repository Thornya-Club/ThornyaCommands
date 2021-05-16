package thornyaplugin.thornyaplugin.prefeitura.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thornyaplugin.thornyaplugin.ThornyaCommands;

import java.util.ArrayList;
import java.util.List;

public class TabPrefeitura implements TabCompleter {
    private ThornyaCommands pl;
    public TabPrefeitura(ThornyaCommands main){
        this.pl = main;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> options = new ArrayList<String>();

        if (args.length > 0){
            if(args.length == 1){
                options.add("ajuda");
                options.add("votar");
                options.add("candidatos");
                if(sender.isOp()) {
                    options.add("reiniciar");
                    options.add("ativar");
                    options.add("desativar");
                }
                if(sender.hasPermission("prefeitura.votos")){
                    options.add("votos");
                }
            }
        }

        return options;
    }
}
