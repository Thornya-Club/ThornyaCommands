package thornyaplugin.thornyaplugin.prefeitura.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import thornyaplugin.thornyaplugin.ThornyaCommands;

public class Leis implements CommandExecutor {

    private ThornyaCommands pl;

    public Leis(ThornyaCommands main){
        this.pl = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender snd, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
            /*
        if(!(snd instanceof Player)){
            Bukkit.getConsoleSender().sendMessage("§4Comandos somente para jogadores!");
        }else {
            Player p = (Player) snd;
            if (cmd.getName().equalsIgnoreCase("leis")) {
                if (args.length == 0) {
                    if (p.hasPermission("prefeitura.leis")) {
                        ArrayList<String> lore = new ArrayList();

                        PaginatedGui gui = new PaginatedGui(6, "§4§lPrefeitura - Leis");
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
                        GuiItem empty = ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem();
                        gui.setItem(6, 1, empty);
                        gui.setItem(6, 2, empty);
                        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName("§cPágina anterior").asGuiItem(event -> {
                            if (event.isLeftClick()) {
                                gui.previous();
                            }
                        }));
                        gui.setItem(6, 4, empty);
                        gui.setItem(6, 5, empty);
                        gui.setItem(6, 6, empty);
                        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName("§cPróxima Página").asGuiItem(event -> {
                            if (event.isLeftClick()) {
                                gui.next();
                            }
                        }));
                        gui.setItem(6, 8, empty);
                        gui.setItem(6, 9, empty);
                        int i = 0;
                        while (i < pl.leisVar.id.size()) {
                            lore.add(" ");
                            lore.add("§c" + pl.leisVar.regra.get(i));
                            lore.add(" ");
                            lore.add("§b" + pl.leisVar.descricao.get(i));
                            if (pl.leisVar.punicao.get(i).equalsIgnoreCase("0") && pl.leisVar.multa.get(i) == 0) {
                                lore.add(" ");
                                lore.add("§2Criado por: §f" + pl.leisVar.criador.get(i));
                            } else if (!(pl.leisVar.punicao.get(i).equalsIgnoreCase("0")) && pl.leisVar.multa.get(i) != 0) {
                                lore.add(" ");
                                lore.add("§eMulta: §aR$" + pl.leisVar.multa.get(i));
                                lore.add(" ");
                                lore.add("§ePunição: §c" + pl.leisVar.punicao.get(i).replace("@player", p.getName()));
                                lore.add(" ");
                                lore.add("§2Criado por: §f" + pl.leisVar.criador.get(i));
                            } else if (pl.leisVar.punicao.get(i).equalsIgnoreCase("0") && pl.leisVar.multa.get(i) != 0) {
                                lore.add(" ");
                                lore.add("§eMulta: §aR$" + pl.leisVar.multa.get(i));
                                lore.add(" ");
                                lore.add("§2Criado por: §f" + pl.leisVar.criador.get(i));
                            } else {
                                lore.add(" ");
                                lore.add("§ePunição: §c" + pl.leisVar.punicao.get(i).replace("@player", p.getName()));
                                lore.add(" ");
                                lore.add("§2Criado por: §f" + pl.leisVar.criador.get(i));
                            }
                            int finalI = i;
                            GuiItem leis = ItemBuilder.from(Material.WRITABLE_BOOK).setName("§a§lLei#" + pl.leisVar.id.get(i)).setLore(lore).asGuiItem(event -> {
                                if (event.isLeftClick()) {
                                    event.getWhoClicked().sendMessage("§e" + pl.leisVar.regra.get(finalI) + " - §c" + pl.leisVar.descricao.get(finalI));
                                }
                            });
                            gui.addItem(leis);
                            lore.clear();
                            i++;
                        }
                        gui.open(p);
                    }
                } else {
                    if (p.isOp() || p.hasPermission("prefeitura.*") ||
                            p.hasPermission("prefeitura.add") ||
                            p.hasPermission("prefeitura.remove") ||
                            p.hasPermission("prefeitura.edit") ||
                            p.hasPermission("prefeitura.punish") ||
                            p.hasPermission("prefeitura.penality")) {
                        if (args[0].equalsIgnoreCase("ajuda")) {
                            p.sendMessage("Ajuda aqui");
                        }
                        if (args.length > 1) {
                            if (args[0].equalsIgnoreCase("adicionar")) {
                                if (args.length == 3) {
                                    String[] args1 = args[1].split("_");
                                    int i = 0;
                                    StringBuilder regra = new StringBuilder();
                                    while (i < args1.length) {
                                        if (i == 0) {
                                            regra.append(args1[i]);
                                        } else {
                                            regra.append(" " + args1[i]);
                                        }
                                        i++;
                                    }
                                    String[] args2 = args[2].split("_");
                                    StringBuilder descricao = new StringBuilder();
                                    i = 0;
                                    while (i < args2.length) {
                                        if (i == 0) {
                                            descricao.append(args2[i]);
                                        } else {
                                            descricao.append(" " + args2[i]);
                                        }
                                        i++;
                                    }
                                    if (!pl.sqlLeis.hasRule(regra.toString())) {
                                        pl.sqlLeis.createLei(regra.toString(), descricao.toString(), p.getName());
                                        pl.leisVar.updateLeis();
                                        pl.sqlLeis.updateLeis();
                                        p.sendMessage("§e" + regra.toString() + " §acriada com sucesso!");
                                    } else {
                                        p.sendMessage("§cUma regra com esse nome já existe!");
                                    }

                                    //notificarplayers
                                } else {
                                    p.sendMessage("§cUse /leis ajuda");
                                }
                            } else if (args[0].equalsIgnoreCase("remover")) {

                                //arrumar pra que se o player for o dono ele poder remover

                                if (pl.ess.getUser(p).getGroup().equalsIgnoreCase("Fundador") ||
                                        pl.ess.getUser(p).getGroup().equalsIgnoreCase("Admin")) {
                                    if (args.length == 2) {
                                        String[] id = args[1].split("#");
                                        if (id.length == 2) {
                                            if (pl.sqlLeis.hasID(id[1])) {
                                                pl.sqlLeis.deleteLei(id[1]);
                                                p.sendMessage("§eLei#" + id[1] + " §bdeletada com sucesso!");
                                                pl.leisVar.updateLeis();
                                                pl.sqlLeis.updateLeis();
                                            } else {
                                                p.sendMessage("§cEssa lei não existe!");
                                            }
                                        } else {
                                            p.sendMessage("§cUse Lei#<número> para apagar a lei");
                                        }
                                    } else {
                                        p.sendMessage("§cUse /leis ajuda");
                                    }
                                } else {
                                    p.sendMessage("§cSomente Fundador e Admin pode remover uma lei!");
                                }
                            } else if (args[0].equalsIgnoreCase("punir")) {
                                if (pl.ess.getUser(p).getGroup().equalsIgnoreCase("Fundador") ||
                                        pl.ess.getUser(p).getGroup().equalsIgnoreCase("Admin")) {
                                    if (args.length >= 3) {
                                        String[] id = args[1].split("#");
                                        if (id.length == 2) {
                                            if (pl.sqlLeis.hasID(id[1])) {
                                                if (args[2].equalsIgnoreCase("adicionar") && args.length > 3) {
                                                    //falta adicionar o punir
                                                    p.sendMessage("Você adicionou!");
                                                } else if (args[2].equalsIgnoreCase("remover")) {
                                                    //remover o punir
                                                    p.sendMessage("Você removeu!");
                                                } else {
                                                    p.sendMessage("§cUse [Adicionar/Remover]");
                                                }
                                            } else {
                                                p.sendMessage("§cEssa lei não existe!");
                                            }
                                        } else {
                                            p.sendMessage("§cUse Lei#<número> ");
                                        }

                                    } else {
                                        p.sendMessage("§cUse /leis ajuda");
                                    }
                                } else {
                                    p.sendMessage("§cSomente Fundador e Admin pode adicionar uma punição!");
                                }
                            }
                        } else {
                            if (!args[0].equalsIgnoreCase("ajuda")) {
                                p.sendMessage("§cUse /leis ajuda");
                            }
                        }
                    } else {
                        p.sendMessage("§cEsse comando não existe!");
                    }
                }

            }

        }
         */
        return false;
    }
}