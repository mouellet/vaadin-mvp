package org.vaadin.mvp.presenter.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.vaadin.mvp.test.Util.SimpleEventBus.SimplePresenter;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPresenterFactoryTest {

    @Mock
    private ApplicationContext applicationContext;

    @Spy
    @InjectMocks
    private DefaultPresenterFactory presenterFactory;

    @Test
    public void create_whenPresenterIsDefinedAsBean_thenReturnsPresenterBean() {

        SimplePresenter presenter = new SimplePresenter();
        when(applicationContext.getBean(SimplePresenter.class)).thenReturn(presenter);

        SimplePresenter result = (SimplePresenter) presenterFactory.create(SimplePresenter.class);

        assertNotNull(result);
        assertTrue(result.equals(presenter));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void create_whenPresenterNotDefinedAsBean_thenThrowsIllegalArgumentException() {

        when(applicationContext.getBean(SimplePresenter.class)).thenThrow(BeanCreationException.class);

        presenterFactory.create(SimplePresenter.class);
    }

    @Test
    public void create_whenPresenterIsDefinedAsPrototypeScopedBean_thenReturnsNewPresenterInstance() {

        when(applicationContext.isPrototype("simplePresenter")).thenReturn(true);
        when(applicationContext.getBean(SimplePresenter.class))
                .thenReturn(new SimplePresenter())
                .thenReturn(new SimplePresenter());

        SimplePresenter firstInstance = (SimplePresenter) presenterFactory.create(SimplePresenter.class);
        SimplePresenter secondInstance = (SimplePresenter) presenterFactory.create(SimplePresenter.class);

        assertNotNull(firstInstance);
        assertNotNull(secondInstance);
        assertFalse(firstInstance.equals(secondInstance));
    }

    @Test
    public void createAndBind_whenWithoutBindingArguements_thenPresenterIsCreatedAndBound() {

        SimplePresenter presenter = spy(SimplePresenter.class);
        when(applicationContext.getBean(SimplePresenter.class)).thenReturn(presenter);

        presenterFactory.createAndBind(SimplePresenter.class);

        verify(presenterFactory).create(SimplePresenter.class);
        verify(presenter).bind();
    }

    @Test
    public void createAndBind_whenWithBindingArguements_thenPresenterIsCreatedAndBound() {

        Object args = new Object();
        SimplePresenter presenter = spy(SimplePresenter.class);
        when(applicationContext.getBean(SimplePresenter.class)).thenReturn(presenter);

        presenterFactory.createAndBind(SimplePresenter.class, args);

        verify(presenterFactory).create(SimplePresenter.class);
        verify(presenter).bind(args);
    }

    /*
    @Presenter(view = SimplePresenterView.class)
    static class SimplePresenter extends AbstractPresenter<SimplePresenterView, SimplePresenterEventBus> {
    
        static class SimplePresenterView extends CssLayout {
        }
    
        @Events
        static interface SimplePresenterEventBus extends EventBus {
        }
    }
    */

}
