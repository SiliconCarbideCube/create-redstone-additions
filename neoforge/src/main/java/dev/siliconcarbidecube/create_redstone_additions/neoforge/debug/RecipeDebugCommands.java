package dev.siliconcarbidecube.create_redstone_additions.neoforge.debug;

import com.mojang.logging.LogUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public final class RecipeDebugCommands {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final AtomicBoolean LOGGED = new AtomicBoolean(false);

    private RecipeDebugCommands() {
    }

    public static void register(final RegisterClientCommandsEvent event) {
        event.getDispatcher().register(root(event.getBuildContext()));
        if (LOGGED.compareAndSet(false, true)) {
            LOGGER.info("[{}] Registered client debug commands", CreateRedstoneAdditions.MOD_ID);
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> root(final CommandBuildContext buildContext) {
        return Commands.literal("cra")
            .then(Commands.literal("recipe_count")
                .then(Commands.argument("item", ItemArgument.item(buildContext))
                    .executes(ctx -> {
                        ItemInput input = ItemArgument.getItem(ctx, "item");
                        ItemStack stack = input.createItemStack(1, false);
                        Item item = stack.getItem();

                        Minecraft minecraft = Minecraft.getInstance();
                        if (minecraft.level == null) {
                            ctx.getSource().sendFailure(Component.literal("No world loaded."));
                            return Command.SINGLE_SUCCESS;
                        }

                        RecipeManager recipeManager = minecraft.level.getRecipeManager();
                        int outputs = 0;
                        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
                            ItemStack result = holder.value().getResultItem(minecraft.level.registryAccess());
                            if (!result.isEmpty() && result.is(item)) {
                                outputs++;
                            }
                        }

                        String id = BuiltInRegistries.ITEM.getKey(item).toString();
                        ctx.getSource().sendSystemMessage(Component.literal("Recipes that OUTPUT " + id + ": " + outputs));
                        return Command.SINGLE_SUCCESS;
                    })))

            .then(Commands.literal("recipe_namespace")
                .then(Commands.argument("namespace", StringArgumentType.word())
                    .executes(ctx -> {
                        String namespace = StringArgumentType.getString(ctx, "namespace");

                        Minecraft minecraft = Minecraft.getInstance();
                        if (minecraft.level == null) {
                            ctx.getSource().sendFailure(Component.literal("No world loaded."));
                            return Command.SINGLE_SUCCESS;
                        }

                        RecipeManager recipeManager = minecraft.level.getRecipeManager();
                        int total = 0;
                        int emptyResult = 0;
                        String firstFew = null;
                        int listed = 0;

                        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
                            if (!holder.id().getNamespace().equals(namespace)) {
                                continue;
                            }
                            total++;

                            ItemStack result = holder.value().getResultItem(minecraft.level.registryAccess());
                            if (result.isEmpty()) {
                                emptyResult++;
                            }

                            if (listed < 5) {
                                String line = holder.id() + " -> " + (result.isEmpty() ? "<empty>" : BuiltInRegistries.ITEM.getKey(result.getItem()));
                                firstFew = firstFew == null ? line : (firstFew + "\n" + line);
                                listed++;
                            }
                        }

                        ctx.getSource().sendSystemMessage(Component.literal(
                            "Recipes in namespace '" + namespace + "': " + total + " (empty result: " + emptyResult + ")"));
                        if (firstFew != null) {
                            ctx.getSource().sendSystemMessage(Component.literal("First few:\n" + firstFew));
                        }
                        return Command.SINGLE_SUCCESS;
                    })))

            .then(Commands.literal("recipe_info")
                .then(Commands.argument("recipe", ResourceLocationArgument.id())
                    .executes(ctx -> {
                        ResourceLocation recipeId = ResourceLocationArgument.getId(ctx, "recipe");

                        Minecraft minecraft = Minecraft.getInstance();
                        if (minecraft.level == null) {
                            ctx.getSource().sendFailure(Component.literal("No world loaded."));
                            return Command.SINGLE_SUCCESS;
                        }

                        RecipeManager recipeManager = minecraft.level.getRecipeManager();
                        for (RecipeHolder<?> holder : recipeManager.getRecipes()) {
                            if (!holder.id().equals(recipeId)) {
                                continue;
                            }

                            ItemStack result = holder.value().getResultItem(minecraft.level.registryAccess());
                            ctx.getSource().sendSystemMessage(Component.literal(
                                "Recipe " + holder.id() + " type=" + BuiltInRegistries.RECIPE_SERIALIZER.getKey(holder.value().getSerializer()) +
                                    " result=" + (result.isEmpty() ? "<empty>" : BuiltInRegistries.ITEM.getKey(result.getItem()))));
                            return Command.SINGLE_SUCCESS;
                        }

                        ctx.getSource().sendFailure(Component.literal("Recipe not found: " + recipeId));
                        return Command.SINGLE_SUCCESS;
                    })));
    }
}
