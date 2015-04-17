package org.tiogasolutions.jobs.agent.resources;

import org.testng.annotations.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

@Test
public class JobsResourceV1Test {

  public void testSplitCommand_empty() {
    String expected = JobsResourceV1.splitCommand("").toString();
    assertEquals(expected, "[]");
  }

  public void testSplitCommand_oneWord() {
    String expected = JobsResourceV1.splitCommand("aaa").toString();
    assertEquals(expected, Collections.singletonList("aaa").toString());
  }

  public void testSplitCommand_twoWords() {
    String expected = JobsResourceV1.splitCommand("aaa bbb").toString();
    assertEquals(expected, asList("aaa","bbb").toString());
  }

  public void testSplitCommand_threeWords() {
    String expected = JobsResourceV1.splitCommand("aaa bbb ccc").toString();
    assertEquals(expected, asList("aaa","bbb", "ccc").toString());
  }

  public void testSplitCommand_threeWordsFirstQuoted() {
    String expected = JobsResourceV1.splitCommand("\"aaa\" bbb ccc").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsSecondQuoted() {
    String expected = JobsResourceV1.splitCommand("aaa \"bbb\" ccc").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsLastQuoted() {
    String expected = JobsResourceV1.splitCommand("aaa bbb \"ccc\"").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsFirstQuotedWithSpace() {
    String expected = JobsResourceV1.splitCommand("\"aaa bbb\" ccc ddd").toString();
    assertEquals(expected, asList("aaa bbb","ccc", "ddd").toString());
  }

  public void testSplitCommand_threeWordsSecondQuotedWithSpace() {
    String expected = JobsResourceV1.splitCommand("aaa \"bbb ccc\" ddd").toString();
    assertEquals(expected, asList("aaa","bbb ccc", "ddd").toString());
  }

  public void testSplitCommand_threeWordsLastQuotedWithSpace() {
    String expected = JobsResourceV1.splitCommand("aaa bbb \"ccc ddd\"").toString();
    assertEquals(expected, asList("aaa","bbb","ccc ddd").toString());
  }
}