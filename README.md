# Furni-Style
Laboratory work 2nd year, 4th semester

Development stack
- Backend: Java, Spring Boot, SQLite
- Frontend: HTML, CSS, JS

A small internal system for processing furniture store orders.

## What’s implemented:
- The application is built using a layered architecture (Controller → Service → Repository) with clear separation of responsibilities.
- Business logic for orders (statuses, validations, stock updates) is implemented in the service layer.
- Data is stored in SQLite, accessed via JDBC and PreparedStatement.
- Synchronization between in-memory objects and the database is implemented when orders are updated.
- Global exception handling is implemented using ExceptionHandler.

----------------------
Лабораторная работа 2 курс 4 семестр

Стек разработки
- Бекенд: Java, Spring Boot, SQLite
- Фронтенд: HTML, CSS, JS

Небольшая внутренняя система для оформления заказов мебельного магазина.

## Что реализованно:
- Приложение построено по архитектуре Controller → Service → Repository с чётким разделением ответственности.
- Бизнес-логика заказов (статусы, проверки, изменение остатков) реализована в сервисном слое.
- Данные хранятся в SQLite, доступ к ним осуществляется через JDBC и PreparedStatement.
- Реализована синхронизация между объектами в памяти и базой данных при изменении заказов.
- Добавлена обработка ошибок через глобальный ExceptionHandler.
