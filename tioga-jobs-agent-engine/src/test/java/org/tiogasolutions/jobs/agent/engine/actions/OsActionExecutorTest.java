package org.tiogasolutions.jobs.agent.engine.actions;

import org.testng.annotations.Test;
import org.tiogasolutions.jobs.agent.engine.actions.OsActionExecutor;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

@Test
public class OsActionExecutorTest {

  public void testSplitCommand_empty() {
    String expected = OsActionExecutor.splitCommand("").toString();
    assertEquals(expected, "[]");
  }

  public void testSplitCommand_oneWord() {
    String expected = OsActionExecutor.splitCommand("aaa").toString();
    assertEquals(expected, Collections.singletonList("aaa").toString());
  }

  public void testSplitCommand_twoWords() {
    String expected = OsActionExecutor.splitCommand("aaa bbb").toString();
    assertEquals(expected, asList("aaa","bbb").toString());
  }

  public void testSplitCommand_threeWords() {
    String expected = OsActionExecutor.splitCommand("aaa bbb ccc").toString();
    assertEquals(expected, asList("aaa","bbb", "ccc").toString());
  }

  public void testSplitCommand_threeWordsFirstQuoted() {
    String expected = OsActionExecutor.splitCommand("\"aaa\" bbb ccc").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsSecondQuoted() {
    String expected = OsActionExecutor.splitCommand("aaa \"bbb\" ccc").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsLastQuoted() {
    String expected = OsActionExecutor.splitCommand("aaa bbb \"ccc\"").toString();
    assertEquals(expected, asList("aaa","bbb","ccc").toString());
  }

  public void testSplitCommand_threeWordsFirstQuotedWithSpace() {
    String expected = OsActionExecutor.splitCommand("\"aaa bbb\" ccc ddd").toString();
    assertEquals(expected, asList("aaa bbb","ccc", "ddd").toString());
  }

  public void testSplitCommand_threeWordsSecondQuotedWithSpace() {
    String expected = OsActionExecutor.splitCommand("aaa \"bbb ccc\" ddd").toString();
    assertEquals(expected, asList("aaa","bbb ccc", "ddd").toString());
  }

  public void testSplitCommand_threeWordsLastQuotedWithSpace() {
    String expected = OsActionExecutor.splitCommand("aaa bbb \"ccc ddd\"").toString();
    assertEquals(expected, asList("aaa","bbb","ccc ddd").toString());
  }
}