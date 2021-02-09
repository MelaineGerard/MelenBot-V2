package fr.melaine_gerard.melenbot.commands.music;

import fr.melaine_gerard.melenbot.enumerations.Category;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class PlayCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de jouer une musique avec le bot";
    }

    @Override
    public Category getCategory() {
        return Category.MUSIC;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event,
                       List<String> args) {
        final TextChannel channel = event.getChannel();
        final Member selfMember = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if(args.isEmpty()){
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer le lien d'une vidéo youtube !").build()).queue();
            return;
        }

        if(!selfVoiceState.inVoiceChannel()){
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans un salon vocal !").build()).queue();
            return;
        }

        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if(!memberVoiceState.inVoiceChannel()){
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Tu dois être dans un salon vocal !").build()).queue();
            return;
        }

        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessage(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans le même salon que toi !").build()).queue();
            return;
        }

        String link = String.join(" ", args);

        if(!isUrl(link)){
            link = "ytsearch:" + link;
        }

        PlayerManager.getInstance().loadAndPlay(channel, link);

    }

    private boolean isUrl(String url){
        try {
            new URI(url);
            return true;
        }catch (URISyntaxException e){
            return false;
        }
    }
}
