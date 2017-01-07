package org.vaadin.mvp.presenter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.test.Util.SimpleEventBus;
import org.vaadin.mvp.test.Util.MissingAnnotationPresenterEventBus.MissingAnnotationPresenter;
import org.vaadin.mvp.test.Util.SimpleEventBus.SimpleEventHandler;
import org.vaadin.mvp.test.Util.SimpleEventBus.SimplePresenter;
import org.vaadin.mvp.view.factory.ViewFactory;
import org.vaadin.mvp.view.factory.ViewFactoryException;

import com.vaadin.server.ClientConnector.DetachEvent;

@RunWith(MockitoJUnitRunner.class)
public class AbstractPresenterTest {

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ViewFactory viewFactory;

    @Mock
    private EventBusManager eventBusManager;

    @InjectMocks
    private SimplePresenter presenter;

    @InjectMocks
    private SimpleEventHandler eventHandler;

    @InjectMocks
    private MissingAnnotationPresenter missingAnnotationPresenter;

    @Before
    public void setUp() throws ViewFactoryException {

        when(applicationContext.isPrototype(anyString())).thenReturn(false);
        when(eventBusManager.create(any())).thenReturn(mock(SimpleEventBus.class));
    }

    @Test
    public void init_whenPresenterIsValid_thenViewAndEventBusSuccessfullyCreated() throws ViewFactoryException {

        when(viewFactory.canCreateView(any())).thenReturn(true);

        presenter.init();

        verify(eventBusManager).create(presenter);
        verify(viewFactory).create(any(), any());
    }

    @Test
    public void init_whenEventHandlerIsValid_thenEventBusSuccessfullyCreated() throws ViewFactoryException {

        eventHandler.init();

        verify(eventBusManager).create(eventHandler);
        verifyZeroInteractions(viewFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void init_whenPresenterAnnotationMissing_thenThrowsIllegalArgumentException() throws ViewFactoryException {

        missingAnnotationPresenter.init();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ViewFactoryException.class)
    public void init_whenViewFactoryFailsToCreateView_thenThrowsViewFactoryException() throws ViewFactoryException {

        when(viewFactory.canCreateView(any())).thenReturn(true);
        when(viewFactory.create(any(), any())).thenThrow(ViewFactoryException.class);

        presenter.init();
    }

    @Test
    public void init_whenViewFactoryCannotCreateView_thenViewNotCreated() throws ViewFactoryException {

        when(viewFactory.canCreateView(any())).thenReturn(false);

        presenter.init();

        assertNull(presenter.getView());
    }

    @Test
    public void destroy_whenEventHandlerIsRegistered_thenResourcesAreCleanedUp() throws ViewFactoryException {

        when(viewFactory.canCreateView(any())).thenReturn(true);
        when(viewFactory.create(any(), any())).thenReturn(new SimplePresenter.View());

        presenter.init();

        assertNotNull(presenter.getEventBus());
        assertNotNull(presenter.getView());

        presenter.destroy();

        verify(eventBusManager).unregister(presenter);
        assertNull(presenter.getEventBus());
        assertNull(presenter.getView());
    }

    @Test
    public void setView_whenPresenterIsPrototypeScoped_thenDetachListenerAutomaticallyAddedToView() {

        when(applicationContext.isPrototype(anyString())).thenReturn(true);

        SimplePresenter.View view = new SimplePresenter.View();
        presenter.setView(view);

        assertFalse(view.getListeners(DetachEvent.class).isEmpty());
    }

}
