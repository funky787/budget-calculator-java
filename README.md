# Калькулятор бюджета — тестирование REST API

Проект на Java 21, Spring Boot, JUnit 5 и MockMvc. Используется Maven.

## Что делает API

POST `/api/budget/calculate` рассчитывает стоимость поездки. При включенной страховке добавляется 5%.

Пример запроса:

```json
{
  "dailyCost": 100,
  "days": 5,
  "includeInsurance": true
}
```

Ответ (200 OK):

```json
{
  "totalAmount": 525.00
}
```

Для days <= 0 возвращается 400 Bad Request.

## Запуск в IntelliJ IDEA

1. Открыть папку проекта, дождаться загрузки Maven-зависимостей.
2. Указать JDK 21 в Project SDK.
3. Запустить `BudgetCalculatorApplication` для старта сервера.
4. Для запуска всех тестов выполнить `mvn test` в терминале IntelliJ.

## Тесты и покрытие

Команды из корня проекта:

```powershell
mvn test
mvn clean verify
```

После `mvn clean verify` отчёт JaCoCo появится в `target/site/jacoco/index.html`.

Шесть тестов проверяют: обычный расчёт, страховку +5%, отрицательные дни, нулевую стоимость, дробную стоимость и нулевые дни.

## Как демонстрировать найденный баг

В `BudgetController.java` временно заменить:

```java
total = total.multiply(new BigDecimal("1.05"));
```

на ошибочный вариант:

```java
total = total.multiply(new BigDecimal("5"));
```

Запустить `mvn test`: тест `calculateWithInsurance` должен упасть, поскольку вместо 525 возвращается 2500. Вернуть `1.05` и запустить тесты повторно.

Тесты изолируют веб-слой с помощью `@WebMvcTest` и `MockMvc`: настоящий сервер и база данных для них не запускаются.
