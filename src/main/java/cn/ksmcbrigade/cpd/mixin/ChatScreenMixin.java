package cn.ksmcbrigade.cpd.mixin;

import cn.ksmcbrigade.cpd.ChatPositionDisplay;
import cn.ksmcbrigade.cpd.config.Mode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.IOException;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow protected EditBox input;

    @Shadow public abstract void tick();

    @Unique
    private String chatPositionDisplay$text = "0 0 0";

    protected ChatScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init",at = @At("TAIL"))
    public void init(CallbackInfo ci){
        if(this.minecraft==null) this.minecraft = Minecraft.getInstance();
        if(this.minecraft.player==null) return;

        if((this.minecraft.screen instanceof InBedChatScreen) && !ChatPositionDisplay.config.isBedChat()){
            return;
        }

        this.input.setCanLoseFocus(true);

        this.chatPositionDisplay$text = ChatPositionDisplay.config.getMode().getPos(this.minecraft.player);

        Button copy = Button.builder(Component.translatable("gui.cpd.copy"), p_93751_ -> {
            this.minecraft.keyboardHandler.setClipboard(this.chatPositionDisplay$text);
            this.setInitialFocus(this.input);
        }).bounds((this.width-this.font.width(this.chatPositionDisplay$text))/2+this.font.width(this.chatPositionDisplay$text)+2,6,35,9).build();

        Button copyTo = Button.builder(Component.translatable("gui.cpd.copy_to"), p_93751_ -> {
            this.input.setValue(this.input.getValue()+this.chatPositionDisplay$text);
            this.setInitialFocus(this.input);
        }).bounds((this.width-this.font.width(this.chatPositionDisplay$text))/2+this.font.width(this.chatPositionDisplay$text)+2+35+2,6,65,9).build();

        this.addRenderableWidget(Button.builder(ChatPositionDisplay.config.getMode().get(), p_93751_ -> {
            Mode mode = Mode.getNext(ChatPositionDisplay.config.getMode());
            try {
                ChatPositionDisplay.config.setMode(mode);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.chatPositionDisplay$text = mode.getPos(this.minecraft.player);
            p_93751_.setMessage(mode.get());

            p_93751_.setPosition((this.width-this.font.width(this.chatPositionDisplay$text))/2-65-2,6);
            copy.setPosition((this.width-this.font.width(this.chatPositionDisplay$text))/2+this.font.width(this.chatPositionDisplay$text)+2,6);
            copyTo.setPosition((this.width-this.font.width(this.chatPositionDisplay$text))/2+this.font.width(this.chatPositionDisplay$text)+2+35+2,6);

            this.setInitialFocus(this.input);

        }).bounds((this.width-this.font.width(this.chatPositionDisplay$text))/2-65-2,6,65,9).build());

        this.addRenderableWidget(copy);
        this.addRenderableWidget(copyTo);
    }

    @Inject(method = "render",at = @At("HEAD"))
    public void render(GuiGraphics p_282470_, int p_282674_, int p_282014_, float p_283132_, CallbackInfo ci){
        if(this.minecraft==null) this.minecraft = Minecraft.getInstance();
        if(this.minecraft.player==null) return;

        if((this.minecraft.screen instanceof InBedChatScreen) && !ChatPositionDisplay.config.isBedChat()){
            return;
        }

        p_282470_.drawString(this.minecraft.font,this.chatPositionDisplay$text,(p_282470_.guiWidth()-this.minecraft.font.width(this.chatPositionDisplay$text))/2,6, Color.WHITE.getRGB());
    }
}
