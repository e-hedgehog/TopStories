package com.ehedgehog.android.topstories;

import com.ehedgehog.android.topstories.model.StoriesResponse;
import com.ehedgehog.android.topstories.network.StoriesService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class StoriesPresenterTest {

    @Rule
    public final MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public final RxSchedulersRule mSchedulersRule = new RxSchedulersRule();

    @Mock
    private StoriesService mStoriesService;

    @Mock
    private StoriesPresenter.Listener mListener;

    @InjectMocks private StoriesPresenter mPresenter;

    @Before
    public void setUp() {
        StoriesResponse response = new StoriesResponse();
        when(mStoriesService.getStories(anyString())).thenReturn(Observable.just(response));

        mPresenter.addListener(mListener);
    }

    @Test
    public void getStories() {
        TestObserver<StoriesResponse> testObserver = mPresenter.getAllStories("test").test();
        testObserver.awaitTerminalEvent();
        testObserver
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1);

    }
}
