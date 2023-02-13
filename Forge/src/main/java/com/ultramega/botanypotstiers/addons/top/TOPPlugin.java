package com.ultramega.botanypotstiers.addons.top;

import com.ultramega.botanypotstiers.Constants;
import com.ultramega.botanypotstiers.block.TieredBlockEntityBotanyPot;
import com.ultramega.botanypotstiers.block.inv.TieredBotanyPotContainer;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.styles.ProgressStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class TOPPlugin implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(Constants.MOD_ID, "top_pot_info");

    @Override
    public ResourceLocation getID() {
        return PLUGIN_ID;
    }

    @Override
    public Void apply(ITheOneProbe probeMod) {
        probeMod.registerProvider(this);
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo info, Player player, Level level, BlockState state, IProbeHitData data) {
        if (level.getBlockEntity(data.getPos()) instanceof TieredBlockEntityBotanyPot pot) {
            final TieredBotanyPotContainer inv = pot.getInventory();

            if (!inv.getSoilStack().isEmpty()) {
                final IProbeInfo soilInfo = info.horizontal(info.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                soilInfo.item(inv.getSoilStack());
                soilInfo.mcText(Component.translatable("tooltip.botanypots.soil", inv.getSoilStack().getHoverName()));
            }

            if (!inv.getCropStack().isEmpty()) {
                final IProbeInfo seedInfo = info.horizontal(info.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
                seedInfo.item(inv.getCropStack());
                seedInfo.mcText(Component.translatable("tooltip.botanypots.seed", inv.getCropStack().getHoverName()));
            }

            if (pot.isGrowing()) {
                final ProgressStyle style = new ProgressStyle();
                style.filledColor(0xff32CD32);
                style.alternateFilledColor(0xff259925);
                style.prefix("tooltip.botanypots.progress");
                style.suffix("%");

                final int f = Math.min(Mth.floor((float)pot.getGrowthTime() / (float)inv.getRequiredGrowthTime() * 100f), 100);
                info.progress(f, 100, style);
            }
        }
    }
}
