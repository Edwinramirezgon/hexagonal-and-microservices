# Hexagonal & Microservices

Sistema de reservas basado en arquitectura hexagonal y microservicios. El proyecto está compuesto por los siguientes servicios:

- **auth-service** — Servicio de autenticación y autorización (JWT).
- **reservations-demo** — Servicio principal de gestión de reservas.
- **email-service** — Servicio de notificaciones por correo electrónico (Gmail).
- **frontend-service** — Interfaz web del sistema.

La infraestructura incluye SQL Server, Redis y la inicialización automática de la base de datos.

---

## Manual de Despliegue

### Requisitos previos

- Tener **Docker** y **Docker Compose** instalados.

### Pasos

1. Clonar el repositorio:

```bash
git clone https://github.com/Edwinramirezgon/hexagonal-and-microservices.git
cd hexagonal-and-microservices
```

2. Copiar el archivo de variables de entorno:

```bash
cp .env.example .env
```

3. Editar el archivo `.env` y configurar las variables de Gmail:

```env
GMAIL_USERNAME=tu-correo@gmail.com
GMAIL_APP_PASSWORD=tu-app-password-de-gmail
```

4. Levantar los servicios:

```bash
docker compose up -d --build
```

### Acceso

- **Frontend:** http://localhost:8083

