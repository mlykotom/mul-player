package cz.vutbr.fit.mulplayer;

import org.junit.Test;

import cz.vutbr.fit.mulplayer.utils.Utils;

import static org.junit.Assert.assertEquals;

/**
 * @author mlyko
 * @since 11.04.2016
 */

public class UtilsTest {
    @Test
    public void testFormatTime1() {
        String formatted = Utils.formatTime(120000);
        assertEquals(formatted, "02:00");
    }

    @Test
    public void testFormatTime2() {
        String formatted = Utils.formatTime(59000);
        assertEquals(formatted, "00:59");
    }
}
