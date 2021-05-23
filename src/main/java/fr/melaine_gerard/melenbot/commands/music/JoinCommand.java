package fr.melaine_gerard.melenbot.commands.music;

import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class JoinCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet au bot de rejoindre ton salon vocal";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MUSIC;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, List<String> args) {
        final TextChannel channel = event.getChannel();
        Member selfMember = event.getGuild().getSelfMember();
        GuildVoiceState selfMemberVoiceState = selfMember.getVoiceState();
        if(selfMemberVoiceState == null) return;
        if (selfMemberVoiceState.inVoiceChannel()){
            channel.sendMessage("Je suis déjà dans un salon vocal !").queue();
            return;
        }

        Member member = event.getMember();
        if(member == null) return;
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null)return;
        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("Tu dois être dans un salon vocal !").queue();
            return;
        }

        final AudioManager audioManager = event.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        if (!selfMember.hasPermission(memberChannel, Permission.VOICE_CONNECT)){
            channel.sendMessage("Je ne peux pas me connecter à ce salon vocal !").queue();
            return;
        }

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessage("Connexion au salon `\uD83C\uDFB5 " + memberChannel.getName() + "`...").queue();


    }
}
