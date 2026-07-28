package com.betier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityNameRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BETierClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("BETier");
    // 当前选中展示模式，null=展示总分段位
    public static String currentViewMode = TierConstants.MODES[0];
    private static KeyMapping cycleModeKey;
    private static KeyMapping openGuiKey;

    @Override
    public void onInitializeClient() {
        registerKeybinds();
        registerRenderEvents();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (cycleModeKey.consumeClick()) cycleMode();
            while (openGuiKey.consumeClick()) openRankGui();
        });
        TierApiClient.fetchAllPlayers(null);
    }

    private void registerKeybinds() {
        // F5 切换模式
        cycleModeKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.betier.cycle_mode",
                90,
                "category.betier"
        ));
        // F6 打开排行榜
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.betier.open_rank",
                91,
                "category.betier"
        ));
    }

    // 循环切换游戏模式
    private void cycleMode() {
        LocalPlayer p = Minecraft.getInstance().player;
        if (p == null) return;
        int idx = -1;
        for (int i = 0; i < TierConstants.MODES.length; i++) {
            if (TierConstants.MODES[i].equals(currentViewMode)) idx = i;
        }
        int nextIdx = (idx + 1) % TierConstants.MODES.length;
        currentViewMode = TierConstants.MODES[nextIdx];
        p.sendSystemMessage(Component.literal("§b当前展示模式：" + currentViewMode));
    }

    private void openRankGui() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        mc.setScreen(new TierRankScreen());
    }

    private void registerRenderEvents() {
        // ==========【核心修改：名字左侧渲染Tier标签】==========
        LivingEntityNameRenderEvents.BEFORE.register((entity, context, text) -> {
            if (!(entity instanceof Player targetPlayer)) return;
            CachedPlayer cache = TierApiClient.getCachedPlayer(targetPlayer.getName().getString());
            if (cache == null) return;
            PlayerTierData data = cache.data;

            String tier = data.getTierByMode(currentViewMode);
            String labelText = "[" + currentViewMode + ":" + tier + "] ";
            int labelColor = TierConstants.getTierLabelColor(tier);

            // 文字宽度，用来向左偏移绘制
            int textWidth = context.font().width(Component.literal(labelText));

            // 向左平移坐标，标签绘制在玩家名字左侧
            context.poseStack().translate(-textWidth - 2, 0, 0);

            // 半透明黑色背景
            context.fill(0, 0, textWidth, context.font().getHeight() + 2, 0x80000000);
            // 绘制段位标签
            context.drawText(context.font(), Component.literal(labelText), 2, 1, labelColor, false);

            // 恢复坐标，防止玩家名字错位
            context.poseStack().translate(textWidth + 2, 0, 0);
        });

        // 左上角HUD（自身信息）
        HudRenderCallback.EVENT.register((ctx, tick) -> {
            LocalPlayer self = Minecraft.getInstance().player;
            if (self == null) return;
            CachedPlayer selfCache = TierApiClient.getCachedPlayer(self.getName().getString());
            if (selfCache == null) return;
            PlayerTierData data = selfCache.data;

            int y = 10;
            ctx.drawText(ctx.font(), Component.literal("§dBETier"), 10, y, 0xFFB35CFF, false);
            y += 12;
            String tier = data.getTierByMode(currentViewMode);
            ctx.drawText(ctx.font(), Component.literal("当前模式[" + currentViewMode + "] : " + tier),
                    10, y, TierConstants.getTierLabelColor(tier), false);
        });
    }

    public static String getTotalTierName(int total) {
        if (total >= 800) return "GrandMaster";
        if (total >= 500) return "Master";
        if (total >= 200) return "Ace";
        if (total >= 100) return "Specialist";
        if (total >= 40) return "Cadet";
        if (total >= 20) return "Novice";
        return "Rookie";
    }
}
