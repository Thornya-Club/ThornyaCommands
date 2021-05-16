package thornyaplugin.thornyaplugin.prefeitura.commands;

import me.mattstudios.mfgui.gui.components.util.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import thornyaplugin.thornyaplugin.ThornyaCommands;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Prefeitura implements CommandExecutor {

    private final ThornyaCommands pl;

    public Prefeitura(ThornyaCommands main){
        this.pl = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender snd, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(!(snd instanceof Player)){
            Bukkit.getConsoleSender().sendMessage("§4Comandos somente para jogadores!");
        }else {
            Player p = (Player) snd;
            if (cmd.getName().equalsIgnoreCase("prefeitura")) {
                if (args.length == 0) {
                    p.sendMessage("§cUse /prefeitura ajuda");
                }else {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("votar")) {
                            if (Objects.requireNonNull(pl.config.getFile("prefeitura").getString("votacao.estado")).equalsIgnoreCase("Ativada")) {
                                if (!pl.sqlLeis.hasVoted(p.getName())) {
                                    Gui guivote = new Gui(6, Objects.requireNonNull(pl.config.getFile("prefeitura").getString("GUI.nome")).replace("&", "§"));
                                    ArrayList ITEM_INFO = new ArrayList();
                                    pl.config.getFile("prefeitura").getStringList("GUI.ITEM_INFO.lore").forEach(s1 -> ITEM_INFO.add(s1.replace("&", "§")));
                                    GuiItem info = ItemBuilder.from(Material.PAPER).setName(Objects.requireNonNull(pl.config.getFile("prefeitura").getString("GUI.ITEM_INFO.nome")).replace("&", "§")).setLore(ITEM_INFO).asGuiItem(event -> guivote.close(p));
                                    guivote.setDefaultClickAction(event -> {
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
                                    ArrayList<String> ITEM_CANDIDATO = new ArrayList();
                                    AtomicInteger listSize = new AtomicInteger(1);
                                    pl.global.getCandidatos().forEach(candidatoName -> {
                                        pl.config.getFile("prefeitura").getStringList("GUI.ITEM_CANDIDATO.lore").forEach(s1 -> ITEM_CANDIDATO.add(s1.replace("&", "§").replace("$candidato$", candidatoName)));
                                        GuiItem candidato = ItemBuilder.from(Material.GOLDEN_HELMET).setName(Objects.requireNonNull(pl.config.getFile("prefeitura").getString("GUI.ITEM_CANDIDATO.nome")).replace("&", "§").replace("$candidato$", candidatoName)).setLore(ITEM_CANDIDATO).asGuiItem(event -> {
                                            if (event.isLeftClick() || event.isRightClick() || event.isShiftClick()) {
                                                if (pl.sqlLeis.hasVoted(p.getName())) {
                                                    guivote.close(p);
                                                    p.sendMessage("§cVocê já votou!");
                                                } else {
                                                    pl.sqlLeis.createVoted(p.getName(), candidatoName);
                                                    guivote.close(p);
                                                    p.sendMessage("§aSeu voto foi registrado com sucesso!");
                                                }
                                            }
                                        });


                                        ITEM_CANDIDATO.clear();
                                        if (pl.global.getCandidatos().size() == 2) {
                                            if (listSize.get() == 1) {
                                                guivote.setItem(3, 4, candidato);
                                            } else if (listSize.get() == 2) {
                                                guivote.setItem(3, 6, candidato);
                                            }

                                        } else if (pl.global.getCandidatos().size() == 3) {
                                            if (listSize.get() == 1) {
                                                guivote.setItem(3, 3, candidato);
                                            } else if (listSize.get() == 2) {
                                                guivote.setItem(3, 5, candidato);
                                            } else if (listSize.get() == 3) {
                                                guivote.setItem(3, 7, candidato);
                                            }
                                        } else if (pl.global.getCandidatos().size() == 4) {
                                            if (listSize.get() == 1) {
                                                guivote.setItem(3, 2, candidato);
                                            } else if (listSize.get() == 2) {
                                                guivote.setItem(3, 4, candidato);
                                            } else if (listSize.get() == 3) {
                                                guivote.setItem(3, 6, candidato);
                                            } else if (listSize.get() == 4) {
                                                guivote.setItem(3, 8, candidato);
                                            }
                                        } else if (pl.global.getCandidatos().size() == 5) {
                                            if (listSize.get() == 1) {
                                                guivote.setItem(3, 2, candidato);
                                            } else if (listSize.get() == 2) {
                                                guivote.setItem(3, 4, candidato);
                                            } else if (listSize.get() == 3) {
                                                guivote.setItem(3, 6, candidato);
                                            } else if (listSize.get() == 4) {
                                                guivote.setItem(3, 8, candidato);
                                            } else if (listSize.get() == 5) {
                                                guivote.setItem(5, 5, candidato);
                                            }
                                        } else if (pl.global.getCandidatos().size() == 6) {
                                            if (listSize.get() == 1) {
                                                guivote.setItem(3, 2, candidato);
                                            } else if (listSize.get() == 2) {
                                                guivote.setItem(3, 4, candidato);
                                            } else if (listSize.get() == 3) {
                                                guivote.setItem(3, 6, candidato);
                                            } else if (listSize.get() == 4) {
                                                guivote.setItem(3, 8, candidato);
                                            } else if (listSize.get() == 5) {
                                                guivote.setItem(5, 4, candidato);
                                            } else if (listSize.get() == 6) {
                                                guivote.setItem(5, 6, candidato);
                                            }
                                        } else if (pl.global.getCandidatos().size() == 7) {
                                            if (listSize.get() == 1) {
                                                guivote.setItem(3, 2, candidato);
                                            } else if (listSize.get() == 2) {
                                                guivote.setItem(3, 4, candidato);
                                            } else if (listSize.get() == 3) {
                                                guivote.setItem(3, 6, candidato);
                                            } else if (listSize.get() == 4) {
                                                guivote.setItem(3, 8, candidato);
                                            } else if (listSize.get() == 5) {
                                                guivote.setItem(5, 3, candidato);
                                            } else if (listSize.get() == 6) {
                                                guivote.setItem(5, 5, candidato);
                                            } else if (listSize.get() == 7) {
                                                guivote.setItem(5, 7, candidato);
                                            }
                                        } else if (pl.global.getCandidatos().size() == 8) {
                                            if (listSize.get() == 1) {
                                                guivote.setItem(3, 2, candidato);
                                            } else if (listSize.get() == 2) {
                                                guivote.setItem(3, 4, candidato);
                                            } else if (listSize.get() == 3) {
                                                guivote.setItem(3, 6, candidato);
                                            } else if (listSize.get() == 4) {
                                                guivote.setItem(3, 8, candidato);
                                            } else if (listSize.get() == 5) {
                                                guivote.setItem(5, 2, candidato);
                                            } else if (listSize.get() == 6) {
                                                guivote.setItem(5, 4, candidato);
                                            } else if (listSize.get() == 7) {
                                                guivote.setItem(5, 6, candidato);
                                            } else if (listSize.get() == 8) {
                                                guivote.setItem(5, 8, candidato);
                                            }
                                        } else {
                                            Bukkit.getConsoleSender().sendMessage(" ");
                                            Bukkit.getConsoleSender().sendMessage(" ");
                                            Bukkit.getConsoleSender().sendMessage(" ");
                                            Bukkit.getConsoleSender().sendMessage("§cVotação com quantidades de candidatos errada!");
                                            Bukkit.getConsoleSender().sendMessage(" ");
                                            Bukkit.getConsoleSender().sendMessage(" ");
                                            Bukkit.getConsoleSender().sendMessage(" ");
                                        }
                                        listSize.getAndIncrement();
                                    });

                                    guivote.setItem(1, 5, info);
                                    guivote.open(p);
                                }else{
                                    p.sendMessage("§cVocê já registrou seu voto!");
                                }
                            }else{
                                p.sendMessage("§cVotações encerradas!");
                            }
                            //

                        }
                        else if (args[0].equalsIgnoreCase("votos")) {
                            if (p.hasPermission("prefeitura.votos")) {
                                p.sendMessage("§6========[§2Votos dos Candidatos§6]========");
                                p.sendMessage(" ");
                                pl.global.getCandidatos().forEach(s1 -> p.sendMessage("§b" + s1 + ": §a" + pl.sqlLeis.getVotes(s1)));
                                //sql
                            } else {
                                p.sendMessage("§cVocê não tem permissão para isso!");
                            }
                        }else if (args[0].equalsIgnoreCase("ajuda")){
                            if(p.isOp()){
                                p.sendMessage("§4/prefeitura ativar");
                                p.sendMessage("§4/prefeitura desativar");
                                p.sendMessage("§4/prefeitura reiniciar");
                            }
                            p.sendMessage("§a/prefeitura votar");
                            p.sendMessage("§a/prefeitura votos");
                            p.sendMessage("§a/prefeitura candidatos");

                        }else if (args[0].equalsIgnoreCase("ativar")){
                            if(p.isOp()){
                                if(!Objects.requireNonNull(pl.config.getFile("prefeitura").getString("votacao.estado")).equalsIgnoreCase("Ativada")){
                                    pl.config.getFile("prefeitura").set("votacao.estado", "Ativada");
                                    pl.salvarConfig();
                                    pl.config.reloadConfig("prefeitura");
                                    p.sendMessage("§aVotações iniciada!");
                                }else{
                                    p.sendMessage("§cVocê não pode ativar uma eleição existente!");
                                }
                            }
                        }else if (args[0].equalsIgnoreCase("desativar")){
                            if(p.isOp()){
                                if(!Objects.requireNonNull(pl.config.getFile("prefeitura").getString("votacao.estado")).equalsIgnoreCase("Desativada")){
                                    pl.config.getFile("prefeitura").set("votacao.estado", "Desativada");
                                    pl.salvarConfig();
                                    pl.config.reloadConfig("prefeitura");
                                    p.sendMessage("§aVotações desativada!");
                                }else{
                                    p.sendMessage("§cVocê não pode desativar uma eleição desativada!");
                                }
                            }
                        }else if (args[0].equalsIgnoreCase("reiniciar")){
                            if(p.isOp()){
                                if(Objects.requireNonNull(pl.config.getFile("prefeitura").getString("votacao.estado")).equalsIgnoreCase("Desativada")){
                                    pl.sqlLeis.restartVotes();
                                    pl.config.reloadConfig("prefeitura");
                                    //pl.candidatosVar.updateCandidatos();
                                    p.sendMessage("§bEleições reiniciada com sucesso!");
                                }else{
                                    p.sendMessage("§cVocê não pode reiniciar a eleição ativada!");
                                }
                            }
                        }else if (args[0].equalsIgnoreCase("candidatos")){
                            p.sendMessage("§6============[§2Candidatos§6]============");
                            p.sendMessage(" ");
                            pl.global.getCandidatos().forEach(s1 -> p.sendMessage("§e" + s1));
                        }else if (args[0].equalsIgnoreCase("prefeito")){
                            p.sendMessage(" ");
                            p.sendMessage(" ");
                            p.sendMessage("§6Atual prefeito - §c" + pl.mysqltaxas.getPrefeito());
                            p.sendMessage(" ");
                            p.sendMessage(" ");
                        }else{
                            p.sendMessage("§cUse /prefeitura ajuda");
                        }
                    }else {
                        p.sendMessage("§cUse /prefeitura ajuda");
                    }
                }
            }

        }








        return false;
    }
}
