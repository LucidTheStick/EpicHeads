package com.songoda.epicheads.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicheads.EpicHeads;
import com.songoda.epicheads.head.Category;
import com.songoda.epicheads.head.Head;
import com.songoda.epicheads.head.HeadManager;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class CommandAdd extends AbstractCommand {

    private final EpicHeads plugin;

    public CommandAdd(EpicHeads plugin) {
        super(CommandType.CONSOLE_OK, "add");
        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 3) return ReturnType.SYNTAX_ERROR;

        String url = args[0];
        String name = args[1].replace("_", " ");
        String categoryStr = args[2].replace("_", " ");

        HeadManager headManager = plugin.getHeadManager();

        if (headManager.getLocalHeads().stream().anyMatch(head -> head.getURL().equals(url))) {
            plugin.getLocale().getMessage("command.add.exists").sendPrefixedMessage(sender);
            return ReturnType.FAILURE;
        }

        List<Category> categories = headManager.getCategories().stream().filter(category1 -> category1.getName().equals(categoryStr)).collect(Collectors.toList());

        Category category = categories.isEmpty() ? new Category(categoryStr) : categories.get(0);

        Head head = new Head(headManager.getNextLocalId(), name, url, category, true, null, (byte) 0);
        headManager.addLocalHead(head);
        plugin.getDataManager().createLocalHead(head);

        plugin.getLocale().getMessage("command.add.success")
                .processPlaceholder("name", name).sendPrefixedMessage(sender);
        if (categories.isEmpty()) {
            plugin.reloadConfig();
        }
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "epicheads.add";
    }

    @Override
    public String getSyntax() {
        return "add <url> <name> <category>";
    }

    @Override
    public String getDescription() {
        return "Adds a head to your local database. Including a category that does not exist will create new category. Make sure to use underscores and now spaces.";
    }
}
