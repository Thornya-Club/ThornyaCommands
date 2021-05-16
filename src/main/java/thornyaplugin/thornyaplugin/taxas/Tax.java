package thornyaplugin.thornyaplugin.taxas;


import org.bukkit.Bukkit;
import thornyaplugin.thornyaplugin.ThornyaCommands;

public class Tax {
    private ThornyaCommands pl;
    public Double taxValue;
    public Double taxClan;
    public Double taxTotal;

    public Tax(ThornyaCommands pl){
        this.pl = pl;
        this.taxValue = pl.config.getFile("configuration").getDouble("tax-prefeitura");
        this.taxClan = pl.config.getFile("configuration").getDouble("tax-supremacia");
        this.taxTotal = this.taxValue + this.taxClan;
    }
    public Double taxValue(Double money){
        return money * this.taxTotal * 1;
    }
    public static boolean hasMoney(Double balance, Double hasM){
        return balance >= hasM;
    }

    public Double divideMoney(Double taxa, String nome){
        double taxasPrefeituraA = (this.taxValue*100)/this.taxTotal;
        double taxasClanA = (this.taxClan*100)/this.taxTotal;
        taxasPrefeituraA = taxasPrefeituraA/100;
        taxasClanA = taxasClanA/100;

        taxasPrefeituraA = taxa * taxasPrefeituraA;
        taxasClanA = taxa * taxasClanA;
        if(nome.equalsIgnoreCase("pf")){
            return taxasPrefeituraA;
        }else if(nome.equalsIgnoreCase("clan")){
            return taxasClanA;
        }
        Bukkit.getConsoleSender().sendMessage("Dividindo para BUG ERROR");
        return taxasPrefeituraA;
    }


}
