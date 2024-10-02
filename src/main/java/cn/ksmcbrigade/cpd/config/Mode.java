package cn.ksmcbrigade.cpd.config;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public enum Mode {
    PLAYER,
    BLOCK,
    NETHER;

    public Component get(){
        return Component.translatable("gui.cpd.mode_"+this.name().toLowerCase());
    }

    public String getPos(Player player) {
        Vec3 pos = player.getPosition(0);
        BlockPos pos2 = player.getOnPos();
        return switch (this) {
            case BLOCK -> pos2.getX() + " " + pos2.getY() +" "+pos2.getZ();
            case NETHER -> pos2.getX() * 8 + " " + pos2.getY() + " " + pos2.getZ() * 8;
            default ->
                    String.format("%.2f", pos.x) + " " + String.format("%.2f", pos.y) + " " + String.format("%.2f", pos.z);
        };
    }

    public static Mode getNext(Mode now){
        return switch (now){
            case PLAYER -> BLOCK;
            case BLOCK -> NETHER;
            default -> PLAYER;
        };
    }
}
