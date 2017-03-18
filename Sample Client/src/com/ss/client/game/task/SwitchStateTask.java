package com.ss.client.game.task;

import static javafx.application.Platform.runLater;
import com.jme3.app.state.AppStateManager;
import com.jme3x.jfx.JmeFxContainer;
import com.ss.client.SampleGame;
import com.ss.client.manager.ExecutorManager;
import com.ss.client.stage.Stage;
import com.ss.client.stage.StageType;
import com.ss.client.ui.scene.UIScene;
import com.ss.client.ui.scene.UISceneType;
import javafx.scene.Group;
import rlib.concurrent.util.ThreadUtils;
import rlib.logging.Logger;
import rlib.logging.LoggerManager;

/**
 * Реализация задачи по смене стадии игры.
 *
 * @author Ronn
 */
public class SwitchStateTask implements Runnable {

    private static final Logger LOGGER = LoggerManager.getLogger(SwitchStateTask.class);

    private static final ExecutorManager EXECUTOR_MANAGER = ExecutorManager.getInstance();

    /**
     * Целевая стадия.
     */
    private final StageType target;

    public SwitchStateTask(final StageType target) {
        this.target = target;
    }

    private void activateNewScene(final JmeFxContainer fxContainer, final UIScene currentScene, final Stage targetStage) {

        final UISceneType fxState = targetStage.getUISceneType();
        final UIScene scene = fxState.getScene();

        if (scene == null) {
            LOGGER.error("can't created scene from " + fxState);
            return;
        }

        scene.hideLoading();
        scene.notifyPreActivate();
        fxContainer.loseFocus();

        EXECUTOR_MANAGER.switchStateExecute(() -> {

            scene.waitLoading();

            runLater(() -> {

                fxContainer.setScene(scene, (Group) scene.getRoot());

                if (currentScene != null) {
                    currentScene.hideLoading();
                    currentScene.notifyPostDeactivate();
                }

                fxContainer.grabFocus();

                runLater(scene::notifyPostActivate);
            });
        });
    }

    private void deactivateCurrentScene(final SampleGame game, final JmeFxContainer fxContainer, final UIScene currentScene) {

        final AppStateManager stateManager = game.getStateManager();
        final StageType current = game.getCurrentStage();

        ThreadUtils.sleep(200);

        Stage currentStage = null;

        if (current != null) {
            currentStage = current.getStage();
        }

        if (currentScene != null) {
            currentScene.waitCleaning();
        }

        final StageType target = getTarget();
        final Stage targetStage = target.getStage();

        if (currentStage != null) {

            final long stamp = game.syncLock();
            try {
                stateManager.detach(currentStage);
            } finally {
                game.syncUnlock(stamp);
            }

            game.setPrevStage(current);
        }

        final long stamp = game.syncLock();
        try {
            stateManager.attach(targetStage);
        } finally {
            game.syncUnlock(stamp);
        }

        game.setCurrentStage(target);
        game.setNextStage(null);

        runLater(() -> activateNewScene(fxContainer, currentScene, targetStage));
    }

    /**
     * @return целевая стадия.
     */
    public StageType getTarget() {
        return target;
    }

    @Override
    public void run() {

        final SampleGame game = SampleGame.getInstance();
        final JmeFxContainer fxContainer = game.getFxContainer();

        final UIScene currentScene = (UIScene) fxContainer.getScene();

        if (currentScene != null) {
            currentScene.showLoading();
            currentScene.notifyPreDeactivate();
        }

        EXECUTOR_MANAGER.switchStateExecute(() -> deactivateCurrentScene(game, fxContainer, currentScene));
    }
}
