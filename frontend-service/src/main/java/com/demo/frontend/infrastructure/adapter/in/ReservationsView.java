package com.demo.frontend.infrastructure.adapter.in;

import com.demo.frontend.application.port.in.AuthUseCase;
import com.demo.frontend.application.port.in.ReservationUseCase;
import com.demo.frontend.application.port.in.dto.ReservationDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("reservations")
@PageTitle("Reservas")
public class ReservationsView extends VerticalLayout implements BeforeEnterObserver {

    private final ReservationUseCase   reservationUseCase;
    private final AuthUseCase          authUseCase;
    private final Grid<ReservationDto> grid      = new Grid<>(ReservationDto.class, false);
    private final DateTimeFormatter    fmt       = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private Div    totalCard;
    private String token;

    public ReservationsView(ReservationUseCase reservationUseCase, AuthUseCase authUseCase) {
        this.reservationUseCase = reservationUseCase;
        this.authUseCase        = authUseCase;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().set("background", "#f8fafc");
        add(buildTopBar(), buildContent());
    }

    private HorizontalLayout buildTopBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidthFull();
        bar.setAlignItems(FlexComponent.Alignment.CENTER);
        bar.getStyle()
            .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            .set("padding", "16px 32px")
            .set("box-shadow", "0 2px 8px rgba(0,0,0,0.15)");

        H3 title = new H3("\uD83C\uDFE2 Sistema de Reservas");
        title.getStyle().set("color", "white").set("margin", "0");

        Span userBadge = new Span();
        userBadge.getStyle().set("color", "rgba(255,255,255,0.9)").set("font-size", "0.9rem");

        Button logoutBtn = new Button("Cerrar sesi\u00f3n", new Icon(VaadinIcon.SIGN_OUT));
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logoutBtn.getStyle().set("color", "white");
        logoutBtn.addClickListener(e -> {
            String t = (String) VaadinSession.getCurrent().getAttribute("token");
            if (t != null) authUseCase.logout(t);
            VaadinSession.getCurrent().close();
            logoutBtn.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });

        HorizontalLayout right = new HorizontalLayout(userBadge, logoutBtn);
        right.setAlignItems(FlexComponent.Alignment.CENTER);
        bar.add(title, right);
        bar.expand(title);

        bar.addAttachListener(ev -> {
            String username = (String) VaadinSession.getCurrent().getAttribute("username");
            userBadge.setText("\uD83D\uDC64 " + (username != null ? username : ""));
        });

