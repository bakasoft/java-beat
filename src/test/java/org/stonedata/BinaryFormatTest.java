package org.stonedata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.stonedata.formats.binary.input.CreatorRepository;
import org.stonedata.formats.binary.input.ValueType;
import org.stonedata.formats.binary.input.impl.Creators;
import org.stonedata.formats.binary.input.impl.DefaultCreatorRepository;
import org.stonedata.formats.binary.output.DescriberRepository;
import org.stonedata.formats.binary.output.impl.DefaultDescriberRepository;
import org.stonedata.formats.binary.output.impl.Describers;
import org.stonedata.formats.binary.output.impl.HashReferenceStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class BinaryFormatTest {

    public static class Song {
        public String title;
        public String artist;
        public Integer duration;
        public int track;
        public boolean instrumental;
    }

    public DescriberRepository buildDescribers() {
        var repository = new DefaultDescriberRepository();

        repository.addDescriber(Describers.builder(Song.class)
                .withTypeIndex(1)
                .withGetter(1, song -> song.title)
                .withGetter(2, song -> song.artist)
                .withGetter(3, song -> song.track)
                .withGetter(4, song -> song.duration)
                .withGetter(5, song -> song.instrumental)
                .build());

        return repository;
    }

    public CreatorRepository buildCreators() {
        var repository = new DefaultCreatorRepository();

        repository.addCreator(Creators.builder(Song.class)
                .withTypeIndex(1)
                .withSetter(1, (song, value) -> song.title = value.asString())
                .withSetter(2, (song, value) -> song.artist = value.asString())
                .withSetter(3, (song, value) -> song.track = value.asInt())
                .withSetter(4, (song, value) -> song.duration = value.asIntOrNull())
                .withSetter(5, (song, value) -> song.instrumental = value.asBoolean())
                .build());

        return repository;
    }

    @Test
    public void testBinaryFormatCase1() throws IOException {
        var describers = buildDescribers();
        var buffer = new ByteArrayOutputStream();
        var references = new HashReferenceStrategy(obj -> obj instanceof Song);
        var serializer = describers.createSerializer(references);

        var song1 = buildSong(7, "Sayonara Baby", "サンボマスター", null, false);
        var song2 = buildSong(12, "キラーチューン", "東京事変", 222, false);
        var song3 = buildSong(1, "Oddloop", "frederic", 375, false);
        var songs = List.of(song1, song2, song3, song1, song2, song3);

        serializer.write(songs, buffer);

        var creators = buildCreators();
        var reader = creators.createDeserializer();

        var resultRaw = reader.readValue(new ByteArrayInputStream(buffer.toByteArray()));

        Assertions.assertEquals(ValueType.LIST, resultRaw.getType());

        var result = resultRaw.asList();

        assertEquals(songs.size(), result.size(), "Result length");

        var copy1 = result.get(0).asObject(Song.class);
        var copy2 = result.get(1).asObject(Song.class);
        var copy3 = result.get(2).asObject(Song.class);
        var ref1 = result.get(3).asObject(Song.class);
        var ref2 = result.get(4).asObject(Song.class);
        var ref3 = result.get(5).asObject(Song.class);

        assertSong(song1, copy1);
        assertSong(song2, copy2);
        assertSong(song3, copy3);

        assertSame(copy1, ref1);
        assertSame(copy2, ref2);
        assertSame(copy3, ref3);
    }

    private void assertSong(Song expected, Song actual) {
        assertEquals(expected.title, actual.title, "Title");
        assertEquals(expected.artist, actual.artist, "Artist");
        assertEquals(expected.track, actual.track, "Track");
        assertEquals(expected.duration, actual.duration, "Duration");
    }

    private Song buildSong(int track, String title, String artist, Integer duration, boolean instrumental) {
        var song = new Song();
        song.track = track;
        song.title = title;
        song.artist = artist;
        song.duration = duration;
        song.instrumental = instrumental;
        return song;
    }

}
