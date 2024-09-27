#### Тестируемый объект: https://github.com/sun6r0/test-service

#### Стек проекта:
- Java 17
- JUnit 5
- Maven
- REST Assured
- Allure
- CI/CD: GitHub Actions

### 1. Создание сущности
#### Шаги:
1. Отправить POST-запрос на http://localhost:8080/api/create .
2. Проверить код состояния.
3. Проверить id из responce.

#### Ожидаемый результат:
1. Статус 200.
2. Вернувшийся id больше 0.

### 2. Удаление сущности
#### Шаги:
1. Создать сущность
2. Отправить DELETE-запрос на http://localhost:8080/api/delete/{id} , в параметре id указать идентификатор удаляемой сущности.
3. Проверить код состояния.

#### Ожидаемый результат:
1. Статус 204.

### 3. Получение одной сущности
#### Шаги:
1. Создать сущность.
2. Отправить GET-запрос на http://localhost:8080/api/get/{id} , в параметре id указать идентификатор сущности.
3. Проверить код состояния.

#### Ожидаемый результат:
1. Статус 200.

### 4. Получение списка сущностей
#### Шаги:
1. Создать две сущности.
2. Отправить GET-запрос на http://localhost:8080/api/getAll . 
3. Проверить код состояния. 
4. Проверить длину массива из responce.

#### Ожидаемый результат:
1. Статус 200.

### 5. Обновление сущности
#### Шаги:
1. Создать сущность. 
2. Полю "title" сущности присвоть значение "Новый заголовок"; 
3. Отправить PATCH-запрос на http://localhost:8080/api/patch/{id} , в параметре id указать идентификатор сущности. 
4. Проверить код состояния. 
5. Проверить значение поля "title".

#### Ожидаемый результат:
1. Статус 204.
2. Поля "title" имеет значение "Новый заголовок".


#### Запуск теста:
1. Предварительно запустить сервис test-service
2. Выполнить в текущем проекте:
```
mvn clean verify
```
#### Построение отчета в Allure:
```
cd target
allure generate
allure open
```