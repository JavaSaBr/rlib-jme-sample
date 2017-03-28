package com.ss.client.ui.component.login;

import static java.util.Objects.requireNonNull;
import com.ss.client.Messages;
import com.ss.client.model.impl.UserAccount;
import com.ss.client.network.client.AuthCredentialsClientPacket;
import com.ss.client.network.server.AuthResultServerPacket;
import com.ss.client.stage.StageType;
import com.ss.client.ui.component.impl.AbstractUIComponent;
import com.ss.client.ui.css.CSSClasses;
import com.ss.client.ui.css.CSSIds;
import com.ss.client.ui.event.impl.AuthResultEvent;
import com.ss.client.ui.scene.UIScene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rlib.ui.util.FXUtils;
import rlib.util.StringUtils;

/**
 * The implementation login authentication component.
 *
 * @author JavaSaBr
 */
public class LoginAuthPanel extends AbstractUIComponent {

    private static final String COMPONENT_ID = LoginAuthPanel.class.getName();

    /**
     * The username field.
     */
    @Nullable
    private TextField usernameField;

    /**
     * The password field.
     */
    @Nullable
    private PasswordField passwordField;

    /**
     * The message label.
     */
    @Nullable
    private Label messageLabel;

    public LoginAuthPanel() {
        FX_EVENT_MANAGER.addEventHandler(AuthResultEvent.EVENT_TYPE, event -> processEvent((AuthResultEvent) event));
    }

    private void processEvent(@NotNull final AuthResultEvent event) {

        final Label messageLabel = getMessageLabel();
        messageLabel.setText(StringUtils.EMPTY);

        final AuthResultServerPacket.ResultType resultType = event.getResultType();

        switch (resultType) {
            case INCORRECT_NAME: {
                messageLabel.setText("The name is incorrect.");
                break;
            }
            case INCORRECT_PASSWORD: {
                messageLabel.setText("The password is incorrect.");
                break;
            }
            case SUCCESSFUL: {
                break;
            }
        }

        final UIScene scene = (UIScene) getScene();
        scene.decrementWaiter();
        scene.hideLoading();

        if (resultType == AuthResultServerPacket.ResultType.SUCCESSFUL) {
            final UserAccount userAccount = GAME_CLIENT.getUserAccount();
            userAccount.setName(getUsernameField().getText());
            userAccount.setPassword(getPasswordField().getText());
            GAME_CLIENT.gotoStage(StageType.HANGAR_STAGE);
        }
    }

    @NotNull
    @Override
    protected String getCssId() {
        return CSSIds.LOGIN_AUTH_PANEL;
    }

    @Override
    public String getComponentId() {
        return COMPONENT_ID;
    }

    @Override
    protected void createComponentsAndControls() {
        super.createComponentsAndControls();

        final Label usernameLabel = new Label();
        usernameLabel.setText(Messages.LOGIN_AUTH_PANEL_USERNAME_FIELD + ":");

        usernameField = new TextField();

        final Label passwordLabel = new Label();
        passwordLabel.setText(Messages.LOGIN_AUTH_PANEL_PASSWORD_FIELD + ":");

        passwordField = new PasswordField();

        messageLabel = new Label();
        messageLabel.setId(CSSIds.LOGIN_AUTH_PANEL_MESSAGE_LABEL);
        messageLabel.prefWidthProperty().bind(widthProperty());

        final Button authenticationButton = new Button("Auth");
        authenticationButton.setOnAction(event -> tryAuth());

        final VBox container = new VBox(messageLabel, authenticationButton);
        container.setAlignment(Pos.CENTER);
        container.prefWidthProperty().bind(widthProperty());

        FXUtils.addClassTo(usernameLabel, CSSClasses.LOGIN_AUTH_PANEL_LABEL);
        FXUtils.addClassTo(usernameField, CSSClasses.LOGIN_AUTH_PANEL_FIELD);
        FXUtils.addClassTo(passwordLabel, CSSClasses.LOGIN_AUTH_PANEL_LABEL);
        FXUtils.addClassTo(passwordField, CSSClasses.LOGIN_AUTH_PANEL_FIELD);
        FXUtils.addClassTo(authenticationButton, CSSClasses.BUTTON_SIZE_1);

        add(usernameLabel, 0, 0);
        add(usernameField, 1, 0);
        add(passwordLabel, 0, 1);
        add(passwordField, 1, 1);
        add(container, 0, 2, 2, 1);
    }

    /**
     * @return the username field.
     */
    @NotNull
    private TextField getUsernameField() {
        return requireNonNull(usernameField);
    }

    /**
     * @return the password field.
     */
    @NotNull
    private PasswordField getPasswordField() {
        return requireNonNull(passwordField);
    }

    /**
     * @return the message label.
     */
    @NotNull
    private Label getMessageLabel() {
        return requireNonNull(messageLabel);
    }

    /**
     * Try to auth.
     */
    private void tryAuth() {

        final Label messageLabel = getMessageLabel();
        messageLabel.setText(StringUtils.EMPTY);

        final TextField usernameField = getUsernameField();
        final PasswordField passwordField = getPasswordField();

        final String name = usernameField.getText();
        final String password = passwordField.getText();

        if (StringUtils.isEmpty(name) || name.length() < 4) {
            messageLabel.setText("The name is too short.");
            return;
        } else if (StringUtils.isEmpty(password) || password.length() < 4) {
            messageLabel.setText("The password is too short.");
            return;
        } else if (!NETWORK.isGameConnected()) {
            messageLabel.setText("You have a problem with connection to the game server.");
            return;
        }

        final String hash = StringUtils.toMD5(password);

        final UIScene scene = (UIScene) getScene();
        scene.incrementWaiter();
        scene.showLoading();

        NETWORK.sendPacketToGameServer(AuthCredentialsClientPacket.getInstance(name, hash));
    }
}