        return bar;
    }

    private VerticalLayout buildContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.getStyle().set("padding", "32px");

        totalCard = statCard("Total Reservas", "0", "#667eea");

        HorizontalLayout stats = new HorizontalLayout(totalCard);
        stats.setWidthFull();

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        toolbar.setAlignItems(FlexComponent.Alignment.CENTER);
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        toolbar.getStyle().set("margin", "24px 0 16px 0");

        H4 sectionTitle = new H4("Mis Reservas");
        sectionTitle.getStyle().set("margin", "0").set("color", "#1a1a2e");

        Button newBtn = new Button("Nueva Reserva", new Icon(VaadinIcon.PLUS));
        newBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newBtn.getStyle().set("background", "linear-gradient(135deg, #667eea, #764ba2)").set("border", "none");
        newBtn.addClickListener(e -> openForm(null));

        Button subscribeBtn = new Button("\uD83D\uDCC5 Suscribir Calendario", new Icon(VaadinIcon.CALENDAR));
        subscribeBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        subscribeBtn.addClickListener(e -> showSubscribeDialog());

        toolbar.add(sectionTitle, new HorizontalLayout(subscribeBtn, newBtn));

        configureGrid();

        Div gridCard = new Div(grid);
        gridCard.setWidthFull();
        gridCard.getStyle()
            .set("background", "white").set("border-radius", "12px")
            .set("box-shadow", "0 2px 12px rgba(0,0,0,0.08)").set("overflow", "hidden");

        content.add(stats, toolbar, gridCard);
        content.expand(gridCard);
        return content;
    }

    private void configureGrid() {
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setHeightFull();

        grid.addColumn(ReservationDto::getId).setHeader("ID").setWidth("80px").setFlexGrow(0);
        grid.addColumn(ReservationDto::getRoomId).setHeader("Sala").setFlexGrow(1);
        grid.addColumn(ReservationDto::getAttendee).setHeader("Asistente").setFlexGrow(1);
        grid.addColumn(r -> r.getStartTime() != null ? r.getStartTime().format(fmt) : "").setHeader("Inicio").setFlexGrow(1);
        grid.addColumn(r -> r.getEndTime()   != null ? r.getEndTime().format(fmt)   : "").setHeader("Fin").setFlexGrow(1);
        grid.addComponentColumn(dto -> {
            Button edit     = new Button(new Icon(VaadinIcon.EDIT));
            Button delete   = new Button(new Icon(VaadinIcon.TRASH));
            Button download = new Button(new Icon(VaadinIcon.DOWNLOAD));
            edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            download.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            edit.addClickListener(e -> openForm(dto));
            delete.addClickListener(e -> deleteReservation(dto.getId()));
            download.addClickListener(e -> downloadIcs(dto.getId()));
            HorizontalLayout actions = new HorizontalLayout(edit, delete, download);
            actions.setSpacing(false);
            return actions;
        }).setHeader("Acciones").setWidth("160px").setFlexGrow(0);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        token = (String) VaadinSession.getCurrent().getAttribute("token");
        if (token == null) { event.rerouteTo(LoginView.class); return; }
        refresh();
    }

    private void refresh() {
        try {
            List<ReservationDto> list = reservationUseCase.findAll(token);
            grid.setItems(list);
            totalCard.getChildren().filter(c -> c instanceof Span).map(c -> (Span) c)
                .reduce((first, second) -> second)
                .ifPresent(s -> s.setText(String.valueOf(list.size())));
        } catch (Exception e) {
            showError("Error al cargar reservas.");
        }
    }

    private void deleteReservation(Long id) {
        try {
            reservationUseCase.delete(id, token);
            refresh();
            showSuccess("Reserva eliminada correctamente.");
        } catch (Exception e) {
            showError("Error al eliminar la reserva.");
        }
    }

    private void openForm(ReservationDto existing) {
        Dialog dialog = new Dialog();
        dialog.setWidth("480px");

        H3 dialogTitle = new H3(existing == null ? "Nueva Reserva" : "Editar Reserva");
        dialogTitle.getStyle().set("margin", "0 0 20px 0").set("color", "#1a1a2e");

        TextField      roomId   = new TextField("Sala");
        TextField      attendee = new TextField("Asistente");
        DateTimePicker start    = new DateTimePicker("Fecha de inicio");
        DateTimePicker end      = new DateTimePicker("Fecha de fin");
        roomId.setWidthFull(); attendee.setWidthFull();
        start.setWidthFull();  end.setWidthFull();

        if (existing != null) {
            roomId.setValue(existing.getRoomId());
            attendee.setValue(existing.getAttendee());
            if (existing.getStartTime() != null) start.setValue(existing.getStartTime());
            if (existing.getEndTime()   != null) end.setValue(existing.getEndTime());
        }

        Button save   = new Button(existing == null ? "Crear reserva" : "Guardar cambios");
        Button cancel = new Button("Cancelar", e -> dialog.close());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.getStyle().set("background", "linear-gradient(135deg, #667eea, #764ba2)").set("border", "none");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(e -> {
            ReservationDto dto = new ReservationDto();
            dto.setRoomId(roomId.getValue());
            dto.setAttendee(attendee.getValue());
            dto.setStartTime(start.getValue());
            dto.setEndTime(end.getValue());
            dto.setCreatedBy((String) VaadinSession.getCurrent().getAttribute("username"));
            try {
                if (existing == null) {
                    reservationUseCase.create(dto, token);
                } else {
                    reservationUseCase.update(existing.getId(), dto, token);
                }
                dialog.close();
                refresh();
                showSuccess(existing == null ? "Reserva creada. Se envi\u00f3 confirmaci\u00f3n a tu email." : "Reserva actualizada.");
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttons.getStyle().set("margin-top", "16px");

        dialog.add(new VerticalLayout(dialogTitle, roomId, attendee, start, end, buttons));
        dialog.open();
    }

    private void downloadIcs(Long id) {
        try {
            byte[] ics = reservationUseCase.downloadIcs(id, token);
            StreamResource resource = new StreamResource("reserva-" + id + ".ics", () -> new ByteArrayInputStream(ics));
            resource.setContentType("text/calendar");
            StreamRegistration reg = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
            getUI().ifPresent(ui -> ui.getPage().open(reg.getResourceUri().toString()));
        } catch (Exception e) {
            showError("Error al descargar el archivo .ics");
        }
    }

    private void showSubscribeDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("480px");

        H3 title = new H3("\uD83D\uDCC5 Suscribir en Outlook");
        title.getStyle().set("margin", "0 0 12px 0").set("color", "#1a1a2e");

        String icsUrl       = "http://localhost:8083/ical/reservations.ics";
        String msOutlookUrl = "ms-outlook://calendars/subscribe?url="
                              + URLEncoder.encode(icsUrl, StandardCharsets.UTF_8);

        Paragraph info = new Paragraph(
            "Haz clic en el bot\u00f3n para abrir Outlook Desktop y suscribirte. " +
            "Outlook debe estar instalado y configurado como app predeterminada de calendario.");
        info.getStyle().set("color", "#6b7280").set("font-size", "0.88rem").set("margin", "0 0 12px 0");

        Button openOutlook = new Button("\uD83D\uDCE7 Abrir en Outlook Desktop", new Icon(VaadinIcon.CALENDAR));
        openOutlook.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        openOutlook.setWidthFull();
        openOutlook.getStyle().set("background", "#0078d4").set("border", "none");
        openOutlook.addClickListener(e -> getUI().ifPresent(ui ->
            ui.getPage().executeJs("window.location.href = $0;", msOutlookUrl)));

        Hr sep = new Hr();
        sep.getStyle().set("margin", "14px 0");

        Paragraph fallback = new Paragraph("Si no se abre autom\u00e1ticamente:");
        fallback.getStyle().set("font-weight", "700").set("margin", "0 0 6px 0").set("font-size", "0.9rem");

        Button downloadAll = new Button("\u2B07 Descargar .ics e importar", new Icon(VaadinIcon.DOWNLOAD));
        downloadAll.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        downloadAll.setWidthFull();
        downloadAll.addClickListener(e -> {
            try {
                byte[] ics = reservationUseCase.downloadAllIcs();
                StreamResource res = new StreamResource("reservas.ics", () -> new ByteArrayInputStream(ics));
                res.setContentType("text/calendar");
                StreamRegistration reg = VaadinSession.getCurrent().getResourceRegistry().registerResource(res);
                getUI().ifPresent(ui -> ui.getPage().open(reg.getResourceUri().toString()));
            } catch (Exception ex) {
                showError("Error al descargar el archivo .ics");
            }
        });

        Paragraph urlPara = new Paragraph(icsUrl);
        urlPara.getStyle()
            .set("background", "#f3f4f6").set("padding", "8px 12px").set("border-radius", "6px")
            .set("font-family", "monospace").set("font-size", "0.78rem")
            .set("word-break", "break-all").set("margin", "8px 0 4px 0");

        Button copyUrl = new Button("Copiar URL", new Icon(VaadinIcon.COPY));
        copyUrl.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_SMALL);
        copyUrl.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.getPage().executeJs("navigator.clipboard.writeText($0)", icsUrl));
            showSuccess("URL copiada.");
        });

        Button close = new Button("Cerrar", ev -> dialog.close());
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        close.getStyle().set("margin-top", "8px");

        VerticalLayout content = new VerticalLayout(
            title, info, openOutlook, sep, fallback, downloadAll, urlPara, copyUrl, close);
        content.setPadding(true);
        content.setSpacing(false);
        content.getStyle().set("gap", "4px");
        dialog.add(content);
        dialog.open();
    }

    private Div statCard(String label, String value, String color) {
        Div card = new Div();
        card.getStyle()
            .set("background", "white").set("border-radius", "12px")
            .set("padding", "20px 24px").set("box-shadow", "0 2px 12px rgba(0,0,0,0.08)")
            .set("border-left", "4px solid " + color).set("min-width", "180px");
        Span lbl = new Span(label);
        lbl.getStyle().set("color", "#6b7280").set("font-size", "0.85rem").set("display", "block");
        Span val = new Span(value);
        val.getStyle().set("color", "#1a1a2e").set("font-size", "1.8rem").set("font-weight", "700").set("display", "block");
        card.add(lbl, val);
        return card;
    }

    private void showSuccess(String msg) {
        Notification n = Notification.show(msg, 3000, Notification.Position.BOTTOM_END);
        n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void showError(String msg) {
        Notification n = Notification.show(msg, 3000, Notification.Position.BOTTOM_END);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
