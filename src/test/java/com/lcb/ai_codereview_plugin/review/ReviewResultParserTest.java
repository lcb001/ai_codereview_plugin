package com.lcb.ai_codereview_plugin.review;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReviewResultParserTest {

    private final ReviewResultParser parser = new ReviewResultParser();

    @Test
    public void parsesPassStatus() throws Exception {
        ReviewResult result = parser.parse("REVIEW_STATUS: PASS\nNo issues.");

        assertEquals(ReviewStatus.PASS, result.getStatus());
    }

    @Test
    public void parsesIssuesStatus() throws Exception {
        ReviewResult result = parser.parse("REVIEW_STATUS: ISSUES\n- Bug found.");

        assertEquals(ReviewStatus.ISSUES, result.getStatus());
    }

    @Test(expected = AiCodeReviewException.class)
    public void rejectsResponsesWithoutProtocol() throws Exception {
        parser.parse("Looks fine.");
    }
}
