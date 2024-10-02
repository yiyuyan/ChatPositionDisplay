package cn.ksmcbrigade.cpd.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Config {
    private File config = new File("config/cpd-config.json");

    private boolean bedChat = true;
    private Mode mode = Mode.PLAYER;

    public Config(File file) throws IOException {
        this.config = file;
        save(false);
        try {
            JsonObject object = JsonParser.parseString(FileUtils.readFileToString(this.config)).getAsJsonObject();
            this.mode = Mode.valueOf(object.get("mode").getAsString());
            this.bedChat = object.get("bedChat").getAsBoolean();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public Config(Mode mode,boolean bed){
        this.mode = mode;
        this.bedChat = bed;
    }

    public void setMode(Mode mode) throws IOException {
        this.mode = mode;
        this.save(true);
    }

    public Mode getMode() {
        return mode;
    }

    public boolean isBedChat() {
        return bedChat;
    }

    public void save(boolean ex) throws IOException {
        if(!config.exists() || ex){
            JsonObject object = new JsonObject();
            object.addProperty("mode",this.mode.name());
            object.addProperty("bedChat",this.bedChat);
            FileUtils.writeStringToFile(this.config,object.toString());
        }
    }
}
