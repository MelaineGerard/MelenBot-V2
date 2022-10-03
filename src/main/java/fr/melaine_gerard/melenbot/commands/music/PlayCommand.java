package fr.melaine_gerard.melenbot.commands.music;

import fr.melaine_gerard.melenbot.MelenBot;
import fr.melaine_gerard.melenbot.enumerations.CommandCategory;
import fr.melaine_gerard.melenbot.interfaces.ICommand;
import fr.melaine_gerard.melenbot.utils.EmbedUtils;
import fr.melaine_gerard.melenbot.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class PlayCommand implements ICommand {
    @Override
    public String getDescription() {
        return "Permet de jouer une musique avec le bot";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MUSIC;
    }

    @Override
    public void handle(MessageReceivedEvent event,
                       List<String> args) {
        final MessageChannelUnion channel = event.getChannel();
        final Member selfMember = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (args.isEmpty()) {
            channel.sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Merci d'indiquer le lien d'une vidéo youtube !").build()).queue();
            return;
        }
        if (selfVoiceState == null || !selfVoiceState.inAudioChannel()) {
            channel.sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans un salon vocal !").build()).queue();
            return;
        }

        final Member member = event.getMember();
        if (member == null) return;
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (memberVoiceState == null) return;
        if (!memberVoiceState.inAudioChannel()) {
            channel.sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Tu dois être dans un salon vocal !").build()).queue();
            return;
        }
        if (memberVoiceState.getChannel() == null) return;
        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessageEmbeds(EmbedUtils.createErrorEmbed(event.getJDA(), "Je dois être dans le même salon que toi !").build()).queue();
            return;
        }

        String link = String.join(" ", args);
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        } else if (link.contains("spotify.com")) {
            try {
                SpotifyApi spotifyApi = new SpotifyApi.Builder()
                        .setClientId(MelenBot.getConfig().getString("spotify.id"))
                        .setClientSecret(MelenBot.getConfig().getString("spotify.secret"))
                        .build();
                ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
                        .build();
                final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
                spotifyApi.setAccessToken(clientCredentials.getAccessToken());
                String spotifyTrackId = link.replace("https://open.spotify.com/track/", "").split("si=")[0].replace("?", "");
                GetTrackRequest getTrackRequest = spotifyApi.getTrack(spotifyTrackId).build();
                Track track = getTrackRequest.execute();
                link = "ytsearch:" + track.getName() + " - " + track.getArtists()[0].getName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        PlayerManager.getInstance().loadAndPlay(channel.asGuildMessageChannel(), link);

    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
