package com.demo.frontend.infrastructure.adapter.in;

import com.demo.frontend.application.port.in.AuthUseCase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("")
@PageTitle("Acceso — Sistema de Reservas")
public class LoginView extends VerticalLayout {

    private final AuthUseCase authUseCase;

    public LoginView(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("min-height", "100vh");

        VerticalLayout card = new VerticalLayout();
        card.setWidth("420px");
        card.getStyle()
            .set("background", "white")
            .set("border-radius", "16px")
            .set("box-shadow", "0 20px 60px rgba(0,0,0,0.2)")
            .set("padding", "40px")
            .set("gap", "0");

        H2 title = new H2("Sistema de Reservas");
        title.getStyle()
            .set("margin", "0 0 8px 0")
            .set("color", "#1a1a2e")
            .set("font-size", "1.6rem")
            .set("text-align", "center");

        Paragraph subtitle = new Paragraph("Gestiona tus salas de forma eficiente");
        subtitle.getStyle()
            .set("color", "#6b7280")
            .set("text-align", "center")
            .set("margin", "0 0 28px 0")
            .set("font-size", "0.9rem");

        Tab loginTab    = new Tab("Iniciar sesión");
        Tab registerTab = new Tab("Registrarse");
        Tabs tabs       = new Tabs(loginTab, registerTab);
        tabs.setWidthFull();
        tabs.getStyle().set("margin-bottom", "24px");

        VerticalLayout loginForm    = buildLoginForm();
        VerticalLayout registerForm = buildRegisterForm();
        registerForm.setVisible(false);

        tabs.addSelectedChangeListener(e -> {
            loginForm.setVisible(tabs.getSelectedTab() == loginTab);
            registerForm.setVisible(tabs.getSelectedTab() == registerTab);
        });

        card.add(title, subtitle, tabs, loginForm, registerForm);
        add(card);
    }

    private VerticalLayout buildLoginForm() {
        TextField     username = styledField("Usuario");
        PasswordField password = styledPassword("Contraseña");

        Button submit = new Button("Iniciar sesión");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        submit.setWidthFull();
        submit.getStyle().set("background", "linear-gradient(135deg, #667eea, #764ba2)").set("border", "none");

        submit.addClickListener(e -> {
            try {
                AuthUseCase.AuthResult result = authUseCase.login(username.getValue(), password.getValue());
                VaadinSession.getCurrent().setAttribute("token",    result.token());
                VaadinSession.getCurrent().setAttribute("username", result.username());
                VaadinSession.getCurrent().setAttribute("email",    result.email());
                VaadinSession.getCurrent().setAttribute("role",     result.role());
                if ("ADMIN".equals(result.role())) {
                    submit.getUI().ifPresent(ui -> ui.navigate(AdminView.class));
                } else {
                    submit.getUI().ifPresent(ui -> ui.navigate(ReservationsView.class));
                }
            } catch (Exception ex) {
                showError("Credenciales inválidas.");
            }
        });

        VerticalLayout form = new VerticalLayout(username, password, submit);
        form.setPadding(false);
        form.setSpacing(true);
        return form;
    }

    private VerticalLayout buildRegisterForm() {
        TextField     username = styledField("Usuario");
        TextField     email    = styledField("Email");
        PasswordField password = styledPassword("Contraseña");

        Button submit = new Button("Crear cuenta");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        submit.setWidthFull();
        submit.getStyle().set("background", "linear-gradient(135deg, #667eea, #764ba2)").set("border", "none");

        submit.addClickListener(e -> {
            try {
                authUseCase.register(username.getValue(), email.getValue(), password.getValue());
                Notification n = Notification.show("¡Cuenta creada! Revisa tu email y luego inicia sesión.");
                n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                n.setDuration(4000);
                username.clear(); email.clear(); password.clear();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        VerticalLayout form = new VerticalLayout(username, email, password, submit);
        form.setPadding(false);
        form.setSpacing(true);
        return form;
    }

    private TextField styledField(String label) {
        TextField f = new TextField(label);
        f.setWidthFull();
        return f;
    }

    private PasswordField styledPassword(String label) {
        PasswordField f = new PasswordField(label);
        f.setWidthFull();
        return f;
    }

    private void showError(String msg) {
        Notification n = Notification.show(msg);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
        n.setDuration(3000);
    }
}
