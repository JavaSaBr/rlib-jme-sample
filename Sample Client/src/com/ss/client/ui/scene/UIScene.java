package com.ss.client.ui.scene;

import static javafx.geometry.Pos.CENTER;
import static javafx.util.Duration.millis;
import com.ss.client.ui.FullScreenComponentSupport;
import com.ss.client.ui.HasMenu;
import com.ss.client.ui.component.UIComponent;
import com.ss.client.ui.util.UIUtils;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.concurrent.atomic.AtomicInteger;
import rlib.concurrent.util.ConcurrentUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;
import rlib.ui.util.FXUtils;
import rlib.util.StringUtils;
import rlib.util.array.Array;
import rlib.util.array.ArrayFactory;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The implementation of UI scene in the game.
 *
 * @author JavaSaBr
 */
public class UIScene extends Scene implements FullScreenComponentSupport {

    protected static final Logger LOGGER = LoggerManager.getLogger(UIScene.class);

    private static final int PROP_ANIMATION_DURATION = 400;

    /**
     * Обработчики при активации.
     */
    @NotNull
    private final Array<Runnable> activateHandlers;

    /**
     * Обработчики при деактивации.
     */
    @NotNull
    private final Array<Runnable> deactivateHandlers;

    /**
     * Список компонентов в сцене.
     */
    @NotNull
    private final Array<UIComponent> components;

    /**
     * Активна ли сейчас сцена.
     */
    @NotNull
    private final AtomicBoolean active;

    /**
     * Контейнер элементов сцены.
     */
    @NotNull
    private final StackPane container;

    /**
     * Слой для размещения индикатора загрузки.
     */
    @NotNull
    private final VBox loadingLayer;

    /**
     * Ссылка на индиктаор загрузки.
     */
    @NotNull
    private final AtomicReference<ProgressIndicator> indicatorReference;

    /**
     * Кол-во ожидающих загрузки данных.
     */
    @NotNull
    private final AtomicInteger waiterCount;

    /**
     * Анимация показа загрузки.
     */
    @Nullable
    private FadeTransition showLoadingAnimation;

    /**
     * Анимация скрытия загрузки.
     */
    @Nullable
    private FadeTransition hideLoadingAnimation;

    /**
     * Текущий показанный полноэкранный компонент.
     */
    private UIComponent fullscreenComponent;

    public UIScene(final Group root) {
        super(root);

        this.components = ArrayFactory.newArray(UIComponent.class);
        this.active = new AtomicBoolean();
        this.indicatorReference = new AtomicReference<>();
        this.activateHandlers = ArrayFactory.newArray(Runnable.class);
        this.deactivateHandlers = ArrayFactory.newArray(Runnable.class);
        this.container = new StackPane();
        this.waiterCount = new AtomicInteger();

        setOnKeyReleased(this::processEvent);
        setOnKeyPressed(this::processEvent);

        root.getChildren().add(container);

        FXUtils.bindFixedWidth(container, widthProperty());
        FXUtils.bindFixedHeight(container, heightProperty());

        this.loadingLayer = createLoadingLayer();

        createHideAnimation();
        createShowAnimation();
    }

    /**
     * Создание анимации показа загрузки.
     */
    protected void createShowAnimation() {
        showLoadingAnimation = new FadeTransition();
        showLoadingAnimation.setNode(loadingLayer);
        showLoadingAnimation.setDuration(millis(PROP_ANIMATION_DURATION));
        showLoadingAnimation.setFromValue(0);
        showLoadingAnimation.setToValue(1);
    }

    /**
     * Создание анимации скрытия загрузки.
     */
    protected void createHideAnimation() {
        hideLoadingAnimation = new FadeTransition();
        hideLoadingAnimation.setNode(loadingLayer);
        hideLoadingAnimation.setDuration(millis(PROP_ANIMATION_DURATION));
        hideLoadingAnimation.setFromValue(1);
        hideLoadingAnimation.setToValue(0);
        hideLoadingAnimation.setOnFinished(event -> hideLoadingImpl());
    }

    /**
     * Реализация процесса скрытия загрузки.
     */
    private void hideLoadingImpl() {

        final VBox loadingLayer = getLoadingLayer();
        loadingLayer.setVisible(false);

        final AtomicReference<ProgressIndicator> reference = getIndicatorReference();

        synchronized (reference) {

            final ProgressIndicator indicator = reference.get();

            if (indicator == null) {
                return;
            }

            FXUtils.removeFromParent(indicator, loadingLayer);

            reference.set(null);
        }
    }

    /**
     * Добавление обработчика при активации сцены.
     */
    public void addActivateHandler(final Runnable handler) {
        activateHandlers.add(handler);
    }

    /**
     * Добавление обработчика при деактивации сцены.
     */
    public void addDeactivateHandler(final Runnable handler) {
        deactivateHandlers.add(handler);
    }

    /**
     * Создание слоя для размещения индикатора.
     */
    protected VBox createLoadingLayer() {

        final VBox loadingLayer = new VBox();
        loadingLayer.setAlignment(CENTER);
        loadingLayer.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");
        loadingLayer.setVisible(false);

        FXUtils.addToPane(loadingLayer, container);

        return loadingLayer;
    }

    /**
     * @return новый индикатор загрузки.
     */
    protected ProgressIndicator createProgressIndicator() {
        return new ProgressIndicator();
    }

