package thornyaplugin.thornyaplugin;

import com.earth2me.essentials.Essentials;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import thornyaplugin.thornyaplugin.commands.Thornya;
import thornyaplugin.thornyaplugin.events.ThornyaE;
import thornyaplugin.thornyaplugin.prefeitura.commands.Prefeitura;
import thornyaplugin.thornyaplugin.prefeitura.commands.TabPrefeitura;
import thornyaplugin.thornyaplugin.prefeitura.database.SQLite;
import thornyaplugin.thornyaplugin.taxas.Economy;
import thornyaplugin.thornyaplugin.taxas.Tax;
import thornyaplugin.thornyaplugin.taxas.database.Mysql;
import thornyaplugin.thornyaplugin.taxas.events.QuickShop;
import thornyaplugin.thornyaplugin.utils.ConfigAPI;
import thornyaplugin.thornyaplugin.utils.TimeFormatter;
import thornyaplugin.thornyaplugin.vars.*;

import java.io.*;
import java.util.Objects;

public final class ThornyaCommands extends JavaPlugin {

    public thornyaplugin.thornyaplugin.taxas.database.SQLite sqlitetaxas;
    public thornyaplugin.thornyaplugin.taxas.database.Mysql mysqltaxas;
    public Tax tax;

    public SimpleClans sc;
    public Essentials ess = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
    public ThornyaAPI api = (ThornyaAPI) getServer().getPluginManager().getPlugin("ThornyaAPI");
    Plugin clanPL = getServer().getPluginManager().getPlugin("SimpleClans");
    public SQLite sqlLeis;
    public Global global;
    public ConfigAPI config;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§ePluginAPI carregado com sucesso!");
        carregarconfigs();
        registrarListener(new QuickShop(this));
        if (clanPL != null) { sc = api.sc; }
        this.sqlitetaxas = new thornyaplugin.thornyaplugin.taxas.database.SQLite(this);
        sqlLeis = new SQLite(this);
        this.mysqltaxas = new Mysql(this);
        this.tax = new Tax(this);

        this.global = api.global;
        this.config = api.config;
        registrarComandos();
    }

    public void registrarComandos(){
        registrarComando("thornya", new Thornya(this));
        registrarComando("prefeitura", new Prefeitura(this));
        Objects.requireNonNull(getCommand("prefeitura")).setTabCompleter(new TabPrefeitura(this));
        registrarComando("pagar", new Economy(this));
        registrarComando("taxa", new Economy(this));
    }

    public void registrarComando(String nome, CommandExecutor comando) {
        Objects.requireNonNull(this.getCommand(nome)).setExecutor(comando);
    }

    public void registrarListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void carregarconfigs(){
        config.criarConfig("configuration");
        config.criarConfig("prefeitura");
        config.criarConfig("translate");
    }
    public void salvarConfig(){
        config.saveConfig("configuration");
        config.saveConfig("prefeitura");
        config.saveConfig("translate");
    }
    public void reloadAll(){
        salvarConfig();
        config.reloadConfig("configuration");
        config.reloadConfig("prefeitura");
        config.reloadConfig("translate");
    }

}
