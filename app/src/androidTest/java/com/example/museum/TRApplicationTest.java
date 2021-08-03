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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TRApplicationTest {

    private final String sampleText = "I find it helpful to imagine writing in a blizzard with" +
            "every inscription designed to prevent snow crystals from drifting in.";

    @Test
    public void getKeywordsTest() {
        TRApplication.initialize(getInstrumentation().getTargetContext());
        List<String> keywords = TRApplication.get().getKeywords(sampleText);
        assertNotNull(keywords);
        assertEquals(keywords.size(), TRApplication.MAX_KEYWORDS);
        assertEquals(keywords, Arrays.asList("snow", "helpful", "prevent", "imagine", "designed", "writing"));
    }



    @Test
    public void onAnalysisTest() {
        TRApplication.get().onAnalysis(sampleText, new Callback() {
            @Override
            public void run(Map<String, List<Piece>> options) {
                Log.d("TRApplicationTest", options.toString());
                assertNotNull(options.size() != 0);
            }
        });
    }

    @Test
    public void testTest() {
        assertTrue(true);
    }

}