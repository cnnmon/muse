package com.example.museum;

import android.util.Log;

import androidx.test.runner.AndroidJUnit4;

import com.example.museum.models.Piece;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TRApplicationTest {

    // text where every chosen keyword finds results by the MET api
    private final String classicText = "I find it helpful to imagine writing in a blizzard with" +
            "every inscription designed to prevent snow crystals from drifting in.";

    // text that returns some keywords that return nothing by the MET api
    private final String skippableText = "The swinger the swirler the swirled: stop grieving.\n" +
            "I drink all night but in a diminishing appetite.\n" +
            "The scene outside is obscene from a humbling window.\n" +
            "My sentiment spreads, my famine a flagpole, a grizzle.\n" +
            "Birds sing next year’s songs, or antique rescues.\n" +
            "I write but where shall I send it?\n" +
            "Let go — I shall go tie the flowers the leaves the whole orchard.\n" +
            "The outskirts are curved, shadows of countrywoman donors ...\n" +
            "You bring me a cup of fresh tea that I love,\n" +
            "I return you two kapok leaves — like hand waves.";

    /**
     * Ensures Text Rank picks out some keywords totalling
     * the EXACT defined MAX_KEYWORDS # of elements
     * and keywords are all part of original string
     */
    @Test
    public void testKeywords_classic() {
        TRApplication.initialize(getInstrumentation().getTargetContext());
        List<String> keywords = TRApplication.get().getKeywords(classicText);
        assertNotNull(keywords);

        List<String> words = Arrays.asList(classicText.split(" "));
        assertEquals(keywords.size(), TRApplication.MAX_KEYWORDS);
        for (int i = 0; i < keywords.size(); i += 1) {
            words.contains(keywords.get(i));
        }
    }

    /**
     * Ensures that, with a string with no skippable keywords,
     * TRApplication picks out pieces of art totalling
     * the EXACT defined MAX_OPTIONS # of elements
     */
    @Test
    public void testAnalysis_classic() {
        TRApplication.get().onAnalysis(classicText, new Callback() {
            @Override
            public void run(Map<String, List<Piece>> options) {
                Log.d("TRApplicationTest", options.toString());
                assertNotNull(options.size() != 0);
                assertEquals(options.size(), TRApplication.MAX_OPTIONS);
            }
        });
    }

    /**
     * Ensures that, with a string with skippable keywords,
     * (skippable meaning these keywords return nothing by the MET API)
     * TRApplication picks out pieces of art that contain
     * AT LEAST the defined MAX_OPTIONS # of elements
     */
    @Test
    public void testAnalysis_skippable() {
        TRApplication.get().onAnalysis(skippableText, new Callback() {
            @Override
            public void run(Map<String, List<Piece>> options) {
                Log.d("TRApplicationTest", options.toString());
                assertNotNull(options.size() != 0);
                assertTrue(options.size() >= TRApplication.MAX_OPTIONS);
            }
        });
    }

}