    /**
     * Уведомить о завершении ожидании загрузки данных.
     */
    public void decrementWaiter() {
        synchronized (waiterCount) {

            if (waiterCount.get() == 0) {
                return;
            } else if (waiterCount.decrementAndGet() != 0) {
                return;
            }

            ConcurrentUtils.notifyAll(waiterCount);
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * Поиск интересуемого компонента через его ид.
     *
     * @param id ид интересуемого компонента.
     * @return искомый компонент либо <code>null</code>.
     */
    public <T extends UIComponent> T findComponent(final String id) {

        final Array<UIComponent> components = getComponents();

        for (final UIComponent component : components.array()) {

            if (component == null) {
                break;
            } else if (StringUtils.equals(id, component.getComponentId())) {
                return (T) component;
            }
        }

        return null;
    }

    /**
     * @return обработчики при активации.
     */
    private Array<Runnable> getActivateHandlers() {
        return activateHandlers;
    }

    /**
     * @return список компонентов в сцене.
     */
    public Array<UIComponent> getComponents() {
        return components;
    }

    /**
     * @return контейнер элементов сцены.
     */
    public StackPane getContainer() {
        return container;
    }

    /**
     * @return обработчики при деактивации.
     */
    private Array<Runnable> getDeactivateHandlers() {
        return deactivateHandlers;
    }

    /**
     * @return текущий показанный полноэкранный компонент.
     */
    protected UIComponent getFullscreenComponent() {
        return fullscreenComponent;
    }

    /**
     * @return ссылка на индиктаор загрузки.
     */
    protected AtomicReference<ProgressIndicator> getIndicatorReference() {
        return indicatorReference;
    }

    /**
     * @return слой для размещения индикатора загрузки.
     */
    private VBox getLoadingLayer() {
        return loadingLayer;
    }

    /**
     * Скрыть индикатор загрузки.
     */
    public void hideLoading() {

        final VBox loadingLayer = getLoadingLayer();

        if(!loadingLayer.isVisible()) {
            return;
        }

        hideLoadingAnimation.play();

        final StackPane container = getContainer();

        final ObservableList<Node> children = container.getChildren();
        children.stream().filter(child -> child != loadingLayer).forEach(child -> child.setDisable(false));
    }

    /**
     * Добавить ожидающего загрузки данных.
     */
    public void incrementWaiter() {
        synchronized (waiterCount) {
            waiterCount.incrementAndGet();
        }
    }

    /**
     * @return активная ли сейчас сцена.
     */
    public boolean isActive() {
        return active.get();
    }

    /**
     * @param active активная ли сейчас сцена.
     */
    protected void setActive(final boolean active) {
        this.active.getAndSet(active);
    }

    public void notifyFinishBuild() {
        UIUtils.fillComponents(getComponents(), getContainer());
        loadingLayer.toFront();
    }

    /**
     * Вызов после активации сцены.
     */
    public void notifyPostActivate() {

        for (final UIComponent component : getComponents()) {
            try {
                component.notifyPostActivate();
            } catch (final Throwable e) {
                LOGGER.warning(this, e);
            }
        }

        setActive(true);

        ConcurrentUtils.notifyAll(this);
    }

    /**
     * Вызов после деактивации.
     */
    public void notifyPostDeactivate() {
        setActive(false);

        for (final UIComponent component : getComponents()) {
            component.notifyPostDeactivate();
        }
    }

    /**
     * Вызов перед активацией сцены.
     */
    public void notifyPreActivate() {

        final Array<Runnable> activateHandlers = getActivateHandlers();

        for (final Runnable handler : activateHandlers) {

            if (handler == null) {
                break;
            }

            handler.run();
        }

        for (final UIComponent component : getComponents()) {
            component.notifyPreActivate();
        }
    }

    /**
     * Вызов перед деактивации.
     */
    public void notifyPreDeactivate() {

        for (final UIComponent component : getComponents()) {
            component.notifyPreDeactivate();
        }

        final Array<Runnable> deactivateHandlers = getDeactivateHandlers();

        for (final Runnable handler : deactivateHandlers) {

            if (handler == null) {
                break;
            }

            handler.run();
        }
    }

    @Override
    public void onHide(final UIComponent component) {
        if (component == fullscreenComponent) {
            this.fullscreenComponent = null;
        }
    }

    @Override
    public void onShow(final UIComponent component) {
        this.fullscreenComponent = component;
    }

    /**
     * Обработка нажатий кнопок в рамкха сцены.
     */
    protected void processEvent(final KeyEvent event) {

        final KeyCode code = event.getCode();

        if (code == KeyCode.ESCAPE) {

            if (!(this instanceof HasMenu)) {
                return;
            }

            final HasMenu hasMenu = (HasMenu) this;

            if (!hasMenu.isMenuShowed()) {
                hasMenu.showMenu();
            } else {
                hasMenu.hideMenu();
            }
        }

        for (final UIComponent component : getComponents()) {
            component.notifyKeyReleased(event);
        }
    }

    /**
     * Отображение индикатора загрузки.
     */
    public void showLoading() {

        final VBox loadingLayer = getLoadingLayer();

        if(loadingLayer.isVisible()) {
            return;
        }

        loadingLayer.setVisible(true);
        showLoadingAnimation.play();

        final AtomicReference<ProgressIndicator> reference = getIndicatorReference();

        synchronized (reference) {

            if (reference.get() != null) {
                return;
            }

            final ProgressIndicator indicator = createProgressIndicator();

            FXUtils.setFixedSize(indicator, new Point(90, 90));
            FXUtils.addToPane(indicator, loadingLayer);

            reference.set(indicator);
        }

        final StackPane container = getContainer();

        final ObservableList<Node> children = container.getChildren();
        children.stream().filter(child -> child != loadingLayer).forEach(child -> child.setDisable(true));
    }

    /**
     * Ожидание очистки текущей сцены.
     */
    public void waitCleaning() {
    }

    /**
     * Ожидание загрузки новой сцены.
     */
    public void waitLoading() {
        synchronized (waiterCount) {
            if (waiterCount.get() != 0) {
                ConcurrentUtils.waitInSynchronize(waiterCount);
            }
        }
    }
}
