package cn.ksmcbrigade.cpd;

import cn.ksmcbrigade.cpd.config.Config;
import cn.ksmcbrigade.cpd.config.Mode;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

@Mod(ChatPositionDisplay.MODID)
public class ChatPositionDisplay {

    public static final String MODID = "cpd";

    public static final Logger LOGGER = LogManager.getLogger();

    public static final Config config;

    public static final KeyMapping playerPosition = new KeyMapping("key.cpd.player", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.Type.KEYSYM,InputConstants.KEY_C,KeyMapping.CATEGORY_GAMEPLAY);
    public static final KeyMapping blockPosition = new KeyMapping("key.cpd.block", KeyConflictContext.IN_GAME, KeyModifier.ALT, InputConstants.Type.KEYSYM,InputConstants.KEY_X,KeyMapping.CATEGORY_GAMEPLAY);

    static {
        Config config1;
        try {
            config1 = new Config(new File("config/cpd-config.json"));
        } catch (IOException e) {
            config1 = new Config(Mode.PLAYER,true);
        }
        config = config1;
    }

    public ChatPositionDisplay() {
        MinecraftForge.EVENT_BUS.register(this);

        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, playerPosition);
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, blockPosition);

        LOGGER.info("ChatPositionDisplay mod loaded.");
    }

    @SubscribeEvent
    public void input(InputEvent.Key event){
        if(Minecraft.getInstance().player==null) return;
        if(playerPosition.isDown()){
            String pos = Mode.PLAYER.getPos(Minecraft.getInstance().player);
            Minecraft.getInstance().keyboardHandler.setClipboard(pos);
            Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat.cpd.player",pos),false);
        }
        if(blockPosition.isDown()){
            String pos = Mode.BLOCK.getPos(Minecraft.getInstance().player);
            Minecraft.getInstance().keyboardHandler.setClipboard(pos);
            Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat.cpd.block",pos),false);
        }
    }
}
