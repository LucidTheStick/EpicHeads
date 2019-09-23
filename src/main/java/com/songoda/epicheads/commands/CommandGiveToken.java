package com.songoda.epicheads.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicheads.EpicHeads;
import com.songoda.epicheads.utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandGiveToken extends AbstractCommand {

    final EpicHeads instance;

    public CommandGiveToken(EpicHeads instance) {
        super(false, "givetoken");
        this.instance = instance;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 2) return ReturnType.SYNTAX_ERROR;

        Player player = Bukkit.getPlayer(args[0]);
        int amount = Integer.parseInt(args[1]);

        if (player == null) {
            instance.getLocale().getMessage("command.give.notonline")
                    .processPlaceholder("name", args[1]).sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        player.getInventory().addItem(Methods.createToken(amount));

        instance.getLocale().getMessage("command.givetoken.receive")
                .processPlaceholder("amount", amount).sendPrefixedMessage(player);

        if (player != sender)
            instance.getLocale().getMessage("command.givetoken.success")
                    .processPlaceholder("player", player.getName())
                    .processPlaceholder("amount", amount).sendPrefixedMessage(sender);

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicheads.givetoken";
    }

    @Override
    public String getSyntax() {
        return "/heads givetoken <player> <amount>";
    }

    @Override
    public String getDescription() {
        return "Gives the player a specified amount of player head tokens.";
    }
}