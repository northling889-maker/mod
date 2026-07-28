package com.betier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.List;

public class TierRankScreen extends Screen {
    private List<PlayerTierData> sortedList;
    private int scrollY = 0;
    private static final int ROW_HEIGHT = 22;

    public TierRankScreen() {
        super(Component.literal("BETier PVP段位榜单"));
        refreshList();
    }

    private void refreshList() {
        sortedList = TierApiClient.ALL_PLAYERS_CACHE.stream()
                .sorted(Comparator.comparingInt(d -> -d.calcTotal()))
                .toList();
    }

    @Override
    protected void init() {
        super.init();
        int btnX = 10;
        for (String mode : TierConstants.MODES) {
            addRenderableWidget(Button.builder(Component.literal(mode), btn -> {
                BETierClient.currentViewMode = mode;
            }).bounds(btnX, 10, 70, 18).build());
            btnX += 75;
        }
        addRenderableWidget(Button.builder(Component.literal("总分"), btn -> {
            BETierClient.currentViewMode = null;
        }).bounds(btnX, 10, 70, 18).build());

        addRenderableWidget(Button.builder(Component.literal("刷新数据"), btn -> {
            TierApiClient.fetchAllPlayers(this::refreshList);
        }).bounds(width - 100, 10, 90, 18).build());
    }

    @Override
    public void render(GuiGraphics gui, int mx, int my, float tick) {
        super.render(gui, mx, my, tick);
        int top = 35;
        int maxShow = (height - top - 20) / ROW_HEIGHT;
        int startIdx = scrollY / ROW_HEIGHT;
        for (int i = startIdx; i < Math.min(startIdx + maxShow, sortedList.size()); i++) {
            PlayerTierData p = sortedList.get(i);
            int drawY = top + (i - startIdx) * ROW_HEIGHT;
            gui.fill(5, drawY, width - 5, drawY + ROW_HEIGHT, 0x40101018);

            int rankColor = 0xFFFFFFFF;
            if (i == 0) rankColor = TierConstants.GOLD;
            if (i == 1) rankColor = TierConstants.SILVER;
            if (i == 2) rankColor = TierConstants.BRONZE;
            gui.drawString(font, "#" + (i + 1), 10, drawY + 4, rankColor);
            gui.drawString(font, p.name, 60, drawY + 4, 0xFFEEEEEE);
            int total = p.calcTotal();
            gui.drawString(font, String.valueOf(total), 180, drawY + 4, TierConstants.getTotalTierColor(total));

            String tierText;
            int tierCol;
            if (BETierClient.currentViewMode == null) {
                tierText = BETierClient.getTotalTierName(total);
                tierCol = TierConstants.getTotalTierColor(total);
            } else {
                String t = p.getTierByMode(BETierClient.currentViewMode);
                tierText = t;
                tierCol = TierConstants.getTierLabelColor(t);
            }
            gui.drawString(font, tierText, 260, drawY + 4, tierCol);
        }
    }

    @Override
    public boolean mouseScrolled(double x, double y, double amountX, double amountY) {
        scrollY += (int) (amountY * 10);
        if (scrollY < 0) scrollY = 0;
        int maxScroll = sortedList.size() * ROW_HEIGHT - (height - 40);
        if (scrollY > maxScroll) scrollY = maxScroll;
        return true;
    }
}
