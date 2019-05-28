package com.ehedgehog.android.topstories;

import com.ehedgehog.android.topstories.network.ApiFactory;

import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ApiFactoryTest {

    @Test
    public void buildStoriesService_isNotNull() {
        assertNotEquals(ApiFactory.buildStoriesService(), null);
    }

}