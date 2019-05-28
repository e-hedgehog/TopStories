package com.ehedgehog.android.topstories;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;

import com.ehedgehog.android.topstories.model.Article;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CatsGalleryFragmentTest {

    @Rule
    public ActivityTestRule<StoriesListActivity> mActivityTestRule =
            new ActivityTestRule<>(StoriesListActivity.class);

    @Rule
    public final MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    Article mArticle;

    private StoriesListFragment fragment;

    @Before
    public void setup() {
        fragment = new StoriesListFragment();
        mActivityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().add(R.id.fragment_container, fragment).commit();

        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.ehedgehog.android.topstories", appContext.getPackageName());
    }

    @Test
    public void testFragment() {
        Fragment fragment = mActivityTestRule.getActivity()
                .getSupportFragmentManager().getFragments().get(0);
        assertTrue(fragment instanceof StoriesListFragment);
    }

    @Test
    public void testFragmentHasRecyclerView() {
        RecyclerView recyclerView = fragment.getView().findViewById(R.id.stories_recycler_view);
        assertTrue(recyclerView.isShown());
    }

    @Test
    public void testRecyclerViewItemClick() {
        RecyclerView recyclerView = fragment.getView().findViewById(R.id.stories_recycler_view);
        StoryHolder holder = (StoryHolder) recyclerView.findViewHolderForAdapterPosition(1);

        assertTrue(holder.itemView.isClickable());
    }

    @Test
    public void testSwipeRefreshIsShown() {
        SwipeRefreshLayout swipeRefreshLayout = fragment.getView()
                .findViewById(R.id.stories_list_refresh_container);
        assertTrue(swipeRefreshLayout.isShown());
    }

    @Test
    public void testSpinnerIsShown() {
        Spinner spinner = fragment.getView().findViewById(R.id.stories_category_spinner);
        assertTrue(spinner.isShown());
    }
}
