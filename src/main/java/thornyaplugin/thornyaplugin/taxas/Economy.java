package thornyaplugin.thornyaplugin.taxas;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.ess3.api.MaxMoneyException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import thornyaplugin.thornyaplugin.ThornyaCommands;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

public class Economy extends com.earth2me.essentials.api.Economy implements CommandExecutor {
    private final ThornyaCommands pl;

    public Economy(ThornyaCommands main){
        this.pl = main;
    }
    public boolean isInteger(double number) {
        return number % 1 == 0;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        DecimalFormat formato = new DecimalFormat("#,##");
        if(command.getName().equalsIgnoreCase("taxa")){
            //if(args[0].equalsIgnoreCase("debug")){
            //    pl.sqlitetaxas.updateValueClan(0.0F);
            //    pl.sqlitetaxas.updateValuePrefeitura(0.0F);
            //}
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("enviardatabase")){
                    if(!(sender instanceof Player)){
                        if(pl.sqlitetaxas.getValueClan() > 1.0 && pl.sqlitetaxas.getValuePrefeitura() > 1.0) {
                            double valorMysqlClan = pl.mysqltaxas.getValorClan();
                            valorMysqlClan = Double.parseDouble(formato.format(valorMysqlClan));

                            double valorMysqlPrefeitura = pl.mysqltaxas.getValorPrefeitura();
                            valorMysqlPrefeitura = Double.parseDouble(formato.format(valorMysqlPrefeitura));

                            double valorSQLiteClan = pl.sqlitetaxas.getValueClan();
                            double valorSQLitePrefeitura = pl.sqlitetaxas.getValuePrefeitura();

                            valorSQLiteClan = Double.parseDouble(formato.format(valorSQLiteClan));
                            valorSQLitePrefeitura = Double.parseDouble(formato.format(valorSQLitePrefeitura));
                            pl.mysqltaxas.updateValorClan(valorSQLiteClan + valorMysqlClan);
                            pl.mysqltaxas.updateValorPrefeitura(valorSQLitePrefeitura + valorMysqlPrefeitura);

                            pl.sqlitetaxas.updateValueClan(0.0F);
                            pl.sqlitetaxas.updateValuePrefeitura(0.0F);
                        }
                    }else{
                        Player p = (Player)sender;
                        p.sendMessage("§4Comando permitido somente para o CONSOLE!");
                    }
                }
                if(args[0].equalsIgnoreCase("pagartodos")){
                    if(!(sender instanceof Player)){
                        if(pl.mysqltaxas.getValorClan() != 0.0){
                            double valorMysqlClan = pl.mysqltaxas.getValorClan();
                            valorMysqlClan = Double.parseDouble(formato.format(valorMysqlClan));
                            pl.sc.getClanManager().getClan(pl.mysqltaxas.getTagClan()).setBalance(pl.sc.getClanManager().getClan(pl.mysqltaxas.getTagClan()).getBalance() + valorMysqlClan);
                            pl.mysqltaxas.updateValorClan(0.0);
                        }
                        if(pl.mysqltaxas.getValorPrefeitura() != 0.0) {
                            double valorMysqlPrefeitura = pl.mysqltaxas.getValorPrefeitura();
                            valorMysqlPrefeitura = Double.parseDouble(formato.format(valorMysqlPrefeitura));
                            Bukkit.dispatchCommand(sender, "eco give $player $valor".replace("$valor", String.valueOf(valorMysqlPrefeitura)).replace("$player", pl.mysqltaxas.getPrefeito()));
                            pl.mysqltaxas.updateValorPrefeitura(0.0);
                        }
                    }else{
                        Player p = (Player)sender;
                        p.sendMessage("§4Comando permitido somente para o CONSOLE!");
                    }
                }
            }
        }
        if(sender instanceof Player){
            Player p = (Player)sender;
            if(command.getName().equalsIgnoreCase("taxa")){
                if(!(args.length == 0)) {
                    if (args.length == 3) {
                        if (args[0].equalsIgnoreCase("mudar")) {
                            if (args[1].equalsIgnoreCase("prefeitura")) {
                                if (p.hasPermission("prefeitura.taxa") || p.isOp()) {
                                    boolean isNumber = true;
                                    try {
                                        Integer.parseInt(args[2]);
                                    } catch (NumberFormatException e) {
                                        isNumber = false;
                                    }
                                    if(isNumber){
                                        isNumber = Integer.parseInt(args[2]) >= 0;
                                    }
                                    if (isNumber) {
                                        double numberConfigGetPrefeitura = pl.config.getFile("configuration").getDouble("tax-prefeitura");
                                        double numberConfigGetSupremacia = pl.config.getFile("configuration").getDouble("tax-supremacia");
                                        int numberConfigGet;
                                        numberConfigGet = (int) (100 - ((numberConfigGetPrefeitura + numberConfigGetSupremacia) * 100));
                                        if (isInteger(Double.parseDouble(args[2]))) {
                                            int number = Integer.parseInt(args[2]);
                                            if (number == 0) {
                                                pl.config.getFile("configuration").set("tax-prefeitura", 0.0);
                                                pl.salvarConfig();
                                                pl.config.reloadConfig("configuration");
                                                pl.config.getFile("translate").getStringList("tax.announce-remove-prefeitura").forEach(s -> pl.getServer().broadcastMessage(s.replace("&", "§")
                                                        .replace("$player", p.getDisplayName())
                                                        .replace("$tax", String.valueOf(number))));
                                            } else if (number <= numberConfigGet && number <= 5) {
                                                double convert = number / 100.0;
                                                pl.config.getFile("configuration").set("tax-prefeitura", convert);
                                                pl.config.getFile("translate").getStringList("tax.announce-prefeitura").forEach(s -> pl.getServer().broadcastMessage(s.replace("&", "§")
                                                        .replace("$player", p.getDisplayName())
                                                        .replace("$tax", String.valueOf(number))));
                                                pl.salvarConfig();
                                                pl.config.reloadConfig("configuration");
                                            } else {
                                                p.sendMessage("§cO taxa deve ser menor ou igual a §4" + numberConfigGet);
                                            }
                                        } else {
                                            p.sendMessage("§cO taxa deve ser menor ou igual a §4" + numberConfigGet);
                                        }
                                    } else {
                                        p.sendMessage(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.invalid-number")))))).replace("&", "§"));
                                    }
                                } else {
                                    p.sendMessage("§cVocê não tem permissão para isso!");
                                }
                            } else if (args[1].equalsIgnoreCase("supremacia")) {
                                if (p.hasPermission("prefeitura.taxa") || p.isOp()) {
                                    boolean isNumber = true;
                                    try {
                                        Integer.parseInt(args[2]);
                                    } catch (NumberFormatException e) {
                                        isNumber = false;
                                    }
                                    if (isNumber) {
                                        double numberConfigGetPrefeitura = pl.config.getFile("configuration").getDouble("tax-prefeitura");
                                        double numberConfigGetSupremacia = pl.config.getFile("configuration").getDouble("tax-supremacia");
                                        int numberConfigGet;
                                        numberConfigGet = (int) (100 - ((numberConfigGetPrefeitura + numberConfigGetSupremacia) * 100));
                                        if (isInteger(Double.parseDouble(args[2]))) {
                                            int number = Integer.parseInt(args[2]);
                                            if (number == 0) {
                                                pl.config.getFile("configuration").set("tax-supremacia", 0.0);
                                                pl.salvarConfig();
                                                pl.config.reloadConfig("configuration");
                                                pl.config.getFile("translate").getStringList("tax.announce-remove-supremacia").forEach(s -> pl.getServer().broadcastMessage(s.replace("&", "§")
                                                        .replace("$player", p.getDisplayName())
                                                        .replace("$tax", String.valueOf(number))));
                                            } else if (number <= numberConfigGet && number <= 5) {
                                                double convert = number / 100.0;
                                                pl.config.getFile("configuration").set("tax-supremacia", convert);
                                                pl.config.getFile("translate").getStringList("tax.announce-supremacia").forEach(s -> pl.getServer().broadcastMessage(s.replace("&", "§")
                                                        .replace("$player", p.getDisplayName())
                                                        .replace("$tax", String.valueOf(number))));
                                                pl.salvarConfig();
                                                pl.config.reloadConfig("configuration");
                                            } else {
                                                p.sendMessage("§cO taxa deve ser menor ou igual a §4" + numberConfigGet);
                                            }
                                        } else {
                                            p.sendMessage("§cO taxa deve ser menor ou igual a §4" + numberConfigGet);
                                        }
                                    } else {
                                        p.sendMessage(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.invalid-number")))).replace("&", "§"));
                                    }
                                } else {
                                    p.sendMessage("§cVocê não tem permissão para isso!");
                                }
                            } else {
                                p.sendMessage("§cUse /taxa ajuda");
                            }
                        }
                    } else if (args.length == 1) {
                        //if(args[0].equalsIgnoreCase("total")){
                        //    p.sendMessage(String.format("§bValor total de taxas acumuladas: R$" +  String.format("%.2f",pl.mysqltaxas.getValorPrefeitura())));
                        //}else
                        if (args[0].equalsIgnoreCase("ajuda")) {
                            if (p.hasPermission("prefeitura.taxa") || p.isOp()) {
                                pl.config.getFile("translate").getStringList("tax.help").forEach(s -> p.sendMessage(s.replace("&", "§")));
                            } else {
                                p.sendMessage("§cVocê não tem permissão para isso!");
                            }
                        }
                    } else {
                        p.sendMessage("§cUse /taxa ajuda");
                    }
                }else {
                    int numberConfigGetPrefeitura = (int) (pl.config.getFile("configuration").getDouble("tax-prefeitura") * 100);
                    int numberConfigGetSupremacia = (int) (pl.config.getFile("configuration").getDouble("tax-supremacia") * 100);
                    int taxTotal = numberConfigGetPrefeitura + numberConfigGetSupremacia;
                    pl.config.getFile("translate").getStringList("tax.show-tax").forEach(s -> p.sendMessage(s.replace("&", "§")
                            .replace("$taxS", String.valueOf(numberConfigGetSupremacia))
                            .replace("$taxP", String.valueOf(numberConfigGetPrefeitura))
                            .replace("$taxtotal", String.valueOf(taxTotal))));
                }
            }

            if (command.getName().equalsIgnoreCase("pagar")) {
                    if (args.length == 2) {
                        if (!args[0].equalsIgnoreCase("*")) {
                            boolean online = true;
                            Player target = Bukkit.getPlayerExact(args[0]);
                            if (target == null) {
                                online = false;
                            }
                            if (online) {
                                if (playerExists(target.getUniqueId())) {
                                    if (!p.equals(target)) {
                                        try {
                                            boolean isNumber = true;
                                            try {
                                                Double.parseDouble(args[1]);
                                            } catch (NumberFormatException e) {
                                                isNumber = false;
                                            }
                                            if (isNumber) {
                                                BigDecimal money = new BigDecimal(args[1]);
                                                if (money.doubleValue() > 0) {
                                                    BigDecimal taxa = BigDecimal.valueOf(pl.tax.taxValue(Double.parseDouble(args[1])));
                                                    BigDecimal moneyWithTax = new BigDecimal(String.valueOf(money)).add(taxa);
                                                    if (hasEnough(p.getUniqueId(), moneyWithTax)) {
                                                        subtract(p.getUniqueId(), moneyWithTax);
                                                        add(target.getUniqueId(), money);
                                                        pl.config.getFile("translate").getStringList("economy.paid-money").forEach(s -> p.sendMessage(s.replace("&", "§")
                                                                .replace("$taxa", format(taxa))
                                                                .replace("$player", target.getDisplayName())
                                                                .replace("$money", format(money))));
                                                        target.sendMessage(Objects.requireNonNull(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.received-money")))
                                                                .replace("&", "§")
                                                                .replace("$player", p.getDisplayName())
                                                                .replace("$money", format(money)));
                                                        pl.sqlitetaxas.updateValueClan((float) Double.parseDouble(formato.format((pl.sqlitetaxas.getValueClan().floatValue() + Double.parseDouble(formato.format(pl.tax.divideMoney(taxa.doubleValue(), "clan")))))));
                                                        pl.sqlitetaxas.updateValuePrefeitura((float) Double.parseDouble(formato.format((pl.sqlitetaxas.getValuePrefeitura().floatValue() + Double.parseDouble(formato.format(pl.tax.divideMoney(taxa.doubleValue(), "pf")))))));
                                                    } else {
                                                        pl.config.getFile("translate").getStringList("economy.dont-have-money").forEach(s -> p.sendMessage(s.replace("&", "§")
                                                                .replace("$total", format(moneyWithTax))
                                                                .replace("$taxa", format(taxa))));
                                                    }
                                                } else {
                                                    p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.invalid-number")).replace("&", "§"));
                                                }
                                            } else {
                                                p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.invalid-number")).replace("&", "§"));
                                            }
                                        } catch (UserDoesNotExistException | NoLoanPermittedException | MaxMoneyException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.pay-yourself")).replace("&", "§"));
                                    }
                                } else {
                                    p.sendMessage(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.player-not-found")))).replace("&", "§"));
                                    return true;
                                }
                            } else {
                                if (p.hasPermission("essentials.pay.offline") || p.isOp()) {
                                    if (playerExists(Bukkit.getOfflinePlayer(args[0]).getUniqueId())) {
                                        try {
                                            boolean isNumber = true;
                                            try {
                                                Double.parseDouble(args[1]);
                                            } catch (NumberFormatException e) {
                                                isNumber = false;
                                            }
                                            if (isNumber) {
                                                BigDecimal money = new BigDecimal(args[1]);
                                                if (money.doubleValue() > 0) {
                                                    BigDecimal taxa = BigDecimal.valueOf(pl.tax.taxValue(Double.parseDouble(args[1])));
                                                    BigDecimal moneyWithTax = new BigDecimal(String.valueOf(money)).add(taxa);
                                                    if (hasEnough(p.getUniqueId(), moneyWithTax)) {
                                                        subtract(p.getUniqueId(), moneyWithTax);
                                                        add(Bukkit.getOfflinePlayer(args[0]).getUniqueId(), money);
                                                        pl.config.getFile("translate").getStringList("economy.paid-money").forEach(s -> p.sendMessage(s.replace("&", "§")
                                                                .replace("$taxa", format(taxa))
                                                                .replace("$player", target.getDisplayName())
                                                                .replace("$money", format(money))));
                                                        pl.sqlitetaxas.updateValueClan((float) Double.parseDouble(formato.format((pl.sqlitetaxas.getValueClan().floatValue() + Double.parseDouble(formato.format(pl.tax.divideMoney(taxa.doubleValue(), "clan")))))));
                                                        pl.sqlitetaxas.updateValuePrefeitura((float) Double.parseDouble(formato.format((pl.sqlitetaxas.getValuePrefeitura().floatValue() + Double.parseDouble(formato.format(pl.tax.divideMoney(taxa.doubleValue(), "pf")))))));
                                                    } else {
                                                        pl.config.getFile("translate").getStringList("economy.dont-have-money").forEach(s -> p.sendMessage(s.replace("&", "§")
                                                                .replace("$total", format(moneyWithTax))
                                                                .replace("$taxa", format(taxa))));
                                                    }
                                                } else {
                                                    p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.invalid-number")).replace("&", "§"));
                                                }
                                            } else {
                                                p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.invalid-number")).replace("&", "§"));
                                            }
                                        } catch (UserDoesNotExistException | NoLoanPermittedException | MaxMoneyException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.player-not-found")).replace("&", "§"));
                                    }
                                }else{
                                    p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.player-not-online")).replace("&", "§"));
                                }
                            }
                        } else {
                            if (p.hasPermission("essentials.pay.multiple") || p.isOp()) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (!player.equals(p)) {
                                        try {
                                            boolean isNumber = true;
                                            try {
                                                Double.parseDouble(args[1]);
                                            } catch (NumberFormatException e) {
                                                isNumber = false;
                                            }
                                            if (isNumber) {
                                                BigDecimal money = new BigDecimal(args[1]);
                                                if (money.doubleValue() > 0) {
                                                    BigDecimal taxa = BigDecimal.valueOf(pl.tax.taxValue(Double.parseDouble(args[1])));
                                                    BigDecimal moneyWithTax = new BigDecimal(String.valueOf(money)).add(taxa);
                                                    if (hasEnough(p.getUniqueId(), moneyWithTax)) {
                                                        subtract(p.getUniqueId(), moneyWithTax);
                                                        add(player.getUniqueId(), money);
                                                        pl.config.getFile("translate").getStringList("economy.paid-money").forEach(s -> p.sendMessage(s.replace("&", "§")
                                                                .replace("$taxa", format(taxa))
                                                                .replace("$player", player.getDisplayName())
                                                                .replace("$money", format(money))));
                                                        pl.sqlitetaxas.updateValueClan((float) Double.parseDouble(formato.format((pl.sqlitetaxas.getValueClan().floatValue() + Double.parseDouble(formato.format(pl.tax.divideMoney(taxa.doubleValue(), "clan")))))));
                                                        pl.sqlitetaxas.updateValuePrefeitura((float) Double.parseDouble(formato.format((pl.sqlitetaxas.getValuePrefeitura().floatValue() + Double.parseDouble(formato.format(pl.tax.divideMoney(taxa.doubleValue(), "pf")))))));
                                                        player.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.received-money"))
                                                                .replace("&", "§")
                                                                .replace("$player", p.getDisplayName())
                                                                .replace("$money", format(money)));
                                                    } else {
                                                        pl.config.getFile("translate").getStringList("economy.dont-have-money").forEach(s -> p.sendMessage(s.replace("&", "§")
                                                                .replace("$total", format(moneyWithTax))
                                                                .replace("$taxa", format(taxa))));
                                                        break;
                                                    }
                                                } else {
                                                    p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.invalid-number")).replace("&", "§"));
                                                    break;
                                                }
                                            } else {
                                                p.sendMessage(Objects.requireNonNull(pl.config.getFile("translate").getString("economy.invalid-number")).replace("&", "§"));
                                                break;
                                            }
                                        } catch (UserDoesNotExistException | NoLoanPermittedException | MaxMoneyException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }else{
                                p.sendMessage("§cUse /pagar {nick} {valor}");
                            }
                        }
                    } else {
                        p.sendMessage("§cUse /pagar {nick} {valor}");
                    }
                }
            }



        return false;
    }
}
